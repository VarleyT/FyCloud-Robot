package fycloud.robot.core.scheduled;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import catcode.Neko;
import fycloud.robot.core.listener.MusicListener;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.sender.Sender;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

/**
 * @author VarleyT
 * @date 2022/5/26 18:45
 */
@Slf4j
public class WeekScheduled {
    public void task(Sender sender) {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        if (day == 0) {
            day = 7;
        }
        String filePath = "classpath:" + "other/week/" + day + ".jpeg";
        final CodeBuilder<Neko> nekoBuilder = CatCodeUtil.getInstance().getNekoBuilder("image", false);
        Neko imgCard = nekoBuilder
                .key("file").value(filePath)
                .build();

        try {
            ScheduledManager.sendMsg(imgCard.toString());
            log.info("每日摸鱼发送完成");
        } catch (Exception e) {
            log.error("每日摸鱼发送异常！\n" + "Cased By: " + e.getMessage());
        }

    }
}
