package fycloud.robot.core.listener;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import catcode.Neko;
import fycloud.robot.FyRobotApp;
import fycloud.robot.core.APIs;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/5/9 19:26
 */
@Beans
public class AtActionListener {
    @OnGroup
    @Filter(value = "(丢|爬)", matchType = MatchType.REGEX_FIND)
    public void diuOrPa(GroupMsg msg, Sender sender) {
        if (!msg.getMsg().matches("(丢|爬)(\\[)CAT:at.+")){
            return;
        }
        FyRobotApp.logger.info(msg.getAccountInfo().getAccountNickname() + "(" + msg.getAccountInfo().getAccountCode() + ") 在 " + msg.getGroupInfo().getGroupName() + "(" + msg.getGroupInfo().getGroupCode() + ") " + " 调用了 <丢爬> 功能--> " + msg.getMsg());
        CatCodeUtil catCodeUtil = CatCodeUtil.getInstance();
        String catCode = catCodeUtil.getCat(msg.getMsg(), 0);
        String code = catCodeUtil.getParam(catCode, "code");
        String url = "";
        if (msg.getText().startsWith("爬")) {
            url = String.format(APIs.XiaoBai_API.Paa, code);
        } else if (msg.getText().startsWith("丢")) {
            url = String.format(APIs.XiaoBai_API.Diu, code);
        }
        if (url.equals("")) {
            return;
        }
        final CodeBuilder<Neko> nekoBuilder = CatCodeUtil.getInstance().getNekoBuilder("image", false);
        Neko imgCard = nekoBuilder
                .key("url").value(url)
                .build();
        sender.sendGroupMsg(msg, imgCard.toString());
    }

}
