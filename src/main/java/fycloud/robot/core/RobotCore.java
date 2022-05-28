package fycloud.robot.core;

import fycloud.robot.FyRobotApp;
import fycloud.robot.core.scheduled.ScheduledManager;
import fycloud.robot.core.scheduled.WeekScheduled;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.core.SimbotContext;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/5/5 12:50
 */
@Slf4j
public class RobotCore {
    public static ExecutorService THREAD_POOL;
    public static ScheduledExecutorService SCHEDULED_POOL;
    public Bot defaultBot;
    public Sender sender;
    public boolean isBoot = true;
    public final long[] adminCode = {
            1963460510
    };
    static {
        THREAD_POOL = Executors.newFixedThreadPool(10);
        SCHEDULED_POOL = Executors.newScheduledThreadPool(10);
        log.info("RobotCore 已加载");
    }

    public RobotCore(SimbotContext simbotContext) {
        defaultBot = simbotContext.getBotManager().getDefaultBot();
        sender = defaultBot.getSender().SENDER;
        new ScheduledManager();
    }
}
