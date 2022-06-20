package fycloud.robot.core.listener;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import catcode.CodeTemplate;
import catcode.Neko;
import fycloud.robot.FyRobotApp;
import fycloud.robot.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.FilterTargets;
import love.forte.simbot.filter.MatchType;

import java.util.Random;

/**
 * @author VarleyT
 * @date 2022/5/27 22:33
 */
@Beans
@Slf4j
public class NudgeListener {
    private static final String localResPath = "classpath:other/nudge/";

    private static String[] res = {
            "1.gif", "2.gif", "3.gif", "4.gif", "5.gif",
            "1.jpg", "2.jpg",
            "干哈？", "哼(ˉ(∞)ˉ)唧", "(☞0 ☜)眼睛啊啊啊", "[╯•́•̀╰]让我静静", "不＞(￣ε￣ = ￣3￣)<要", "( •̀⊿•́)ง妖妖灵吗？我要报警了！"
    };

    @OnGroup
    @Filter(target = FilterTargets.MSG, value = "(\\[)CAT:nudge.+", matchType = MatchType.REGEX_MATCHES)
    public void nudge(GroupMsg msg, Sender sender) {
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String targetCode = util.getParam(msg.getMsg(), "target");
        String botCode = FyRobotApp.ROBOT_CORE.defaultBot.getBotInfo().getBotCode();
        if (!targetCode.equals(botCode)) {
            return;
        }
        log.info(LogUtil.getLog(msg, "戳一戳"));

        int randomNum = new Random().nextInt(res.length);
        String resPath = res[randomNum];
        CodeTemplate<String> template = util.getStringTemplate();
        if (resPath.matches("\\d+\\..*")) {
            String context = template.image(localResPath + resPath);
            sender.sendGroupMsg(msg, context);
        } else {
            sender.sendGroupMsg(msg, resPath);
        }
    }
}
