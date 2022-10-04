package online.fycloud.bot.core.service.Impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.forte.simbot.api.message.events.GroupMsg;
import online.fycloud.bot.core.entity.GroupMsgInfo;
import online.fycloud.bot.core.mapper.MessageSaveMapper;
import online.fycloud.bot.core.service.MessageSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author VarleyT
 */
@Service
public class MessageSaveServiceImpl extends ServiceImpl<MessageSaveMapper, GroupMsgInfo> implements MessageSaveService {
    @Autowired
    private MessageSaveMapper messageSaveMapper;

    public int countByTime(GroupMsg msg) {
        int year = getYear();
        int month = getMonth() - 1;
        if (month == 0) {
            year -= 1;
            month = 12;
        }
        long groupCode = msg.getGroupInfo().getGroupCodeNumber();
        return messageSaveMapper.countByTime(year, month, groupCode);
    }

    @Override
    public long frequentSender(GroupMsg msg) {
        int year = getYear();
        int month = getMonth() - 1;
        if (month == 0) {
            year -= 1;
            month = 12;
        }
        long groupCode = msg.getGroupInfo().getGroupCodeNumber();
        return messageSaveMapper.frequentSender(year, month, groupCode);
    }

    @Override
    public int frequentSenderCount(GroupMsg msg) {
        int year = getYear();
        int month = getMonth() - 1;
        if (month == 0) {
            year -= 1;
            month = 12;
        }
        long groupCode = msg.getGroupInfo().getGroupCodeNumber();
        return messageSaveMapper.frequentSenderCount(year, month, groupCode);
    }

    private int getYear() {
        return DateUtil.date().year();
    }

    private int getMonth() {
        return DateUtil.date().month() + 1;
    }
}
