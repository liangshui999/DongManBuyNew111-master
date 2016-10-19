package com.example.asus_cp.dongmanbuy.activity.dian_pu_jie;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.adapter.ShopHomeHotProductAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺主页的界面
 * Created by asus-cp on 2016-06-07.
 */
public class ShopHomeActivity extends BaseActivity implements View.OnClickListener {

    private String tag = "ShopHomeActivity";

    private ImageView daoHangImagView;//导航
    private ImageView searchImageView;//搜索框
    private TextView categoryTextView;//分类
    private ImageView logoImageView;//logo
    private TextView shopNameTextView;//店铺名称
    private TextView guanZhuRenShuTextView;//关注人数
    private TextView guanZhuTextView;//关注
    private TextView allProductsTextView;//全部商品
    private TextView newProductTextView;//新商品
    private TextView tuijianProductTextView;//推荐商品
    private LinearLayout allProductLinearLayout;//全部商品
    private LinearLayout newProductLinearLayout;//新商品
    private LinearLayout tuiJianProductLinearLayout;//推荐商品
    private MyGridViewA hotProductGridView;//热门商品的网格
    private TextView seeMoreTextView;//查看更多
    private TextView shopDetailTextView;//店铺详情
    private LinearLayout hotCategoryLinearLayout;//热门分类
    private LinearLayout keFuLinearLayout;//客服

    private ScrollView shopHomeScrollView;

    private String shopUserId;//店铺所有者的id

    private String shopInfoUrl = "http://www.zmobuy.com/PHP/?url=/store/shopinfo";//店铺详细信息的接口

    private String guanZhuUrl="http://www.zmobuy.com/PHP/?url=/store/addcollect";//关注的接口

    private String guanZhuListUrl="http://www.zmobuy.com/PHP/?url=/user/storelist";//获取关注列表的数据

    private ImageLoadHelper helper;

    private ShopModel shopModel;

