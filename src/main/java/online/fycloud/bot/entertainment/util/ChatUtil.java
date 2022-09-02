package online.fycloud.bot.entertainment.util;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import catcode.Neko;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import online.fycloud.bot.core.util.BotHttpUtil;
import online.fycloud.bot.entertainment.entity.ChatResourceInfo;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${API.MoLiCloud.CHAT_API}")
    private String MoLi_ChatApi;
    @Value("${API.MoLiCloud.RESOURCES_API}")
    private String MoLi_ResourceApi;
    @Value("${API.MoLiCloud.API_KEY}")
    private String MoLi_ApiKey;
    @Value("${API.MoLiCloud.API_SECRET}")
    private String MoLi_ApiSecret;

    public List<String> chat(GroupMsg msg){
        String text = msg.getText();
        String senderCode = msg.getAccountInfo().getAccountCode();
        String groupCode = msg.getGroupInfo().getGroupCode();
        Map<String, String> body = new HashMap<String, String>(4) {{
            put("content", text);
            put("type", "2");
            put("from", senderCode);
            put("to", groupCode);
        }};
        Map<String, String> header = new HashMap<String, String>(2) {{
            put("Api-Key", MoLi_ApiKey);
            put("Api-Secret", MoLi_ApiSecret);
        }};
        JSONObject jsonObject = BotHttpUtil.doPost(MoLi_ChatApi, header, body);
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
                imgAndTextCard.append(imgBuilder.key("url").value(MoLi_ResourceApi + str).build());
                imgAndTextCard.append(" ");
            });
        }
        imgAndTextCard.append(msgContent);
        List<String> list = new ArrayList<>();
        list.add(imgAndTextCard.toString());
        if (!audioList.isEmpty()) {
            String audio = audioBuilder.key("file").value(MoLi_ResourceApi + audioList.get(0)).build();
            list.add(audio);
        }
        return list;
    }
}
