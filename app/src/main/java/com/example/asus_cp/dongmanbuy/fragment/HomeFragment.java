package com.example.asus_cp.dongmanbuy.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.asus_cp.dongmanbuy.activity.MainActivity;
import com.example.asus_cp.dongmanbuy.activity.dian_pu_jie.ShopHomeActivity;
import com.example.asus_cp.dongmanbuy.activity.gou_wu.DingDanListActivity;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.activity.main_activity_xiang_guan.LiuLanJiLuListActivity;
import com.example.asus_cp.dongmanbuy.activity.more.JingPinTuiJianMoreActivity;
import com.example.asus_cp.dongmanbuy.activity.more.XianShiMiaoShaMoreActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.GuanZhuListActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.data_set.EditShipAddressActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager.FundManagerActivity;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.adapter.CaiNiXiHuanAdapter;
import com.example.asus_cp.dongmanbuy.adapter.HomeShopStreetAdapter;
import com.example.asus_cp.dongmanbuy.adapter.JingPinAdapter;
import com.example.asus_cp.dongmanbuy.adapter.XianShiAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridView;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.model.Binner;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.model.User;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.MyNetHelper;
import com.example.asus_cp.dongmanbuy.util.MyScreenInfoHelper;
import com.example.asus_cp.dongmanbuy.util.MyTimeHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页的展示界面
 * Created by asus-cp on 2016-05-19.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private String tag="HomeFragment";
    //第一个广告条
    private ViewPager firstViewPager;
    private LinearLayout firstPointGroup;
    public static final int SCROLL__FIRST_BINNER =1;//自动滚动消息的what
    public static final int REFRESH_FIRST_BINNER = 2;//更新第一个位置的广告的
    private static final int FIRST_BINNER_FLAG =3 ;//第一幅广告的标记
    private List<View> firstImageViews;//装载imageview的集合

    //第二个广告条
    private ViewPager secondViewPager;
    private LinearLayout secondPointGroup;
    public static final int SCROLL__SECOND_BINNER =4;//自动滚动消息的what
    public static final int REFRESH_SECOND_BINNER = 5;//更新第一个位置的广告的
    private static final int SECOND_BINNER_FLAG =6 ;//第二幅广告的标记
    private List<View> secondImageViews;//装载imageview的集合

    //第三个广告条
    private ViewPager threeViewPager;
    private LinearLayout threePointGroup;
    public static final int SCROLL__THREE_BINNER =7;//自动滚动消息的what
    public static final int REFRESH_THREE_BINNER = 8;//更新第一个位置的广告的
    private static final int THREE_BINNER_FLAG =9 ;//第三幅广告的标记
    private List<View> threeImageViews;//装载imageview的集合

    //限时秒杀
    public static final int XIAN_SHI_TIME=11;//限时秒杀的消息标记
    private String promoteEndTime;//促销结束时间


    private int[] imageIds={R.drawable.guanggao1,R.drawable.guanggao2,R.drawable.guanggao3};//装imagview图片id的数组
    private Context context;
    private View v;//fragment的布局
    protected int lastPosition;//上一个页面的位置
    private boolean isRunning = true;//判断是否自动滚动


    //我的钱包，我的订单等8个按钮
    private LinearLayout allDongManll;
    private LinearLayout tuiJianZhuTill;
    private LinearLayout youHuiHuoDongll;
    private LinearLayout tuanGoull;
    private LinearLayout myGuanZhull;
    private LinearLayout myOrderll;
    private LinearLayout jiFenDuiHuanll;
    private LinearLayout helpll;

    //限时秒杀的gridview
    private TextView hourTextView;
    private TextView minuteTextView;
    private TextView secondTextView;
    private MyGridView xianShiMiaoShaGridView;
    private ImageView xianShiMiaoShaImagView;
    public static final int REFRESH_XIAN_SHI_MIAO_SHA=10;

    //精品推荐的gridview
    private MyGridView jingPinTuiJianGridview;

    //猜你喜欢的gridview
    private MyGridViewA caiNiXiHuanGridView;

    //精品推荐的viewpager
    private ViewPager jingPinViewPager;

    //店铺街的viewpager
    private ViewPager shopStreetViewPager;

    //更多按钮
    private TextView xianShiMoreTextView;
    private TextView jingPinMoreTextView;
    private TextView dianPuJieMoreTextView;

    private JsonHelper jsonHelper;//json解析的帮助类

    private RequestQueue requestQueue;//请求队列

    public static String GOOD_KEY="good_key";//传递good时的键

    private SharedPreferences sharedPreferences;

    public static final int REQUEST_CODE_LOGIN_WALLET=13;//从钱包跳转到登陆界面的请求码

    public static final int REQUEST_CODE_LOGIN_ORDER=14;//从订单跳转到登陆界面的请求码

    public static final int REQUEST_CODE_LOGIN_LIU_LAN_JI_LU=15;//从浏览记录跳转到登陆界面的请求码

    public static final int REQUEST_CODE_LOGIN_SHIP_ADDRESS=16;//从收货地址跳转到登陆界面的请求码

    public static final int REQUEST_CODE_LOGIN_GUAN_ZHU=17;//从关注跳转到登陆界面的请求码

    private String userInfoUrl="http://www.zmobuy.com/PHP/?url=/user/info";//用户信息的接口

    private MainActivity mainActivity;

    private int screenWidth;

    private SharedPreferences cachSharePrefrences;//保存缓存数据的shareprefrences,主要缓存json数据，不是缓存图片


    private Handler handler = new MyHandler();

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SCROLL__FIRST_BINNER:
                    //让viewPager 滑动到下一页
                    if(firstImageViews.size()>1){
                        firstViewPager.setCurrentItem(firstViewPager.getCurrentItem() + 1);
                        handler.sendEmptyMessageDelayed(SCROLL__FIRST_BINNER, 2000);
                    }
                    //Log.d(tag, "接收定时消息");
                    break;
                case REFRESH_FIRST_BINNER://更新首页第一个广告位置的广告
                    //firstPointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_first);
                    //指示点的初始化
                    initPoint(firstPointGroup,firstImageViews.size());
                    firstViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - 1);
                    firstViewPager.setAdapter(new MyPagerAdapter(firstImageViews));
                    break;
                case SCROLL__SECOND_BINNER:
                    //让viewPager 滑动到下一页
                    if(secondImageViews.size()>1){
                        secondViewPager.setCurrentItem(secondViewPager.getCurrentItem()+1);
                        handler.sendEmptyMessageDelayed(SCROLL__SECOND_BINNER, 2000);
                    }

                    //Log.d(tag, "接收定时消息");
                    break;
                case REFRESH_SECOND_BINNER://更新首页第二个广告位置的广告
                    //secondPointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_second);
                    //指示点的初始化
                    initPoint(secondPointGroup,secondImageViews.size());
                    secondViewPager.setCurrentItem(0);
                    secondViewPager.setAdapter(new MyPagerAdapter(secondImageViews));
                    break;
                case SCROLL__THREE_BINNER:
                    //让viewPager 滑动到下一页
                    if(threeImageViews.size()>1){
                        threeViewPager.setCurrentItem(threeViewPager.getCurrentItem()+1);
                        handler.sendEmptyMessageDelayed(SCROLL__THREE_BINNER, 2000);
                    }
                    //Log.d(tag, "接收定时消息");
                    break;
                case REFRESH_THREE_BINNER://更新首页第三个广告位置的广告
                    //threePointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_three);
                    //指示点的初始化
                    initPoint(threePointGroup,threeImageViews.size());
                    threeViewPager.setCurrentItem(0);
                    threeViewPager.setAdapter(new MyPagerAdapter(threeImageViews));
                    break;
                case REFRESH_XIAN_SHI_MIAO_SHA://更新限时秒杀
                    Bitmap bitmap= (Bitmap) msg.obj;
                    xianShiMiaoShaImagView.setImageBitmap(bitmap);
                    break;
                case XIAN_SHI_TIME://限时秒杀
                    setXianShiTime();
                    handler.sendEmptyMessageDelayed(XIAN_SHI_TIME,1000);
                    break;
            }
        }
    }

    /**
     * 初始化指示点
     * @param pointGroup 装指示点的layout
     * @param size 指示点的数量
     */
    private void initPoint(LinearLayout pointGroup,int size) {
        for(int i=0;i< size;i++){
            //添加指示点
            ImageView point =new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 30;
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.point);
            if(i==0){
                point.setEnabled(true);
            }else{
                point.setEnabled(false);
            }
            pointGroup.addView(point);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        mainActivity= (MainActivity) getActivity();
        sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,Context.MODE_APPEND);
        v=inflater.inflate(R.layout.home_fragment_layout, null);
        initView();
        return v;
    }

    /**
     * 初始化view
     */
    private void initView() {
        requestQueue= MyApplication.getRequestQueue();
        firstPointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_first);
        secondPointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_second);
        threePointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_three);

        screenWidth= MyScreenInfoHelper.getScreenWidth();//获取屏幕的宽度

        //-----------------第一个广告的初始化动作-----------------------------------
        if(!MyNetHelper.isNetworkAvailable()){
            Toast.makeText(context,"网络连接不可用",Toast.LENGTH_SHORT).show();
        }
        firstViewPager = (ViewPager) v.findViewById(R.id.viewpager_binner_first);
        setHeightToViewPager(firstViewPager,414,216);//设置firstviewpager的高度

        firstImageViews =new ArrayList<View>();
        cachSharePrefrences=context.getSharedPreferences(MyConstant.CACH_SHAREPREFERENCE_NAME,Context.MODE_APPEND);
        String firstGuangGaoCach=cachSharePrefrences.getString(MyConstant.GUANG_GAO_ONE_CACH_KEY,null);
        if(firstGuangGaoCach!=null){
            getBinnerImageFromIntenet(firstGuangGaoCach,FIRST_BINNER_FLAG,firstImageViews,REFRESH_FIRST_BINNER,SCROLL__FIRST_BINNER);
        }else{
            DialogHelper.showDialog(context);
            String binnerUrl="http://api.zmobuy.com/JK/base/model.php";
            StringRequest binnerOneRequest=new StringRequest(Request.Method.POST, binnerUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    DialogHelper.dissmisDialog();
                    getBinnerImageFromIntenet(s, FIRST_BINNER_FLAG, firstImageViews, REFRESH_FIRST_BINNER, SCROLL__FIRST_BINNER);
                    writeDataToCach(MyConstant.GUANG_GAO_ONE_CACH_KEY,s);//将json数据写入缓存
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                /*service	ads
                position_id	256*/
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("service","ads");
                    map.put("position_id","256");
                    return map;
                }
            };
            requestQueue.add(binnerOneRequest);//加入到队列
        }

        firstViewPager.addOnPageChangeListener(new MyPageChangeListener(firstImageViews, firstPointGroup));
        firstViewPager.setOnTouchListener(new MyPageTouchListener(SCROLL__FIRST_BINNER, firstViewPager));

        //mainActivity.menu.addIgnoredView(firstViewPager);


        //--------------第二个广告的初始化----------------------------------------------

        secondViewPager = (ViewPager) v.findViewById(R.id.viewpager_binner_second);

        //动态设置viewpager的高度
        setHeightToViewPager(secondViewPager, 414, 129);
        secondImageViews =new ArrayList<View>();
        String guangGaoTwoCach=cachSharePrefrences.getString(MyConstant.GUANG_GAO_TWO_CACH_KEY,null);
        if(guangGaoTwoCach!=null){
            getBinnerImageFromIntenet(guangGaoTwoCach,SECOND_BINNER_FLAG,secondImageViews,REFRESH_SECOND_BINNER,SCROLL__SECOND_BINNER);
        }else{
            String secondBinnerUrl="http://api.zmobuy.com/JK/base/model.php";
            StringRequest binnerSecondRequest=new StringRequest(Request.Method.POST, secondBinnerUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    getBinnerImageFromIntenet(s,SECOND_BINNER_FLAG,secondImageViews,REFRESH_SECOND_BINNER,SCROLL__SECOND_BINNER);
                    writeDataToCach(MyConstant.GUANG_GAO_TWO_CACH_KEY,s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("service","ads");
                    map.put("position_id","257");
                    return map;
                }
            };
            requestQueue.add(binnerSecondRequest);//加入到队列
        }

        secondViewPager.addOnPageChangeListener(new MyPageChangeListener(secondImageViews, secondPointGroup));
        secondViewPager.setOnTouchListener(new MyPageTouchListener(SCROLL__SECOND_BINNER, secondViewPager));



        //---------------------------------第三个广告的初始化-------------------------------------
        threeViewPager = (ViewPager) v.findViewById(R.id.viewpager_binner_three);
        //动态设置viewpager的高度
        setHeightToViewPager(threeViewPager, 414, 129);

        threeImageViews =new ArrayList<View>();
        String guangGaoThreeCach=cachSharePrefrences.getString(MyConstant.GUANG_GAO_THREE_CACH_KEY,null);
        if(guangGaoThreeCach!=null){
            getBinnerImageFromIntenet(guangGaoThreeCach,THREE_BINNER_FLAG,threeImageViews,REFRESH_THREE_BINNER,SCROLL__THREE_BINNER);
        }else{
            String threebinnerUrl="http://api.zmobuy.com/JK/base/model.php";
            StringRequest binnerThreeRequest=new StringRequest(Request.Method.POST, threebinnerUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    getBinnerImageFromIntenet(s,THREE_BINNER_FLAG,threeImageViews,REFRESH_THREE_BINNER,SCROLL__THREE_BINNER);
                    writeDataToCach(MyConstant.GUANG_GAO_THREE_CACH_KEY,s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("service","ads");
                    map.put("position_id","258");
                    return map;
                }
            };
            requestQueue.add(binnerThreeRequest);//加入到队列
        }

        threeViewPager.addOnPageChangeListener(new MyPageChangeListener(threeImageViews, threePointGroup));
        threeViewPager.setOnTouchListener(new MyPageTouchListener(SCROLL__THREE_BINNER,threeViewPager));



        //-----------初始化我的钱包，我的按钮等8个组件-------------------
        allDongManll = (LinearLayout) v.findViewById(R.id.ll_all_dong_man);
        tuiJianZhuTill = (LinearLayout) v.findViewById(R.id.ll_tui_jian_zhu_ti);
        youHuiHuoDongll = (LinearLayout) v.findViewById(R.id.ll_you_hui_huo_dong);
        tuanGoull = (LinearLayout) v.findViewById(R.id.ll_ship_tuan_gou);
        myGuanZhull = (LinearLayout) v.findViewById(R.id.ll_my_guan_zhu);
        myOrderll = (LinearLayout) v.findViewById(R.id.ll_my_order);
        jiFenDuiHuanll = (LinearLayout) v.findViewById(R.id.ll_ji_fen_dui_huan);
        helpll = (LinearLayout) v.findViewById(R.id.ll_help_center);

        allDongManll.setOnClickListener(this);
        tuiJianZhuTill.setOnClickListener(this);
        youHuiHuoDongll.setOnClickListener(this);
        tuanGoull.setOnClickListener(this);
        myGuanZhull.setOnClickListener(this);
        myOrderll.setOnClickListener(this);
        jiFenDuiHuanll.setOnClickListener(this);
        helpll.setOnClickListener(this);

        //接收json数据
        jsonHelper=new JsonHelper();


        //-----------------------限时秒杀部分---------------------------------
        hourTextView= (TextView) v.findViewById(R.id.text_hour_xian_shi_miao_sha);
        minuteTextView= (TextView) v.findViewById(R.id.text_minute_xian_shi_miao_sha);
        secondTextView= (TextView) v.findViewById(R.id.text_second_xian_shi_miao_sha);

        String xianShiMiaoShaCach=cachSharePrefrences.getString(MyConstant.XIAN_SHI_MIAO_SHA_CACH_KEY,null);
        if(xianShiMiaoShaCach!=null){
            setValueToXianShiMiaoSha(xianShiMiaoShaCach);
        }else{
            String shanShiUrl="http://www.zmobuy.com/PHP/index.php?url=/home/grab";
            StringRequest xianShiStringRequest=new StringRequest(Request.Method.GET, shanShiUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    MyLog.d(tag,"显示秒杀返回的数据是："+s);
                    setValueToXianShiMiaoSha(s);//给限时秒杀的view设置值
                    writeDataToCach(MyConstant.XIAN_SHI_MIAO_SHA_CACH_KEY,s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            requestQueue.add(xianShiStringRequest);
        }




        //----------------------用viewpager做的精品推荐部分-----------------------
        jingPinViewPager= (ViewPager) v.findViewById(R.id.viewpager_jing_pin);
        final List<View> gridViews=new ArrayList<View>();
        String jingPinTuiJianCach=cachSharePrefrences.getString(MyConstant.JING_PING_TUI_JIAN_CACH_KEY,null);
        if(jingPinTuiJianCach!=null){
            setValueToJingPinTuiJian(jingPinTuiJianCach, gridViews);
        }else{
            String jingPinRequestUrl="http://www.zmobuy.com/PHP/index.php?url=/home/bestgoods";
            StringRequest jingPinStringRequest=new StringRequest(Request.Method.GET, jingPinRequestUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    setValueToJingPinTuiJian(s, gridViews);
                    writeDataToCach(MyConstant.JING_PING_TUI_JIAN_CACH_KEY,s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            requestQueue.add(jingPinStringRequest);
        }




        //-----------------------精品推荐部分---------------------------------
        /*String jingPinRequestUrl="http://www.zmobuy.com/PHP/index.php?url=/home/bestgoods";
        StringRequest jingPinStringRequest=new StringRequest(Request.Method.GET, jingPinRequestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                List<Good> goods = parseCaiNiLikeAndJingPin(s);
                    //精品推荐的gridview
                jingPinTuiJianGridview = (MyGridView) v.findViewById(R.id.grid_view_jing_pin_tui_jian);
                if (goods.size() > 0) {
                    JingPinAdapter jingPinAdapter = new JingPinAdapter(context, getElementsFromList(goods, 3));
                    jingPinTuiJianGridview.setAdapter(jingPinAdapter);
                    jingPinTuiJianGridview.setOnItemClickListener(new JingPinOnItemClickListener());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(jingPinStringRequest);*/


        //----------------------店铺街部分-----------------------------------
        String indexUrl="http://www.zmobuy.com/PHP/?url=/store/index";//店铺分类的url
        shopStreetViewPager= (ViewPager) v.findViewById(R.id.viewpager_shop_street);
        final List<View> shopStreetGridViews=new ArrayList<View>();
        String shopStreetCach=cachSharePrefrences.getString(MyConstant.HOME_DIAN_PU_JIE_CACH_KEY,null);
        if(shopStreetCach!=null){
            setValueToShopStrret(shopStreetCach, shopStreetGridViews);
        }else{
            StringRequest shopStreetStringRequest=new StringRequest(Request.Method.POST, indexUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    setValueToShopStrret(s, shopStreetGridViews);
                    writeDataToCach(MyConstant.HOME_DIAN_PU_JIE_CACH_KEY,s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>map=new HashMap<String,String>();
                    String json="{\"page\":\"1\",\"where\":\"\""+""+"\"\",\"type\":\"1\"}";
                    map.put("json",json);
                    return map;
                }
            };
            requestQueue.add(shopStreetStringRequest);
        }




        //-----------------------猜你喜欢部分---------------------------------
        caiNiXiHuanGridView= (MyGridViewA) v.findViewById(R.id.grid_view_cai_ni_xi_huan);
        String caiNiXiHuanCach=cachSharePrefrences.getString(MyConstant.CAI_NI_XI_HUAN_CACH_KEY,null);
        if(caiNiXiHuanCach!=null){
            setValueToCaiNiXiHuan(caiNiXiHuanCach);
        }else{
            String caiNiUrl="http://www.zmobuy.com/PHP/index.php?url=/home/hotgoods";
            StringRequest caiNiXiHuanRequest=new StringRequest(Request.Method.GET, caiNiUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    MyLog.d(tag,"猜你喜欢部分"+s);
                    setValueToCaiNiXiHuan(s);
                    writeDataToCach(MyConstant.CAI_NI_XI_HUAN_CACH_KEY,s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            requestQueue.add(caiNiXiHuanRequest);
        }



        //将各个viewpager添加到忽视的区域
        mainActivity.menu.addIgnoredView(firstViewPager);
        mainActivity.menu.addIgnoredView(secondViewPager);
        mainActivity.menu.addIgnoredView(threeViewPager);
        mainActivity.menu.addIgnoredView(jingPinViewPager);
        mainActivity.menu.addIgnoredView(shopStreetViewPager);

        //3个更多按钮的初始化和点击事件
        xianShiMoreTextView= (TextView) v.findViewById(R.id.text_xian_shi_more);
        jingPinMoreTextView= (TextView) v.findViewById(R.id.text_jing_pin_more);
        dianPuJieMoreTextView= (TextView) v.findViewById(R.id.text_dian_pu_jie_more);
        xianShiMoreTextView.setOnClickListener(this);
        jingPinMoreTextView.setOnClickListener(this);
        dianPuJieMoreTextView.setOnClickListener(this);
    }


    /**
     * 给猜你喜欢设置值
     * @param s
     */
    private void setValueToCaiNiXiHuan(String s) {
        final List<Good> goods=parseCaiNiLikeAndJingPin(s);
        CaiNiXiHuanAdapter caiNiXiHuanAdapter=new CaiNiXiHuanAdapter(context,goods);
        caiNiXiHuanGridView.setAdapter(caiNiXiHuanAdapter);
        //CategoryImageLoadHelper.setGridViewViewHeightBasedOnChildren(caiNiXiHuanGridView);
        caiNiXiHuanGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(GOOD_KEY, goods.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    /**
     * 给店铺街的view设置值
     * @param s
     * @param shopStreetGridViews
     */
    private void setValueToShopStrret(String s, List<View> shopStreetGridViews) {
        final List<ShopModel> shopModels = parseJsonShopStreet(s);
        // Good[] goodArray= (Good[]) goods.toArray();
        int size=shopModels.size();
        int count=0;//取的次数
        if(size%3==0){
            count=size;
        }else{
            count=size/3+1;
        }
        for(int i=0;i<count;i++){
            final List<ShopModel> shopItems=new ArrayList<ShopModel>();
            for(int j=3*i;j<3*i+3;j++){
                if(j<size){
                    shopItems.add(shopModels.get(j));
                }
            }
            HomeShopStreetAdapter homeShopStreetAdapter=new HomeShopStreetAdapter(context,shopItems);
            MyGridView gridView=new MyGridView(context);
            //gridView.setColumnWidth(230);
            gridView.setHorizontalSpacing(5);
            gridView.setVerticalSpacing(5);
            gridView.setGravity(Gravity.CENTER);
            gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            gridView.setNumColumns(3);
            gridView.setAdapter(homeShopStreetAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context,ShopHomeActivity.class);
                    intent.putExtra(MyConstant.SHOP_USER_ID_KEY,shopItems.get(position).getUserId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            shopStreetGridViews.add(gridView);
        }
        shopStreetViewPager.setAdapter(new MyPagerAdapter(shopStreetGridViews));
        //mainActivity.menu.addIgnoredView(shopStreetViewPager);
        LinearLayout shopStreetPointGroup= (LinearLayout) v.findViewById(R.id.ll_point_group_shop_street);
        initPoint(shopStreetPointGroup,shopStreetGridViews.size());
        shopStreetViewPager.addOnPageChangeListener(new MyPageChangeListener(shopStreetGridViews,shopStreetPointGroup));
    }


    /**
     * 给精品推荐的view设置值
     * @param s
     * @param gridViews
     */
    private void setValueToJingPinTuiJian(String s, List<View> gridViews) {
        final List<Good> goods = parseCaiNiLikeAndJingPin(s);
        // Good[] goodArray= (Good[]) goods.toArray();
        int size=goods.size();
        int count=0;//取的次数
        if(size%3==0){
            count=size;
        }else{
            count=size/3+1;
        }
        for(int i=0;i<count;i++){
            final List<Good> goodItems=new ArrayList<Good>();
            for(int j=3*i;j<3*i+3;j++){
                if(j<size){
                    goodItems.add(goods.get(j));
                }
            }
            JingPinAdapter jingPinAdapter=new JingPinAdapter(context,goodItems);
            MyGridView gridView=new MyGridView(context);
            //gridView.setColumnWidth(230);
            gridView.setHorizontalSpacing(5);
            gridView.setVerticalSpacing(5);
            gridView.setGravity(Gravity.CENTER);
            gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            gridView.setNumColumns(3);
            gridView.setAdapter(jingPinAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   // Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context,ProductDetailActivity.class);
                    intent.putExtra(MyConstant.GOOD_KEY,goodItems.get(position));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            gridViews.add(gridView);
        }
        jingPinViewPager.setAdapter(new MyPagerAdapter(gridViews));
        //mainActivity.menu.addIgnoredView(jingPinViewPager);
        LinearLayout jingPinPointGroup= (LinearLayout) v.findViewById(R.id.ll_point_group_jing_pin);
        initPoint(jingPinPointGroup,gridViews.size());
        jingPinViewPager.addOnPageChangeListener(new MyPageChangeListener(gridViews,jingPinPointGroup));
    }


    /**
     * 给限时秒杀的各个view设置值
     * @param s
     */
    private void setValueToXianShiMiaoSha(String s) {
        final List<Good> goods = new ArrayList<Good>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Good good = new Good();
                JSONObject js = jsonArray.getJSONObject(i);
                good.setGoodId(js.getString("goods_id"));
                good.setGoodsImg(js.getString("goods_img"));
                good.setGoodsThumb(js.getString("goods_thumb"));
                good.setPromoteEndDate(js.getString("promote_end_date"));
                good.setPromotePrice(js.getString("promote_price"));
                good.setIsPromote(js.getString("is_promote"));
                good.setPromoteStartDate(js.getString("promote_start_date"));
                good.setGoodName(JsonHelper.decodeUnicode(js.getString("goods_name")));
                good.setMarket_price(js.getString("market_price"));
                good.setShopPrice(JsonHelper.decodeUnicode(js.getString("shop_price")));
                good.setGoodsNumber(js.getString("goods_number"));
                goods.add(good);
                promoteEndTime=js.getString("promote_end_date");//获取促销结束时间
                //给限时秒杀的时间设置值
                setXianShiTime();
                handler.sendEmptyMessageDelayed(XIAN_SHI_TIME,1000);
            }
            xianShiMiaoShaImagView= (ImageView) v.findViewById(R.id.img_xian_shi_miao_sha_content);
            //动态设置imageview的高度
            int tempWidth4=screenWidth/2-5;
            ViewGroup.LayoutParams layoutParams4=xianShiMiaoShaImagView.getLayoutParams();
            layoutParams4.height=tempWidth4;
            xianShiMiaoShaImagView.setLayoutParams(layoutParams4);

            xianShiMiaoShaImagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra(MyConstant.GOOD_KEY, goods.get(0));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            if(goods.size()>0){
                final String urlString=goods.get(0).getGoodsThumb();
                ImageLoadHelper imageLoadHelper=new ImageLoadHelper();
                ImageLoader imageLoader=imageLoadHelper.getImageLoader();
                ImageLoader.ImageListener listener=imageLoader.getImageListener(xianShiMiaoShaImagView,R.mipmap.yu_jia_zai,
                        R.mipmap.yu_jia_zai);
                imageLoader.get(urlString,listener);
            }


            //限时秒杀的gridview
            xianShiMiaoShaGridView = (MyGridView) v.findViewById(R.id.grid_view_xian_shi_miao_sha);
            if (goods.size() >= 5) {
                XianShiAdapter xianShiAdapter = new XianShiAdapter(context, getElementsFromList(goods, 5));
                xianShiMiaoShaGridView.setAdapter(xianShiAdapter);
                xianShiMiaoShaGridView.setOnItemClickListener(new XianShiOnItemClickListener(getElementsFromList(goods, 5)));
            }else if(goods.size()>0 && goods.size()<5){
                goods.remove(0);
                XianShiAdapter xianShiAdapter = new XianShiAdapter(context, goods);
                xianShiMiaoShaGridView.setAdapter(xianShiAdapter);
                xianShiMiaoShaGridView.setOnItemClickListener(new XianShiOnItemClickListener(goods));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将json数据写入缓存中
     * @param key ,写入缓存时使用的key
     * @param data
     */
    private void writeDataToCach(String key,String data) {
        SharedPreferences.Editor editor=cachSharePrefrences.edit();
        editor.putString(key,data);
        editor.commit();
    }


    /**
     * 手动设定viewpager的高度
     * @param width viewpager里面装的图片的宽度，注意是图片的宽度，而不是viewpager本身的宽度
     */
    private void setHeightToViewPager(ViewPager viewPager,int width,int height) {
        //设置firstviewpager的高度，使它能铺满全屏
        int tempHeight=screenWidth*height/width;//216和414是图片的宽高比
        ViewGroup.LayoutParams layoutParams=viewPager.getLayoutParams();
        layoutParams.height=tempHeight;
        viewPager.setLayoutParams(layoutParams);
    }

    /**
     * 给限时秒杀的时间设置值
     */
    private void setXianShiTime() {
        Map<String,Long> map= MyTimeHelper.getTimeCha(promoteEndTime);
        hourTextView.setText(FormatHelper.convertStringToTwoString(""+map.get(MyConstant.HOUR_KEY)));
        minuteTextView.setText(FormatHelper.convertStringToTwoString(""+map.get(MyConstant.MINUTE_KEY)));
        secondTextView.setText(FormatHelper.convertStringToTwoString(""+map.get(MyConstant.SECOND_KEY)));
    }


    /**
     *将json数据解析出来放到集合里面
     */
    private List<ShopModel> parseJsonShopStreet(String s) {
        MyLog.d(tag, "店铺街返回的数据是" + s);
        List<ShopModel> shopModels=new ArrayList<ShopModel>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONArray jsonArray=jsonObject1.getJSONArray("list");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject ziJsonObj=jsonArray.getJSONObject(i);
                ShopModel shopModel=new ShopModel();
                shopModel.setShopId(ziJsonObj.getString("shop_id"));
                shopModel.setUserId(ziJsonObj.getString("user_id"));
                shopModel.setShopName(JsonHelper.decodeUnicode(ziJsonObj.getString("shop_name")));
                shopModel.setShopLogo(ziJsonObj.getString("shop_logo"));
                shopModel.setLogoThumb(ziJsonObj.getString("logo_thumb"));
                shopModel.setStreetThumb(ziJsonObj.getString("street_thumb"));
                shopModel.setBrandThumb(ziJsonObj.getString("brand_thumb"));
                shopModel.setCommenTrank(ziJsonObj.getString("commentrank_font"));
                shopModel.setCommentServer(ziJsonObj.getString("commentserver_font"));
                shopModel.setCommentDelivery(ziJsonObj.getString("commentdelivery_font"));
                shopModel.setCommenTrankScore(ziJsonObj.getString("commentrank"));
                shopModel.setCommentServerScore(ziJsonObj.getString("commentserver"));
                shopModel.setCommentDeliveryScore(ziJsonObj.getString("commentdelivery"));
                shopModel.setGazeNumber(ziJsonObj.getString("gaze_number"));
                shopModel.setGazeStatus(ziJsonObj.getString("gaze_status"));
                JSONArray goodsArray=null;
                try{
                    goodsArray=ziJsonObj.getJSONArray("goods");//注意这里的处理方法，这句话崩了，不至于让整个程序都崩掉
                }catch (Exception e){
                    e.printStackTrace();
                }
                List<Good> goods=new ArrayList<Good>();
                if(goodsArray!=null){
                    for(int j=0;j<goodsArray.length();j++){
                        JSONObject goodJsonObject=goodsArray.getJSONObject(j);
                        Good good=new Good();
                        good.setGoodId(goodJsonObject.getString("goods_id"));
                        good.setGoodName(JsonHelper.decodeUnicode(goodJsonObject.getString("goods_name")));
                        good.setGoodsNumber(goodJsonObject.getString("goods_number"));
                        good.setMarket_price(JsonHelper.decodeUnicode(goodJsonObject.getString("market_price")));
                        good.setShopPrice(goodJsonObject.getString("shop_price"));
                        good.setGoodsThumb(goodJsonObject.getString("goods_thumb"));
                        good.setGoodsImg(goodJsonObject.getString("goods_img"));
                        good.setSalesVolume(goodJsonObject.getString("sales_volume"));
                        good.setCommentsNumber(goodJsonObject.getString("comments_number"));
                        goods.add(good);
                    }
                }
                shopModel.setGoods(goods);
                shopModels.add(shopModel);
            }
            MyLog.d(tag, "集合的大小:" + shopModels.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopModels;
    }





    /**
     * 猜你喜欢和精品推荐的json数据解析
     */
    public List<Good> parseCaiNiLikeAndJingPin(String s){
        List<Good> goods = new ArrayList<Good>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Good good = new Good();
                JSONObject js = jsonArray.getJSONObject(i);
                good.setGoodId(js.getString("goods_id"));
                good.setUserId(js.getString("user_id"));
                good.setGoodName(JsonHelper.decodeUnicode(js.getString("goods_name")));
                good.setWarehousePrice(js.getString("warehouse_price"));
                good.setWarehousePromotePrice(js.getString("warehouse_promote_price"));
                good.setRegionPrice(js.getString("region_price"));
                good.setPromotePrice(js.getString("region_promote_price"));
                good.setModel_price(js.getString("model_price"));
                good.setModel_attr(js.getString("model_attr"));
                good.setGoods_name_style(js.getString("goods_name_style"));
                good.setCommentsNumber(js.getString("comments_number"));
                good.setSalesVolume(js.getString("sales_volume"));
                good.setMarket_price(js.getString("market_price"));
                good.setIsNew(js.getString("is_new"));
                good.setIsBest(js.getString("is_best"));
                good.setIsHot(js.getString("is_hot"));
                good.setGoodsNumber(js.getString("goods_number"));
                good.setOrgPrice(js.getString("org_price"));
                good.setShopPrice(JsonHelper.decodeUnicode(js.getString("shop_price")));
                good.setPromotePrice(JsonHelper.decodeUnicode(js.getString("promote_price")));
                good.setGoodType(js.getString("goods_type"));
                good.setPromoteStartDate(js.getString("promote_start_date"));
                good.setPromoteEndDate(js.getString("promote_end_date"));
                good.setIsPromote(js.getString("is_promote"));
                good.setGoodsBrief(js.getString("goods_brief"));
                good.setGoodsThumb(js.getString("goods_thumb"));
                good.setGoodsImg(js.getString("goods_img"));
                goods.add(good);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return goods;
    }




    /**
     * 从网络获取广告图片
     * @param s 从网络返回的json数据
     * @param binnerPositionFlag 广告的位置，是最上面的，还是中间的，还是最下面的
     * @param imageViews 将广告图片添加到的view的集合
     * @param refreshFlag 需要刷新的view的消息标记
     * @param scrollFlag 需要滚动的view的标记
     */
    private void getBinnerImageFromIntenet(String s,int binnerPositionFlag, final List<View> imageViews, final int refreshFlag, final int scrollFlag) {
        MyLog.d(tag, "广告位的:" + s);
        s= FormatHelper.removeBom(s);
        final List<Binner> binners=new ArrayList<Binner>();
        try {
            JSONArray jsonArray=new JSONArray(s);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                Binner binner=new Binner();
                binner.setImg(jsonObject.getString("ad_code"));
                binners.add(binner);
                /*switch (binnerPositionFlag){
                    case FIRST_BINNER_FLAG:
                        if(!("2".equals(binner.getId())||"16".equals(binner.getId())||"15".equals(binner.getId()))){
                            binners.add(binner);
                        }
                        break;
                    case SECOND_BINNER_FLAG:
                        if("2".equals(binner.getId())){
                            binners.add(binner);
                        }
                        break;
                    case THREE_BINNER_FLAG:
                        if("16".equals(binner.getId())){
                            binners.add(binner);
                        }
                        break;
                }*/

            }
            final ImageLoadHelper imageLoadHelper=new ImageLoadHelper();
            for(final Binner binner:binners){
                ImageView imageView=new ImageView(context);
                ImageLoader imageLoader=imageLoadHelper.getImageLoader();
                ImageLoader.ImageListener imageListener=imageLoader.getImageListener(imageView,
                        R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
                imageLoader.get(binner.getImg(),imageListener);
                imageViews.add(imageView);
            }
            handler.sendEmptyMessage(refreshFlag);
            handler.sendEmptyMessage(scrollFlag);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从good集合里面取出前面n个元素
     *
     */
    public List<Good> getElementsFromList(List<Good> goods, int num) {
        List<Good> list = new ArrayList<Good>();
        for (int i = 1; i < num; i++) {
            list.add(goods.get(i));
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_all_dong_man://全部动漫
                Toast.makeText(context, "点击了全部动漫", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_tui_jian_zhu_ti://推荐主题
                Toast.makeText(context, "点击了推荐主题", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_you_hui_huo_dong://优惠活动
                Toast.makeText(context, "点击了优惠活动", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_ship_tuan_gou://团购
                Toast.makeText(context, "点击了团购", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_my_guan_zhu://我的关注
                myGuanZhuClickChuLi();
                break;
            case R.id.ll_my_order://我的订单
                MyOrderClickChuLi();
                break;
            case R.id.ll_ji_fen_dui_huan://积分兑换
                Toast.makeText(context, "点击了积分兑换", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_help_center://帮助中心
                Toast.makeText(context, "点击了帮助中心", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_xian_shi_more://限时秒杀的更多按钮
                Intent toXianShiMiaoShaMore=new Intent(context, XianShiMiaoShaMoreActivity.class);
                toXianShiMiaoShaMore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toXianShiMiaoShaMore);
                break;
            case R.id.text_jing_pin_more://精品推荐的更多按钮
                Intent toJingPinTuiJianMore=new Intent(context, JingPinTuiJianMoreActivity.class);
                toJingPinTuiJianMore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toJingPinTuiJianMore);
                break;
            case R.id.text_dian_pu_jie_more://店铺街的更多按钮
                //Toast.makeText(context, "点击了店铺街更多", Toast.LENGTH_SHORT).show();
                Intent toDianPuJieIntent=new Intent(context, MainActivity.class);
                toDianPuJieIntent.putExtra(MyConstant.MAIN_ACTIVITY_LABLE_FALG_KEY,MyConstant.SHOPPING_STRRET_FLAG_KEY);
                startActivity(toDianPuJieIntent);
                break;
        }
    }


    /**
     * 我的关注点击事件处理
     */
    private void myGuanZhuClickChuLi() {
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(context,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_GUAN_ZHU);
        }else {
            toGuanZhuActivity();
        }
    }

    /**
     * 跳转到关注界面
     */
    private void toGuanZhuActivity() {
        Intent intent=new Intent(context, GuanZhuListActivity.class);
        startActivity(intent);
    }


    /**
     * 浏览记录的点击事件处理
     */
    private void liuLanJiLuClickChuLi() {
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(context,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_LIU_LAN_JI_LU);
        }else {
            Intent intent=new Intent(context, LiuLanJiLuListActivity.class);
            startActivity(intent);
            //MyLog.d(tag,"浏览记录内部的执行了吗？");
        }
    }


    /**
     * 我的钱包点击事件处理
     */
    private void MyWalletClickChuLi() {
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(context,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_WALLET);
        }else {
            sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
            getDataFromIntenetAndSetView(uid,sid);
        }
    }

    /**
     * 我的订单的点击事件处理
     */
    private void MyOrderClickChuLi(){

        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(context,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_ORDER);
        }else {
            toDingDanListAcitivy(MyConstant.ALL_DING_DAN);
        }
    }


    /**
     * 收获地址的点击事件处理
     */
    private void shipAddressClickChuLi(){
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(context,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_SHIP_ADDRESS);
        }else {
            Intent toShipAddressIntent=new Intent(context, EditShipAddressActivity.class);
            startActivity(toShipAddressIntent);
        }
    }




    /**
     * 联网获取数据，并给view赋值
     */
    private void getDataFromIntenetAndSetView(final String uid, final String sid) {
        StringRequest userInfoRequest=new StringRequest(Request.Method.POST, userInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        User user=parseJson(s);
                        Intent toMyWalletIntent=new Intent(context, FundManagerActivity.class);
                        toMyWalletIntent.putExtra(MyConstant.USER_KEY,user);
                        startActivity(toMyWalletIntent);
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
     * 跳转到订单列表界面
     * @param str 传给订单列表界面的标记，到底是从哪一个跳转过去的
     */
    private void toDingDanListAcitivy(String str) {
        Intent allDingDanIntent=new Intent(context, DingDanListActivity.class);
        allDingDanIntent.putExtra(MyConstant.TO_DING_DAN_LIST_KEY, str);
        allDingDanIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(allDingDanIntent);

    }


    @Override
    public void onStart() {
        super.onStart();
//        handler.removeMessages(SCROLL__FIRST_BINNER);
//        handler.removeMessages(XIAN_SHI_TIME);
//        handler.sendEmptyMessage(SCROLL__FIRST_BINNER);
//        handler.sendEmptyMessage(XIAN_SHI_TIME);
    }

    @Override
    public void onStop() {
        super.onStop();

        //移除忽视的view，否则容易产生内存溢出
        mainActivity.menu.removeIgnoredView(firstViewPager);
        mainActivity.menu.removeIgnoredView(secondViewPager);
        mainActivity.menu.removeIgnoredView(threeViewPager);
        mainActivity.menu.removeIgnoredView(jingPinViewPager);
        mainActivity.menu.removeIgnoredView(shopStreetViewPager);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(SCROLL__FIRST_BINNER);
        handler.removeMessages(XIAN_SHI_TIME);

    }

    /**
     * 广告viewpager的滚动监听器
     */
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener{
        private List<View> imageViews;
        private LinearLayout pointGroup;
        private int lastPosition;

        public MyPageChangeListener(List<View> imageViews, LinearLayout pointGroup) {
            this.imageViews = imageViews;
            this.pointGroup = pointGroup;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        /**
         * 页面切换后调用
         * position  新的页面位置
         */
        public void onPageSelected(int position) {
            if(imageViews.size()>0){
                position = position % imageViews.size();
            }
            //改变指示点的状态
            //把当前点enbale 为true
            if(imageViews.size()>1){
                pointGroup.getChildAt(position).setEnabled(true);
                //把上一个点设为false
                pointGroup.getChildAt(lastPosition).setEnabled(false);
                lastPosition = position;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    /**
     * 广告viewpager的触摸监听器
     */
    public class MyPageTouchListener implements View.OnTouchListener{
        private boolean isStoped = false;
        private int scrollFlag;
        private ViewPager viewPager;

        /**
         *
         * @param scrollFlag 向handler里面发送消息的标记
         * @param viewPager
         */
        public MyPageTouchListener(int scrollFlag, ViewPager viewPager) {
            this.scrollFlag = scrollFlag;
            this.viewPager = viewPager;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float firstX = 0;//手指按下时的x值
            float firstY = 0;
            float distance = 0f;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    firstX = event.getX();
                    firstY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float lastX = event.getX();
                    float lastY = event.getY();
                    distance = (float) Math.sqrt((lastX - firstX) * (lastX - firstX) +
                            (lastY - firstY) * (lastY - firstY));
                    if (distance > 10) {
                        handler.removeMessages(scrollFlag);//暂停轮播
                        isStoped = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (isStoped) {
                        handler.sendEmptyMessageDelayed(scrollFlag, 2000);//重新开始轮播
                        isStoped=false;
                    } else { //这种情况下属于点击事件
                        Toast.makeText(context, "" + viewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;//这里必须返回false，否则viewpager的ontouchenevnt接收不到事件
        }
    }

    /**
     * 限时秒杀gridview的项目点击监听器
     */
    public class XianShiOnItemClickListener implements AdapterView.OnItemClickListener{
        private List<Good> goods;
        public XianShiOnItemClickListener(List<Good> goods){
            this.goods=goods;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(context,position+"",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(context,ProductDetailActivity.class);
            intent.putExtra(MyConstant.GOOD_KEY,goods.get(position));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 精品推荐gridview的项目点击监听器
     */
    public class JingPinOnItemClickListener implements AdapterView.OnItemClickListener{
        private List<Good> goods;

        public JingPinOnItemClickListener(List<Good> goods) {
            this.goods = goods;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(context,position+"",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(context,ProductDetailActivity.class);
            intent.putExtra(MyConstant.GOOD_KEY,goods.get(position));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 广告viewpager的适配器
     */
    private class MyPagerAdapter extends PagerAdapter {
        private List<View> imageViews;
        public MyPagerAdapter(List<View> imageViews){
            this.imageViews=imageViews;
        }
        @Override
        /**
         * 获得页面的总数
         */
        public int getCount() {
            if(imageViews.size()==1){
                return 1;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        /**
         * 获得相应位置上的view
         * container  view的容器，其实就是viewpager自身
         * position 	相应的位置
         */
        public Object instantiateItem(ViewGroup container, int position) {
            View view=null;
            //对ViewPager页号求模取出View列表中要显示的项
            if(imageViews.size()>0){
                position %= imageViews.size();
                if (position<0 ){
                    position = imageViews.size()+position;
                }

                view =imageViews.get(position);
//如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                ViewParent vp =view.getParent();
                if (vp!= null ){
                    ViewGroup parent = (ViewGroup)vp;
                    parent.removeView(view);
                }
                container.addView(view);
            }

            return view;
        }

        @Override
        /**
         * 判断 view和object的对应关系
         */
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        /**
         * 销毁对应位置上的object
         */
        public void destroyItem(ViewGroup container, int position, Object object) {
            //container.removeView(firstImageViews.get(position % firstImageViews.size()));
        }
    }


    /**
     * 从其他活动返回的数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_LOGIN_WALLET://从登陆界面返回的数据,我的钱包跳过去的
                if(resultCode== Activity.RESULT_OK){
                    String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
                    String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
                    getDataFromIntenetAndSetView(uid,sid);
                }
                break;
            case REQUEST_CODE_LOGIN_ORDER://从订单跳过去的
                if(resultCode== Activity.RESULT_OK){
                    toDingDanListAcitivy(MyConstant.ALL_DING_DAN);
                }
                break;
            case REQUEST_CODE_LOGIN_GUAN_ZHU://从关注跳过去的
                if(resultCode== Activity.RESULT_OK){
                    toGuanZhuActivity();
                }
                break;
            case REQUEST_CODE_LOGIN_SHIP_ADDRESS://从收获地址跳过去的
                if(resultCode== Activity.RESULT_OK){
                    Intent toShipAddressIntent=new Intent(context, EditShipAddressActivity.class);
                    startActivity(toShipAddressIntent);
                }
                break;
            case REQUEST_CODE_LOGIN_LIU_LAN_JI_LU://从浏览记录跳过去的
                if(resultCode== Activity.RESULT_OK){
                    liuLanJiLuClickChuLi();
                    MyLog.d(tag,"浏览记录回来了吗");
                }
                break;
        }
    }
}
