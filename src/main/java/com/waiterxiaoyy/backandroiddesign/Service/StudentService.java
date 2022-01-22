package com.waiterxiaoyy.backandroiddesign.Service;

import com.waiterxiaoyy.backandroiddesign.utils.front.*;
import com.waiterxiaoyy.backandroiddesign.utils.vo.DataVo;
import com.waiterxiaoyy.backandroiddesign.utils.vo.GradeVo;

public interface StudentService {
    DataVo getStudentInfo(LoginForm loginForm);

    GradeVo getStudentGrade(GetGradeInfo getGradeInfo);

    DataVo getStudentCourse(GetCourseInfo getCourseInfo);

    DataVo getStudentHaveStudied(GetHaveStudiedInfo getHaveStudiedInfo);

    DataVo getStuQuaDeve(String studentId);

    DataVo getExamTime(GetExamTimeInfo getExamTimeInfo);

    DataVo creatQuadeve(String studentid, String quadeveid);
}
