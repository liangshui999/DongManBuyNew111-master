package com.example.asus_cp.dongmanbuy.activity.personal_center;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.example.asus_cp.dongmanbuy.activity.gou_wu.AwaitCommentListActivity;
import com.example.asus_cp.dongmanbuy.activity.gou_wu.DingDanListActivity;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.data_set.DataSetActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager.FundManagerActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager.HongBaoListActivity;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.adapter.LiuLanJiLuAdapter;
import com.example.asus_cp.dongmanbuy.constant.DBConstant;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyScrollView;
import com.example.asus_cp.dongmanbuy.customview.OnScrollChangeListnerMy;
import com.example.asus_cp.dongmanbuy.db.CursorHandler;
import com.example.asus_cp.dongmanbuy.db.BookDBOperateHelper;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.User;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
//import com.example.asus_cp.dongmanbuy.util.MyIMHelper;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人中心的界面
 * Created by asus-cp on 2016-06-22.
 */
public class PersonalCenterActivity extends Activity implements View.OnClickListener{

    private String tag="PersonalCenterActivity";

    private RelativeLayout titleRelativeLayout;
    private ImageView daoHangImageView;//导航
    private TextView titleTextView;
    private ImageView settingImageView;//设置

    private RelativeLayout titleRelativeLayoutInner;
    private ImageView daoHangImageViewInner;//导航
    private ImageView settingImageViewInner;

    private MyScrollView scrollView;
    private de.hdodenhof.circleimageview.CircleImageView touXiangImageView;//头像
    private TextView nameTextView;//名字
    private TextView dengJiTextView;//等级
    private LinearLayout nameLinearLayout;//名字和等级点击用
    //private ImageView xinFengImageView;//信封图标
    //private LinearLayout settingLinearLayout;//设置
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
    private String guanZhuShuUrl="http://api.zmobuy.com/JK/base/model.php";//关注数的接口

    private ImageLoadHelper helper;

    public static final int REQUEST_CODE_LOGIN_KEY=0;//跳转到登陆界面用
    public static final int REQUEST_CODE_SHOU_CANG_KEY=1;//跳转到收藏界面
    public static final int REQUEST_DATA_SET_KEY=2;//跳转到资料设置界面
    public static final int REQUESTCODE_DING_DAN_LIST_KEY=3;//跳转到订单界面
    public static final int REQUEST_GUAN_ZHU_LIST_KEY=4;//跳转到关注列表界面

    private User passUser;//传递到设置资料界面

    private BookDBOperateHelper dbHelper;

    private LiuLanJiLuAdapter liuLanJiLuAdapter;
    private List<Good> goods;

    private RequestQueue requestQueue;

