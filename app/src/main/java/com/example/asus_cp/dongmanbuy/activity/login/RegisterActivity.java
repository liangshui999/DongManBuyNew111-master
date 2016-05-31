package com.example.asus_cp.dongmanbuy.activity.login;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.fragment.register_fragment.EmailRegisterFragment;
import com.example.asus_cp.dongmanbuy.fragment.register_fragment.PhoneRegisterFragment;

/**
 * 注册活动所在的界面
 * Created by asus-cp on 2016-05-27.
 */
public class RegisterActivity extends FragmentActivity implements View.OnClickListener{
    private TextView phoneRegisterTextView;//手机注册
    private TextView emailRegisterTextView;//邮箱注册
    private FrameLayout bufFrameLayout;//装载碎片的容器

    private FragmentManager fragmentManager;
    private PhoneRegisterFragment phoneRegisterFragment;//手机注册的碎片
    private EmailRegisterFragment emailRegisterFragment;//邮箱注册的碎片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_activity_layout);

        phoneRegisterTextView= (TextView) findViewById(R.id.text_phone_register);
        emailRegisterTextView= (TextView) findViewById(R.id.text_email_register);
        bufFrameLayout= (FrameLayout) findViewById(R.id.frame_register_buf);

        phoneRegisterTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));

        phoneRegisterTextView.setOnClickListener(this);
        emailRegisterTextView.setOnClickListener(this);

        phoneRegisterFragment=new PhoneRegisterFragment();
        emailRegisterFragment=new EmailRegisterFragment();

        fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.frame_register_buf,phoneRegisterFragment);
        transaction.commit();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_phone_register://手机注册
                resetFontColor();
                FragmentTransaction transaction1=fragmentManager.beginTransaction();
                transaction1.replace(R.id.frame_register_buf, phoneRegisterFragment);
                transaction1.commit();
                phoneRegisterTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case R.id.text_email_register://邮箱注册
                resetFontColor();
                FragmentTransaction transaction2=fragmentManager.beginTransaction();
                transaction2.replace(R.id.frame_register_buf, emailRegisterFragment);
                transaction2.commit();
                emailRegisterTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
        }
    }

    /**
     * 重置字体颜色
     */
    public void resetFontColor(){
        phoneRegisterTextView.setTextColor(getResources().getColor(R.color.black));
        emailRegisterTextView.setTextColor(getResources().getColor(R.color.black));
    }
}
