package com.example.asus_cp.dongmanbuy.activity.more;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
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
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.activity.search.SearchActivity;
import com.example.asus_cp.dongmanbuy.adapter.JingPinAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopProductBigListAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopProductGridAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopProductSmallListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridView;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 精品推荐的更多界面
 * Created by asus-cp on 2016-07-01.
 */
public class JingPinTuiJianMoreActivity extends Activity implements View.OnClickListener{

    private String tag="JingPinTuiJianMoreActivity";
    private ImageView daoHangImageView;//导航的imageview
    private ImageView searchImagView;//搜索框
    private TextView zongHeTextView;//综合
    private TextView xinPinTextView;//新品
    private TextView xiaoLiangTextView;//销量
    private TextView priceTextView;//价格
    private ImageView zongHeDownImageView;//综合右边的箭头
    private ImageView priceDownImageView;//价格右边的箭头
    private ImageView displayYangShiImageView;//分类方式
    private PullToRefreshGridView productGridView;//商品列表
    private PullToRefreshListView productListViewSmall;//商品列表
    private PullToRefreshListView productListViewBig;//大的商品列表
    private LinearLayout zongHeLinearLayout;
    private LinearLayout xinPinLinearLayout;
    private LinearLayout xiaoLiangLinearLayout;
    private LinearLayout priceLinearLayout;
    private LinearLayout displayLinearLayout;

    private int count = 1;//展示样式
    public static final int KA_PIAN = 1;//卡片的样式
    public static final int LIST_SMALL = 2;
    public static final int LIST_BIG = 0;

    public static final int GRID_VIEW_FLAG = 3;
    public static final int BIG_LIST_VIEW_FLAG = 4;
    public static final int SMALL_LIST_VIEW_FLAG = 5;

    private List<Good> goods;//
    private String shopId;//店铺id

    private int zongHeFlag;//综合标签点击次数的标记
    private int priceFlag;

    private String searchContent;//搜索的内容

    private String searchUrl = "http://www.zmobuy.com/PHP/?url=/search";//搜索的接口
    private String jingPinTuiJianUrl ="http://www.zmobuy.com/PHP/index.php?url=/home/bestgoods";//精品推荐的接口

    private RequestQueue requestQueue;
    private ImageLoadHelper helper;

    //注意下面的3对是一一对应的,上拉加载只负责集合里面添加数据，至于什么时候清空集合则是综合，价格，销量等标签的点击事件负责
    private ShopProductGridAdapter gridAdapter;
    private List<Good> gridGoods;

    private ShopProductSmallListAdapter smallListAdapter;
    private List<Good> smallListGoods;

    private ShopProductBigListAdapter bigListAdapter;
    private List<Good> bigListGoods;


    public static final String LOAD_COUNT_ONCE_STR ="10";//每次加载的商品数量，不要太大了，太大了加载太慢
    public static final int LOAD_COUNT_ONCE_INT =10;//每次加载的商品数量，不要太大了，太大了加载太慢

    private int loadCount = 2;//记录向上加载的次数




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.jing_pin_tui_jian_more_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        initView();
        searchContent="";
        requestQueue = MyApplication.getRequestQueue();
        helper = new ImageLoadHelper();

        gridGoods = new ArrayList<Good>();
        smallListGoods = new ArrayList<Good>();
        bigListGoods = new ArrayList<Good>();

        gridAdapter = new ShopProductGridAdapter(JingPinTuiJianMoreActivity.this, gridGoods);
        smallListAdapter = new ShopProductSmallListAdapter(JingPinTuiJianMoreActivity.this, smallListGoods);
        bigListAdapter = new ShopProductBigListAdapter(JingPinTuiJianMoreActivity.this, bigListGoods);

        productGridView.setAdapter(gridAdapter);
        productListViewSmall.setAdapter(smallListAdapter);
        productListViewBig.setAdapter(bigListAdapter);


        zongHeTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        zongHeDownImageView.setImageResource(R.mipmap.down_red_sort);
//        ShopProductGridAdapter shopProductGridAdapter=new ShopProductGridAdapter(this,goods);
//        productGridView.setAdapter(shopProductGridAdapter);
        productGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShopProdcutSortActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                Intent gridIntent = new Intent(JingPinTuiJianMoreActivity.this, ProductDetailActivity.class);
                gridIntent.putExtra(MyConstant.GOOD_KEY, gridGoods.get(position));
                startActivity(gridIntent);
            }
        });

        productListViewSmall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShopProdcutSortActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                Intent smallIntent = new Intent(JingPinTuiJianMoreActivity.this, ProductDetailActivity.class);
                smallIntent.putExtra(MyConstant.GOOD_KEY, smallListGoods.get(position-1));
                startActivity(smallIntent);
            }
        });

        productListViewBig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShopProdcutSortActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                Intent bigIntent = new Intent(JingPinTuiJianMoreActivity.this, ProductDetailActivity.class);
                bigIntent.putExtra(MyConstant.GOOD_KEY, bigListGoods.get(position-1));
                startActivity(bigIntent);
            }
        });

        //弹出正在加载的对话框
        DialogHelper.showDialog(this);

        //初始化界面的显示,由于接口，只能初始化的时候，显示限时秒杀的商品,之后再跳转就是全部的商品了
        //getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "id_asc", "1", LOAD_COUNT_ONCE_STR);
        initJieMian();

        //给gridview设置上拉加载的监听事件
        productGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        productGridView.setOnRefreshListener(new MyOnrefreshListener());

        productListViewBig.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        productListViewBig.setOnRefreshListener(new MyOnrefreshListener());

        productListViewSmall.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        productListViewSmall.setOnRefreshListener(new MyOnrefreshListener());


        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        searchImagView.setOnClickListener(this);
        zongHeLinearLayout.setOnClickListener(this);
        xinPinLinearLayout.setOnClickListener(this);
        xiaoLiangLinearLayout.setOnClickListener(this);
        priceLinearLayout.setOnClickListener(this);
        displayLinearLayout.setOnClickListener(this);
    }


    /**
     * 初始化界面的显示，只有第一次显示精品推荐的界面
     */
    private void initJieMian() {
        StringRequest jingPinStringRequest=new StringRequest(Request.Method.GET, jingPinTuiJianUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DialogHelper.dissmisDialog();
                List<Good> goods = parseCaiNiLikeAndJingPin(s);
                MyLog.d(tag,"精品推荐返回的数据是："+s);
                MyLog.d(tag,"精品推荐的商品数量是："+goods.size());
                gridGoods.clear();
                gridGoods.addAll(goods);
                gridAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(jingPinStringRequest);
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
     * 初始化视图
     */
    private void initView() {
        daoHangImageView = (ImageView) findViewById(R.id.img_dao_hang_jing_pin_tui_jian_more);
        searchImagView = (ImageView) findViewById(R.id.img_search_jing_pin_tui_jian_more);
        zongHeTextView = (TextView) findViewById(R.id.text_zong_he_sort_jing_pin_tui_jian_more);
        xinPinTextView = (TextView) findViewById(R.id.text_xin_pin_sort_jing_pin_tui_jian_more);
        xiaoLiangTextView = (TextView) findViewById(R.id.text_xiao_liang_sort_jing_pin_tui_jian_more);
        priceTextView = (TextView) findViewById(R.id.text_price_sort_jing_pin_tui_jian_more);
        zongHeDownImageView = (ImageView) findViewById(R.id.img_down_zong_he_sort_jing_pin_tui_jian_more);
        priceDownImageView = (ImageView) findViewById(R.id.img_down_price_sort_jing_pin_tui_jian_more);
        displayYangShiImageView = (ImageView) findViewById(R.id.img_display_style_sort_jing_pin_tui_jian_more);
        productGridView = (PullToRefreshGridView) findViewById(R.id.grid_view_product_sort_jing_pin_tui_jian_more);
        productListViewSmall = (PullToRefreshListView) findViewById(R.id.list_view_product_sort_small_jing_pin_tui_jian_more);
        productListViewBig = (PullToRefreshListView) findViewById(R.id.list_view_product_sort_big_jing_pin_tui_jian_more);
        zongHeLinearLayout= (LinearLayout) findViewById(R.id.ll_zong_he_sort_jing_pin_tui_jian_more);
        xinPinLinearLayout= (LinearLayout) findViewById(R.id.ll_xin_pin_sort_jing_pin_tui_jian_more);
        xiaoLiangLinearLayout= (LinearLayout) findViewById(R.id.ll_xiao_liang_sort_jing_pin_tui_jian_more);
        priceLinearLayout= (LinearLayout) findViewById(R.id.ll_price_sort_jing_pin_tui_jian_more);
        displayLinearLayout= (LinearLayout) findViewById(R.id.ll_display_style_sort_jing_pin_tui_jian_more);
    }


    /**
     * 从网络获取数据
     *
     * @param flag    给哪个view设置适配器
     * @param keyWord 搜索的关键字
     * @param sortBy  排序方式
     * @param start   开始
     * @param end     结束
     */
    private void getDataFromIntenet(final int flag, final String keyWord, final String sortBy, final String start, final String end) {
        StringRequest searchRequest = new StringRequest(Request.Method.POST, searchUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        List<Good> goods = parseJson(s);
                        MyLog.d(tag, "商品数量是：" + goods.size());
                        switch (flag) {
                            case GRID_VIEW_FLAG:
                                MyLog.d(tag, "网格");
                                if(goods.size()>1){
                                    gridGoods.addAll(goods);
                                    gridAdapter.notifyDataSetChanged();
                                    productGridView.onRefreshComplete();
                                }else{
                                    Toast.makeText(JingPinTuiJianMoreActivity.this, "已经是最后一项了", Toast.LENGTH_SHORT).show();
                                    productGridView.onRefreshComplete();
                                }

                                break;
                            case SMALL_LIST_VIEW_FLAG:
                                MyLog.d(tag, "小列表");
                                if(goods.size()>1){
                                    smallListGoods.addAll(goods);
                                    smallListAdapter.notifyDataSetChanged();
                                    productListViewSmall.onRefreshComplete();
                                }else{
                                    Toast.makeText(JingPinTuiJianMoreActivity.this,"已经是最后一项了",Toast.LENGTH_SHORT).show();
                                    productListViewSmall.onRefreshComplete();
                                }
                                break;
                            case BIG_LIST_VIEW_FLAG:
                                MyLog.d(tag, "大列表");
                                if(goods.size()>1){
                                    bigListGoods.addAll(goods);
                                    bigListAdapter.notifyDataSetChanged();
                                    productListViewBig.onRefreshComplete();
                                }else{
                                    Toast.makeText(JingPinTuiJianMoreActivity.this,"已经是最后一项了",Toast.LENGTH_SHORT).show();
                                    productListViewBig.onRefreshComplete();
                                }
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                MyLog.d(tag,"sortby="+sortBy+"...."+"keyword="+keyWord+"....."+"start="+start+"....."+"end="+end);
                Map<String, String> map = new HashMap<String, String>();
                String json = "{\"filter\":{\"keywords\":\"" + keyWord + "\",\"category_id\":\"\",\"price_range\":\"\",\"brand_id\":\"\",\"intro\":\"\",\"sort_by\":\"" + sortBy + "\"},\"pagination\":{\"page\":\"" + start + "\",\"loadCount\":\"" + end + "\"}}";
                map.put("json", json);
                return map;
            }
        };
        requestQueue.add(searchRequest);
    }


    /**
     * 解析json数据
     *
     * @param s
     */
    private List<Good> parseJson(String s) {
        MyLog.d(tag, "返回的数据是：" + s);
        List<Good> goods = new ArrayList<Good>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject ziJsonObj = jsonArray.getJSONObject(i);
                Good good = new Good();
                good.setGoodId(ziJsonObj.getString("goods_id"));
                good.setSalesVolume(ziJsonObj.getString("sales_volume"));
                good.setGoodName(JsonHelper.decodeUnicode(ziJsonObj.getString("name")));
                good.setMarket_price(JsonHelper.decodeUnicode(ziJsonObj.getString("market_price")));
                good.setShopPrice(JsonHelper.decodeUnicode(ziJsonObj.getString("shop_price")));

                JSONObject imgObj = ziJsonObj.getJSONObject("img");
                good.setGoodsThumb(imgObj.getString("thumb"));
                good.setGoodsImg(imgObj.getString("url"));
                good.setGoodsSmallImag(imgObj.getString("small"));
                goods.add(good);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goods;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_dao_hang_jing_pin_tui_jian_more://导航
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.img_search_jing_pin_tui_jian_more://搜索按钮
                Intent searchIntent=new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.ll_zong_he_sort_jing_pin_tui_jian_more://综合
                zongHeClickChuLi();
                break;
            case R.id.ll_xin_pin_sort_jing_pin_tui_jian_more://新品
                XinPinClickChuLi();
                break;
            case R.id.ll_xiao_liang_sort_jing_pin_tui_jian_more://销量
                xiaoLiangClickChuLi();
                break;
            case R.id.ll_price_sort_jing_pin_tui_jian_more://价格
                priceClickChuLi();
                break;
            case R.id.ll_display_style_sort_jing_pin_tui_jian_more://展示样式
                dispalyClickChuLi();
                break;
        }
    }


    /**
     * 展示样式的点击事件处理
     */
    private void dispalyClickChuLi() {
        count++;
        if (count % 3 == KA_PIAN) {
            displayYangShiImageView.setBackgroundResource(R.mipmap.kapian);
            productListViewSmall.setVisibility(View.GONE);
            productListViewBig.setVisibility(View.GONE);
            productGridView.setVisibility(View.VISIBLE);

            if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
                    && zongHeFlag % 2 == 0) {
                gridAdapter.notifyDataSetChanged();

            } else if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向上
                    && zongHeFlag % 2 == 1) {
                gridAdapter.notifyDataSetChanged();

            } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向下
                    && priceFlag % 2 == 0) {

                gridAdapter.notifyDataSetChanged();

            } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向上
                    && priceFlag % 2 == 1) {

                gridAdapter.notifyDataSetChanged();

            } else if (xinPinTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//新品红色

                gridAdapter.notifyDataSetChanged();

            } else if (xiaoLiangTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//销量红色

                gridAdapter.notifyDataSetChanged();
            }


        } else if (count % 3 == LIST_SMALL) {
            displayYangShiImageView.setBackgroundResource(R.mipmap.liebiao);
            productGridView.setVisibility(View.GONE);
            productListViewBig.setVisibility(View.GONE);
            productListViewSmall.setVisibility(View.VISIBLE);

            if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
                    && zongHeFlag % 2 == 0) {
                smallListGoods.clear();
                smallListGoods.addAll(gridGoods);
                smallListAdapter.notifyDataSetChanged();

            } else if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向上
                    && zongHeFlag % 2 == 1) {
                smallListGoods.clear();
                smallListGoods.addAll(gridGoods);
                smallListAdapter.notifyDataSetChanged();

            } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向下
                    && priceFlag % 2 == 0) {
                smallListGoods.clear();
                smallListGoods.addAll(gridGoods);
                smallListAdapter.notifyDataSetChanged();

            } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向上
                    && priceFlag % 2 == 1) {
                smallListGoods.clear();
                smallListGoods.addAll(gridGoods);
                smallListAdapter.notifyDataSetChanged();

            } else if (xinPinTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//新品红色
                smallListGoods.clear();
                smallListGoods.addAll(gridGoods);
                smallListAdapter.notifyDataSetChanged();

            } else if (xiaoLiangTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//销量红色
                smallListGoods.clear();
                smallListGoods.addAll(gridGoods);
                smallListAdapter.notifyDataSetChanged();
            }


        } else if (count % 3 == LIST_BIG) {
            displayYangShiImageView.setBackgroundResource(R.mipmap.fangxing);
            productGridView.setVisibility(View.GONE);
            productListViewSmall.setVisibility(View.GONE);
            productListViewBig.setVisibility(View.VISIBLE);

            if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
                    && zongHeFlag % 2 == 0) {
                bigListGoods.clear();
                bigListGoods.addAll(gridGoods);
                bigListAdapter.notifyDataSetChanged();

            } else if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向上
                    && zongHeFlag % 2 == 1) {
                bigListGoods.clear();
                bigListGoods.addAll(gridGoods);
                bigListAdapter.notifyDataSetChanged();

            } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向下
                    && priceFlag % 2 == 0) {
                bigListGoods.clear();
                bigListGoods.addAll(gridGoods);
                bigListAdapter.notifyDataSetChanged();

            } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向上
                    && priceFlag % 2 == 1) {
                bigListGoods.clear();
                bigListGoods.addAll(gridGoods);
                bigListAdapter.notifyDataSetChanged();

            } else if (xinPinTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//新品红色
                bigListGoods.clear();
                bigListGoods.addAll(gridGoods);
                bigListAdapter.notifyDataSetChanged();

            } else if (xiaoLiangTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//销量红色
                bigListGoods.clear();
                bigListGoods.addAll(gridGoods);
                bigListAdapter.notifyDataSetChanged();
            }

        }
    }


    /**
     * 价格的点击事件处理
     */
    private void priceClickChuLi() {
        reset();
        priceTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        if (priceFlag % 2 == 0) {
            priceDownImageView.setImageResource(R.mipmap.down_red_sort);
            if (count % 3 == KA_PIAN) {
                gridGoods.clear();
                getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "price_desc", "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_SMALL) {
                smallListGoods.clear();
                getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "price_desc", "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_BIG) {
                bigListGoods.clear();
                getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "price_desc", "1", LOAD_COUNT_ONCE_STR);
            }
        } else {
            priceDownImageView.setImageResource(R.mipmap.up_red_sort);
            if (count % 3 == KA_PIAN) {
                gridGoods.clear();
                getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "price_asc", "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_SMALL) {
                smallListGoods.clear();
                getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "price_asc", "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_BIG) {
                bigListGoods.clear();
                getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "price_asc", "1", LOAD_COUNT_ONCE_STR);
            }
        }
        priceFlag++;
    }


    /**
     * 销量的点击事件处理
     */
    private void xiaoLiangClickChuLi() {
        reset();
        xiaoLiangTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        if (count % 3 == KA_PIAN) {
            gridGoods.clear();
            getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "sales_volume_desc", "1", LOAD_COUNT_ONCE_STR);
        } else if (count % 3 == LIST_SMALL) {
            smallListGoods.clear();
            getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "sales_volume_desc", "1", LOAD_COUNT_ONCE_STR);
        } else if (count % 3 == LIST_BIG) {
            bigListGoods.clear();
            getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "sales_volume_desc", "1", LOAD_COUNT_ONCE_STR);
        }
    }


    /**
     * 新品的点击事件处理
     */
    private void XinPinClickChuLi() {
        reset();
        xinPinTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        if (count % 3 == KA_PIAN) {
            gridGoods.clear();
            getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "is_hot", "1", LOAD_COUNT_ONCE_STR);
        } else if (count % 3 == LIST_SMALL) {
            smallListGoods.clear();
            getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "is_hot", "1", LOAD_COUNT_ONCE_STR);
        } else if (count % 3 == LIST_BIG) {
            bigListGoods.clear();
            getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "is_hot", "1", LOAD_COUNT_ONCE_STR);
        }
    }


    /**
     * 综合点击事件处理
     */
    private void zongHeClickChuLi() {
        reset();
        zongHeTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        if (zongHeFlag % 2 == 0) {
            zongHeDownImageView.setImageResource(R.mipmap.down_red_sort);
            if (count % 3 == KA_PIAN) {
                gridGoods.clear();
                getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "id_desc", "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_SMALL) {
                smallListGoods.clear();
                getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "id_desc", "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_BIG) {
                bigListGoods.clear();
                getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "id_desc", "1", LOAD_COUNT_ONCE_STR);
            }
        } else {
            zongHeDownImageView.setImageResource(R.mipmap.up_red_sort);
            if (count % 3 == KA_PIAN) {
                gridGoods.clear();
                getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "id_asc", "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_SMALL) {
                smallListGoods.clear();
                getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "id_asc", "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_BIG) {
                bigListGoods.clear();
                getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "id_asc", "1", LOAD_COUNT_ONCE_STR);
            }
        }
        zongHeFlag++;
    }

    /**
     * 将标签的颜色设置成初始状态
     */
    public void reset() {
        zongHeTextView.setTextColor(getResources().getColor(R.color.black));
        xinPinTextView.setTextColor(getResources().getColor(R.color.black));
        xiaoLiangTextView.setTextColor(getResources().getColor(R.color.black));
        priceTextView.setTextColor(getResources().getColor(R.color.black));
        zongHeDownImageView.setImageResource(R.mipmap.down_black_sort);
        priceDownImageView.setImageResource(R.mipmap.down_black_sort);
        loadCount=2;//将loadcount设置为初始值
    }


    /**
     * 自定义的刷新监听器
     */
    class MyOnrefreshListener implements PullToRefreshBase.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatedDate = simpleDateFormat.format(new Date());
            // Update the LastUpdatedLabel
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(formatedDate);
            PullToRefreshBase.Mode mode = refreshView.getCurrentMode();//注意是currentmode，不是mode

            if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {      //下拉刷新
                refreshView.getLoadingLayoutProxy().setPullLabel("下拉可以刷新");
                refreshView.getLoadingLayoutProxy().setReleaseLabel("释放刷新");
                refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新...");

            } else if (mode == PullToRefreshBase.Mode.PULL_FROM_END) {      //向上加载
                refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在加载...");
                refreshView.getLoadingLayoutProxy().setPullLabel("上拉可以加载");
                refreshView.getLoadingLayoutProxy().setReleaseLabel("释放刷新");

                if (count % 3 == KA_PIAN) {
                    if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
                            && zongHeFlag % 2 == 1) {
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "id_desc", loadCount + "", LOAD_COUNT_ONCE_STR);

                    } else if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向上
                            && zongHeFlag % 2 == 0) {
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "id_asc", loadCount+ "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向下
                            && priceFlag % 2 == 1) {
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "price_desc", loadCount + "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向上
                            && priceFlag % 2 == 0) {
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "price_asc", loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (xinPinTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//新品红色
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "is_hot", loadCount +  "", LOAD_COUNT_ONCE_STR);
                    } else if (xiaoLiangTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//销量红色
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "sales_volume_desc", loadCount  + "", LOAD_COUNT_ONCE_STR);
                    }
                } else if (count % 3 == LIST_SMALL) {
                    if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
                            && zongHeFlag % 2 == 1) {
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "id_desc", loadCount  + "",LOAD_COUNT_ONCE_STR);

                    } else if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向上
                            && zongHeFlag % 2 == 0) {
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "id_asc", loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向下
                            && priceFlag % 2 == 1) {
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "price_desc", loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向上
                            && priceFlag % 2 == 0) {
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "price_asc", loadCount + "", LOAD_COUNT_ONCE_STR);
                    } else if (xinPinTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//新品红色
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "is_hot", loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (xiaoLiangTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//销量红色
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, "sales_volume_desc", loadCount + "", LOAD_COUNT_ONCE_STR);
                    }
                } else if (count % 3 == LIST_BIG) {
                    if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
                            && zongHeFlag % 2 == 1) {
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "id_desc", loadCount+ "", LOAD_COUNT_ONCE_STR);

                    } else if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向上
                            && zongHeFlag % 2 == 0) {
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "id_asc", loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向下
                            && priceFlag % 2 == 1) {
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "price_desc", loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向上
                            && priceFlag % 2 == 0) {
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "price_asc", loadCount  + "",LOAD_COUNT_ONCE_STR);
                    } else if (xinPinTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//新品红色
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "is_hot", loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (xiaoLiangTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//销量红色
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, "sales_volume_desc", loadCount  + "", LOAD_COUNT_ONCE_STR);
                    }
                }

            }
            loadCount++;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

}
