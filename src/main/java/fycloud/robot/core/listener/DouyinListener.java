package fycloud.robot.core.listener;

import com.alibaba.fastjson.JSONObject;
import fycloud.robot.core.APIs;
import fycloud.robot.util.HttpUtil;
import fycloud.robot.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author VarleyT
 * @date 2022/8/2 20:47
 */
@Beans
@Slf4j
public class DouyinListener {
    @OnGroup
    @Filters(value = {
            @Filter(value = "解析{{url}}",matchType = MatchType.REGEX_MATCHES),
            @Filter(value = "抖音解析{{url}}",matchType = MatchType.REGEX_MATCHES)
    })
    public void douyin(GroupMsg msg, Sender sender, @FilterValue("url")String originalText){
        String r = "https:\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
        Pattern p1 = Pattern.compile(r);
        Matcher m1 = p1.matcher(originalText);
        if (!m1.find()){
            return;
        }

        log.info(LogUtil.getLog(msg, "抖音解析"));
        final String regex = "https://v\\.douyin\\.com/\\w{7}/";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(originalText);
        String originalUrl = "";
        if (m.find()) {
            originalUrl = m.group(0);
        }else {
            sender.sendGroupMsg(msg, "解析失败，请输入正确的链接");
            return;
        }
        if (originalUrl.equals("")){
            return;
        }

        String request = String.format(APIs.LinHunYun_API.DOUYIN, originalUrl);
        JSONObject jsonObject = HttpUtil.Get(request);
        if(!jsonObject.getString("code").equals("200")){
            try {
                String tips = jsonObject.getString("tips");
                sender.sendGroupMsg(msg,tips);
                return;
            }catch (Exception e){
                log.error("抖音解析失败！！Cased By: \n{}",e.getMessage());
                sender.sendGroupMsg(msg,"解析失败");
                return;
            }
        }
        String url = jsonObject.getString("url");
        if (url.equals("")){
            return;
        }
        sender.sendGroupMsg(msg, url);
    }


}
