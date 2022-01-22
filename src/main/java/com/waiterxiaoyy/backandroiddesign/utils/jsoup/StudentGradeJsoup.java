package com.waiterxiaoyy.backandroiddesign.utils.jsoup;

import com.waiterxiaoyy.backandroiddesign.entity.Explain;
import com.waiterxiaoyy.backandroiddesign.entity.Gpa;
import com.waiterxiaoyy.backandroiddesign.entity.Grade;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/16 21:07
 */
public class StudentGradeJsoup {

    public static String toNum(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static Map<String, List> getGrade(String studentId, String password, String termTime) throws IOException {

        Map<String, List> listMap = new HashMap<>();
        List<Grade> gradeList = new ArrayList<>();
        List<Explain> explainList = new ArrayList<>();
        List<Gpa> gpaList = new ArrayList<>();

        InitLogin initLogin = new InitLogin();
        initLogin.setUsername(studentId);
        initLogin.setPassword(password);
        initLogin.getCookie();
        initLogin.initLogin();

        double gpaUp = 0;
        double creditTotal = 0;

//        JsoupSafeCode jsoupSafeCode = new JsoupSafeCode();
//        jsoupSafeCode.getSafeCode();
//        jsoupSafeCode.initLogin();

        //解析文档
        Map<String, String> data = new HashMap<String, String>();
        data.put("kksj", termTime);
        data.put("kcxz", "");
        data.put("kcmc", "");
        data.put("fxkc", "0");
        data.put("xsfs", "all");
        Connection connection = Jsoup.connect("http://jwxt.gdufe.edu.cn/jsxsd/kscj/cjcx_list")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.POST)
                .data(data)
                .timeout(3000)
                .cookies(initLogin.cookie);
        Connection.Response response = connection.execute();
        Document document = response.parse();

        Explain explain = new Explain();

        //解析文档
        //Document document = Jsoup.parse(file, "UTF-8", "");

        //获取学生学号
        Element personalInfo = document.getElementById("Top1_divLoginName");
        explain.setStudentid(toNum(personalInfo.text()));

        //获取个人修读信息
        Elements spans = document.select("span");

        //打印个人修读信息
        for (int i = 0; i < spans.size(); i++) {
            Element span = spans.get(i);
            if (i == 4) {
                explain.setTotalCredit(Double.parseDouble(span.text()));
            }
            if (i == 5) {
                explain.setNoNeedCredit(Double.parseDouble(span.text()));
            }
            if (i == 6) {
                explain.setReceivedCredit(Double.parseDouble(span.text()));
            }
            if (i == 7) {
                explain.setPendingCredit(Double.parseDouble(span.text()));
            }
            if (i == 8) {
                explain.setMajorGPA(Double.parseDouble(span.text().substring(0, span.text().length() - 1)));
            }
            if (i == 9) {
                explain.setMinorGPA(Double.parseDouble(span.text().substring(0, span.text().length() - 1)));
                break;
            }
        }
        explainList.add(explain);
        listMap.put("explain", explainList);

        //获取课程成绩
        Element table = document.getElementById("dataList");
        Elements trs = table.getElementsByTag("tr");

        for (int i = 0; i < trs.size(); i++) {

            if (i > 0) {
                Grade grade = new Grade();;
                Element tr = trs.get(i);
                Elements tds = tr.getElementsByTag("td");
                grade.setStudentid(explain.getStudentid());
                for (int j = 1; j < 15; j++) {
                    Element td = tds.get(j);
                    if (j == 1) {
                        grade.setTerm(td.text());
                    }
                    if (j == 2) {
                        grade.setCourseid(td.text());
                    }
                    if (j == 3) {
                        grade.setCourse(td.text());
                    }
                    if (j == 4) {
                        if (td.text().equals("")) {
                            grade.setRegularScore(0);
                        } else {
                            grade.setRegularScore(Double.parseDouble(td.text()));
                        }
                    }
                    if (j == 5) {
                        if (td.text().equals("")) {
                            grade.setExperimentScore(0);
                        } else {
                            grade.setExperimentScore(Double.parseDouble(td.text()));
                        }
                    }
                    if (j == 6) {
                        if (td.text().equals("")) {
                            grade.setFinalScore(0);
                        } else {
                            grade.setFinalScore(Double.parseDouble(td.text()));
                        }
                    }
                    if (j == 7) {
                        if (td.text().equals("")) {
                            grade.setTotalmark(0);
                        } else if (td.text().equals("优")) {
                            grade.setTotalmark(95);
                        } else if (td.text().equals("良")) {
                            grade.setTotalmark(85);
                        } else if (td.text().equals("中")) {
                            grade.setTotalmark(75);
                        } else if (td.text().equals("及格")) {
                            grade.setTotalmark(65);
                        } else if (td.text().equals("不及格")) {
                            grade.setTotalmark(0);
                        } else {
                            grade.setTotalmark(Double.parseDouble(td.text()));
                        }
                    }
                    if (j == 8) {
                        grade.setCredit(Double.parseDouble(td.text()));
                    }
                    if (j == 11) {
                        grade.setCoursecategory(td.text());
                    }
                    if (j == 12) {
                        grade.setCourseNature(td.text());
                    }
                    if (j == 14) {
                        grade.setExamNature(td.text());
                    }
                }
                gpaUp += (grade.getTotalmark() - 50) / 10 * grade.getCredit();
                creditTotal += grade.getCredit();
                gradeList.add(grade);
            }
        }

//        System.out.println("学年绩点:" + gpaUp / creditTotal);
        Gpa gpa = new Gpa();
        BigDecimal bigDecimal = new BigDecimal(gpaUp / creditTotal);
        gpa.setGpa(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        gpaList.add(gpa);
        listMap.put("grade", gradeList);
        listMap.put("gpa", gpaList);
        return listMap;

    }
}
