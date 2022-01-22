package com.waiterxiaoyy.backandroiddesign.Service.impl;

import com.waiterxiaoyy.backandroiddesign.Service.StudentService;
import com.waiterxiaoyy.backandroiddesign.entity.*;
import com.waiterxiaoyy.backandroiddesign.utils.front.*;
import com.waiterxiaoyy.backandroiddesign.utils.jsoup.*;
import com.waiterxiaoyy.backandroiddesign.utils.vo.DataVo;
import com.waiterxiaoyy.backandroiddesign.utils.vo.GradeVo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/7 22:37
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Override
    public DataVo getStudentInfo(LoginForm loginForm) {
        DataVo dataVo = new DataVo();
        dataVo.setCode(202);
        dataVo.setMsg("获取学生信息失败");
        try {
            List<StudentInfo> studentInfoList = new ArrayList<>();
            StudentInfo studentInfo = StudentInfoJsoup.getStudentInfo(loginForm.getStudentId(), loginForm.getPassword());
            if(studentInfo == null) {
                dataVo.setCode(203);
                dataVo.setMsg("账号或密码错误");
            } else {
                studentInfoList.add(studentInfo);
                dataVo.setCode(200);
                dataVo.setMsg("获取学生信息成功");
            }

            dataVo.setData(studentInfoList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return dataVo;
        }
    }

    @Override
    public GradeVo getStudentGrade(GetGradeInfo getGradeInfo) {
        GradeVo gradeVo = new GradeVo();
        gradeVo.setCode(202);
        gradeVo.setMsg("获取学生成绩信息失败");
        try {
            Map<String, List> listMap = StudentGradeJsoup.getGrade(getGradeInfo.getStudentId(), getGradeInfo.getPassword(), getGradeInfo.getTermTime());
            List<Grade> gradeList = listMap.get("grade");
            List<Explain> explainList = listMap.get("explain");
            List<Gpa> gpaList = listMap.get("gpa");
            gradeVo.setCode(200);
            gradeVo.setMsg("获取学生成绩信息成功");
            gradeVo.setCount((long) gradeList.size());
            gradeVo.setGrade(gradeList);
            gradeVo.setExplain(explainList);
            gradeVo.setGpa(gpaList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return gradeVo;
        }
    }

    @Override
    public DataVo getStudentCourse(GetCourseInfo getCourseInfo) {
        DataVo dataVo = new DataVo();
        dataVo.setCode(202);
        dataVo.setMsg("获取学生课程失败");
        try {
            List<Course> courseList = StudentCourseJsoup.getStudentCourse(getCourseInfo.getStudentId(), getCourseInfo.getPassword(), getCourseInfo.getTermTime());
            dataVo.setCode(200);
            dataVo.setMsg("获取学生课程成功");
            dataVo.setData(courseList);
            dataVo.setCount((long) courseList.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return dataVo;
        }
    }

    @Override
    public DataVo getStudentHaveStudied(GetHaveStudiedInfo getHaveStudiedInfo) {
        DataVo dataVo = new DataVo();
        dataVo.setCode(202);
        dataVo.setMsg("获取已修模块课程学分失败");
        try {
            List<HaveStudied> haveStudiedList = SelectiveCourseJsoup.getHaveStudied(getHaveStudiedInfo.getStudentId(), getHaveStudiedInfo.getPassword());
            dataVo.setCode(200);
            dataVo.setMsg("获取已修模块课程学分成功");
            dataVo.setData(haveStudiedList);
            double total = 0L;
            for(HaveStudied haveStudied : haveStudiedList) {
                total += haveStudied.getTotal();
            }
            dataVo.setCount((long)total);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return dataVo;
        }
    }

    @Override
    public DataVo getStuQuaDeve(String studentId) {
        DataVo dataVo = new DataVo();
        dataVo.setCode(202);
        dataVo.setMsg("获取素质拓展修读情况失败");
        try {
            List<QuaDeveInfo> quaDeveInfoList = QuaDeveJsoup.getQuaDeve(studentId);
            dataVo.setCode(200);
            dataVo.setMsg("获取素质拓展修读情况成功");
            dataVo.setData(quaDeveInfoList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return dataVo;
        }

    }

    @Override
    public DataVo getExamTime(GetExamTimeInfo getExamTimeInfo) {
        DataVo dataVo = new DataVo();
        dataVo.setCode(202);
        dataVo.setMsg("获取学生考试时间失败");
        try {
            List<ExamTime> examTimeList = ExamJsoup.getExamTime(getExamTimeInfo.getStudentId(), getExamTimeInfo.getPassword(), getExamTimeInfo.getTermTime());
            dataVo.setCode(200);
            dataVo.setMsg("获取学生考试时间成功");
            dataVo.setData(examTimeList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return dataVo;
        }
    }

    @Override
    public DataVo creatQuadeve(String studentid, String quadeveid) {
        DataVo dataVo = new DataVo();
        dataVo.setCode(202);
        dataVo.setMsg("增加素拓活动失败");
        try {
            if(AddQuaDeveJsoup.addQuaDeve(studentid, quadeveid) == true) {
                dataVo.setCode(200);
                dataVo.setMsg("增加素拓活动成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return dataVo;
        }
    }
}
