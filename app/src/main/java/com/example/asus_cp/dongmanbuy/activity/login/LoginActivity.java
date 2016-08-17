package com.example.asus_cp.dongmanbuy.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.product_detail.QueHuoDengJiActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.constant.SinaConstants;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.SinaAccessTokenKeeper;
import com.example.asuscp.dongmanbuy.util.JieKouHelper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录的界面
 * Created by asus-cp on 2016-05-27.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    private RequestQueue requestQueue;
    private String loginUrl;
    private String tag="LoginActivity";

    private int passwordFlag;//改变密码明码的标记

    private ImageView daoHangImageView;//导航
    private EditText zhangHaoEditText;//账号
    private EditText passWordEdtiText;//密码
    private ImageView seePassWordImagView;//设置显示密码还是明码
    private TextView forgetPassWord;//忘记密码
    private Button loginButton;//登录按钮
    private TextView userRegister;//用户注册
    private LinearLayout qqLinearLayout;//qq
    private LinearLayout weiBoLinearLayout;//微博
    private LinearLayout weiXinLinearLayout;//微信

    private String whoStartMe;//谁开启的我

    public static final String USER_NAME_KEY="userName";//向下一个activity传递username时的键

    private JieKouHelper jieKouHelper;

    private AuthInfo mAuthInfo;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//不要标题栏
        setContentView(R.layout.login_layout);

        requestQueue= MyApplication.getRequestQueue();
        whoStartMe=getIntent().getStringExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY);//谁开启了我
        jieKouHelper=new JieKouHelper();
        loginUrl=jieKouHelper.getLoginUrl();//返回加密的登陆接口
        MyLog.d(tag, "登陆的接口数据是" + jieKouHelper.getLoginUrl());
        MyLog.d(tag,"注册接口返回的数据是："+jieKouHelper.getRegisterUrl());

        initView();

        // 创建微博实例
        //mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, SinaConstants.APP_KEY, SinaConstants.REDIRECT_URL,null);
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);

    }


    /**
     * 初始化视图
     */
    private void initView() {
        daoHangImageView= (ImageView) findViewById(R.id.img_dao_hang_login);
        zhangHaoEditText= (EditText) findViewById(R.id.edit_zhang_hao);
        passWordEdtiText= (EditText) findViewById(R.id.edit_password);
        seePassWordImagView= (ImageView) findViewById(R.id.img_see_password);
        forgetPassWord= (TextView) findViewById(R.id.text_forget_password);
        loginButton= (Button) findViewById(R.id.btn_login);
        userRegister= (TextView) findViewById(R.id.text_new_user_register);
        qqLinearLayout= (LinearLayout) findViewById(R.id.ll_qq);
        weiBoLinearLayout= (LinearLayout) findViewById(R.id.ll_wei_bo);
        weiXinLinearLayout= (LinearLayout) findViewById(R.id.ll_wei_xin);

        daoHangImageView.setOnClickListener(this);
        seePassWordImagView.setOnClickListener(this);
        forgetPassWord.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        userRegister.setOnClickListener(this);
        qqLinearLayout.setOnClickListener(this);
        weiBoLinearLayout.setOnClickListener(this);
        weiXinLinearLayout.setOnClickListener(this);
    }


    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_dao_hang_login://点击了导航
                finish();
                break;
            case R.id.img_see_password:
                if(passwordFlag%2==0){
                    seePassWordImagView.setBackgroundResource(R.drawable.see_password_selected);
                    passWordEdtiText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    seePassWordImagView.setBackgroundResource(R.drawable.see_password_normal);
                    passWordEdtiText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                passwordFlag++;
                break;
            case R.id.text_forget_password://忘记密码
                String userName=zhangHaoEditText.getText().toString();
                Intent forgetIntent=new Intent(this,FindPassworByPhoneActivity.class);
                forgetIntent.putExtra(USER_NAME_KEY,userName);
                startActivity(forgetIntent);
                break;
            case R.id.btn_login://登录
                loginClickChuLi();
                break;
            case R.id.text_new_user_register://用户注册
                Intent registerIntent=new Intent(this,RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.ll_qq:
                Toast.makeText(this,"点击了qq登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_wei_bo://微博
                mSsoHandler.authorize(new SinaAuthListener());
                break;
            case R.id.ll_wei_xin:
                Toast.makeText(this,"点击了微信登录",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /**
     * 登陆的点击事件处理
     */
    private void loginClickChuLi() {
        final String zhangHao=zhangHaoEditText.getText().toString();
        final String password=passWordEdtiText.getText().toString();
        //final String jiaMiPassword= MyMd5.md5encode(password);
        //MyLog.d(tag,"加密后的密码为："+jiaMiPassword);
        if(zhangHao.equals("")||zhangHao.isEmpty()){
            Toast.makeText(this, "账号为空", Toast.LENGTH_SHORT).show();
        }else if(password.equals("")||password.isEmpty()){
            Toast.makeText(this,"密码为空",Toast.LENGTH_SHORT).show();
        }else {
            StringRequest loginRequest=new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    MyLog.d(tag, "登录的数据返回:" + s);
                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        JSONObject jsonObject2=jsonObject1.getJSONObject("session");
                        String sid=jsonObject2.getString("sid");
                        String uid=jsonObject2.getString("uid");
                        if(!sid.isEmpty() && !sid.equals("")){
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            clearSharePerefrences();//清空之前保存的数据
                            writeToSharePreferences(uid, MyConstant.UID_KEY);
                            writeToSharePreferences(sid, MyConstant.SID_KEY);
                            writeToSharePreferences(zhangHao, MyConstant.USER_NAME);
                            writeToSharePreferences(password, MyConstant.PASS_WORD);
                            if(whoStartMe.equals("shouCang")){//说明是从收藏跳转过来的
                                Intent shouCangIntent=new Intent();
                                setResult(RESULT_OK,shouCangIntent);
                                finish();
                            }else if("queHuo".equals(whoStartMe)){//因为缺货从加入购物车跳转过来的
                                Intent intent=new Intent(LoginActivity.this, QueHuoDengJiActivity.class);
                                startActivity(intent);
                                finish();
                            }else if("shoppingCar".equals(whoStartMe)){//直接从购物车跳转过来的
                                Intent shoppingCarIntent=new Intent();
                                setResult(RESULT_OK,shoppingCarIntent);
                                finish();
                            }else if("guanZhu".equals(whoStartMe)){//从店铺街的关注跳转过来的
                                finish();
                            }else if("shoppingCarActivity".equals(whoStartMe)){//从购物车调过来的
                                finish();
                            }else if("personalCenter".equals(whoStartMe)){//从个人中心跳转过来的
                                finish();
                            }else if("changPassword".equals(whoStartMe)){//从个人中心的修改密码跳过来的
                                finish();
                            }else if("homeFragment".equals(whoStartMe)){//从首页碎片跳转过来的
                                Intent homeIntent=new Intent();
                                setResult(RESULT_OK,homeIntent);
                                finish();
                            }else if("productDetail".equals(whoStartMe)){//从商品详情跳转过去的
                                Intent detailIntent=new Intent();
                                setResult(RESULT_OK,detailIntent);
                                finish();
                            }
                        }else {
                            Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("name", zhangHao);
                    map.put("password", password);
                    //MyLog.d(tag,jiaMiPassword);
                    return map;
                }
            };
            requestQueue.add(loginRequest);
        }
    }


    /**
     * 清空shareprefrence
     */
    public void clearSharePerefrences(){
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, MODE_APPEND);
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
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,s);
        editor.apply();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class SinaAuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String  phoneNum =  mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                SinaAccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                Toast.makeText(LoginActivity.this,
                        "登陆成功"+phoneNum, Toast.LENGTH_SHORT).show();
                MyLog.d(tag,mAccessToken.getPhoneNum()+mAccessToken.getUid()+mAccessToken.toString());
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "登陆失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,
                    "登陆取消", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
    }
}
