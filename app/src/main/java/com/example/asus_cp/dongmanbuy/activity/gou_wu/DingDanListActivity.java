package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.adapter.DingDanListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.DingDanModel;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
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
public class DingDanListActivity extends BaseActivity implements View.OnClickListener{

    private String tag="DingDanListActivity";

    private LinearLayout allDingDanLinearLayout;//所有订单
    private LinearLayout daiFuKuanDingDanLinearLayout;//待付款订单
    private LinearLayout daiShouHuoDingDanLinearLayout;//待收货订单
    private ListView dingDanListListView;//订单列表

    private TextView allDingDanTextView;
    private TextView daiFuKuanDingDanTextView;
    private TextView daiShouHuoDingDanTextView;

    private String dingDanListUrl="http://mv.zmobuy.com/order/show.action";//订单列表的接口

    private String whoStartMe;//谁开启了我

    public static final int REQUEST_DING_DAN_DETAIL_KEY_ALL =1;//跳转到订单detail界面
    public static final int REQUEST_DING_DAN_DETAIL_KEY_AWAIT_PAY =2;
    public static final int REQUEST_DING_DAN_DETAIL_KEY_AWAIT_SHIP =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ding_dan_list_activty_layout);
        setTitle(R.string.order_list);
        initView();
        init();
    }

    @Override
    public void initView() {
        allDingDanLinearLayout= (LinearLayout) findViewById(R.id.ll_all_ding_dan);
        daiFuKuanDingDanLinearLayout= (LinearLayout) findViewById(R.id.ll_dai_fu_kuan_ding_dan);
        daiShouHuoDingDanLinearLayout= (LinearLayout) findViewById(R.id.ll_dai_shou_huo_ding_dan);
        dingDanListListView= (ListView) findViewById(R.id.list_view_ding_dan_list);
        allDingDanTextView= (TextView) findViewById(R.id.text_all_ding_dan);
        daiFuKuanDingDanTextView= (TextView) findViewById(R.id.text_dai_fu_kuan_ding_dan);
        daiShouHuoDingDanTextView= (TextView) findViewById(R.id.text_dai_shou_huo_ding_dan);

        //设置点击事件
        allDingDanLinearLayout.setOnClickListener(this);
        daiFuKuanDingDanLinearLayout.setOnClickListener(this);
        daiShouHuoDingDanLinearLayout.setOnClickListener(this);
    }

    /**
     * 初始化的方法
     */
    private void init() {

        whoStartMe=getIntent().getStringExtra(MyConstant.TO_DING_DAN_LIST_KEY);
        switch (whoStartMe){
            case MyConstant.ALL_DING_DAN:
                //弹出正在加载的对话框
                DialogHelper.showDialog(this);
                allDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                getOrderList("");//获取所有的订单数据
                break;
            case MyConstant.DAI_FU_KUAN_DING_DAN:
                //弹出正在加载的对话框
                DialogHelper.showDialog(this);
                getOrderList("await_pay");//获取待付款的订单数据
                daiFuKuanDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case MyConstant.DAI_SHOU_HUO_DING_DAN:
                //弹出正在加载的对话框
                DialogHelper.showDialog(this);
                getOrderList("await_ship");//获取代收货的订单数据
                daiShouHuoDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case MyConstant.DAI_PING_JIA_DING_DAN:
                break;

        }
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
                        DialogHelper.dissmisDialog();
                        MyLog.d(tag, "返回的数据是" + s);
                        final List<DingDanModel> dingDanModels = parseJson(s);

                        //清除掉空的订单信息（清除那些订单里面没有商品的订单）
                       /* final List<DingDanModel> tempModels=new ArrayList<DingDanModel>();
                        for(int i=0;i<dingDanModels.size();i++){
                            if(dingDanModels.get(i).getGoods().size()>0){
                                tempModels.add(dingDanModels.get(i));
                            }
                        }*/
                        final List<DingDanModel> tempModels=dingDanModels;
                        DingDanListAdapter dingDanListAdapter=new DingDanListAdapter(DingDanListActivity.this,tempModels);
                        dingDanListListView.setAdapter(dingDanListAdapter);

                        dingDanListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                if(tempModels.get(position).getGoods()!=null && tempModels.get(position).getGoods().size()>0){
                                    Intent intent=new Intent(DingDanListActivity.this, DingDanDetailActivity.class);
                                    intent.putExtra(MyConstant.DING_DAN_MODEL_KEY, tempModels.get(position));
                                    if(allDingDanTextView.getCurrentTextColor()==getResources().getColor(R.color.bottom_lable_color)){

                                        startActivityForResult(intent, REQUEST_DING_DAN_DETAIL_KEY_ALL);

                                    }else if(daiFuKuanDingDanTextView.getCurrentTextColor()==getResources().getColor(R.color.bottom_lable_color)){

                                        startActivityForResult(intent, REQUEST_DING_DAN_DETAIL_KEY_AWAIT_PAY);

                                    }else if(daiShouHuoDingDanTextView.getCurrentTextColor()==getResources().getColor(R.color.bottom_lable_color)){

                                        startActivityForResult(intent, REQUEST_DING_DAN_DETAIL_KEY_AWAIT_SHIP);

                                    }
                                }else{
                                    Toast.makeText(DingDanListActivity.this,"没有商品哦",Toast.LENGTH_SHORT).show();
                                }


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
                Map<String,String> map=new HashMap<String,String>();
//                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"pagination\":{\"page\":\"1\",\"count\":\"100\"},\"type\":\""+type+"\"}";
//                map.put("json",json);
                map.put("user_id",uid);
                return map;
            }
        };
        requestQueue.add(allDingDanRequest);
    }


    /**
     * 解析json数据
     * @param s
     * @return
     */
    @NonNull
    private List<DingDanModel> parseJson(String s) {
        final List<DingDanModel> dingDanModels=new ArrayList<DingDanModel>();
        try {
//            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject ziJsObj = jsonArray.getJSONObject(i);
                DingDanModel model = new DingDanModel();
                model.setShopName(JsonHelper.decodeUnicode(ziJsObj.getString("goods_shop_name")));
                model.setOrderId(ziJsObj.getString("order_id"));
                model.setOrderBianHao(ziJsObj.getString("order_sn"));
                model.setOrderTime(ziJsObj.getString("order_time"));
                model.setSumPrice(ziJsObj.getString("total_fee"));
                List<Good> goods = new ArrayList<Good>();
                JSONArray goodArray = null;
                try {
                    goodArray = ziJsObj.getJSONArray("goods_list");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (goodArray != null) {
                    for (int j = 0; j < goodArray.length(); j++) {
                        JSONObject goodJs = goodArray.getJSONObject(j);
                        Good good = new Good();
                        good.setGoodId(goodJs.getString("goods_id"));
                        good.setGoodName(JsonHelper.decodeUnicode(goodJs.getString("name")));
                        good.setDingDanNumber(goodJs.getString("goods_number"));
                        good.setDingDanSumPrice(JsonHelper.decodeUnicode(goodJs.getString("subtotal")));
                        JSONObject imgJs = goodJs.getJSONObject("img");
                        good.setGoodsImg(imgJs.getString("url"));
                        good.setGoodsThumb(imgJs.getString("thumb"));
                        good.setGoodsSmallImag(imgJs.getString("small"));
                        goods.add(good);
                    }
                }
                model.setGoods(goods);
                dingDanModels.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dingDanModels;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_all_ding_dan://点击了所有订单
                allDingDanClickChuLi();
                break;
            case R.id.ll_dai_fu_kuan_ding_dan://点击了待付款订单
                daiFuKuanClickChuLi();
                break;
            case R.id.ll_dai_shou_huo_ding_dan://点击了代收货订单
                daiShouHuoClickChuLi();
                break;
        }
    }

    /**
     * 待收货的点击事件处理
     */
    private void daiShouHuoClickChuLi() {
        getOrderList("await_ship");//获取代收货的订单数据
        reset();
        daiShouHuoDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
    }


    /**
     * 待付款的点击事件处理
     */
    private void daiFuKuanClickChuLi() {
        getOrderList("await_pay");//获取待付款的订单数据
        reset();
        daiFuKuanDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
    }


    /**
     * 所有订单的点击事件处理
     */
    private void allDingDanClickChuLi() {
        getOrderList("");//获取所有的订单数据
        reset();
        allDingDanTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
    }

    /**
     * 将所有标签的颜色都设置成黑色
     */
    public void reset(){
        allDingDanTextView.setTextColor(getResources().getColor(R.color.myblack));
        daiFuKuanDingDanTextView.setTextColor(getResources().getColor(R.color.myblack));
        daiShouHuoDingDanTextView.setTextColor(getResources().getColor(R.color.myblack));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_DING_DAN_DETAIL_KEY_ALL:
                if(resultCode==RESULT_OK){
                    allDingDanClickChuLi();
                }
                break;
            case REQUEST_DING_DAN_DETAIL_KEY_AWAIT_PAY:
                if(resultCode==RESULT_OK){
                    daiFuKuanClickChuLi();
                }
                break;
            case REQUEST_DING_DAN_DETAIL_KEY_AWAIT_SHIP:
                if(resultCode==RESULT_OK){
                    daiShouHuoClickChuLi();
                }
                break;
        }
    }
}
