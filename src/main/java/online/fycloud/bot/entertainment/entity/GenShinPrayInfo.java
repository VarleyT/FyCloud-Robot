package online.fycloud.bot.entertainment.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author VarleyT
 *
 */
@Builder
@Getter
public class GenShinPrayInfo {
    /**
     * 祈愿次数
     */
    private Integer prayCount;
    /**
     * 本次获取五星物品累计消耗多少抽，如果本次未抽出五星时，值为0
     */
    private Integer star5Cost;
    /**
     * 图片在tomcat中的http地址
     */
    private String imgHttpUrl;
    /**
     * 4星物品列表
     */
    private List<GoodsInfo> star4Goods;
    /**
     * 5星物品列表
     */
    private List<GoodsInfo> star5Goods;
    /**
     * 当前蛋池中的5星UP列表
     */
    private List<GoodsInfo> star5Up;

    @Builder
    @Getter
    public static class GoodsInfo {
        /**
         * 物品名称
         */
        private String goodsName;
        /**
         * 物品类型，武器/角色
         */
        private String goodsType;
        /**
         * 物品子类型
         */
        private String goodsSubType;
        /**
         * 稀有类型
         */
        private String rareType;
    }
}
