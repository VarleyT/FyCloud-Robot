package online.fycloud.bot.core.util;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VarleyT
 *
 */
@Slf4j
public class BotHttpUtil {

    /**
     * GET请求
     *
     * @param url 网络链接
     * @return JSONObject JSON对象
     */
    public static JSONObject doGet(String url) {
        return doGet(url, (Map<String, String>) null, null);
    }

    /**
     * GET请求
     *
     */
    public static JSONObject doGet(String url, String paramKey, String paramValue) {
        return doGet(url, new HashMap<String, String>(1) {
            {
                put(paramKey, paramValue);
            }
        }, null);
    }

    /**
     * GET请求
     *
     */
    public static JSONObject doGet(String url, Map<String, String> paramMap) {
        return doGet(url, paramMap, null);
    }

    /**
     * GET请求
     *
     */
    public static JSONObject doGet(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
        HttpRequest request;
        if (paramMap != null && !paramMap.isEmpty()) {
            StringBuilder builder = new StringBuilder("?");
            paramMap.forEach((key, value) -> {
                final String CN_REGEX = "[\u4e00-\u9fa5]+";
                if (value.matches(CN_REGEX)) {
                    try {
                        // 中文自动编码
                        value = URLEncoder.encode(value, "UTF-8");
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
            url += builder.toString();
        }
        request = HttpRequest.get(url)
                .timeout(3000);
        if (headerMap != null && !headerMap.isEmpty()) {
            request = request.headerMap(headerMap, false);
        }
        return submit(request);
    }

    /**
     * POST方法
     *
     */
    public static JSONObject doPost(String url, Map<String, String> bodyJsonMap) {
        return doPost(url, null, bodyJsonMap);
    }

    /**
     * POST方法
     *
     */
    public static JSONObject doPost(String url,
                                    Map<String, String> headerMap, Map<String, String> bodyJsonMap) {
        HttpRequest request = HttpRequest.post(url);
        if (headerMap != null || !headerMap.isEmpty()) {
            request = request.headerMap(headerMap, false);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(bodyJsonMap);
        request = request.timeout(3000)
                .body(jsonObject.toString(), "application/json;charset=UTF-8");
        return submit(request);
    }

    public static JSONObject submit(HttpRequest request) {
        HttpResponse response = request.execute();
        if (response.isOk()) {
            String responseBody = response.body();
            if (responseBody != null || responseBody.equals("")) {
                try {
                    JSONObject jsonObject = JSON.parseObject(responseBody);
                    if (jsonObject != null) {
                        return jsonObject;
                    }
                } catch (Exception e) {
                    log.error("转换httpResponseBody至JSONObject时失败！Cased by：{}", e);
                }
            }
        }
        return null;
    }
}
