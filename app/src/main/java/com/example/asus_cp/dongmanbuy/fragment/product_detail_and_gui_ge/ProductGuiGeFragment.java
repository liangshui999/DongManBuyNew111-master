package com.example.asus_cp.dongmanbuy.fragment.product_detail_and_gui_ge;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.GuiGeAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.fragment.BaseFragment;
import com.example.asus_cp.dongmanbuy.model.GuiGeModel;
import com.example.asus_cp.dongmanbuy.net.MyImageRequest;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
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
 * 商品规格的碎片
 * Created by asus-cp on 2016-06-02.
 */
public class ProductGuiGeFragment extends BaseFragment{

    private String tag="ProductGuiGeFragment";
    private RequestQueue requestQueue;
    private String guiGeUrl="http://api.zmobuy.com/JK/base/model.php";
    private ListView listView;
    private String goodId;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.product_gui_ge_fragment_layout,null);
        listView= (ListView) v.findViewById(R.id.list_view_gui_ge);
        init();
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init() {
        context=getActivity();
        requestQueue= MyApplication.getRequestQueue();
        goodId=getArguments().getString(MyConstant.GOOD_ID_KEY);
        DialogHelper.showDialog(context);
        StringRequest guiGeRequest=new StringRequest(Request.Method.POST, guiGeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DialogHelper.dissmisDialog();
                MyLog.d(tag,"返回的数据是："+response);
                MyLog.d(tag,"转换后的数据是："+convertJsObjToJsArr(response));
                List<GuiGeModel> models=new ArrayList<GuiGeModel>();
                String s=convertJsObjToJsArr(response);
                if(s!=null){
                    try {
                        JSONArray jsonArray=new JSONArray(s);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            GuiGeModel guiGeModel=new GuiGeModel();
                            guiGeModel.setName(jsonObject.getString("name"));
                            guiGeModel.setValue(jsonObject.getString("value"));
                            models.add(guiGeModel);
                            //MyLog.d(tag,jsonObject.toString());
                        }
                        GuiGeAdapter adapter=new GuiGeAdapter(context,models);
                        listView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                map.put("service","get_goods_properties");
                map.put("goods_id",goodId);
                return map;
            }
        };
        requestQueue.add(guiGeRequest);
    }


    /**
     * 将jsonobject转换成json数组
     */
    private String convertJsObjToJsArr(String s){
        s=FormatHelper.removeBom(s);//清除掉返回的数据里面自带隐藏的东西
        if("[]".equals(s)){
            return null;
        }else {
            int last=s.length()-2;
            String temp=s.substring(9,last);
            String regex="\"[0-9]+\":"; //这是最关键的一步
            String temp2=temp.replaceAll(regex,"");
            String result="["+temp2+"]";
            return result;
        }
    }
}
