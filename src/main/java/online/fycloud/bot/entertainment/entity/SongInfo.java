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
@TableName("song_save")
public class SongInfo implements Serializable {
    /**
     * 序号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 歌曲名称
     */
    private String songName;
    /**
     * 歌手
     */
    private String songSinger;
    /**
     * 演唱
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userImage;
    /**
     * 歌曲Url
     */
    private String songUrl;
    /**
     * 歌词
     */
    private String songLyric;
    /**
     * 跳转链接
     */
    private String url;

    private static final long serialVersionUID = 1L;
}
