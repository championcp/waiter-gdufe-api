package com.waiterxiaoyy.backandroiddesign.utils.jsoup;

import com.waiterxiaoyy.backandroiddesign.entity.ExamTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExamJsoup {
    public static String toNum(String str) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static List<ExamTime> getExamTime(String studentId, String password, String termTime) throws IOException {
//        File file = new File("F:\\教务系统爬取\\exam.html");
//        Document document = Jsoup.parse(file, "UTF-8","");
        List<ExamTime> examTimeList = new ArrayList<>();
        InitLogin initLogin = new InitLogin();
        initLogin.setUsername(studentId);
        initLogin.setPassword(password);
        initLogin.getCookie();
        initLogin.initLogin();
        Map<String, String> data = new HashMap<String, String>();

        data.put("xnxqid", termTime);
        data.put("xqlb", "3");
        data.put("xqlbmc", "期末");
        Connection connection = Jsoup.connect("http://jwxt.gdufe.edu.cn/jsxsd/xsks/xsksap_list")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.POST)
                .data(data)
                .timeout(3000)
                .cookies(initLogin.cookie);
        Connection.Response response = connection.execute();
        Document document = response.parse();
//
//        Document document = Jsoup.connect("http://jwxt.gdufe.edu.cn/jsxsd/xsks/xsksap_list").cookies(initLogin.cookie).get();
//        System.out.println(document);
        //获取学生学号
        Element personalInfo = document.getElementById("Top1_divLoginName");
        String studentid = toNum(personalInfo.text());

        //获取考试时间table
        Element table = document.getElementById("dataList");
        Elements trs = table.select("tr");
        for(int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            ExamTime examTime = new ExamTime();
            examTime.setStudentid(studentid);
            for(int j = 1; j < tds.size() - 1; j++) {
                Element td = tds.get(j);
                if(j == 1) {
                    examTime.setCourseid(td.text());
                }
                if(j == 2) {
                    examTime.setCourse(td.text());
                }
                if(j == 3) {
                    examTime.setExamTime(td.text());
                }
                if(j == 4) {
                    examTime.setExamAddress(td.text());
                }
                if(j == 5) {
                    examTime.setExamClassroom(td.text());
                }
            }
            examTimeList.add(examTime);
        }
        return examTimeList;
    }
    public static void main(String[] args) throws IOException {
        File file = new File("F:\\教务系统爬取\\exam.html");
        Document document = Jsoup.parse(file, "UTF-8","");
//        JsoupSafeCode jsoupSafeCode = new JsoupSafeCode();
//        jsoupSafeCode.getSafeCode();
//        jsoupSafeCode.initLogin();
//        Map<String, String> data = new HashMap<String, String>();
//
//        data.put("xnxqid", "2019-2020-2");
//        data.put("xqlb", "3");
//        data.put("xqlbmc", "期末");
//        Connection connection = Jsoup.connect("http://jwxt.gdufe.edu.cn/jsxsd/xsks/xsksap_list")
//                .header("Accept",
//                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
//                .method(Connection.Method.POST)
//                .data(data)
//                .timeout(3000)
//                .cookies(jsoupSafeCode.cookie);
//        Connection.Response response = connection.execute();
//        Document document = response.parse();

//        Document document = Jsoup.connect("http://jwxt.gdufe.edu.cn/jsxsd/xsks/xsksap_list").cookies(jsoupSafeCode.cookie).get();
//        System.out.println(document);
        //获取学生学号
        Element personalInfo = document.getElementById("Top1_divLoginName");
        String studentid = toNum(personalInfo.text());

        //获取考试时间table
        Element table = document.getElementById("dataList");
        Elements trs = table.select("tr");
        for(int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            ExamTime examTime = new ExamTime();
            examTime.setStudentid(studentid);
            for(int j = 1; j < tds.size() - 1; j++) {
                Element td = tds.get(j);
                if(j == 1) {
                    examTime.setCourseid(td.text());
                }
                if(j == 2) {
                    examTime.setCourse(td.text());
                }
                if(j == 3) {
                    examTime.setExamTime(td.text());
                }
                if(j == 4) {
                    examTime.setExamAddress(td.text());
                }
                if(j == 5) {
                    examTime.setExamClassroom(td.text());
                }
            }
            System.out.println(examTime.toString());
        }
    }
}

