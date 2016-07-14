package com.example.asus_cp.dongmanbuy.activity.personal_center.data_set;

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
import com.example.asus_cp.dongmanbuy.util.CheckMobileAndEmail;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 改变手机号的界面
 * Created by asus-cp on 2016-06-23.
 */
public class ChangePhoneActivity extends Activity{

    private String tag="ChangePhoneActivity";

    private EditText editText;
    private Button confirmButton;

    private String editInfoUrl="http://www.zmobuy.com/PHP/?url=/user/editinfo";

    private RequestQueue requestQueue;

    private String uid;

    private String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_phone_activity_layout);
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

        editText= (EditText) findViewById(R.id.edit_change_phone);
        confirmButton= (Button) findViewById(R.id.btn_confirm_change_phone);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                if ("".equals(s) || s.isEmpty()) {
                    Toast.makeText(ChangePhoneActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else if (!CheckMobileAndEmail.isMobileNO(s)) {
                    Toast.makeText(ChangePhoneActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                } else {
                    //conirmClickChuLi(s);
                    Toast.makeText(ChangePhoneActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.putExtra(MyConstant.PHONE_KEY, s);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }



    /**
     * 确认的点击事件处理
     */
    private void conirmClickChuLi(final String param) {

        StringRequest changSexRequest=new StringRequest(Request.Method.POST, editInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "返回的数据是:" + s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            String str=jsonObject1.getString("succeed");
                            if("1".equals(str)){
                                Toast.makeText(ChangePhoneActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent();
                                intent.putExtra(MyConstant.EMAIL_KEY, param);
                                setResult(RESULT_OK, intent);
                            }else{
                                Toast.makeText(ChangePhoneActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangePhoneActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"sex\":\""+""+"\",\"email\":\""+param+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(changSexRequest);
    }

}
