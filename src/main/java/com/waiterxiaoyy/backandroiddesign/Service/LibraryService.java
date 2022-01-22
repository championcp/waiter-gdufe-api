package com.waiterxiaoyy.backandroiddesign.Service;

import com.waiterxiaoyy.backandroiddesign.utils.vo.DataVo;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/7 17:58
 */
public interface LibraryService {
    DataVo getRecommendBook();

    DataVo getSearchList(String query);
}
