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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码的确认界面
 * Created by asus-cp on 2016-05-30.
 */
public class ChangPasswordConfirmActivity extends Activity implements View.OnClickListener{
    private EditText inputNewPasswordEditText;
    private Button confirmChangeButton;
    private ImageView seePasswordImagView;
    private String email;//邮箱
    private String yanZhegnMa;//验证码

    private int passwordFlag;//密码和明码显示的标记

    private RequestQueue requestQueue;

    private String changUrl="http://www.zmobuy.com/PHP/?url=/user/getpasswordemail";

    private String tag="ChangPasswordConfirmActivity";
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
                final String newPassword=inputNewPasswordEditText.getText().toString().trim();
                if(newPassword.equals("")||newPassword.isEmpty()){
                    Toast.makeText(ChangPasswordConfirmActivity.this,"密码为空",Toast.LENGTH_SHORT).show();
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
                            MyLog.d(tag, "邮箱" + email);
                            MyLog.d(tag,"验证码"+yanZhegnMa.trim());
                            MyLog.d(tag,"新密码"+newPassword);
                            Map<String,String> map = new HashMap<String,String>();
                            String json="{\"username\":\"\",\"email\":\""+email+"\",\"email_code\":\""+yanZhegnMa+"\",\"sms_code\":\"\",\"mobile\":\"\",\"new_password\":\""+newPassword+"\"}";
                            map.put("json", json);
                            MyLog.d(tag,json);
                            return map;
                        }
                    };
                   requestQueue.add(stringRequest);//添加到队列中去
                }

                break;
        }
    }

    /**
     * 手动发起post请求，此方法作废
     * @param newPassword
     */
    private void shouDongPost(final String newPassword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    //String ceShiUrl="http://192.168.1.104:2006";
                    URL url=new URL(changUrl);
                    conn= (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10 * 1000);
                    conn.setReadTimeout(10 * 1000);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + "UTF-8");
                    OutputStream out=conn.getOutputStream();
                    String content="{\"username\":\"\",\"email\":\""+email+"\",\"email_code\":\""+yanZhegnMa+"\",\"sms_code\":\"\",\"mobile\":\"\",\"new_password\":\""+newPassword+"\"}";
                    content="json="+ URLEncoder.encode(content,"utf-8");
                    out.write(content.getBytes("utf-8"));
                    out.flush();
                    out.close();
                    InputStream in=conn.getInputStream();
                    byte[] buf=new byte[1024*1024];
                    in.read(buf);
                    MyLog.d(tag, "发送的数据是" + content);
                    MyLog.d(tag, new String(buf));
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    conn.disconnect();
                }
            }
        }).start();
    }
}
