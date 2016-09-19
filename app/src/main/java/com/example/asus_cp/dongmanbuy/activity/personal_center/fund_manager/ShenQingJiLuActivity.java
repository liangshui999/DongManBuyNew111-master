package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

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
import com.example.asus_cp.dongmanbuy.adapter.ShenQingJiLuListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.ShenQingJiLuModel;
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
 * 申请记录的界面
 * Created by asus-cp on 2016-06-28.
 */
public class ShenQingJiLuActivity extends BaseActivity {

    private String tag="ShenQingJiLuActivity";

    private ListView listView;
    private LinearLayout noContentLinearLayout;

    private String shenQingJiLuUrl="http://www.zmobuy.com/PHP/?url=/user/log";//申请记录列表的接口


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.shen_qing_ji_lu_activity_layout);
        setTitle(R.string.shen_qing_ji_lu);
        initView();
        init();
    }

    @Override
    public void initView() {
        listView= (ListView) findViewById(R.id.list_view_shen_qing_ji_lu);
        noContentLinearLayout= (LinearLayout) findViewById(R.id.ll_no_content_shen_qing_ji_lu);
    }


    /**
     * 初始化的方法
     */
    private void init() {
        //弹出正在加载的对话框
        DialogHelper.showDialog(this);
        getDataFromIntenet();

    }


    /**
     * 从网络获取数据
     */
    private void getDataFromIntenet() {
        StringRequest shenQingJiLuRequest=new StringRequest(Request.Method.POST, shenQingJiLuUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogHelper.dissmisDialog();
                        final List<ShenQingJiLuModel> models=parseJson(s);
                        if(models.size()>0){
                            listView.setVisibility(View.VISIBLE);
                            noContentLinearLayout.setVisibility(View.GONE);
                            ShenQingJiLuListAdapter adapter=new ShenQingJiLuListAdapter(ShenQingJiLuActivity.this,models);
                            listView.setAdapter(adapter);
                        }else{
                            listView.setVisibility(View.GONE);
                            noContentLinearLayout.setVisibility(View.VISIBLE);
                        }

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(ShenQingJiLuActivity.this,ShenQingJiLuDetailActivity.class);
                                intent.putExtra(MyConstant.SHEN_QING_JI_LU_MODEL_KEY,models.get(position));
                                startActivity(intent);

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
                String json="{\"id\":\""+""+"\",\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"pagination\":{\"page\":\"1\",\"count\":\"20\"}}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(shenQingJiLuRequest);
    }


    /**
     * 解析json数据
     * @param s
     */
    private List<ShenQingJiLuModel> parseJson(String s) {
        MyLog.d(tag, "返回的数据是：" + s);
        List<ShenQingJiLuModel> models=new ArrayList<ShenQingJiLuModel>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject ziJsonObj=jsonArray.getJSONObject(i);
                ShenQingJiLuModel model=new ShenQingJiLuModel();
                model.setId(ziJsonObj.getString("id"));
                model.setUserId(ziJsonObj.getString("user_id"));
                model.setJinE(JsonHelper.decodeUnicode(ziJsonObj.getString("amount")));
                model.setTime(ziJsonObj.getString("add_time"));
                model.setManagerNote(JsonHelper.decodeUnicode(ziJsonObj.getString("short_admin_note")));
                model.setUserNote(JsonHelper.decodeUnicode(ziJsonObj.getString("short_user_note")));
                model.setStatus(JsonHelper.decodeUnicode(ziJsonObj.getString("pay_status")));
                model.setType(JsonHelper.decodeUnicode(ziJsonObj.getString("type")));
                model.setShouXuFei(JsonHelper.decodeUnicode(ziJsonObj.getString("pay_fee")));
                models.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return models;
    }
}
