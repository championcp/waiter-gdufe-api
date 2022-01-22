package com.waiterxiaoyy.backandroiddesign.entity;

import lombok.Data;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/19 14:27
 */
@Data
public class BookSearch {
    private String bookName;
    private String bookISBN;
    private String author;
    private String publishHouse;
    private String collection;
    private String borrowNum;
}
