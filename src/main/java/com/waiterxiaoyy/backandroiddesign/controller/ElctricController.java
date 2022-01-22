package com.waiterxiaoyy.backandroiddesign.controller;

import com.waiterxiaoyy.backandroiddesign.Service.ElctricService;
import com.waiterxiaoyy.backandroiddesign.utils.front.ElctricOneInfo;
import com.waiterxiaoyy.backandroiddesign.utils.vo.DataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/17 10:29
 */
@RestController
@RequestMapping("/api")
@Api(description = "电控控制，由于学校校园网站崩溃，此接口作废")
public class ElctricController {

    @Autowired
    private ElctricService elctricService;

    @PostMapping("/getelctricone")
    @ApiOperation("获取电控信息 /// 只支持23:5，30:97，32:911，化1:1947，化2:2019，化3：2074，化5：2139")
    public DataVo getElctricOne(@RequestBody ElctricOneInfo elctricOneInfo) {
        return elctricService.getElctricOne(elctricOneInfo);
    }
}
