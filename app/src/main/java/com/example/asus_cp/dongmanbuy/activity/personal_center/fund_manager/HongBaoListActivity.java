package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.adapter.HongBaoListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.YouHuiQuanModel;
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
 * 红包列表的界面
 * Created by asus-cp on 2016-06-27.
 */
public class HongBaoListActivity extends BaseActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_ADD_HONG_BAO =1 ;
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

    public static final String ALL="all";

    public static final String UN_USED_BONUS="0";//未使用红包
    public static final String USED_BONUS="1";//已使用红包
    public static final String EXPIRED_BONUS="3";//已过期红包




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.hong_bao_list_activity_layout);
        setTitle(R.string.hong_bao);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {

        initView();

        //弹出正在加载的对话框
        DialogHelper.showDialog(this);
        getDataFromIntet(UN_USED_BONUS, false);

    }


    /**
     * 从网络获取数据
     */
    private void getDataFromIntet(final String category, final boolean isGray) {
        StringRequest getHongBaoListRequest=new StringRequest(Request.Method.POST, hongBaoListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogHelper.dissmisDialog();
                        List<YouHuiQuanModel> models=parseJson(s,category);
                        HongBaoListAdapter adapter=new HongBaoListAdapter(HongBaoListActivity.this,models,isGray);
                        listView.setAdapter(adapter);

                        if(UN_USED_BONUS.equals(category)){
                            weiShiYongShuTextView.setText("("+models.size()+")");
                        }else if(USED_BONUS.equals(category)){
                            yiShiYongShuTextView.setText("("+models.size()+")");
                        }else if(EXPIRED_BONUS.equals(category)){
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
                youHuiQuanModel.setStatus(ziJsObj.getString("bonus_status"));
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
    public void initView() {

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
                getDataFromIntet(UN_USED_BONUS,false);
                break;
            case R.id.ll_yi_shi_yong://点击了已使用
                reset();
                yiShiYongTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                yiShiYongShuTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                getDataFromIntet(USED_BONUS,true);
                break;
            case R.id.ll_yi_guo_qi://点击了已过期
                reset();
                yiGuoQiTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                yiGuoQiShuTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                getDataFromIntet(EXPIRED_BONUS,true);
                break;
            case R.id.btn_add_hong_bao_hong_bao_list://点击了添加红包
                //Toast.makeText(this,"点击了添加红包",Toast.LENGTH_SHORT).show();
                Intent addHongBaoIntent=new Intent(this,AddHongBaoActivity.class);
                startActivityForResult(addHongBaoIntent, REQUEST_CODE_ADD_HONG_BAO);
                break;
        }
    }


    /**
     * 将所有颜色设置成黑色
     */
    private void reset() {
        weiShiYongTextView.setTextColor(getResources().getColor(R.color.myblack));
        weiShiYongShuTextView.setTextColor(getResources().getColor(R.color.myblack));
        yiShiYongTextView.setTextColor(getResources().getColor(R.color.myblack));
        yiShiYongShuTextView.setTextColor(getResources().getColor(R.color.myblack));
        yiGuoQiTextView.setTextColor(getResources().getColor(R.color.myblack));
        yiGuoQiShuTextView.setTextColor(getResources().getColor(R.color.myblack));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_ADD_HONG_BAO://添加红包
                if(resultCode==RESULT_OK){
                    reset();
                    weiShiYongTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                    weiShiYongShuTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                    getDataFromIntet("未使用",false);
                }
                break;
        }
    }
}
