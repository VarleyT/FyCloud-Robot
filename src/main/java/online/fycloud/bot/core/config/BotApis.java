package online.fycloud.bot.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author VarleyT
 */
@Data
@Component
public class BotApis {
    /**
     * 茉莉云
     */
    @Value("${API.MoLiCloud.CHAT_API}")
    private String MoLi_ChatApi;
    @Value("${API.MoLiCloud.RESOURCES_API}")
    private String MoLi_ResourceApi;
    @Value("${API.MoLiCloud.API_KEY}")
    private String MoLi_ApiKey;
    @Value("${API.MoLiCloud.API_SECRET}")
    private String MoLi_ApiSecret;

    /**
     * 原神模拟抽卡
     */
    @Value("${API.GenShinPray.AUTHORIZATION}")
    private String GenShinPray_Authorization;
    @Value("${API.GenShinPray.PRAY_API}")
    private String GenShinPray_Api;

    /**
     * 网易云
     */
    @Value("${API.NETEASE_API.SEARCH}")
    private String Netease_SearchApi;
    @Value("${API.NETEASE_API.MP3}")
    private String Netease_Mp3Api;

    /**
     * 小白API
     */
    @Value("${API.XiaoBaiAPI.SING}")
    private String XiaoBai_SingApi;

    /**
     * 林魂云
     */
    @Value("${API.LinHunYun.DOU_YIN}")
    private String LinHun_DouYinApi;
}
