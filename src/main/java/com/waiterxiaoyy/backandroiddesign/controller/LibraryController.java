package com.waiterxiaoyy.backandroiddesign.controller;

import com.waiterxiaoyy.backandroiddesign.Service.LibraryService;
import com.waiterxiaoyy.backandroiddesign.utils.vo.DataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/7 17:59
 */
@RestController
@RequestMapping("/api")
@Api(description = "图书馆控制")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @GetMapping("/getrecommendbook")
    @ApiOperation("获取当前图书馆热门推荐")
    public DataVo getRecommendBook() {
        return libraryService.getRecommendBook();
    }

    @GetMapping("searchbook/{query}")
    @ApiOperation("搜索图书")
    public DataVo getSearchList(@PathVariable("query") String query) {
        return libraryService.getSearchList(query);
    }
}
