package fycloud.robot.core.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fycloud.robot.FyRobotApp;
import fycloud.robot.core.APIs;
import fycloud.robot.util.HttpUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

/**
 * @author VarleyT
 * @date 2022/5/19 23:07
 */
@Beans
public class ChatListener {
    @OnGroup
    @Filter(atBot = true)
    public void start(GroupMsg msg, Sender sender) {
        FyRobotApp.logger.info(msg.getAccountInfo().getAccountNickname() + "(" + msg.getAccountInfo().getAccountCode() + ") 在 " + msg.getGroupInfo().getGroupName() + "(" + msg.getGroupInfo().getGroupCode() + ") " + " 调用了 <聊天> 功能--> " + msg.getMsg());
        String question = msg.getText().trim();
        JSONObject json = JSON.parseObject(HttpUtil.doGET(String.format(APIs.XiaoBai_API.XiaoAi, question)));
        String answer = json.getString("text");
        sender.sendGroupMsg(msg, answer);
    }
}