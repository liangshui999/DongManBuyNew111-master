package com.example.asus_cp.dongmanbuy.activity.personal_center.data_set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.activity.gou_wu.AddShouHuoAddressActivity;
import com.example.asus_cp.dongmanbuy.adapter.EditShipAddressListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.UserModel;
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
 * 编辑收货人信息的界面，位于个人中心下面
 * Created by asus-cp on 2016-06-23.
 */
public class EditShipAddressActivity extends BaseActivity {

    private String tag="EditShipAddressActivity";

    private ListView listView;
    private Button addShouHuoRenXinXiButton;

    private String shouHuoAddressListUrl="http://www.zmobuy.com/PHP/index.php?url=/address/list";//收货地址列表的接口

    private List<Boolean> checks;

    private EditShipAddressListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.edit_shi_address_acitivity_layout);
        setTitle(R.string.ship_address_list);
        initView();
    }

    @Override
    public void initView() {
        listView= (ListView) findViewById(R.id.list_ship_address_personal_center);
        addShouHuoRenXinXiButton= (Button) findViewById(R.id.btn_add_ship_address_personal_center);

        addShouHuoRenXinXiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditShipAddressActivity.this, AddShouHuoAddressActivity.class);
                intent.putExtra(MyConstant.START_ADD_SHOU_HUO_ADDRESS_ACTIVTIY_KEY,"haha");
                startActivityForResult(intent, 1);
            }
        });

        //获取收货地址列表
        getShipAddressList();
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
//        MyLog.d(tag,"从其他活动返回的数据是："+data.getStringExtra(MyConstant.ADAPTER_KEY));
        getShipAddressList();
    }



    /**
     * 获取收获地址第列表
     */
    private void getShipAddressList() {
        DialogHelper.showDialog(this);
        //获取收货地址列表
        StringRequest shouHuoAddressListRequset=new StringRequest(Request.Method.POST, shouHuoAddressListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogHelper.dissmisDialog();
                        final List<UserModel> userModels=parseJson(s);
                        if (userModels.size()>0){
                            adapter=new EditShipAddressListAdapter(EditShipAddressActivity.this,
                                    userModels);
                            listView.setAdapter(adapter);
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
        requestQueue.add(shouHuoAddressListRequset);
    }
}
