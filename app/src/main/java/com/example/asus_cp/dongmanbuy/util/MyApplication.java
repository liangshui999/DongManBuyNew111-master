package com.example.asus_cp.dongmanbuy.util;

import android.app.Application;
import android.content.Context;


//import com.alibaba.mobileim.YWAPI;
//import com.alibaba.wxlib.util.SysUtil;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.asus_cp.dongmanbuy.constant.AliBaiChuanConstant;
import com.example.asus_cp.dongmanbuy.crash.MyCrashHandler;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by asus-cp on 2016-05-20.
 */
public class MyApplication extends Application {
    private static Context context;
    private static RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        requestQueue= Volley.newRequestQueue(context);
        //SDKInitializer.initialize(context);
        //必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
//        SysUtil.setApplication(this);
//        if (SysUtil.isTCMSServiceProcess(this)) {
//            return;
//        }
        //第一个参数是Application Context
        //这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
//        if (SysUtil.isMainProcess()) {
//            YWAPI.init(this, AliBaiChuanConstant.APP_KEY);
//        }

        //crashhandler的配置
        MyCrashHandler myCrashHandler=MyCrashHandler.getInstance();
        myCrashHandler.init();


        //友盟,设置统计场景为普通场景
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType. E_UM_NORMAL);
        MobclickAgent.enableEncrypt(true);//允许加密

    }
    public static Context getContext(){
        return context;
    }

    public static RequestQueue getRequestQueue(){
        return requestQueue;
    }
}
