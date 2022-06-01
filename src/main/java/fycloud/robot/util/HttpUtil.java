package fycloud.robot.util;

/**
 * @author VarleyT
 * @date 2022/5/20 19:06
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fycloud.robot.FyRobotApp;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author VarleyT
 * @date 2022/5/20 13:03
 */
@Slf4j
public class HttpUtil {
    private static OkHttpClient client;
    private static Request request;

    static {
        client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.MINUTES))
                .build();
        request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .url("http://www.baidu.com/")
                .build();
    }

    /**
     * Get请求
     *
     * @param url
     * @param params
     * @param headers
     * @return JSONObject
     */
    public static JSONObject Get(String url, Map<String, String> params, Map<String, String> headers) {
        if (params != null) {
            url += GetParams(params);
        }
        request = request.newBuilder()
                .url(url)
                .build();
        if (headers != null) {
            request = request.newBuilder()
                    .headers(GetHeaders(headers))
                    .build();
        }
        return Request(request);
    }

    public static JSONObject Get(String url, Map<String, String> params) {
        return Get(url, params, null);
    }

    public static JSONObject Get(String url) {
        return Get(url, null, null);
    }

    public static JSONObject Post(String url, Map<String, String> params, Map<String, String> headers) {
        request = request.newBuilder()
                .url(url)
                .build();
        if (params != null) {
            final MediaType parse = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(parse, JSON.toJSONString(params));
            request = request.newBuilder()
                    .post(requestBody)
                    .build();
        }
        if (headers != null) {
            for (Map.Entry<String, String> entry :
                    headers.entrySet()) {
                request = request.newBuilder()
                        .addHeader(entry.getKey(), entry.getValue())
                        .build();
            }
        }
        return Request(request);
    }

    private static JSONObject Request(Request request) {
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200) {
                return JSON.parseObject(response.body().string());
            } else {
                log.error("网页请求失败，状态码：{}，请求体：{}", response.code(), response.body().string());
                throw new RuntimeException("网页请求失败");
            }
        } catch (IOException e) {
            log.error("网页请求出错！Cased by：{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static String GetParams(Map<String, String> params) {
        StringBuilder builder = new StringBuilder("?");
        params.forEach((key, value) -> {
            if (value.matches("[\u4e00-\u9fa5]+")) {
                try {
                    String newValue = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            builder.append(key)
                    .append("=")
                    .append(value)
                    .append("&");
        });
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    private static Headers GetHeaders(Map<String, String> headers) {
        Headers header = null;
        Headers.Builder builder = new Headers.Builder();
        headers.forEach((key, value) -> {
            builder.add(key, value);
        });
        header = builder.build();
        return header;
    }
}

