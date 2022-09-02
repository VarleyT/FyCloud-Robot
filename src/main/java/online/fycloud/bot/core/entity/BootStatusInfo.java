package online.fycloud.bot.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author VarleyT
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("group_status")
public class BootStatusInfo implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 群号
     */
    private long groupCode;
    /**
     * 状态
     */
    private boolean status;

    private static final long serialVersionUID = 1L;
}
