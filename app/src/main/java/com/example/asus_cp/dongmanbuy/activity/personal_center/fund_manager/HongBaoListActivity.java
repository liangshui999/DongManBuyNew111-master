package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.HongBaoListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.YouHuiQuanModel;
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
 * 红包列表的界面
 * Created by asus-cp on 2016-06-27.
 */
public class HongBaoListActivity extends Activity implements View.OnClickListener{

    private String tag="HongBaoListActivity";

    private LinearLayout weiShiYongLinearLayout;//未使用
    private LinearLayout yiShiYongLinearLayout;//已使用
    private LinearLayout yiGuoQiLinearLayout;//已过期

    private TextView weiShiYongTextView;
    private TextView yiShiYongTextView;
    private TextView yiGuoQiTextView;

    private TextView weiShiYongShuTextView;
    private TextView yiShiYongShuTextView;
    private TextView yiGuoQiShuTextView;

    private ListView listView;
    private Button addHongBaoButton;//添加红包

    private String hongBaoListUrl="http://www.zmobuy.com/PHP/?url=/user/bonus_list";//获取所有红包的接口
    private RequestQueue requestQueue;

    private String uid;
    private String sid;

    public static final String ALL="all";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hong_bao_list_activity_layout);
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
        initView();
        getDataFromIntet("未使用", false);

    }


    /**
     * 从网络获取数据
     */
    private void getDataFromIntet(final String category, final boolean isGray) {
        StringRequest getHongBaoListRequest=new StringRequest(Request.Method.POST, hongBaoListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        List<YouHuiQuanModel> models=parseJson(s,category);
                        HongBaoListAdapter adapter=new HongBaoListAdapter(HongBaoListActivity.this,models,isGray);
                        listView.setAdapter(adapter);

                        if("未使用".equals(category)){
                            weiShiYongShuTextView.setText("("+models.size()+")");
                        }else if("已使用".equals(category)){
                            yiShiYongShuTextView.setText("("+models.size()+")");
                        }else if("已过期".equals(category)){
                            yiGuoQiShuTextView.setText("("+models.size()+")");
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
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"}}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(getHongBaoListRequest);
    }


    /**
     * 解析json数据
     * @param s
     * @param category 需要的数据类型，未使用，已使用，已过期3种之一
     */
    private List<YouHuiQuanModel> parseJson(String s,String category) {
        MyLog.d(tag, "返回的数据是:" + s);
        List<YouHuiQuanModel> models=new ArrayList<YouHuiQuanModel>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject ziJsObj=jsonArray.getJSONObject(i);
                YouHuiQuanModel youHuiQuanModel=new YouHuiQuanModel();
                youHuiQuanModel.setName(JsonHelper.decodeUnicode(ziJsObj.getString("type_name")));
                youHuiQuanModel.setJinE(ziJsObj.getString("type_money"));
                youHuiQuanModel.setStatus(JsonHelper.decodeUnicode(ziJsObj.getString("status")));
                youHuiQuanModel.setStartTime(ziJsObj.getString("use_startdate"));
                youHuiQuanModel.setEndTime(ziJsObj.getString("use_enddate"));
                youHuiQuanModel.setSuoShuDianPu(ziJsObj.getString("rz_shopName"));
                if (category.equals(youHuiQuanModel.getStatus())) {
                    models.add(youHuiQuanModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return models;
    }

    /**
     * 初始化view
     */
    private void initView() {
        weiShiYongLinearLayout= (LinearLayout) findViewById(R.id.ll_wei_shi_yong);
        yiShiYongLinearLayout= (LinearLayout) findViewById(R.id.ll_yi_shi_yong);
        yiGuoQiLinearLayout= (LinearLayout) findViewById(R.id.ll_yi_guo_qi);

        weiShiYongTextView= (TextView) findViewById(R.id.text_wei_shi_yong_hong_bao_list);
        yiShiYongTextView= (TextView) findViewById(R.id.text_yi_shi_yong_hong_bao_list);
        yiGuoQiTextView= (TextView) findViewById(R.id.text_yi_guo_qi_hong_bao_list);

        weiShiYongShuTextView= (TextView) findViewById(R.id.text_wei_shi_yong_shu_hong_bao_list);
        yiShiYongShuTextView= (TextView) findViewById(R.id.text_yi_shi_yong_shu_hong_bao_list);
        yiGuoQiShuTextView= (TextView) findViewById(R.id.text_yi_guo_qi_shu_hong_bao_list);

        listView= (ListView) findViewById(R.id.list_view_hong_bao_list);
        addHongBaoButton= (Button) findViewById(R.id.btn_add_hong_bao_hong_bao_list);


        weiShiYongTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        weiShiYongShuTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));

        //设置点击事件
        weiShiYongLinearLayout.setOnClickListener(this);
        yiShiYongLinearLayout.setOnClickListener(this);
        yiGuoQiLinearLayout.setOnClickListener(this);
        addHongBaoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_wei_shi_yong://点击了未使用
                reset();
                weiShiYongTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                weiShiYongShuTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                getDataFromIntet("未使用",false);
                break;
            case R.id.ll_yi_shi_yong://点击了已使用
                reset();
                yiShiYongTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                yiShiYongShuTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                getDataFromIntet("已使用",true);
                break;
            case R.id.ll_yi_guo_qi://点击了已过期
                reset();
                yiGuoQiTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                yiGuoQiShuTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                getDataFromIntet("已过期",true);
                break;
            case R.id.btn_add_hong_bao_hong_bao_list://点击了添加红包
                Toast.makeText(this,"点击了添加红包",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /**
     * 将所有颜色设置成黑色
     */
    private void reset() {
        weiShiYongTextView.setTextColor(getResources().getColor(R.color.black));
        weiShiYongShuTextView.setTextColor(getResources().getColor(R.color.black));
        yiShiYongTextView.setTextColor(getResources().getColor(R.color.black));
        yiShiYongShuTextView.setTextColor(getResources().getColor(R.color.black));
        yiGuoQiTextView.setTextColor(getResources().getColor(R.color.black));
        yiGuoQiShuTextView.setTextColor(getResources().getColor(R.color.black));
    }


}