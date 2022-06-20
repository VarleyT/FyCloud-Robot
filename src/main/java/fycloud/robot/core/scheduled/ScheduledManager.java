package fycloud.robot.core.scheduled;

import fycloud.robot.FyRobotApp;
import fycloud.robot.core.RobotCore;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author VarleyT
 * @date 2022/5/26 19:55
 */
@Slf4j
public class ScheduledManager {
    public static long[] ScheduledGroups = {
            489764903,
            1043409458
    };

    public ScheduledManager() {
        RobotCore.SCHEDULED_POOL.scheduleAtFixedRate(() -> {
            new WeekScheduled().task(FyRobotApp.ROBOT_CORE.sender);
        }, (24 + 8 - new Date().getHours()) % 24, 24, TimeUnit.HOURS);

        RobotCore.SCHEDULED_POOL.scheduleAtFixedRate(() -> {
            if (new Date().getHours() < 8 || new Date().getHours() > 23) {
                if (new Random().nextDouble() > 0.9) {
                    new RandomScheduled().task(FyRobotApp.ROBOT_CORE.sender);
                }
            }
        }, 0, 1, TimeUnit.HOURS);
        log.info("定时任务加载成功");
    }

    public static void sendMsg(String msg) throws RuntimeException {
        for (long scheduledGroup : ScheduledGroups) {
            FyRobotApp.ROBOT_CORE.sender.sendGroupMsg(scheduledGroup, msg);

        }
    }
}
