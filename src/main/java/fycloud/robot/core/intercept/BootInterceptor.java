package fycloud.robot.core.intercept;

import fycloud.robot.FyRobotApp;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.intercept.InterceptionType;
import love.forte.simbot.listener.ListenerInterceptContext;
import love.forte.simbot.listener.ListenerInterceptor;
import org.jetbrains.annotations.NotNull;

/**
 * @author VarleyT
 * @date 2022/5/28 15:37
 */
@Beans
public class BootInterceptor implements ListenerInterceptor {
    @NotNull
    @Override
    public InterceptionType intercept(@NotNull ListenerInterceptContext context) {
        if (FyRobotApp.ROBOT_CORE.isBoot){
            return InterceptionType.PASS;
        }else {
            return InterceptionType.INTERCEPT;
        }
    }
}
