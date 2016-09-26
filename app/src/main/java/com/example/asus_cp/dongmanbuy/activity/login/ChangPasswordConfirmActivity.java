package com.example.asus_cp.dongmanbuy.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 修改密码的确认界面
 * Created by asus-cp on 2016-05-30.
 */
public class ChangPasswordConfirmActivity extends BaseActivity implements View.OnClickListener{

    private EditText inputNewPasswordEditText;
    private Button confirmChangeButton;
    private ImageView seePasswordImagView;
    private String email;//邮箱
    private String yanZhegnMa;//验证码

    private int passwordFlag;//密码和明码显示的标记

    private String changUrl="http://www.zmobuy.com/PHP/?url=/user/getpasswordemail";

    private String tag="ChangPasswordConfirmActivity";

    private String sid;

    public static final int CHANGE_SUCEED=1;
    public static final int CHANGE_FALSE=2;//修改失败
    public static final int NET_FALSE=3;//网络请求失败

    private MyHandler handler=new MyHandler();

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String errorDesc= (String) msg.obj;
            switch (msg.what){
                case CHANGE_SUCEED:
                    Toast.makeText(ChangPasswordConfirmActivity.this,errorDesc,Toast.LENGTH_SHORT).show();
                    break;
                case CHANGE_FALSE:
                    Toast.makeText(ChangPasswordConfirmActivity.this,errorDesc,Toast.LENGTH_SHORT).show();
                    break;
                case NET_FALSE:
                    Toast.makeText(ChangPasswordConfirmActivity.this,errorDesc,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.chang_password_confirm_layout);
        setTitle(R.string.confirm_change);
        initView();
        init();
    }

    @Override
    public void initView() {
        email=getIntent().getStringExtra(FindPassworByEmaildActivity.EMAIL_KEY);
        yanZhegnMa=getIntent().getStringExtra(FindByEmailYanZhengMaActiity.YAN_ZHENG_MA_KEY);
        inputNewPasswordEditText= (EditText) findViewById(R.id.edit_please_input_new_password);
        confirmChangeButton= (Button) findViewById(R.id.btn_confirm_change);
        seePasswordImagView= (ImageView) findViewById(R.id.img_see_password_confirm_change);

        //设置点击事件
        seePasswordImagView.setOnClickListener(this);
        confirmChangeButton.setOnClickListener(this);
    }

    /**
     * 初始化的方法
     */
    private void init() {
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,"");
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
                    /*StringRequest stringRequest=new StringRequest(Request.Method.POST, changUrl, new Response.Listener<String>() {
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
                            String json="{\"username\":\"\",\"email\":\"254304837@qq.com\",\"email_code\":\""+yanZhegnMa+"\",\"sms_code\":\"\",\"mobile\":\"\",\"new_password\":\"1234567\"}";
                            //String json="{\"username\":\"\",\"email\":\""+email+"\",\"email_code\":\""+yanZhegnMa+"\",\"sms_code\":\"\",\"mobile\":\"\",\"new_password\":\""+newPassword+"\"}";
                            map.put("json", json);
                            MyLog.d(tag,json);
                            return map;
                        }
                    };
                   requestQueue.add(stringRequest);//添加到队列中去*/

                    shouDongPost(newPassword);
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
                    sid=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND).getString(MyConstant.SESSION_ID_KEY,null);
                    MyLog.d(tag,"sid="+sid);
                    conn.setRequestProperty("Cookie", sid);// 有网站需要将当前的session id一并上传
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + "UTF-8");
                    OutputStream out=conn.getOutputStream();
                    String content="{\"username\":\"\",\"email\":\""+email+"\",\"email_code\":\""+yanZhegnMa+"\",\"sms_code\":\"\",\"mobile\":\"\",\"new_password\":\""+newPassword+"\"}";
                    content="json="+ URLEncoder.encode(content,"utf-8");
                    out.write(content.getBytes("utf-8"));
                    out.flush();
                    out.close();
                    Map<String, List<String>>mapp=conn.getHeaderFields();
                    Set<Map.Entry<String,List<String>>> sett=mapp.entrySet();
                    for(Map.Entry<String,List<String>> m:sett){
                        if("Set-Cookie".equals(m.getKey())){

                        }
                        MyLog.d(tag,"key："+m.getKey());
                        MyLog.d(tag,"value："+m.getValue());
                    }
                    String responseCookie = conn.getHeaderField("Set-Cookie");// 取到服务端返回的Cookie
                    MyLog.d(tag, "服务器返回的cookes："+responseCookie+"...."+conn.getResponseMessage()+"....."+conn.getResponseCode());

                    if(conn.getResponseCode()==200){
                        InputStream in=conn.getInputStream();
                        byte[] buf=new byte[1024*10];
                        in.read(buf);
                        String respnses=new String(buf);
                        String s=respnses.trim();
                        MyLog.d(tag, "发送的数据是" + content);
                        MyLog.d(tag, "返回的数据是："+s);
                        JSONObject jsonObject=new JSONObject(s);
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        String succedd=jsonObject1.getString("succeed");
                        String error_desc= JsonHelper.decodeUnicode(jsonObject1.getString("error_desc"));
                        if("1".equals(succedd)){    //找回密码成功
                            Message message=handler.obtainMessage(CHANGE_SUCEED);
                            message.obj=error_desc;
                            handler.sendMessage(message);
                            Intent intent=new Intent(ChangPasswordConfirmActivity.this,LoginActivity.class);
                            intent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"findPassword");
                            startActivity(intent);
                        }else{  //找回密码失败
                           // Toast.makeText(ChangPasswordConfirmActivity.this,error_desc,Toast.LENGTH_SHORT).show();
                            Message message=handler.obtainMessage(CHANGE_FALSE);
                            message.obj=error_desc;
                            handler.sendMessage(message);
                        }
                    }else{
                        //Toast.makeText(ChangPasswordConfirmActivity.this,"网络请求失败",Toast.LENGTH_SHORT).show();
                        Message message=handler.obtainMessage(NET_FALSE);
                        message.obj="找回密码失败";
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    conn.disconnect();
                }
            }
        }).start();
    }
}
