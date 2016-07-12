package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.DingDanListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.DingDanModel;
import com.example.asus_cp.dongmanbuy.model.Good;
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
 * 订单列表的activity
 * Created by asus-cp on 2016-06-20.
 */
public class DingDanListActivity extends Activity implements View.OnClickListener{

    private String tag="DingDanListActivity";

    private LinearLayout allDingDanLinearLayout;//所有订单
    private LinearLayout daiFuKuanDingDanLinearLayout;//待付款订单
    private LinearLayout daiShouHuoDingDanLinearLayout;//待收货订单
    private ListView dingDanListListView;//订单列表

    private TextView allDingDanTextView;
    private TextView daiFuKuanDingDanTextView;
    private TextView daiShouHuoDingDanTextView;


    private String dingDanListUrl="http://www.zmobuy.com/PHP/?url=/order/list";//订单列表的接口

    private RequestQueue requestQueue;

    private String uid;
    private String sid;

    private String whoStartMe;//谁开启了我

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ding_dan_list_activty_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {

        whoStartMe=getIntent().getStringExtra(MyConstant.TO_DING_DAN_LIST_KEY);
        requestQueue= MyApplication.getRequestQueue();

        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);

        allDingDanLinearLayout= (LinearLayout) findViewById(R.id.ll_all_ding_dan);
        daiFuKuanDingDanLinearLayout= (LinearLayout) findViewById(R.id.ll_dai_fu_kuan_ding_dan);
        daiShouHuoDingDanLinearLayout= (LinearLayout) findViewById(R.id.ll_dai_shou_huo_ding_dan);
        dingDanListListView= (ListView) findViewById(R.id.list_view_ding_dan_list);
        allDingDanTextView= (TextView) findViewById(R.id.text_all_ding_dan);
        daiFuKuanDingDanTextView= (TextView) findViewById(R.id.text_dai_fu_kuan_ding_dan);
        daiShouHuoDingDanTextView= (TextView) findViewById(R.id.text_dai_shou_huo_ding_dan);

        switch (whoStartMe){
            case MyConstant.ALL_DING_DAN:
                allDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                getOrderList("");//获取所有的订单数据
                break;
            case MyConstant.DAI_FU_KUAN_DING_DAN:
                getOrderList("await_pay");//获取待付款的订单数据
                daiFuKuanDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case MyConstant.DAI_SHOU_HUO_DING_DAN:
                getOrderList("await_ship");//获取代收货的订单数据
                daiShouHuoDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case MyConstant.DAI_PING_JIA_DING_DAN:
                break;

        }


        //设置点击事件
        allDingDanLinearLayout.setOnClickListener(this);
        daiFuKuanDingDanLinearLayout.setOnClickListener(this);
        daiShouHuoDingDanLinearLayout.setOnClickListener(this);



    }


    /**
     * 获取订单列表
     * @param type 订单类型
     */
    private void getOrderList(final String type) {
        //请求获取所有的订单
        StringRequest allDingDanRequest=new StringRequest(Request.Method.POST, dingDanListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "返回的数据是" + s);
//                        MyLog.d(tag,"uid="+uid);
//                        MyLog.d(tag,"sid="+sid);
                        final List<DingDanModel> dingDanModels=new ArrayList<DingDanModel>();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            if(jsonArray!=null){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject ziJsObj=jsonArray.getJSONObject(i);
                                    DingDanModel model=new DingDanModel();
                                    model.setOrderId(ziJsObj.getString("order_id"));
                                    model.setOrderBianHao(ziJsObj.getString("order_sn"));
                                    model.setOrderTime(ziJsObj.getString("order_time"));
                                    model.setSumPrice(ziJsObj.getString("total_fee"));

                                    List<Good> goods=new ArrayList<Good>();
                                    JSONArray goodArray=null;
                                    try{
                                        goodArray=ziJsObj.getJSONArray("goods_list");
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    if(goodArray!=null){
                                        for(int j=0;j<goodArray.length();j++){
                                            JSONObject goodJs=goodArray.getJSONObject(j);
                                            Good good=new Good();
                                            good.setGoodId(goodJs.getString("goods_id"));
                                            good.setGoodName(JsonHelper.decodeUnicode(goodJs.getString("name")));
                                            good.setDingDanNumber(goodJs.getString("goods_number"));
                                            good.setDingDanSumPrice(JsonHelper.decodeUnicode(goodJs.getString("subtotal")));
                                            JSONObject imgJs=goodJs.getJSONObject("img");
                                            good.setGoodsImg(imgJs.getString("url"));
                                            good.setGoodsThumb(imgJs.getString("thumb"));
                                            good.setGoodsSmallImag(imgJs.getString("small"));

                                            goods.add(good);
                                        }
                                    }
                                    model.setGoods(goods);
                                    dingDanModels.add(model);
                                }
                                DingDanListAdapter dingDanListAdapter=new DingDanListAdapter(DingDanListActivity.this,dingDanModels);
                                dingDanListListView.setAdapter(dingDanListAdapter);

                                dingDanListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent=new Intent(DingDanListActivity.this, DingDanDetailActivity.class);
                                        intent.putExtra(MyConstant.DING_DAN_MODEL_KEY, dingDanModels.get(position));
                                        startActivity(intent);
                                    }
                                });
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
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"pagination\":{\"page\":\"1\",\"count\":\"100\"},\"type\":\""+type+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(allDingDanRequest);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_all_ding_dan://点击了所有订单
                getOrderList("");//获取所有的订单数据
                reset();
                allDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case R.id.ll_dai_fu_kuan_ding_dan://点击了待付款订单
                getOrderList("await_pay");//获取待付款的订单数据
                reset();
                daiFuKuanDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case R.id.ll_dai_shou_huo_ding_dan://点击了代收货订单
                getOrderList("await_ship");//获取代收货的订单数据
                reset();
                daiShouHuoDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
        }
    }

    /**
     * 将所有标签的颜色都设置成黑色
     */
    public void reset(){
        allDingDanTextView.setTextColor(getResources().getColor(R.color.black));
        daiFuKuanDingDanTextView.setTextColor(getResources().getColor(R.color.black));
        daiShouHuoDingDanTextView.setTextColor(getResources().getColor(R.color.black));
    }
}
