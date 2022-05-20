package fycloud.robot.core.listener;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import com.alibaba.fastjson.JSONObject;
import fycloud.robot.FyRobotApp;
import fycloud.robot.core.APIs;
import fycloud.robot.util.HttpUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/5/9 20:11
 */
@Beans
public class SingerListener {
    @OnGroup
    @Filter(value = "(唱首歌|来首歌|唱歌)", matchType = MatchType.REGEX_MATCHES)
    public void listen(GroupMsg msg, Sender sender) {
        FyRobotApp.logger.info(msg.getAccountInfo().getAccountNickname() + "(" + msg.getAccountInfo().getAccountCode() + ") 在 " + msg.getGroupInfo().getGroupName() + "(" + msg.getGroupInfo().getGroupCode() + ") "+" 调用了 <唱歌> 功能--> " + msg.getText());
        JSONObject response = HttpUtil.Get(APIs.XiaoBai_API.Sing);
        JSONObject data = response.getJSONObject("data");
        String audioUrl = data.getString("audioSrc");
        final CodeBuilder<String> codeBuilder = CatCodeUtil.getInstance().getStringCodeBuilder("record", false);
        String catCode = codeBuilder
                .key("file").value(audioUrl)
                .build();
        sender.sendGroupMsg(msg, catCode);
    }
}
