package com.example.asus_cp.dongmanbuy.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从换乘里面提取出公交信息
 * Created by asus-cp on 2016-07-28.
 */
public class MyGongJiaoUtil {

    private static String tag="MyGongJiaoUtil";

    /**
     * 根据线路规划提出去公交名称
     * @param s
     * @return
     */
    public static String getGongJiaoName(String s){
        MyLog.d(tag,"传递进来的数据是："+s);
        String result=null;
        String regex="\\乘坐.+?\\,";//正则表达式
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(s);
        if(matcher.find()){
            String temp=matcher.group();
            MyLog.d(tag,"匹配出来的数据是："+temp);
            int index=temp.indexOf(",");
            result=temp.substring(2,index);
            MyLog.d(tag,"结果数据是："+result);
        }
        return result;
    }


}
