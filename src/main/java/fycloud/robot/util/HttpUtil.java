package fycloud.robot.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import static java.util.regex.Pattern.matches;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/4/22 16:42
 */
public class HttpUtil {
    /**
     * GET方法
     */
    public static String doGET(String httpUrl) {
        return doGET(httpUrl, null);
    }

    public static String doGET(String httpUrl, Map<String, String> header) {
        return doGET(httpUrl, header, null);
    }

    public static String doGET(String httpUrl, Map<String, String> header, Map<String, String> param) {
        String result = "";
        InputStream is;
        BufferedReader br;
        try {
            if (param != null) {
                httpUrl += "?";
                param.forEach((key, value) -> {
                    if (matches("[\u4e00-\u9fa5]+", value)) {
                        try {
                            param.replace(key, URLEncoder.encode(value, "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
                for (String key : param.keySet()) {
                    httpUrl += key + "=" + param.get(key) + "&";
                }
                httpUrl = httpUrl.substring(0, httpUrl.length() - 1);
            }
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36");
            if (header != null) {
                for (String key : header.keySet()) {
                    connection.setRequestProperty(key, header.get(key));
                }
            }
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                br.close();
                is.close();
                connection.disconnect();
            } else {
                connection.disconnect();
                throw new RuntimeException("URL连接失败!!" + " [ " + responseCode + " ] ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * POST方法
     */
    public static String doPOST(String HttpUrl) {
        return doPOST(HttpUrl,null);
    }
    public static String doPOST(String HttpUrl, Map<String, String> header){
        return null;
    }
}
