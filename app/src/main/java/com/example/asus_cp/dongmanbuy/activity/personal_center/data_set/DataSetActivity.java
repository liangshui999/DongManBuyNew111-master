package com.example.asus_cp.dongmanbuy.activity.personal_center.data_set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.MainActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.User;
import com.example.asus_cp.dongmanbuy.service.UidService;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 资料设置的界面
 * Created by asus-cp on 2016-06-22.
 */
public class DataSetActivity extends Activity implements View.OnClickListener{

    private ImageView daoHangImageView;//导航
    private de.hdodenhof.circleimageview.CircleImageView touXiangImageView;//头像
    private TextView nameTextView;//姓名
    private RelativeLayout sexRelativeLayout;//性别
    private TextView sexTextView;//性别
    private RelativeLayout phoneRelativeLayout;//手机
    private TextView phoneTextView;//手机
    private RelativeLayout emailRelativeLayout;//邮箱
    private TextView emailTextView;//邮箱
    private RelativeLayout changePassWordRelativeLayout;//修改密码
    private RelativeLayout shipAddressRelativeLayout;//收货地址
    private Button exitButton;//退出

    private User user;

    private ImageLoadHelper helper;

    public static final int REQUEST_CODE_SEX=1;//跳转到性别的修改界面
    public static final int REQUEST_CODE_PHONE=2;
    public static final int REQUEST_CODE_EMAIL=3;

    private String whoStartMe;//谁开启了我




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.data_set_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {

        user=getIntent().getParcelableExtra(MyConstant.USER_KEY);
        helper=new ImageLoadHelper();
        whoStartMe=getIntent().getStringExtra(MyConstant.DATA_SET_ACTIVITY_LABLE_FLAG_KEY);//谁开启了我

        daoHangImageView= (ImageView) findViewById(R.id.img_dao_hang_data_set);
        touXiangImageView= (CircleImageView) findViewById(R.id.img_tou_xiang_data_set);
        nameTextView= (TextView) findViewById(R.id.text_name_data_set);
        sexRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_sex_data_set);
        sexTextView= (TextView) findViewById(R.id.text_sex_data_set);
        phoneRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_phone_data_set);
        phoneTextView= (TextView) findViewById(R.id.text_phone_data_set);
        emailRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_email_data_set);
        emailTextView= (TextView) findViewById(R.id.text_email_data_set);
        changePassWordRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_change_password_data_set);
        shipAddressRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_ship_address_data_set);
        exitButton= (Button) findViewById(R.id.btn_exit_data_set);

        //设置值
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(touXiangImageView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);

        if(user!=null){
            imageLoader.get(MyConstant.YU_MING + user.getPic(), listener, 200, 200);

            nameTextView.setText(user.getName());
            sexTextView.setText(user.getSex());
            phoneTextView.setText(user.getPhone());
            emailTextView.setText(user.getEmail());
        }



        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        sexRelativeLayout.setOnClickListener(this);
        phoneRelativeLayout.setOnClickListener(this);
        emailRelativeLayout.setOnClickListener(this);
        changePassWordRelativeLayout.setOnClickListener(this);
        shipAddressRelativeLayout.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_dao_hang_data_set://导航
                finish();
                break;
            case R.id.re_layout_sex_data_set://点击了性别
                Intent toSexIntent=new Intent(this,SexSelectActivity.class);
                toSexIntent.putExtra(MyConstant.USER_KEY,user);
                startActivityForResult(toSexIntent, REQUEST_CODE_SEX);
                break;
            case R.id.re_layout_phone_data_set://点击了手机
                Intent toPhoneIntent=new Intent(this,ChangePhoneActivity.class);//注意要修改后一个参数
                startActivityForResult(toPhoneIntent,REQUEST_CODE_PHONE);
                break;
            case R.id.re_layout_email_data_set://点击邮箱
                Intent toEmailIntent=new Intent(this,ChangeEmailActivity.class);
                toEmailIntent.putExtra(MyConstant.USER_KEY,user);
                startActivityForResult(toEmailIntent, REQUEST_CODE_EMAIL);
                break;
            case R.id.re_layout_change_password_data_set://点击了修改密码
                Intent toChangePasswordIntent=new Intent(this,ChangPasswordPersonalCenterActivity.class);
                startActivity(toChangePasswordIntent);
                break;
            case R.id.re_layout_ship_address_data_set://点击了收货地址
                Intent toEditShipAddressListIntent=new Intent(this,EditShipAddressActivity.class);
                startActivity(toEditShipAddressListIntent);
                break;
            case R.id.btn_exit_data_set://点击了退出按钮
                SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,
                        MODE_APPEND);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent serviceIntent=new Intent(this, UidService.class);
                stopService(serviceIntent);
                Intent backIntent=new Intent();
                setResult(RESULT_OK, backIntent);

                if(whoStartMe!=null){   //说明从个人中心跳转过来的
                    Intent toMainActivity=new Intent(this, MainActivity.class);
                    startActivity(toMainActivity);
                    finish();
                }else{  //说明从主页跳转过来的
                    finish();
                }

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_SEX:
                if(resultCode==RESULT_OK){
                    String sex=data.getStringExtra(MyConstant.SEX_KEY);
                    sexTextView.setText(sex);
                }
                break;
            case REQUEST_CODE_PHONE:
                if(resultCode==RESULT_OK){
                    String phone=data.getStringExtra(MyConstant.PHONE_KEY);
                    phoneTextView.setText(phone);
                }
                break;
            case REQUEST_CODE_EMAIL:
                if(resultCode==RESULT_OK){
                    String email=data.getStringExtra(MyConstant.EMAIL_KEY);
                    emailTextView.setText(email);
                }
                break;
        }
    }
}
