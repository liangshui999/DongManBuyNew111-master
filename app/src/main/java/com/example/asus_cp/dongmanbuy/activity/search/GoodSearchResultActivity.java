package com.example.asus_cp.dongmanbuy.activity.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
import com.example.asus_cp.dongmanbuy.adapter.ShopProductBigListAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopProductGridAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopProductSmallListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.db.SearchRecordDBOperateHelper;
import com.example.asus_cp.dongmanbuy.model.Good;
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
 * 搜索结果展示的界面
 * Created by asus-cp on 2016-06-29.
 */
public class GoodSearchResultActivity extends Activity implements View.OnClickListener {

    private String tag = "GoodSearchResultActivity";

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

    private SearchRecordDBOperateHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_result_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        initView();

        searchContent = getIntent().getStringExtra(MyConstant.SEARCH_CONTENT_KEY);
        dbHelper=new SearchRecordDBOperateHelper();
        if(dbHelper.queryByKeyWord(searchContent)){
            dbHelper.deleteByKeyWord(searchContent);
            dbHelper.insert(searchContent);
        }else{
            dbHelper.insert(searchContent);
        }
        MyLog.d(tag, "传递过来的数据是：" + searchContent);
        requestQueue = MyApplication.getRequestQueue();
        helper = new ImageLoadHelper();

        gridGoods = new ArrayList<Good>();
        smallListGoods = new ArrayList<Good>();
        bigListGoods = new ArrayList<Good>();

