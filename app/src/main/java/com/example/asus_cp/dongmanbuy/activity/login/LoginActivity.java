package com.example.asus_cp.dongmanbuy.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
 * 登录的界面
 * Created by asus-cp on 2016-05-27.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    private RequestQueue requestQueue;
    private String loginUrl="http://www.zmobuy.com/PHP/index.php?url=/user/signin";
    private String tag="LoginActivity";

    private int passwordFlag;//改变密码明码的标记

    private EditText zhangHaoEditText;//账号
    private EditText passWordEdtiText;//密码
    private ImageView seePassWordImagView;//设置显示密码还是明码
    private TextView forgetPassWord;//忘记密码
    private Button loginButton;//登录按钮
    private TextView userRegister;//用户注册
    private LinearLayout qqLinearLayout;//qq
    private LinearLayout weiBoLinearLayout;//微博
    private LinearLayout weiXinLinearLayout;//微信

    public static final String USER_NAME_KEY="userName";//向下一个activity传递username时的键
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//不要标题栏
        setContentView(R.layout.login_layout);

        requestQueue= MyApplication.getRequestQueue();

        zhangHaoEditText= (EditText) findViewById(R.id.edit_zhang_hao);
        passWordEdtiText= (EditText) findViewById(R.id.edit_password);
        seePassWordImagView= (ImageView) findViewById(R.id.img_see_password);
        forgetPassWord= (TextView) findViewById(R.id.text_forget_password);
        loginButton= (Button) findViewById(R.id.btn_login);
        userRegister= (TextView) findViewById(R.id.text_new_user_register);
        qqLinearLayout= (LinearLayout) findViewById(R.id.ll_qq);
        weiBoLinearLayout= (LinearLayout) findViewById(R.id.ll_wei_bo);
        weiXinLinearLayout= (LinearLayout) findViewById(R.id.ll_wei_xin);

        seePassWordImagView.setOnClickListener(this);
        forgetPassWord.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        userRegister.setOnClickListener(this);
        qqLinearLayout.setOnClickListener(this);
        weiBoLinearLayout.setOnClickListener(this);
        weiXinLinearLayout.setOnClickListener(this);
    }


    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_see_password:
                if(passwordFlag%2==0){
                    seePassWordImagView.setBackgroundResource(R.drawable.see_password_selected);
                    passWordEdtiText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    seePassWordImagView.setBackgroundResource(R.drawable.see_password_normal);
                    passWordEdtiText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                passwordFlag++;
                break;
            case R.id.text_forget_password://忘记密码
                String userName=zhangHaoEditText.getText().toString();
                Intent forgetIntent=new Intent(this,FindPassworByPhoneActivity.class);
                forgetIntent.putExtra(USER_NAME_KEY,userName);
                startActivity(forgetIntent);
                break;
            case R.id.btn_login://登录
                final String zhangHao=zhangHaoEditText.getText().toString();
                final String password=passWordEdtiText.getText().toString();
                if(zhangHao.equals("")||zhangHao.isEmpty()){
                    Toast.makeText(this,"账号为空",Toast.LENGTH_SHORT).show();
                }else if(password.equals("")||password.isEmpty()){
                    Toast.makeText(this,"密码为空",Toast.LENGTH_SHORT).show();
                }else {
                    StringRequest loginRequest=new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag, "登录的数据返回:" + s);
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                if(jsonObject1!=null){
                                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<String,String>();
                            map.put("name", zhangHao);
                            map.put("password", password);
                            //MyLog.d(tag,MyMd5.md5encode(password));
                            return map;
                        }
                    };
                    requestQueue.add(loginRequest);
                }

                break;
            case R.id.text_new_user_register://用户注册
                Intent registerIntent=new Intent(this,RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.ll_qq:
                Toast.makeText(this,"点击了qq登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_wei_bo:
                Toast.makeText(this,"点击了微博登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_wei_xin:
                Toast.makeText(this,"点击了微信登录",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
