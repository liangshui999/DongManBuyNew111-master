package com.example.asus_cp.dongmanbuy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.ShopStreetShopListAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopStreetSpinnerAdapter;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
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
 * 店铺街的界面
 * Created by asus-cp on 2016-05-19.
 */
public class ShopStreetFragment extends Fragment {
    private Context context;
    private String tag="ShopStreetFragment";
    private Spinner paiLieShunXunSpinner;//排列顺序
    private Spinner productCategorySpinner;//商品类型
    private Spinner shopPostionSpinner;//店铺位置
    private ListView shopListListView;//店铺列表

    private RequestQueue requestQueue;

    private String indexUrl="http://www.zmobuy.com/PHP/?url=/store/index";//店铺分类的url

    public static final int SHU_JI=1;
    public static final int MO_WAN=2;
    public static final int DIY=3;
    public static final int SHANG_ZHUANG=4;
    public static final int XIA_ZHUANG=5;
    public static final int XIANG_BAO=6;
    public static final int ZHAI_PIN=7;
    public static final int MAO_RONG=8;
    public static final int PEI_SHI=9;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.shop_street_fragment_layout,null);
        init(v);
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init(View v) {
        context=getActivity();
        requestQueue= MyApplication.getRequestQueue();
        paiLieShunXunSpinner= (Spinner) v.findViewById(R.id.spin_pai_lie_shun_xu);
        productCategorySpinner= (Spinner) v.findViewById(R.id.spin_product_category);
        shopPostionSpinner= (Spinner) v.findViewById(R.id.spin_shop_street_position);
        shopListListView= (ListView) v.findViewById(R.id.list_view_shop_list);
        List<String> paiLies=new ArrayList<String>();
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

        ShopStreetSpinnerAdapter paiLieAdapter=new ShopStreetSpinnerAdapter(context,paiLies);
        ShopStreetSpinnerAdapter shopPostionAdapter=new ShopStreetSpinnerAdapter(context,shopPostions);
        ShopStreetSpinnerAdapter productCategroyAdapter=new ShopStreetSpinnerAdapter(context,productCategories);

        paiLieShunXunSpinner.setAdapter(paiLieAdapter);
        shopPostionSpinner.setAdapter(shopPostionAdapter);
        productCategorySpinner.setAdapter(productCategroyAdapter);
        productCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MyLog.d(tag, "点击了：" + productCategories.get(position));
                switch (position) {
                    case SHU_JI:
                        moWanClickChuLi(1625 + "");
                        break;
                    case MO_WAN:
                        moWanClickChuLi(1661 + "");
                        break;
                    case DIY:
                        moWanClickChuLi(1647+"");
                        break;
                    case SHANG_ZHUANG:
                        moWanClickChuLi(1464+"");
                        break;
                    case XIA_ZHUANG:
                        moWanClickChuLi(1479+"");
                        break;
                    case XIANG_BAO:
                        moWanClickChuLi(1486+"");
                        break;
                    case ZHAI_PIN:
                        moWanClickChuLi(1492+"");
                        break;
                    case MAO_RONG:
                        moWanClickChuLi(1501+"");
                        break;
                    case PEI_SHI:
                        moWanClickChuLi(1506+"");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        moWanClickChuLi(1661+"");
    }

    /**
     * 点击事件处理
     * @param category 类别的代号，比如书籍是1625
     */
    private void moWanClickChuLi(final String category) {
        StringRequest moWanRequest=new StringRequest(Request.Method.POST, indexUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ShopStreetShopListAdapter shopListAdapter=new ShopStreetShopListAdapter(context,parseJson(s));
                shopListListView.setAdapter(shopListAdapter);
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
     *将json数据解析出来放到集合厘米那
     */
    private List<ShopModel> parseJson(String s) {
        MyLog.d(tag, "书籍返回的数据是" + s);
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
                    shopModels.add(shopModel);
                }
            }
            MyLog.d(tag, "集合的大小:" + shopModels.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopModels;
    }
}
