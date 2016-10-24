package com.example.asus_cp.dongmanbuy.activity.dian_pu_jie;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
//import com.example.asus_cp.dongmanbuy.util.MyIMHelper;
import com.example.asus_cp.dongmanbuy.util.MyLog;
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


/**
 * 店铺详情的界面
 * Created by asus-cp on 2016-06-08.
 */
public class ShopDetailActivity extends BaseActivity implements View.OnClickListener{

    private String tag="ShopDetailActivity";

    private ImageView logoImageView;//logo
    private TextView shopNameTextView;//店铺名字
    private TextView guanZhuRenShuTextView;//关注人数
    private TextView guanZhuTextView;//关注
    private TextView productScoreTextView;//商品分数
    private TextView fuWuScoreTextView;//服务分数
    private TextView shiXiaoScoreTextView;//时效分数
    private TextView productPingJiTextView;//商品评级
    private TextView fuWuPingJiTextView;//服务评级
    private TextView shiXiaoPingJiTextView;//时效评级
    private TextView allProductTextView;//所有商品
    private TextView newProductTextView;//新商品
    private TextView cuXiaoProductTextView;//促销商品
    private LinearLayout allProductLinearLayout;//所有商品
    private LinearLayout newProductLinearLayout;//新商品
    private LinearLayout cuXiaoProductLinearLayout;//促销商品
    private RelativeLayout keFuRelalltiveLayout;//客服
    private RelativeLayout erWeiMaRelativeLayout;//二维码
    private RelativeLayout shangJiaPhoneRelativeLayout;//商家电话
    private TextView shangJiaPhoneTextView;//商家电话
    private TextView shopDescTextView;//店铺简介
    private TextView gongSiNameTextView;//公司名字
    private TextView kaiDianTimeTextView;//开店时间
    private TextView suoZaiAreaTextView;//所在地区
    private RelativeLayout suoZaiDiQuRelativeLayout;//所在地区

    private ShopModel shopModel;

    private ImageLoadHelper helper;
    //private MyIMHelper myIMHelper;

    private String guanZhuUrl="http://www.zmobuy.com/PHP/?url=/store/addcollect";//关注的接口
    private String erWeiMaUrl ="http://api.zmobuy.com/JK/base/model.php";//二维码的接口
    private String guanZhuListUrl="http://www.zmobuy.com/PHP/?url=/user/storelist";//获取关注列表的数据

    private ImageLoadHelper imageLoadHelper;

    private LayoutInflater inflater;
    private View erWeiMaView;
    private ImageView erWeiMaImageView;
    private ImageView erWeiMaCloseImageView;
    private PopupWindow erWeiMaWindow;
    private View parentView;

    private AlertDialog loginDialog;//登陆的对话框

    private static final int REQUEST_LOGIN = 1;

