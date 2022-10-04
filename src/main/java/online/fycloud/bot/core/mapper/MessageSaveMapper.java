package online.fycloud.bot.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import online.fycloud.bot.core.entity.GroupMsgInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author VarleyT
 */
@Mapper
public interface MessageSaveMapper extends BaseMapper<GroupMsgInfo> {
    /**
     * 查询时间段内消息条数
     *
     * @param year
     * @param month
     * @return
     */
    int countByTime(@Param("year") int year, @Param("month") int month, @Param("groupCode") long groupCode);

    /**
     * 查询时间段内发送消息最频繁的用户
     *
     * @param year
     * @param month
     * @return
     */
    long frequentSender(@Param("year") int year, @Param("month") int month, @Param("groupCode") long groupCode);

    /**
     * 查询时间段内发送消息最频繁的消息条数
     *
     * @param year
     * @param month
     * @return
     */
    int frequentSenderCount(@Param("year") int year, @Param("month") int month, @Param("groupCode") long groupCode);
}
