package online.fycloud.bot.core.listener;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import online.fycloud.bot.core.entity.GroupMsgInfo;
import online.fycloud.bot.core.service.MessageSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author VarleyT
 */
@Component
@Slf4j
public class MessageSaveListener {
    @Autowired
    private MessageSaveService messageSaveService;

    @OnGroup
    public void saveMessage(GroupMsg msg){
        GroupInfo groupInfo = msg.getGroupInfo();
        GroupAccountInfo senderInfo = msg.getAccountInfo();
        long groupCode = Long.parseLong(groupInfo.getGroupCode());
        String groupName = groupInfo.getGroupName();
        long senderCode = Long.parseLong(senderInfo.getAccountCode());
        String senderName = senderInfo.getAccountNickname();
        Date sendTime = new Date(msg.getTime());
        String content = msg.getMsg();
        GroupMsgInfo msgInfo = new GroupMsgInfo(null, groupCode, groupName, senderCode, senderName, sendTime, content);
        messageSaveService.save(msgInfo);
    }
}
