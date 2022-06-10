package fycloud.robot.core.listener;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import catcode.Neko;
import com.alibaba.fastjson.JSONObject;
import fycloud.robot.FyRobotApp;
import fycloud.robot.core.APIs;
import fycloud.robot.util.HttpUtil;
import fycloud.robot.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;

/**
 * @author VarleyT
 * @date 2022/5/20 21:02
 */
@Beans
@Slf4j
public class LookLookListener {
    @OnGroup
    @Filter(value = "(来点好看|来点好康).*",matchType = MatchType.REGEX_MATCHES)
    public void look(GroupMsg msg, Sender sender){
        log.info(LogUtil.getLog(msg, "来点好康的"));
        JSONObject result = HttpUtil.Get(APIs.DuJiaoShou_API.LookLook);
        String imgUrl = result.getString("text");
        final CodeBuilder<Neko> nekoBuilder = CatCodeUtil.getInstance().getNekoBuilder("image", false);
        Neko imgCard = nekoBuilder
                .key("url").value(imgUrl)
                .build();
        sender.sendGroupMsg(msg, imgCard.toString());
    }
}
