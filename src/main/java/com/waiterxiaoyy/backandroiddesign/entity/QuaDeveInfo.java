package com.waiterxiaoyy.backandroiddesign.entity;

import lombok.Data;

import java.util.List;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/19 17:17
 */
@Data
public class QuaDeveInfo {
    private String queDeveType;
    private double total;
    private List<QuaDeve> queDeveList;

}
