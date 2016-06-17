package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.example.asus_cp.dongmanbuy.R;

/**
 * 自提地点的界面
 * Created by asus-cp on 2016-06-17.
 */
public class ZiTiAreaActivity extends Activity {
    private RelativeLayout closeRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zi_ti_area_activity_layout);
        closeRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_close_area_zi_ti);
        closeRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
