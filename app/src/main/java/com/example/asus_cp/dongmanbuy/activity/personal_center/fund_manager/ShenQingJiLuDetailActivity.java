package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.ShenQingJiLuModel;

/**
 * 申请记录详情界面
 * Created by asus-cp on 2016-06-28.
 */
public class ShenQingJiLuDetailActivity extends Activity{

    private TextView typeTextView;//交易类型
    private TextView timeTextView;//交易时间
    private TextView moneyTextView;//交易资金
    private TextView statusTextView;//交易状态
    private TextView shouXuFeiTextView;//手续费
    private TextView managerNoteTextView;//管理员备注
    private Button cancelButton;//取消按钮


    private ShenQingJiLuModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shen_qing_ji_lu_detail_activity_layout);
        init();
    }


    /**
     * 初始化的方法
     */
    private void init() {

        model=getIntent().getParcelableExtra(MyConstant.SHEN_QING_JI_LU_MODEL_KEY);

        typeTextView= (TextView) findViewById(R.id.text_jiao_yi_fang_shi_shen_qing_ji_lu_detail);
        timeTextView= (TextView) findViewById(R.id.text_time_shen_qing_ji_lu_detail);
        moneyTextView= (TextView) findViewById(R.id.text_jin_e_shen_qing_ji_lu_detail);
        statusTextView= (TextView) findViewById(R.id.text_status_shen_qing_ji_lu_detail);
        shouXuFeiTextView= (TextView) findViewById(R.id.text_shou_xu_fei_shen_qing_ji_lu_detail);
        managerNoteTextView= (TextView) findViewById(R.id.text_manager_bei_zhu_shen_qing_ji_lu_detail);
        cancelButton= (Button) findViewById(R.id.btn_cancel_shen_qing_ji_lu_detail);

        //给view设置值
        typeTextView.setText(model.getType());
        timeTextView.setText(model.getTime());
        moneyTextView.setText(model.getJinE());
        statusTextView.setText(model.getStatus());
        shouXuFeiTextView.setText(model.getShouXuFei());
        managerNoteTextView.setText(model.getManagerNote());

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
