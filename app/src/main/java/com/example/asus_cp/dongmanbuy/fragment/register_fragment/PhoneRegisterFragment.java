package com.example.asus_cp.dongmanbuy.fragment.register_fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.asus_cp.dongmanbuy.activity.personal_center.PersonalCenterActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.CheckHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机注册的碎片
 * Created by asus-cp on 2016-05-27.
 */
public class PhoneRegisterFragment extends Fragment implements View.OnClickListener{

    private String tag="PhoneRegisterFragment";
    private Context context;
    private RequestQueue requestQueue;

    private String phoneRegisterUrl="http://api.zmobuy.com/JK/base/model.php";

    private EditText phoneNumEditText;
    private EditText inputyanZhengMaEditText;
    private TextView sendYanZhengMaTextView;
    private EditText inputPasswordEditText;
    private EditText inputAgainPasswordEditText;
    private ImageView seePasswordImageView;//判断密码是否可见的imageview
    private ImageView seeAgainPasswordImageView;
    private Button registerButton;
    private TextView zhiJieLoginTextView;//已注册直接登录
    private int passwordFlag;//判断password的点击状态
    private int againPasswordFlag;//判断againpassword的状态

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.phone_register_fragment_layout,null);
        init(v);
        return v;
    }

    private void init(View v) {
        context=getActivity();
        requestQueue= MyApplication.getRequestQueue();

        phoneNumEditText= (EditText) v.findViewById(R.id.edit_phone_num);
        inputyanZhengMaEditText= (EditText) v.findViewById(R.id.edit_input_yan_zheng_ma);
        sendYanZhengMaTextView= (TextView) v.findViewById(R.id.text_send_yan_zheng_ma);
        inputPasswordEditText= (EditText) v.findViewById(R.id.edit_password_phone_register);
        inputAgainPasswordEditText= (EditText) v.findViewById(R.id.edit_password_again_phone_register);
        seePasswordImageView= (ImageView) v.findViewById(R.id.img_see_password_phone_register);
        seeAgainPasswordImageView= (ImageView) v.findViewById(R.id.img_see_password_again_phone_register);
        registerButton= (Button) v.findViewById(R.id.btn_phone_register);
        zhiJieLoginTextView= (TextView) v.findViewById(R.id.text_zhi_jie_login_phone_register);

        //设置点击事件
        sendYanZhengMaTextView.setOnClickListener(this);
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
            case R.id.text_send_yan_zheng_ma://发送验证码
                Toast.makeText(context,"点击了发送验证码",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_phone_register://点击了注册按钮
                zhuCeClickChuLi();
                break;
            case R.id.text_zhi_jie_login_phone_register://点击了直接登录
                Intent intent=new Intent(context, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.img_see_password_phone_register:
                if(passwordFlag%2==0){
                    seePasswordImageView.setBackgroundResource(R.drawable.see_password_selected);
                    inputPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    seePasswordImageView.setBackgroundResource(R.drawable.see_password_normal);
                    inputPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                passwordFlag++;
                break;
            case R.id.img_see_password_again_phone_register:
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



    /**
     * 注册按钮的点击事件处理
     */
    private void zhuCeClickChuLi() {
        final String phoneNumber=phoneNumEditText.getText().toString();
        final String yanZhengMa=inputyanZhengMaEditText.getText().toString();
        final String passWord=inputPasswordEditText.getText().toString();
        String aginPassWord=inputAgainPasswordEditText.getText().toString();
        //final String jiaMiPassWord= MyMd5.md5encode(passWord);//对password进行md5加密
        if(phoneNumber.equals("")||phoneNumber.isEmpty()){
            Toast.makeText(context, "用户名为空", Toast.LENGTH_SHORT).show();
        }else if(!CheckHelper.checkEmail(phoneNumber)){
            Toast.makeText(context,"手机格式错误，请输入正确的手机号",Toast.LENGTH_SHORT).show();
        }else if(passWord.equals("")||passWord.isEmpty()){
            Toast.makeText(context,"密码为空",Toast.LENGTH_SHORT).show();
        }else if(aginPassWord.equals("")||aginPassWord.isEmpty()){
            Toast.makeText(context,"再次输入密码为空",Toast.LENGTH_SHORT).show();
        }else if(!passWord.equals(aginPassWord)){
            Toast.makeText(context,"2次密码不相等",Toast.LENGTH_SHORT).show();
        }else{
            StringRequest registerRequest=new StringRequest(Request.Method.POST, phoneRegisterUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    MyLog.d(tag, "返回的数据是：" + s);
                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        JSONObject jsonObject2=jsonObject1.getJSONObject("session");
                        String sid=jsonObject2.getString("sid");
                        String uid=jsonObject2.getString("uid");
                        if(!sid.isEmpty() && !"".equals(sid)){
                            Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT).show();
                            clearSharePerefrences();//清空之前保存的数据
                            writeToSharePreferences(uid, MyConstant.UID_KEY);
                            writeToSharePreferences(sid, MyConstant.SID_KEY);
                            writeToSharePreferences(phoneNumber, MyConstant.USER_NAME);
                            writeToSharePreferences(passWord, MyConstant.PASS_WORD);//将加密后的密码保存
                            //跳转到个人中心
                            Intent intent=new Intent(context, PersonalCenterActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(context,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context,"注册失败，账号或者邮箱已经注册过了",Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();

                    return map;
                }
            };
            requestQueue.add(registerRequest);
        }
    }



    /**
     * 清空shareprefrence
     */
    public void clearSharePerefrences(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 将数据写入shareprefrence里面
     * @param s 要写入的字符串
     * @param key 写入时的键
     */
    public void writeToSharePreferences(String s,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,s);
        editor.apply();
    }

}
