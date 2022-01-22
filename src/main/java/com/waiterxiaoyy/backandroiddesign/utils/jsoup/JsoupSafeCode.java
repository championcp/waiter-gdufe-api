package com.waiterxiaoyy.backandroiddesign.utils.jsoup;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * 模拟登录带验证码的教务系统
 *
 * 2018-2-9
 */
public class JsoupSafeCode {
    private String url_safecode = "http://jwxt.gdufe.edu.cn/jsxsd/verifycode.servlet"; // 验证码
    private String url_encode = "http://jwxt.gdufe.edu.cn/Logon.do?method=logon&flag=sess"; // 加密字符串
    private String url_Login = "http://jwxt.gdufe.edu.cn/jsxsd/xk/LoginToXkLdap"; // 登录
    private String username = "18251104126";
    private String password = "aaa121223gc123";
    private String path = JsoupSafeCode.class.getResource("/").getPath().replaceAll("%20", " ") + "safecode.png";
    public Map<String, String> cookie;

    /**
     * 下载验证码
     * 保存Cookie
     * @throws IOException
     */
    public void getSafeCode() throws IOException {
        Response response = Jsoup.connect(url_safecode).ignoreContentType(true) // 获取图片需设置忽略内容类型
                .userAgent("Mozilla").method(Method.GET).timeout(3000).execute();
        cookie = response.cookies();
        byte[] bytes = response.bodyAsBytes();
        Util.saveFile(path, bytes);
        System.out.println("保存验证码到：" + path);
    }

    /**
     * 登录教务系统
     */
    public void initLogin() throws IOException {
        System.out.print("输入验证码：");
        Scanner scan = new Scanner(System.in);
        String code = scan.next();
        try {
            Map<String, String> data = new HashMap<String, String>();
            data.put("USERNAME", username);
            data.put("PASSWORD", password);
            data.put("RANDOMCODE", code);
            Connection connect = Jsoup.connect(url_Login)
                    .header("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                    .method(Method.POST)
                    .data(data)
                    .timeout(3000);

            for (Map.Entry<String, String> entry : cookie.entrySet()) {
                connect.cookie(entry.getKey(), entry.getValue());
            }

            Response response = connect.execute();
            //System.out.println(response.parse().text().toString());
        } catch (IOException e) {

        }
    }

    /**
     * 加密参数
     */
    public String getEncoded() {
        try {
            Connection connect = Jsoup.connect(url_encode)
                    .header("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36").method(Method.POST).timeout(3000);
            for (Map.Entry<String, String> entry : cookie.entrySet()) {
                connect.cookie(entry.getKey(), entry.getValue());
            }
            Response response = connect.execute();
            String dataStr = response.parse().text();
            // 把JS中的加密算法用Java写一遍：
            String scode = dataStr.split("#")[0];
            String sxh = dataStr.split("#")[1];
            String code = username + "%%%" + password;
            String encoded = "";
            for (int i = 0; i < code.length(); i++) {
                if (i < 20) {
                    encoded = encoded + code.substring(i, i + 1)
                            + scode.substring(0, Integer.parseInt(sxh.substring(i, i + 1)));
                    scode = scode.substring(Integer.parseInt(sxh.substring(i, i + 1)), scode.length());
                } else {
                    encoded = encoded + code.substring(i, code.length());
                    i = code.length();
                }
            }
            return encoded;
        } catch (IOException e) {

        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        JsoupSafeCode jsoupSafeCode = new JsoupSafeCode();
        jsoupSafeCode.getSafeCode();
        jsoupSafeCode.initLogin();
//        Document document = Jsoup.connect("http://jwxt.gdufe.edu.cn/jsxsd/xskb/xskb_list.do").cookies(jsoupSafeCode.cookie).get();
//        System.out.println(jsoupSafeCode.cookie);
//        Element table = document.getElementById("kbtable");
//        Elements divkbcontent = table.select("div.kbcontent");
//
//        String studentid = "18251104126";
//
//        String coursename = "";
//        String teacher = "";
//        String week = "";
//        String position = "";
//        String count = "";
//        for(Element div : divkbcontent) {
//            Elements fonts = div.select("font");
//            if (fonts.size() > 0) {
//                String inforStr = div.text();
//                String courseid = "111111";
//                String[] info = inforStr.split(" ");
//                coursename = info[0];
//                teacher = info[1];
//                week = info[2];
//                position = info[3];
//                count = info[4];
//                //Course course = new Course(studentid, courseid, coursename, teacher, week, position, count);
//                //System.out.println(course.toString());
//            }
//        }
    }
}
