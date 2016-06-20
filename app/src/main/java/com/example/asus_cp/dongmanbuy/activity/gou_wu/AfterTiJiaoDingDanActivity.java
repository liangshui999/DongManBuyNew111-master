package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;

/**
 * 提交订单之后出现的页面
 * Created by asus-cp on 2016-06-20.
 */
public class AfterTiJiaoDingDanActivity extends Activity implements View.OnClickListener{
    private TextView priceTextView;
    private TextView dingDanHaoTextView;
    private Button zhiFuBaoZhiFuButton;
    private TextView seeDingDanTextView;

    private String price;//付款金额
    private String bianHao;//订单编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.after_ti_jiao_order_activity_layout);
        priceTextView= (TextView) findViewById(R.id.text_fu_kuan_jin_e_after_ti_jiao_ding_dan);
        dingDanHaoTextView= (TextView) findViewById(R.id.text_ding_dan_hao_after_ti_jiao_ding_dan);
        zhiFuBaoZhiFuButton= (Button) findViewById(R.id.btn_zhi_fu_bao_zhi_fu_after_ti_jiao_ding_dan);
        seeDingDanTextView= (TextView) findViewById(R.id.text_see_ding_dan_after_ti_jiao_ding_dan);

        //设置付款金额
        price=getIntent().getStringExtra(MyConstant.SHI_FU_KUAN_KEY);
        priceTextView.setText(price);

        //设置订单编号
        bianHao=getIntent().getStringExtra(MyConstant.DING_DAN_BIAN_HAO_KEY);
        dingDanHaoTextView.setText(bianHao);

        //设置点击事件
        zhiFuBaoZhiFuButton.setOnClickListener(this);
        seeDingDanTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_zhi_fu_bao_zhi_fu_after_ti_jiao_ding_dan://点击了支付宝支付
                Toast.makeText(this,"点击了支付宝支付",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_see_ding_dan_after_ti_jiao_ding_dan://点击了查看订单
                Intent seeIntent=new Intent(this,DingDanListActivity.class);
                startActivity(seeIntent);
                break;
        }
    }
}
