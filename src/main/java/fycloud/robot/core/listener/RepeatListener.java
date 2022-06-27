package fycloud.robot.core.listener;

import fycloud.robot.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;

/**
 * @author VarleyT
 * @date 2022/6/27 10:56
 */
@Beans
@Slf4j
public class RepeatListener {
    @OnGroup
    @Filter(value = "复读", matchType = MatchType.STARTS_WITH)
    public void repeat(GroupMsg msg, Sender sender) {
        log.info(LogUtil.getLog(msg, "复读"));

        String msgContent = msg.getMsg();
        msgContent.replaceAll(".*复读", "");
        sender.sendGroupMsg(msg, msgContent);
    }
}