    public static final int PERMISSION_CALL=1;//申请打电话的运行时权限



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.shop_detail);
        setContentLayout(R.layout.shop_detail_activity_layout);
        initView();
        init();
    }

    @Override
    public void initView() {
        logoImageView= (ImageView) findViewById(R.id.img_shop_logo_shop_detail);
        shopNameTextView= (TextView) findViewById(R.id.text_shop_name_shop_detail);
        guanZhuRenShuTextView= (TextView) findViewById(R.id.text_guan_zhu_ren_shu_shop_detail);
        guanZhuTextView= (TextView) findViewById(R.id.text_guan_zhu_shop_detail);
        productScoreTextView= (TextView) findViewById(R.id.text_product_score_shop_detail);
        fuWuScoreTextView= (TextView) findViewById(R.id.text_fu_wu_score_shop_detail);
        shiXiaoScoreTextView= (TextView) findViewById(R.id.text_shi_xiao_score_shop_detail);
        productPingJiTextView= (TextView) findViewById(R.id.text_product_ping_ji_shop_detail);
        fuWuPingJiTextView= (TextView) findViewById(R.id.text_fu_wu_ping_ji_shop_detail);
        shiXiaoPingJiTextView= (TextView) findViewById(R.id.text_shi_xiao_ping_ji_shop_detail);
        allProductTextView= (TextView) findViewById(R.id.text_all_product_count_shop_detail);
        newProductTextView= (TextView) findViewById(R.id.text_new_product_count_shop_detail);
        cuXiaoProductTextView= (TextView) findViewById(R.id.text_cu_xiao_product_count_shop_detail);
        allProductLinearLayout= (LinearLayout) findViewById(R.id.ll_all_product_shop_detail);
        newProductLinearLayout= (LinearLayout) findViewById(R.id.ll_new_product_shop_detail);
        cuXiaoProductLinearLayout= (LinearLayout) findViewById(R.id.ll_cu_xiao_product_shop_detail);
        keFuRelalltiveLayout = (RelativeLayout) findViewById(R.id.re_layout_ke_fu);
        erWeiMaRelativeLayout = (RelativeLayout) findViewById(R.id.re_layout_shop_er_wei_ma);
        shangJiaPhoneRelativeLayout = (RelativeLayout) findViewById(R.id.re_layout_shang_jia_phone);
        shangJiaPhoneTextView = (TextView) findViewById(R.id.text_shang_jia_phone_shop_detail);
        shopDescTextView= (TextView) findViewById(R.id.text_shop_desc);
        gongSiNameTextView= (TextView) findViewById(R.id.text_gong_si_name);
        kaiDianTimeTextView= (TextView) findViewById(R.id.text_kai_dian_time);
        suoZaiAreaTextView= (TextView) findViewById(R.id.text_suo_zai_di_qu_shop_detail);
        suoZaiDiQuRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_suo_zai_di_qu_map);


        //给控件设置点击事件
        logoImageView.setOnClickListener(this);
        shopNameTextView.setOnClickListener(this);
        guanZhuTextView.setOnClickListener(this);
        allProductLinearLayout.setOnClickListener(this);
        newProductLinearLayout.setOnClickListener(this);
        cuXiaoProductLinearLayout.setOnClickListener(this);
        keFuRelalltiveLayout.setOnClickListener(this);
        erWeiMaRelativeLayout.setOnClickListener(this);
        shangJiaPhoneRelativeLayout.setOnClickListener(this);
        suoZaiDiQuRelativeLayout.setOnClickListener(this);


        //初始化二维码弹出窗口的view
        inflater=LayoutInflater.from(this);
        erWeiMaView=inflater.inflate(R.layout.er_wei_ma_tan_chu, null);
        erWeiMaImageView= (ImageView) erWeiMaView.findViewById(R.id.img_er_wei_ma);
        erWeiMaCloseImageView= (ImageView) erWeiMaView.findViewById(R.id.img_close_er_wei_ma);
        parentView=inflater.inflate(R.layout.shop_detail_activity_layout,null);

        erWeiMaCloseImageView.setOnClickListener(this);
    }

    /**
     * 初始化的方法
     */
    private void init() {

        imageLoadHelper=new ImageLoadHelper();

        shopModel=getIntent().getParcelableExtra(MyConstant.SHOP_MODEL_KEY);
        if(shopModel!=null){
            helper=new ImageLoadHelper();
            logoImageView.setTag(shopModel.getShopLogo());
            ImageLoader imageLoader=helper.getImageLoader();
            ImageLoader.ImageListener listener=imageLoader.getImageListener(logoImageView,R.mipmap.yu_jia_zai,
                    R.mipmap.yu_jia_zai);
            imageLoader.get(shopModel.getShopLogo(), listener, 200, 200);

            //给控件设置值
            shopNameTextView.setText(shopModel.getShopName());
            guanZhuRenShuTextView.setText("已经有"+shopModel.getGazeNumber()+"人关注");
            productScoreTextView.setText(shopModel.getCommenTrankScore());
            fuWuScoreTextView.setText(shopModel.getCommentServerScore());
            shiXiaoScoreTextView.setText(shopModel.getCommentDeliveryScore());
            productPingJiTextView.setText(shopModel.getCommenTrank());
            fuWuPingJiTextView.setText(shopModel.getCommentServer());
            shiXiaoPingJiTextView.setText(shopModel.getCommentDelivery());

            allProductTextView.setText(shopModel.getAllGoodsCount());
            newProductTextView.setText(shopModel.getNewGoodCount());
            cuXiaoProductTextView.setText(shopModel.getCuXiaoGoodCount());

            shangJiaPhoneTextView.setText(shopModel.getShopTel());
            shopDescTextView.setText(shopModel.getShopDesc());
            gongSiNameTextView.setText(shopModel.getShopName());
            kaiDianTimeTextView.setText(shopModel.getShopStartTime());
            suoZaiAreaTextView.setText(shopModel.getShopAddress());

            //给关注textview设置颜色和值
            setGuanZhuTextViewFirstValue(guanZhuTextView,shopModel);

            //实例化Myimhelper
            //myIMHelper=new MyIMHelper();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_shop_logo_shop_detail://点击了店铺logo
                toShopHomeActivity();
                break;
            case R.id.text_shop_name_shop_detail://点击了店铺名称
                toShopHomeActivity();
                break;
            case R.id.text_guan_zhu_shop_detail://点击了关注
                guanZhuClickChuLi();
                break;
            case R.id.ll_all_product_shop_detail://点击了全部商品
                toShopGoodSortActivity("");
                break;
            case R.id.ll_new_product_shop_detail://点击了新商品
                toShopGoodSortActivity("is_new");
                break;
            case R.id.ll_cu_xiao_product_shop_detail://点击了促销商品
                toShopGoodSortActivity("is_promote");
                break;
            case R.id.re_layout_ke_fu://点击了在线客服
                //Toast.makeText(this,"点击了在线客服",Toast.LENGTH_SHORT).show();
                //keFuClickChuLi();
                //myIMHelper.openKeFuLiaoTianAndSendMessage("");
                break;
            case R.id.re_layout_shop_er_wei_ma://点击了二维码
                /*Intent saoYiSaoIntent = new Intent(ShopDetailActivity.this, MipcaActivityCapture.class);
                startActivityForResult(saoYiSaoIntent, REQUEST_LOGIN);*/
                erWeiMaClickChuLi();
                break;
            case R.id.re_layout_shang_jia_phone://点击了商家电话
                MPermissions.requestPermissions(this,PERMISSION_CALL, Manifest.permission.CALL_PHONE);
                //callSeller();
                break;
            case R.id.re_layout_suo_zai_di_qu_map://点击了所在地区
                if(isInstallByread("com.baidu.BaiduMap")){
                    startBaiDuDiTu();
                }else if(isInstallByread("com.autonavi.minimap")){
                    startGaoDeDiTu();
                }else{
                    Toast.makeText(ShopDetailActivity.this,"您的手机上没有安装地图应用,请先下载一个地图应用",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_close_er_wei_ma://关闭二维码
                erWeiMaWindow.dismiss();
                break;

        }
    }


    /**
     * 给商家打电话
     */
    private void callSeller() {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                shangJiaPhoneTextView.getText().toString()));
        startActivity(callIntent);
    }


    /**
     * 客服的点击事件处理
     */
    private void keFuClickChuLi() {
        /*//开始登录
        String userid = "zmobuy1";
        String password = "123456";
        final YWIMKit mIMKit= MyYWIMKitHelper.getYwimkit(userid);//需要userId才能得到这个
        IYWLoginService loginService = mIMKit.getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
        loginService.login(loginParam, new IWxCallback() {
            @Override
            public void onSuccess(Object... arg0) {
                MyLog.d(tag,"登陆成功了");
                //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
                EServiceContact contact = new EServiceContact("动漫卡哇伊周小沫",161017570);
                //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
                //的setNeedByPass方法，参数为false。
                //contact.setNeedByPass(false);
                Intent intent = mIMKit.getChattingActivityIntent(contact);
                startActivity(intent);
            }

            @Override
            public void onProgress(int arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int errCode, String description) {
                //如果登录失败，errCode为错误码,description是错误的具体描述信息
            }
        });*/
    }


    /**
     * 开启百度地图
     */
    private void startBaiDuDiTu() {
        Intent intent = null;
        try {
            intent = Intent.getIntent("intent://map/geocoder?address="+shopModel.getShopAddress()+"&src=thirdapp.geo.yourCompanyName.zmobuy#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            startActivity(intent); //启动调用
            //intent = Intent.getIntent("intent://map/marker?location="+weiDu+","+jingDu+"&title="+shopModel.getShopName()+"&content="+shopModel.getShopAddress()+"&src=thirdapp.marker.yourCompanyName.yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 开启高德地图
     */
    private void startGaoDeDiTu() {
//        Intent intent = new Intent("android.intent.action.VIEW",
//                android.net.Uri.parse("androidamap://viewMap?sourceApplication=zmobuy&poiname="+shopModel.getShopName()+"&lat="+weiDu+"&lon="+jingDu+"&dev=0"));

        try{
            Intent intent = new Intent("android.intent.action.VIEW",
                    android.net.Uri.parse("androidamap://viewGeo?sourceApplication=zmobuy&addr="+shopModel.getShopAddress()+""));
            intent.setPackage("com.autonavi.minimap");
            startActivity(intent); //启动调用
        }catch (Exception e){
            e.printStackTrace();
        }

    }





    /**
     * 检测手机中是否安装了其他安装包
     */
    private boolean isInstallByread(String packageName) {
        //return new File("/data/data/" + packageName).exists();
        //获取packagemanager
        final PackageManager packageManager = this.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    /**
     * 二维码点击事件处理
     */
    private void erWeiMaClickChuLi() {
        StringRequest erWeiRequest=new StringRequest(Request.Method.POST, erWeiMaUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag,"二维码返回的数据是："+s);
                        s=FormatHelper.removeBom(s);
                        String temp=s.replace(" ","");
                        erWeiMaImageView.setTag(temp);
                        ImageLoader imageLoader=imageLoadHelper.getImageLoader();
                        ImageLoader.ImageListener listener=imageLoader.getImageListener(erWeiMaImageView,
                                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
                        imageLoader.get(temp, listener,300,300);

                        erWeiMaWindow = new PopupWindow(erWeiMaView,
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        //外部点击时可以消失
                        erWeiMaWindow.setBackgroundDrawable(new ColorDrawable());
                        //erWeiMaWindow.setFocusable(true);
                        erWeiMaWindow.setOutsideTouchable(true);
                        erWeiMaWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);//最后才是show,顺序很重要
                        setBackgroundAlpha(0.5f);
                        erWeiMaWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                setBackgroundAlpha(1f);
                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                /*service	code
                ru_id	4150*/
                MyLog.d(tag,"ruId="+shopModel.getUserId());
                 Map<String,String> map=new HashMap<String,String>();
                map.put("service","code");
                map.put("ru_id",shopModel.getUserId());
                return map;
            }
        };
        requestQueue.add(erWeiRequest);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    /**
     * 跳转到店铺商品排序的界面
     */
    private void toShopGoodSortActivity(String goodType) {
        Intent allIntent=new Intent(this,ShopProdcutSortActivity.class);
        allIntent.putExtra(MyConstant.SHOP_USER_ID_KEY,
                shopModel.getUserId());
        allIntent.putExtra(MyConstant.GOOD_TYPE_KEY,goodType);
        startActivity(allIntent);
    }


    /**
     * 跳转到店铺主页
     */
    private void toShopHomeActivity() {
        finish();
    }



    /**
     * 关注点击事件的处理
     */
    private void guanZhuClickChuLi() {

        if(uid!=null && !uid.isEmpty()){
            StringRequest guanZhuRequest=new StringRequest(Request.Method.POST, guanZhuUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag, "关注接口返回的数据" + s);
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                String erroeDesc= JsonHelper.decodeUnicode(jsonObject1.getString("error_desc"));
                                if("已关注".equals(erroeDesc)){
                                    Toast.makeText(ShopDetailActivity.this,"关注成功",Toast.LENGTH_SHORT).show();
                                    guanZhuTextView.setTextColor(getResources().getColor(R.color.white_my));
                                    guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_successed_background);
                                    int guanZhuRenShu=Integer.parseInt(shopModel.getGazeNumber());
                                    guanZhuRenShuTextView.setText("已经有" + (1 + guanZhuRenShu) + "人关注");
                                    shopModel.setGazeNumber("" + (1 + guanZhuRenShu));
                                }else if("已取消关注".equals(erroeDesc)){
                                    Toast.makeText(ShopDetailActivity.this,"取消关注成功",Toast.LENGTH_SHORT).show();
                                    guanZhuTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                                    guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_background);
                                    int guanZhuRenShu=Integer.parseInt(shopModel.getGazeNumber());
                                    guanZhuRenShuTextView.setText("已经有" + (guanZhuRenShu - 1) + "人关注");
                                    shopModel.setGazeNumber("" + (guanZhuRenShu - 1));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<String,String>();
                    String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"shop_userid\":\""+shopModel.getUserId()+"\"}";
                    map.put("json",json);
                    return map;
                }
            };
            requestQueue.add(guanZhuRequest);
        }else{//没有记录就跳转到登陆界面

            Intent intent = new Intent(ShopDetailActivity.this, LoginActivity.class);
            intent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY, "guanZhu");
            startActivityForResult(intent, REQUEST_LOGIN);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this,requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(PERMISSION_CALL)
    public void requestCallPhoneSuccess(){
        callSeller();
    }

    @PermissionDenied(PERMISSION_CALL)
    public void requsetCallPhoneDenied(){
        Toast.makeText(this,"需要拨打电话的权限才能使用该功能，请到设置中设置",Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_LOGIN:
                if(resultCode==RESULT_OK){
                    /*SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
                    uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
                    sid=sharedPreferences.getString(MyConstant.SID_KEY,null);*/

                    //因为这个方法里面已经有了上面的3行代码
                    setGuanZhuTextViewFirstValue(guanZhuTextView,shopModel);
                }
                break;
        }
    }



    /**
     * 给关注textview设置初始颜色和背景
     * @param guanZhuTextView
     */
    private void setGuanZhuTextViewFirstValue(final TextView guanZhuTextView, final ShopModel shopModel) {
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);

        if(uid!=null && !uid.isEmpty()){
            StringRequest getGuanZhuListRequest=new StringRequest(Request.Method.POST, guanZhuListUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag, "返回的数据是：" + s);
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                JSONArray jsonArray=jsonObject1.getJSONArray("store_list");
                                if(jsonArray.length()==0){  //关注列表的长度为空，说明没有关注
                                    guanZhuTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                                    guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_background);
                                }else{
                                    int count=0;
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject ziJsObj=jsonArray.getJSONObject(i);
                                        String shopId=ziJsObj.getString("shop_id");
                                        if(shopId.equals(shopModel.getUserId())){   //店铺在店铺列表里面，说明已经关注过了
                                            guanZhuTextView.setTextColor(getResources().getColor(R.color.white_my));
                                            guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_successed_background);
                                            break;
                                        }
                                        count++;
                                    }
                                    if(count==jsonArray.length()){  //店铺不在店铺列表里面
                                        guanZhuTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                                        guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_background);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<String,String>();
                    String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"page\":\""+"1"+"\"}";
                    map.put("json",json);
                    return map;
                }
            };
            requestQueue.add(getGuanZhuListRequest);
        }else { //用户没有登陆
            guanZhuTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
            guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_background);
        }
    }

}
