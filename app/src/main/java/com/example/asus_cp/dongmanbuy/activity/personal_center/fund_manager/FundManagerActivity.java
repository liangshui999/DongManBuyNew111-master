package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.User;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 资金管理的界面,个人中心里面
 * Created by asus-cp on 2016-06-24.
 */
public class FundManagerActivity extends BaseActivity implements View.OnClickListener{

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

    private String userInfoUrl="http://www.zmobuy.com/PHP/?url=/user/info";//用户信息的接口

    public static final int REQUST_CODE_CHONG_ZHI=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.fund_manager_activity_layout);
        setTitle(R.string.fund_manager);
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
    public void initView() {
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

        //给view设置值
        setValueToView();

        //设置点击事件
        chongZhiLinearLayout.setOnClickListener(this);
        tiXianLinearLayout.setOnClickListener(this);
        hongBaoLinearLayout.setOnClickListener(this);
        yinHangKaLineatLayout.setOnClickListener(this);
        zhangHuMingXiRelativeLayout.setOnClickListener(this);
        shenQingJiLuRelativeLayout.setOnClickListener(this);
    }


    /**
     * 给view设置值
     */
    private void setValueToView() {
        //设置初始值
        MyLog.d(tag, user.getMoney() + "...." + user.getDongJieJinE());
        keYongYuETextView.setText(FormatHelper.getMoneyFormat(user.getMoney()));
        dongJieJinETextView.setText(user.getDongJieJinE());
        hongBaoTextView.setText(user.getHongBao());
        yinHangKaTextView.setText(user.getBankCards());
        dangQianJiFenTextView.setText(user.getJiFen());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_chong_zhi_fund_manager://点击了充值
                Intent toChongZhiIntent=new Intent(this,ChongZhiActivity.class);
                startActivityForResult(toChongZhiIntent, REQUST_CODE_CHONG_ZHI);
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
                Intent toShenQingJiLuIntent=new Intent(this,ShenQingJiLuActivity.class);
                startActivity(toShenQingJiLuIntent);
                break;
        }
    }


    /**
     * 联网获取数据，并给view赋值
     */
    private void getDataFromIntenetAndSetView() {
        StringRequest userInfoRequest=new StringRequest(Request.Method.POST, userInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        user=parseJson(s);
                        setValueToView();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"}}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(userInfoRequest);
    }


    /**
     * 解析json数据
     * @param s
     */
    private User parseJson(String s) {
        MyLog.d(tag, "返回的数据是：" + s);
        User user=new User();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            user.setId(jsonObject1.getString("user_id"));
            user.setEmail(jsonObject1.getString("email"));
            user.setName(jsonObject1.getString("user_name"));
            user.setMoney(jsonObject1.getString("user_money"));
            user.setDongJieJinE(jsonObject1.getString("frozen_money"));
            user.setJiFen(jsonObject1.getString("pay_points"));
            user.setPhone(jsonObject1.getString("mobile_phone"));
            user.setPic(jsonObject1.getString("user_picture"));
            user.setSex(JsonHelper.decodeUnicode(jsonObject1.getString("sexcn")));
            user.setBankCards(jsonObject1.getString("bank_cards"));
            user.setHongBao(jsonObject1.getString("bonus"));
            user.setRankName(JsonHelper.decodeUnicode(jsonObject1.getString("rank_name")));
            user.setShouCangShu(jsonObject1.getString("collection_num"));
            JSONObject jsonObject2=jsonObject1.getJSONObject("order_num");
            user.setAwaitPay(jsonObject2.getString("await_pay"));
            user.setAwaitShip(jsonObject2.getString("await_ship"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUST_CODE_CHONG_ZHI://从充值返回来的
                getDataFromIntenetAndSetView();
                break;
        }
    }
}
