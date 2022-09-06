package online.fycloud.bot.core.interceptor;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.core.intercept.FixedRangeGroupedListenerInterceptor;
import love.forte.simbot.intercept.InterceptionType;
import love.forte.simbot.listener.ListenerInterceptContext;
import online.fycloud.bot.core.BotCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author VarleyT
 */
@Component
public class TimeLimitedInterceptor extends FixedRangeGroupedListenerInterceptor {
    /**
     * 阻塞阈值
     * 发送一次消息的毫秒数
     */
    private static final long BLOCKING_VALUE = 1000L;

    @NotNull
    @Override
    protected String[] getGroupRange() {
        return new String[]{"TimeLimited"};
    }

    @NotNull
    @Override
    protected InterceptionType doIntercept(@NotNull ListenerInterceptContext context, @Nullable String group) {
        MsgGet msgGet = context.getMsgGet();
        if (msgGet instanceof GroupMsg) {
            String groupCode = ((GroupMsg) msgGet).getGroupInfo().getGroupCode();
            String functionName = context.getListenerFunction().getName();
            String timerId = groupCode + "." + functionName;
            long ms = BotCore.TIMER.intervalMs(timerId);
            if (ms > BLOCKING_VALUE) {
                BotCore.TIMER.intervalRestart(timerId);
                return InterceptionType.PASS;
            } else {
                BotCore.TIMER.intervalRestart(timerId);
                return InterceptionType.INTERCEPT;
            }
        }
        return InterceptionType.PASS;
    }
}
