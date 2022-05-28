package fycloud.robot.core.listener;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import catcode.Neko;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import fycloud.robot.core.APIs;
import fycloud.robot.core.entity.chat.ChatResourceInfo;
import fycloud.robot.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author VarleyT
 * @date 2022/5/20 19:37
 */
@Beans
@Slf4j
public class ChatListener {
    @OnGroup
    @Filter(atBot = true)
    public void chat(GroupMsg msg, Sender sender) {
        if (!msg.getMsg().matches("(\\[)CAT:at.+")){
            return;
        }
        log.info(msg.getAccountInfo().getAccountNickname() + "(" + msg.getAccountInfo().getAccountCode() + ") 在 " + msg.getGroupInfo().getGroupName() + "(" + msg.getGroupInfo().getGroupCode() + ") " + " 调用了 <聊天> 功能--> " + msg.getText());
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        String nickName = msg.getAccountInfo().getAccountNickname();
        String qCode = msg.getAccountInfo().getAccountCode();
        String content = msg.getText().trim();
        headers.put("Api-Key", APIs.MoliCloud.API_KEY);
        headers.put("Api-Secret", APIs.MoliCloud.API_SECRET);
        params.put("content", content);
        params.put("type", "2");
        params.put("from", qCode);
        params.put("fromName", nickName);
        params.put("to", qCode);
        params.put("toName", nickName);

        JSONObject jsonObject = HttpUtil.Post(APIs.MoliCloud.MoliChat_API, params, headers);
        String type = jsonObject.getString("plugin");
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
        List<String> imgList = new ArrayList();
        List<String> audioList = new ArrayList();

        resourceInfos.stream().forEach(item -> {
            String typed = item.getTyped();
            String itemContent = item.getContent();
            if (typed.equals("1")) {
                msgContent.append(itemContent);
            } else if (typed.equals("2")) {
                imgList.add(itemContent);
            } else if (typed.equals("4")) {
                audioList.add(itemContent);
            }
        });
        StringBuilder imgAndTextCard = new StringBuilder();
        if (!imgList.isEmpty()) {
            imgList.forEach(str -> {
                imgAndTextCard.append(imgBuilder.key("url").value(APIs.MoliCloud.RESOURCES_API + str).build());
                imgAndTextCard.append(" ");
            });
        }
        imgAndTextCard.append(msgContent);
        sender.sendGroupMsg(msg, imgAndTextCard.toString());
        if (!audioList.isEmpty()) {
            String audio = audioBuilder.key("file").value(APIs.MoliCloud.RESOURCES_API + audioList.get(0)).build();
            sender.sendGroupMsg(msg, audio);
        }
    }
}
