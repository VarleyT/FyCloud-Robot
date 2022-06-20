package fycloud.robot.core.listener;

import fycloud.robot.FyRobotApp;
import fycloud.robot.util.TimeUtil;
import io.ktor.utils.io.bits.Memory;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.management.MemoryMXBean;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VarleyT
 * @date 2022/5/29 22:55
 */
@Beans
@Slf4j
public class StatusListener {
    @OnGroup
    @Filter(value = "状态")
    public void status(GroupMsg msg, Sender sender) {
        String bootStatus;
        if (FyRobotApp.ROBOT_CORE.isBoot) {
            bootStatus = "已启动";
        } else {
            bootStatus = "已关闭";
        }
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString();

        StringBuilder sb = new StringBuilder();
        sb.append("【状态】")
                .append("\nBOT状态：" + bootStatus)
                .append("\n内存使用率：" + getMemUsage() + "%")
                .append("\n运行时间：" + TimeUtil.getRunningTime())
                .append("\n当前时间：" + nowTime);
        sender.sendGroupMsg(msg, sb.toString());
    }

    public static double getMemUsage() {
        Map<String, Object> map = new HashMap<String, Object>();
        InputStreamReader inputs = null;
        BufferedReader buffer = null;

        try {
            inputs = new InputStreamReader(new FileInputStream(
                    "/proc/meminfo"));
            buffer = new BufferedReader(inputs);

            String line = "";

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
}
