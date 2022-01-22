package com.waiterxiaoyy.backandroiddesign.utils.jsoup;

import com.waiterxiaoyy.backandroiddesign.entity.QuaDeve;
import com.waiterxiaoyy.backandroiddesign.entity.QuaDeveInfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/19 17:13
 */
public class QuaDeveJsoup {
    public static List<QuaDeveInfo> getQuaDeve(String studentId) throws IOException {
        String userName = studentId;
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

        Connection connection1 = Jsoup.connect("http://sztz.gdufe.edu.cn/sztz/student/authentication/authentication/authenticationList.jsp")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.GET)
                .cookies(cookie)
                .timeout(1500);
        Connection.Response response1 = connection1.execute();
        Document document = response1.parse();
        Element form = document.getElementById("pageForm");
        Elements inputs = form.getElementsByTag("input");
        String userId = inputs.get(0).attr("value");

        Map<String, String> type = new HashMap<String, String>();

        type.put("思想品德素质", "3681");
        type.put("创新创业素质", "3682");
        type.put("文化艺术素质", "3683");
        type.put("身心素质", "3684");
        type.put("技能素质", "3685");


        Map<String, String> data1 = new HashMap<String, String>();
        data1.put("userId", userId);
        data1.put("conditionSelector", "itemTypeSpan");
        data1.put("itemTypeId", "");
        data1.put("beginTime", "");
        data1.put("endTime", "");
        data1.put("classAuditStatus", "");
        data1.put("collegeAuditStatus", "");
        data1.put("schoolAuditStatus", "");
        data1.put("hasComplaint", "-1");
        data1.put("itemName", "");
        data1.put("search", "查询");
        data1.put("Submit3", "查询");
        data1.put("listTableClass", "HiddenList");
        data1.put("itemPerPage", "30");
        data1.put("currentPage", "1");
        data1.put("sort.sortColumn", "participant.grade");
        data1.put("sort.ascending", "false");

        Connection connection2 = null;
        Connection.Response response2 = null;
        Document document2 = null;

        List<QuaDeveInfo> quaDeveInfoList = new ArrayList<QuaDeveInfo>();

        for(String typeStr : type.keySet()) {
            QuaDeveInfo quaDeveInfo = new QuaDeveInfo();
            quaDeveInfo.setQueDeveType(typeStr);
            data1.put("itemTypeId", type.get(typeStr));
            connection2 = Jsoup.connect("http://sztz.gdufe.edu.cn/sztz/student/authentication/authentication/authenticationList.jsp")
                    .header("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                    .method(Connection.Method.GET)
                    .cookies(cookie)
                    .data(data1)
                    .timeout(2000);
            response2 = connection2.execute();
            document2 = response2.parse();

            Element table = document2.getElementById("ListTable");
//        System.out.println(table);

            Elements trs = table.getElementsByTag("tr");
            double total = 0;
            List<QuaDeve> quaDeveList = new ArrayList<QuaDeve>();
            for (int i = 1; i < trs.size(); i++) {
                if (trs.get(i).getElementsByAttributeValue("type", "checkbox").size() > 0) {
                    Elements tds = trs.get(i).getElementsByTag("td");
                    QuaDeve quaDeve = new QuaDeve();
                    quaDeve.setActivity(tds.get(1).text());
                    quaDeve.setCredit(Double.parseDouble(tds.get(4).text()));

                    quaDeve.setStatus(tds.get(8).text());
                    if(quaDeve.getStatus().equals("通过")) {
                        total += quaDeve.getCredit();
                    }
                    quaDeveList.add(quaDeve);
                }
            }
            quaDeveInfo.setQueDeveList(quaDeveList);
            BigDecimal bd= new BigDecimal(total);
            quaDeveInfo.setTotal(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            quaDeveInfoList.add(quaDeveInfo);
        }
        return quaDeveInfoList;
    }
}
