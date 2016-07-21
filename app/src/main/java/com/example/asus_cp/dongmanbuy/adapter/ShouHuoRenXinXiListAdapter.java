package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.UserModel;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 收货人信息列表的适配器
 * Created by asus-cp on 2016-06-15.
 */
public class ShouHuoRenXinXiListAdapter extends BaseAdapter{
    private Context context;
    private List <UserModel> users;
    private LayoutInflater inflater;
    private List<Boolean> checks;
    private String tag="ShouHuoRenXinXiListAdapter";


    public ShouHuoRenXinXiListAdapter(Context context, List<UserModel> users) {
        this.context = context;
        this.users = users;
        inflater=LayoutInflater.from(context);
        initChecks();
    }

    /**
     * 初始化checks
     */
    private void initChecks() {
        checks=new ArrayList<Boolean>();
        for(int i=0;i<users.size();i++){
            checks.add(false);
        }
    }


    /**
     * 将checks都设置成false
     * @return
     */
    public  void allBuXuanZhong(){
        for(int i=0;i<checks.size();i++){
            checks.set(i, false);
        }
    }

    /**
     * 将checks都设置成true
     * @return
     */
    public  void allXuanZhong(){
        for(int i=0;i<users.size();i++){
            checks.set(i,true);
        }
    }


    /**
     *返回checks
     */
    public List<Boolean> getChecks() {
        return checks;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.shou_huo_ren_xin_xi_item,null);
            viewHolder=new ViewHolder();
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_shou_huo_ren_name_list_item);
            viewHolder.phoneTextView= (TextView) v.findViewById(R.id.text_shou_huo_ren_phone_list_item);
            viewHolder.areaTextView= (TextView) v.findViewById(R.id.text_shou_huo_ren_address_list_item);
            viewHolder.checkBox= (CheckBox) v.findViewById(R.id.check_box_shou_huo_ren_xin_xi_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        UserModel userModel=users.get(position);
        viewHolder.nameTextView.setText(userModel.getUserName());
        viewHolder.phoneTextView.setText(userModel.getUserPhone());
        viewHolder.areaTextView.setText(userModel.getCountryName() + userModel.getProvinceName()
                + userModel.getCityName() + userModel.getDistrictName() + " " + userModel.getShouHuoArea());
        if(checks.size()>0){
            viewHolder.checkBox.setChecked(checks.get(position));
            MyLog.d(tag,"check的值："+checks.get(position));
        }
        return v;
    }

    class ViewHolder{
        TextView nameTextView;
        TextView phoneTextView;
        TextView areaTextView;
        CheckBox checkBox;
    }
}
