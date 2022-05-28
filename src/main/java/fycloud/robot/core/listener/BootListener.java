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
public class BootListener {
    @OnGroup
    @Filter(value = "开机")
    public void boot(GroupMsg msg, Sender sender) {
        if (checkPermission(msg)) {
            if (FyRobotApp.ROBOT_CORE.isBoot) {
                sender.sendGroupMsg(msg, "BOT已经启动");
            } else {
                FyRobotApp.ROBOT_CORE.isBoot = true;
                sender.sendGroupMsg(msg, "BOT启动成功");
            }
        } else {
            sender.sendGroupMsg(msg, "权限不足");
        }
    }
    @OnGroup
    @Filter(value = "关机")
    public void shutdown(GroupMsg msg, Sender sender) {
        if (checkPermission(msg)) {
            if (FyRobotApp.ROBOT_CORE.isBoot) {
                FyRobotApp.ROBOT_CORE.isBoot = false;
                sender.sendGroupMsg(msg, "BOT关机成功");
            } else {
                sender.sendGroupMsg(msg, "BOT已经关闭");
            }
        } else {
            sender.sendGroupMsg(msg, "权限不足");
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
