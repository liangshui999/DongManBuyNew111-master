package com.example.asus_cp.dongmanbuy.crash;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.*;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.umeng.analytics.MobclickAgent;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 我自己提供的崩溃处理器
 * Created by asus-cp on 2016-09-12.
 */
public class MyCrashHandler implements Thread.UncaughtExceptionHandler {

    private String tag="MyCrashHandler";

    private Context context;

    private Thread.UncaughtExceptionHandler mDefaultHandler;//系统默认的handler

    private static MyCrashHandler handler=new MyCrashHandler();

    private String sdCardPath=Environment.getExternalStorageDirectory().getAbsolutePath();//sd的路径

    private String houZui=".txt";//后缀名

    private String fenGe="--";


    private MyCrashHandler(){}

    /**
     * 获取crashHandler的实例
     * @return
     */
    public static MyCrashHandler getInstance(){
        return handler;
    }

    /**
     * 初始化的方法
     */
    public void init(){
        context= MyApplication.getContext();
        mDefaultHandler=Thread.getDefaultUncaughtExceptionHandler();//获取系统默认的handler
        Thread.setDefaultUncaughtExceptionHandler(this);
    }



    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        MyLog.d(tag, "uncaughtException");

        //将ex写入sd卡中
        //writeExceptionToSdCard(ex);

        //把崩溃信息上传到友盟服务器
        MobclickAgent.reportError(context,ex);

        ex.printStackTrace();//打印异常信息，方便调试
        if(mDefaultHandler!=null){
            mDefaultHandler.uncaughtException(thread,ex);
        }else{
            android.os.Process.killProcess(Process.myPid());//杀死自己的进程
        }
    }


    /**
     * 将ex写入sd卡中
     */
    private void writeExceptionToSdCard(Throwable ex) {

        MyLog.d(tag,"writeExceptionToSdCard");
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=dateFormat.format(new Date());
        MyLog.d(tag, "sd卡可用：" + isSDCardAvailable());
        //检查sd卡的状态是否可用
        if(isSDCardAvailable()){
            String dirPath=sdCardPath+"/"+"zmobuyCrash";
            File dirs=new File(dirPath);
            if(!dirs.exists()){
                dirs.mkdirs();
                MyLog.d(tag, "文件夹创建了吗" );
            }
            String filePath="log"+time+houZui;
            File file=new File(dirs,filePath);
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fileOutputStream=new FileOutputStream(file);
                PrintWriter printWriter=new PrintWriter(fileOutputStream);
                ex.printStackTrace(printWriter);//注意是用这个
                printWriter.println();//换行
                printWriter.println(time);

                MyLog.d(tag, "异常信息写入了吗");

                //将手机信息写入sd卡中
                writePhoneInfoToSdCard(printWriter);
            } catch (java.io.IOException e) {
                e.printStackTrace();
                MyLog.d(tag, e.toString());
            }
        }else{
            MyLog.d(tag, "sd卡不可用");
        }
    }

    /**
     * 将手机信息写入sd卡中
     */
    private void writePhoneInfoToSdCard(PrintWriter printWriter) {
        PackageManager manager=context.getPackageManager();
        try {
            PackageInfo info=manager.getPackageInfo(context.getPackageName(),PackageManager.GET_ACTIVITIES);
            printWriter.print("APP版本：");
            printWriter.print(info.versionName);
            printWriter.print(fenGe);
            printWriter.println(info.versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //打印安卓系统版本
        printWriter.print("安卓版本：");
        printWriter.print(Build.VERSION.RELEASE);
        printWriter.print(fenGe);
        printWriter.println(Build.VERSION.SDK_INT);

        //打印手机制造商
        printWriter.print("厂商：");
        printWriter.println(Build.MANUFACTURER);

        //打印手机型号
        printWriter.print("手机型号：");
        printWriter.println(Build.MODEL);

        //打印cpu架构
        printWriter.print("cpu型号：");
        printWriter.println(Build.CPU_ABI);

        printWriter.flush();
        printWriter.close();

    }

    /**
     * sd卡是否可用，true为可用。
     * @return
     */
    private boolean isSDCardAvailable() {
        String state=Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }else{
            return false;
        }
    }


}
