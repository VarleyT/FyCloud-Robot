package online.fycloud.bot.entertainment.listener;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import catcode.CodeTemplate;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import online.fycloud.bot.core.BotCore;
import online.fycloud.bot.core.annotation.RobotLimit;
import online.fycloud.bot.core.config.BotApis;
import online.fycloud.bot.core.util.BotHttpUtil;
import online.fycloud.bot.entertainment.entity.ImageInfo;
import online.fycloud.bot.entertainment.logic.ChatUtil;
import online.fycloud.bot.entertainment.logic.GenShinPrayUtil;
import online.fycloud.bot.entertainment.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author VarleyT
 *
 */
@Slf4j
@Component
public class GroupListener {
    @Autowired
    private MessageContentBuilderFactory messageContentBuilderFactory;
    @Autowired
    private BotApis botApis;

    @Autowired
    private ChatUtil chatUtil;
    /**
     * 聊天功能
     *
     */
    @OnGroup
    @RobotLimit
    @Filter(atBot = true, anyAt = true, trim = true)
    public void chat(GroupMsg msg, Sender sender) {
        log.info("[{}] <{}({})> {}({})：{}",
                "聊天",
                msg.getGroupInfo().getGroupName(), msg.getGroupInfo().getGroupCode(),
                msg.getAccountInfo().getAccountNickname(), msg.getAccountInfo().getAccountCode(),
                msg.getMsg());
        List<String> contentList = chatUtil.chat(msg);
        contentList.forEach(text -> sender.sendGroupMsg(msg, text));
    }

    @Autowired
    private GenShinPrayUtil genShinPrayUtil;

    /**
     * 原神模拟抽卡
     *
     */
    @OnGroup
    @RobotLimit
    @Filters(value = {
            @Filter(value = "角色{{param}}", matchType = MatchType.REGEX_MATCHES),
            @Filter(value = "武器{{param}}", matchType = MatchType.REGEX_MATCHES),
            @Filter(value = "常驻{{param}}", matchType = MatchType.REGEX_MATCHES)
    })
    public void pray(GroupMsg msg, Sender sender, @FilterValue("param") String param) {
        log.info("[{}] <{}({})> {}({})：{}",
                "原神模拟抽卡",
                msg.getGroupInfo().getGroupName(), msg.getGroupInfo().getGroupCode(),
                msg.getAccountInfo().getAccountNickname(), msg.getAccountInfo().getAccountCode(),
                msg.getMsg());
        String prayPool = msg.getText().substring(0, 2);
        boolean isOne = true;
        if (param.matches("(单|十).+")) {
            if (param.matches("十.+")) {
                isOne = false;
            }
        } else {
            sender.sendGroupMsg(msg, "命令错误！角色/武器/常驻 [单抽|十连]");
            return;
        }
        MessageContent content = genShinPrayUtil.pray(msg.getAccountInfo(), prayPool, isOne);
        sender.sendGroupMsg(msg, content);
    }

    /**
     * 抖音视频解析功能
     *
     */
    @OnGroup
    @RobotLimit
    @Filters(value = {
            @Filter(value = "解析{{url}}", matchType = MatchType.REGEX_MATCHES),
            @Filter(value = "抖音解析{{url}}", matchType = MatchType.REGEX_MATCHES)
    })
    public void douyin(GroupMsg msg, Sender sender, @FilterValue("url") String originalText) {
        log.info("[{}] <{}({})> {}({})：{}",
                "抖音视频解析",
                msg.getGroupInfo().getGroupName(), msg.getGroupInfo().getGroupCode(),
                msg.getAccountInfo().getAccountNickname(), msg.getAccountInfo().getAccountCode(),
                msg.getMsg());
        String r = "https:\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
        Pattern p1 = Pattern.compile(r);
        Matcher m1 = p1.matcher(originalText);
        if (!m1.find()) {
            return;
        }
        final String regex = "https://v\\.douyin\\.com/\\w{7}/";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(originalText);
        String originalUrl;
        if (m.find()) {
            originalUrl = m.group(0);
        } else {
            sender.sendGroupMsg(msg, "解析失败，请输入正确的链接");
            return;
        }
        if (originalUrl.equals("")) {
            return;
        }
        String request = String.format(botApis.getLinHun_DouYinApi(), originalUrl);
        JSONObject jsonObject = BotHttpUtil.doGet(request);
        if (!jsonObject.getString("code").equals("200")) {
            try {
                String tips = jsonObject.getString("tips");
                sender.sendGroupMsg(msg, tips);
                return;
            } catch (Exception e) {
                log.error("抖音解析失败！！Cased By: \n{}", e.getMessage());
                sender.sendGroupMsg(msg, "解析失败");
                return;
            }
        }
        String url = jsonObject.getString("url");
        if (url.equals("")) {
            return;
        }
        sender.sendGroupMsg(msg, url);
    }

