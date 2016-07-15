package com.example.asus_cp.dongmanbuy.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.util.CheckMobileAndEmail;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮箱找回密码的界面
 * Created by asus-cp on 2016-05-30.
 */
public class FindPassworByEmaildActivity extends Activity{
    private EditText inputEmailEditText;
    private Button nextStepButton;
    private TextView alsoFindByPhoneTextView;

    private String userName;//用户名

    private String findPasswordUrl="http://www.zmobuy.com/PHP/?url=/user/getpasswordemail";

    private RequestQueue requestQueue;//消息队列

    private String tag="FindPassworByEmaildActivity";

    public static final String EMAIL_KEY="emailKey";//传email的key
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
        requestQueue=MyApplication.getRequestQueue();
        inputEmailEditText= (EditText) findViewById(R.id.edit_email_num_find_password);
        nextStepButton= (Button) findViewById(R.id.btn_next_step_email);

        userName=getIntent().getStringExtra(LoginActivity.USER_NAME_KEY);

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行邮箱的验证
                final String email=inputEmailEditText.getText().toString();
                if(email.equals("")||email.isEmpty()){
                    Toast.makeText(FindPassworByEmaildActivity.this, "邮箱为空", Toast.LENGTH_SHORT).show();
                }else if(!CheckMobileAndEmail.checkEmail(email)){
                    Toast.makeText(FindPassworByEmaildActivity.this,"邮箱格式错误，@请用英文状态下的@",Toast.LENGTH_SHORT).show();
                }else{
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, findPasswordUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag,"返回的数据："+s);
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                String succeed=jsonObject1.getString("succeed");
                                if("1".equals(succeed)){
                                    Intent intent=new Intent(FindPassworByEmaildActivity.this,FindByEmailYanZhengMaActiity.class);
                                    intent.putExtra(EMAIL_KEY,email);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(FindPassworByEmaildActivity.this,"邮箱和用户名不匹配",Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(FindPassworByEmaildActivity.this,"邮箱和用户名不匹配",Toast.LENGTH_SHORT).show();
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
                    requestQueue.add(stringRequest);
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

}
