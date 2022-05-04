package fycloud.robot;

import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.core.SimbotApp;
import love.forte.simbot.core.SimbotContext;

/**
 * @author 19634
 */
@SimbotApplication
public class FyRobotApp {
    public static void main(String[] args) {
        final SimbotContext simbotContext = SimbotApp.run(FyRobotApp.class, args);
    }
}
