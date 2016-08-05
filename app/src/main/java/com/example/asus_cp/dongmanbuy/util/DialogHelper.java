package com.example.asus_cp.dongmanbuy.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 加载对话框的帮助类
 * Created by asus-cp on 2016-08-05.
 */
public class DialogHelper {

    private static ProgressDialog progressDialog;

    /**
     * 显示对话框
     */
    public static void showDialog(Context context){
        //显示进度框
        progressDialog=new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在加载，请稍后...");
        progressDialog.show();
    }

    /**
     * 关闭对话框
     */
    public static void dissmisDialog(){
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
