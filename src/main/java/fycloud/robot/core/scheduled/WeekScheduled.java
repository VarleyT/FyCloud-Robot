package fycloud.robot.core.scheduled;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import catcode.Neko;
import fycloud.robot.FyRobotApp;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.api.sender.Sender;

import java.net.URL;
import java.util.Calendar;

/**
 * @author VarleyT
 * @date 2022/5/26 18:45
 */
@Slf4j
public class WeekScheduled {
    public static void task(Sender sender) {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        if (day == 0) {
            day = 7;
        }
        String fileName = day + ".jpeg";
        URL file = WeekScheduled.class.getClassLoader().getResource("WeekTimer/" + fileName);

        final CodeBuilder<Neko> nekoBuilder = CatCodeUtil.getInstance().getNekoBuilder("image", false);
        Neko imgCard = nekoBuilder
                .key("file").value(file)
                .build();
        for (long groupCode : ScheduledManager.ScheduledGroups) {
            try {
                sender.sendGroupMsg(groupCode,imgCard.toString());
            }catch (Exception e){
                log.error("定时任务发送异常！");
            }
        }
    }
}
