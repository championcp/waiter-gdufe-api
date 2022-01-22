package com.waiterxiaoyy.backandroiddesign.entity;

import lombok.Data;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/16 21:10
 */
@Data
public class Grade {
    private String studentid;
    private String term;
    private String courseid;
    private String course;
    private double regularScore;
    private double experimentScore;
    private double finalScore;
    private double totalmark;
    private double credit;
    private String coursecategory;
    private String courseNature;
    private String examNature;
}
