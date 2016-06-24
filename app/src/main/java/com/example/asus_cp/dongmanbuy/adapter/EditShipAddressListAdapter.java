package com.example.asus_cp.dongmanbuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.personal_center.data_set.UpdateShipAddressActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.UserModel;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑收货地址列表的适配器，归属于个人中心
 * Created by asus-cp on 2016-06-23.
 */
public class EditShipAddressListAdapter extends BaseAdapter{

    private String tag="EditShipAddressListAdapter";

    private Activity context;
    private List<UserModel> userModels;
    private LayoutInflater inflater;
    private List<Boolean> checks;

    private String deleteshouHuoAddressUrl="http://www.zmobuy.com/PHP/?url=/address/delete";//删除收获地址的接口
    private String defaultAddressUrl="http://www.zmobuy.com/PHP/?url=/address/setDefault";//设置默认地址的接口

    private RequestQueue requestQueue;

    private String uid;

    private String sid;

    public static final int REQUEST_CODE_TO_UPDATE=1;


    public EditShipAddressListAdapter(Activity context, List<UserModel> userModels) {
        this.context = context;
        this.userModels = userModels;
        inflater=LayoutInflater.from(context);
        initChecks();

        requestQueue= MyApplication.getRequestQueue();
        //获取uid和sid
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, Context.MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY, null);
    }

    /**
     * 初始化checks
     */
    private void initChecks() {
        checks=new ArrayList<Boolean>();
        for(int i=0;i<userModels.size();i++){
            checks.add(false);
        }
    }

    /**
     * 都不选中
     */
    public void allBuXuanZhong(){
        for(int i=0;i<userModels.size();i++){
            checks.set(i, false);
        }
    }

    public List<Boolean> getChecks() {
        return checks;
    }

    @Override
    public int getCount() {
        return userModels.size();
    }

    @Override
    public Object getItem(int position) {
        return userModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.edit_ship_address_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.checkBox= (CheckBox) v.findViewById(R.id.check_box_edit_ship_address_list_item);
            viewHolder.editLinearLayout= (LinearLayout) v.findViewById(R.id.ll_edit_ship_address_list_item);
            viewHolder.deleteLinearLayout= (LinearLayout) v.findViewById(R.id.ll_delete_ship_address_list_item);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_name_ship_address_list_item);
            viewHolder.phoneTextView= (TextView) v.findViewById(R.id.text_phone_ship_address_list_item);
            viewHolder.addressTextView= (TextView) v.findViewById(R.id.text_address_ship_address_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        final UserModel userModel=userModels.get(position);
        viewHolder.checkBox.setChecked(checks.get(position));
        viewHolder.nameTextView.setText(userModel.getUserName());
        viewHolder.phoneTextView.setText(userModel.getUserPhone());
        viewHolder.addressTextView.setText(userModel.getProvinceName() + userModel.getCityName() +
                userModel.getDistrictName() + MyConstant.KONG_GE + userModel.getShouHuoArea());


        //设置点击事件
        viewHolder.editLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateShipAddressActivity.class);
                intent.putExtra(MyConstant.USER_MODLE_KEY, userModel);
                context.startActivityForResult(intent,REQUEST_CODE_TO_UPDATE);
            }
        });
        viewHolder.deleteLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClickChuLi(position, userModel);
            }
        });

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDefaultAddressClickChuLi(position, userModel);
            }
        });
        return v;
    }


    /**
     * 更改默认地址的click处理
     * @param position
     * @param userModel
     */
    private void updateDefaultAddressClickChuLi(int position, final UserModel userModel) {
        allBuXuanZhong();
        checks.set(position, true);
        notifyDataSetChanged();
        StringRequest setDefaltRequest=new StringRequest(Request.Method.POST, defaultAddressUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "设置默认地址返回的结果：" + s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("status");
                            String succed=jsonObject1.getString("succeed");
                            if("1".equals(succed)){
                                Toast.makeText(context, "更改默认地址成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(context, "更改默认地址失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"address_id\":\""+userModel.getId()+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(setDefaltRequest);
    }


    /**
     * 删除的点击事件处理
     * @param position
     * @param userModel
     */
    private void deleteClickChuLi(final int position, final UserModel userModel) {
        StringRequest deleteRequest=new StringRequest(Request.Method.POST, deleteshouHuoAddressUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "返回的数据是：" + s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("status");
                            String succed=jsonObject1.getString("succeed");
                            if("1".equals(succed)){
                                checks.remove(position);
                                userModels.remove(position);
                                notifyDataSetChanged();
                            }else {
                                Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"address_id\":\""+userModel.getId()+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(deleteRequest);
    }




    class ViewHolder{
        CheckBox checkBox;
        LinearLayout editLinearLayout;
        LinearLayout deleteLinearLayout;
        TextView nameTextView;
        TextView phoneTextView;
        TextView addressTextView;
    }
}
