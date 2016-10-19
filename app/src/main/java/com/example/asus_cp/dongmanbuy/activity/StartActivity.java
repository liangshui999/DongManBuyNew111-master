package com.example.asus_cp.dongmanbuy.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.service.UidService;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.MyNetHelper;
import com.example.asus_cp.dongmanbuy.util.YouMengHelper;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 启动引导界面
 * Created by asus-cp on 2016-07-18.
 */
public class StartActivity extends Activity{

    private String tag="StartActivity";


    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start_activity_layout);

        //隐藏系统状态栏
        View decroView=getWindow().getDecorView();
        decroView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        imageView= (ImageView) findViewById(R.id.img_start_img);
        Animation animation=AnimationUtils.loadAnimation(this, R.anim.start_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //开启服务
                Intent intent = new Intent(StartActivity.this, UidService.class);
                startService(intent);

                //获取设备信息，并写入shareprfrences里面
                getPhoneInfoAndWriteToSharePrefrences();

                if(MyNetHelper.isNetworkAvailable()){
                    clearCachSharePrefrences();//清空缓存
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences sharedPreferences = getSharedPreferences(MyConstant.GUIDE_SHAREPRENCE_NAME, MODE_PRIVATE);
                boolean isFirst = sharedPreferences.getBoolean(MyConstant.IS_FIRST_KEY, true);
                if (!isFirst) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(StartActivity.this, GuideActivity.class);
                    startActivity(intent);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(MyConstant.IS_FIRST_KEY, false);
                    editor.commit();
                    finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animation);

        //requestQueue= MyApplication.getRequestQueue();

        //MyLog.d(tag,YouMengHelper.getDeviceInfo(this));

    }

    /**
     * 清空首页的缓存数据
     */
    private void clearCachSharePrefrences() {
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.CACH_SHAREPREFERENCE_NAME,MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 获取设备信息，并写入shareprfrences里面
     */
    private void getPhoneInfoAndWriteToSharePrefrences() {
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.SCREEN_INFO_SHARE_NAME, MODE_PRIVATE);
        int temp=sharedPreferences.getInt(MyConstant.SCREEN_DENSTY_DPI_KEY, -1);
        if(temp==-1){
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int screenWith = metric.widthPixels;  // 屏幕宽度（像素）
            int screenHeight = metric.heightPixels;  // 屏幕高度（像素）
            int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt(MyConstant.SCREEN_WIDTH_KEY,screenWith);
            editor.putInt(MyConstant.SCREEN_HEIGHT_KEY,screenHeight);
            editor.putInt(MyConstant.SCREEN_DENSTY_DPI_KEY,densityDpi);
            editor.apply();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                finish();
            case 2:
                finish();
            break;
        }
    }


    /**
     * 手动发起post请求，此方法作废
     */
    /*private void shouDongPost() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    URL url=new URL(regionUrl);
                    conn= (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10 * 1000);
                    conn.setReadTimeout(10 * 1000);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + "UTF-8");
                    OutputStream out=conn.getOutputStream();
                    StringBuffer params = new StringBuffer();
                    // 表单参数与get形式一样
                    params.append("region_id").append("=").append(""+11).append("&")
                            .append("service").append("=").append("region");
                    //byte[] bypes = params.toString().getBytes();
                    out.write(params.toString().getBytes("utf-8"));
                    out.flush();
                    out.close();
                    InputStream in=conn.getInputStream();
                    byte[] buf=new byte[1024*1024];
                    in.read(buf);
                    MyLog.d(tag, "发送的数据是" + params.toString());
                    MyLog.d(tag, new String(buf));
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    conn.disconnect();
                }
            }
        }).start();
    }*/
}
