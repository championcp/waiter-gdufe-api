package com.waiterxiaoyy.backandroiddesign.utils.jsoup;

import com.waiterxiaoyy.backandroiddesign.entity.StudentInfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/7 21:21
 */
public class StudentInfoJsoup {

    public static StudentInfo getStudentInfo(String username, String password) throws ParseException, IOException {

        InitLogin initLogin = new InitLogin();
        initLogin.setUsername(username);
        initLogin.setPassword(password);
        initLogin.getCookie();
        initLogin.initLogin();

        Connection connection = Jsoup.connect("http://jwxt.gdufe.edu.cn/jsxsd/grxx/xsxx")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.GET)
                .timeout(3000)
                .cookies(initLogin.cookie);
        Connection.Response response = connection.execute();
        Document document = response.parse();

        Element table = document.getElementById("xjkpTable");
        if(table == null) {
            return null;
        }
        Elements trs = table.select("tr");

        StudentInfo studentInfo = new StudentInfo();

        // 获取学院、专业、学制、学号
        Element tr1 = trs.get(2);
        studentInfo.setCollege(tr1.getElementsByTag("td").get(0).text().substring(3));
        studentInfo.setMajor(tr1.getElementsByTag("td").get(1).text().substring(3));
        studentInfo.setLearnYear(Integer.parseInt(tr1.getElementsByTag("td").get(2).text().substring(3)));
        studentInfo.setStudentId(tr1.getElementsByTag("td").get(4).text().substring(3));

        // 获取学生的姓名，性别，姓名拼音
        Element tr2 = trs.get(3);
        studentInfo.setStudentName(tr2.getElementsByTag("td").get(1).text().replace(Jsoup.parse("&nbsp;").text(), " ").trim());
        studentInfo.setStudentSex(tr2.getElementsByTag("td").get(3).text().replace(Jsoup.parse("&nbsp;").text(), " ").trim());
        studentInfo.setStudentNamePy(tr2.getElementsByTag("td").get(5).text().replace(Jsoup.parse("&nbsp;").text(), " ").trim());

        // 获取学生生日
        Element tr3 = trs.get(4);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date birthday = simpleDateFormat.parse(tr3.getElementsByTag("td").get(1).text().replace(Jsoup.parse("&nbsp;").text(), " ").trim());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        studentInfo.setBirthday(simpleDateFormat.format(birthday));

        // 获取学生政治面貌
        Element tr4 = trs.get(5);
        studentInfo.setPoliticalStatus(tr4.getElementsByTag("td").get(3).text().replace(Jsoup.parse("&nbsp;").text(), " ").trim());

        // 获取学生的籍贯
        Element tr5 = trs.get(6);
        studentInfo.setHometown(tr5.getElementsByTag("td").get(1).text().replace(Jsoup.parse("&nbsp;").text(), " ").trim());

        // 获取学生的民族
        Element tr6 = trs.get(7);
        studentInfo.setNation(tr6.getElementsByTag("td").get(3).text().replace(Jsoup.parse("&nbsp;").text(), " ").trim());

        // 获取学生的电话
        Element tr7 = trs.get(10);
        studentInfo.setPhone(tr7.getElementsByTag("td").get(3).text().replace(Jsoup.parse("&nbsp;").text(), " ").trim());
        return studentInfo;
    }

    public static void main(String[] args) throws IOException, ParseException {
        String infoUrl = "http://jwxt.gdufe.edu.cn/jsxsd/grxx/xsxx";
        Map<String, String> data = new HashMap<String, String>();

        String username = "";
        String password = "";
        InitLogin initLogin = new InitLogin();
        initLogin.setUsername(username);
        initLogin.setPassword(password);
        initLogin.getCookie();
        initLogin.initLogin();

        Connection connection = Jsoup.connect("http://jwxt.gdufe.edu.cn/jsxsd/grxx/xsxx")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.GET)
                .timeout(3000)
                .cookies(initLogin.cookie);
        Connection.Response response = connection.execute();
        Document document = response.parse();

        Element table = document.getElementById("xjkpTable");

        Elements trs = table.select("tr");

        StudentInfo studentInfo = new StudentInfo();

        // 获取学院、专业、学制、学号
        Element tr1 = trs.get(2);
        studentInfo.setCollege(tr1.getElementsByTag("td").get(0).text().substring(3));
        studentInfo.setMajor(tr1.getElementsByTag("td").get(1).text().substring(3));
        studentInfo.setLearnYear(Integer.parseInt(tr1.getElementsByTag("td").get(2).text().substring(3)));
        studentInfo.setStudentId(tr1.getElementsByTag("td").get(4).text().substring(3));

        // 获取学生的姓名，性别，姓名拼音
        Element tr2 = trs.get(3);
        studentInfo.setStudentName(tr2.getElementsByTag("td").get(1).text());
        studentInfo.setStudentSex(tr2.getElementsByTag("td").get(3).text());
        studentInfo.setStudentNamePy(tr2.getElementsByTag("td").get(5).text());

        // 获取学生生日
        Element tr3 = trs.get(4);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date birthday = simpleDateFormat.parse(tr3.getElementsByTag("td").get(1).text());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        studentInfo.setBirthday(simpleDateFormat.format(birthday));

        // 获取学生政治面貌
        Element tr4 = trs.get(5);
        studentInfo.setPoliticalStatus(tr4.getElementsByTag("td").get(3).text());

        // 获取学生的籍贯
        Element tr5 = trs.get(6);
        studentInfo.setHometown(tr5.getElementsByTag("td").get(1).text());

        // 获取学生的民族
        Element tr6 = trs.get(7);
        studentInfo.setNation(tr6.getElementsByTag("td").get(3).text());

        // 获取学生的电话
        Element tr7 = trs.get(10);
        studentInfo.setPhone(tr7.getElementsByTag("td").get(3).text());

        System.out.println(studentInfo);
    }

}
