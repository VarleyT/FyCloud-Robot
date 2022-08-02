package fycloud.robot.core.listener;

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
 * @date 2022/5/28 19:10
 */
@Beans
@Slf4j
public class MenuListener {
    @OnGroup
    @Filter(value = "菜单|帮助|help",matchType = MatchType.REGEX_MATCHES)
    public void menu(GroupMsg msg, Sender sender) {
        log.info(LogUtil.getLog(msg, "菜单"));
        StringBuilder sb = new StringBuilder();
        sb.append("【菜单】\n")
                .append("一、娱乐功能\n")
                .append("1.聊天：@bot [内容]\n")
                .append("2.丢爬：丢|爬 @人\n")
                .append("3.原神祈愿：武器|角色|常驻 [单抽|十连]\n")
                .append("4.好康的：来点好看|来点好康 \n")
                .append("5.网易云点歌：点歌|搜歌 [歌名]\n")
                .append("6.戳一戳：戳头像触发\n")
                .append("7.唱歌：唱首歌|来首歌|唱歌\n")
                .append("8.抖音解析：解析|抖音解析 [抖音视频链接]\n")
                .append("\n")
                .append("二、管理功能(需相应权限)\n")
                .append("1.开关机：开机|关机\n")
                .append("2.状态：获取机器人状态\n");
        sender.sendGroupMsg(msg, sb.toString());
    }
}
