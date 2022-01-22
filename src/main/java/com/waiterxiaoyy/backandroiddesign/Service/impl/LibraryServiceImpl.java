package com.waiterxiaoyy.backandroiddesign.Service.impl;

import com.waiterxiaoyy.backandroiddesign.Service.LibraryService;
import com.waiterxiaoyy.backandroiddesign.entity.BookSearch;
import com.waiterxiaoyy.backandroiddesign.entity.RecommendBook;
import com.waiterxiaoyy.backandroiddesign.utils.jsoup.LibraryJsoup;
import com.waiterxiaoyy.backandroiddesign.utils.vo.DataVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/7 17:59
 */
@Service
public class LibraryServiceImpl implements LibraryService {


    @Override
    public DataVo getRecommendBook() {
        DataVo dataVo = new DataVo();
        dataVo.setCode(202);
        dataVo.setMsg("获取热门推荐书籍失败");
        try {

            List<RecommendBook> recommendBookList = LibraryJsoup.getRecommendBook();
            dataVo.setCode(200);
            dataVo.setMsg("获取热门推荐书籍成功");
            dataVo.setData(recommendBookList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return dataVo;
        }
    }

    @Override
    public DataVo getSearchList(String query) {
        DataVo dataVo = new DataVo();
        dataVo.setCode(202);
        dataVo.setMsg("搜索图书失败");
        try {

            List<BookSearch> bookSearchList = LibraryJsoup.getSearchBookList(query);
            dataVo.setCode(200);
            dataVo.setMsg("搜索图书成功");
            dataVo.setData(bookSearchList);
            dataVo.setCount((long)bookSearchList.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return dataVo;
        }
    }
}
