package com.waiterxiaoyy.backandroiddesign.controller;

import com.waiterxiaoyy.backandroiddesign.Service.StudentService;
import com.waiterxiaoyy.backandroiddesign.utils.front.*;
import com.waiterxiaoyy.backandroiddesign.utils.vo.DataVo;
import com.waiterxiaoyy.backandroiddesign.utils.vo.GradeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author :WaiterXiaoYY
 * @description: 教务系统接口控制层
 * @data :2020/12/7 22:36
 */
@RestController
@RequestMapping("/api")
@Api(description = "教务系统接口控制层")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/getstudentinfo")
    @ApiOperation("获取学生信息")
    public DataVo getStudentInfo(@RequestBody LoginForm loginForm) {
        return studentService.getStudentInfo(loginForm);
    }

    @PostMapping("/getstugrade")
    @ApiOperation("获取学生成绩")
    public GradeVo getStudentGrade(@RequestBody GetGradeInfo getGradeInfo) {
        return studentService.getStudentGrade(getGradeInfo);
    }

    @PostMapping("/getstucourse")
    @ApiOperation("获取学生课程")
    public DataVo getStudentCourse(@RequestBody GetCourseInfo getCourseInfo) {
        return studentService.getStudentCourse(getCourseInfo);
    }

    @PostMapping("/getstuhavestudied")
    @ApiOperation("获取已修各模块的课程学分情况")
    public DataVo getStudentHaveStudied(@RequestBody GetHaveStudiedInfo getHaveStudiedInfo) {
        return studentService.getStudentHaveStudied(getHaveStudiedInfo);
    }

    @GetMapping("getstuquadeve/{studentId}")
    @ApiOperation("获取素质拓展已修情况")
    public DataVo getStuQuaDeve(@PathVariable("studentId") String studentId) {
        return studentService.getStuQuaDeve(studentId);
    }

    @PostMapping("getexamtime")
    @ApiOperation("获取学生的考试时间")
    public DataVo getExamTime(@RequestBody GetExamTimeInfo getExamTimeInfo) {
        return studentService.getExamTime(getExamTimeInfo);
    }

    @GetMapping("creatstuquadeve/{studentid}/{quadeveid}")
    @ApiOperation("")
    public DataVo creatQuadeve(@PathVariable("studentid") String studentid, @PathVariable("quadeveid") String quadeveid) {
        DataVo dataVo = new DataVo();
        dataVo = studentService.creatQuadeve(studentid, quadeveid);
        return dataVo;

    }
}
