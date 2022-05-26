package fycloud.robot;

import fycloud.robot.core.RobotCore;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.core.SimbotApp;
import love.forte.simbot.core.SimbotContext;

/**
 * @author 19634
 */
@SimbotApplication
@Slf4j
public class FyRobotApp {
    public static SimbotContext simbotContext;
    public static RobotCore ROBOT_CORE;

    public static void main(String[] args) {
        simbotContext = SimbotApp.run(FyRobotApp.class, args);
        ROBOT_CORE = new RobotCore(simbotContext);
        log.info(" " + "Robot启动成功");
    }
}