    /**
     * 唱歌功能
     *
     */
    @OnGroup
    @RobotLimit
    @Filter(value = "唱歌|唱首歌|来首歌", matchType = MatchType.REGEX_MATCHES)
    public void sing(GroupMsg msg, Sender sender) {
        log.info("[{}] <{}({})> {}({})：{}",
                "唱歌",
                msg.getGroupInfo().getGroupName(), msg.getGroupInfo().getGroupCode(),
                msg.getAccountInfo().getAccountNickname(), msg.getAccountInfo().getAccountCode(),
                msg.getMsg());
        JSONObject response = BotHttpUtil.doGet(botApis.getXiaoBai_SingApi());
        if (response == null || response.isEmpty()){
            log.error("SingApi返回值response为空！");
            return;
        }
        JSONObject data = response.getJSONObject("data");
        if (data == null || data.isEmpty()){
            log.error("SingApi返回值data为空！");
            return;
        }
        String audioUrl = data.getString("audioSrc");
        final CodeBuilder<String> codeBuilder = CatCodeUtil.getInstance().getStringCodeBuilder("record", false);
        String catCode = codeBuilder
                .key("file").value(audioUrl)
                .build();
        sender.sendGroupMsg(msg, catCode);
    }

    @Autowired
    private ImageService imageService;

    /**
     * 看图功能
     *
     * @param msg
     * @param sender
     */
    @OnGroup
    @RobotLimit
    @Filter(value = "(来点好看的|来点好康的)(\\*\\d+)?.*", matchType = MatchType.REGEX_MATCHES)
    public void image(GroupMsg msg, Sender sender) {
        log.info("[{}] <{}({})> {}({})：{}",
                "看图",
                msg.getGroupInfo().getGroupName(), msg.getGroupInfo().getGroupCode(),
                msg.getAccountInfo().getAccountNickname(), msg.getAccountInfo().getAccountCode(),
                msg.getMsg());
        int size = (int) imageService.count();
        if (size == 0) {
            log.error("数据表为空！");
            return;
        }

        String text = msg.getText();
        Random random = new Random();
        if (text.matches("(来点好看的|来点好康的)\\*\\d+")) {
            String replace = text.replaceAll("(来点好看的|来点好康的)\\*", "");
            if (replace.length() > 2) {
                sender.sendGroupMsg(msg, "参数错误！参数值范围应为(1-10)");
                return;
            }
            int amount = Integer.parseInt(replace);
            if (amount != 1) {
                if (amount >= 2 && amount <= 10) {
                    if (!(messageContentBuilderFactory instanceof MiraiMessageContentBuilderFactory)) {
                        log.error("MiraiMessageContentBuilderFactory初始化失败！");
                        sender.sendGroupMsg(msg, "发送失败！");
                        return;
                    }
                    int[] ids = NumberUtil.generateRandomNumber(1, size, amount);
                    List<String> imageUrlList = new ArrayList<>();
                    for (int id : ids) {
                        ImageInfo info = imageService.getOne(new QueryWrapper<ImageInfo>().eq("id", id));
                        if (info == null || info.getUrl() == null || info.getUrl().equals("")) {
                            continue;
                        }
                        imageUrlList.add(info.getUrl());
                    }
                    if (imageUrlList.isEmpty()) {
                        log.error("imageUrlList为空！");
                        return;
                    }
                    MiraiMessageContentBuilder imgBuilder =
                            ((MiraiMessageContentBuilderFactory) messageContentBuilderFactory).getMessageContentBuilder();
                    List<MessageContent> imageContents = imageUrlList.stream().map(imageUrl -> {
                        imgBuilder.clear();
                        MessageContent content = imgBuilder.imageUrl(imageUrl).build();
                        return content;
                    }).collect(Collectors.toList());
                    BotInfo botInfo = BotCore.bot.getBotInfo();
                    MiraiMessageContentBuilder builder =
                            ((MiraiMessageContentBuilderFactory) messageContentBuilderFactory).getMessageContentBuilder();
                    builder.forwardMessage(forwardBuilder -> {
                        imageContents.forEach(content -> {
                            if (!content.isEmpty()) {
                                forwardBuilder.add(botInfo, content);
                            }
                        });
                    });
                    MiraiMessageContent build = builder.build();
                    sender.sendGroupMsg(msg, build);
                    return;
                } else {
                    sender.sendGroupMsg(msg, "参数错误！参数值范围应为(1-10)");
                    return;
                }
            }
        }
        int id = random.nextInt(size - 1) + 1;
        ImageInfo info = imageService.getOne(new QueryWrapper<ImageInfo>().eq("id", id));
        if (info == null || info.getUrl().equals("")) {
            log.error("ImageInfo查询值为空！");
            return;
        }
        final CodeTemplate<String> template = CatCodeUtil.INSTANCE.getStringTemplate();
        String imgCard = template.image(info.getUrl());
        sender.sendGroupMsg(msg, imgCard);
    }
}
