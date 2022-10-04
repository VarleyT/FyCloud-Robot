package online.fycloud.bot.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.forte.simbot.api.message.events.GroupMsg;
import online.fycloud.bot.core.entity.GroupMsgInfo;

/**
 * @author VarleyT
 */

public interface MessageSaveService extends IService<GroupMsgInfo> {
    /**
     * 查询当前时间段消息条数
     *
     * @return
     */
    int countByTime(GroupMsg msg);

    /**
     * 查询时间段内发送消息最频繁的用户
     *
     * @return
     */
    long frequentSender(GroupMsg msg);

    /**
     * 查询时间段内发送消息最频繁的消息条数
     *
     * @return
     */
    int frequentSenderCount(GroupMsg msg);
}
