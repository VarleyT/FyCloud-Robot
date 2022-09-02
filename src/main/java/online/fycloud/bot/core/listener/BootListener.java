package online.fycloud.bot.core.listener;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import online.fycloud.bot.core.BotCore;
import online.fycloud.bot.core.service.BootStatusService;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VarleyT
 */
@Component
@RequiredArgsConstructor
public class BootListener {
    private final BootStatusService bootStatusService;

    @OnGroup
    @Filter(value = "开机")
    public void boot(GroupMsg msg, Sender sender) {
        if (!checkPermission(msg)) {
            return;
        }
        String groupName = msg.getGroupInfo().getGroupName();
        String senderName = msg.getAccountInfo().getAccountNickname();
        String senderCode = msg.getAccountInfo().getAccountCode();
        Long groupCode = Long.valueOf(msg.getGroupInfo().getGroupCode());
        Boolean status = BotCore.BOOT_MAP.get(groupCode);
        if (status) {
            sender.sendGroupMsg(msg, "当前群已处于开机状态！");
        } else {
            BotCore.BOOT_MAP.replace(groupCode, true);
            bootStatusService.setBootStatus(groupCode, true);
            StringBuilder sb = new StringBuilder();
            sb.append("【状态】\n")
                    .append("当前群：" + groupName + "\n")
                    .append("操作人：" + senderName + "(" + senderCode + ")\n")
                    .append("BOT状态：" + "已开机");
            sender.sendGroupMsg(msg, sb.toString());
        }
    }

    @OnGroup
    @Filter("关机")
    public void shutdown(GroupMsg msg, Sender sender) {
        if (!checkPermission(msg)) {
            return;
        }
        String groupName = msg.getGroupInfo().getGroupName();
        String senderName = msg.getAccountInfo().getAccountNickname();
        String senderCode = msg.getAccountInfo().getAccountCode();
        Long groupCode = Long.valueOf(msg.getGroupInfo().getGroupCode());
        Boolean status = BotCore.BOOT_MAP.get(groupCode);
        if (!status) {
            sender.sendGroupMsg(msg, "当前群已处于关机状态！");
        } else {
            BotCore.BOOT_MAP.replace(groupCode, false);
            bootStatusService.setBootStatus(groupCode, false);
            StringBuilder sb = new StringBuilder();
            sb.append("【状态】\n")
                    .append("当前群：" + groupName + "\n")
                    .append("操作人：" + senderName + "(" + senderCode + ")\n")
                    .append("BOT状态：" + "已关机");
            sender.sendGroupMsg(msg, sb.toString());
        }
    }

    @OnGroup
    @Filter("状态")
    public void status(GroupMsg msg, Sender sender) {
        String groupCode = msg.getGroupInfo().getGroupCode();
        Boolean isBoot = BotCore.BOOT_MAP.get(Long.valueOf(groupCode));
        String bootStatus;
        if (isBoot) {
            bootStatus = "已启动";
        } else {
            bootStatus = "已关闭";
        }
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("【状态】")
                .append("\nBOT状态：" + bootStatus)
                .append("\n内存使用率：" + getMemUsage() + "%")
                .append("\n运行时间：" + getRunningTime())
                .append("\n当前时间：" + nowTime);
        sender.sendGroupMsg(groupCode, sb.toString());
    }

    private boolean checkPermission(GroupMsg msg) {
        boolean ownerOrAdmin = msg.getPermission().isOwnerOrAdmin();
        Long numberCode = msg.getAccountInfo().getAccountCodeNumber();
        boolean master = numberCode.equals(BotCore.ADMINISTRATOR);
        if (ownerOrAdmin || master) {
            return true;
        }
        return false;
    }

    private double getMemUsage() {
        Map<String, Object> map = new HashMap<>();
        InputStreamReader inputs = null;
        BufferedReader buffer = null;
        try {
            inputs = new InputStreamReader(new FileInputStream("/proc/meminfo"));
            buffer = new BufferedReader(inputs);
            String line;
            while (true) {
                line = buffer.readLine();
                if (line == null) {
                    break;
                }
                int beginIndex = 0;
                int endIndex = line.indexOf(":");
                if (endIndex != -1) {
                    String key = line.substring(beginIndex, endIndex);
                    beginIndex = endIndex + 1;
                    endIndex = line.length();
                    String memory = line.substring(beginIndex, endIndex);
                    String value = memory.replace("kB", "").trim();
                    map.put(key, value);
                }
            }
            long memTotal = Long.parseLong(map.get("MemTotal").toString());
            long memFree = Long.parseLong(map.get("MemFree").toString());
            long memused = memTotal - memFree;
            long buffers = Long.parseLong(map.get("Buffers").toString());
            long cached = Long.parseLong(map.get("Cached").toString());
            double usage = (double) (memused - buffers - cached) / memTotal * 100;
            BigDecimal b1 = new BigDecimal(usage);
            double memoryUsage = b1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            return memoryUsage;
        } catch (Exception e) {

        } finally {
            try {
                buffer.close();
                inputs.close();
            } catch (Exception e2) {

            }
        }
        return 0.0;
    }

    private String getRunningTime() {
        DateTime NOW_TIME = DateTime.now();
        DateTime START_TIME = BotCore.startDateTime;
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtil.between(START_TIME, NOW_TIME, DateUnit.DAY))
                .append("天")
                .append(DateUtil.between(START_TIME, NOW_TIME, DateUnit.HOUR) % 24)
                .append("时")
                .append(DateUtil.between(START_TIME, NOW_TIME, DateUnit.MINUTE) % 60)
                .append("分")
                .append(DateUtil.between(START_TIME, NOW_TIME, DateUnit.SECOND) % 60)
                .append("秒");
        return sb.toString();
    }
}
