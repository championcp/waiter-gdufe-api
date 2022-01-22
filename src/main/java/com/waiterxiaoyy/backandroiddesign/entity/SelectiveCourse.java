package com.waiterxiaoyy.backandroiddesign.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/17 19:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectiveCourse {
    private String coursetype;
    private List<String> courseid;
    private String course;
    private List<Integer> credit;
    private String courseCollege;
}
