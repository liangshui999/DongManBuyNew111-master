package com.example.asus_cp.dongmanbuy.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

/**
 * 手机找回密码的界面
 * Created by asus-cp on 2016-05-30.
 */
public class FindPassworByPhoneActivity extends Activity{
    private EditText phoneNumberEditText;
    private Button nextButton;
    private TextView alsoFindByEmailTextView;
    private String userName;

    private String tag="FindPassworByPhoneActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.find_password_by_phone_layout);
        phoneNumberEditText= (EditText) findViewById(R.id.edit_phone_num_find_password);
        nextButton= (Button) findViewById(R.id.btn_next_step_phone);
        alsoFindByEmailTextView= (TextView) findViewById(R.id.text_also_find_by_email);

        phoneNumberEditText.clearFocus();

        userName=getIntent().getStringExtra(LoginActivity.USER_NAME_KEY);

        MyLog.d(tag, "tag有效吗");
        SpannableString spannableString=new SpannableString("您也可以通过邮箱找回");
        spannableString.setSpan(new NoLineClickSpan() {
                                         @Override
                                         public void onClick(View widget) {
                                             MyLog.d(tag,"新添加的点击了吗");
                                             Intent intent=new Intent(FindPassworByPhoneActivity.this,
                                                     FindPassworByEmaildActivity.class);
                                             intent.putExtra(LoginActivity.USER_NAME_KEY,userName);
                                             startActivity(intent);
                                            }
                                    },6,10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        alsoFindByEmailTextView.setText(spannableString);
        alsoFindByEmailTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //无下划线超链接，使用textColorLink、textColorHighlight分别修改超链接前景色和按下时的颜色
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
