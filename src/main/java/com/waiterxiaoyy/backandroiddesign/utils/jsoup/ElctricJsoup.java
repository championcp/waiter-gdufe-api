package com.waiterxiaoyy.backandroiddesign.utils.jsoup;

import com.waiterxiaoyy.backandroiddesign.entity.Electric;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/17 10:26
 */
public class ElctricJsoup {
    public static Map<String, String> cookie;
    public static List<Electric> getEletric(String buildingId, String roomName) throws IOException, ParseException {
        Map<String, String> data = new HashMap<String, String>();

        data.put("buildingId", buildingId);
        data.put("roomName", roomName);

        Connection connection = Jsoup.connect("http://202.116.48.108:8090/sdms-select/webSelect/roomFillLogView.do")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.POST)
                .data(data)
                .timeout(3000);

        Connection.Response response = connection.execute();
        cookie = response.cookies();

        Connection connection1 = Jsoup.connect("http://202.116.48.108:8090/sdms-select/webSelect/welcome2.jsp")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.GET)
                .cookies(cookie)
                .timeout(3000);
        Connection.Response response1 = connection1.execute();
        Document document = response1.parse();
//        System.out.println(document);

        Element usedEleDiv = document.getElementById("usedEleDiv");
//        System.out.println(usedEleDiv);

        Elements trs = usedEleDiv.getElementsByTag("tr");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        List<Electric> electricList = new ArrayList<>();
        if(trs.size() > 1) {
            for(int i = 1; i < trs.size(); i++) {
//                System.out.println(trs.get(i));
                Elements tds = trs.get(i).getElementsByTag("td");
                Electric electric = new Electric();
                electric.setPosition(tds.get(0).text());
                electric.setElectric(tds.get(3).text());
                electric.setBill(tds.get(4).text());
                electric.setMoney(tds.get(5).text());
                String time = tds.get(6).text();
                Date date = simpleDateFormat.parse(time);
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                electric.setTime(simpleDateFormat.format(date));
                electricList.add(electric);
            }
        }
        return electricList;
    }
}
