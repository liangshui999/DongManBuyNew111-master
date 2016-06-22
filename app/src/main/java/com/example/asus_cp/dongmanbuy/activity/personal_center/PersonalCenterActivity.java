package com.example.asus_cp.dongmanbuy.activity.personal_center;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.User;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心的界面
 * Created by asus-cp on 2016-06-22.
 */
public class PersonalCenterActivity extends Activity implements View.OnClickListener{

    private String tag="PersonalCenterActivity";

    private ImageView touXiangImageView;//头像
    private TextView nameTextView;//名字
    private TextView dengJiTextView;//等级
    private LinearLayout nameLinearLayout;//名字和等级点击用
    private ImageView xinFengImageView;//信封图标
    private LinearLayout settingLinearLayout;//设置
    private LinearLayout shouCangLinearLayout;//收藏
    private LinearLayout guanZhuLinearLayout;//关注
    private TextView shouCangShuTextView;//收藏的数目
    private TextView guanZhuShuTextView;//关注的数目
    private RelativeLayout myOrderRelaytiveLayout;//我的订单
    private LinearLayout daiFuKuanLinearLayout;//待付款
    private LinearLayout daiShouHuoLinearLayout;//待收货
    private LinearLayout daiPingJiaLinearLayout;//待评价
    private TextView daiFuKuanShuTextView;//待付款数
    private TextView daiShouHuoShuTextView;//待收货数
    private TextView daiPingJiaShuTextView;//待评价数
    private RelativeLayout myWalletRelaytiveLayout;//我的钱包
    private LinearLayout yuELinearLayout;//余额
    private LinearLayout youHuiQuanLinearLayout;//优惠券
    private LinearLayout jiFenLinearLayout;//积分
    private TextView yuEShuTextView;//余额数
    private TextView youHuiQuanShuTextView;//优惠券数
    private TextView jiFenShuTextView;//积分数
    private RelativeLayout keFuRelativeLayout;//客服
    private LinearLayout qingKongLinearLayout;//清空
    private RecyclerView recyclerView;

    private String userInfoUrl="http://www.zmobuy.com/PHP/?url=/user/info";//用户信息的接口
    private RequestQueue requestQueue;

    private ImageLoadHelper helper;

    private SharedPreferences sharedPreferences;

    private String uid;

    private String sid;

    public static final int REQUEST_CODE_LOGIN_KEY=0;//跳转到登陆界面用

