package fycloud.robot.core.listener;

import fycloud.robot.FyRobotApp;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

/**
 * @author VarleyT
 * @date 2022/5/28 15:41
 */
@Beans
@Slf4j
public class BootListener{
    @OnGroup
    @Filter(value = "开机")
    public void boot(GroupMsg msg, Sender sender) {
        if (checkPermission(msg)) {
            StringBuilder sb = new StringBuilder();
            sb.append("【状态】\n")
                    .append("当前群：" + msg.getGroupInfo().getGroupName() + "\n")
                    .append("操作人：" + msg.getAccountInfo().getAccountNickname() + "(" + msg.getAccountInfo().getAccountCode() + ")\n")
                    .append("BOT状态：");
            if (FyRobotApp.ROBOT_CORE.isBoot) {
                sb.append("已处于开机状态");
            } else {
                FyRobotApp.ROBOT_CORE.isBoot = true;
                sb.append("已开机");
            }
            sender.sendGroupMsg(msg, sb.toString());
        } else {
            sender.sendGroupMsg(msg, "权限不足！");
        }
    }
    @OnGroup
    @Filter(value = "关机")
    public void shutdown(GroupMsg msg, Sender sender) {
        if (checkPermission(msg)) {
            StringBuilder sb = new StringBuilder();
            sb.append("【状态】\n")
                    .append("当前群：" + msg.getGroupInfo().getGroupName() + "\n")
                    .append("操作人：" + msg.getAccountInfo().getAccountNickname() + "(" + msg.getAccountInfo().getAccountCode() + ")\n")
                    .append("BOT状态：");
            if (FyRobotApp.ROBOT_CORE.isBoot) {
                FyRobotApp.ROBOT_CORE.isBoot = false;
                sb.append("已关机");
            } else {
                sb.append("已处于关机状态");
            }
            sender.sendGroupMsg(msg, sb.toString());
        } else {
            sender.sendGroupMsg(msg, "权限不足！");
        }
    }

    public static boolean checkPermission(GroupMsg msg) {
        String accountCode = msg.getAccountInfo().getAccountCode();
        for (long adminCode : FyRobotApp.ROBOT_CORE.adminCode) {
            if (accountCode.equals(String.valueOf(adminCode))) {
                return true;
            }
        }
        if (msg.getPermission().isOwnerOrAdmin()) {
            return true;
        }
        return false;
    }

}
