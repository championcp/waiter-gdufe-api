package com.waiterxiaoyy.backandroiddesign.utils.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddQuaDeveJsoup {
    public static boolean addQuaDeve(String studentid, String quadeveid) throws IOException {
        String url = "http://sztz.gdufe.edu.cn/sztz/student/authentication/authentication/authenticationAdd.jsp?itemId=" + quadeveid;

        String userName = studentid;
        Map<String, String> cookie;
        Map<String, String> data = new HashMap<String, String>();
        data.put("j_username", userName);
        data.put("j_password", userName);
        Connection connection = Jsoup.connect("http://sztz.gdufe.edu.cn/sztz/Login")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.POST)
                .data(data)
                .timeout(1000);

        Connection.Response response = connection.execute();
        cookie = response.cookies();

        Connection connection1 = Jsoup.connect(url)
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.GET)
                .cookies(cookie)
                .timeout(1500);
        Connection.Response response1 = connection1.execute();
        Document document = response1.parse();
//        System.out.println(document);

        String session_token = "";
        String itemId = "";
        String modifying= "modifying";
        String itemParticipantRoleId = "154";
        String achievementId = "506357";
        String credit = "";
        String optType = "9010";
        String beginTime = "";
        String endTime = "";
        String activityContent = "";
        String applyLast = "确定";

        Elements inputs = document.getElementsByTag("input");

        session_token = inputs.get(0).attr("value");
        itemId = inputs.get(1).attr("value");
        modifying = inputs.get(2).attr("value");

        Element table = document.getElementsByTag("table").get(1);
        Elements tds = table.getElementsByAttributeValue("class", "tittle3");

        credit = tds.get(3).text();
        String time = tds.get(6).text();
        if(time.split("－").length > 1) {
            beginTime = time.split("－")[0];
            endTime = time.split("－")[1];
        } else {
            beginTime = endTime = time;
        }

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("session_token", session_token);
        infoMap.put("itemId", itemId );
        infoMap.put("modifying", modifying);
        infoMap.put("itemParticipantRoleId", itemParticipantRoleId);
        infoMap.put("achievementId", achievementId);
        infoMap.put("credit", credit);
        infoMap.put("optType", optType);
        infoMap.put("beginTime", beginTime);
        infoMap.put("endTime", endTime);
        infoMap.put("activityContent", activityContent);
        infoMap.put("applyLast", applyLast);


        Connection connection2 = Jsoup.connect(url)
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.POST)
                .cookies(cookie)
                .data(infoMap)
                .timeout(1500);
        Connection.Response response2 = connection2.execute();
        return true;
    }

    public static void main(String[] args) throws IOException {
        String quadeveid = "231";

        String url = "http://sztz.gdufe.edu.cn/sztz/student/authentication/authentication/authenticationAdd.jsp?itemId=" +quadeveid;

        String userName = "18251104126";
        Map<String, String> cookie;
        Map<String, String> data = new HashMap<String, String>();
        data.put("j_username", userName);
        data.put("j_password", userName);
        Connection connection = Jsoup.connect("http://sztz.gdufe.edu.cn/sztz/Login")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.POST)
                .data(data)
                .timeout(1000);

        Connection.Response response = connection.execute();
        cookie = response.cookies();

        Connection connection1 = Jsoup.connect(url)
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.GET)
                .cookies(cookie)
                .timeout(1500);
        Connection.Response response1 = connection1.execute();
        Document document = response1.parse();
//        System.out.println(document);

        String session_token = "";
        String itemId = "";
        String modifying= "modifying";
        String itemParticipantRoleId = "154";
        String achievementId = "506357";
        String credit = "";
        String optType = "9010";
        String beginTime = "";
        String endTime = "";
        String activityContent = "";
        String applyLast = "确定";

        Elements inputs = document.getElementsByTag("input");

        session_token = inputs.get(0).attr("value");
        itemId = inputs.get(1).attr("value");
        modifying = inputs.get(2).attr("value");

        Element table = document.getElementsByTag("table").get(1);
        Elements tds = table.getElementsByAttributeValue("class", "tittle3");

        credit = tds.get(3).text();
        String time = tds.get(6).text();
        if(time.split("－").length > 1) {
            beginTime = time.split("－")[0];
            endTime = time.split("－")[1];
        } else {
            beginTime = endTime = time;
        }
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("session_token", session_token);
        infoMap.put("itemId", itemId );
        infoMap.put("modifying", modifying);
        infoMap.put("itemParticipantRoleId", itemParticipantRoleId);
        infoMap.put("achievementId", achievementId);
        infoMap.put("credit", credit);
        infoMap.put("optType", optType);
        infoMap.put("beginTime", beginTime);
        infoMap.put("endTime", endTime);
        infoMap.put("activityContent", activityContent);
        infoMap.put("applyLast", applyLast);


        Connection connection2 = Jsoup.connect(url)
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.POST)
                .cookies(cookie)
                .data(infoMap)
                .timeout(1500);
        Connection.Response response2 = connection2.execute();
        System.out.println(response2);
        Document document2 = response2.parse();
        System.out.println(document2);
    }
}
