package com.waiterxiaoyy.backandroiddesign.utils.jsoup;


import com.waiterxiaoyy.backandroiddesign.entity.Explain;
import com.waiterxiaoyy.backandroiddesign.entity.Grade;
import com.waiterxiaoyy.backandroiddesign.entity.HaveStudied;
import com.waiterxiaoyy.backandroiddesign.entity.SelectiveCourse;
import com.waiterxiaoyy.backandroiddesign.utils.poi.SelectiveCoursePoi;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/17 20:03
 */

public class SelectiveCourseJsoup {
    public static String toNum(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static List<HaveStudied> getHaveStudied(String studentId, String password) throws IOException {
        Map<String, HaveStudied> haveStudiedMap = new HashMap<String, HaveStudied>();
        Map<String, String> data = new HashMap<String, String>();

        InitLogin initLogin = new InitLogin();
        initLogin.setUsername(studentId);
        initLogin.setPassword(password);
        initLogin.getCookie();
        initLogin.initLogin();

        double gpaUp = 0;
        double creditTotal = 0;


        //设置请求体参数
        data.put("kksj", "");
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

        //解析文档
        //Document document = Jsoup.parse(file, "UTF-8", "");

        Map<String, SelectiveCourse> selectiveCourseMap = SelectiveCoursePoi.getSelectiveCourse();

        //获取课程成绩
        Element table = document.getElementById("dataList");
        Elements trs = table.getElementsByTag("tr");
        Grade grade = null;
        for (int i = 0; i < trs.size(); i++) {
            if (i > 0) {
                Element tr = trs.get(i);
                Elements tds = tr.getElementsByTag("td");
                grade = new Grade();
                grade.setStudentid(studentId);
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

                String scoreType = grade.getCoursecategory() + grade.getCourseNature();

                String coursetype = "";
                if (scoreType.equals("选修通识课")) {
                    if (selectiveCourseMap.get(grade.getCourse()) == null) {
                        coursetype = "选修通识课-未知模块";
                    } else
                        coursetype = "选修通识课-" + selectiveCourseMap.get(grade.getCourse()).getCoursetype();
                } else {
                    coursetype = scoreType;
                }


                if (haveStudiedMap.containsKey(coursetype)) {
                    HaveStudied haveStudied = haveStudiedMap.get(coursetype);
                    List<Grade> scoreList = haveStudied.getScoreList();
                    scoreList.add(grade);
                    haveStudied.setScoreList(scoreList);
                    haveStudied.setTotal(haveStudied.getTotal() + grade.getCredit());
                    haveStudiedMap.put(coursetype, haveStudied);
                } else {
                    HaveStudied haveStudied = new HaveStudied();
                    haveStudied.setCoursetype(coursetype);
                    List<Grade> scoreList = new ArrayList<Grade>();
                    scoreList.add(grade);
                    haveStudied.setScoreList(scoreList);
                    haveStudied.setTotal(grade.getCredit());
                    haveStudiedMap.put(coursetype, haveStudied);
                }
            }
        }
        List<HaveStudied> haveStudiedList = new ArrayList<>();
        for (HaveStudied haveStudied : haveStudiedMap.values()) {
            haveStudiedList.add(haveStudied);
        }
        return haveStudiedList;
    }


    public static void main(String[] args) throws IOException {
        Map<String, HaveStudied> haveStudiedMap = new HashMap<String, HaveStudied>();
        Map<String, String> data = new HashMap<String, String>();

        InitLogin initLogin = new InitLogin();
        initLogin.getCookie();
        initLogin.initLogin();

        double gpaUp = 0;
        double creditTotal = 0;


        //解析文档
        data.put("kksj", "");
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

        //解析文档
        //Document document = Jsoup.parse(file, "UTF-8", "");

        Map<String, SelectiveCourse> selectiveCourseMap = SelectiveCoursePoi.getSelectiveCourse();

        //获取课程成绩
        Element table = document.getElementById("dataList");
        Elements trs = table.getElementsByTag("tr");
        Grade grade = null;
        for (int i = 0; i < trs.size(); i++) {
            if (i > 0) {
                Element tr = trs.get(i);
                Elements tds = tr.getElementsByTag("td");
                grade = new Grade();
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

                String scoreType = grade.getCoursecategory() + grade.getCourseNature();

                String coursetype = "";
                if(scoreType.equals("选修通识课")) {
                     if(selectiveCourseMap.get(grade.getCourse()) == null) {
                         coursetype = "选修通识课-未知模块";
                     }
                     else
                         coursetype = "选修通识课-" + selectiveCourseMap.get(grade.getCourse()).getCoursetype();
                } else {
                     coursetype = scoreType;
                }


                if(haveStudiedMap.containsKey(coursetype)) {
                    HaveStudied haveStudied = haveStudiedMap.get(coursetype);
                    List<Grade> scoreList = haveStudied.getScoreList();
                    scoreList.add(grade);
                    haveStudied.setScoreList(scoreList);
                    haveStudied.setTotal(haveStudied.getTotal() + grade.getCredit());
                    haveStudiedMap.put(coursetype, haveStudied);
                } else {
                    HaveStudied haveStudied = new HaveStudied();
                    haveStudied.setCoursetype(coursetype);
                    List<Grade> scoreList = new ArrayList<Grade>();
                    scoreList.add(grade);
                    haveStudied.setScoreList(scoreList);
                    haveStudied.setTotal(grade.getCredit());
                    haveStudiedMap.put(coursetype, haveStudied);
                }
            }
        }
        for(HaveStudied haveStudied : haveStudiedMap.values()) {
            System.out.println(haveStudied);
        }
    }
}
