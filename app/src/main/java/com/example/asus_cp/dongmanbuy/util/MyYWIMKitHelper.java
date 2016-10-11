package com.example.asus_cp.dongmanbuy.util;



import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.example.asus_cp.dongmanbuy.constant.AliBaiChuanConstant;

/**
 * 用于获取全局的YWIMKit这个对象
 * Created by asus-cp on 2016-08-12.
 */
public class MyYWIMKitHelper {

    public static YWIMKit mIMKit;

    public static YWIMKit getYwimkit(String userid){
        mIMKit = YWAPI.getIMKitInstance(userid, AliBaiChuanConstant.APP_KEY);
        return mIMKit;
    }
}
