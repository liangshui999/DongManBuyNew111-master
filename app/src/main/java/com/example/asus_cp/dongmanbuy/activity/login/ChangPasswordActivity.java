package com.example.asus_cp.dongmanbuy.activity.login;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码的确认界面
 * Created by asus-cp on 2016-05-30.
 */
public class ChangPasswordActivity extends Activity implements View.OnClickListener{
    private EditText inputNewPasswordEditText;
    private Button confirmChangeButton;
    private ImageView seePasswordImagView;
    private String email;//邮箱
    private String yanZhegnMa;//验证码

    private int passwordFlag;//密码和明码显示的标记

    private RequestQueue requestQueue;

    private String changUrl="http://www.zmobuy.com/PHP/index.php?url=/user/getpasswordemail";

    private String tag="ChangPasswordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chang_password_confirm_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        requestQueue= MyApplication.getRequestQueue();
        email=getIntent().getStringExtra(FindPassworByEmaildActivity.EMAIL_KEY);
        yanZhegnMa=getIntent().getStringExtra(FindByEmailYanZhengMaActiity.YAN_ZHENG_MA_KEY);
        inputNewPasswordEditText= (EditText) findViewById(R.id.edit_please_input_new_password);
        confirmChangeButton= (Button) findViewById(R.id.btn_confirm_change);
        seePasswordImagView= (ImageView) findViewById(R.id.img_see_password_confirm_change);
        seePasswordImagView.setOnClickListener(this);
        confirmChangeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_see_password_confirm_change://是明码显示密码，还是密码显示
                if(passwordFlag%2==0){
                    seePasswordImagView.setBackgroundResource(R.drawable.see_password_selected);
                    inputNewPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    seePasswordImagView.setBackgroundResource(R.drawable.see_password_normal);
                    inputNewPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                passwordFlag++;
                break;
            case R.id.btn_confirm_change://确认修改
                final String newPassword=inputNewPasswordEditText.getText().toString();
                if(newPassword.equals("")||newPassword.isEmpty()){
                    Toast.makeText(ChangPasswordActivity.this,"密码为空",Toast.LENGTH_SHORT).show();
                }else{
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, changUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag,"返回数据为："+s);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<String,String>();
//                            map.put("email", email);
//                            map.put("email_code", yanZhegnMa.trim());
//                            map.put("new_password",newPassword);

                            String json="{\"username\":\"\",\"email\":\""+email+"\",\"email_code\":\""+yanZhegnMa.trim()+"\",\"sms_code\":\"\",\"mobile\":\"\",\"new_password\":\""+newPassword+"\"}";
                            map.put("json", json);

                            try {
                                JSONObject jsonObject=new JSONObject(json);
                                MyLog.d(tag, jsonObject.getString("email")+"......"+jsonObject.getString("email_code")+"..........."+jsonObject.getString("new_password"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                MyLog.d(tag, e.toString());
                            }

                            MyLog.d(tag, "邮箱" + email);
                            MyLog.d(tag,"验证码"+yanZhegnMa.trim());
                            MyLog.d(tag,"新密码"+newPassword);
                            MyLog.d(tag,json);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);//添加到队列中去
                }
                break;
        }
    }
}
