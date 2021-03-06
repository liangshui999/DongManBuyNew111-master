package com.example.asus_cp.dongmanbuy.activity.personal_center.data_set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.example.asus_cp.dongmanbuy.util.CheckHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改邮箱的界面
 * Created by asus-cp on 2016-06-22.
 */
public class ChangeEmailActivity extends BaseActivity {

    private String tag="ChangeEmailActivity";

    private EditText editText;
    private Button confirmButton;

    private String editInfoUrl="http://www.zmobuy.com/PHP/?url=/user/editinfo";

    private User user;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.change_email_activity_layout);
        setTitle(R.string.change_email);
        initView();
        init();
    }

    @Override
    public void initView() {
        editText= (EditText) findViewById(R.id.edit_change_email);
        confirmButton= (Button) findViewById(R.id.btn_confirm_change_email);
    }

    /**
     * 初始化的方法
     */
    private void init() {
        user=getIntent().getParcelableExtra(MyConstant.USER_KEY);
        String sexcn=user.getSex();
        if("男".equals(sexcn)){
            sex=1+"";
        }else if("女".equals(sexcn)){
            sex=2+"";
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                if ("".equals(s) || s.isEmpty()) {
                    Toast.makeText(ChangeEmailActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                } else if (!CheckHelper.checkEmail(s)) {
                    Toast.makeText(ChangeEmailActivity.this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
                } else {
                    conirmClickChuLi(s);
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
                                Toast.makeText(ChangeEmailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent();
                                intent.putExtra(MyConstant.EMAIL_KEY, param);
                                setResult(RESULT_OK, intent);
                            }else{
                                Toast.makeText(ChangeEmailActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangeEmailActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
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
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"sex\":\""+sex+"\",\"email\":\""+param+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(changSexRequest);
    }

}
