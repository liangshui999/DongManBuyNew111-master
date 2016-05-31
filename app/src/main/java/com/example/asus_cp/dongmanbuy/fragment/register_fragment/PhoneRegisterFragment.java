package com.example.asus_cp.dongmanbuy.fragment.register_fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.R;

/**
 * 手机注册的碎片
 * Created by asus-cp on 2016-05-27.
 */
public class PhoneRegisterFragment extends Fragment implements View.OnClickListener{
    private Context context;

    private EditText phoneNumEditText;
    private EditText inputyanZhengMaEditText;
    private TextView sendYanZhengMaTextView;
    private EditText inputPasswordEditText;
    private EditText inputAgainPasswordEditText;
    private ImageView seePasswordImageView;//判断密码是否可见的imageview
    private ImageView seeAgainPasswordImageView;
    private Button registerButton;
    private TextView zhiJieLoginTextView;//已注册直接登录
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.phone_register_fragment_layout,null);
        init(v);
        return v;
    }

    private void init(View v) {
        context=getActivity();

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
                Toast.makeText(context,"点击了注册按钮",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_zhi_jie_login_phone_register://点击了直接登录
                Toast.makeText(context,"点击了直接登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_see_password_phone_register:
                Toast.makeText(context,"点击了改变密码明码",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_see_password_again_phone_register:
                Toast.makeText(context,"点击了重新改变密码明码",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
