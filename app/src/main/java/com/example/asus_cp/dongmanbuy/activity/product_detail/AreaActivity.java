package com.example.asus_cp.dongmanbuy.activity.product_detail;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.ShengMingListViewAdapter;
import com.example.asus_cp.dongmanbuy.adapter.XianQuMingAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.AreaModel;
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
import java.util.Set;

/**
 * 所在地区的界面
 * Created by asus-cp on 2016-06-03.
 */
public class AreaActivity extends Activity {
    private String tag = "AreaActivity";

    private RelativeLayout closeReativeLayout;//关闭按钮
    private ListView shengListView;//省名列表
    private ExpandableListView shiExapanListView;//市名列表
    private RequestQueue requestQueue;

    private List<String> shengMings;//省名列表
    private List<String> shiMings;//市名列表
    private Map<String,List<String>> shengAndShis;//省名和市名
    private Map<String, List<String>> shiMingAndXianMings;//市名和县名

    //------------实体的集合--------------------------
    private List<AreaModel> shengShiTis;//省的实体的集合
    private Map<String,List<AreaModel>> shengAndShiShiTi;//省和市的实体的对应关系
    private Map<String,List<AreaModel>> shiAndXianShiTi;//市和县的实体对应关系

    public static final String SHI_MING_KEY = "shiMingKey";//向商品详情返回数据时用

    public static final String XIAN_MING_KEY = "xianMingKey";//向商品详情返回数据时用

    private SharedPreferences sharedPreferences;

    private String uid;

    private String sid;

