package fycloud.robot.core;

import cn.hutool.core.date.DateTime;
import fycloud.robot.FyRobotApp;
import fycloud.robot.core.scheduled.ScheduledManager;
import fycloud.robot.core.scheduled.WeekScheduled;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.core.SimbotContext;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/5/5 12:50
 */
@Slf4j
public class RobotCore {
    /**
     * 线程池
     */
    public static ExecutorService THREAD_POOL;
    /**
     * 定时任务池
     */
    public static ScheduledExecutorService SCHEDULED_POOL;
    /**
     * 启动时间
     */
    public DateTime START_TIME;
    /**
     * 默认BOT
     */
    public Bot defaultBot;
    /**
     * 送信器
     */
    public Sender sender;
    /**
     * 启动状态
     */
    public boolean isBoot = true;
    /**
     * 管理员列表
     */
    public final long[] adminCode = {
            1963460510
    };

    static {
        THREAD_POOL = Executors.newFixedThreadPool(10);
        SCHEDULED_POOL = Executors.newScheduledThreadPool(10);
        log.info("RobotCore 已加载");
    }

    public RobotCore(SimbotContext simbotContext) {
        START_TIME = DateTime.now();
        defaultBot = simbotContext.getBotManager().getDefaultBot();
        sender = defaultBot.getSender().SENDER;
        new ScheduledManager();
    }
}
