package fycloud.robot.core.listener;

import fycloud.robot.FyRobotApp;
import fycloud.robot.core.entity.genshin.GenshinPrayType;
import fycloud.robot.core.service.GenshinPrayService;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/4/26 20:41
 */
@Beans
public class GenshinListener {
    @Depend
    private MessageContentBuilderFactory builderFactory;

    @OnGroup
    @Filters(value = {
            @Filter(value = "角色{{param}}",matchType = MatchType.REGEX_MATCHES),
            @Filter(value = "武器{{param}}",matchType = MatchType.REGEX_MATCHES),
            @Filter(value = "常驻{{param}}",matchType = MatchType.REGEX_MATCHES)
    })
    public void RolePray(@FilterValue("param")String param, GroupMsg msg, Sender sender) {
        FyRobotApp.logger.info(msg.getAccountInfo().getAccountNickname() + "(" + msg.getAccountInfo().getAccountCode() + ") 在 " + msg.getGroupInfo().getGroupName() + "(" + msg.getGroupInfo().getGroupCode() + ") "+" 调用了 <祈愿> 功能--> " + msg.getText());
        GenshinPrayService prayService = new GenshinPrayService();
        String pefix = msg.getText().substring(0,2);
        boolean isOne = false;
        if (param.equals("单抽")) {
            isOne = true;
        } else if (param.equals("十连")) {
            isOne = false;
        } else {
            sender.sendGroupMsg(msg, "命令错误！角色/武器/常驻 [单抽|十连]");
            return;
        }
        if (pefix.equals("角色")){
            sender.sendGroupMsg(msg, prayService.Pray(GenshinPrayType.role,isOne,msg,builderFactory));
        }else if(pefix.equals("武器")){
            sender.sendGroupMsg(msg,prayService.Pray(GenshinPrayType.arm,isOne,msg,builderFactory));
        }else if(pefix.equals("常驻")){
            sender.sendGroupMsg(msg,prayService.Pray(GenshinPrayType.perm,isOne,msg,builderFactory));
        }
    }

}
