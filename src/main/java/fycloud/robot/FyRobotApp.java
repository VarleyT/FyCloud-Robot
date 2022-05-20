package fycloud.robot;

import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.core.SimbotApp;
import love.forte.simbot.core.SimbotContext;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * @author 19634
 */
@SimbotApplication
public class FyRobotApp {
    public static final Logger logger = Logger.getLogger(FyRobotApp.class);

    public static void main(String[] args) {
        final SimbotContext simbotContext = SimbotApp.run(FyRobotApp.class, args);
        logger.info(" " + "Robot启动成功");
    }
}