    private AlertDialog loginDialog;//登陆的对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_home_activity_layout);
        init();
    }

    @Override
    public void initView() {
        daoHangImagView = (ImageView) findViewById(R.id.img_shop_home_dao_hang);
        searchImageView = (ImageView) findViewById(R.id.img_search_shop_home);
        categoryTextView = (TextView) findViewById(R.id.text_category_shop_home);
        logoImageView = (ImageView) findViewById(R.id.img_shop_logo_home);
        shopNameTextView = (TextView) findViewById(R.id.text_shop_name_home);
        guanZhuRenShuTextView = (TextView) findViewById(R.id.text_guan_zhu_ren_shu_home);
        guanZhuTextView = (TextView) findViewById(R.id.text_guan_zhu_home);
        allProductsTextView = (TextView) findViewById(R.id.text_all_product_count);
        newProductTextView = (TextView) findViewById(R.id.text_new_product_count);
        tuijianProductTextView = (TextView) findViewById(R.id.text_tui_jian_product_count);
        allProductLinearLayout = (LinearLayout) findViewById(R.id.ll_all_product);
        newProductLinearLayout = (LinearLayout) findViewById(R.id.ll_new_product);
        tuiJianProductLinearLayout = (LinearLayout) findViewById(R.id.ll_tui_jian_product);
        hotProductGridView = (MyGridViewA) findViewById(R.id.grid_view_hot_product);
        seeMoreTextView = (TextView) findViewById(R.id.text_see_more);
        shopDetailTextView = (TextView) findViewById(R.id.text_shop_detail);
        hotCategoryLinearLayout = (LinearLayout) findViewById(R.id.ll_hot_category);
        keFuLinearLayout = (LinearLayout) findViewById(R.id.ll_ke_fu_shop_home);
        shopHomeScrollView= (ScrollView) findViewById(R.id.scrollView_shop_home);


        //给view设置点击事件
        daoHangImagView.setOnClickListener(this);
        searchImageView.setOnClickListener(this);
        categoryTextView.setOnClickListener(this);
        guanZhuTextView.setOnClickListener(this);
        allProductLinearLayout.setOnClickListener(this);
        newProductLinearLayout.setOnClickListener(this);
        tuiJianProductLinearLayout.setOnClickListener(this);
        shopDetailTextView.setOnClickListener(this);
        hotCategoryLinearLayout.setOnClickListener(this);
        keFuLinearLayout.setOnClickListener(this);
        seeMoreTextView.setOnClickListener(this);
    }

    /**
     * 初始化的方法
     */
    private void init() {
        shopUserId = getIntent().getStringExtra(MyConstant.SHOP_USER_ID_KEY);
        helper=new ImageLoadHelper();
        initView();
        getDataFromIntenetAndSetView();
    }


    /**
     * 从网络获取数据并且给view赋值
     */
    private void getDataFromIntenetAndSetView() {
        DialogHelper.showDialog(this);
        StringRequest getShopInfoRequest = new StringRequest(Request.Method.POST, shopInfoUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DialogHelper.dissmisDialog();
                MyLog.d(tag, "返回的数据是：" + s);
                shopModel=parseJson(s);
                logoImageView.setTag(shopModel.getShopLogo());
                ImageLoader imageLoader=helper.getImageLoader();
                ImageLoader.ImageListener listener=imageLoader.getImageListener(logoImageView,
                        R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
                imageLoader.get(shopModel.getShopLogo(), listener, 200, 200);

                //给view赋值
                shopNameTextView.setText(shopModel.getShopName());
                guanZhuRenShuTextView.setText("已经有" + shopModel.getGazeNumber() + "人关注");
                allProductsTextView.setText(shopModel.getAllGoodsCount());
                newProductTextView.setText(shopModel.getNewGoodCount());
                tuijianProductTextView.setText(shopModel.getTuiJianGoodCount());

                //给关注textview设置初始值
                setGuanZhuTextViewFirstValue(guanZhuTextView,shopModel);

                List<Good> goods=shopModel.getGoods();
                MyLog.d(tag, "商品的数量：" + goods.size());
                ShopHomeHotProductAdapter adapter=new ShopHomeHotProductAdapter(ShopHomeActivity.this,
                        goods);
                hotProductGridView.setAdapter(adapter);
                hotProductGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(ShopHomeActivity.this,"点击的位置是"+position,Toast.LENGTH_SHORT).show();
                        Intent toDetailActivityIntent=new Intent(ShopHomeActivity.this,ProductDetailActivity.class);
                        toDetailActivityIntent.putExtra(MyConstant.GOOD_KEY,shopModel.getGoods().get(position));
                        startActivity(toDetailActivityIntent);
                    }
                });
