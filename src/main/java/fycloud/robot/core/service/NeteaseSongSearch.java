package fycloud.robot.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import fycloud.robot.core.APIs;
import fycloud.robot.core.entity.netease.NeteaseMusicInfo;
import fycloud.robot.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/4/22 18:30
 */
@Slf4j
public class NeteaseSongSearch {
    public static List<NeteaseMusicInfo> SongOfList(String Name) {
        JSONObject response = HttpUtil.Get(String.format(APIs.Netease_API.search, Name));
        final JSONObject result = response.getJSONObject("result");
        JSONArray songs;
        songs = result.getJSONArray("songs");
        List<NeteaseMusicInfo> SongLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            JSONObject songdetail;
            try {
                songdetail = songs.getJSONObject(i);
            }catch (Exception e) {
                log.error("获取歌曲列表失败！！\nCause by: "+ response.toJSONString());
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
            SongLists.add(new NeteaseMusicInfo(id, name, author, imgUrl, "", jumpUrl));
        }
        return SongLists;
    }

    public static NeteaseMusicInfo SongDetail(NeteaseMusicInfo neteaseMusicInfo, String SongId) {
        JSONObject response = HttpUtil.Get(String.format(APIs.Netease_API.mp3url, SongId));
        JSONArray data = response.getJSONArray("data");
        final String mp3Url = data.getJSONObject(0).getString("url");
        return new NeteaseMusicInfo(neteaseMusicInfo.getId(), neteaseMusicInfo.getName(), neteaseMusicInfo.getAuthor(), neteaseMusicInfo.getImgUrl(), mp3Url, neteaseMusicInfo.getJumpUrl());
    }
}