    private String adrressUrl = "http://www.zmobuy.com/PHP/index.php?url=/region";//地址列表的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.area_activity_layout);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        requestQueue = MyApplication.getRequestQueue();
        sharedPreferences = getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, MODE_APPEND);
        uid = sharedPreferences.getString(MyConstant.UID_KEY, null);
        sid = sharedPreferences.getString(MyConstant.SID_KEY, null);

        shengShiTis=new ArrayList<AreaModel>();
        shengAndShiShiTi=new HashMap<String,List<AreaModel>>();
        shiAndXianShiTi=new HashMap<String,List<AreaModel>>();
        StringRequest addressListRequest = new StringRequest(Request.Method.POST, adrressUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                jsonParse(s, shengShiTis);//解析出来省的实体数据

                //------------------将省实体遍历出来--------------------------
                for(final AreaModel areaModel:shengShiTis){
                    final String shengMing= areaModel.getName();
                    final String shengId=areaModel.getId();
                    StringRequest requestShiShiTi=new StringRequest(Request.Method.POST, adrressUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    MyLog.d(tag, "省对应的市的数据" + s);
                                    List<AreaModel> shiShiTis=new ArrayList<AreaModel>();//装载市实体的集合
                                    jsonParse(s,shiShiTis);
                                    shengAndShiShiTi.put(shengMing,shiShiTis);//把该省对应的市添加到集合里面

                                    //------------------把市的实体遍历出来------------------------------
                                    for(AreaModel areaModel1:shiShiTis){
                                        final String shiMing=areaModel1.getName();
                                        final String shiId=areaModel1.getId();
                                        StringRequest xianShiTiRequest=new StringRequest(Request.Method.POST,
                                                adrressUrl, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String s) {
                                                MyLog.d(tag,"市对应的县数据"+s);
                                                List<AreaModel> xianShiTis=new ArrayList<AreaModel>();
                                                jsonParse(s,xianShiTis);
                                                shiAndXianShiTi.put(shiMing,xianShiTis);//把该市对应的县添加到结合里面

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {

                                            }
                                        }){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> map = new HashMap<String, String>();
                                                String json = "{\"parent_id\":\"" + shiId + "\"}";
                                                map.put("json", json);
                                                return map;
                                            }
                                        };
                                        requestQueue.add(xianShiTiRequest);

                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            String json = "{\"parent_id\":\"" + shengId + "\"}";
                            map.put("json", json);
                            return map;
                        }
                    };
                    requestQueue.add(requestShiShiTi);
                }

                //--------------------所有的数据都循环完了以后---------------------------------------------------------------------------

                shengMings=getStringListFromModelList(shengShiTis);
                //省列表的初始化
                ShengMingListViewAdapter shengMingListViewAdapter = new ShengMingListViewAdapter(AreaActivity.this, shengMings);
                shengListView.setAdapter(shengMingListViewAdapter);

               /* //未点击之前必须进行市县的初始化
                List<AreaModel> shiShiTi = shengAndShiShiTi.get(shengMings.get(0));
                MyLog.d(tag,"省名"+shengMings.get(0));
                shiMings=getStringListFromModelList(shiShiTi);
                shiMingAndXianMings = getMapStrigFromMapShiTi(shiAndXianShiTi);
                //市县列表------------------------------------------
                XianQuMingAdapter xianQuMingAdapter = new XianQuMingAdapter(AreaActivity.this, shiMingAndXianMings, shiMings);
                shiExapanListView.setAdapter(xianQuMingAdapter);*/



                //省列表的点击事件
                shengListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(AreaActivity.this, position + "", Toast.LENGTH_SHORT).show();
                        List<AreaModel> shiShiTi = shengAndShiShiTi.get(shengMings.get(position));
                        MyLog.d(tag,"省名"+shengMings.get(position));
                        shiMings=getStringListFromModelList(shiShiTi);
                        shiMingAndXianMings = getMapStrigFromMapShiTi(shiAndXianShiTi);

                        //市县列表------------------------------------------
                        XianQuMingAdapter xianQuMingAdapter = new XianQuMingAdapter(AreaActivity.this, shiMingAndXianMings, shiMings);
                        shiExapanListView.setAdapter(xianQuMingAdapter);
                        shiExapanListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                String shiMing = shiMings.get(groupPosition);
                                String xianMing = shiMingAndXianMings.get(shiMing).get(childPosition);
                                Intent intent = new Intent();
                                intent.putExtra(SHI_MING_KEY, shiMing);
                                intent.putExtra(XIAN_MING_KEY, xianMing);
                                setResult(RESULT_OK, intent);//向商品详情返回数据
                                finish();
                                return false;
                            }
                        });
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String json = "{\"parent_id\":\"" + "1" + "\"}";
                map.put("json", json);
                return map;
            }
        };
        requestQueue.add(addressListRequest);


        //初始化view
        closeReativeLayout = (RelativeLayout) findViewById(R.id.re_layout_close_area);
        shengListView = (ListView) findViewById(R.id.list_sheng_ming);
        shiExapanListView = (ExpandableListView) findViewById(R.id.expan_list_view_shi_ming);

        closeReativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//关闭活动
            }
        });

    }

    /**
     * 解析传回来的地址数据
     * @param s json字符串
     */
    private void jsonParse(String s,List<AreaModel> shiTis) {
        MyLog.d(tag, "返回的数据是：" + s);
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONArray jsonArray=jsonObject1.getJSONArray("regions");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject areaJsonObj=jsonArray.getJSONObject(i);
                AreaModel areaModel=new AreaModel();
                areaModel.setId(areaJsonObj.getString("id"));
                areaModel.setName(JsonHelper.decodeUnicode(areaJsonObj.getString("name")));
                shiTis.add(areaModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据省实体反求省得string集合
     */
    public List<String> getStringListFromModelList(List<AreaModel> as){
        List<String> results=new ArrayList<>();
        for(AreaModel areaModel:as){
            results.add(areaModel.getName());
        }
        return results;
    }

    /**
     * 根据map实体反求map的string
     */
    public Map<String,List<String>> getMapStrigFromMapShiTi(Map<String,List<AreaModel>> map){
        Map<String,List<String>> result=new HashMap<String,List<String>>();//结果
        Set<Map.Entry<String, List<AreaModel>>> set=map.entrySet();
        for(Map.Entry<String, List<AreaModel>> area:set){
            String key=area.getKey();
            List<String> names=new ArrayList<String>();
            List<AreaModel> models=area.getValue();
            for(AreaModel areaModel:models){
                String name=areaModel.getName();
                names.add(name);
            }
            result.put(key,names);
        }
        return result;
    }
}
