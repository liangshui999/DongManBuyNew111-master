package com.example.asus_cp.dongmanbuy.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;

/**
 * 所有activity的基类
 * Created by asus-cp on 2016-09-13.
 */
public abstract class BaseActivity extends AppCompatActivity{

    //基类里面定义好一些共有变量，子类里面都需要使用的
    public RequestQueue requestQueue;

    public String uid;

    public String sid;

    private LinearLayout contentLinearLayout;
    private ImageView backImageView;//返回按钮
    private TextView titleTextView;//标题栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//注意是调用这个，而不是上面的
        //getSupportActionBar().hide();
        setContentView(R.layout.base_activity_layout);
        contentLinearLayout= (LinearLayout) findViewById(R.id.ll_content_base_activity);
        backImageView = (ImageView) findViewById(R.id.img_dao_hang_base_acitivity);
        titleTextView = (TextView) findViewById(R.id.text_dao_hang_base_activity);

        //给返回按钮设置点击事件
        backImageViewClickChuLi();

        requestQueue= MyApplication.getRequestQueue();
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY, null);
    }


    /**
     * 设置内容的布局
     * @param resId
     */
    public void setContentLayout(int resId){
        View v= LayoutInflater.from(this).inflate(resId,null);
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(layoutParams);
        contentLinearLayout.addView(v);
    }

    /**
     * 设置内容的布局
     */
    public void setContentLayout(View v){
        contentLinearLayout.addView(v);
    }


    /**
     * 返回按钮的点击事件处理,默认的处理是finish
     * 子类可以重写该方法，自己设置点击事件
     */
    public void backImageViewClickChuLi(){
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 获取返回按钮
     * @return
     */
    public ImageView getBackImageView(){
        return backImageView;
    }

    /**
     * 设置标题
     */
    public void setTitle(String s){
        titleTextView.setText(s);
    }


    /**
     * 设置标题
     */
    public void setTitle(int resId){
        titleTextView.setText(resId);
    }



    /**
     * 初始化view
     */
    public abstract void initView();
}
