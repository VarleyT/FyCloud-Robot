package online.fycloud.bot.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author VarleyT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("group_message")
public class GroupMsgInfo implements Serializable {
    /**
     * 序号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 群号
     */
    private Long groupCode;

    /**
     * 群名
     */
    private String groupName;

    /**
     * 发送人号码
     */
    private Long senderCode;

    /**
     * 发送人名字
     */
    private String senderName;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 消息正文
     */
    private String msgContent;

    private static final long serialVersionUID = 1L;
}
