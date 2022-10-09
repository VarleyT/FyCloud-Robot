package online.fycloud.bot.core.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.assists.Permissions;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import online.fycloud.bot.core.BotCore;
import online.fycloud.bot.core.annotation.RobotLimit;
import online.fycloud.bot.core.service.BootStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import oshi.hardware.GlobalMemory;

/**
 * @author VarleyT
 */
@Component
public class BootListener {
    @Autowired
    private BootStatusService bootStatusService;

    @OnGroup
    @Filter(value = "开机")
    @RobotLimit(isBoot = false, permission = Permissions.ADMINISTRATOR)
    public void boot(GroupMsg msg, Sender sender) {
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
    @RobotLimit(isBoot = false, permission = Permissions.ADMINISTRATOR)
    public void shutdown(GroupMsg msg, Sender sender) {
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
    @RobotLimit(isBoot = false, permission = Permissions.ADMINISTRATOR)
    public void status(GroupMsg msg, Sender sender) {
        String groupCode = msg.getGroupInfo().getGroupCode();
        String groupName = msg.getGroupInfo().getGroupName();
        Boolean isBoot = BotCore.BOOT_MAP.get(Long.valueOf(groupCode));
        String bootStatus;
        if (isBoot) {
            bootStatus = "已启动";
        } else {
            bootStatus = "已关闭";
        }
        GlobalMemory memory = OshiUtil.getMemory();
        CpuInfo cpu = OshiUtil.getCpuInfo();
        long available = memory.getAvailable();
        long total = memory.getTotal();
        String memUsage = NumberUtil.decimalFormat("#.##%", available * 1.0 / total);
        String cpuUsage = cpu.getUsed() + "%";

        StringBuilder sb = new StringBuilder();
        sb.append("【状态】")
                .append("\n当前群：" + groupName + "(" + groupCode + ")")
                .append("\nBOT状态：" + bootStatus)
                .append("\n内存使用率：" + memUsage)
                .append("\nCPU使用率：" + cpuUsage)
                .append("\n运行时间：" + BotCore.TIMER.intervalPretty("RunTime"))
                .append("\n当前时间：" + DateUtil.now());
        sender.sendGroupMsg(msg, sb.toString());
    }

}
