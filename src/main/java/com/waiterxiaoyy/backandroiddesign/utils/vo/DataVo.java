package com.waiterxiaoyy.backandroiddesign.utils.vo;

import lombok.Data;

import java.util.List;

/**
 * @author :WaiterXiaoYY
 * @description: 封装json数据格式
 * @data :2020/10/11 18:15
 */

@Data
public class DataVo<T> {
    private Integer code;
    private String msg;
    private Long count;
    private List<T> data;
}
