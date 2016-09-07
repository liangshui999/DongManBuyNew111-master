package com.example.asus_cp.dongmanbuy.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.asus_cp.dongmanbuy.constant.MyConstant;

/**
 * 屏幕信息的帮助类
 * Created by asus-cp on 2016-09-07.
 */
public class MyScreenInfoHelper {



    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getScreenWidth(){
        Context context=MyApplication.getContext();
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.SCREEN_INFO_SHARE_NAME,Context.MODE_PRIVATE);
        return  sharedPreferences.getInt(MyConstant.SCREEN_WIDTH_KEY,-1);
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public static int getScreenHeight(){
        Context context=MyApplication.getContext();
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.SCREEN_INFO_SHARE_NAME,Context.MODE_PRIVATE);
        return  sharedPreferences.getInt(MyConstant.SCREEN_HEIGHT_KEY,-1);
    }

    /**
     * 获取屏幕dpi
     * @return
     */
    public static int getScreenDpi(){
        Context context=MyApplication.getContext();
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.SCREEN_INFO_SHARE_NAME,Context.MODE_PRIVATE);
        return  sharedPreferences.getInt(MyConstant.SCREEN_DENSTY_DPI_KEY,-1);
    }
}
