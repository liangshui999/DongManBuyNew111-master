package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.product_detail.AreaActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.CheckHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加收货地址的界面
 * Created by asus-cp on 2016-06-16.
 */
public class AddShouHuoAddressActivity extends Activity implements View.OnClickListener{
    private String tag="AddShouHuoAddressActivity";

    private String shengId;//省的编码
    private String shiId;//市的编码
    private String xianId;//县的编码

    private RequestQueue requestQueue;

    private String addAddressUrl="http://www.zmobuy.com/PHP/?url=/address/add";//添加地址的接口

    private String uid;
    private String sid;

    private String shouHuoRenName;
    private String shouHuoRenPhone;
    private String xiangXiDiZhi;
    private String suoZaiDiQu;

    private EditText shouHuoRenNameEditText;//收货人姓名
    private EditText shouHuoRenPhoneEditText;//收货人电话
    private RelativeLayout suoZaiDiQuRelaytiveLayout;//所在地区
    private EditText xiangXiDiZhiEditText;//详细地址
    private TextView suoZaiDiQuTextView;//所在地区
    private Button saveButton;//保存按钮

    public static final int REQUEST_CODE_FOR_AREA_ACTIVTY=1;//跳转到地区活动

    private String whoStartMe;//谁开启了我

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_shou_huo_ren_xin_xi_layout);
        init();
    }


    /**
     * 初始化的方法
     */
    private void init() {

        whoStartMe=getIntent().getStringExtra(MyConstant.START_ADD_SHOU_HUO_ADDRESS_ACTIVTIY_KEY);
        requestQueue= MyApplication.getRequestQueue();

        //获取uid和sid
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY, null);

        shouHuoRenNameEditText= (EditText) findViewById(R.id.edit_shou_huo_ren_name);
        shouHuoRenPhoneEditText= (EditText) findViewById(R.id.edit_shou_huo_ren_phone);
        suoZaiDiQuTextView= (TextView) findViewById(R.id.text_suo_zai_di_qu_add_xin_xi);
        suoZaiDiQuRelaytiveLayout= (RelativeLayout) findViewById(R.id.re_layout_suo_zai_di_qu_add_shou_huo_ren_xin_xi);
        xiangXiDiZhiEditText= (EditText) findViewById(R.id.edit_xiang_xi_di_zhi);
        saveButton= (Button) findViewById(R.id.btn_save);

        //设置点击事件
        suoZaiDiQuRelaytiveLayout.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_layout_suo_zai_di_qu_add_shou_huo_ren_xin_xi://所在地区的点击事件
                //Toast.makeText(this,"点击了所在地区",Toast.LENGTH_SHORT).show();
                Intent areaIntent=new Intent(this,AreaActivity.class);
                startActivityForResult(areaIntent, REQUEST_CODE_FOR_AREA_ACTIVTY);//地区选择结束后会向本活动返回数据
                break;
            case R.id.btn_save://保存的点击事件
                saveButtonClickChuLi();
                break;
        }
    }


    /**
     * 保存的点击事件处理
     */
    private void saveButtonClickChuLi() {
        //Toast.makeText(this,"点击了保存",Toast.LENGTH_SHORT).show();
        shouHuoRenName=shouHuoRenNameEditText.getText().toString();
        shouHuoRenPhone=shouHuoRenPhoneEditText.getText().toString();
        xiangXiDiZhi=xiangXiDiZhiEditText.getText().toString();
        suoZaiDiQu=suoZaiDiQuTextView.getText().toString();
        if("".equals(shouHuoRenName) || shouHuoRenName.isEmpty()){
            Toast.makeText(this, "收货人名为空", Toast.LENGTH_SHORT).show();
        }else if("".equals(shouHuoRenPhone) || shouHuoRenPhone.isEmpty()){
            Toast.makeText(this,"收货人电话为空",Toast.LENGTH_SHORT).show();
        }else if(!CheckHelper.isMobileNO(shouHuoRenPhone)){
            Toast.makeText(this,"电话格式错误",Toast.LENGTH_SHORT).show();
        }
        else if("请选择".equals(suoZaiDiQu)){
            Toast.makeText(this,"请选择地区",Toast.LENGTH_SHORT).show();
        }else if("".equals(xiangXiDiZhi) || xiangXiDiZhi.isEmpty()){
            Toast.makeText(this,"请填写详细地址",Toast.LENGTH_SHORT).show();
        }else{
            StringRequest saveAddressRequest=new StringRequest(Request.Method.POST, addAddressUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag, "返回的数据是：" + s);
                            Toast.makeText(AddShouHuoAddressActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                            if(whoStartMe!=null){
                                Intent intent=new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<>();
                    MyLog.d(tag,"uid="+uid+"....sid="+sid);
                    String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"address\":{\"id\":\""+""+"\",\"consignee\":\""+shouHuoRenName+"\",\"email\":\""+""+"\",\"country\":\"1\",\"province\":\""+shengId+"\",\"city\":\""+shiId+"\",\"district\":\""+xianId+"\",\"address\":\""+xiangXiDiZhi+"\",\"zipcode\":\"\",\"tel\":\""+shouHuoRenPhone+"\",\"mobile\":\""+shouHuoRenPhone+"\",\"sign_building\":\""+""+"\",\"best_time\":\"\",\"default_address\":\"0\"}}";
                    map.put("json",json);
                    return map;
                }
            };
            requestQueue.add(saveAddressRequest);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_FOR_AREA_ACTIVTY://从所在地区活动返回的数据
                if(resultCode==RESULT_OK){
                    String shengMing=data.getStringExtra(AreaActivity.SHENG_MING_KEY);
                    String shiMing=data.getStringExtra(AreaActivity.SHI_MING_KEY);
                    String xianMing=data.getStringExtra(AreaActivity.XIAN_MING_KEY);
                    suoZaiDiQuTextView.setText(shengMing+ MyConstant.KONG_GE+shiMing+MyConstant.KONG_GE
                    +xianMing);
                    shengId=data.getStringExtra(AreaActivity.SHENG_BIAN_MA_KEY);
                    shiId=data.getStringExtra(AreaActivity.SHI_BIAN_MA_KEY);
                    xianId=data.getStringExtra(AreaActivity.XIAN_BIAN_MA_KEY);
                    MyLog.d(tag,shengId+"....."+shiId+"......"+xianId);
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        //if(whoStartMe!=null){
            Intent intent=new Intent();
            setResult(RESULT_CANCELED,intent);
            finish();
       // }
    }
}
