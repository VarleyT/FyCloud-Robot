package fycloud.robot.core.listener;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import catcode.Neko;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fycloud.robot.core.APIs;
import fycloud.robot.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/5/7 23:55
 */
@Beans
@Slf4j
public class BeautyPicListener {

    @OnGroup
    @Filter(value = "(来点好看|来点好康)",matchType = MatchType.REGEX_FIND)
    public void listen(GroupMsg m, Sender sender){
        log.info(m.getAccountInfo().getAccountNickname() + "(" + m.getAccountInfo().getAccountCode() + ") 在 " + m.getGroupInfo().getGroupName() + "(" + m.getGroupInfo().getGroupCode() + ") " + " 调用了 <看图> 功能--> " + m.getText());
        final String responseJSON = HttpUtil.doGET(APIs.BeautyPic_API);
        JSONObject result = JSON.parseObject(responseJSON);
        JSONObject data = result.getJSONObject("data");
        final String imgUrl = data.getString("url");
        final CodeBuilder<Neko> nekoBuilder = CatCodeUtil.getInstance().getNekoBuilder("image",false);
        Neko imgCode = nekoBuilder
                .key("url").value(imgUrl)
                .build();
        sender.sendGroupMsg(m,imgCode.toString());
    }
}
