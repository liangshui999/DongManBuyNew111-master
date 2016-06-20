package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;

/**
 * 余额支付成功后的界面
 * Created by asus-cp on 2016-06-20.
 */
public class YuEZhiFuSuccessedActivity extends Activity{
    private TextView dingDanHaoTextView;
    private TextView seeDingDanTextView;

    private String dingDanHao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.yu_e_zhi_fu_successed_layout);

        dingDanHaoTextView= (TextView) findViewById(R.id.text_ding_dan_hao_yu_e_zhi_fu);
        seeDingDanTextView= (TextView) findViewById(R.id.text_see_ding_dan_yu_e_zhi_fu);

        //设置订单号
        dingDanHao=getIntent().getStringExtra(MyConstant.DING_DAN_BIAN_HAO_KEY);
        dingDanHaoTextView.setText(dingDanHao);

        //设置点击事件
        seeDingDanTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
