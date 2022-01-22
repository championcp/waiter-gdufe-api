package com.waiterxiaoyy.backandroiddesign.utils.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InitLogin {

    private String url_Login = "http://jwxt.gdufe.edu.cn/jsxsd/xk/LoginToXkLdap"; // 登录
    private String url_safecode = "http://jwxt.gdufe.edu.cn/jsxsd/verifycode.servlet"; // 验证码

//    private String username = "18251104126";
//    private String password = "aaa121223gc123";

    private String username;
    private String password;
//    private String username = "18251104129";
//    private String password = "guangzhou0901";

    private GetSafeCode getSafeCode = new GetSafeCode();
//    private String path = JsoupSafeCode.class.getResource("/").getPath().replaceAll("%20", " ") + "safecode.png";
    public Map<String, String> cookie;
    public byte[] bytes;



    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 读取验证码
     * 保存Cookie
     * @throws IOException
     */
    public void getCookie() throws IOException {
        Connection.Response response = Jsoup.connect(url_safecode).ignoreContentType(true) // 获取图片需设置忽略内容类型
                .userAgent("Mozilla").method(Connection.Method.GET).timeout(3000).execute();
        cookie = response.cookies();
        bytes = response.bodyAsBytes();
    }
    /**
     * 登录教务系统
     */
    public void initLogin() throws IOException {
        String code = getSafeCode.getSafeCode(bytes);
        try {
            Map<String, String> data = new HashMap<String, String>();
            data.put("USERNAME", username);
            data.put("PASSWORD", password);
            data.put("RANDOMCODE", code);
            Connection connect = Jsoup.connect(url_Login)
                    .header("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                    .method(Connection.Method.POST)
                    .data(data)
                    .timeout(3000);

            for (Map.Entry<String, String> entry : cookie.entrySet()) {
                connect.cookie(entry.getKey(), entry.getValue());
            }
            Connection.Response response = connect.execute();
        } catch (IOException e) {

        }
    }

}
