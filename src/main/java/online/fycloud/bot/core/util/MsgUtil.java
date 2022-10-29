package online.fycloud.bot.core.util;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.api.message.events.PrivateMsg;
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
    public static void sendMsgByTime(GroupMsg msg, String text) {
        sendMsgByTime(msg, text, 20, TimeUnit.SECONDS);
    }

    public static void sendMsgByTime(PrivateMsg msg, String text) {
        sendMsgByTime(msg, text, 20, TimeUnit.SECONDS);
    }

    public static void sendMsgByTime(MessageGet messageGet, String text, int duration, TimeUnit timeUnit) {
        if (messageGet instanceof GroupMsg) {
            GroupMsg msg = (GroupMsg) messageGet;
            final String groupCode = msg.getGroupInfo().getGroupCode();
            final String KEY = groupCode + text;
            if (map.containsKey(KEY)) {
                return;
            }
            map.put(KEY, "", duration, timeUnit);
            sender.sendGroupMsg(msg, text);
        } else if (messageGet instanceof PrivateMsg) {
            PrivateMsg msg = (PrivateMsg) messageGet;
            final String accountCode = msg.getAccountInfo().getAccountCode();
            final String KEY = "PrivateMsg" + accountCode;
            if (map.containsKey(KEY)) {
                return;
            }
            map.put(KEY, "", duration, timeUnit);
            sender.sendPrivateMsg(msg, text);
        }
    }
}
