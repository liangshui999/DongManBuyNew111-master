package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.User;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.MyLog;

/**
 * 资金管理的界面,个人中心里面
 * Created by asus-cp on 2016-06-24.
 */
public class FundManagerActivity extends Activity implements View.OnClickListener{

    private String tag="FundManagerActivity";

    private TextView keYongYuETextView;//可用余额
    private TextView dongJieJinETextView;//冻结金额
    private LinearLayout chongZhiLinearLayout;//充值
    private LinearLayout tiXianLinearLayout;//提现
    private LinearLayout hongBaoLinearLayout;//红包
    private LinearLayout yinHangKaLineatLayout;//银行卡
    private TextView hongBaoTextView;//红包
    private TextView yinHangKaTextView;//银行卡
    private TextView dangQianJiFenTextView;//当前积分
    private RelativeLayout zhangHuMingXiRelativeLayout;//账户明细
    private RelativeLayout shenQingJiLuRelativeLayout;//申请记录

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fund_manager_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        user=getIntent().getParcelableExtra(MyConstant.USER_KEY);
        initView();

    }


    /**
     * 初始化视图
     */
    private void initView() {
        keYongYuETextView= (TextView) findViewById(R.id.text_ke_yong_yu_e_fund_manager);
        dongJieJinETextView= (TextView) findViewById(R.id.text_dong_jie_jin_e_fund_manager);
        chongZhiLinearLayout= (LinearLayout) findViewById(R.id.ll_chong_zhi_fund_manager);
        tiXianLinearLayout= (LinearLayout) findViewById(R.id.ll_ti_xian_fund_manager);
        hongBaoLinearLayout= (LinearLayout) findViewById(R.id.ll_hong_bao_fund_manager);
        yinHangKaLineatLayout= (LinearLayout) findViewById(R.id.ll_yin_hang_ka_fund_manager);
        hongBaoTextView= (TextView) findViewById(R.id.text_hong_bao_fund_manager);
        yinHangKaTextView= (TextView) findViewById(R.id.text_yin_hang_ka_fund_manager);
        dangQianJiFenTextView= (TextView) findViewById(R.id.text_dang_qian_ji_fen_fund_manager);
        zhangHuMingXiRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_zhang_hu_ming_xi_fund_manager);
        shenQingJiLuRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_shen_qing_ji_lu_fund_manager);

        //设置初始值
        MyLog.d(tag,user.getMoney()+"...."+user.getDongJieJinE());
        keYongYuETextView.setText(user.getMoney());
        dongJieJinETextView.setText(user.getDongJieJinE());
        hongBaoTextView.setText(user.getHongBao());
        yinHangKaTextView.setText(user.getBankCards());
        dangQianJiFenTextView.setText(user.getJiFen());


        //设置点击事件
        chongZhiLinearLayout.setOnClickListener(this);
        tiXianLinearLayout.setOnClickListener(this);
        hongBaoLinearLayout.setOnClickListener(this);
        yinHangKaLineatLayout.setOnClickListener(this);
        zhangHuMingXiRelativeLayout.setOnClickListener(this);
        shenQingJiLuRelativeLayout.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_chong_zhi_fund_manager://点击了充值
                Intent toChongZhiIntent=new Intent(this,ChongZhiActivity.class);
                startActivity(toChongZhiIntent);
                break;
            case R.id.ll_ti_xian_fund_manager://点击提现
                Intent toTiXianIntent=new Intent(this,TiXianActivity.class);
                toTiXianIntent.putExtra(MyConstant.USER_KEY,user);
                startActivity(toTiXianIntent);
                break;
            case R.id.ll_hong_bao_fund_manager://点击了红包
                Intent toHongBaoIntent=new Intent(this,HongBaoListActivity.class);
                startActivity(toHongBaoIntent);
                break;
            case R.id.ll_yin_hang_ka_fund_manager://点击了银行卡
                Intent toBankCardListActivityIntent=new Intent(this,BankCardListActivity.class);
                startActivity(toBankCardListActivityIntent);
                break;
            case R.id.re_layout_zhang_hu_ming_xi_fund_manager://点击了账户明细
                Intent toAccountDetailIntent=new Intent(this,AccountDetailActivity.class);
                startActivity(toAccountDetailIntent);
                break;
            case R.id.re_layout_shen_qing_ji_lu_fund_manager://点击了申请记录
                Toast.makeText(this,"点击了申请记录",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
