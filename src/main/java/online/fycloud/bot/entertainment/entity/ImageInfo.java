package online.fycloud.bot.entertainment.entity;

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
@TableName("image_save")
public class ImageInfo implements Serializable {
    /**
     * 序号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 类型
     */
    private String type;
    /**
     * 图片链接
     */
    private String url;

    private static final long serialVersionUID = 1L;
}
