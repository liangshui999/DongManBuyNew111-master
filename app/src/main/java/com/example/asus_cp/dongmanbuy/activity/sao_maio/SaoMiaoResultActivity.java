package com.example.asus_cp.dongmanbuy.activity.sao_maio;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;

/**
 * 扫描结果的展示页
 * Created by asus-cp on 2016-07-04.
 */
public class SaoMiaoResultActivity extends Activity{

    private TextView saoMiaoResultTextView;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sao_miao_result_activity_layout);
        result=getIntent().getStringExtra(MyConstant.SAO_MIAO_RESULT_KEY);
        saoMiaoResultTextView= (TextView) findViewById(R.id.text_sao_miao_result);
        saoMiaoResultTextView.setText(result);
    }
}
