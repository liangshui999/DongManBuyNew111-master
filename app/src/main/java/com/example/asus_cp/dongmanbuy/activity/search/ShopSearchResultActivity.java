package com.example.asus_cp.dongmanbuy.activity.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.activity.MainActivity;
import com.example.asus_cp.dongmanbuy.adapter.ShopStreetShopListAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopStreetSpinnerAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.db.SearchRecordDBOperateHelper;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
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
 * 店铺的搜索结果
 * Created by asus-cp on 2016-06-30.
 */
public class ShopSearchResultActivity extends BaseActivity implements View.OnClickListener{

    private String tag="ShopSearchResultActivity";

   // private Spinner paiLieShunXunSpinner;//排列顺序
    //private Spinner productCategorySpinner;//商品类型
    //private Spinner shopPostionSpinner;//店铺位置
    private ImageView daoHangImageView;
    private ImageView searchImageView;
    private PullToRefreshListView shopListListView;//店铺列表

    private String indexUrl="http://www.zmobuy.com/PHP/?url=/store/index";//店铺分类的url

    public static final int ALL=0;
    public static final int SHU_JI=1;
    public static final int MO_WAN=2;
    public static final int DIY=3;
    public static final int SHANG_ZHUANG=4;
    public static final int XIA_ZHUANG=5;
    public static final int XIANG_BAO=6;
    public static final int ZHAI_PIN=7;
    public static final int MAO_RONG=8;
    public static final int PEI_SHI=9;

    private ShopStreetShopListAdapter allAdapter;
    private ShopStreetShopListAdapter shuJiAdapter;
    private ShopStreetShopListAdapter moWanAdapter;
    private ShopStreetShopListAdapter DIYAdapter;
    private ShopStreetShopListAdapter shangZhuangAdapter;
    private ShopStreetShopListAdapter xiaZhuangAdapter;
    private ShopStreetShopListAdapter xiangBaoAdapter;
    private ShopStreetShopListAdapter zhaiPinAdapter;
    private ShopStreetShopListAdapter maoRongAdapter;
    private ShopStreetShopListAdapter peiShiAdapter;


    private List<ShopModel> allModles=new ArrayList<ShopModel>();
    private List<ShopModel> shuJiModles=new ArrayList<ShopModel>();
    private List<ShopModel> moWanModles=new ArrayList<ShopModel>();
    private List<ShopModel> DIYModles=new ArrayList<ShopModel>();
    private List<ShopModel> shangZhuangModles=new ArrayList<ShopModel>();
    private List<ShopModel> xiaZhuangModles=new ArrayList<ShopModel>();
    private List<ShopModel> xiangBaoModles=new ArrayList<ShopModel>();
    private List<ShopModel> zhaiPinModles=new ArrayList<ShopModel>();
    private List<ShopModel> maoRongModles=new ArrayList<ShopModel>();
    private List<ShopModel> peiShiModles=new ArrayList<ShopModel>();


    private int count0=1;
    private int count1=1;
    private int count2=1;
    private int count3=1;
    private int count4=1;
    private int count5=1;
    private int count6=1;
    private int count7=1;
    private int count8=1;
    private int count9=1;
    private int count10=1;

    private int location;//用于上拉加载的判断

    private String searchContent;//传递过来的搜索关键词

    private ShopStreetShopListAdapter searchResultAdapter;//搜索结果的适配器
    private List<ShopModel> searchResultModels=new ArrayList<ShopModel>();

