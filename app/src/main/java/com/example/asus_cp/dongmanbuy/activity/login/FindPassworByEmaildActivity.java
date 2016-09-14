package com.example.asus_cp.dongmanbuy.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
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
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.CheckHelper;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 邮箱找回密码的界面
 * Created by asus-cp on 2016-05-30.
 */
public class FindPassworByEmaildActivity extends Activity{
    private ImageView daoHangImageView;
    private EditText inputEmailEditText;
    private Button nextStepButton;
    private TextView alsoFindByPhoneTextView;

    private String userName;//用户名

    private String findPasswordUrl="http://www.zmobuy.com/PHP/?url=/user/getpasswordemail";

    private RequestQueue requestQueue;//消息队列

    private String tag="FindPassworByEmaildActivity";

    public static final String EMAIL_KEY="emailKey";//传email的key

    private static final int LOAD_COMPLETE=1;

    private static final int TOAST_KEY=2;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_COMPLETE:
                    DialogHelper.dissmisDialog();
                    break;
                case TOAST_KEY:
                    String s= (String) msg.obj;
                    Toast.makeText(FindPassworByEmaildActivity.this,s,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.find_password_by_email_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
//        Looper.prepare();
        requestQueue=MyApplication.getRequestQueue();
        daoHangImageView= (ImageView) findViewById(R.id.img_dao_hang_find_by_email);
        inputEmailEditText= (EditText) findViewById(R.id.edit_email_num_find_password);
        nextStepButton= (Button) findViewById(R.id.btn_next_step_email);

        userName=getIntent().getStringExtra(LoginActivity.USER_NAME_KEY);

        daoHangImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行邮箱的验证
                final String email=inputEmailEditText.getText().toString();
                if(email.equals("")||email.isEmpty()){
                    Toast.makeText(FindPassworByEmaildActivity.this, "邮箱为空", Toast.LENGTH_SHORT).show();
                }else if(!CheckHelper.checkEmail(email)){
                    Toast.makeText(FindPassworByEmaildActivity.this,"邮箱格式错误，@请用英文状态下的@",Toast.LENGTH_SHORT).show();
                }else{
                    /*StringRequest stringRequest=new StringRequest(Request.Method.POST, findPasswordUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag,"返回的数据："+s);
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                String succeed=jsonObject1.getString("succeed");
                                if("1".equals(succeed)){
                                    Intent intent=new Intent(FindPassworByEmaildActivity.this,FindByEmailYanZhengMaActiity.class);
                                    intent.putExtra(EMAIL_KEY, email);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(FindPassworByEmaildActivity.this,"邮箱不存在",Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(FindPassworByEmaildActivity.this,"邮箱不存在",Toast.LENGTH_SHORT).show();
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
                            Map<String,String> map = new HashMap<String,String>();
                            MyLog.d(tag,"email的值为："+email);
                            String json="{\"username\":\"\",\"email\":\""+email+"\",\"email_code\":\"\",\"sms_code\":\"\",\"mobile\":\"\",\"new_password\":\"\"}";
                            map.put("json",json);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);*/
                    shouDongPost(email);
                }
            }
        });



        //跳转到通过手机找回
        alsoFindByPhoneTextView = (TextView) findViewById(R.id.text_also_find_by_phone);
        SpannableString spannableString=new SpannableString("您也可以通过手机找回");
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.bottom_lable_color));
        spannableString.setSpan(span, 6, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new NoLineClickSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent=new Intent(FindPassworByEmaildActivity.this,FindPassworByPhoneActivity.class);
                startActivity(intent);
            }
        },6, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        alsoFindByPhoneTextView.setText(spannableString);
        alsoFindByPhoneTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * 无下划线超链接，使用textColorLink、textColorHighlight分别修改超链接前景色和按下时的颜色
     */
    class NoLineClickSpan extends ClickableSpan {
        public NoLineClickSpan() {
            super();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(MyApplication.getContext().getResources().getColor(R.color.bottom_lable_color));
            ds.setUnderlineText(false); //去掉下划线</span>
        }

        @Override
        public void onClick(View widget) {
        }
    }


    /**
     * 手动发起post请求
     *
     */
    private void shouDongPost(final String email) {
        DialogHelper.showDialog(this,"正在等待验证码...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    //String ceShiUrl="http://192.168.1.104:2006";
                    URL url=new URL(findPasswordUrl);
                    conn= (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10 * 1000);
                    conn.setReadTimeout(10 * 1000);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
//                    sid="ECS_ID=fda22a552433c1c8408dcbe85e702861cfcb487c";
//                    conn.setRequestProperty("Cookie", sid);// 有网站需要将当前的session id一并上传
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + "UTF-8");
                    OutputStream out=conn.getOutputStream();
                    String content="{\"username\":\"\",\"email\":\""+email+"\",\"email_code\":\""+""+"\",\"sms_code\":\"\",\"mobile\":\"\",\"new_password\":\""+""+"\"}";
                    content="json="+ URLEncoder.encode(content, "utf-8");
                    out.write(content.getBytes("utf-8"));
                    out.flush();
                    out.close();

                    Map<String, List<String>>mapp=conn.getHeaderFields();
                    Set<Map.Entry<String,List<String>>> sett=mapp.entrySet();
                    for(Map.Entry<String,List<String>> m:sett){
                        if("Set-Cookie".equals(m.getKey())){
                            SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
                            String sessionId=sharedPreferences.getString(MyConstant.SESSION_ID_KEY,null);
                            if(sessionId!=null){
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.remove(MyConstant.SESSION_ID_KEY);
                                editor.apply();
                            }
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString(MyConstant.SESSION_ID_KEY,FormatHelper.getSessionId(m.getValue()+""));
                            editor.apply();
                            MyLog.d(tag,"SessionId："+ FormatHelper.getSessionId(m.getValue()+""));
                        }
                        MyLog.d(tag,"key："+m.getKey());
                        MyLog.d(tag,"value："+m.getValue());
                    }
                    String responseCookie = conn.getHeaderField("Set-Cookie");// 取到服务端返回的Cookie,这个cooke返回的不全
                    MyLog.d(tag, "服务器返回的cookes："+responseCookie+"...."+conn.getResponseMessage()+"....."+conn.getResponseCode());

                    if(conn.getResponseCode()==200){
                        InputStream in=conn.getInputStream();
                        byte[] buf=new byte[1024*10];
                        in.read(buf);
                        String responses=new String(buf);
                        String s=responses.trim();
                        Message message=handler.obtainMessage(LOAD_COMPLETE);//关闭对话框
                        handler.sendMessage(message);
                        MyLog.d(tag, "发送的数据是" + content);
                        MyLog.d(tag, "返回的数据是："+responses.trim());
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            String succeed=jsonObject1.getString("succeed");
                            if("1".equals(succeed)){
                                Intent intent=new Intent(FindPassworByEmaildActivity.this,FindByEmailYanZhengMaActiity.class);
                                intent.putExtra(EMAIL_KEY, email);
                                startActivity(intent);
                            }else{
                                String error= JsonHelper.decodeUnicode(jsonObject1.getString("error_desc"));
                                MyLog.d(tag,"邮箱不存在");
                                Message message1=handler.obtainMessage(TOAST_KEY,error);
                                handler.sendMessage(message1);
                            }
                        } catch (Exception e) {
                            Message message2=handler.obtainMessage(TOAST_KEY,"邮箱不存在");
                            handler.sendMessage(message2);
                            e.printStackTrace();
                            MyLog.d(tag,e.toString());
                        }

                    }else{
                        Toast.makeText(FindPassworByEmaildActivity.this,"网络请求失败",Toast.LENGTH_SHORT).show();
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
