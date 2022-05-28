package fycloud.robot.util;

import love.forte.simbot.api.message.events.GroupMsg;

/**
 * @author VarleyT
 * @date 2022/5/28 19:15
 */
public class LogUtil {
    public static String getLog(GroupMsg msg, String name) {
        return msg.getAccountInfo().getAccountNickname() + "(" + msg.getAccountInfo().getAccountCode() + ") 在 "
                + msg.getGroupInfo().getGroupName() + "(" + msg.getGroupInfo().getGroupCode() + ") "
                + " 调用了 <" + name + "> 功能--> " + msg.getMsg();
    }
}