    private SearchRecordDBOperateHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_search_result_activity_layout);
        init();
    }

    @Override
    public void initView() {

    }

    /**
     * 初始化的方法
     */
    private void init() {

        searchContent=getIntent().getStringExtra(MyConstant.SEARCH_CONTENT_KEY);
        dbHelper=new SearchRecordDBOperateHelper();
        if(dbHelper.queryByKeyWord(searchContent)){
            dbHelper.deleteByKeyWord(searchContent);
            dbHelper.insert(searchContent);
        }else{
            dbHelper.insert(searchContent);
        }


        daoHangImageView= (ImageView) findViewById(R.id.img_dao_hang_search_result);
        searchImageView= (ImageView) findViewById(R.id.img_search_search_result);
        //paiLieShunXunSpinner= (Spinner) findViewById(R.id.spin_pai_lie_shun_xu_search_result);
        //productCategorySpinner= (Spinner) findViewById(R.id.spin_product_category_search_result);
        //shopPostionSpinner= (Spinner) findViewById(R.id.spin_shop_street_position_search_result);
        shopListListView= (PullToRefreshListView) findViewById(R.id.list_view_shop_list_search_result);
        shopListListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//设置模式是上拉加载
        shopListListView.setOnRefreshListener(new MyOnrefreshListener());
        DialogHelper.showDialog(ShopSearchResultActivity.this);
        searchResultDisplay();

        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        searchImageView.setOnClickListener(this);


       /* List<String> paiLies=new ArrayList<String>();
        paiLies.add("排列顺序");
        List<String> shopPostions=new ArrayList<String>();
        shopPostions.add("店铺位置");
        final List<String> productCategories=new ArrayList<String>();
        productCategories.add("商品类型");
        productCategories.add("书籍");
        productCategories.add("模玩 手办");
        productCategories.add("DIY定制");
        productCategories.add("上装");
        productCategories.add("下装");
        productCategories.add("箱包");
        productCategories.add("宅品");
        productCategories.add("毛绒");
        productCategories.add("配饰");

        ShopStreetSpinnerAdapter paiLieAdapter=new ShopStreetSpinnerAdapter(ShopSearchResultActivity.this,paiLies);
        ShopStreetSpinnerAdapter shopPostionAdapter=new ShopStreetSpinnerAdapter(ShopSearchResultActivity.this,shopPostions);
        ShopStreetSpinnerAdapter productCategroyAdapter=new ShopStreetSpinnerAdapter(ShopSearchResultActivity.this,productCategories);

        paiLieShunXunSpinner.setAdapter(paiLieAdapter);
        shopPostionSpinner.setAdapter(shopPostionAdapter);
        productCategorySpinner.setAdapter(productCategroyAdapter);
        productCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //MyLog.d(tag, "点击了：" + productCategories.get(position));
                switch (position) {
                    case ALL:
//                        tongYongClickChuLi("", ALL);
//                        location = position;
//                        count0 = 1;//将count设为初始值
                        break;
                    case SHU_JI:
                        DialogHelper.showDialog(ShopSearchResultActivity.this);
                        tongYongClickChuLi(1625 + "", SHU_JI);
                        location = position;
                        count1 = 1;
                        break;
                    case MO_WAN:
                        DialogHelper.showDialog(ShopSearchResultActivity.this);
                        tongYongClickChuLi(1661 + "", MO_WAN);
                        location = position;
                        count2 = 1;
                        break;
                    case DIY:
                        DialogHelper.showDialog(ShopSearchResultActivity.this);
                        tongYongClickChuLi(1647 + "", DIY);
                        location = position;
                        count3 = 1;
                        break;
                    case SHANG_ZHUANG:
                        DialogHelper.showDialog(ShopSearchResultActivity.this);
                        tongYongClickChuLi(1464 + "", SHANG_ZHUANG);
                        location = position;
                        count4 = 1;
                        break;
                    case XIA_ZHUANG:
                        DialogHelper.showDialog(ShopSearchResultActivity.this);
                        tongYongClickChuLi(1479 + "", XIA_ZHUANG);
                        location = position;
                        count5 = 1;
                        break;
                    case XIANG_BAO:
                        DialogHelper.showDialog(ShopSearchResultActivity.this);
                        tongYongClickChuLi(1486 + "", XIANG_BAO);
                        location = position;
                        count6 = 1;
                        break;
                    case ZHAI_PIN:
                        DialogHelper.showDialog(ShopSearchResultActivity.this);
                        tongYongClickChuLi(1492 + "", ZHAI_PIN);
                        location = position;
                        count7 = 1;
                        break;
                    case MAO_RONG:
                        DialogHelper.showDialog(ShopSearchResultActivity.this);
                        tongYongClickChuLi(1501 + "", MAO_RONG);
                        location = position;
                        count8 = 1;
                        break;
                    case PEI_SHI:
                        DialogHelper.showDialog(ShopSearchResultActivity.this);
                        tongYongClickChuLi(1506 + "", PEI_SHI);
                        location = position;
                        count9 = 1;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        //tongYongClickChuLi("",ALL);//初始状态


    }



    /**
     * 搜索结果的展示
     */
    private void searchResultDisplay() {
        StringRequest initRequest=new StringRequest(Request.Method.POST, indexUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogHelper.dissmisDialog();
                        searchResultModels.clear();
                        searchResultModels.addAll(parseJson(s));
                        searchResultAdapter=new ShopStreetShopListAdapter(ShopSearchResultActivity.this,searchResultModels);
                        shopListListView.setAdapter(searchResultAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                String json="{\"page\":\"1\",\"where\":\""+searchContent+"\",\"type\":\"2\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(initRequest);
    }


    /**
     * 搜索结果的上拉加载处理
     */
    public void searchResultUpLoad(final String page){
        StringRequest initRequest=new StringRequest(Request.Method.POST, indexUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(parseJson(s)==null || parseJson(s).size()==0){
                            Toast.makeText(ShopSearchResultActivity.this, "已经是最后一项了", Toast.LENGTH_SHORT).show();
                            shopListListView.onRefreshComplete();//通知刷新完毕
                        }else{
                            searchResultModels.addAll(parseJson(s));
                            searchResultAdapter.notifyDataSetChanged();
                            shopListListView.onRefreshComplete();//通知刷新完毕
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                MyLog.d(tag,"page="+page+"......"+"searchContent="+searchContent);
                Map<String,String> map=new HashMap<String,String>();
                String json="{\"page\":\""+page+"\",\"where\":\""+searchContent+"\",\"type\":\"2\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(initRequest);
    }

    /**
     * 点击事件处理,主要用于点击
     * @param category 类别的代号，比如书籍是1625
     */
    private void tongYongClickChuLi(final String category, final int position) {
        StringRequest moWanRequest=new StringRequest(Request.Method.POST, indexUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                switch (position) {
                    case ALL:
                        DialogHelper.dissmisDialog();
                        allModles.clear();
                        allModles.addAll(parseJson(s));
                        allAdapter=new ShopStreetShopListAdapter(ShopSearchResultActivity.this,allModles);
                        shopListListView.setAdapter(allAdapter);
                        break;
                    case SHU_JI:
                        DialogHelper.dissmisDialog();
                        shuJiModles.clear();
                        shuJiModles.addAll(parseJson(s));
                        shuJiAdapter=new ShopStreetShopListAdapter(ShopSearchResultActivity.this,shuJiModles);
                        shopListListView.setAdapter(shuJiAdapter);
                        break;
                    case MO_WAN:
                        DialogHelper.dissmisDialog();
                        moWanModles.clear();
                        moWanModles.addAll(parseJson(s));
                        moWanAdapter=new ShopStreetShopListAdapter(ShopSearchResultActivity.this,moWanModles);
                        shopListListView.setAdapter(moWanAdapter);
                        break;
                    case DIY:
                        DialogHelper.dissmisDialog();
                        DIYModles.clear();
                        DIYModles.addAll(parseJson(s));
                        DIYAdapter=new ShopStreetShopListAdapter(ShopSearchResultActivity.this,DIYModles);
                        shopListListView.setAdapter(DIYAdapter);
                        break;
                    case SHANG_ZHUANG:
                        DialogHelper.dissmisDialog();
                        shangZhuangModles.clear();
                        shangZhuangModles.addAll(parseJson(s));
                        shangZhuangAdapter=new ShopStreetShopListAdapter(ShopSearchResultActivity.this,shangZhuangModles);
                        shopListListView.setAdapter(shuJiAdapter);
                        break;
                    case XIA_ZHUANG:
                        DialogHelper.dissmisDialog();
                        xiaZhuangModles.clear();
                        xiaZhuangModles.addAll(parseJson(s));
                        xiaZhuangAdapter=new ShopStreetShopListAdapter(ShopSearchResultActivity.this,xiaZhuangModles);
                        shopListListView.setAdapter(xiaZhuangAdapter);
                        break;
                    case XIANG_BAO:
                        DialogHelper.dissmisDialog();
                        xiangBaoModles.clear();
                        xiangBaoModles.addAll(parseJson(s));
                        xiangBaoAdapter=new ShopStreetShopListAdapter(ShopSearchResultActivity.this,xiangBaoModles);
                        shopListListView.setAdapter(xiangBaoAdapter);
                        break;
                    case ZHAI_PIN:
                        DialogHelper.dissmisDialog();
                        zhaiPinModles.clear();
                        zhaiPinModles.addAll(parseJson(s));
                        zhaiPinAdapter=new ShopStreetShopListAdapter(ShopSearchResultActivity.this,zhaiPinModles);
                        shopListListView.setAdapter(zhaiPinAdapter);
                        break;
                    case MAO_RONG:
                        DialogHelper.dissmisDialog();
                        maoRongModles.clear();
                        maoRongModles.addAll(parseJson(s));
                        maoRongAdapter=new ShopStreetShopListAdapter(ShopSearchResultActivity.this,maoRongModles);
                        shopListListView.setAdapter(maoRongAdapter);
                        break;
                    case PEI_SHI:
                        DialogHelper.dissmisDialog();
                        peiShiModles.clear();
                        peiShiModles.addAll(parseJson(s));
                        peiShiAdapter=new ShopStreetShopListAdapter(ShopSearchResultActivity.this,peiShiModles);
                        shopListListView.setAdapter(peiShiAdapter);
                        break;
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
                String json="{\"page\":\"1\",\"where\":\""+category+"\",\"type\":\"1\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(moWanRequest);
    }

    /**
     * 主要用于上拉加载
     * @param category
     * @param page
     */
    private void tongYongClickChuLi(final String category, final String page){
        StringRequest moWanRequest=new StringRequest(Request.Method.POST, indexUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag,"category="+category+"page="+page+"s="+s);
                if(parseJson(s)==null || parseJson(s).size()==0){
                    Toast.makeText(ShopSearchResultActivity.this, "已经是最后一项了", Toast.LENGTH_SHORT).show();
                    shopListListView.onRefreshComplete();//通知刷新完毕
                }else{
                    switch (location) {
                        case ALL:
                            allModles.addAll(parseJson(s));
                            allAdapter.notifyDataSetChanged();
                            break;
                        case SHU_JI:
                            shuJiModles.addAll(parseJson(s));
                            shuJiAdapter.notifyDataSetChanged();
                            break;
                        case MO_WAN:
                            moWanModles.addAll(parseJson(s));
                            moWanAdapter.notifyDataSetChanged();
                            break;
                        case DIY:
                            DIYModles.addAll(parseJson(s));
                            DIYAdapter.notifyDataSetChanged();
                            break;
                        case SHANG_ZHUANG:
                            shangZhuangModles.addAll(parseJson(s));
                            shangZhuangAdapter.notifyDataSetChanged();
                            break;
                        case XIA_ZHUANG:
                            xiaZhuangModles.addAll(parseJson(s));
                            xiaZhuangAdapter.notifyDataSetChanged();
                            break;
                        case XIANG_BAO:
                            xiangBaoModles.addAll(parseJson(s));
                            xiangBaoAdapter.notifyDataSetChanged();
                            break;
                        case ZHAI_PIN:
                            zhaiPinModles.addAll(parseJson(s));
                            zhaiPinAdapter.notifyDataSetChanged();
                            break;
                        case MAO_RONG:
                            maoRongModles.addAll(parseJson(s));
                            maoRongAdapter.notifyDataSetChanged();
                            break;
                        case PEI_SHI:
                            peiShiModles.addAll(parseJson(s));
                            peiShiAdapter.notifyDataSetChanged();
                            break;
                    }
                    shopListListView.onRefreshComplete();//通知刷新完毕
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
                String json="{\"page\":\""+page+"\",\"where\":\""+category+"\",\"type\":\"1\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(moWanRequest);
    }

    /**
     *将json数据解析出来放到集合厘米那
     */
    private List<ShopModel> parseJson(String s) {
        //MyLog.d(tag, "书籍返回的数据是" + s);
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
                    shopModel.setGoods(goods);
                }
                shopModels.add(shopModel);
            }
            //MyLog.d(tag, "集合的大小:" + shopModels.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopModels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_dao_hang_search_result://点击了导航
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.img_search_search_result://点击了搜索框
                finish();
                break;
        }
    }


    /**
     * 自定义的刷新监听器
     */
    class MyOnrefreshListener implements PullToRefreshBase.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatedDate=simpleDateFormat.format(new Date());
            // Update the LastUpdatedLabel
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(formatedDate);
            PullToRefreshBase.Mode mode= shopListListView.getCurrentMode();//注意是currentmode，不是mode
            if(mode== PullToRefreshBase.Mode.PULL_FROM_START){      //下拉刷新
                refreshView.getLoadingLayoutProxy().setPullLabel("下拉可以刷新");
                refreshView.getLoadingLayoutProxy().setReleaseLabel("释放刷新");
                refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新...");

            }else if(mode== PullToRefreshBase.Mode.PULL_FROM_END){      //向上加载
                refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在加载...");
                refreshView.getLoadingLayoutProxy().setPullLabel("上拉可以加载");
                refreshView.getLoadingLayoutProxy().setReleaseLabel("释放刷新");
                count0++;
                count1++;
                count2++;
                count3++;
                count4++;
                count5++;
                count6++;
                count7++;
                count8++;
                count9++;
                count10++;
                switch (location) {
                    case ALL:
                        //tongYongClickChuLi("", count0 + "");
                        break;
                    case SHU_JI:
                        tongYongClickChuLi(1625+"", count1 + "");
                        break;
                    case MO_WAN:
                        tongYongClickChuLi(1661+"", count2 + "");
                        break;
                    case DIY:
                        tongYongClickChuLi(1647+"", count3 + "");
                        break;
                    case SHANG_ZHUANG:
                        tongYongClickChuLi(1464+"", count4 + "");
                        break;
                    case XIA_ZHUANG:
                        tongYongClickChuLi(1479+"", count5 + "");
                        break;
                    case XIANG_BAO:
                        tongYongClickChuLi(1486+"", count6+ "");
                        break;
                    case ZHAI_PIN:
                        tongYongClickChuLi(1492+"", count7 + "");
                        break;
                    case MAO_RONG:
                        tongYongClickChuLi(1501+"", count8 + "");
                        break;
                    case PEI_SHI:
                        tongYongClickChuLi(1506+"", count9 + "");
                        break;
                }
                searchResultUpLoad(count10+"");
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
