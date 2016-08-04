package com.example.asus_cp.dongmanbuy.activity.personal_center;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.dian_pu_jie.ShopHomeActivity;
import com.example.asus_cp.dongmanbuy.adapter.GuanZhuListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
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
 * 关注列表的界面，个人中心里面
 * Created by asus-cp on 2016-06-24.
 */
public class GuanZhuListActivity extends Activity{

    private String tag="GuanZhuListActivity";

    private String guanZhuListUrl="http://www.zmobuy.com/PHP/?url=/user/storelist";//获取关注列表的数据

    private RequestQueue requestQueue;

    private String uid;
    private String sid;

    private ListView listView;
    private LinearLayout noContentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guan_zhu_list_activity);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        requestQueue= MyApplication.getRequestQueue();
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);

        listView= (ListView) findViewById(R.id.list_guan_zhu);
        noContentLinearLayout= (LinearLayout) findViewById(R.id.ll_no_content_guan_zhu);

        StringRequest getGuanZhuListRequest=new StringRequest(Request.Method.POST, guanZhuListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        final List<ShopModel> shopModels=parseJson(s);
                        if(shopModels.size()>0){
                            listView.setVisibility(View.VISIBLE);
                            noContentLinearLayout.setVisibility(View.GONE);
                            GuanZhuListAdapter adapter = new GuanZhuListAdapter(GuanZhuListActivity.this, shopModels);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent=new Intent(GuanZhuListActivity.this, ShopHomeActivity.class);
                                    intent.putExtra(MyConstant.SHOP_USER_ID_KEY,shopModels.get(position).getUserId());
                                    startActivity(intent);
                                }
                            });
                        }else{
                            listView.setVisibility(View.GONE);
                            noContentLinearLayout.setVisibility(View.VISIBLE);
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
    }



    /**
     * 解析json数据
     * @param s
     * @return
     */
    private List<ShopModel> parseJson(String s) {
        MyLog.d(tag, "返回的数据是：" + s);
        List<ShopModel> shopModels=new ArrayList<ShopModel>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONArray jsonArray=jsonObject1.getJSONArray("store_list");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject ziJsObj=jsonArray.getJSONObject(i);
                ShopModel shopModel=new ShopModel();
                shopModel.setUserId(ziJsObj.getString("shop_id"));
                shopModel.setShopName(JsonHelper.decodeUnicode(ziJsObj.getString("store_name")));
                shopModel.setShopLogo(ziJsObj.getString("shop_logo"));
                shopModel.setGazeNumber(ziJsObj.getString("count_store"));
                shopModel.setBrandThumb(ziJsObj.getString("brand_thumb"));
                shopModels.add(shopModel);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopModels;
    }



    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
