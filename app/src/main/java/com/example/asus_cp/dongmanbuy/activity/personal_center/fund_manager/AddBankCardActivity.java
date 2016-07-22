package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.CheckHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加银行卡的界面
 * Created by asus-cp on 2016-06-27.
 */
public class AddBankCardActivity extends Activity{

    private String tag="AddBankCardActivity";

    private EditText kaHaoEdtiText;//卡号
    private EditText chiKaRenEdtiText;//持卡人
    private EditText kaiHuHangEdtiText;//开户行
    private EditText bankEdtiText;//银行
    private Button tiJiaoButton;//提交

    private String addBankCardUrl="http://www.zmobuy.com/PHP/?url=/user/card_add";//添加银行卡的接口

    private RequestQueue requestQueue;
    private String uid;
    private String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_band_card_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {

        requestQueue= MyApplication.getRequestQueue();
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);

        kaHaoEdtiText= (EditText) findViewById(R.id.edit_ka_hao_add_bank_card);
        chiKaRenEdtiText= (EditText) findViewById(R.id.edit_chi_ka_ren_add_bank_card);
        kaiHuHangEdtiText= (EditText) findViewById(R.id.edit_kai_hu_hang_add_bank_card);
        bankEdtiText= (EditText) findViewById(R.id.edit_bank_add_bank_card);
        tiJiaoButton= (Button) findViewById(R.id.btn_ti_jiao_add_bank_card);

        tiJiaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String kaHao=kaHaoEdtiText.getText().toString();
                final String chiKaRen=chiKaRenEdtiText.getText().toString();
                final String kaiHuHang=kaiHuHangEdtiText.getText().toString();
                final String bankName=bankEdtiText.getText().toString();
                if("".equals(kaHao) || kaHao.isEmpty()){
                    Toast.makeText(AddBankCardActivity.this,"卡号为空",Toast.LENGTH_SHORT).show();
                }else if("".equals(chiKaRen) || chiKaRen.isEmpty()){
                    Toast.makeText(AddBankCardActivity.this,"持卡人为空",Toast.LENGTH_SHORT).show();
                }else if("".equals(kaiHuHang) || kaiHuHang.isEmpty()){
                    Toast.makeText(AddBankCardActivity.this,"开户行为空",Toast.LENGTH_SHORT).show();
                }else if("".equals(bankName) || bankName.isEmpty()){
                    Toast.makeText(AddBankCardActivity.this,"银行为空",Toast.LENGTH_SHORT).show();
                }else if(!CheckHelper.checkBankCard(kaHao)){
                    Toast.makeText(AddBankCardActivity.this,"银行卡格式错误",Toast.LENGTH_SHORT).show();
                }else{
                    getDataFromIntenet(kaHao, chiKaRen, kaiHuHang, bankName);
                }
            }
        });
    }


    /**
     * 从网络获取数据
     * @param kaHao
     * @param chiKaRen
     * @param kaiHuHang
     * @param bankName
     */
    private void getDataFromIntenet(final String kaHao, final String chiKaRen, final String kaiHuHang, final String bankName) {
        StringRequest addBankCardRequest=new StringRequest(Request.Method.POST, addBankCardUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "返回的数据是：" + s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("status");
                            String succed=jsonObject1.getString("succeed");
                            //String errordesc=jsonObject1.getString("error_desc");
                            if("1".equals(succed)){
                                Intent intent=new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }else{
                                Toast.makeText(AddBankCardActivity.this, "添加银行卡失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"bank_card\":\""+kaHao+"\",\"bank_region\":\""+kaiHuHang+"\",\"bank_name\":\""+bankName+"\",\"bank_user_name\":\""+chiKaRen+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(addBankCardRequest);
    }
}
