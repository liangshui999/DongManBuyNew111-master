package com.example.asus_cp.dongmanbuy.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;


/**
 * 加载对话框的帮助类
 * Created by asus-cp on 2016-08-05.
 */
public class DialogHelper {

    private static Dialog progressDialog;


    /**
     * 显示对话框
     */
    public static void showDialog(Context context){
        //显示进度框
        showDialog(context,"正在加载...");
    }

    /**
     * 显示对话框
     */
    public static void showDialog(Context context,String message){
        //显示进度框
        progressDialog=createLoadingDialog(context,message);
        //setBackgroundAlpha(0.0f,progressDialog);
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


    /**
     * 得到自定义的progressDialog
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.my_progress_dialog_layout, null);// 得到加载view

        LinearLayout ll= (LinearLayout) v.findViewById(R.id.ll_loading);
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img_loading);
        TextView tipTextView = (TextView) v.findViewById(R.id.text_loading);// 提示文字
        // 加载动画
        Animation animation = AnimationUtils.loadAnimation(
                context, R.anim.loading_img_anim);
        LinearInterpolator linearInterpolator=new LinearInterpolator();
        animation.setInterpolator(linearInterpolator);//设置动画是线性旋转

        // 使用ImageView显示动画
        spaceshipImage.startAnimation(animation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context,R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);// 可以用“返回键”取消
        loadingDialog.setContentView(ll, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void setBackgroundAlpha(float bgAlpha,Dialog loadingDialog) {
        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        loadingDialog.getWindow().setAttributes(lp);
    }
}
