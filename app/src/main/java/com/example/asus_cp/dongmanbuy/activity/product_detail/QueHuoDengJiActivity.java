package com.example.asus_cp.dongmanbuy.activity.product_detail;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.asus_cp.dongmanbuy.R;

/**
 * 缺货登记的activity
 * Created by asus-cp on 2016-06-06.
 */
public class QueHuoDengJiActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.que_huo_deng_ji);
    }
}
