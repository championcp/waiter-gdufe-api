package com.waiterxiaoyy.backandroiddesign.Service.impl;

import com.waiterxiaoyy.backandroiddesign.Service.ElctricService;
import com.waiterxiaoyy.backandroiddesign.entity.Electric;
import com.waiterxiaoyy.backandroiddesign.utils.front.ElctricOneInfo;
import com.waiterxiaoyy.backandroiddesign.utils.jsoup.ElctricJsoup;
import com.waiterxiaoyy.backandroiddesign.utils.vo.DataVo;
import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/17 10:32
 */
@Service
public class ElctricServiceImpl implements ElctricService {
    @Override
    public DataVo getElctricOne(ElctricOneInfo elctricOneInfo) {
        DataVo dataVo = new DataVo();
        dataVo.setCode(202);
        dataVo.setMsg("获取电控信息失败");
        try {
            List<Electric> electricList = ElctricJsoup.getEletric(elctricOneInfo.getBuildingId(), elctricOneInfo.getRoomName());
            dataVo.setCode(200);
            dataVo.setMsg("获取电控信息成功");
            dataVo.setCount((long) electricList.size());
            dataVo.setData(electricList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return dataVo;
        }
    }
}
