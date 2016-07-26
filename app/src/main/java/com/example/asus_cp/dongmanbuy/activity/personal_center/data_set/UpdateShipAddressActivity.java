package com.example.asus_cp.dongmanbuy.activity.personal_center.data_set;

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
import com.example.asus_cp.dongmanbuy.model.UserModel;
import com.example.asus_cp.dongmanbuy.util.CheckHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心里面的更新收货人地址
 * Created by asus-cp on 2016-06-23.
 */
public class UpdateShipAddressActivity extends Activity implements View.OnClickListener{

    private String tag="UpdateShipAddressActivity";

    private String shengId;//省的编码
    private String shiId;//市的编码
    private String xianId;//县的编码

    private RequestQueue requestQueue;

    private String updateAddressUrl ="http://www.zmobuy.com/PHP/?url=/address/update";//更新地址的接口

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

    private UserModel userModel;//从收货地址列表传递过来的



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_shou_huo_address_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        requestQueue= MyApplication.getRequestQueue();
        userModel=getIntent().getParcelableExtra(MyConstant.USER_MODLE_KEY);


        //获取uid和sid
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY, null);

        shouHuoRenNameEditText= (EditText) findViewById(R.id.edit_shou_huo_ren_name_update);
        shouHuoRenPhoneEditText= (EditText) findViewById(R.id.edit_shou_huo_ren_phone_update);
        suoZaiDiQuTextView= (TextView) findViewById(R.id.text_suo_zai_di_qu_add_xin_xi_update);
        suoZaiDiQuRelaytiveLayout= (RelativeLayout) findViewById(R.id.re_layout_suo_zai_di_qu_add_shou_huo_ren_xin_xi_update);
        xiangXiDiZhiEditText= (EditText) findViewById(R.id.edit_xiang_xi_di_zhi_update);
        saveButton= (Button) findViewById(R.id.btn_save_update);

        //设置初始值
        shouHuoRenNameEditText.setText(userModel.getUserName());
        shouHuoRenPhoneEditText.setText(userModel.getUserPhone());
        suoZaiDiQuTextView.setText(userModel.getProvinceName()+userModel.getCityName()+userModel.getDistrictName());
        xiangXiDiZhiEditText.setText(userModel.getShouHuoArea());

        //设置省id市id县id的初始值
        shengId=13+"";
        shiId=180+"";
        xianId=1545+"";

        //设置点击事件
        suoZaiDiQuRelaytiveLayout.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_layout_suo_zai_di_qu_add_shou_huo_ren_xin_xi_update://所在地区的点击事件
                //Toast.makeText(this,"点击了所在地区",Toast.LENGTH_SHORT).show();
                Intent areaIntent=new Intent(this,AreaActivity.class);
                startActivityForResult(areaIntent, REQUEST_CODE_FOR_AREA_ACTIVTY);//地区选择结束后会向本活动返回数据
                break;
            case R.id.btn_save_update://保存的点击事件
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
        else if("".equals(suoZaiDiQu)){
            Toast.makeText(this,"请选择地区",Toast.LENGTH_SHORT).show();
        }else if("".equals(xiangXiDiZhi) || xiangXiDiZhi.isEmpty()){
            Toast.makeText(this,"请填写详细地址",Toast.LENGTH_SHORT).show();
        }else{

            StringRequest updateAddressRequest=new StringRequest(Request.Method.POST, updateAddressUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag, "返回的数据是：" + s);
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                JSONObject jsonObject1=jsonObject.getJSONObject("status");
                                String suseed=jsonObject1.getString("succeed");
                                if("1".equals(suseed)){
                                    Toast.makeText(UpdateShipAddressActivity.this,"更改成功",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(UpdateShipAddressActivity.this,"更改失败",Toast.LENGTH_SHORT).show();
                                }
                                Intent intent=new Intent();
                                intent.putExtra(MyConstant.ADAPTER_KEY,"haha");
                                setResult(RESULT_OK,intent);
                                finish();
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
                    Map<String,String> map=new HashMap<>();
                    MyLog.d(tag,"uid="+uid+"....sid="+sid);
                    String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"address_id\":\""+userModel.getId()+"\",\"address\":{\"id\":\""+""+"\",\"consignee\":\""+shouHuoRenName+"\",\"email\":\""+""+"\",\"country\":\"1\",\"province\":\""+shengId+"\",\"city\":\""+shiId+"\",\"district\":\""+xianId+"\",\"address\":\""+xiangXiDiZhi+"\",\"zipcode\":\"\",\"tel\":\""+shouHuoRenPhone+"\",\"mobile\":\""+shouHuoRenPhone+"\",\"sign_building\":\""+""+"\",\"best_time\":\"\",\"default_address\":\"0\"}}";
                    map.put("json",json);
                    return map;
                }
            };
            requestQueue.add(updateAddressRequest);
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
        super.onBackPressed();
        Intent intent=new Intent();
        intent.putExtra(MyConstant.ADAPTER_KEY,"haha");
        setResult(RESULT_OK,intent);
        finish();
    }
}
