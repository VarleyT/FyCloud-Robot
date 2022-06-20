package fycloud.robot.core.scheduled;

import catcode.CatCodeUtil;
import catcode.CodeTemplate;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.sender.Sender;

/**
 * @author VarleyT
 * @date 2022/6/20 19:24
 */
@Slf4j
public class RandomScheduled {
    public static final String localResourcePath = "classpath:other/emoji/";

    public void task(Sender sender) {
        CodeTemplate<String> stringTemplate = CatCodeUtil.getInstance().getStringTemplate();
        String image = stringTemplate.image(localResourcePath + "lookgroup.jpg");

        try {
            ScheduledManager.sendMsg(image);
            log.info("随机事件发送完成");
        } catch (Exception e) {
            log.error("随机事件发送失败！\n" + "Cased By: " + e.getMessage());
        }

    }
}
