package online.fycloud.bot.core.listener;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Getter;
import love.forte.simbot.api.sender.Sender;
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
    public void saveMessage(GroupMsg msg) {
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

    @OnGroup
    @Filter(value = "月报")
    public void msgCount(GroupMsg msg, Sender sender, Getter getter) {
        int count = messageSaveService.countByTime(msg);
        long senderCode = messageSaveService.frequentSender(msg);
        int senderMsgCount = messageSaveService.frequentSenderCount(msg);
        StringBuilder sb = new StringBuilder("【月报】\n");
        if (count > 500) {
            sb.append("上个月群非常热闹!\n");
        } else {
            sb.append("上个月群有点冷清!\n");
        }
        sb.append("共发送了").append(count).append("条消息!\n");
        String senderName = getter.getMemberInfo(msg.getGroupInfo().getGroupCodeNumber(), senderCode)
                .getAccountNickname();
        sb.append("其中").append(senderName).append("（").append(senderCode).append("）");
        sb.append("这个吊毛共发送了").append(senderMsgCount).append("条消息，");
        sb.append("真是惊叹啊！");
        sender.sendGroupMsg(msg, sb.toString());
    }
}
