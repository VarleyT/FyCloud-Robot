package online.fycloud.bot.entertainment.logic;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import catcode.Neko;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.simbot.api.message.events.GroupMsg;
import online.fycloud.bot.core.config.BotApis;
import online.fycloud.bot.core.util.BotHttpUtil;
import online.fycloud.bot.entertainment.entity.ChatResourceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author VarleyT
 *
 */
@Component
public class ChatUtil {
    @Autowired
    private BotApis botApis;

    public List<String> chat(GroupMsg msg){
        String text = msg.getText();
        String senderCode = msg.getAccountInfo().getAccountCode();
        String groupCode = msg.getGroupInfo().getGroupCode();
        Map<String, String> body = new HashMap<>(4) {{
            put("content", text);
            put("type", "2");
            put("from", senderCode);
            put("to", groupCode);
        }};
        Map<String, String> header = new HashMap<>(2) {{
            put("Api-Key", botApis.getMoLi_ApiKey());
            put("Api-Secret", botApis.getMoLi_ApiSecret());
        }};
        JSONObject jsonObject = BotHttpUtil.doPost(botApis.getMoLi_ChatApi(), header, body);
        //解析json
        JSONArray data = jsonObject.getJSONArray("data");
        List<ChatResourceInfo> resourceInfos = data.stream().map(obj -> {
            JSONObject item = (JSONObject) obj;
            return ChatResourceInfo.builder()
                    .typed(item.getString("typed"))
                    .content(item.getString("content"))
                    .remark(item.getString("remark"))
                    .build();
        }).collect(Collectors.toList());
        final CodeBuilder<String> audioBuilder = CatCodeUtil.getInstance().getStringCodeBuilder("record", false);
        final CodeBuilder<Neko> imgBuilder = CatCodeUtil.getInstance().getNekoBuilder("image", false);
        StringBuilder msgContent = new StringBuilder();
        List<String> imgList = new ArrayList<>();
        List<String> audioList = new ArrayList<>();
        resourceInfos.forEach(item -> {
            String typed = item.getTyped();
            String itemContent = item.getContent();
            if ("1".equals(typed)) {
                msgContent.append(itemContent);
            } else if ("2".equals(typed)) {
                imgList.add(itemContent);
            } else if ("4".equals(typed)) {
                audioList.add(itemContent);
            }
        });
        StringBuilder imgAndTextCard = new StringBuilder();
        if (!imgList.isEmpty()) {
            imgList.forEach(str -> {
                imgAndTextCard.append(imgBuilder.key("url").value(botApis.getMoLi_ResourceApi() + str).build());
                imgAndTextCard.append(" ");
            });
        }
        imgAndTextCard.append(msgContent);
        List<String> list = new ArrayList<>();
        list.add(imgAndTextCard.toString());
        if (!audioList.isEmpty()) {
            String audio = audioBuilder.key("file").value(botApis.getMoLi_ResourceApi() + audioList.get(0)).build();
            list.add(audio);
        }
        return list;
    }
}
