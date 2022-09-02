package online.fycloud.bot.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import online.fycloud.bot.core.entity.GroupMsgInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author VarleyT
 */
@Mapper
public interface MessageSaveMapper extends BaseMapper<GroupMsgInfo> {
}
