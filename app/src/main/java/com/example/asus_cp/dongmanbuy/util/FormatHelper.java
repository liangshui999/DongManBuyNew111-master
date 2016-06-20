package com.example.asus_cp.dongmanbuy.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 格式化的帮助类
 * Created by asus-cp on 2016-05-21.
 */
public class FormatHelper {
    /**
     * 将数字保留2位小数，且以人民币开头
     */
    public static String getMoneyFormat(String str){
        if(isHaveRenMingBi(str)){
            return str;
        }else{
            double d=Double.parseDouble(str);
            DecimalFormat a = new DecimalFormat("¥.##");
            return a.format(d);
        }
    }

    /**
     * 将数字保留1位小数
     */
    public static String getOneXiaoShuFormat(String str){
        double d=Double.parseDouble(str);
        DecimalFormat a = new DecimalFormat(".#");
        return a.format(d);
    }

    /**
     * 从人民币中取出数字
     */
    public static String getNumberFromRenMingBi(String s){
        if(isHaveFuHao(s)){
            String str1=s.substring(2);
            return str1;
        }else{
            if(isHaveRenMingBi(s)){
                String str=s.substring(1);
                return str;
            }else{
                return s;
            }
        }
    }

    /**
     * 判断某个字符串是否带人名币符号
     */
    public static boolean isHaveRenMingBi(String s){
        String fuHao=s.substring(0,1);
        if("¥".equals(fuHao)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断某个字符是否带有负号
     */

    public static boolean isHaveFuHao(String s){
        String fuHao=s.substring(0,1);
        if("-".equals(fuHao)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 如果字符串是一位的话，就在他的前面加0，如：9变成09
     * @return
     */
    public static String convertStringToTwoString(String s){
        if(s.length()==1){
            return 0+s;
        }else{
            return s;
        }
    }

    /**
     * 对日期进行格式化，2016/03/16 09:55:33 +0800,注意虚拟机上运行的时候时间不对是很正常的，到真机上运行就好了
     *
     * EEE, d MMM yyyy HH:mm:ss Z"  Wed, 4 Jul 2001 12:08:56 -0700
     *
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ"  2001-07-04T12:08:56.235-0700

     */
    public static String getDate(String s){
        String inputPattern="yyyy/MM/dd HH:mm:ss Z";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(inputPattern,Locale.CHINESE);
        String result=null;
        try {
            Date date=simpleDateFormat.parse(s);
            String output="yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat simpleDateFormat1=new SimpleDateFormat(output,Locale.CHINESE);
            result=simpleDateFormat1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