    private User passUser;//传递到设置资料界面


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.personal_center_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        initView();
        requestQueue= MyApplication.getRequestQueue();
        helper=new ImageLoadHelper();
        sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        if(uid==null){
            Intent loginIntent=new Intent(this, LoginActivity.class);
            loginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"personalCenter");
            startActivityForResult(loginIntent, REQUEST_CODE_LOGIN_KEY);
        }else{
            getDataFromIntenetAndSetView();
        }


    }


    /**
     * 联网获取数据，并给view赋值
     */
    private void getDataFromIntenetAndSetView() {
        uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        StringRequest userInfoRequest=new StringRequest(Request.Method.POST, userInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        User user=parseJson(s);
                        passUser=user;
                        ImageLoader imageLoader=helper.getImageLoader();
                        ImageLoader.ImageListener listener=imageLoader.getImageListener(touXiangImageView,
                                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
                        //imageLoader.get(user.getPic(),listener,200,200);
                        nameTextView.setText(user.getName());
                        dengJiTextView.setText(user.getRankName());
                        shouCangShuTextView.setText(user.getShouCangShu());
                        guanZhuShuTextView.setText(user.getGuanZhuShu());
                        daiFuKuanShuTextView.setText(user.getAwaitPay());
                        daiShouHuoShuTextView.setText(user.getAwaitShip());
                        daiPingJiaShuTextView.setText(user.getAwaitComment());
                        yuEShuTextView.setText(user.getMoney());
                        youHuiQuanShuTextView.setText(user.getYouHuiQuanShu());
                        jiFenShuTextView.setText(user.getJiFen());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"}}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(userInfoRequest);
    }


    /**
     * 解析json数据
     * @param s
     */
    private User parseJson(String s) {
        MyLog.d(tag, "返回的数据是：" + s);
        User user=new User();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            user.setId(jsonObject1.getString("user_id"));
            user.setEmail(jsonObject1.getString("email"));
            user.setName(jsonObject1.getString("user_name"));
            user.setMoney(jsonObject1.getString("user_money"));
            user.setJiFen(jsonObject1.getString("pay_points"));
            user.setPhone(jsonObject1.getString("mobile_phone"));
            user.setPic(jsonObject1.getString("user_picture"));
            user.setSex(JsonHelper.decodeUnicode(jsonObject1.getString("sexcn")));
            user.setBankCards(jsonObject1.getString("bank_cards"));
            user.setHongBao(jsonObject1.getString("bonus"));
            user.setRankName(JsonHelper.decodeUnicode(jsonObject1.getString("rank_name")));
            user.setShouCangShu(jsonObject1.getString("collection_num"));
            JSONObject jsonObject2=jsonObject1.getJSONObject("order_num");
            user.setAwaitPay(jsonObject2.getString("await_pay"));
            user.setAwaitShip(jsonObject2.getString("await_ship"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 初始化view
     */
    private void initView() {
        touXiangImageView= (ImageView) findViewById(R.id.img_tou_xiang_personal_center);
        nameTextView= (TextView) findViewById(R.id.text_name_personal_center);
        dengJiTextView= (TextView) findViewById(R.id.text_deng_ji_personal_center);
        nameLinearLayout= (LinearLayout) findViewById(R.id.ll_name_personal_center);
        xinFengImageView= (ImageView) findViewById(R.id.img_xin_feng_personal_center);
        settingLinearLayout= (LinearLayout) findViewById(R.id.ll_setting_personal_center);
        shouCangLinearLayout= (LinearLayout) findViewById(R.id.ll_shou_cang_personal_center);
        guanZhuLinearLayout= (LinearLayout) findViewById(R.id.ll_guan_zhu_personal_center);
        shouCangShuTextView= (TextView) findViewById(R.id.text_shou_cang_shu_personal_center);
        guanZhuShuTextView= (TextView) findViewById(R.id.text_guan_zhu_shu_personal_center);
        myOrderRelaytiveLayout= (RelativeLayout) findViewById(R.id.re_layout_my_order_personal_center);
        daiFuKuanLinearLayout= (LinearLayout) findViewById(R.id.ll_dai_fu_kuan_personal_center);
        daiShouHuoLinearLayout= (LinearLayout) findViewById(R.id.ll_dai_shou_huo_personal_center);
        daiPingJiaLinearLayout= (LinearLayout) findViewById(R.id.ll_dai_ping_jia_personal_center);
        daiFuKuanShuTextView= (TextView) findViewById(R.id.text_dai_fu_kuan_count_personal_center);
        daiShouHuoShuTextView= (TextView) findViewById(R.id.text_dai_shou_huo_count_personal_center);
        daiPingJiaShuTextView= (TextView) findViewById(R.id.text_dai_ping_jia_count_personal_center);
        myWalletRelaytiveLayout= (RelativeLayout) findViewById(R.id.re_layout_my_wallet_personal_center);
        yuELinearLayout= (LinearLayout) findViewById(R.id.ll_yu_e_personal_center);
        youHuiQuanLinearLayout= (LinearLayout) findViewById(R.id.ll_you_hui_quan_personal_center);
        jiFenLinearLayout= (LinearLayout) findViewById(R.id.ll_ji_fen_personal_center);
        yuEShuTextView= (TextView) findViewById(R.id.text_yu_e_shu_personal_center);
        youHuiQuanShuTextView= (TextView) findViewById(R.id.text_you_hui_quan_shu_personal_center);
        jiFenShuTextView= (TextView) findViewById(R.id.text_ji_fen_shu_personal_center);
        keFuRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_ke_fu_personal_center);
        qingKongLinearLayout= (LinearLayout) findViewById(R.id.ll_qing_kong_personal_center);
        recyclerView= (RecyclerView) findViewById(R.id.id_recyle_view_personal_center);


        //设置点击事件
        nameLinearLayout.setOnClickListener(this);
        touXiangImageView.setOnClickListener(this);
        xinFengImageView.setOnClickListener(this);
        settingLinearLayout.setOnClickListener(this);
        shouCangLinearLayout.setOnClickListener(this);
        guanZhuLinearLayout.setOnClickListener(this);
        myOrderRelaytiveLayout.setOnClickListener(this);
        daiFuKuanLinearLayout.setOnClickListener(this);
        daiShouHuoLinearLayout.setOnClickListener(this);
        daiPingJiaLinearLayout.setOnClickListener(this);
        myWalletRelaytiveLayout.setOnClickListener(this);
        yuELinearLayout.setOnClickListener(this);
        youHuiQuanLinearLayout.setOnClickListener(this);
        jiFenLinearLayout.setOnClickListener(this);
        keFuRelativeLayout.setOnClickListener(this);
        qingKongLinearLayout.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_tou_xiang_personal_center://点击了头像
                toDataSetActivity();
                break;
            case R.id.ll_name_personal_center://点击了名字
                toDataSetActivity();
                break;
            case R.id.img_xin_feng_personal_center://点击了信封
                Toast.makeText(this,"点击了信封",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_setting_personal_center://点击了设置
                toDataSetActivity();
                break;
            case R.id.ll_shou_cang_personal_center://点击了收藏
                Toast.makeText(this,"点击了收藏",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_guan_zhu_personal_center://点击了关注
                Toast.makeText(this,"点击了关注",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_my_order_personal_center://点击了我的订单
                Toast.makeText(this,"点击了我的订单",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_dai_fu_kuan_personal_center://点击了待付款
                Toast.makeText(this,"点击了待付款",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_dai_shou_huo_personal_center://点击了待收货
                Toast.makeText(this,"点击了待收货",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_dai_ping_jia_personal_center://点击了待评价
                Toast.makeText(this,"点击了待评价",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_my_wallet_personal_center://点击了我的钱包
                Toast.makeText(this,"点击了我的钱包",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_yu_e_personal_center://点击了余额
                Toast.makeText(this,"点击了余额",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_you_hui_quan_personal_center://点击了优惠券
                Toast.makeText(this,"点击了优惠券",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_ji_fen_personal_center://点击了积分
                Toast.makeText(this,"点击了积分",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_ke_fu_personal_center://点击了客服
                Toast.makeText(this,"点击了客服",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_qing_kong_personal_center://点击了清空
                Toast.makeText(this,"点击了清空",Toast.LENGTH_SHORT).show();
                break;
        }
    }



    /**
     * 跳转到资料设置的界面
     */
    private void toDataSetActivity() {
        Intent intent=new Intent(this,DataSetActivity.class);
        intent.putExtra(MyConstant.USER_KEY,passUser);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_LOGIN_KEY://从登陆界面返回的
                getDataFromIntenetAndSetView();
                break;
        }
    }
}
