package online.fycloud.bot.entertainment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author VarleyT
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NetEaseMusicInfo {
    /**
     * 歌曲id
     */
    private String id;
    /**
     * 歌曲名字
     */
    private String name;
    /**
     * 作者
     */
    private String author;
    /**
     * 封面url
     */
    private String imgUrl;
    /**
     * 歌曲url
     */
    private String mp3Url;
    /**
     * 跳转url
     */
    private String jumpUrl;
}
