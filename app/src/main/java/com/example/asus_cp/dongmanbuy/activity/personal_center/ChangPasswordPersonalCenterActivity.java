package com.example.asus_cp.dongmanbuy.activity.personal_center;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心的修改密码
 * Created by asus-cp on 2016-06-23.
 */
public class ChangPasswordPersonalCenterActivity extends Activity implements View.OnClickListener{

    private String tag="ChangPasswordPersonalCenterActivity";

    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText newPasswordAgainEditText;
    private ImageView oldPasswordImageView;
    private ImageView newPasswordImageView;
    private ImageView newPasswordAgainImageView;
    private Button confirmButton;

    private int oldPasswordFlag;
    private int newPasswordFlag;
    private int newPasswordAgainFlag;

    private String changePasswordUrl="http://www.zmobuy.com/PHP/?url=/user/editpassword";//修改密码的接口

    private String uid;
    private String sid;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_password_personal_center);
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

        oldPasswordEditText= (EditText) findViewById(R.id.edit_input_old_password);
        newPasswordEditText= (EditText) findViewById(R.id.edit_input_new_password);
        newPasswordAgainEditText= (EditText) findViewById(R.id.edit_input_new_password_again);
        oldPasswordImageView= (ImageView) findViewById(R.id.img_see_input_old_password);
        newPasswordImageView= (ImageView) findViewById(R.id.img_see_input_new_password);
        newPasswordAgainImageView= (ImageView) findViewById(R.id.img_see_input_new_password_again);
        confirmButton= (Button) findViewById(R.id.btn_confirm_change_password);

        //设置点击事件
        oldPasswordImageView.setOnClickListener(this);
        newPasswordImageView.setOnClickListener(this);
        newPasswordAgainImageView.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_see_input_old_password://点击了旧密码旁边的是否可见
                if(oldPasswordFlag%2==0){
                    oldPasswordImageView.setBackgroundResource(R.drawable.see_password_selected);
                    oldPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    oldPasswordImageView.setBackgroundResource(R.drawable.see_password_normal);
                    oldPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                oldPasswordFlag++;
                break;
            case R.id.img_see_input_new_password://点击了新密码旁边的是否可见
                if(newPasswordFlag%2==0){
                    newPasswordImageView.setBackgroundResource(R.drawable.see_password_selected);
                    newPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    newPasswordImageView.setBackgroundResource(R.drawable.see_password_normal);
                    newPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                newPasswordFlag++;
                break;
            case R.id.img_see_input_new_password_again://点击了再次输入新密码旁边的是否可见
                if(newPasswordAgainFlag%2==0){
                    newPasswordAgainImageView.setBackgroundResource(R.drawable.see_password_selected);
                    newPasswordAgainEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    newPasswordAgainImageView.setBackgroundResource(R.drawable.see_password_normal);
                    newPasswordAgainEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                newPasswordAgainFlag++;
                break;
            case R.id.btn_confirm_change_password://点击了确认修改
                final String oldPasswordImput=oldPasswordEditText.getText().toString();
                final String newPasswordImput=newPasswordEditText.getText().toString();
                String newPasswordAgainImput=newPasswordAgainEditText.getText().toString();
                if("".equals(oldPasswordImput) || oldPasswordImput.isEmpty()){
                    Toast.makeText(this,"旧密码为空",Toast.LENGTH_SHORT).show();
                }else if("".equals(newPasswordImput) || newPasswordImput.isEmpty()){
                    Toast.makeText(this,"新密码为空",Toast.LENGTH_SHORT).show();
                }else if("".equals(newPasswordAgainImput) || newPasswordAgainImput.isEmpty()){
                    Toast.makeText(this,"再次输入新密码为空",Toast.LENGTH_SHORT).show();
                }else if(newPasswordImput.length()<6){
                    Toast.makeText(this,"新密码长度不能小于6位",Toast.LENGTH_SHORT).show();
                }else if(!newPasswordImput.equals(newPasswordAgainImput)){
                    Toast.makeText(this,"新密码和再次输入新密码的内容不同",Toast.LENGTH_SHORT).show();
                }else{
                    commitPasswordToIntenet(oldPasswordImput, newPasswordImput);
                }

                break;
        }
    }


    /**
     * 向服务器端提交密码数据
     * @param oldPasswordImput
     * @param newPasswordImput
     */
    private void commitPasswordToIntenet(final String oldPasswordImput, final String newPasswordImput) {
        StringRequest changePasswordRequest=new StringRequest(Request.Method.POST, changePasswordUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "返回的数据是" + s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            String succeed=jsonObject1.getString("succeed");
                            String errorDesc=jsonObject1.getString("error_desc");
                            if("1".equals(succeed)){
                                Toast.makeText(ChangPasswordPersonalCenterActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                Intent toLoginIntent=new Intent(ChangPasswordPersonalCenterActivity.this, LoginActivity.class);
                                toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"changPassword");
                                startActivity(toLoginIntent);
                                finish();
                            }else{
                                Toast.makeText(ChangPasswordPersonalCenterActivity.this,errorDesc,Toast.LENGTH_SHORT).show();
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
                Map<String,String> map=new HashMap<String,String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"old_password\":\""+oldPasswordImput+"\",\"new_password\":\""+newPasswordImput+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(changePasswordRequest);
    }
}
