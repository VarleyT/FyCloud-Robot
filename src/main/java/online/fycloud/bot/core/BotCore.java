package online.fycloud.bot.core;

import cn.hutool.core.date.TimeInterval;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.results.GroupList;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import online.fycloud.bot.core.entity.BootStatusInfo;
import online.fycloud.bot.core.service.BootStatusService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author VarleyT
 *
 */
@Component
@Slf4j
public class BotCore {
    /**
     * 定时任务池
     */
    public static ScheduledExecutorService SCHEDULED_POOL;
    /**
     * 群开关缓存
     */
    public static Map<Long, Boolean> BOOT_MAP = new HashMap<>();
    /**
     * 全局Bot
     */
    public static Bot bot;
    /**
     * 全局BotSender
     */
    public static BotSender sender;
    /**
     * 全局计时器
     */
    public static final TimeInterval TIMER = new TimeInterval();
    /**
     * 上下文
     */
    @Getter
    private final ApplicationContext applicationContext;
    private final BootStatusService bootStatusService;


    static {
        SCHEDULED_POOL = new ScheduledThreadPoolExecutor(10);
        TIMER.start("RunTime");
    }

    public BotCore(ApplicationContext applicationContext, BootStatusService bootStatusService) {
        this.applicationContext = applicationContext;
        this.bootStatusService = bootStatusService;
    }

    @PostConstruct
    public void init() {
        bot = applicationContext.getBean(BotManager.class).getDefaultBot();
        sender = bot.getSender();
        initBootMap();
        log.info("BOT初始化完成！");
    }

    private void initBootMap() {
        ArrayList<Long> groupCodeList = new ArrayList<>();
        ArrayList<Long> DBGroupCodeList = new ArrayList<>();
        GroupList groupList = sender.GETTER.getGroupList();
        groupList.forEach(group -> groupCodeList.add(Long.valueOf(group.getGroupCode())));
        bootStatusService.list().forEach(group -> DBGroupCodeList.add(group.getGroupCode()));

        ArrayList<Long> tempList = (ArrayList<Long>) DBGroupCodeList.clone();
        tempList.retainAll(groupCodeList);
        if (tempList.size() < groupCodeList.size()) {
            groupCodeList.removeAll(tempList);
            groupCodeList.forEach(groupCode -> bootStatusService.save(new BootStatusInfo(null, groupCode, true)));
        } else if (tempList.size() > groupCodeList.size()) {
            tempList.removeAll(groupCodeList);
            tempList.forEach(groupCode -> bootStatusService.remove(new QueryWrapper<BootStatusInfo>().eq("group_code", groupCode)));
        }
        bootStatusService.list().forEach(groupInfo -> BotCore.BOOT_MAP.put(groupInfo.getGroupCode(), groupInfo.isStatus()));
    }
}
