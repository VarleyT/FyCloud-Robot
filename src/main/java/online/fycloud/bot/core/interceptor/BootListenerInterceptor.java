package online.fycloud.bot.core.interceptor;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.core.intercept.FixedRangeGroupedListenerInterceptor;
import love.forte.simbot.intercept.InterceptionType;
import love.forte.simbot.listener.ListenerInterceptContext;
import online.fycloud.bot.core.BotCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VarleyT
 *
 */
@Beans
public class BootListenerInterceptor extends FixedRangeGroupedListenerInterceptor {

    @NotNull
    @Override
    protected String[] getGroupRange() {
        return new String[]{"GroupListenerFunction"};
    }

    @NotNull
    @Override
    protected InterceptionType doIntercept(@NotNull ListenerInterceptContext context, @Nullable String group) {
        MsgGet msgGet = context.getMsgGet();
        if (msgGet instanceof GroupMsg){
            Long groupCode = Long.valueOf(((GroupMsg) msgGet).getGroupInfo().getGroupCode());
            Boolean isBoot = BotCore.BOOT_MAP.get(groupCode);
            if (!isBoot){
                return InterceptionType.INTERCEPT;
            }
        }
        return InterceptionType.PASS;
    }
}
