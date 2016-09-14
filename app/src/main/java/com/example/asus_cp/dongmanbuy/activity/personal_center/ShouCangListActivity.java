package com.example.asus_cp.dongmanbuy.activity.personal_center;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.adapter.ShouCangListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
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
 * 收藏列表
 * Created by asus-cp on 2016-06-23.
 */
public class ShouCangListActivity extends BaseActivity {

    private String tag="ShouCangListActivity";

    private String shouCangListUrl="http://www.zmobuy.com/PHP/?url=/user/collect/list";//获取收藏列表

    private ListView listView;

    private LinearLayout noContentLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.shou_cang_list);
        setContentLayout(R.layout.shou_cang_list_activity_layout);
        initView();
        init();
    }

    @Override
    public void initView() {
        listView= (ListView) findViewById(R.id.list_shou_cang);
        noContentLinearLayout= (LinearLayout) findViewById(R.id.ll_no_content_shou_cang);
    }

    private void init() {

        //弹出正在加载的对话框
        DialogHelper.showDialog(this);

        //获取收藏列表
        StringRequest getShouCangListRequest=new StringRequest(Request.Method.POST, shouCangListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogHelper.dissmisDialog();//关闭正在加载的对话框
                        final List<Good> goods=parseJson(s);
                        if(goods.size()>0){
                            noContentLinearLayout.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            ShouCangListAdapter adapter=new ShouCangListAdapter(ShouCangListActivity.this,goods);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent=new Intent(ShouCangListActivity.this, ProductDetailActivity.class);
                                    intent.putExtra(MyConstant.GOOD_KEY,goods.get(position));
                                    startActivity(intent);
                                }
                            });
                        }else{
                            noContentLinearLayout.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
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
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"pagination\":{\"page\":\""+"1"+"\",\"count\":\""+"50"+"\"}}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(getShouCangListRequest);
    }


    /**
     * 解析json数据
     * @param s
     */
    private List<Good> parseJson(String s) {
        MyLog.d(tag, "返回的数据是" + s);
        List<Good> goods=new ArrayList<Good>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject ziJsObj=jsonArray.getJSONObject(i);
                Good good=new Good();
                good.setGoodId(ziJsObj.getString("goods_id"));
                good.setSalesVolume(ziJsObj.getString("sales_volume"));
                good.setGoodName(JsonHelper.decodeUnicode(ziJsObj.getString("name")));
                good.setMarket_price(JsonHelper.decodeUnicode(ziJsObj.getString("market_price")));
                good.setShopPrice(JsonHelper.decodeUnicode(ziJsObj.getString("shop_price")));
                good.setPromotePrice(JsonHelper.decodeUnicode(ziJsObj.getString("promote_price")));
                JSONObject picJs=ziJsObj.getJSONObject("img");
                good.setGoodsThumb(picJs.getString("thumb"));
                good.setGoodsImg(picJs.getString("url"));
                good.setGoodsSmallImag(picJs.getString("small"));
                good.setRecId(ziJsObj.getString("rec_id"));
                goods.add(good);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goods;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
    }
}
