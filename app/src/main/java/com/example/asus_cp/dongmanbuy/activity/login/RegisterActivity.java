package com.example.asus_cp.dongmanbuy.activity.login;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.fragment.register_fragment.EmailRegisterFragment;
import com.example.asus_cp.dongmanbuy.fragment.register_fragment.PhoneRegisterFragment;

/**
 * 注册活动所在的界面
 * Created by asus-cp on 2016-05-27.
 */
public class RegisterActivity extends FragmentActivity implements View.OnClickListener{
    private ImageView daoHangImageView;//导航
    private TextView phoneRegisterTextView;//手机注册
    private TextView emailRegisterTextView;//邮箱注册
    private ImageView phoneRegisterImageView;
    private ImageView emailRegisterImageView;
    private TextView zhiJieLoginTextView;//已注册直接登陆

    private FrameLayout bufFrameLayout;//装载碎片的容器

    private FragmentManager fragmentManager;
    private PhoneRegisterFragment phoneRegisterFragment;//手机注册的碎片
    private EmailRegisterFragment emailRegisterFragment;//邮箱注册的碎片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_activity_layout);

        daoHangImageView= (ImageView) findViewById(R.id.img_dao_hang_register);
        phoneRegisterTextView= (TextView) findViewById(R.id.text_phone_register);
        emailRegisterTextView= (TextView) findViewById(R.id.text_email_register);
        phoneRegisterImageView= (ImageView) findViewById(R.id.img_phone_register_status);
        emailRegisterImageView= (ImageView) findViewById(R.id.img_email_register_status);
        zhiJieLoginTextView= (TextView) findViewById(R.id.text_zhi_jie_login);
        bufFrameLayout= (FrameLayout) findViewById(R.id.frame_register_buf);

        phoneRegisterTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));

        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        phoneRegisterTextView.setOnClickListener(this);
        emailRegisterTextView.setOnClickListener(this);
        zhiJieLoginTextView.setOnClickListener(this);//直接登陆

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
            case R.id.img_dao_hang_register:
                finish();
                break;
            case R.id.text_phone_register://手机注册
                reset();
                FragmentTransaction transaction1=fragmentManager.beginTransaction();
                transaction1.replace(R.id.frame_register_buf, phoneRegisterFragment);
                transaction1.commit();
                phoneRegisterTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                phoneRegisterImageView.setImageResource(R.mipmap.register_selected);
                break;
            case R.id.text_email_register://邮箱注册
                reset();
                FragmentTransaction transaction2=fragmentManager.beginTransaction();
                transaction2.replace(R.id.frame_register_buf, emailRegisterFragment);
                transaction2.commit();
                emailRegisterTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                emailRegisterImageView.setImageResource(R.mipmap.register_selected);
                break;
            case R.id.text_zhi_jie_login://已注册直接登陆
                finish();
                break;
        }
    }

    /**
     * 重置字体颜色
     */
    public void reset(){
        phoneRegisterTextView.setTextColor(getResources().getColor(R.color.black));
        emailRegisterTextView.setTextColor(getResources().getColor(R.color.black));
        phoneRegisterImageView.setImageResource(R.color.white_my);
        emailRegisterImageView.setImageResource(R.color.white_my);
    }
}
