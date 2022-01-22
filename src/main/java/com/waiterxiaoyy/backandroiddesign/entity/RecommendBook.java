package com.waiterxiaoyy.backandroiddesign.entity;

import lombok.Data;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/7 16:23
 */
@Data
public class RecommendBook {
    private String bookid;
    private String bookname;
    private String author;
    private String PublishHouse;
    private String callNum;
    private int collection;
    private int borrowNum;
    private double borrowRatio;
}
