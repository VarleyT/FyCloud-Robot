package online.fycloud.bot.entertainment.listener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import online.fycloud.bot.core.annotation.RobotLimit;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VarleyT
 */
@Slf4j
@Component
public class RepeatListener {
    private static Map<String, Entity> temp = new HashMap<>();

    @OnGroup
    @RobotLimit
    public void repeat(GroupMsg msg, Sender sender) {
        String groupCode = msg.getGroupInfo().getGroupCode();
        String text = msg.getText();
        if (text.equals("") || text == null) {
            return;
        }
        if (temp.containsKey(groupCode)) {
            Entity entity = temp.get(groupCode);
            if (entity.getTemp().equals(text)) {
                if (!entity.isRepeat()) {
                    entity.setRepeat(true);
                    sender.sendGroupMsg(msg, text);
                }
            } else {
                entity.setRepeat(false);
                entity.setTemp(text);
            }
        } else {
            temp.put(groupCode, new Entity(false, text));
        }
    }

    @Data
    @AllArgsConstructor
    class Entity {
        private boolean isRepeat;
        private String temp;
    }
}
