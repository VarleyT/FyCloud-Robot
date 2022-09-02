package online.fycloud.bot.entertainment.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import online.fycloud.bot.core.util.BotHttpUtil;
import online.fycloud.bot.entertainment.entity.GenShinPrayInfo;
import online.fycloud.bot.entertainment.entity.GenShinPrayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author VarleyT
 */
@Component
public class GenShinPrayUtil {

    @Value("${API.GenShinPray.AUTHORIZATION}")
    private String GenShinPrayAuthorization;

    @Value("${API.GenShinPray.PRAY_API}")
    private String GenShinPrayApi;

    @Autowired
    private MessageContentBuilderFactory factory;

    public MessageContent pray(AccountInfo accountInfo, String prayPool, boolean isOne) {
        String url = GenShinPrayApi;

        if ("角色".equals(prayPool)) {
            url += GenShinPrayType.ROLE.getValue();
        } else if ("武器".equals(prayPool)) {
            url += GenShinPrayType.ARM.getValue();
        } else if ("常驻".equals(prayPool)) {
            url += GenShinPrayType.PERM.getValue();
        }
        if (isOne) {
            url += GenShinPrayType.ONE.getValue();
        } else {
            url += GenShinPrayType.TEN.getValue();
        }
        HashMap<String, String> paramMap = new HashMap<String, String>(2) {{
            put("memberCode", accountInfo.getAccountCode());
            put("memberName", accountInfo.getAccountNickname());
        }};
        HashMap<String, String> headerMap = new HashMap<String, String>(1) {{
            put("authorzation", GenShinPrayAuthorization);
        }};
        final JSONObject json = BotHttpUtil.doGet(url, paramMap, headerMap);
        final JSONObject data = json.getJSONObject("data");
        final List<GenShinPrayInfo.GoodsInfo> star4GoodsList = toGenShinGoodsList(data.getJSONArray("star4Goods"));
        final List<GenShinPrayInfo.GoodsInfo> star5GoodsList = toGenShinGoodsList(data.getJSONArray("star5Goods"));
        final List<GenShinPrayInfo.GoodsInfo> star5UpList = toGenShinGoodsList(data.getJSONArray("star5Up"));
        final List<String> star5UpNameList = star5UpList.stream()
                .map(GenShinPrayInfo.GoodsInfo::getGoodsName)
                .collect(Collectors.toList());
        final GenShinPrayInfo genShinPrayInfo = GenShinPrayInfo.builder()
                .prayCount(data.getInteger("prayCount"))
                .star5Cost(data.getInteger("star5Cost"))
                .imgHttpUrl(data.getString("imgHttpUrl"))
                .star4Goods(star4GoodsList)
                .star5Goods(star5GoodsList)
                .star5Up(star5UpList)
                .build();
        String star5Names = String.join("&", star5UpNameList);
        MessageContentBuilder builder = factory.getMessageContentBuilder();
        MessageContent msgContent;
        msgContent = builder.at(accountInfo.getAccountCode())
                .text("当前卡池为：" + star5Names)
                .text("\n本次消耗材料：*" + genShinPrayInfo.getPrayCount())
                .image(genShinPrayInfo.getImgHttpUrl())
                .build();
        if (genShinPrayInfo.getStar5Cost() != 0) {
            msgContent = builder.text("\n恭喜你，获得了5星~真是羡煞旁人！").build();
        } else {
            msgContent = builder.text("\n很遗憾，没有抽中5星，小伙子，快去洗洗手吧！").build();
        }
        return msgContent;
    }

    private List<GenShinPrayInfo.GoodsInfo> toGenShinGoodsList(JSONArray jsonArray) {
        List<GenShinPrayInfo.GoodsInfo> list = jsonArray.stream().map(item -> {
            JSONObject obj = (JSONObject) item;
            return GenShinPrayInfo.GoodsInfo.builder()
                    .goodsName(obj.getString("goodsName"))
                    .goodsType(obj.getString("goodsType"))
                    .goodsSubType(obj.getString("goodsSubType"))
                    .rareType(obj.getString("rareType"))
                    .build();
        }).collect(Collectors.toList());
        return list;
    }

}
