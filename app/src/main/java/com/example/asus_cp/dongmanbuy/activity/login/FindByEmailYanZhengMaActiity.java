package com.example.asus_cp.dongmanbuy.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.R;

/**
 * 邮箱找回密码，输入验证码的界面
 * Created by asus-cp on 2016-05-30.
 */
public class FindByEmailYanZhengMaActiity extends Activity {
    private TextView emailAddressTextView;
    private EditText yanZhengMaEdtiText;
    private Button nextStepButton;

    private String email;

    private String yanZhengMa;

    public static final String YAN_ZHENG_MA_KEY="yanZhengMaKey";//传递验证码的key
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.find_by_email_yan_zheng_ma_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        email=getIntent().getStringExtra(FindPassworByEmaildActivity.EMAIL_KEY);
        yanZhengMaEdtiText= (EditText) findViewById(R.id.edit_yan_zheng_ma_find_by_email);
        nextStepButton= (Button) findViewById(R.id.btn_next_step_email_yan_zheng_ma);
        emailAddressTextView= (TextView) findViewById(R.id.text_email_address_yan_zheng);

        //设置邮箱地址
        emailAddressTextView.setText(email);

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yanZhengMa=yanZhengMaEdtiText.getText().toString();
                if(yanZhengMa.equals("")||yanZhengMa.isEmpty()){
                    Toast.makeText(FindByEmailYanZhengMaActiity.this,"验证码为空",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(FindByEmailYanZhengMaActiity.this,ChangPasswordActivity.class);
                    intent.putExtra(FindPassworByEmaildActivity.EMAIL_KEY,email);
                    intent.putExtra(YAN_ZHENG_MA_KEY,yanZhengMa);
                    startActivity(intent);
                }
            }
        });
    }
}
