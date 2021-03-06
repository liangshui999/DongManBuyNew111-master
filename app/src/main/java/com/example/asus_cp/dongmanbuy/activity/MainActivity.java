package com.example.asus_cp.dongmanbuy.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.dian_pu_jie.ShopHomeActivity;
import com.example.asus_cp.dongmanbuy.activity.gou_wu.DingDanListActivity;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.activity.main_activity_xiang_guan.LiuLanJiLuListActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.GuanZhuListActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.PersonalCenterActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.ShouCangListActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.data_set.ChangPasswordPersonalCenterActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.data_set.DataSetActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.data_set.EditShipAddressActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager.FundManagerActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager.HongBaoListActivity;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.activity.search.SearchActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.fragment.CategoryFragment;
import com.example.asus_cp.dongmanbuy.fragment.FindFragment;
import com.example.asus_cp.dongmanbuy.fragment.HomeFragment;
import com.example.asus_cp.dongmanbuy.fragment.ShopStreetFragment;
import com.example.asus_cp.dongmanbuy.fragment.ShoppingCarFragment;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.User;
import com.example.asus_cp.dongmanbuy.util.CategoryImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.MyScreenInfoHelper;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final int REQUEST_CODE_SHOU_CANG_KEY = 9;
    private static final int REQUEST_CODE_SHOPPING_CAR =10 ;
    private static final int REQUEST_CODE_TO_LOGIN_ACTIVITY = 12;

    private String shoppingCarListUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/list";//购物车列表
    //private android.support.v7.widget.SearchView searchView;
    private DrawerLayout drawerLayout;
    private ImageView searchImageView;//搜索的图片
    private de.hdodenhof.circleimageview.CircleImageView loginButton;//登录按钮
    private ImageView messageButton;//消息按钮
    //private ViewPager viewPager;
    private FrameLayout frameLaout;//内容区域的容器
    private FragmentManager fragmentManager;//
    private HomeFragment homeFragment;
    private CategoryFragment categoryFragment;
    private FindFragment findFragment;
    private ShopStreetFragment shopStreetFragment;
    private ShoppingCarFragment shoppingCarFragment;


    private LinearLayout homell;//首页标签
    private LinearLayout categoryll;//分类标签
    private LinearLayout findll;//发现标签
    private LinearLayout shopStreetll;//店铺街标签
    private LinearLayout shoppingCarll;//购物车标签
    private LinearLayout messageAndSao;//消息和扫一扫
    private LinearLayout messagell;//消息
    private LinearLayout sao;//消息和扫一扫
    private List<Fragment> fragments;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ImageView homeImg;
    private ImageView categoryImg;
    private ImageView findImg;
    private ImageView shopStreetImg;
    private ImageView shoppingCarImg;
    private TextView homeText;
    private TextView categoryText;
    private TextView findText;
    private TextView shopStreetText;
    private TextView shoppingCarText;

    //常量
    public static final int HOME=0;
    public static final int CATEGORY=1;
    public static final int FIND=2;
    public static final int SHOP_STREET=3;
    public static final int SHOPPING_CAR=4;

    public SlidingMenu menu;

    //侧滑菜单
    private String hongBaoListUrl="http://www.zmobuy.com/PHP/?url=/user/bonus_list";//获取所有红包的接口

    private de.hdodenhof.circleimageview.CircleImageView loginImage;//登录按钮
    private TextView nameTextView;//名字
    private RelativeLayout jiFenAndHelpRelativeLayout;//积分和帮助所在的布局
    private TextView jiFenTextView;//积分
    private ImageView helpImageView;//帮助按钮
    private RelativeLayout homeRelativeLayout;//首页
    private RelativeLayout liuLanJiLuRelativeLayout;//浏览记录
    private RelativeLayout myShouCangRelativeLayout;//我的收藏
    private RelativeLayout guanZhuRelativeLayout;//关注
    private RelativeLayout youHuiQuanRelativeLayou;//优惠券
    private TextView youHuiQuanCountTextView;//优惠券数目
    private RelativeLayout settingRelativeLayout;//设置

    /*private TextView myZhuYeTextView;//我的主页
    private TextView myOrderTextView;//我的订单
    private TextView myWalletTextView;//我的钱包
    private TextView myShouCangTextView;//我的收藏
    private TextView guanZhuDianPuTextView;//关注店铺
    private TextView shipAddressTextView;//收货地址
    private TextView changPassWordTextView;//修改密码
    private TextView setTingTextView;//设置
    private TextView emailTextView;//邮箱*/

    private String tag="MainActivity";

    private SharedPreferences sharedPreferences;

    public static final int REQUEST_CODE_LOGIN_HOME=1;//从主页跳转到登陆界面的请求码

    public static final int REQUEST_CODE_LOGIN_ORDER=2;//从订单跳转到登陆界面的请求码

    public static final int REQUEST_CODE_LOGIN_WALLET=3;//从钱包跳转到登陆界面的请求码

    public static final int REQUEST_CODE_LOGIN_SHOU_CANG=4;//从收藏跳转到登陆界面的请求码

    public static final int REQUEST_CODE_LOGIN_GUAN_ZHU=5;//从关注跳转到登陆界面的请求码

    public static final int REQUEST_CODE_LOGIN_SHIP_ADDRESS=6;//从收货地址跳转到登陆界面的请求码

    public static final int REQUEST_CODE_LOGIN_CHANGE_PASSWORD=7;//从修改密码跳转到登陆界面的请求码

    public static final int REQUEST_CODE_LOGIN_SETTING=8;//从设置跳转到登陆界面的请求码

    public static final int REQUEST_CODE_TO_SETTING=9;//直接跳转到设置界面

    public static final int REQUEST_CODE_TO_PERSONAL_CENTER=11;//跳转到个人中心

    public static final int REQUEST_CODE_LOGIN_LIU_LAN_JI_LU=12;//浏览记录

    public static final int REQUEST_CODE_YOU_HUI_QUAN=13;//优惠券

    private String userInfoUrl="http://www.zmobuy.com/PHP/?url=/user/info";//用户信息的接口


    private View messageAndSaoYiSaoView;

    private PopupWindow messageAndSaoPopuWindow;

    private int messageAndSaoCount;//标记message和扫一扫的是显示还是隐藏

    public static final int SCAN_CODE = 1;//扫一扫的请求码

    public static final int PERMISSION_CODE_CAMERA =1;//扫一扫请求运行时权限的请求吗

    private String passUid;//传递给碎片的uid
    private String passsid;

    private String labelFlag;//跳转到哪个标签的标记

    private int densty;//屏幕像素密度

    private ImageLoadHelper imageLoaderhelper;

    private long pressedTime;//按下返回键的时间

    private boolean isLogined;//判断用户是否已经登陆，侧滑菜单需要用到

    private Fragment mCurrentFragment;//当前的fragment

    private Fragment mLastFragment;//上一个fragment


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        //MyLog.d(tag,"oncreate()执行了");

    }

    /**
     * 初始化数据
     */
    private void initData() {
        sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, MODE_APPEND);
        fragments=new ArrayList<Fragment>();
        homeFragment=new HomeFragment();
        categoryFragment=new CategoryFragment();
        findFragment=new FindFragment();
        shopStreetFragment=new ShopStreetFragment();
        shoppingCarFragment=new ShoppingCarFragment();
        fragments.add(homeFragment);
        fragments.add(categoryFragment);
        fragments.add(findFragment);
        fragments.add(shopStreetFragment);
        fragments.add(shoppingCarFragment);

        fragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }


        };
        new CategoryImageLoadHelper(getXiangSuMiDu());

        //获取屏幕像素密度
        densty= MyScreenInfoHelper.getScreenDpi();

        imageLoaderhelper=new ImageLoadHelper();

    }

    public int getDensty(){
        return densty;
    }


    /**
     * 初始化view
     */
    public void initView() {
        /*searchView= (SearchView) findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);//默认不聚焦
        searchView.setFocusableInTouchMode(true);
        searchView.clearFocus();
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //searchView.clearFocus();
                }else{
                    //searchView.requestFocus();
                }
            }
        });*/
        //searchView.setFocusableInTouchMode(false);
        //searchView.setEnabled(false);
        // Get the SearchView and set the searchable configuration
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //searchView = (SearchView) findViewById(R.id.search_view);
        // 前面的包名是根节点下的包名，不要带activity
        /*if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo
                    (new ComponentName("com.example.asus_cp.dongmanbuy",
                            "com.example.asus_cp.dongmanbuy.activity.MySearchActivity")));
            searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
            searchView.clearFocus();
        }*/

        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        searchImageView= (ImageView) findViewById(R.id.img_search_main);
        loginButton= (CircleImageView) findViewById(R.id.img_btn_login);
        messageButton= (ImageView) findViewById(R.id.img_btn_message);

        loginButton.setOnClickListener(this);
        searchImageView.setOnClickListener(this);
        messageButton.setOnClickListener(this);

        //初始化底部标签
        homell = (LinearLayout) findViewById(R.id.ll_home);
        categoryll = (LinearLayout) findViewById(R.id.ll_category);
        findll = (LinearLayout) findViewById(R.id.ll_find);
        shopStreetll = (LinearLayout) findViewById(R.id.ll_shop_street);
        shoppingCarll = (LinearLayout) findViewById(R.id.ll_shopping_car);
        homell.setOnClickListener(this);
        categoryll.setOnClickListener(this);
        findll.setOnClickListener(this);
        shopStreetll.setOnClickListener(this);
        shoppingCarll.setOnClickListener(this);
        homeImg= (ImageView) findViewById(R.id.img_home);
        categoryImg= (ImageView) findViewById(R.id.img_category);
        findImg= (ImageView) findViewById(R.id.img_find);
        shopStreetImg= (ImageView) findViewById(R.id.img_shop_street);
        shoppingCarImg= (ImageView) findViewById(R.id.img_shopping_car);
        homeText= (TextView) findViewById(R.id.text_home);
        categoryText= (TextView) findViewById(R.id.text_category);
        findText= (TextView) findViewById(R.id.text_find);
        shopStreetText= (TextView) findViewById(R.id.text_shop_street);
        shoppingCarText= (TextView) findViewById(R.id.text_shoppingz_car);

        //将首页的文字和图片设置为红色
        homeImg.setImageResource(R.mipmap.home_selected);
        homeText.setTextColor(getResources().getColor(R.color.bottom_lable_color));


        //初始化framelayout
        frameLaout= (FrameLayout) findViewById(R.id.frame_layout_main);
        fragmentManager=getSupportFragmentManager();
        mLastFragment=homeFragment;
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_main, homeFragment);
        fragmentTransaction.commit();



        //跳转到购物车碎片(从别的活动跳过来的时候,只有当活动在后台销毁的时候，这段代码才会调用)
        labelFlag=getIntent().getStringExtra(MyConstant.MAIN_ACTIVITY_LABLE_FALG_KEY);
        MyLog.d(tag, "labelFlag=" + labelFlag);
        if(MyConstant.SHOPPING_CAR_FLAG_KEY.equals(labelFlag)){
            MyLog.d(tag, "init中的方法执行了吗");
            showAndHideShoppingCarFragment();
            resetLabel();
            shoppingCarImg.setImageResource(R.mipmap.home_selected);
            shoppingCarText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }else if(MyConstant.SHOPPING_STRRET_FLAG_KEY.equals(labelFlag)){
//            FragmentTransaction shopStreetTransaction=fragmentManager.beginTransaction();
//            shopStreetTransaction.replace(R.id.frame_layout_main,shopStreetFragment);
//            shopStreetTransaction.commit();
            showAndHideFragment(shopStreetFragment);
            resetLabel();
            shopStreetImg.setImageResource(R.mipmap.shopstreet_selected);
            shopStreetText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }else if(MyConstant.HOME_FLAG_KEY.equals(labelFlag)){
//            FragmentTransaction homeTransaction=fragmentManager.beginTransaction();
//            homeTransaction.replace(R.id.frame_layout_main,homeFragment);
//            homeTransaction.commit();
            showAndHideFragment(homeFragment);
            resetLabel();
            homeImg.setImageResource(R.mipmap.home_selected);
            homeText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }



        //初始化viewpager
        /*viewPager= (ViewPager) findViewById(R.id.viewpager_main);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetLabel();
                switch (position) {
                    case HOME:
                        homeImg.setImageResource(R.mipmap.home_selected);
                        homeText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        break;
                    case CATEGORY:
                        categoryImg.setImageResource(R.mipmap.category_selected);
                        categoryText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        break;
                    case FIND:
                        findImg.setImageResource(R.mipmap.find_selected);
                        findText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        break;
                    case SHOP_STREET:
                        shopStreetImg.setImageResource(R.mipmap.shopstreet_selected);
                        shopStreetText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        break;
                    case SHOPPING_CAR:
                        shoppingCarImg.setImageResource(R.mipmap.shoppingcar_selected);
                        shoppingCarText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        //slidingMenu= (SlidingMenu) findViewById(R.id.id_menu);


//        //使用侧滑菜单
//        menu = new SlidingMenu(this);
//        menu.setMode(SlidingMenu.LEFT);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);//这个是调节阴影宽度的
//        menu.setShadowDrawable(R.drawable.shadow);
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        menu.setFadeDegree(0.35f);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        menu.setMenu(R.layout.menu_new_layout);
//
//
//        //给侧滑菜单设置滚动的监听事件
//        menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
//            @Override
//            public void onOpened() {
//
//                String uid = sharedPreferences.getString(MyConstant.UID_KEY, null);
//                String sid = sharedPreferences.getString(MyConstant.SID_KEY, null);
//                if (uid != null && !uid.isEmpty()) {
//                    if (!isLogined) {
//                        getDataFromIntenetAndSetYouHuiQuanCount(uid,sid);
//                        getDataFromIntenetAndSetNameAndEmailAndPic(uid, sid);
//                    }
//                } else {
//                    nameTextView.setText(R.string.click_tou_xiang_login);
//                    jiFenTextView.setText("");
//                    loginImage.setImageResource(R.mipmap.tou_xiang);
//                    loginButton.setImageResource(R.mipmap.tou_xiang);
//                    jiFenAndHelpRelativeLayout.setVisibility(View.GONE);//将积分和帮助隐藏
//                    youHuiQuanCountTextView.setText("");
//                    isLogined = false;
//                }
//            }
//        });


        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                String uid = sharedPreferences.getString(MyConstant.UID_KEY, null);
                String sid = sharedPreferences.getString(MyConstant.SID_KEY, null);
                if (uid != null && !uid.isEmpty()) {
                    if (!isLogined) {
                        getDataFromIntenetAndSetYouHuiQuanCount(uid,sid);
                        getDataFromIntenetAndSetNameAndEmailAndPic(uid, sid);
                    }
                } else {
                    nameTextView.setText(R.string.click_tou_xiang_login);
                    jiFenTextView.setText("");
                    loginImage.setImageResource(R.mipmap.tou_xiang);
                    loginButton.setImageResource(R.mipmap.tou_xiang);
                    jiFenAndHelpRelativeLayout.setVisibility(View.GONE);//将积分和帮助隐藏
                    youHuiQuanCountTextView.setText("");
                    isLogined = false;
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        });


        //-----------------------初始化侧滑菜单-----------------------------
        loginImage= (CircleImageView) findViewById(R.id.img_login);
        nameTextView= (TextView) findViewById(R.id.text_name_slid_menu);
        jiFenAndHelpRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_ji_fen_and_help_sliding_menu);
        jiFenTextView= (TextView) findViewById(R.id.text_ji_fen_slding_menu);
        helpImageView= (ImageView) findViewById(R.id.img_help_sliding_menu);
        homeRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_home_menu);
        liuLanJiLuRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_liu_lan_ji_lu_menu);
        myShouCangRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_shou_cang_menu);
        guanZhuRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_guan_zhu_menu);
        youHuiQuanRelativeLayou= (RelativeLayout) findViewById(R.id.re_layout_you_hui_quan_menu);
        youHuiQuanCountTextView= (TextView) findViewById(R.id.text_you_hui_quan_count_sliding_menu);
        settingRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_setting_menu);


        //给侧滑菜单设置点击事件
        loginImage.setOnClickListener(this);
        helpImageView.setOnClickListener(this);
        homeRelativeLayout.setOnClickListener(this);
        myShouCangRelativeLayout.setOnClickListener(this);
        guanZhuRelativeLayout.setOnClickListener(this);
        youHuiQuanRelativeLayou.setOnClickListener(this);
        settingRelativeLayout.setOnClickListener(this);
        liuLanJiLuRelativeLayout.setOnClickListener(this);

        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){

        }else {
            sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
            getDataFromIntenetAndSetYouHuiQuanCount(uid,sid);
            getDataFromIntenetAndSetNameAndEmailAndPic(uid,sid);
        }


        /*myZhuYeTextView= (TextView) findViewById(R.id.text_my_zhu_ye);
        myOrderTextView= (TextView) findViewById(R.id.text_my_order);
        myWalletTextView= (TextView) findViewById(R.id.text_my_wallet);
        myShouCangTextView= (TextView) findViewById(R.id.text_my_shou_cang);
        guanZhuDianPuTextView= (TextView) findViewById(R.id.text_guan_zhu_dian_pu);
        shipAddressTextView= (TextView) findViewById(R.id.text_ship_addres);
        changPassWordTextView= (TextView) findViewById(R.id.text_change_password);
        setTingTextView= (TextView) findViewById(R.id.text_setting);
        emailTextView= (TextView) findViewById(R.id.text_email_slid_menu);*/



       /* myZhuYeTextView.setOnClickListener(this);
        myOrderTextView.setOnClickListener(this);
        myWalletTextView.setOnClickListener(this);
        myShouCangTextView.setOnClickListener(this);
        guanZhuDianPuTextView.setOnClickListener(this);
        shipAddressTextView.setOnClickListener(this);
        changPassWordTextView.setOnClickListener(this);
        setTingTextView.setOnClickListener(this);*/


        //--------初始化消息和扫一扫的视图
        messageAndSaoYiSaoView= LayoutInflater.from(this).inflate(R.layout.message_and_sao_yi_sao_layout,null);
        messageAndSao= (LinearLayout) messageAndSaoYiSaoView.findViewById(R.id.ll_message_and_sao);
        messagell= (LinearLayout)messageAndSaoYiSaoView. findViewById(R.id.ll_message);
        sao= (LinearLayout)messageAndSaoYiSaoView. findViewById(R.id.ll_sao);
        messageAndSao.setOnClickListener(this);
        messagell.setOnClickListener(this);
        sao.setOnClickListener(this);

        messageAndSaoPopuWindow = new PopupWindow(messageAndSaoYiSaoView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        messageAndSaoPopuWindow.setBackgroundDrawable(new ColorDrawable());
        messageAndSaoPopuWindow.setOutsideTouchable(true);
        messageAndSaoPopuWindow.setFocusable(true);







        //原来自己写的侧滑的监听事件
        /*slidingMenu.setOnScrollListnerMy(new OnScrollListenerMySli() {
            @Override
            public void onScroll(View v) {
                String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
                String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
                if(uid!=null && !uid.isEmpty()){
                    getDataFromIntenetAndSetNameAndEmailAndPic(uid,sid);
                }else{
                    nameTextView.setText("");
                    emailTextView.setText("");
                    //loginImage.setImageResource(R.mipmap.ic_launcher);
                }
            }
        });*/

        //throw new RuntimeException("对方不想和你说话，并向你抛出了一个空指针异常");//只是测试用
    }


    /**
     * 显示和隐藏shoppingcarfragment
     */
    private void showAndHideShoppingCarFragment() {
        FragmentTransaction shoppingCarTranscation=fragmentManager.beginTransaction();
        shoppingCarTranscation.hide(mLastFragment);
        if(shoppingCarFragment.isAdded()){
            shoppingCarTranscation.remove(shoppingCarFragment);
            shoppingCarFragment=new ShoppingCarFragment();
            shoppingCarTranscation.add(R.id.frame_layout_main, shoppingCarFragment);
        }else{
            shoppingCarTranscation.add(R.id.frame_layout_main,shoppingCarFragment);
        }
        shoppingCarTranscation.commit();
        mLastFragment=shoppingCarFragment;
    }


    /**
     * 显示新的fragment，隐藏旧的fragemnt
     */
    private void showAndHideFragment(Fragment fragment) {
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        MyLog.d(tag,"fragment.isAdded()="+fragment.isAdded());
        MyLog.d(tag, "mLastFragment:" + mLastFragment.getClass().getSimpleName());
        transaction.hide(mLastFragment);
        if(fragment.isAdded()){
            transaction.show(fragment);
        }else{
            transaction.add(R.id.frame_layout_main,fragment);
        }
        transaction.commit();
        mLastFragment=fragment;
    }


    /**
     * 联网获取优惠券的数目
     */
    private void getDataFromIntenetAndSetYouHuiQuanCount(final String uid, final String sid) {
        StringRequest hongBaoRequest=new StringRequest(Request.Method.POST, hongBaoListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyLog.d(tag,"红包返回的数据是："+response);
                try {
                    int size=0;
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject ziJsObj=jsonArray.getJSONObject(i);
                        String status=ziJsObj.getString("bonus_status");
                        if ("0".equals(status)) {
                            size++;
                        }
                    }
                    if(size>0){
                        youHuiQuanCountTextView.setText("您有"+size+"张优惠券可用");
                    }else{
                        youHuiQuanCountTextView.setText("");
                    }
                } catch (JSONException e) {
                    youHuiQuanCountTextView.setText("");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"}}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(hongBaoRequest);
    }

    /**
     * 将底部标签设置为初始状态
     *
     */
    public void resetLabel(){
        //将标签图片设置为初始状态
        homeImg.setImageResource(R.mipmap.home_normal);
        categoryImg.setImageResource(R.mipmap.category_normal);
        findImg.setImageResource(R.mipmap.find_normal);
        shopStreetImg.setImageResource(R.mipmap.shopstreet_normal);
        shoppingCarImg.setImageResource(R.mipmap.shoppingcar_normal);

        //将文字颜色设置为初始状态
        homeText.setTextColor(Color.BLACK);
        categoryText.setTextColor(Color.BLACK);
        findText.setTextColor(Color.BLACK);
        shopStreetText.setTextColor(Color.BLACK);
        shoppingCarText.setTextColor(Color.BLACK);
    }



    @Override
    protected void onStart() {
        super.onStart();
        MyLog.d(tag, "onStart");
        setIsNeedTongJiPage(false);
    }




    /**
     * 当模式为singtask时，用普通的方法根本就得不到intent的数据，必须使用该方法
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //跳转到购物车碎片
        labelFlag=intent.getStringExtra(MyConstant.MAIN_ACTIVITY_LABLE_FALG_KEY);
        MyLog.d(tag, "labelFlag=" + labelFlag);
        if(MyConstant.SHOPPING_CAR_FLAG_KEY.equals(labelFlag)){
            MyLog.d(tag, "init中的方法执行了吗");
//            FragmentTransaction shoppingTransaction=fragmentManager.beginTransaction();
//            shoppingTransaction.replace(R.id.frame_layout_main, shoppingCarFragment);
//            shoppingTransaction.commit();
            //showAndHideFragment(shoppingCarFragment);
            showAndHideShoppingCarFragment();
            resetLabel();
            shoppingCarImg.setImageResource(R.mipmap.shoppingcar_selected);
            shoppingCarText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }else if(MyConstant.SHOPPING_STRRET_FLAG_KEY.equals(labelFlag)){
//            FragmentTransaction shopStreetTransaction=fragmentManager.beginTransaction();
//            shopStreetTransaction.replace(R.id.frame_layout_main,shopStreetFragment);
//            shopStreetTransaction.commit();
            showAndHideFragment(shopStreetFragment);
            resetLabel();
            shopStreetImg.setImageResource(R.mipmap.shopstreet_selected);
            shopStreetText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }else if(MyConstant.HOME_FLAG_KEY.equals(labelFlag)){
//            FragmentTransaction homeTransaction=fragmentManager.beginTransaction();
//            homeTransaction.replace(R.id.frame_layout_main,homeFragment);
//            homeTransaction.commit();
            showAndHideFragment(homeFragment);
            resetLabel();
            homeImg.setImageResource(R.mipmap.home_selected);
            homeText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_btn_login://登录按钮的点击事件
                //Toast.makeText(this,"点击了登录按钮",Toast.LENGTH_SHORT).show();
                //slidingMenu.toggle();
                //menu.toggle();
                toggleDrawLayout();
                break;
            case R.id.img_btn_message://消息按钮的点击事件
                messageAndSaoClickChuLi();
               /* if(messageAndSaoCount%2==0){
                    messageAndSao.setVisibility(View.VISIBLE);//让下拉框弹出来
                }else{
                    messageAndSao.setVisibility(View.GONE);
                }
                messageAndSaoCount++;*/
                break;
            case R.id.ll_home:
                //viewPager.setCurrentItem(HOME);
//                FragmentTransaction homeTransaction=fragmentManager.beginTransaction();
//                homeTransaction.replace(R.id.frame_layout_main,homeFragment);
//                homeTransaction.commit();
                showAndHideFragment(homeFragment);
                resetLabel();
                homeImg.setImageResource(R.mipmap.home_selected);
                homeText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case R.id.ll_category:
                //viewPager.setCurrentItem(CATEGORY);
//                FragmentTransaction categoryTransaction=fragmentManager.beginTransaction();
//                categoryTransaction.replace(R.id.frame_layout_main,categoryFragment);
//                categoryTransaction.commit();
                showAndHideFragment(categoryFragment);
                resetLabel();
                categoryImg.setImageResource(R.mipmap.category_selected);
                categoryText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case R.id.ll_find:
                //viewPager.setCurrentItem(FIND);
//                FragmentTransaction findTransaction=fragmentManager.beginTransaction();
//                findTransaction.replace(R.id.frame_layout_main,findFragment);
//                findTransaction.commit();
                showAndHideFragment(findFragment);
                resetLabel();
                findImg.setImageResource(R.mipmap.find_selected);
                findText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case R.id.ll_shop_street:
                //viewPager.setCurrentItem(SHOP_STREET);
//                FragmentTransaction shopStreetTransaction=fragmentManager.beginTransaction();
//                shopStreetTransaction.replace(R.id.frame_layout_main,shopStreetFragment);
//                shopStreetTransaction.commit();
                showAndHideFragment(shopStreetFragment);
                resetLabel();
                shopStreetImg.setImageResource(R.mipmap.shopstreet_selected);
                shopStreetText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case R.id.ll_shopping_car:
                shoppingCarClickChuLi();
                break;
            case R.id.ll_message:
                Toast.makeText(this,"点击了消息",Toast.LENGTH_SHORT).show();
                //messageAndSao.setVisibility(View.GONE);
                messageAndSaoPopuWindow.dismiss();
                break;
            case R.id.ll_sao:
                //Toast.makeText(this,"点击了扫一扫",Toast.LENGTH_SHORT).show();
                //messageAndSao.setVisibility(View.GONE);
                messageAndSaoPopuWindow.dismiss();
//                if(MPermissions.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.CAMERA,PERMISSION_CODE_CAMERA)){
//                    Toast.makeText(this,"解释",Toast.LENGTH_SHORT).show();
//                }
                MPermissions.requestPermissions(MainActivity.this, PERMISSION_CODE_CAMERA,
                        Manifest.permission.CAMERA);

                //toScanActivity();
                break;

            case R.id.img_login://登陆
                toPersonalCenter();
                break;
            case R.id.re_layout_home_menu://我的主页
                toPersonalCenter();
                break;
            case R.id.re_layout_shou_cang_menu://我的收藏
                shouCangClickChuLi();
                break;
            case R.id.re_layout_guan_zhu_menu://我的关注
                myGuanZhuClickChuLi();
                break;
            case R.id.re_layout_setting_menu://设置
                setClickChuLi();
                break;
            case R.id.re_layout_liu_lan_ji_lu_menu://浏览记录
                liuLanJiLuClickChuLi();
                break;
            case R.id.re_layout_you_hui_quan_menu://优惠券
                youHuiQuanClickChuLi();
                break;
            case R.id.img_search_main://点击了search按钮
                Intent toSearchIntent=new Intent(this, SearchActivity.class);
                startActivity(toSearchIntent);
                break;
//            case R.id.text_my_order://我的订单
//                myOrderClickChuLi();
//                break;
//            case R.id.text_my_wallet://我的钱包
//                myWalletClickChuLi();
//                break;
//
//            case R.id.text_ship_addres://收获地址
//                shipAddressClickChuLi();
//                break;
//            case R.id.text_change_password://修改密码
//                changePasswordClickChuLi();
//                break;


        }
    }


    /**
     * toggle drawlayout
     */
    public void toggleDrawLayout(){
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }


    /**
     * 跳转到扫描的activity
     */
    private void toScanActivity() {
        Intent intent = new Intent(MainActivity.this, MipcaActivityCapture.class);
        startActivityForResult(intent, SCAN_CODE);
    }


    /**
     * 优惠券的点击事件处理
     */
    private void youHuiQuanClickChuLi() {
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(this,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_YOU_HUI_QUAN);
        }else {
            Intent intent=new Intent(this, HongBaoListActivity.class);
//            if(menu.isMenuShowing()){
//                menu.toggle();
//            }
            toggleDrawLayout();
            startActivity(intent);
            //MyLog.d(tag,"浏览记录内部的执行了吗？");
        }
    }

    /**
     * 浏览记录的点击事件处理
     */
    private void liuLanJiLuClickChuLi() {
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(this,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_LIU_LAN_JI_LU);
        }else {
            Intent intent=new Intent(this, LiuLanJiLuListActivity.class);
//            if(menu.isMenuShowing()){
//                menu.toggle();
//            }
            toggleDrawLayout();
            startActivity(intent);
            //MyLog.d(tag,"浏览记录内部的执行了吗？");
        }
    }


    /**
     * 消息和扫一扫的点击事件处理
     */
    private void messageAndSaoClickChuLi() {
        MyLog.d(tag,"是否显示:"+messageAndSaoPopuWindow.isShowing());
        if(messageAndSaoPopuWindow.isShowing()){
            messageAndSaoPopuWindow.dismiss();
        }else{
            messageAndSaoPopuWindow.showAsDropDown(messageButton, 0, 10);
        }
    }




    /**
     * 购物车的点击事件处理
     */
    private void shoppingCarClickChuLi() {
        //viewPager.setCurrentItem(SHOPPING_CAR);
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(this,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_SHOPPING_CAR);
        }else {
//            FragmentTransaction shoppingTransaction=fragmentManager.beginTransaction();
//            shoppingTransaction.replace(R.id.frame_layout_main, shoppingCarFragment);
//            shoppingTransaction.commitAllowingStateLoss();
            //showAndHideFragment(shoppingCarFragment);
            showAndHideShoppingCarFragment();
            resetLabel();
            shoppingCarImg.setImageResource(R.mipmap.shoppingcar_selected);
            shoppingCarText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }

    }



    /**
     * 我的订单的点击事件处理
     */
    private void myOrderClickChuLi(){

        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(this,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_ORDER);
        }else {
            toDingDanListAcitivy(MyConstant.ALL_DING_DAN);
        }
    }


    /**
     * 我的钱包点击事件处理
     */
    private void myWalletClickChuLi() {
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(this,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_WALLET);
        }else {
            sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
            getDataFromIntenetAndToMyWallet(uid, sid);
        }
    }


    /**
     * 收藏的点击事件处理
     */
    private void shouCangClickChuLi(){
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(this,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_SHOU_CANG);
        }else {
            Intent toShouCangIntent=new Intent(this,ShouCangListActivity.class);
//            if(menu.isMenuShowing()){
//                menu.toggle();
//            }
            toggleDrawLayout();
            startActivity(toShouCangIntent);
        }
    }


    /**
     * 我的关注的点击事件处理
     */
    private void myGuanZhuClickChuLi(){
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(this,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_GUAN_ZHU);
        }else {
            Intent toGuanZhuIntent=new Intent(this,GuanZhuListActivity.class);
//            if(menu.isMenuShowing()){
//                menu.toggle();
//            }
            toggleDrawLayout();
            startActivity(toGuanZhuIntent);
        }
    }



    /**
     * 收获地址的点击事件处理
     */
    private void shipAddressClickChuLi(){
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(this,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_SHIP_ADDRESS);
        }else {
            Intent toShipAddressIntent=new Intent(this, EditShipAddressActivity.class);
            startActivity(toShipAddressIntent);
        }
    }


    /**
     * 修改密码的点击事件处理
     */
    private void changePasswordClickChuLi(){
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(this,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_CHANGE_PASSWORD);
        }else {
            Intent changPassWordIntent=new Intent(this, ChangPasswordPersonalCenterActivity.class);
            startActivity(changPassWordIntent);
        }
    }


    /**
     * 设置的点击事件处理
     */
    private void setClickChuLi(){
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){
            Intent toLoginIntent=new Intent(this,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_LOGIN_SETTING);
        }else {
            sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
            getDataFromIntenetAndToSetting(uid, sid);
        }
    }


    /**
     * 联网获取数据并设置name和email
     */
    public void getDataFromIntenetAndSetNameAndEmailAndPic(final String uid, final String sid){
        StringRequest userInfoRequest=new StringRequest(Request.Method.POST, userInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        jiFenAndHelpRelativeLayout.setVisibility(View.VISIBLE);
                        User user=parseJson(s);
                        nameTextView.setText(user.getName());
                        //emailTextView.setText(user.getEmail());
                        jiFenTextView.setText(user.getJiFen());
                        loginImage.setTag(MyConstant.YU_MING + user.getPic());//设置tag
                        ImageLoader imageLoader1=imageLoaderhelper.getImageLoader();
                        ImageLoader.ImageListener listener=imageLoader1.getImageListener(loginImage,R.mipmap.tou_xiang,
                                R.mipmap.tou_xiang);
                        imageLoader1.get(MyConstant.YU_MING + user.getPic(), listener, 200, 200);

                        loginButton.setTag(MyConstant.YU_MING + user.getPic());
                        ImageLoader imageLoader2=imageLoaderhelper.getImageLoader();
                        ImageLoader.ImageListener listener1=imageLoader2.getImageListener(loginButton,R.mipmap.tou_xiang,
                                R.mipmap.tou_xiang);
                        imageLoader2.get(MyConstant.YU_MING + user.getPic(), listener1, 200, 200);
                        isLogined=true;
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
     * 联网获取数据，并跳转到我的设置
     */
    private void getDataFromIntenetAndToSetting(final String uid, final String sid){
        StringRequest userInfoRequest=new StringRequest(Request.Method.POST, userInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        User user=parseJson(s);
                        Intent settingIntent=new Intent(MainActivity.this, DataSetActivity.class);
                        settingIntent.putExtra(MyConstant.USER_KEY, user);
                        startActivityForResult(settingIntent, REQUEST_CODE_TO_SETTING);
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
     * 联网获取数据，跳转到我的钱包
     */
    private void getDataFromIntenetAndToMyWallet(final String uid, final String sid) {
        StringRequest userInfoRequest=new StringRequest(Request.Method.POST, userInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        User user=parseJson(s);
                        Intent toMyWalletIntent=new Intent(MainActivity.this, FundManagerActivity.class);
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
        Intent allDingDanIntent=new Intent(this, DingDanListActivity.class);
        allDingDanIntent.putExtra(MyConstant.TO_DING_DAN_LIST_KEY, str);
        startActivity(allDingDanIntent);
    }



    /**
     * 跳转到个人中心
     */
    private void toPersonalCenter() {
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        if(uid!=null && !uid.isEmpty()){
            Intent toPersonalCenterIntent=new Intent(this, PersonalCenterActivity.class);
            startActivity(toPersonalCenterIntent);
//            if(menu.isMenuShowing()){
//                menu.toggle();
//            }
            toggleDrawLayout();
        }else{
            Intent toLoginIntent=new Intent(this,LoginActivity.class);
            toLoginIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"homeFragment");
            startActivityForResult(toLoginIntent, REQUEST_CODE_TO_PERSONAL_CENTER);
        }

    }

    @Override
    public void onBackPressed() {
        //searchView.clearFocus();
        MyLog.d(tag, "返回键");
//        //退出程序时，清除sharepre里面的数据
//        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,
//                MODE_APPEND);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.remove(MyConstant.UID_KEY);
//        editor.remove(MyConstant.SID_KEY);
//        editor.apply();


        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            long nowTime=System.currentTimeMillis();
            if(nowTime-pressedTime>2000){
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                pressedTime=nowTime;
            }else{
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
           /* AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("您确定要退出吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //android.os.Process.killProcess(android.os.Process.myPid());//把自己的进程杀掉
                    Intent intent=new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();*/
        }
        //super.onBackPressed();
    }


    /**
     * 获取屏幕分辨率的方法,获取屏幕的像素密度
     */
    public int getXiangSuMiDu(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）
        MyLog.d(tag,"像素密度："+densityDPI+"");
        return densityDPI;
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            MyLog.d(tag, "keydown返回键");
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(PERMISSION_CODE_CAMERA)
    public void requestCameraSuccess(){
        toScanActivity();
    }

    @PermissionDenied(PERMISSION_CODE_CAMERA)
    public void requestCameraDenied(){
        Toast.makeText(MainActivity.this, "需要打开摄像头的权限才能使用，请到设置中设置", Toast.LENGTH_LONG).show();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_LOGIN_WALLET://从登陆界面返回的数据,我的钱包跳过去的
                if(resultCode==RESULT_OK){
                    String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
                    String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
                    getDataFromIntenetAndToMyWallet(uid, sid);
                }
                break;
            case REQUEST_CODE_LOGIN_ORDER://从订单跳过去的
                if(resultCode==RESULT_OK){
                    toDingDanListAcitivy(MyConstant.ALL_DING_DAN);
                }
                break;
            case REQUEST_CODE_LOGIN_SHIP_ADDRESS://从收货地址跳过去的
                if(resultCode==RESULT_OK){
                    Intent toShipAddressIntent=new Intent(this, EditShipAddressActivity.class);
                    startActivity(toShipAddressIntent);
                }
                break;
            case REQUEST_CODE_LOGIN_SHOU_CANG://从收藏跳过去的
                if(resultCode==RESULT_OK){
                    Intent toShouCangIntent=new Intent(this,ShouCangListActivity.class);
                    startActivity(toShouCangIntent);
                }
                break;
            case REQUEST_CODE_LOGIN_GUAN_ZHU://从关注跳过去的
                if(resultCode==RESULT_OK){
                    Intent toGuanZhuIntent=new Intent(this,GuanZhuListActivity.class);
                    startActivity(toGuanZhuIntent);
                }
                break;
            case REQUEST_CODE_LOGIN_CHANGE_PASSWORD://从修改密码跳转过去的
                if(resultCode==RESULT_OK){
                    Intent changPassWordIntent=new Intent(this, ChangPasswordPersonalCenterActivity.class);
                    startActivity(changPassWordIntent);
                }
                break;
            case REQUEST_CODE_LOGIN_SETTING://从设置跳转过去的
                if(resultCode==RESULT_OK){
                    String uidSetting=sharedPreferences.getString(MyConstant.UID_KEY,null);
                    String sidSetting=sharedPreferences.getString(MyConstant.SID_KEY,null);
                    getDataFromIntenetAndToSetting(uidSetting, sidSetting);
                }
                break;
            case SCAN_CODE:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("scan_result");
                    String id=FormatHelper.getIdFromUrl(result);
                    if(id!=null){
                        if("A".equals(id.charAt(0)+"")){  //商品
                            String goodId=id.substring(1);
                            MyLog.d(tag,"goodId="+goodId);
                            Good good=new Good();
                            good.setGoodId(goodId);
                            Intent toGoodIntent=new Intent(this, ProductDetailActivity.class);
                            toGoodIntent.putExtra(MyConstant.GOOD_KEY,good);
                            startActivity(toGoodIntent);
                        }else if("B".equals(id.charAt(0)+"")){    //商店
                            String shopId=id.substring(1);
                            MyLog.d(tag,"shopId="+shopId);
                            Intent toShopHomeIntent=new Intent(this, ShopHomeActivity.class);
                            toShopHomeIntent.putExtra(MyConstant.SHOP_USER_ID_KEY,shopId);
                            startActivity(toShopHomeIntent);
                        }
                    }
                    /*Intent toSaoMiaoResultIntent=new Intent(this, SaoMiaoResultActivity.class);
                    toSaoMiaoResultIntent.putExtra(MyConstant.SAO_MIAO_RESULT_KEY,result);
                    startActivity(toSaoMiaoResultIntent);*/
                } else if (resultCode == RESULT_CANCELED) {
                    //Toast.makeText(this,"扫描出错",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_TO_SETTING://从设置界面回来的
                if(resultCode==RESULT_OK){
                    loginImage.setImageResource(R.mipmap.tou_xiang);
                    nameTextView.setText(R.string.click_tou_xiang_login);
                    jiFenAndHelpRelativeLayout.setVisibility(View.GONE);
                    jiFenTextView.setText("");
                    loginButton.setImageResource(R.mipmap.tou_xiang);
                    youHuiQuanCountTextView.setText("");
                    isLogined=false;
                }
                break;
            case REQUEST_CODE_SHOPPING_CAR://跳转到购物车碎片
                if(resultCode==RESULT_OK){
                    shoppingCarClickChuLi();
                }
                break;
            case REQUEST_CODE_TO_PERSONAL_CENTER://跳转到个人中心
                if(resultCode==RESULT_OK){
                    toPersonalCenter();
                }
                break;
            case REQUEST_CODE_LOGIN_LIU_LAN_JI_LU://浏览记录
                if(resultCode== Activity.RESULT_OK){
                    liuLanJiLuClickChuLi();
                    MyLog.d(tag, "浏览记录回来了吗");
                }
                break;
            case REQUEST_CODE_YOU_HUI_QUAN://优惠券
                if(resultCode== Activity.RESULT_OK){
                    youHuiQuanClickChuLi();
                    //MyLog.d(tag, "浏览记录回来了吗");
                }
                break;

        }
    }




}
