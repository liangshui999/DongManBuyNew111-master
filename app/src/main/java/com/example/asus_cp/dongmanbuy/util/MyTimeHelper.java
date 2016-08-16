package com.example.asus_cp.dongmanbuy.util;

import com.example.asus_cp.dongmanbuy.constant.MyConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间相关计算的帮助类
 * Created by asus-cp on 2016-08-16.
 */
public class MyTimeHelper {

    /**
     * 计算给定的时间戳和当前时间戳之间的差值
     */
    public static Map<String,Long> getTimeCha(String time){
        Map<String,Long> result=new HashMap<String,Long>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Date date= null;
        try {
            date = df.parse("2016-08-17 11:30:24");
            long l=date.getTime()-now.getTime();
            long day=l/(24*60*60*1000);
            long hour=(l/(60*60*1000)-day*24);
            long min=((l/(60*1000))-day*24*60-hour*60);
            long s=(l/1000-day*24*60*60-hour*60*60-min*60);
            System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
            result.put(MyConstant.HOUR_KEY, hour);
            result.put(MyConstant.MINUTE_KEY,min);
            result.put(MyConstant.SECOND_KEY,s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
}
