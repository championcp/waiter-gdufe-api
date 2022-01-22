package com.waiterxiaoyy.backandroiddesign.entity;

import lombok.Data;

import java.util.List;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/17 20:07
 */
@Data
public class HaveStudied {
    private String coursetype;
    private List<Grade> scoreList;
    private Double total;

}
