package fycloud.robot.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import fycloud.robot.core.APIs;
import fycloud.robot.core.entity.genshin.GenshinPrayResultInfo;
import fycloud.robot.core.entity.genshin.GenshinPrayType;
import fycloud.robot.util.HttpUtil;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/4/28 10:31
 */

public class GenshinPrayService {
    private final Map<String, String> header = new HashMap<>();
    private final Map<String, String> param = new HashMap<>();

    public GenshinPrayService() {
        header.put("authorzation", APIs.GenshinPray.genshinPrayAuthorzation);
    }

    public MessageContent Pray(String type, boolean isOne, GroupMsg msg,MessageContentBuilderFactory factory) {
        String requestUrl = APIs.GenshinPray.genshinPray_API;
        if (type.equals(GenshinPrayType.role)) {
            requestUrl += GenshinPrayType.role;
        } else if (type.equals(GenshinPrayType.arm)) {
            requestUrl += GenshinPrayType.arm;
        } else if (type.equals(GenshinPrayType.perm)) {
            requestUrl += GenshinPrayType.perm;
        } else if (type.equals(GenshinPrayType.fullRole)) {
            requestUrl += GenshinPrayType.fullRole;
        } else if (type.equals(GenshinPrayType.fullArm)) {
            requestUrl += GenshinPrayType.fullArm;
        }
        if (isOne) {
            requestUrl += GenshinPrayType.one;
        } else {
            requestUrl += GenshinPrayType.ten;
        }

        param.put("memberCode", msg.getAccountInfo().getAccountCode());
        param.put("memberName",msg.getAccountInfo().getAccountNickname());
        JSONObject response = HttpUtil.Get(requestUrl,param,header);
        final JSONObject data = response.getJSONObject("data");

        JSONArray star4Arrays = data.getJSONArray("star4Goods");
        final List<GenshinPrayResultInfo.GoodsInfo> star4Goods = star4Arrays.stream().map(item -> {
            JSONObject obj = (JSONObject) item;
            return GenshinPrayResultInfo.GoodsInfo.builder()
                    .goodsName(obj.getString("goodsName"))
                    .goodsType(obj.getString("goodsType"))
                    .goodsSubType(obj.getString("goodsSubType"))
                    .rareType(obj.getString("rareType"))
                    .build();
        }).collect(Collectors.toList());
        JSONArray star5Arrays = data.getJSONArray("star5Goods");
        final List<GenshinPrayResultInfo.GoodsInfo> star5Goods = star5Arrays.stream().map(item -> {
            JSONObject obj = (JSONObject) item;
            return GenshinPrayResultInfo.GoodsInfo.builder()
                    .goodsName(obj.getString("goodsName"))
                    .goodsType(obj.getString("goodsType"))
                    .goodsSubType(obj.getString("goodsSubType"))
                    .rareType(obj.getString("rareType"))
                    .build();
        }).collect(Collectors.toList());
        JSONArray star5UpArrays = data.getJSONArray("star5Up");
        final List<GenshinPrayResultInfo.GoodsInfo> star5Up = star5UpArrays.stream().map(item -> {
            JSONObject obj = (JSONObject) item;
            return GenshinPrayResultInfo.GoodsInfo.builder()
                    .goodsName(obj.getString("goodsName"))
                    .goodsType(obj.getString("goodsType"))
                    .goodsSubType(obj.getString("goodsSubType"))
                    .rareType(obj.getString("rareType"))
                    .build();
        }).collect(Collectors.toList());
        final List<String> star5GoodsList = star5Up.stream().map(item -> {
            return item.getGoodsName();
        }).collect(Collectors.toList());

        GenshinPrayResultInfo genshinPrayResultInfo = GenshinPrayResultInfo.builder()
                .prayCount(data.getInteger("prayCount"))
                .star5Cost(data.getInteger("star5Cost"))
                .imgHttpUrl(data.getString("imgHttpUrl"))
                .star4Goods(star4Goods)
                .star5Goods(star5Goods)
                .star5Up(star5Up)
                .build();
        String star5Names = String.join("&", star5GoodsList);

        MessageContentBuilder builder = factory.getMessageContentBuilder();
        MessageContent msgContent = builder.at(msg.getAccountInfo().getAccountCode()).build();
        msgContent = builder.text("当前卡池为：" + star5Names)
                .text("\n本次消耗材料：*" + genshinPrayResultInfo.getPrayCount())
                .build();
        if (genshinPrayResultInfo.getStar5Cost() != 0) {
            msgContent = builder.text("\n恭喜你，获得了5星~真是羡煞旁人！").build();
        } else {
            msgContent = builder.text("\n很遗憾，没有抽中5星，小伙子，快去洗洗手吧！").build();
        }

        msgContent = builder.image(genshinPrayResultInfo.getImgHttpUrl()).build();
        return msgContent;
    }
}
