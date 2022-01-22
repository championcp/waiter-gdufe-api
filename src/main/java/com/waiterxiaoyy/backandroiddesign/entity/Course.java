package com.waiterxiaoyy.backandroiddesign.entity;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/17 9:27
 */
@Data
public class Course {
        private String studentid;
        private String coureseid;
        private String course;
        private String teacher;
    private List<String> week;
    private List<String> position;
    private List<String> count;
    private List<String> weekCountPositon;

    public Course(String studentid, String coureseid, String course, String teacher, List<String> week, List<String> position, List<String> count) {
        this.studentid = studentid;
        this.coureseid = coureseid;
        this.course = course;
        this.teacher = teacher;
        this.week = week;
        this.position = position;
        this.count = count;
    }
}
