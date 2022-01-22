package com.waiterxiaoyy.backandroiddesign.utils.jsoup;

import com.waiterxiaoyy.backandroiddesign.entity.Course;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/17 9:27
 */
public class StudentCourseJsoup {
    private static Map<String, Course> termCourseMap = new HashMap<String, Course>();
    public static String toNum(String str) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    public static List<Course> getStudentCourse(String studnetId, String password, String termTime) throws IOException {
        InitLogin initLogin = new InitLogin();
        initLogin.setUsername(studnetId);
        initLogin.setPassword(password);
        initLogin.getCookie();
        initLogin.initLogin();
        Map<String, String> data = new HashMap<String, String>();

        data.put("cj0701id", "");
        data.put("zc", "");
        data.put("demo", "");
        data.put("xnxq01id", termTime);
        data.put("sfFD", "1");
        Connection connection = Jsoup.connect("http://jwxt.gdufe.edu.cn/jsxsd/xskb/xskb_list.do")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                .method(Connection.Method.POST)
                .data(data)
                .timeout(3000)
                .cookies(initLogin.cookie);
        Connection.Response response = connection.execute();
        Document document = response.parse();
        //获取学生学号
        Element personalInfo = document.getElementById("Top1_divLoginName");
        String studentid = toNum(personalInfo.text());

        Element table = document.getElementById("kbtable");
        Elements divkbcontent = table.select("div.kbcontent");
        String coursename = "";
        String teacher = "";
        String courseid = "";

        //获取选修课
//        SelectiveCoursePoi selectiveCoursePoi = new SelectiveCoursePoi();
//        HashMap<String, SelectiveCourse> selectiveCourseMap = selectiveCoursePoi.getSelectiveCourse();

        for (Element div : divkbcontent) {
            Elements fonts = div.select("font");
            if (fonts.size() > 0) {
                String inforStr = div.text();
                String infoStrSplit[] = inforStr.split(" --------------------- ");
                for (int k = 0; k < infoStrSplit.length; k++) {
                    String[] info = infoStrSplit[k].split(" ");
                    coursename = info[0];
//                courseid = map.get(coursename);
                    courseid = UUID.randomUUID().toString().substring(0, 10);
                    teacher = info[1];
                    Course course = null;
                    if (termCourseMap.containsKey(coursename) == true) {
                        course = termCourseMap.get(coursename);
                        List<String> week1 = course.getWeek();
                        week1.add(info[2]);
                        course.setWeek(week1);

                        List<String> position1 = course.getPosition();
                        position1.add(info[3]);
                        course.setPosition(position1);

                        List<String> count1 = course.getCount();
                        count1.add(info[4]);
                        course.setCount(count1);

                    } else if (termCourseMap.containsKey(coursename) == false) {
                        List<String> week = new ArrayList<String>();
                        List<String> position = new ArrayList<String>();
                        List<String> count = new ArrayList<String>();
                        week.add(info[2]);
                        position.add(info[3]);
                        count.add(info[4]);
                        course = new Course(studentid, courseid, coursename, teacher, week, position, count);
                    }
                    termCourseMap.put(coursename, course);
                }
            }
        }

        List<Course> courseList = new ArrayList<>();
        for (Course course : termCourseMap.values()) {
            if (course.getWeek().size() >= 1) {
                Set<String> set = new HashSet<String>();
                List<String> list = new ArrayList<String>();
                String array[] = new String[course.getWeek().size()];
                for (int i = 0; i < course.getWeek().size(); i++) {
                    array[i] = course.getWeek().get(i) + course.getCount().get(i) + course.getPosition().get(i);
                }
                Arrays.sort(array);
                for (int i = 0; i < array.length; i++) {
//                    System.out.println(array[i]);
                    if (set.contains(array[i]) == false) {
                        list.add(array[i]);
                    }
                    set.add(array[i]);
                }
                course.setWeekCountPositon(list);
            }
            courseList.add(course);
        }
        return courseList;
    }
}