    private String uid;
    private String sid;
    //private MyIMHelper myIMHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_center_activity_layout);
        setTitle(R.string.personal_center);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        requestQueue=MyApplication.getRequestQueue();
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        //myIMHelper=new MyIMHelper();
        dbHelper=new BookDBOperateHelper();
        initView();
        helper=new ImageLoadHelper();
        if(uid==null){
            Intent loginIntent=new Intent(this, LoginActivity.class);
            loginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"personalCenter");
            startActivityForResult(loginIntent, REQUEST_CODE_LOGIN_KEY);
        }else{
            getDataFromIntenetAndSetView();
            setValueToGuanZhuText();//设置关注数
        }

        scrollView.smoothScrollTo(0,0);
        //给scrollview设置滚动的监听事件
        scrollView.setOnScrollChangeListnerMy(new OnScrollChangeListnerMy() {
            @Override
            public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
                MyLog.d(tag,"y="+y);
                titleRelativeLayout.setAlpha(y/120);//因为y是整数，我这里也没有进行强制类型转换，所以y<120之前一直都是0
                titleRelativeLayoutInner.setAlpha(1-y/120);
//                if(y>120){
//                    titleRelativeLayout.setVisibility(View.VISIBLE);
//                    titleRelativeLayoutInner.setVisibility(View.GONE);
//                }else if(y==0){
//                    titleRelativeLayout.setVisibility(View.GONE);
//                    titleRelativeLayoutInner.setVisibility(View.VISIBLE);
//                }else{
//
//                }
            }
        });

    }


    /**
     * 给关注textview设置值
     */
    private void setValueToGuanZhuText() {
        StringRequest guanZhuRequest=new StringRequest(Request.Method.POST, guanZhuShuUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag, "关注接口返回的数据：" + s);
                setTextViewValue(guanZhuShuTextView,s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               /* service	collect_store
                user_id	4789*/
                Map<String,String> map=new HashMap<>();
                map.put("service","collect_store");
                map.put("user_id",uid);
                return map;
            }
        };
        requestQueue.add(guanZhuRequest);
    }


    /**
     * 联网获取数据，并给view赋值
     */
    private void getDataFromIntenetAndSetView() {
        StringRequest userInfoRequest=new StringRequest(Request.Method.POST, userInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        User user=parseJson(s);
                        passUser=user;
                        touXiangImageView.setTag(MyConstant.YU_MING + user.getPic());
                        ImageLoader imageLoader=helper.getImageLoader();
                        ImageLoader.ImageListener listener=imageLoader.getImageListener(touXiangImageView,
                                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
                        imageLoader.get(MyConstant.YU_MING + user.getPic(), listener, 200, 200);
                        MyLog.d(tag, "用户头像的数据：" + MyConstant.YU_MING + user.getPic());
                        nameTextView.setText(user.getName());
                        dengJiTextView.setText(user.getRankName());

                        setTextViewValue(shouCangShuTextView, user.getShouCangShu());
                        //setTextViewValue(guanZhuShuTextView,user.getGuanZhuShu());
                        setTextViewValue(daiFuKuanShuTextView, user.getAwaitPay());
                        setTextViewValue(daiShouHuoShuTextView,user.getAwaitShip());
                        setTextViewValue(daiPingJiaShuTextView,user.getAwaitComment());
                        setTextViewValue(yuEShuTextView,user.getMoney());
                        setTextViewValue(youHuiQuanShuTextView,user.getHongBao());
                        setTextViewValue(jiFenShuTextView,user.getJiFen());
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
     * 给view赋值的通用方法
     * @param textView 需要赋值的textview
     * @param str 准备给textview赋值的值
     */
    public void setTextViewValue(TextView textView,String str){
        if(str==null || str.equals("")){
            textView.setText("0");
        }else{
            textView.setText(str);
        }
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
            user.setDongJieJinE(jsonObject1.getString("frozen_money"));
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
    public void initView() {
        titleRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_head);
        daoHangImageView= (ImageView) findViewById(R.id.img_dao_hang_personal_center);
        titleTextView= (TextView) findViewById(R.id.text_title);
        settingImageView= (ImageView) findViewById(R.id.img_setting_personal_center);

        titleRelativeLayoutInner=(RelativeLayout) findViewById(R.id.re_layout_head_inner);
        daoHangImageViewInner=(ImageView) findViewById(R.id.img_dao_hang_personal_center_inner);
        settingImageViewInner=(ImageView) findViewById(R.id.img_setting_personal_center_inner);

        scrollView= (MyScrollView) findViewById(R.id.scroll_view_personal_center);
        touXiangImageView= (CircleImageView) findViewById(R.id.img_tou_xiang_personal_center);
        nameTextView= (TextView) findViewById(R.id.text_name_personal_center);
        dengJiTextView= (TextView) findViewById(R.id.text_deng_ji_personal_center);
        nameLinearLayout= (LinearLayout) findViewById(R.id.ll_name_personal_center);
        //xinFengImageView= (ImageView) findViewById(R.id.img_xin_feng_personal_center);
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


        //设置recyclerView

        goods= (List<Good>) dbHelper.queryGoods("0", "20", new CursorHandler() {
            private List<Good> goodsN=new ArrayList<Good>();
            @Override
            public Object handleCursor(Cursor cursor) {
                while(cursor.moveToNext()){
                    Good good=new Good();
                    good.setGoodId(cursor.getString(cursor.getColumnIndex(DBConstant.Good.GOOD_ID)));
                    good.setGoodsImg(cursor.getString(cursor.getColumnIndex(DBConstant.Good.BIG_IMAG)));
                    good.setGoodsThumb(cursor.getString(cursor.getColumnIndex(DBConstant.Good.THUM_IMAG)));
                    good.setGoodsSmallImag(cursor.getString(cursor.getColumnIndex(DBConstant.Good.SMALL_IMAG)));
                    good.setGoodName(cursor.getString(cursor.getColumnIndex(DBConstant.Good.GOOD_NAME)));
                    good.setShopPrice(cursor.getString(cursor.getColumnIndex(DBConstant.Good.SHOP_PRICE)));
                    good.setMarket_price(cursor.getString(cursor.getColumnIndex(DBConstant.Good.MARKET_PRICE)));
                    good.setSalesVolume(cursor.getString(cursor.getColumnIndex(DBConstant.Good.SALE_VOLUME)));
                    good.setGoodsNumber(cursor.getString(cursor.getColumnIndex(DBConstant.Good.GOODS_NUMBER)));
                    goodsN.add(good);
                }
                return goodsN;
            }
        });
        liuLanJiLuAdapter=new LiuLanJiLuAdapter(this,goods);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        recyclerView.setAdapter(liuLanJiLuAdapter);

       liuLanJiLuAdapter.setOnItemClickLitener(new LiuLanJiLuAdapter.OnItemClickLitener() {
           @Override
           public void onItemClick(View view, int position) {
               //Toast.makeText(PersonalCenterActivity.this, "点击的位置是" + position, Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(PersonalCenterActivity.this, ProductDetailActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               intent.putExtra(MyConstant.GOOD_KEY, goods.get(position));
               startActivity(intent);
           }
       });

        recyclerView.setFocusable(false);
        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        settingImageView.setOnClickListener(this);
        daoHangImageViewInner.setOnClickListener(this);
        settingImageViewInner.setOnClickListener(this);
        nameLinearLayout.setOnClickListener(this);
        touXiangImageView.setOnClickListener(this);
        //xinFengImageView.setOnClickListener(this);
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
            case R.id.img_dao_hang_personal_center://导航
                finish();
                break;
            case R.id.img_dao_hang_personal_center_inner://导航
                finish();
                break;
            case R.id.img_tou_xiang_personal_center://点击了头像
                toDataSetActivity();
                break;
            case R.id.ll_name_personal_center://点击了名字
                toDataSetActivity();
                break;
//            case R.id.img_xin_feng_personal_center://点击了信封
//                Toast.makeText(this,"点击了信封",Toast.LENGTH_SHORT).show();
//                break;
            case R.id.img_setting_personal_center://点击了设置
                toDataSetActivity();
                break;
            case R.id.img_setting_personal_center_inner://点击了设置
                toDataSetActivity();
                break;
            case R.id.ll_shou_cang_personal_center://点击了收藏
                Intent toShouCangIntent=new Intent(this,ShouCangListActivity.class);
                startActivityForResult(toShouCangIntent, REQUEST_CODE_SHOU_CANG_KEY);
                break;
            case R.id.ll_guan_zhu_personal_center://点击了关注
                Intent toGuanZhuIntent=new Intent(this,GuanZhuListActivity.class);
                startActivityForResult(toGuanZhuIntent, REQUEST_GUAN_ZHU_LIST_KEY);
                break;
            case R.id.re_layout_my_order_personal_center://点击了我的订单
                toDingDanListAcitivy(MyConstant.ALL_DING_DAN);
                break;
            case R.id.ll_dai_fu_kuan_personal_center://点击了待付款
                toDingDanListAcitivy(MyConstant.DAI_FU_KUAN_DING_DAN);
                break;
            case R.id.ll_dai_shou_huo_personal_center://点击了待收货
                toDingDanListAcitivy(MyConstant.DAI_SHOU_HUO_DING_DAN);
                break;
            case R.id.ll_dai_ping_jia_personal_center://点击了待评价
                //toDingDanListAcitivy(MyConstant.DAI_PING_JIA_DING_DAN);
                Intent toAwaitCommentIntent=new Intent(this, AwaitCommentListActivity.class);
                startActivity(toAwaitCommentIntent);
                break;
            case R.id.re_layout_my_wallet_personal_center://点击了我的钱包
                toFundManagerAcitvity();
                break;
            case R.id.ll_yu_e_personal_center://点击了余额
                toFundManagerAcitvity();
                break;
            case R.id.ll_you_hui_quan_personal_center://点击了优惠券
                Intent toHongBaoListIntent=new Intent(this, HongBaoListActivity.class);
                startActivity(toHongBaoListIntent);
                break;
            case R.id.ll_ji_fen_personal_center://点击了积分
                //Toast.makeText(this,"点击了积分",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_ke_fu_personal_center://点击了客服
                //Toast.makeText(this,"点击了客服",Toast.LENGTH_SHORT).show();
                //myIMHelper.openKeFuLiaoTianAndSendMessage("");
                break;
            case R.id.ll_qing_kong_personal_center://点击了清空
                dbHelper.deleteAll();
                List<Good> goodss=(List<Good>) dbHelper.queryGoods("0", "20", new CursorHandler() {
                    private List<Good> goodsN=new ArrayList<Good>();
                    @Override
                    public Object handleCursor(Cursor cursor) {
                        while(cursor.moveToNext()){
                            Good good=new Good();
                            good.setGoodId(cursor.getString(cursor.getColumnIndex(DBConstant.Good.GOOD_ID)));
                            good.setGoodsImg(cursor.getString(cursor.getColumnIndex(DBConstant.Good.BIG_IMAG)));
                            good.setGoodsThumb(cursor.getString(cursor.getColumnIndex(DBConstant.Good.THUM_IMAG)));
                            good.setGoodsSmallImag(cursor.getString(cursor.getColumnIndex(DBConstant.Good.SMALL_IMAG)));
                            good.setGoodName(cursor.getString(cursor.getColumnIndex(DBConstant.Good.GOOD_NAME)));
                            good.setShopPrice(cursor.getString(cursor.getColumnIndex(DBConstant.Good.SHOP_PRICE)));
                            good.setMarket_price(cursor.getString(cursor.getColumnIndex(DBConstant.Good.MARKET_PRICE)));
                            good.setSalesVolume(cursor.getString(cursor.getColumnIndex(DBConstant.Good.SALE_VOLUME)));
                            good.setGoodsNumber(cursor.getString(cursor.getColumnIndex(DBConstant.Good.GOODS_NUMBER)));
                            goodsN.add(good);
                        }
                        return goodsN;
                    }
                });
                liuLanJiLuAdapter=new LiuLanJiLuAdapter(this,goodss);
                recyclerView.setAdapter(liuLanJiLuAdapter);
                break;
        }
    }


    /**
     * 跳转到资金管理的界面
     */
    private void toFundManagerAcitvity() {
        Intent toFundManagerIntent=new Intent(this,FundManagerActivity.class);
        toFundManagerIntent.putExtra(MyConstant.USER_KEY,passUser);
        startActivity(toFundManagerIntent);
    }


    /**
     * 跳转到订单列表界面
     * @param str 传给订单列表界面的标记，到底是从哪一个跳转过去的
     */
    private void toDingDanListAcitivy(String str) {
        Intent allDingDanIntent=new Intent(this, DingDanListActivity.class);
        allDingDanIntent.putExtra(MyConstant.TO_DING_DAN_LIST_KEY,str);
        startActivityForResult(allDingDanIntent, REQUESTCODE_DING_DAN_LIST_KEY);
    }


    /**
     * 跳转到资料设置的界面
     */
    private void toDataSetActivity() {
        Intent intent=new Intent(this,DataSetActivity.class);
        intent.putExtra(MyConstant.USER_KEY,passUser);
        intent.putExtra(MyConstant.DATA_SET_ACTIVITY_LABLE_FLAG_KEY,"fromPersonalCenter");
        startActivityForResult(intent, REQUEST_DATA_SET_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_LOGIN_KEY://从登陆界面返回的
                getDataFromIntenetAndSetView();
                setValueToGuanZhuText();
                break;
            case REQUEST_CODE_SHOU_CANG_KEY://从收藏界面返回的
                getDataFromIntenetAndSetView();
                break;
            case REQUEST_DATA_SET_KEY://从资料设置返回
                getDataFromIntenetAndSetView();
                break;
            case REQUESTCODE_DING_DAN_LIST_KEY://从订单列表返回的
                getDataFromIntenetAndSetView();
                break;
            case REQUEST_GUAN_ZHU_LIST_KEY://从关注列表返回的
                getDataFromIntenetAndSetView();
                setValueToGuanZhuText();
                break;
        }
    }
}
