package online.fycloud.bot.entertainment.logic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import online.fycloud.bot.core.config.BotApis;
import online.fycloud.bot.core.util.BotHttpUtil;
import online.fycloud.bot.entertainment.entity.NetEaseMusicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author VarleyT
 */
@Component
@Slf4j
public class MusicUtil {
    @Autowired
    private BotApis botApis;

    public List<NetEaseMusicInfo> songOfList(String Name) {
        JSONObject response = BotHttpUtil.doGet(String.format(botApis.getNetease_SearchApi(), Name));
        final JSONObject result = response.getJSONObject("result");
        JSONArray songs = result.getJSONArray("songs");
        List<NetEaseMusicInfo> SongLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            JSONObject songdetail;
            try {
                songdetail = songs.getJSONObject(i);
            } catch (Exception e) {
                log.error("获取歌曲列表失败！！\nCause by: " + response.toJSONString());
                return null;
            }
            final String id = songdetail.getString("id");
            final String name = songdetail.getString("name");
            final JSONArray artistsList = songdetail.getJSONArray("ar");
            final List<String> artistList = artistsList.stream().map(item -> {
                JSONObject object = (JSONObject) item;
                return object.getString("name");
            }).collect(Collectors.toList());
            final String author = String.join("&", artistList);
            final String jumpUrl = "https://music.163.com/#/song?id=" + id;
            final String imgUrl = songdetail.getJSONObject("al").getString("picUrl");
            SongLists.add(new NetEaseMusicInfo(id, name, author, imgUrl, "", jumpUrl));
        }
        return SongLists;
    }

    public NetEaseMusicInfo SongDetail(NetEaseMusicInfo neteaseMusicInfo, String SongId) {
        JSONObject response = BotHttpUtil.doGet(String.format(botApis.getNetease_Mp3Api(), SongId));
        System.out.println(response);
        JSONArray data = response.getJSONArray("data");
        final String mp3Url = data.getJSONObject(0).getString("url");
        return new NetEaseMusicInfo(neteaseMusicInfo.getId(), neteaseMusicInfo.getName(), neteaseMusicInfo.getAuthor(), neteaseMusicInfo.getImgUrl(), mp3Url, neteaseMusicInfo.getJumpUrl());
    }
}
