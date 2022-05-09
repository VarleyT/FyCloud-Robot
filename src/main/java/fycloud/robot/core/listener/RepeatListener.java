package fycloud.robot.core.listener;

import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.listener.ListenerContext;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/4/22 19:51
 */

//@Beans
@OnGroup
@OnPrivate
public class RepeatListener {
    public void Listen(GroupMsg m, Sender sender, ListenerContext context){

    }
}
