package com.waiterxiaoyy.backandroiddesign.utils.vo;

import com.waiterxiaoyy.backandroiddesign.entity.Explain;
import com.waiterxiaoyy.backandroiddesign.entity.Gpa;
import com.waiterxiaoyy.backandroiddesign.entity.Grade;
import lombok.Data;

import java.util.List;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/16 21:17
 */
@Data
public class GradeVo {
    private Integer code;
    private String msg;
    private Long count;
    private List<Grade> grade;
    private List<Explain> explain;
    private List<Gpa> gpa;
}
