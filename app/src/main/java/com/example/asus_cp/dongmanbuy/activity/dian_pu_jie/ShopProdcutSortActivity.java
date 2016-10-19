package com.example.asus_cp.dongmanbuy.activity.dian_pu_jie;

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
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.adapter.ShopProductBigListAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopProductGridAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopProductSmallListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.db.SearchRecordDBOperateHelper;
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
 * 店铺商品排序的界面
 * Created by asus-cp on 2016-06-12.
 */
public class ShopProdcutSortActivity extends BaseActivity implements View.OnClickListener{

    private String tag="ShopProdcutSortActivity";

    private ImageView daoHangImageView;//导航的imageview
    private ImageView searchView;//搜索框
    private TextView categoryTextView;//分类的按钮
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
    private LinearLayout noContentLinearLayout;//没有内容时，展示这个内容

    private int count=1;//展示样式
    public static final int KA_PIAN=1;//卡片的样式
    public static final int LIST_SMALL=2;
    public static final int LIST_BIG=0;

    public static final int GRID_VIEW_FLAG = 3;
    public static final int BIG_LIST_VIEW_FLAG = 4;
    public static final int SMALL_LIST_VIEW_FLAG = 5;

    private String shopUserId;//店铺id
    private String goodType;//商品类型

    private int zongHeFlag;//综合标签点击次数的标记
    private int priceFlag;

    private String searchContent;//搜索的内容
    private String searchUrl = "http://www.zmobuy.com/PHP/?url=/store/prolist";//店铺商品排序的接口
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

