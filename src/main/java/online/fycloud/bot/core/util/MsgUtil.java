package online.fycloud.bot.core.util;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import online.fycloud.bot.core.BotCore;

import java.util.concurrent.TimeUnit;

/**
 * @author VarleyT
 * @date 2022/10/9
 */
public class MsgUtil {
    private static Sender sender;
    private static ExpiringMap<String, String> map;

    static {
        sender = BotCore.bot.getSender().SENDER;
        map = ExpiringMap.builder().variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();
    }

    public static void sendMsg(GroupMsg msg, String text) {
        sender.sendGroupMsg(msg, text);
    }

    /**
     * 发送消息（时间限制）
     */
    public static void sendMsgByTime(GroupMsg msg, String text, int duration, TimeUnit timeUnit) {
        final String groupCode = msg.getGroupInfo().getGroupCode();
        final String KEY = groupCode + text;
        if (map.containsKey(KEY)) {
            return;
        }
        map.put(KEY, "", duration, timeUnit);
        sender.sendGroupMsg(msg, text);
    }

    public static void sendMsgByTime(GroupMsg msg, String text) {
        sendMsgByTime(msg, text, 20, TimeUnit.SECONDS);
    }
}
