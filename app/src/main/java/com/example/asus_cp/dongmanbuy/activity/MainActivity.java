package com.example.asus_cp.dongmanbuy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.example.asus_cp.dongmanbuy.activity.dian_pu_jie.ShopHomeActivity;
import com.example.asus_cp.dongmanbuy.activity.gou_wu.DingDanListActivity;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.GuanZhuListActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.PersonalCenterActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.ShouCangListActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.data_set.ChangPasswordPersonalCenterActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.data_set.DataSetActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.data_set.EditShipAddressActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager.FundManagerActivity;
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
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_CODE_SHOU_CANG_KEY = 9;
    private static final int REQUEST_CODE_SHOPPING_CAR =10 ;
    private static final int REQUEST_CODE_TO_LOGIN_ACTIVITY = 12;

    private String shoppingCarListUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/list";//购物车列表
    //private android.support.v7.widget.SearchView searchView;
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
    private de.hdodenhof.circleimageview.CircleImageView loginImage;//登录按钮
    private TextView myZhuYeTextView;//我的主页
    private TextView myOrderTextView;//我的订单
    private TextView myWalletTextView;//我的钱包
    private TextView myShouCangTextView;//我的收藏
    private TextView guanZhuDianPuTextView;//关注店铺
    private TextView shipAddressTextView;//收货地址
    private TextView changPassWordTextView;//修改密码
    private TextView setTingTextView;//设置
    private TextView nameTextView;//名字
    private TextView emailTextView;//邮箱

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

    private String userInfoUrl="http://www.zmobuy.com/PHP/?url=/user/info";//用户信息的接口

    private RequestQueue requestQueue;

    private View messageAndSaoYiSaoView;

    private PopupWindow messageAndSaoPopuWindow;

    private int messageAndSaoCount;//标记message和扫一扫的是显示还是隐藏

    public static final int SCAN_CODE = 1;//扫一扫的请求码

    private String passUid;//传递给碎片的uid
    private String passsid;

    private String labelFlag;//跳转到哪个标签的标记

    private int densty;//屏幕像素密度

    private ImageLoadHelper imageLoaderhelper;

    private long pressedTime;//按下返回键的时间


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//不要标题栏
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
        requestQueue= MyApplication.getRequestQueue();
        fragments=new ArrayList<Fragment>();
        Fragment homeFragment=new HomeFragment();
        Fragment categoryFragment=new CategoryFragment();
        Fragment findFragment=new FindFragment();
        Fragment shopStreetFragment=new ShopStreetFragment();
        Fragment shoppingCarFragment=new ShoppingCarFragment();
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
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        densty = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）

        imageLoaderhelper=new ImageLoadHelper();
        String uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        String sid=null;
        if(uid==null || uid.isEmpty()){

        }else {
            sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
            getDataFromIntenetAndSetNameAndEmailAndPic(uid,sid);
        }

    }

    public int getDensty(){
        return densty;
    }


    /**
     * 初始化view
     */
    private void initView() {
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
        homeFragment=new HomeFragment();
        categoryFragment=new CategoryFragment();
        findFragment=new FindFragment();
        shopStreetFragment=new ShopStreetFragment();
        shoppingCarFragment=new ShoppingCarFragment();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_main, homeFragment);
        fragmentTransaction.commit();


        //跳转到购物车碎片(从别的活动跳过来的时候,只有当活动在后台销毁的时候，这段代码才会调用)
        labelFlag=getIntent().getStringExtra(MyConstant.MAIN_ACTIVITY_LABLE_FALG_KEY);
        MyLog.d(tag, "labelFlag=" + labelFlag);
        if(MyConstant.SHOPPING_CAR_FLAG_KEY.equals(labelFlag)){
            MyLog.d(tag,"init中的方法执行了吗");
            FragmentTransaction shoppingTransaction=fragmentManager.beginTransaction();
            shoppingTransaction.replace(R.id.frame_layout_main, shoppingCarFragment);
            shoppingTransaction.commit();
            resetLabel();
            shoppingCarImg.setImageResource(R.mipmap.home_selected);
            shoppingCarText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }else if(MyConstant.SHOPPING_STRRET_FLAG_KEY.equals(labelFlag)){
            FragmentTransaction shopStreetTransaction=fragmentManager.beginTransaction();
            shopStreetTransaction.replace(R.id.frame_layout_main,shopStreetFragment);
            shopStreetTransaction.commit();
            resetLabel();
            shopStreetImg.setImageResource(R.mipmap.shopstreet_selected);
            shopStreetText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }else if(MyConstant.HOME_FLAG_KEY.equals(labelFlag)){
            FragmentTransaction homeTransaction=fragmentManager.beginTransaction();
            homeTransaction.replace(R.id.frame_layout_main,homeFragment);
            homeTransaction.commit();
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


        //使用侧滑菜单
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);//这个是调节阴影宽度的
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.layout_menu);


        //给侧滑菜单设置滚动的监听事件
        menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                String uid = sharedPreferences.getString(MyConstant.UID_KEY, null);
                String sid = sharedPreferences.getString(MyConstant.SID_KEY, null);
                if (uid != null && !uid.isEmpty()) {
                    getDataFromIntenetAndSetNameAndEmailAndPic(uid, sid);
                } else {
                    nameTextView.setText("");
                    emailTextView.setText("");
                    loginImage.setImageResource(R.mipmap.yu_jia_zai);
                    loginButton.setImageResource(R.mipmap.yu_jia_zai);
                }
            }
        });


        //-----------------------初始化侧滑菜单-----------------------------
        loginImage= (CircleImageView) findViewById(R.id.img_login);
        myZhuYeTextView= (TextView) findViewById(R.id.text_my_zhu_ye);
        myOrderTextView= (TextView) findViewById(R.id.text_my_order);
        myWalletTextView= (TextView) findViewById(R.id.text_my_wallet);
        myShouCangTextView= (TextView) findViewById(R.id.text_my_shou_cang);
        guanZhuDianPuTextView= (TextView) findViewById(R.id.text_guan_zhu_dian_pu);
        shipAddressTextView= (TextView) findViewById(R.id.text_ship_addres);
        changPassWordTextView= (TextView) findViewById(R.id.text_change_password);
        setTingTextView= (TextView) findViewById(R.id.text_setting);
        nameTextView= (TextView) findViewById(R.id.text_name_slid_menu);
        emailTextView= (TextView) findViewById(R.id.text_email_slid_menu);

        loginImage.setOnClickListener(this);
        myZhuYeTextView.setOnClickListener(this);
        myOrderTextView.setOnClickListener(this);
        myWalletTextView.setOnClickListener(this);
        myShouCangTextView.setOnClickListener(this);
        guanZhuDianPuTextView.setOnClickListener(this);
        shipAddressTextView.setOnClickListener(this);
        changPassWordTextView.setOnClickListener(this);
        setTingTextView.setOnClickListener(this);


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
        MyLog.d(tag, "onResume");
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
            MyLog.d(tag,"init中的方法执行了吗");
            FragmentTransaction shoppingTransaction=fragmentManager.beginTransaction();
            shoppingTransaction.replace(R.id.frame_layout_main, shoppingCarFragment);
            shoppingTransaction.commit();
            resetLabel();
            shoppingCarImg.setImageResource(R.mipmap.shoppingcar_selected);
            shoppingCarText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }else if(MyConstant.SHOPPING_STRRET_FLAG_KEY.equals(labelFlag)){
            FragmentTransaction shopStreetTransaction=fragmentManager.beginTransaction();
            shopStreetTransaction.replace(R.id.frame_layout_main,shopStreetFragment);
            shopStreetTransaction.commit();
            resetLabel();
            shopStreetImg.setImageResource(R.mipmap.shopstreet_selected);
            shopStreetText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }else if(MyConstant.HOME_FLAG_KEY.equals(labelFlag)){
            FragmentTransaction homeTransaction=fragmentManager.beginTransaction();
            homeTransaction.replace(R.id.frame_layout_main,homeFragment);
            homeTransaction.commit();
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
                menu.toggle();
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
                FragmentTransaction homeTransaction=fragmentManager.beginTransaction();
                homeTransaction.replace(R.id.frame_layout_main,homeFragment);
                homeTransaction.commit();
                resetLabel();
                homeImg.setImageResource(R.mipmap.home_selected);
                homeText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case R.id.ll_category:
                //viewPager.setCurrentItem(CATEGORY);
                FragmentTransaction categoryTransaction=fragmentManager.beginTransaction();
                categoryTransaction.replace(R.id.frame_layout_main,categoryFragment);
                categoryTransaction.commit();
                resetLabel();
                categoryImg.setImageResource(R.mipmap.category_selected);
                categoryText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case R.id.ll_find:
                //viewPager.setCurrentItem(FIND);
                FragmentTransaction findTransaction=fragmentManager.beginTransaction();
                findTransaction.replace(R.id.frame_layout_main,findFragment);
                findTransaction.commit();
                resetLabel();
                findImg.setImageResource(R.mipmap.find_selected);
                findText.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case R.id.ll_shop_street:
                //viewPager.setCurrentItem(SHOP_STREET);
                FragmentTransaction shopStreetTransaction=fragmentManager.beginTransaction();
                shopStreetTransaction.replace(R.id.frame_layout_main,shopStreetFragment);
                shopStreetTransaction.commit();
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
                Intent intent = new Intent(MainActivity.this, MipcaActivityCapture.class);
                startActivityForResult(intent, SCAN_CODE);
                break;

            case R.id.img_login://登陆
                toPersonalCenter();
                break;
            case R.id.text_my_zhu_ye://我的主页
                toPersonalCenter();
                break;
            case R.id.text_my_order://我的订单
                myOrderClickChuLi();
                break;
            case R.id.text_my_wallet://我的钱包
                myWalletClickChuLi();
                break;
            case R.id.text_my_shou_cang://我的收藏
                shouCangClickChuLi();
                break;
            case R.id.text_guan_zhu_dian_pu://我的关注
                myGuanZhuClickChuLi();
                break;
            case R.id.text_ship_addres://收获地址
                shipAddressClickChuLi();
                break;
            case R.id.text_change_password://修改密码
                changePasswordClickChuLi();
                break;
            case R.id.text_setting://设置
                setClickChuLi();
                break;
            case R.id.img_search_main://点击了search按钮
                Intent toSearchIntent=new Intent(this, SearchActivity.class);
                startActivity(toSearchIntent);
                break;
        }
    }


    /**
     * 消息和扫一扫的点击事件处理
     */
    private void messageAndSaoClickChuLi() {
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
            startActivityForResult(toLoginIntent,REQUEST_CODE_SHOPPING_CAR);
        }else {
            FragmentTransaction shoppingTransaction=fragmentManager.beginTransaction();
            shoppingTransaction.replace(R.id.frame_layout_main, shoppingCarFragment);
            shoppingTransaction.commitAllowingStateLoss();
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
                        User user=parseJson(s);
                        nameTextView.setText(user.getName());
                        emailTextView.setText(user.getEmail());
                        ImageLoader imageLoader1=imageLoaderhelper.getImageLoader();
                        ImageLoader.ImageListener listener=imageLoader1.getImageListener(loginImage,R.mipmap.yu_jia_zai,
                                R.mipmap.yu_jia_zai);
                        imageLoader1.get(MyConstant.YU_MING + user.getPic(), listener, 200, 200);
                        ImageLoader imageLoader2=imageLoaderhelper.getImageLoader();
                        ImageLoader.ImageListener listener1=imageLoader2.getImageListener(loginButton,R.mipmap.yu_jia_zai,
                                R.mipmap.yu_jia_zai);
                        imageLoader2.get(MyConstant.YU_MING + user.getPic(), listener1, 200, 200);
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
                        settingIntent.putExtra(MyConstant.USER_KEY,user);
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
            if(menu.isMenuShowing()){
                menu.toggle();
            }
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

        if (menu.isMenuShowing()) {
            menu.showContent();
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
                    Toast.makeText(this,"扫描出错",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_TO_SETTING://从设置界面回来的
                if(resultCode==RESULT_OK){
                    loginImage.setImageResource(R.mipmap.yu_jia_zai);
                    nameTextView.setText("");
                    emailTextView.setText("");
                    loginButton.setImageResource(R.mipmap.yu_jia_zai);
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

        }
    }




}
