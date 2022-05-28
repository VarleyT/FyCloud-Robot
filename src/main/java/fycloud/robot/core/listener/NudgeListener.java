package fycloud.robot.core.listener;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import catcode.Neko;
import fycloud.robot.FyRobotApp;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.filter.FilterTargets;
import love.forte.simbot.filter.MatchType;

import java.util.Random;

/**
 * @author VarleyT
 * @date 2022/5/27 22:33
 */
@Beans
@Slf4j
public class NudgeListener {
    private static String[] res = {
            "classpath:emoji/1.jpg", "classpath:emoji/2.jpg", "classpath:emoji/3.jpg", "classpath:emoji/4.jpg",
            "classpath:emoji/5.jpg", "classpath:emoji/6.jpg", "classpath:emoji/7.jpg", "classpath:emoji/8.jpg",
            "classpath:emoji/1.gif",
            "干哈？", "哼(ˉ(∞)ˉ)唧", "(☞0 ☜)眼睛啊啊啊", "[╯•́•̀╰]让我静静", "不＞(￣ε￣ = ￣3￣)<要", "( •̀⊿•́)ง妖妖灵吗？我要报警了！"
    };

    @OnGroup
    @Filter(target = FilterTargets.MSG, value = "(\\[)CAT:nudge.+", matchType = MatchType.REGEX_MATCHES)
    public void listener(GroupMsg msg, Sender sender) {
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String targetCode = util.getParam(msg.getMsg(), "target");
        String botCode = FyRobotApp.ROBOT_CORE.defaultBot.getBotInfo().getBotCode();
        if (!targetCode.equals(botCode)) {
            return;
        }

        log.info(msg.getAccountInfo().getAccountNickname() + "(" + msg.getAccountInfo().getAccountCode() + ") 在 " + msg.getGroupInfo().getGroupName() + "(" + msg.getGroupInfo().getGroupCode() + ") " + " 调用了 <戳一戳> 功能--> ");
        int randomNum = new Random().nextInt(res.length);
        String resPath = res[randomNum];
        final CodeBuilder<Neko> nekoBuilder = CatCodeUtil.getInstance().getNekoBuilder("image", false);
        if (resPath.matches("classpath:emoji/.+")) {
            Neko context = nekoBuilder
                    .key("file").value(resPath)
                    .build();
            sender.sendGroupMsg(msg, context.toString());
        } else {
            sender.sendGroupMsg(msg, resPath);
        }
    }
}