//                CategoryImageLoadHelper.setGridViewHeightBasedOnChildren(hotProductGridView);
//                shopHomeScrollView.invalidate();//通知scrollview重绘

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String json = "{\"id\":\"" + shopUserId + "\"}";
                map.put("json", json);
                return map;
            }
        };
        requestQueue.add(getShopInfoRequest);
    }


    /**
     * 解析json数据
     * @param s
     */
    private ShopModel parseJson(String s) {
        ShopModel shopModel = new ShopModel();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            shopModel.setAllGoodsCount(jsonObject1.getString("count_goods"));
            shopModel.setCuXiaoGoodCount(jsonObject1.getString("count_goods_promote"));
            shopModel.setTuiJianGoodCount(jsonObject1.getString("count_bonus"));
            shopModel.setNewGoodCount(jsonObject1.getString("count_goods_new"));
            shopModel.setShopId(jsonObject1.getString("shop_id"));
            shopModel.setUserId(jsonObject1.getString("ru_id"));
            shopModel.setShopLogo(jsonObject1.getString("brand_thumb"));
            shopModel.setGazeNumber(jsonObject1.getString("count_gaze"));

            JSONArray goodsArray = null;
            try {
                goodsArray = jsonObject1.getJSONArray("goods_list");//注意这里的处理方法，这句话崩了，不至于让整个程序都崩掉
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<Good> goods = new ArrayList<Good>();
            if (goodsArray != null) {
                for (int j = 0; j < goodsArray.length(); j++) {
                    JSONObject goodJsonObject = goodsArray.getJSONObject(j);
                    Good good = new Good();
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
                shopModel.setGoods(goods);
            }
            shopModel.setShopName(jsonObject1.getString("shop_name"));
            shopModel.setShopDesc(jsonObject1.getString("shop_desc"));
            shopModel.setShopStartTime(jsonObject1.getString("shop_start"));
            shopModel.setShopAddress(jsonObject1.getString("shop_address"));
            shopModel.setShopFlash(jsonObject1.getString("shop_flash"));
            shopModel.setShopWangWang(jsonObject1.getString("shop_wangwang"));
            shopModel.setShopQQ(jsonObject1.getString("shop_qq"));
            shopModel.setShopTel(jsonObject1.getString("shop_tel"));
            shopModel.setCommenTrankScore(jsonObject1.getString("commentrank"));
            shopModel.setCommentServerScore(jsonObject1.getString("commentserver"));
            shopModel.setCommentDeliveryScore(jsonObject1.getString("commentdelivery"));
            shopModel.setCommenTrank(jsonObject1.getString("commentrank_font"));
            shopModel.setCommentServer(jsonObject1.getString("commentserver_font"));
            shopModel.setCommentDelivery(jsonObject1.getString("commentdelivery_font"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shopModel;
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_shop_home_dao_hang://导航
                //Toast.makeText(this, "点击了导航按钮", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.img_search_shop_home://点击了搜索
                Intent toShopSearchIntent=new Intent(this,ShopSearchActivity.class);
                toShopSearchIntent.putExtra(MyConstant.SHOP_USER_ID_KEY,shopModel.getUserId());
                startActivity(toShopSearchIntent);
                break;
            case R.id.text_category_shop_home://分类
                Toast.makeText(this, "点击了分类按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_guan_zhu_home://关注按钮
                guanZhuClickChuLi();
                break;
            case R.id.ll_all_product://全部商品
                toSortActivity("");
                break;
            case R.id.ll_new_product://新商品
                toSortActivity("is_new");
                break;
            case R.id.ll_tui_jian_product://推荐商品
                toSortActivity("is_promote");
                break;
            case R.id.text_see_more://查看更多
                //Toast.makeText(this, "点击了查看更多", Toast.LENGTH_SHORT).show();
                toSortActivity("");
                break;
            case R.id.text_shop_detail://点击了店铺详情
                //Toast.makeText(this, "点击了店铺详情", Toast.LENGTH_SHORT).show();
                Intent toProductDetailIntent=new Intent(this, ShopDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelable(MyConstant.SHOP_MODEL_KEY,shopModel);
                toProductDetailIntent.putExtras(bundle);
                startActivity(toProductDetailIntent);
                break;
            case R.id.ll_hot_category://热门分类
                Toast.makeText(this, "点击了热门分类", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_ke_fu_shop_home://点击了客服
                Toast.makeText(this, "点击了客服", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 跳转到店铺搜索界面
     */
    public void toSortActivity(String type){
        Intent toSortActivityIntent=new Intent(this,ShopProdcutSortActivity.class);
        toSortActivityIntent.putExtra(MyConstant.SHOP_USER_ID_KEY,
                shopModel.getUserId());
        toSortActivityIntent.putExtra(MyConstant.SEARCH_CONTENT_KEY,"");
        toSortActivityIntent.putExtra(MyConstant.GOOD_TYPE_KEY,type);
        startActivity(toSortActivityIntent);
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
                                    Toast.makeText(ShopHomeActivity.this,"关注成功",Toast.LENGTH_SHORT).show();
                                    guanZhuTextView.setTextColor(getResources().getColor(R.color.white_my));
                                    guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_successed_background);
                                    int guanZhuRenShu=Integer.parseInt(shopModel.getGazeNumber());
                                    guanZhuRenShuTextView.setText("已经有" + (1 + guanZhuRenShu) + "人关注");
                                    shopModel.setGazeNumber("" + (1 + guanZhuRenShu));
                                }else if("已取消关注".equals(erroeDesc)){
                                    Toast.makeText(ShopHomeActivity.this,"取消关注成功",Toast.LENGTH_SHORT).show();
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
            AlertDialog.Builder builder=new AlertDialog.Builder(ShopHomeActivity.this);
            builder.setMessage("请登录后关注该店铺");
            builder.setPositiveButton("立即登陆", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ShopHomeActivity.this, LoginActivity.class);
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
