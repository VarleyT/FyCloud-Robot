package online.fycloud.bot.entertainment.listener;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import kotlinx.coroutines.TimeoutCancellationException;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnlySession;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.listener.ContinuousSessionScopeContext;
import love.forte.simbot.listener.ListenerContext;
import love.forte.simbot.listener.SessionCallback;
import online.fycloud.bot.entertainment.entity.NetEaseMusicInfo;
import online.fycloud.bot.entertainment.logic.MusicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author VarleyT
 */
@Component
@Slf4j
public class MusicListener {
    private final String SelectNumGroup = "SelectNumGroup_1n5z2ia";
    @Autowired
    private MusicUtil musicUtil;
    @Autowired
    private MessageContentBuilderFactory messageContentBuilderFactory;

    @OnGroup
    @Filter(value = "(点歌|搜歌){{songName}}", matchType = MatchType.REGEX_MATCHES)
    public void music(GroupMsg msg, Sender sender, ListenerContext context, @FilterValue("songName") String songName) {
        log.info("[{}] <{}({})> {}({})：{}",
                "点歌",
                msg.getGroupInfo().getGroupName(), msg.getGroupInfo().getGroupCode(),
                msg.getAccountInfo().getAccountNickname(), msg.getAccountInfo().getAccountCode(),
                msg.getMsg());
        if (songName.equals("")) {
            log.error("歌曲名为空！");
            return;
        }
        String encodedName;
        try {
            encodedName = URLEncoder.encode(songName, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("歌名加密失败！！");
        }
        List<NetEaseMusicInfo> songList = musicUtil.songOfList(encodedName);
        MessageContentBuilder msgBuilder = messageContentBuilderFactory.getMessageContentBuilder();
        MessageContent searchResultContent = msgBuilder.text("---------【搜索结果】---------\n").build();
        for (int i = 0; i < songList.size(); i++) {
            searchResultContent = msgBuilder.text("【" + (i + 1) + "】 ")
                    .text(songList.get(i).getName())
                    .text(" - ")
                    .text(songList.get(i).getAuthor())
                    .text("\n")
                    .build();
        }
        searchResultContent = msgBuilder.text("\n请选择播放的序号：\n")
                .text("-------------------------------")
                .build();
        sender.sendGroupMsg(msg, searchResultContent);
        final ContinuousSessionScopeContext sessionContext = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert sessionContext != null;
        final String groupCode = msg.getGroupInfo().getGroupCode();
        final String accountCode = msg.getAccountInfo().getAccountCode();
        final String key = groupCode + ":" + accountCode;
        final SessionCallback<Integer> callback = SessionCallback.<Integer>builder().onResume(selectNum -> {
            NetEaseMusicInfo SongDetail = musicUtil.SongDetail(songList.get(selectNum), songList.get(selectNum).getId());
            CodeBuilder<String> builder = CatCodeUtil.getInstance().getStringCodeBuilder("music", false);
            String songCard = builder.key("type").value("neteaseCloudMusic")
                    .key("musicUrl").value(SongDetail.getMp3Url())
                    .key("title").value(SongDetail.getName())
                    .key("pictureUrl").value(SongDetail.getImgUrl())
                    .key("jumpUrl").value(SongDetail.getJumpUrl())
                    .key("brief").value(SongDetail.getName() + "-" + SongDetail.getAuthor())
                    .key("summary").value(SongDetail.getAuthor())
                    .build();
            if (SongDetail.getMp3Url().equals("") || SongDetail.getMp3Url() == null) {
                sender.sendGroupMsg(msg, "该歌曲暂时无法播放，换一个吧~");
            } else {
                sender.sendGroupMsg(msg, songCard);
            }
        }).onError(e -> {
            if (e instanceof TimeoutCancellationException) {
                sender.sendGroupMsg(msg, "超时啦！请重新点歌");
            } else {
                e.printStackTrace();
                sender.sendGroupMsg(msg, "出错了！请尝试重新点歌");
            }
        }).onCancel(e -> {

        }).build();
        sessionContext.waiting(SelectNumGroup, key, callback);
    }

    /**
     * 延续第一个监听
     * 读入选择的序号 (Integer)
     */
    @OnGroup
    @OnlySession(group = SelectNumGroup)
    @Filter(value = "10|[1-9]", matchType = MatchType.REGEX_MATCHES)
    public void SelectNumListen(GroupMsg m, ListenerContext context) {
        final ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;
        final String groupCode = m.getGroupInfo().getGroupCode();
        final String accountCode = m.getAccountInfo().getAccountCode();
        String key = groupCode + ":" + accountCode;
        session.push(SelectNumGroup, key, Integer.parseInt(m.getText()) - 1);
    }
}
