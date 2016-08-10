package com.example.asus_cp.dongmanbuy.activity.dian_pu_jie;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.example.asus_cp.dongmanbuy.activity.map_activity_my.ShopStreerMapActivity;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 店铺详情的界面
 * Created by asus-cp on 2016-06-08.
 */
public class ShopDetailActivity extends Activity implements View.OnClickListener{

    private static final int SCAN_CODE = 1;
    private String tag="ShopDetailActivity";

    private ImageView daoHangImagView;//导航
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


    private String guanZhuUrl="http://www.zmobuy.com/PHP/?url=/store/addcollect";//关注的接口
    private String erWeiMaUrl ="http://api.zmobuy.com/JK/base/model.php";//二维码的接口
    private String guanZhuListUrl="http://www.zmobuy.com/PHP/?url=/user/storelist";//获取关注列表的数据

    private RequestQueue requestQueue;

    private ImageLoadHelper imageLoadHelper;

    private LayoutInflater inflater;
    private View erWeiMaView;
    private ImageView erWeiMaImageView;
    private PopupWindow erWeiMaWindow;
    private View parentView;

    private AlertDialog loginDialog;//登陆的对话框



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shop_detail_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {

        requestQueue= MyApplication.getRequestQueue();
        imageLoadHelper=new ImageLoadHelper();
        inflater=LayoutInflater.from(this);

        daoHangImagView= (ImageView) findViewById(R.id.img_dao_hang_shop_detail);
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

        shopModel=getIntent().getParcelableExtra(MyConstant.SHOP_MODEL_KEY);
        if(shopModel!=null){
            helper=new ImageLoadHelper();
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


            //给控件设置点击事件
            daoHangImagView.setOnClickListener(this);
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
            erWeiMaView=inflater.inflate(R.layout.er_wei_ma_tan_chu,null);
            erWeiMaImageView= (ImageView) erWeiMaView.findViewById(R.id.img_er_wei_ma);
            parentView=inflater.inflate(R.layout.shop_detail_activity_layout,null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_dao_hang_shop_detail://点击了导航按钮
                finish();
                break;
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
                Toast.makeText(this,"点击了在线客服",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_shop_er_wei_ma://点击了二维码
                /*Intent saoYiSaoIntent = new Intent(ShopDetailActivity.this, MipcaActivityCapture.class);
                startActivityForResult(saoYiSaoIntent, SCAN_CODE);*/
                erWeiMaClickChuLi();
                break;
            case R.id.re_layout_shang_jia_phone://点击了商家电话
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+
                    shangJiaPhoneTextView.getText().toString()));
                startActivity(callIntent);
                break;
            case R.id.re_layout_suo_zai_di_qu_map://点击了所在地区
                Intent mapIntent=new Intent(this, ShopStreerMapActivity.class);
                mapIntent.putExtra(MyConstant.SHOP_MODEL_KEY,shopModel);
                startActivity(mapIntent);
                break;
        }
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
        //Toast.makeText(this, "点击了关注按钮", Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, "点击了关注", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,
                Context.MODE_APPEND);
        final String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        final String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
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
            AlertDialog.Builder builder=new AlertDialog.Builder(ShopDetailActivity.this);
            builder.setMessage("请登录后关注该店铺");
            builder.setPositiveButton("立即登陆", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ShopDetailActivity.this, LoginActivity.class);
                    intent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY, "guanZhu");
                    startActivity(intent);
                    loginDialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loginDialog.dismiss();
                }
            });
            loginDialog=builder.show();
            loginDialog.show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SCAN_CODE:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("scan_result");
                    String id= FormatHelper.getIdFromUrl(result);
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
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this,"扫描出错",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    /**
     * 给关注textview设置初始颜色和背景
     * @param guanZhuTextView
     */
    private void setGuanZhuTextViewFirstValue(final TextView guanZhuTextView, final ShopModel shopModel) {
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, Context.MODE_APPEND);
        final String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        final String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
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
