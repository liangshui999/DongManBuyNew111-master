package com.example.asus_cp.dongmanbuy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.GuideViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页面(程序第一次安装到手机上时，会进入该页面，之后不会再进入该页面了)
 * Created by asus-cp on 2016-08-10.
 */
public class GuideActivity extends Activity implements View.OnClickListener{

    private ViewPager viewPager;
    private LinearLayout dotsLinearLayout;
    private TextView inTextView;//立即进入
    private TextView tiaoGuoOneTextView;//跳过
    private TextView tiaoGuoTwoTextView;//跳过

    private List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guide_activity_layout);

        //隐藏系统状态栏
        View decroView=getWindow().getDecorView();
        decroView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        viewPager= (ViewPager) findViewById(R.id.view_pager_guide);
        dotsLinearLayout= (LinearLayout) findViewById(R.id.ll_dots);

        LayoutInflater inflater=LayoutInflater.from(this);
        views=new ArrayList<View>();
        View viewOne=inflater.inflate(R.layout.guide_one_layout,null);
        View viewTwo=inflater.inflate(R.layout.guide_two_layout,null);
        View viewThree=inflater.inflate(R.layout.guide_three_layout,null);
        views.add(viewOne);
        views.add(viewTwo);
        views.add(viewThree);

        inTextView= (TextView) viewThree.findViewById(R.id.text_in);//立即进入
        tiaoGuoOneTextView= (TextView) viewOne.findViewById(R.id.text_tiao_guo_guide_one);
        tiaoGuoTwoTextView= (TextView) viewTwo.findViewById(R.id.text_tiao_guo_guide_two);

        GuideViewPagerAdapter adapter=new GuideViewPagerAdapter(this,views);
        viewPager.setAdapter(adapter);

        //设置点击事件
        inTextView.setOnClickListener(this);
        tiaoGuoOneTextView.setOnClickListener(this);
        tiaoGuoTwoTextView.setOnClickListener(this);

    }

    /**
     * 跳转到主界面
     */
    private void toMainActivity() {
        Intent intent=new Intent(GuideActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                finish();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_in://立即进入
                toMainActivity();
                break;
            case R.id.text_tiao_guo_guide_one://跳过1
                toMainActivity();
                break;
            case R.id.text_tiao_guo_guide_two://跳过2
                toMainActivity();
                break;
        }
    }
}
