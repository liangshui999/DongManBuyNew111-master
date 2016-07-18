package com.example.asus_cp.dongmanbuy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.service.UidService;

/**
 * 启动引导界面
 * Created by asus-cp on 2016-07-18.
 */
public class StartActivity extends Activity{
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start_activity_layout);
        imageView= (ImageView) findViewById(R.id.img_start_img);
        Animation animation=AnimationUtils.loadAnimation(this, R.anim.start_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //开启服务
                Intent intent = new Intent(StartActivity.this, UidService.class);
                startService(intent);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent=new Intent(StartActivity.this,MainActivity.class);
                startActivityForResult(intent,1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                finish();
            break;
        }
    }
}