    public static final String GOOD_ID_DESC=1+"";//按照id倒序排列
    public static final String GOOD_ID_ASC=10+"";
    public static final String PRICE_DESC=4+"";
    public static final String PRICE_ASC=40+"";
    public static final String XIN_PIN_DESC=2+"";
    public static final String XIAO_LIANG_DESC=3+"";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_product_sort_activity_layout);
        init();

    }

    /**
     * 初始化的方法
     */
    private void init() {
        initView();

        searchContent = getIntent().getStringExtra(MyConstant.SEARCH_CONTENT_KEY);
        shopUserId=getIntent().getStringExtra(MyConstant.SHOP_USER_ID_KEY);
        goodType=getIntent().getStringExtra(MyConstant.GOOD_TYPE_KEY);
        if(goodType==null){
            goodType="";
        }
        if(searchContent==null){
            searchContent="";
        }
        MyLog.d(tag, "传递过来的数据是：" + searchContent+"......."+shopUserId+"........"+goodType);

        dbHelper=new SearchRecordDBOperateHelper();
        if(dbHelper.queryByKeyWord(searchContent)){
            dbHelper.deleteByKeyWord(searchContent);
            dbHelper.insert(searchContent);
        }else{
            dbHelper.insert(searchContent);
        }

        helper = new ImageLoadHelper();

        gridGoods = new ArrayList<Good>();
        smallListGoods = new ArrayList<Good>();
        bigListGoods = new ArrayList<Good>();

        gridAdapter = new ShopProductGridAdapter(ShopProdcutSortActivity.this, gridGoods,productGridView);
        smallListAdapter = new ShopProductSmallListAdapter(ShopProdcutSortActivity.this, smallListGoods,productListViewSmall);
        bigListAdapter = new ShopProductBigListAdapter(ShopProdcutSortActivity.this, bigListGoods,productListViewBig);

        productGridView.setAdapter(gridAdapter);
        productListViewSmall.setAdapter(smallListAdapter);
        productListViewBig.setAdapter(bigListAdapter);


        zongHeTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        zongHeDownImageView.setImageResource(R.mipmap.down_red_sort);
        zongHeFlag=1;

        productGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShopProdcutSortActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                Intent gridIntent = new Intent(ShopProdcutSortActivity.this, ProductDetailActivity.class);
                gridIntent.putExtra(MyConstant.GOOD_KEY, gridGoods.get(position));
                startActivity(gridIntent);
            }
        });

        productListViewSmall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShopProdcutSortActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();
                Intent smallIntent = new Intent(ShopProdcutSortActivity.this, ProductDetailActivity.class);
                smallIntent.putExtra(MyConstant.GOOD_KEY, smallListGoods.get(position-1));
                startActivity(smallIntent);
            }
        });

        productListViewBig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShopProdcutSortActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();
                Intent bigIntent = new Intent(ShopProdcutSortActivity.this, ProductDetailActivity.class);
                bigIntent.putExtra(MyConstant.GOOD_KEY, bigListGoods.get(position-1));
                startActivity(bigIntent);
            }
        });

        //弹出正在加载的对话框
        DialogHelper.showDialog(this);
        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, GOOD_ID_DESC, "1", LOAD_COUNT_ONCE_STR);

        //给gridview设置上拉加载的监听事件
        productGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        productGridView.setOnRefreshListener(new MyOnrefreshListener());

        productListViewBig.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        productListViewBig.setOnRefreshListener(new MyOnrefreshListener());

        productListViewSmall.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        productListViewSmall.setOnRefreshListener(new MyOnrefreshListener());



        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        searchView.setOnClickListener(this);
        categoryTextView.setOnClickListener(this);
        zongHeTextView.setOnClickListener(this);
        xinPinTextView.setOnClickListener(this);
        xiaoLiangTextView.setOnClickListener(this);
        priceTextView.setOnClickListener(this);
        displayYangShiImageView.setOnClickListener(this);
    }



    /**
     * 从网络获取数据
     * @param flag    给哪个view设置适配器
     * @param keyWord 搜索的关键字
     * @param sortBy  排序方式
     * @param page   页数
     * @param countEveryPage 每页的条数
     */
    private void getDataFromIntenet(final int flag, final String keyWord, final String sortBy, final String page, final String countEveryPage) {
        StringRequest searchRequest = new StringRequest(Request.Method.POST, searchUrl,
                new Response.Listener<String>() {
                    List<Good> temp=new ArrayList<Good>();
                    @Override
                    public void onResponse(String s) {
                        DialogHelper.dissmisDialog();//隐藏对话框
                        List<Good> goods = parseJson(s);
                        temp.clear();
                        MyLog.d(tag, "商品数量是：" + goods.size());
                        if(goods.size()<=0 && loadCount==2){    //没有商品的情况
                            productGridView.setVisibility(View.GONE);
                            productListViewBig.setVisibility(View.GONE);
                            productListViewSmall.setVisibility(View.GONE);
                            noContentLinearLayout.setVisibility(View.VISIBLE);
                        }else{
                            productGridView.setVisibility(View.VISIBLE);
                            productListViewBig.setVisibility(View.VISIBLE);
                            productListViewSmall.setVisibility(View.VISIBLE);
                            noContentLinearLayout.setVisibility(View.GONE);
                            switch (flag) {
                                case GRID_VIEW_FLAG:
                                    productGridView.setVisibility(View.VISIBLE);
                                    productListViewBig.setVisibility(View.GONE);
                                    productListViewSmall.setVisibility(View.GONE);
                                    MyLog.d(tag, "网格");
                                    int gridCount=0;
                                    if(gridGoods.size()>0){
                                        for (int i = 0; i < goods.size(); i++) {
                                            for (int j = 0; j < gridGoods.size(); j++) {
                                                if (gridGoods.get(j).getGoodId().equals(goods.get(i).getGoodId())) {
                                                    Toast.makeText(ShopProdcutSortActivity.this, "已经是最后一项了", Toast.LENGTH_SHORT).show();
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
                                    }else{  //说明是第一次添加
                                        gridGoods.addAll(goods);
                                        gridAdapter.notifyDataSetChanged();
                                        productGridView.onRefreshComplete();
                                    }

                                    break;
                                case SMALL_LIST_VIEW_FLAG:
                                    productGridView.setVisibility(View.GONE);
                                    productListViewBig.setVisibility(View.GONE);
                                    productListViewSmall.setVisibility(View.VISIBLE);
                                    MyLog.d(tag, "小列表");
                                    int smallCount=0;
                                    if(smallListGoods.size()>0){
                                        for (int i = 0; i < goods.size(); i++) {
                                            for (int j = 0; j < smallListGoods.size(); j++) {
                                                if (smallListGoods.get(j).getGoodId().equals(goods.get(i).getGoodId())) {
                                                    Toast.makeText(ShopProdcutSortActivity.this, "已经是最后一项了", Toast.LENGTH_SHORT).show();
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
                                        MyLog.d(tag,"商品列表的数量是："+smallListGoods.size());
                                        smallListGoods.addAll(temp);
                                        smallListAdapter.notifyDataSetChanged();
                                        productListViewSmall.onRefreshComplete();
                                    }else{
                                        smallListGoods.addAll(goods);
                                        smallListAdapter.notifyDataSetChanged();
                                        productListViewSmall.onRefreshComplete();
                                    }

                                    break;
                                case BIG_LIST_VIEW_FLAG:
                                    productGridView.setVisibility(View.GONE);
                                    productListViewBig.setVisibility(View.VISIBLE);
                                    productListViewSmall.setVisibility(View.GONE);
                                    MyLog.d(tag, "大列表");
                                    int bigCount=0;
                                    if(bigListGoods.size()>0){
                                        for (int i = 0; i < goods.size(); i++) {
                                            for (int j = 0; j < bigListGoods.size(); j++) {
                                                if (bigListGoods.get(j).getGoodId().equals(goods.get(i).getGoodId())) {
                                                    Toast.makeText(ShopProdcutSortActivity.this, "已经是最后一项了", Toast.LENGTH_SHORT).show();
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
                                    break;
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                MyLog.d(tag,"shopUserId="+shopUserId+"sortby="+sortBy+"...."+"goodType="+goodType+"keyword="+keyWord+"....."+"page="+page+"....."+"countEveryPage="+countEveryPage);
                Map<String, String> map = new HashMap<String, String>();
                String json = "{\"page\":\""+page+"\",\"type\":\""+goodType+"\",\"ru_id\":\""+shopUserId+"\",\"sort\":\""+sortBy+"\",\"keyword\":\""+searchContent+"\",\"bid\":\"\",\"cat_id\":\"\",\"bigcat\":\"\",\"where\":\"\"}";
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
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject ziJsonObj = jsonArray.getJSONObject(i);
                Good good = new Good();
                good.setGoodId(ziJsonObj.getString("goods_id"));
                good.setSalesVolume(ziJsonObj.getString("sales_volume"));
                good.setGoodName(JsonHelper.decodeUnicode(ziJsonObj.getString("goods_name")));
                good.setGoodsNumber(ziJsonObj.getString("goods_number"));
                good.setMarket_price(JsonHelper.decodeUnicode(ziJsonObj.getString("market_price")));
                good.setShopPrice(JsonHelper.decodeUnicode(ziJsonObj.getString("shop_price")));
                good.setGoodsThumb(ziJsonObj.getString("goods_thumb"));
                good.setGoodsImg(ziJsonObj.getString("goods_img"));
                good.setGoodsSmallImag(ziJsonObj.getString("original_img"));
                goods.add(good);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goods;
    }




    /**
     * 初始化视图
     */
    public void initView() {
        daoHangImageView= (ImageView) findViewById(R.id.img_shop_product_sort_dao_hang);
        searchView= (ImageView) findViewById(R.id.img_search_shop_product_sort);
        categoryTextView= (TextView) findViewById(R.id.text_category_shop_product_sort);
        zongHeTextView= (TextView) findViewById(R.id.text_zong_he_sort);
        xinPinTextView= (TextView) findViewById(R.id.text_xin_pin_sort);
        xiaoLiangTextView= (TextView) findViewById(R.id.text_xiao_liang_sort);
        priceTextView= (TextView) findViewById(R.id.text_price_sort);
        zongHeDownImageView= (ImageView) findViewById(R.id.img_down_zong_he_sort);
        priceDownImageView= (ImageView) findViewById(R.id.img_down_price_sort);
        displayYangShiImageView= (ImageView) findViewById(R.id.img_display_style_sort);
        productGridView= (PullToRefreshGridView) findViewById(R.id.grid_view_product_sort);
        productListViewSmall= (PullToRefreshListView) findViewById(R.id.list_view_product_sort_small);
        productListViewBig= (PullToRefreshListView) findViewById(R.id.list_view_product_sort_big);
        noContentLinearLayout= (LinearLayout) findViewById(R.id.ll_no_content_display);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_shop_product_sort_dao_hang://导航
                finish();
            break;
            case R.id.img_search_shop_product_sort://点击了搜索按钮
                Intent toSearchIntent=new Intent(this,ShopSearchActivity.class);
                toSearchIntent.putExtra(MyConstant.SHOP_USER_ID_KEY,shopUserId);
                startActivity(toSearchIntent);
                break;
            case R.id.text_category_shop_product_sort://分类
                Toast.makeText(this,"点击了分类",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_zong_he_sort://综合
                zongHeClickChuLi();
                break;
            case R.id.text_xin_pin_sort://新品
                XinPinClickChuLi();
                break;
            case R.id.text_xiao_liang_sort://销量
                xiaoLiangClickChuLi();
                break;
            case R.id.text_price_sort://价格
                priceClickChuLi();
                break;
            case R.id.img_display_style_sort://展示样式
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

            gridAdapter.notifyDataSetChanged();
            /*if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
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
            }*/


        } else if (count % 3 == LIST_SMALL) {
            displayYangShiImageView.setBackgroundResource(R.mipmap.liebiao);
            productGridView.setVisibility(View.GONE);
            productListViewBig.setVisibility(View.GONE);
            productListViewSmall.setVisibility(View.VISIBLE);

            smallListGoods.clear();
            smallListGoods.addAll(gridGoods);
            smallListAdapter.notifyDataSetChanged();

            /*if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
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
            }*/


        } else if (count % 3 == LIST_BIG) {
            displayYangShiImageView.setBackgroundResource(R.mipmap.fangxing);
            productGridView.setVisibility(View.GONE);
            productListViewSmall.setVisibility(View.GONE);
            productListViewBig.setVisibility(View.VISIBLE);

            bigListGoods.clear();
            bigListGoods.addAll(gridGoods);
            bigListAdapter.notifyDataSetChanged();

            /*if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
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
            }*/

        }
    }


    /**
     * 价格的点击事件处理
     */
    private void priceClickChuLi() {
        zongHeFlag=0;
        reset();
        priceTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        if (priceFlag % 2 == 0) {
            priceDownImageView.setImageResource(R.mipmap.down_red_sort);
            if (count % 3 == KA_PIAN) {
                gridGoods.clear();
                getDataFromIntenet(GRID_VIEW_FLAG, searchContent, PRICE_DESC, "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_SMALL) {
                smallListGoods.clear();
                getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, PRICE_DESC, "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_BIG) {
                bigListGoods.clear();
                getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, PRICE_DESC, "1", LOAD_COUNT_ONCE_STR);
            }
        } else {
            priceDownImageView.setImageResource(R.mipmap.up_red_sort);
            if (count % 3 == KA_PIAN) {
                gridGoods.clear();
                getDataFromIntenet(GRID_VIEW_FLAG, searchContent, PRICE_ASC, "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_SMALL) {
                smallListGoods.clear();
                getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, PRICE_ASC, "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_BIG) {
                bigListGoods.clear();
                getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, PRICE_ASC, "1", LOAD_COUNT_ONCE_STR);
            }
        }
        priceFlag++;
    }


    /**
     * 销量的点击事件处理
     */
    private void xiaoLiangClickChuLi() {
        reset();
        zongHeFlag=0;
        priceFlag=0;
        xiaoLiangTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        if (count % 3 == KA_PIAN) {
            gridGoods.clear();
            getDataFromIntenet(GRID_VIEW_FLAG, searchContent, XIAO_LIANG_DESC, "1", LOAD_COUNT_ONCE_STR);
        } else if (count % 3 == LIST_SMALL) {
            smallListGoods.clear();
            getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, XIAO_LIANG_DESC, "1", LOAD_COUNT_ONCE_STR);
        } else if (count % 3 == LIST_BIG) {
            bigListGoods.clear();
            getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, XIAO_LIANG_DESC, "1", LOAD_COUNT_ONCE_STR);
        }
    }


    /**
     * 新品的点击事件处理
     */
    private void XinPinClickChuLi() {
        reset();
        zongHeFlag=0;
        priceFlag=0;
        xinPinTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        if (count % 3 == KA_PIAN) {
            gridGoods.clear();
            getDataFromIntenet(GRID_VIEW_FLAG, searchContent, XIN_PIN_DESC, "1", LOAD_COUNT_ONCE_STR);
        } else if (count % 3 == LIST_SMALL) {
            smallListGoods.clear();
            getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, XIN_PIN_DESC, "1", LOAD_COUNT_ONCE_STR);
        } else if (count % 3 == LIST_BIG) {
            bigListGoods.clear();
            getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, XIN_PIN_DESC, "1", LOAD_COUNT_ONCE_STR);
        }
    }


    /**
     * 综合点击事件处理
     */
    private void zongHeClickChuLi() {
        reset();
        priceFlag=0;
        zongHeTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        if (zongHeFlag % 2 == 0) {
            zongHeDownImageView.setImageResource(R.mipmap.down_red_sort);
            if (count % 3 == KA_PIAN) {
                gridGoods.clear();
                getDataFromIntenet(GRID_VIEW_FLAG, searchContent, GOOD_ID_DESC, "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_SMALL) {
                smallListGoods.clear();
                getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, GOOD_ID_DESC, "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_BIG) {
                bigListGoods.clear();
                getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, GOOD_ID_DESC, "1", LOAD_COUNT_ONCE_STR);
            }
        } else {
            zongHeDownImageView.setImageResource(R.mipmap.up_red_sort);
            if (count % 3 == KA_PIAN) {
                gridGoods.clear();
                getDataFromIntenet(GRID_VIEW_FLAG, searchContent, GOOD_ID_ASC, "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_SMALL) {
                smallListGoods.clear();
                getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, GOOD_ID_ASC, "1", LOAD_COUNT_ONCE_STR);
            } else if (count % 3 == LIST_BIG) {
                bigListGoods.clear();
                getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, GOOD_ID_ASC, "1", LOAD_COUNT_ONCE_STR);
            }
        }
        zongHeFlag++;
    }




    /**
     * 将标签的颜色设置成初始状态
     */
    public void reset(){
        zongHeTextView.setTextColor(getResources().getColor(R.color.myblack));
        xinPinTextView.setTextColor(getResources().getColor(R.color.myblack));
        xiaoLiangTextView.setTextColor(getResources().getColor(R.color.myblack));
        priceTextView.setTextColor(getResources().getColor(R.color.myblack));
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

                MyLog.d(tag,"count="+count);
                if (count % 3 == KA_PIAN) {
                    if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
                            && zongHeFlag % 2 == 1) {
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, GOOD_ID_DESC, loadCount + "", LOAD_COUNT_ONCE_STR);
                        MyLog.d(tag,"执行了卡片");

                    } else if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向上
                            && zongHeFlag % 2 == 0) {
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, GOOD_ID_ASC, loadCount+ "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向下
                            && priceFlag % 2 == 1) {
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, PRICE_DESC, loadCount + "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向上
                            && priceFlag % 2 == 0) {
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, PRICE_ASC, loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (xinPinTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//新品红色
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, XIN_PIN_DESC, loadCount +  "", LOAD_COUNT_ONCE_STR);
                    } else if (xiaoLiangTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//销量红色
                        getDataFromIntenet(GRID_VIEW_FLAG, searchContent, XIAO_LIANG_DESC, loadCount  + "", LOAD_COUNT_ONCE_STR);
                    }
                } else if (count % 3 == LIST_SMALL) {
                    if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
                            && zongHeFlag % 2 == 1) {
                        MyLog.d(tag,"执行了small");
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, GOOD_ID_DESC, loadCount  + "",LOAD_COUNT_ONCE_STR);

                    } else if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向上
                            && zongHeFlag % 2 == 0) {
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, GOOD_ID_ASC, loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向下
                            && priceFlag % 2 == 1) {
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, PRICE_DESC, loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向上
                            && priceFlag % 2 == 0) {
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, PRICE_ASC, loadCount + "", LOAD_COUNT_ONCE_STR);
                    } else if (xinPinTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//新品红色
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, XIN_PIN_DESC, loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (xiaoLiangTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//销量红色
                        getDataFromIntenet(SMALL_LIST_VIEW_FLAG, searchContent, XIAO_LIANG_DESC, loadCount + "", LOAD_COUNT_ONCE_STR);
                    }
                } else if (count % 3 == LIST_BIG) {
                    if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向下
                            && zongHeFlag % 2 == 1) {
                        MyLog.d(tag,"执行了big");
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, GOOD_ID_DESC, loadCount+ "", LOAD_COUNT_ONCE_STR);

                    } else if (zongHeTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//综合红色向上
                            && zongHeFlag % 2 == 0) {
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, GOOD_ID_ASC, loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向下
                            && priceFlag % 2 == 1) {
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, PRICE_DESC, loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (priceTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)//价格红色向上
                            && priceFlag % 2 == 0) {
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, PRICE_ASC, loadCount  + "",LOAD_COUNT_ONCE_STR);
                    } else if (xinPinTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//新品红色
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, XIN_PIN_DESC, loadCount  + "", LOAD_COUNT_ONCE_STR);
                    } else if (xiaoLiangTextView.getCurrentTextColor() == getResources().getColor(R.color.bottom_lable_color)) {//销量红色
                        getDataFromIntenet(BIG_LIST_VIEW_FLAG, searchContent, XIAO_LIANG_DESC, loadCount  + "", LOAD_COUNT_ONCE_STR);
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
        setResult(RESULT_OK, intent);
        finish();
    }
}
