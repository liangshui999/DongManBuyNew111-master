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
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加红包的界面
 * Created by asus-cp on 2016-07-14.
 */
public class AddHongBaoActivity extends Activity{

    private String tag="AddHongBaoActivity";

    private EditText kouLingEditText;
    private EditText passwordEditText;
    private Button tiJiaoButton;

    private String addHongBaoUrl="http://www.zmobuy.com/PHP/?url=/user/bonus_add";//添加红包的接口
    private RequestQueue requestQueue;
    private String uid;
    private String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_hong_bao_activity_layout);
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

        kouLingEditText= (EditText) findViewById(R.id.edit_hong_bao_kou_ling);
        passwordEditText= (EditText) findViewById(R.id.edit_hong_bao_password);
        tiJiaoButton= (Button) findViewById(R.id.btn_add_hong_bao);

        tiJiaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String kouLing=kouLingEditText.getText().toString();
                final String password=passwordEditText.getText().toString();
                if("".equals(kouLing) || kouLing.isEmpty()){
                    Toast.makeText(AddHongBaoActivity.this,"口令为空",Toast.LENGTH_SHORT).show();
                }else if("".equals(password) || password.isEmpty()){
                    Toast.makeText(AddHongBaoActivity.this,"密码为空",Toast.LENGTH_SHORT).show();
                }else{
                    StringRequest addRquest=new StringRequest(Request.Method.POST, addHongBaoUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    MyLog.d(tag, "返回的数据是：" + s);
                                    try {
                                        JSONObject jsonObject=new JSONObject(s);//这里返回的json数据有问题，前面有数字，所以需要先把数字踢掉
                                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                        JSONObject jsonObject2=jsonObject1.getJSONObject("status");
                                        String suceed=jsonObject2.getString("succeed");
                                        String errorDesc=jsonObject2.getString("error_desc");
                                        if("1".equals(suceed)){
                                            Toast.makeText(AddHongBaoActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                                            Intent intent=new Intent();
                                            setResult(RESULT_OK,intent);
                                            finish();
                                        }else if("0".equals(suceed)){
                                            Toast.makeText(AddHongBaoActivity.this,"添加失败（红包可能已经使用）",Toast.LENGTH_LONG).show();
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
                            String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"bonus_sn\":\""+kouLing+"\",\"bonus_password\":\""+password+"\"}";
                            map.put("json",json);
                            MyLog.d(tag,"uid="+uid+"..............."+"sid="+sid);
                            return map;
                        }
                    };
                    requestQueue.add(addRquest);
                }
            }
        });
    }
}
