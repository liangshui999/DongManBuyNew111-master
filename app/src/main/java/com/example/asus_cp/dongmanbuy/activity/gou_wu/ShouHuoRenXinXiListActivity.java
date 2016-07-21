package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
import com.example.asus_cp.dongmanbuy.adapter.ShouHuoRenXinXiListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.UserModel;
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
 * 收货人信息列表
 * Created by asus-cp on 2016-06-15.
 */
public class ShouHuoRenXinXiListActivity extends Activity{

    private String tag="ShouHuoRenXinXiListActivity";
    private ListView listView;
    private TextView addShouHuoRenTextView;//新增收货人

    private String shouHuoAddressListUrl="http://www.zmobuy.com/PHP/index.php?url=/address/list";//收货地址列表的接口

    private RequestQueue requestQueue;

    private String uid;

    private String sid;

    private List<Boolean> checks;

    private ShouHuoRenXinXiListAdapter adapter;

    private  List<UserModel> userModels;

    public static final int REQUEST_CODE_ADD_SHOU_HUO_ADDRESS=1;//跳转到添加收获人地址的界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shou_huo_ren_xin_xi_list_layout);
        init();
    }

    /**
     *初始化方法
     */
    private void init() {
        requestQueue= MyApplication.getRequestQueue();

        //获取uid和sid
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);

        listView= (ListView) findViewById(R.id.list_view_shou_huo_ren_xin_xi);
        addShouHuoRenTextView= (TextView) findViewById(R.id.text_xin_zeng_shou_huo_ren_xin_xi);
        addShouHuoRenTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ShouHuoRenXinXiListActivity.this,"新增收货人信息",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ShouHuoRenXinXiListActivity.this,AddShouHuoAddressActivity.class);
                intent.putExtra(MyConstant.START_ADD_SHOU_HUO_ADDRESS_ACTIVTIY_KEY,"shouhuorenxinxilist");
                startActivityForResult(intent, REQUEST_CODE_ADD_SHOU_HUO_ADDRESS);
            }
        });

        userModels=new ArrayList<UserModel>();
        adapter=new ShouHuoRenXinXiListAdapter(ShouHuoRenXinXiListActivity.this,
                userModels);
        listView.setAdapter(adapter);
        checks=adapter.getChecks();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.allBuXuanZhong();
                checks.set(position, true);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.putExtra(MyConstant.USER_MODLE_KEY, userModels.get(position));
                setResult(RESULT_OK, intent);
                finish();
                //Toast.makeText(ShouHuoRenXinXiListActivity.this,"点击的位置"+checks.get(position),Toast.LENGTH_SHORT).show();
            }
        });
        getShouHuoDiZhiFromIntenet();
    }


    /**
     * 从网络获取收获地址列表并刷新数据
     */
    private void getShouHuoDiZhiFromIntenet() {
        //获取收货地址列表
        StringRequest shouHuoAddressListRequset=new StringRequest(Request.Method.POST, shouHuoAddressListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        List<UserModel> models=parseJson(s);
                        userModels.clear();
                        userModels.addAll(models);
                        adapter=new ShouHuoRenXinXiListAdapter(ShouHuoRenXinXiListActivity.this,
                                userModels);
                        listView.setAdapter(adapter);
                        checks=adapter.getChecks();
                        /*checks=adapter.getChecks();
                        if (userModels.size()>0){
                            adapter.notifyDataSetChanged();
                        }*/
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
        requestQueue.add(shouHuoAddressListRequset);
    }


    /**
     * 解析json数据
     * @param s
     */
    private List<UserModel> parseJson(String s) {
        List<UserModel> userModels=new ArrayList<UserModel>();
        MyLog.d(tag, "返回的数据是：" + s);
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=null;
            try{
                jsonArray=jsonObject.getJSONArray("data");
            }catch (Exception e){
                e.printStackTrace();
            }

            if(jsonArray!=null && jsonArray.length()>0){
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject ziJsObj=jsonArray.getJSONObject(i);
                    UserModel userModel=new UserModel();
                    userModel.setId(ziJsObj.getString("id"));
                    userModel.setUserName(JsonHelper.decodeUnicode(ziJsObj.getString("consignee")));
                    userModel.setShouHuoArea(JsonHelper.decodeUnicode(ziJsObj.getString("address")));
                    userModel.setUserPhone(ziJsObj.getString("mobile"));
                    userModel.setCountryName(JsonHelper.decodeUnicode(ziJsObj.getString("country_name")));
                    userModel.setProvinceName(JsonHelper.decodeUnicode(ziJsObj.getString("province_name")));
                    userModel.setCityName(JsonHelper.decodeUnicode(ziJsObj.getString("city_name")));
                    userModel.setDistrictName(JsonHelper.decodeUnicode(ziJsObj.getString("district_name")));
                    userModel.setDefaultAddress(ziJsObj.getString("default_address"));
                    userModels.add(userModel);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userModels;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_ADD_SHOU_HUO_ADDRESS://跳转到收获地址界面
                if(resultCode==RESULT_OK){
                    getShouHuoDiZhiFromIntenet();
                }
                break;
        }
    }
}
