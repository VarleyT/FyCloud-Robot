package fycloud.robot.core.entity.netease;

import lombok.Builder;
import lombok.Value;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/4/22 17:09
 */
@Value
public class NeteaseMusicInfo {
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

    public static class Builder{
        private String id;
        private String name;
        private String author;
        private String imgUrl;
        private String mp3Url;
        private String jumpUrl;

        public Builder(){

        }

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder author(String auther){
            this.author = auther;
            return this;
        }

        public Builder imgUrl(String imgUrl){
            this.imgUrl = imgUrl;
            return this;
        }

        public Builder mp3Url(String mp3Url){
            this.mp3Url = mp3Url;
            return this;
        }

        public Builder jumpUrl(String jumpUrl){
            this.jumpUrl = jumpUrl;
            return this;
        }

        public NeteaseMusicInfo build(){
            return new NeteaseMusicInfo(id,name,author,imgUrl,mp3Url,jumpUrl);
        }
    }
}
