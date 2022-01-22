package com.waiterxiaoyy.backandroiddesign.utils.util;


import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/11/29 11:40
 */
public class DateUtil {

    public static boolean outTime(String endtime, String posttime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate =  simpleDateFormat.parse(endtime);
        Date postTime = simpleDateFormat.parse(posttime);
        if(postTime.after(endDate)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean inTime(String endtime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate =  simpleDateFormat.parse(endtime);
        Date nowDate = new Date();
        if(nowDate.before(endDate)) {
            return true;
        } else {
            return false;
        }
    }
    public static int getRandom(int start,int end){
        int random = (int)(Math.random()*(end - start + 1)+start);
        return random;
    }

    public static String getNowTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return df.format(date);
    }
    public static String getNowDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return df.format(date);
    }
    public static String getWeekOfDate() {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static Integer getWeek() throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dstr="2020-9-14";
        Date date=sdf.parse(dstr);
        long s1=date.getTime();//将时间bai转为毫秒
        long s2=System.currentTimeMillis();//得到当前的du毫秒
        long day=(s2-s1)/86400000;
        long week = day / 7;
        long count = (s2-s1)%86400000;
        if(count > 0) {
            week++;
        }
        return (int)week;
    }


    public static String getWeekOfDate(String date) throws ParseException {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        cal.setTime(simpleDateFormat.parse(date));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }



    public static boolean beforeTime(String starttime) throws ParseException {
        Date nowtime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = simpleDateFormat.parse(starttime);
        if(nowtime.before(time)) {
            return true;
        }
        return false;
    }
    public static boolean aftertime(String endtime) throws ParseException {
        Date nowtime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = simpleDateFormat.parse(endtime);
        if(nowtime.after(time)) {
            return true;
        }
        return false;
    }

    public static String getIntTimePre(int num) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        num = 7;
        //过去七天
        String time = null;
        if(num <= 7) {
            c.setTime(new Date());
            c.add(Calendar.DATE, - num);
            Date d = c.getTime();
            time = format.format(d);
        } else if(num == 30) {
            //过去一月
            c.setTime(new Date());
            c.add(Calendar.MONTH, -1);
            Date m = c.getTime();
            time = format.format(m);
        } else if(num == 90) {
            //过去三个月
            c.setTime(new Date());
            c.add(Calendar.MONTH, -3);
            Date m3 = c.getTime();
            time = format.format(m3);
        } else if (num == 0) {
            time =  "2020-9-14";
        }

        return time;
    }


}
