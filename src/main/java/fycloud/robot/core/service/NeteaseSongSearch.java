package fycloud.robot.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import fycloud.robot.core.APIs;
import fycloud.robot.core.entity.netease.NeteaseMusicInfo;
import fycloud.robot.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/4/22 18:30
 */
public class NeteaseSongSearch {
    public static List<NeteaseMusicInfo> SongOfList(String Name) {
        final String resultJson = HttpUtil.doGET(String.format(APIs.Netease_API.getsearch, Name, 1, 0, 10));
        System.out.println(resultJson);
        final JSONObject response = JSON.parseObject(resultJson);
        final JSONObject result = response.getJSONObject("result");
        final JSONArray songs = result.getJSONArray("songs");
        List<NeteaseMusicInfo> SongLists = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            JSONObject songdetail = songs.getJSONObject(i);
            final String id = songdetail.getString("id");
            final String name = songdetail.getString("name");
            final JSONArray artistsList = songdetail.getJSONArray("artists");
            final List<String> artistList = artistsList.stream().map(item -> {
                JSONObject object = (JSONObject) item;
                return object.getString("name");
            }).collect(Collectors.toList());
            final String author = String.join("&", artistList);
            final String jumpUrl = "https://music.163.com/#/song?id=" + id;
            SongLists.add(new NeteaseMusicInfo(id, name, author, "", "", jumpUrl));
        }

        return SongLists;
    }

    public static NeteaseMusicInfo SongDetail(NeteaseMusicInfo neteaseMusicInfo, String SongId) {
        final JSONObject response = JSON.parseObject(HttpUtil.doGET(String.format(APIs.Netease_API.getsongdetail, SongId)));
        String imgUrl = response.getString("cover");
        String mp3Url = response.getString("mp3url");
        return new NeteaseMusicInfo(neteaseMusicInfo.getId(), neteaseMusicInfo.getName(), neteaseMusicInfo.getAuthor(), imgUrl, mp3Url, neteaseMusicInfo.getJumpUrl());
    }
}
