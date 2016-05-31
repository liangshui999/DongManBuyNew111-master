package com.example.asus_cp.dongmanbuy.fragment.register_fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.util.CheckMobileAndEmail;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮箱注册的碎片
 * Created by asus-cp on 2016-05-27.
 */
public class EmailRegisterFragment extends Fragment implements View.OnClickListener{

    private String tag="EmailRegisterFragment";

    private Context context;

    private RequestQueue requestQueue;

    private String userName;//用户输入的用户名
    private String email;//用户输入的邮箱
    private String password;//用户输入的密码
    private String confirmPassword;//用户的确认密码

    private EditText userNameEditText;
    private EditText inputEmailEditText;
    private EditText inputPasswordEditText;
    private EditText inputAgainPasswordEditText;
    private ImageView seePasswordImageView;//判断密码是否可见的imageview
    private ImageView seeAgainPasswordImageView;
    private Button registerButton;
    private TextView zhiJieLoginTextView;//已注册直接登录

    private int passwordFlag;//判断password的点击状态
    private int againPasswordFlag;//判断againpassword的状态

    private String requestUrl="http://www.zmobuy.com/PHP/index.php?url=/user/signup";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.email_register_fragment_layout,container,false);
        init(v);
        return v;
    }

    private void init(View v) {
        context=getActivity();
        requestQueue= MyApplication.getRequestQueue();
        userNameEditText = (EditText) v.findViewById(R.id.edit_user_name);
        inputEmailEditText = (EditText) v.findViewById(R.id.edit_input_email);

        inputPasswordEditText= (EditText) v.findViewById(R.id.edit_password_email_register);
        inputAgainPasswordEditText= (EditText) v.findViewById(R.id.edit_password_again_email_register);
        seePasswordImageView= (ImageView) v.findViewById(R.id.img_see_password_email_register);
        seeAgainPasswordImageView= (ImageView) v.findViewById(R.id.img_see_password_again_email_register);
        registerButton= (Button) v.findViewById(R.id.btn_email_register);
        zhiJieLoginTextView= (TextView) v.findViewById(R.id.text_zhi_jie_login_email_register);

        //设置点击事件

        seePasswordImageView.setOnClickListener(this);
        seeAgainPasswordImageView.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        zhiJieLoginTextView.setOnClickListener(this);
    }

    /**
     * 点击事件的设置
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_email_register://点击了注册按钮
                final String userName=userNameEditText.getText().toString();
                final String email=inputEmailEditText.getText().toString();
                final String passWord=inputPasswordEditText.getText().toString();
                String aginPassWord=inputAgainPasswordEditText.getText().toString();
                if(userName.equals("")||userName.isEmpty()){
                    Toast.makeText(context,"用户名为空",Toast.LENGTH_SHORT).show();
                }else if(!CheckMobileAndEmail.checkEmail(email)){
                    Toast.makeText(context,"邮箱格式错误,输入@时请用英文状态下的@",Toast.LENGTH_SHORT).show();
                }else if(passWord.equals("")||passWord.isEmpty()){
                    Toast.makeText(context,"密码为空",Toast.LENGTH_SHORT).show();
                }else if(aginPassWord.equals("")||aginPassWord.isEmpty()){
                    Toast.makeText(context,"再次输入密码为空",Toast.LENGTH_SHORT).show();
                }else if(!passWord.equals(aginPassWord)){
                    Toast.makeText(context,"2次密码不相等",Toast.LENGTH_SHORT).show();
                }else{
                    StringRequest registerRequest=new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT).show();
                            MyLog.d(tag,"返回的数据是："+s);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<String,String>();
                            map.put("name", userName);
                            map.put("email", email);
                            map.put("password", passWord);
                            return map;
                        }
                    };
                    requestQueue.add(registerRequest);
                }
                break;
            case R.id.text_zhi_jie_login_email_register://点击了直接登录
                Intent intent=new Intent(context, LoginActivity.class);
                startActivity(intent);

                break;
            case R.id.img_see_password_email_register://点击了改变密码明码
                if(passwordFlag%2==0){
                    seePasswordImageView.setBackgroundResource(R.drawable.see_password_selected);
                    inputPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    seePasswordImageView.setBackgroundResource(R.drawable.see_password_normal);
                    inputPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                passwordFlag++;

                break;
            case R.id.img_see_password_again_email_register://重新改变密码明码
                if(againPasswordFlag%2==0){
                    seeAgainPasswordImageView.setBackgroundResource(R.drawable.see_password_selected);
                    inputAgainPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    seeAgainPasswordImageView.setBackgroundResource(R.drawable.see_password_normal);
                    inputAgainPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                againPasswordFlag++;
                break;
        }
    }
}
