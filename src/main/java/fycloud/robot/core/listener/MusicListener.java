package fycloud.robot.core.listener;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import fycloud.robot.FyRobotApp;
import fycloud.robot.core.entity.netease.NeteaseMusicInfo;
import fycloud.robot.core.service.NeteaseSongSearch;
import kotlinx.coroutines.TimeoutCancellationException;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
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

import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/4/22 16:41
 */

@Beans
public class MusicListener {
    private final String SelectNumGroup = "SelectNumGroup_1n5z2ia";

    @Depend
    public MessageContentBuilderFactory messageContentBuilderFactory;

    @OnGroup
    @Filter(value = "(点歌|搜歌){{name}}", matchType = MatchType.REGEX_MATCHES)
    public void start(@FilterValue("name")String searchSongName, GroupMsg m, ListenerContext context, Sender sender) {
        FyRobotApp.logger.info(m.getAccountInfo().getAccountNickname() + "(" + m.getAccountInfo().getAccountCode() + ") 在 " + m.getGroupInfo().getGroupName() + "(" + m.getGroupInfo().getGroupCode() + ") " + " 调用了 <点歌> 功能--> " + m.getText());
        String EncodedName = "";
        try {
            EncodedName = URLEncoder.encode(searchSongName, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException("歌名加密失败！！");
        }
        List<NeteaseMusicInfo> SongList = NeteaseSongSearch.SongOfList(EncodedName);
        MessageContentBuilder MsgBuilder = messageContentBuilderFactory.getMessageContentBuilder();
        MessageContent SearchResultContent = MsgBuilder.text("----------【搜索结果】----------\n").build();
        for (int i = 0; i < SongList.size(); i++) {
            SearchResultContent = MsgBuilder.text("【" + (i + 1) + "】 ")
                    .text(SongList.get(i).getName())
                    .text(" - ")
                    .text(SongList.get(i).getAuthor())
                    .text("\n")
                    .build();
        }
        SearchResultContent = MsgBuilder.text("\n请选择播放的序号：\n")
                .text("---------------------------------")
                .build();
        sender.sendGroupMsg(m, SearchResultContent);

        final ContinuousSessionScopeContext sessionContext = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert sessionContext != null;
        final String groupCode = m.getGroupInfo().getGroupCode();
        final String accountCode = m.getAccountInfo().getAccountCode();
        final String key = groupCode + ":" + accountCode;

        final SessionCallback<Integer> callback = SessionCallback.<Integer>builder().onResume(SelectNum -> {
            NeteaseMusicInfo SongDetail = NeteaseSongSearch.SongDetail(SongList.get(SelectNum), SongList.get(SelectNum).getId());
            CodeBuilder<String> builder = CatCodeUtil.getInstance().getStringCodeBuilder("music", false);
            String SongCard = builder.key("type").value("neteaseCloudMusic")
                    .key("musicUrl").value(SongDetail.getMp3Url())
                    .key("title").value(SongDetail.getName())
                    .key("pictureUrl").value(SongDetail.getImgUrl())
                    .key("jumpUrl").value(SongDetail.getJumpUrl())
                    .key("brief").value(SongDetail.getName() + "-" + SongDetail.getAuthor())
                    .key("summary").value(SongDetail.getAuthor())
                    .build();
            if (SongDetail.getMp3Url().equals("") || SongDetail.getMp3Url() == null) {
                sender.sendGroupMsg(m, "该歌曲暂时无法播放，换一个吧~");
            } else {
                sender.sendGroupMsg(m, SongCard);
            }
        }).onError(e -> {
            if (e instanceof TimeoutCancellationException) {
                sender.sendGroupMsg(m, "超时啦！请重新点歌");
            } else if (e instanceof CancellationException){

            } else {
                e.printStackTrace();
                sender.sendGroupMsg(m, "出错了！请尝试重新点播");
            }
        }).onCancel(e -> {
            if (e instanceof TimeoutCancellationException) {

            } else if (e instanceof CancellationException) {

            } else {
//                sender.sendGroupMsg(m, "点播取消！");
            }
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