        gridAdapter = new ShopProductGridAdapter(GoodSearchResultActivity.this, gridGoods);
        smallListAdapter = new ShopProductSmallListAdapter(GoodSearchResultActivity.this, smallListGoods);
        bigListAdapter = new ShopProductBigListAdapter(GoodSearchResultActivity.this, bigListGoods);

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
                Intent gridIntent = new Intent(GoodSearchResultActivity.this, ProductDetailActivity.class);
                gridIntent.putExtra(MyConstant.GOOD_KEY, goods.get(position));
                startActivity(gridIntent);
            }
        });

        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, "id_asc", "1", LOAD_COUNT_ONCE_STR);

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
     * 初始化视图
     */
    private void initView() {
        daoHangImageView = (ImageView) findViewById(R.id.img_dao_hang_search_result);
        searchImagView = (ImageView) findViewById(R.id.img_search_search_result);
        zongHeTextView = (TextView) findViewById(R.id.text_zong_he_sort_search_result);
        xinPinTextView = (TextView) findViewById(R.id.text_xin_pin_sort_search_result);
        xiaoLiangTextView = (TextView) findViewById(R.id.text_xiao_liang_sort_search_result);
        priceTextView = (TextView) findViewById(R.id.text_price_sort_search_result);
        zongHeDownImageView = (ImageView) findViewById(R.id.img_down_zong_he_sort_search_result);
        priceDownImageView = (ImageView) findViewById(R.id.img_down_price_sort_search_result);
        displayYangShiImageView = (ImageView) findViewById(R.id.img_display_style_sort_search_result);
        productGridView = (PullToRefreshGridView) findViewById(R.id.grid_view_product_sort_search_result);
        productListViewSmall = (PullToRefreshListView) findViewById(R.id.list_view_product_sort_small_search_result);
        productListViewBig = (PullToRefreshListView) findViewById(R.id.list_view_product_sort_big_search_result);
        zongHeLinearLayout= (LinearLayout) findViewById(R.id.ll_zong_he_sort_search_result);
        xinPinLinearLayout= (LinearLayout) findViewById(R.id.ll_xin_pin_sort_search_result);
        xiaoLiangLinearLayout= (LinearLayout) findViewById(R.id.ll_xiao_liang_sort_search_result);
        priceLinearLayout= (LinearLayout) findViewById(R.id.ll_price_sort_search_result);
        displayLinearLayout= (LinearLayout) findViewById(R.id.ll_display_style_sort_search_result);
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
                    List<Good> temp=new ArrayList<Good>();
                    @Override
                    public void onResponse(String s) {
                        List<Good> goods = parseJson(s);
                        temp.clear();
                        MyLog.d(tag, "商品数量是：" + goods.size());
                        switch (flag) {
                            case GRID_VIEW_FLAG:
                                MyLog.d(tag, "网格");
                                int gridCount=0;
                                if(gridGoods.size()>0){
                                    for (int i = 0; i < goods.size(); i++) {
                                        for (int j = 0; j < gridGoods.size(); j++) {
                                            if (gridGoods.get(j).getGoodId().equals(goods.get(i).getGoodId())) {
                                                Toast.makeText(GoodSearchResultActivity.this, "已经是最后一项了", Toast.LENGTH_SHORT).show();
                                                productGridView.onRefreshComplete();
                                                break ;
                                            }else{
                                                gridCount++;
                                            }
                                        }
                                        if(gridCount==gridGoods.size()){//说明在整个gridgoods里面都没有找到该商品
                                            temp.add(goods.get(i));
                                        }
                                    }
                                    gridGoods.addAll(temp);
                                    gridAdapter.notifyDataSetChanged();
                                    productGridView.onRefreshComplete();
                                }else{
                                    gridGoods.addAll(goods);
                                    gridAdapter.notifyDataSetChanged();
                                    productGridView.onRefreshComplete();
                                }

                                productGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //Toast.makeText(ShopProdcutSortActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                                        Intent gridIntent = new Intent(GoodSearchResultActivity.this, ProductDetailActivity.class);
                                        gridIntent.putExtra(MyConstant.GOOD_KEY, gridGoods.get(position));
                                        startActivity(gridIntent);
                                    }
                                });
                                break;
                            case SMALL_LIST_VIEW_FLAG:
                                MyLog.d(tag, "小列表");
                                int smallCount=0;
                                if(smallListGoods.size()>0){
                                    for (int i = 0; i < goods.size(); i++) {
                                        for (int j = 0; j < smallListGoods.size(); j++) {
                                            if (smallListGoods.get(j).getGoodId().equals(goods.get(i).getGoodId())) {
                                                Toast.makeText(GoodSearchResultActivity.this, "已经是最后一项了", Toast.LENGTH_SHORT).show();
                                                productListViewSmall.onRefreshComplete();
                                                break ;
                                            }else{
                                                smallCount++;
                                            }
                                        }
                                        if(smallCount==smallListGoods.size()){//说明在整个gridgoods里面都没有找到该商品
                                            temp.add(goods.get(i));
                                        }
                                    }
                                    smallListGoods.addAll(temp);
                                    smallListAdapter.notifyDataSetChanged();
                                    productListViewSmall.onRefreshComplete();
                                }else{
                                    smallListGoods.addAll(goods);
                                    smallListAdapter.notifyDataSetChanged();
                                    productListViewSmall.onRefreshComplete();
                                }

                                productListViewSmall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //Toast.makeText(ShopProdcutSortActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                                        Intent smallIntent = new Intent(GoodSearchResultActivity.this, ProductDetailActivity.class);
                                        smallIntent.putExtra(MyConstant.GOOD_KEY, smallListGoods.get(position));
                                        startActivity(smallIntent);
                                    }
                                });
                                break;
                            case BIG_LIST_VIEW_FLAG:
                                MyLog.d(tag, "大列表");
                                int bigCount=0;
                                if(bigListGoods.size()>0){
                                    for (int i = 0; i < goods.size(); i++) {
                                        for (int j = 0; j < bigListGoods.size(); j++) {
                                            if (bigListGoods.get(j).getGoodId().equals(goods.get(i).getGoodId())) {
                                                Toast.makeText(GoodSearchResultActivity.this, "已经是最后一项了", Toast.LENGTH_SHORT).show();
                                                productListViewBig.onRefreshComplete();
                                                break ;
                                            }else{
                                                bigCount++;
                                            }
                                        }
                                        if(bigCount==bigListGoods.size()){//说明在整个gridgoods里面都没有找到该商品
                                            temp.add(goods.get(i));
                                        }
                                    }
                                    bigListGoods.addAll(temp);
                                    bigListAdapter.notifyDataSetChanged();
                                    productListViewBig.onRefreshComplete();
                                }else{
                                    bigListGoods.addAll(goods);
                                    bigListAdapter.notifyDataSetChanged();
                                    productListViewBig.onRefreshComplete();
                                }

                                productListViewBig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //Toast.makeText(ShopProdcutSortActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                                        Intent bigIntent = new Intent(GoodSearchResultActivity.this, ProductDetailActivity.class);
                                        bigIntent.putExtra(MyConstant.GOOD_KEY, bigListGoods.get(position));
                                        startActivity(bigIntent);
                                    }
                                });
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
            case R.id.img_dao_hang_search_result://导航
                //Toast.makeText(this,"点击了导航",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.img_search_search_result://搜索按钮
                //Toast.makeText(this, "点击了按钮", Toast.LENGTH_SHORT).show();
                Intent toSearchIntent=new Intent(this,SearchActivity.class);
                startActivity(toSearchIntent);
                finish();
                break;
            case R.id.ll_zong_he_sort_search_result://综合
                zongHeClickChuLi();
                break;
            case R.id.ll_xin_pin_sort_search_result://新品
                XinPinClickChuLi();
                break;
            case R.id.ll_xiao_liang_sort_search_result://销量
                xiaoLiangClickChuLi();
                break;
            case R.id.ll_price_sort_search_result://价格
                priceClickChuLi();
                break;
            case R.id.ll_display_style_sort_search_result://展示样式
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
