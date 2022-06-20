package fycloud.robot.core.listener;

import catcode.CatCodeUtil;
import catcode.CodeTemplate;
import com.alibaba.fastjson.JSONObject;
import fycloud.robot.FyRobotApp;
import fycloud.robot.core.APIs;
import fycloud.robot.util.HttpUtil;
import fycloud.robot.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;

/**
 * @author VarleyT
 * @date 2022/5/20 21:02
 */
@Beans
@Slf4j
public class LookLookListener {
    @Depend
    private MiraiMessageContentBuilderFactory factory;

    @OnGroup
    @Filter(value = "(来点好看|来点好康).*", matchType = MatchType.REGEX_MATCHES)
    public void look(GroupMsg msg, Sender sender) {
        log.info(LogUtil.getLog(msg, "来点好康的"));
        MiraiMessageContentBuilder builder = factory.getMessageContentBuilder();
        CodeTemplate<String> template = CatCodeUtil.INSTANCE.getStringTemplate();

        String imgCard = template.image(getUrl());
        sender.sendGroupMsg(msg, imgCard);
    }

    private String getUrl() {
        JSONObject result = HttpUtil.Get(APIs.DuJiaoShou_API.LookLook);
        return result.getString("text");
    }
}
