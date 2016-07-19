package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.AccountDetailListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.DingDanModel;
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
 * 账户明细的界面
 * Created by asus-cp on 2016-06-27.
 */
public class AccountDetailActivity extends Activity{

    private String tag="AccountDetailActivity";

    private ListView listView;
    private LinearLayout noContentLinearLayout;

    private String accountDetailUrl="http://www.zmobuy.com/PHP/?url=/user/account_detail";//账户明细的接口

    private RequestQueue requestQueue;
    private String uid;
    private String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_detail_activity_layout);
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

        listView= (ListView) findViewById(R.id.list_view_account_detail);
        noContentLinearLayout= (LinearLayout) findViewById(R.id.ll_no_content_account_detail);

        StringRequest accontDetailRequest=new StringRequest(Request.Method.POST, accountDetailUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        List<DingDanModel> models=parseJson(s);
                        if(models.size()>0){
                            noContentLinearLayout.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            AccountDetailListAdapter adapter=new AccountDetailListAdapter(AccountDetailActivity.this,models);
                            listView.setAdapter(adapter);
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
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"pagination\":{\"page\":\"1\",\"count\":\"20\"}}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(accontDetailRequest);
    }


    /**
     * 解析json数据
     * @param s
     */
    private List<DingDanModel> parseJson(String s) {
        MyLog.d(tag, "返回的数据是：" + s);
        List<DingDanModel> models=new ArrayList<DingDanModel>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject ziJsonObj=jsonArray.getJSONObject(i);
                DingDanModel dingDanModel=new DingDanModel();
                dingDanModel.setOrderBianHao(JsonHelper.decodeUnicode(ziJsonObj.getString("short_change_desc")));
                dingDanModel.setSumPrice(ziJsonObj.getString("user_money"));
                dingDanModel.setOrderTime(ziJsonObj.getString("change_time"));
                models.add(dingDanModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return models;
    }
}
