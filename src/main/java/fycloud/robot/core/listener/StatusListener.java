package fycloud.robot.core.listener;

import fycloud.robot.FyRobotApp;
import fycloud.robot.core.RobotCore;
import fycloud.robot.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author VarleyT
 * @date 2022/5/29 22:55
 */
@Beans
@Slf4j
public class StatusListener {
    @OnGroup
    @Filter(value = "状态")
    public void status(GroupMsg msg, Sender sender) {
        String bootStatus;
        if (FyRobotApp.ROBOT_CORE.isBoot) {
            bootStatus = "已启动";
        } else {
            bootStatus = "已关闭";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("【状态】\n")
                .append("BOT状态：" + bootStatus + "\n")
                .append("运行时间：" + TimeUtil.getRuntime() + "\n");
        sender.sendGroupMsg(msg, sb.toString());
    }
}
