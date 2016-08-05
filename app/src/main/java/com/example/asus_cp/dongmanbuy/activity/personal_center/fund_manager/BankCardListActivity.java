package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.BankCardListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.CardModel;
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
 * 银行卡列表的界面
 * Created by asus-cp on 2016-06-27.
 */
public class BankCardListActivity extends Activity{

    private String tag="BankCardListActivity";

    private ListView listView;

    private String cardListUrl="http://www.zmobuy.com/PHP/?url=/user/card_list";//所有银行卡列表的接口
    private RequestQueue requestQueue;
    private String uid;
    private String sid;

    public static int REQUEST_CODE_ADD_BANK_CARD=1;//跳转到添加银行卡的界面



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bank_card_list_activity_layout);
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

        listView= (ListView) findViewById(R.id.list_view_bank_card_list);
        View v= LayoutInflater.from(this).inflate(R.layout.bank_card_list_foot_view_layout,null);
        Button addCardButton= (Button) v.findViewById(R.id.btn_add_bank_card);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BankCardListActivity.this, AddBankCardActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_BANK_CARD);
            }
        });
        listView.addFooterView(v);

        //弹出正在加载的对话框
        DialogHelper.showDialog(this);

        getDataFromIntenet();
    }


    /**
     * 联网获取数据
     */
    private void getDataFromIntenet() {
        StringRequest getCardListRequest=new StringRequest(Request.Method.POST, cardListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogHelper.dissmisDialog();
                        List<CardModel> models=parseJson(s);
                        BankCardListAdapter adapter=new BankCardListAdapter(BankCardListActivity.this,models);
                        listView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"}}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(getCardListRequest);
    }


    /**
     * 解析json数据
     * @param s
     */
    private List<CardModel> parseJson(String s) {
        MyLog.d(tag, "返回的数据是：" + s);
        List<CardModel> models=new ArrayList<CardModel>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject ziJsonObj=jsonArray.getJSONObject(i);
                CardModel cardModel=new CardModel();
                cardModel.setBankName(JsonHelper.decodeUnicode(ziJsonObj.getString("bank_name")));
                cardModel.setKaHao(ziJsonObj.getString("bank_card"));
                models.add(cardModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return models;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getDataFromIntenet();//再次从网络获取数据
    }
}
