package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.YouHuiQuanModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券的适配器
 * Created by asus-cp on 2016-06-17.
 */
public class YouHuiQuanListAdapter extends BaseAdapter{
    private Context context;
    private List<YouHuiQuanModel> youHuiQuanModels;
    private LayoutInflater inflater;
    private List<Boolean> checks;

    public YouHuiQuanListAdapter(Context context, List<YouHuiQuanModel> youHuiQuanModels) {
        this.context = context;
        this.youHuiQuanModels = youHuiQuanModels;
        inflater=LayoutInflater.from(context);
        initChecks();
    }

    /**
     * 初始化checks
     */
    private void initChecks() {
        checks=new ArrayList<Boolean>();
        checks.add(true);
        for(int i=1;i<youHuiQuanModels.size();i++){
            checks.add(false);
        }
    }

    /**
     *都不选中
     */
    public void allBuXuanZhong(){
        for(int i=1;i<youHuiQuanModels.size();i++){
            checks.set(i,false);
        }
    }


    public List<Boolean> getChecks() {
        return checks;
    }

    @Override
    public int getCount() {
        return youHuiQuanModels.size();
    }

    @Override
    public Object getItem(int position) {
        return youHuiQuanModels.get(position);
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
            v=inflater.inflate(R.layout.you_hui_quan_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.jinETextView= (TextView) v.findViewById(R.id.text_you_hui_quan_jin_e_list_item);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_you_hui_quan_name_list_item);
            viewHolder.timeTextView= (TextView) v.findViewById(R.id.text_you_hui_quan_shi_jian_list_item);
            viewHolder.checkBox= (CheckBox) v.findViewById(R.id.check_box_you_hui_quan_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        YouHuiQuanModel model=youHuiQuanModels.get(position);
        viewHolder.jinETextView.setText(model.getYouHuiQuanJinE());
        viewHolder.nameTextView.setText(model.getYouHuiQuanName());
        viewHolder.timeTextView.setText(model.getYouHuiQuanTime());
        viewHolder.checkBox.setChecked(checks.get(position));

        return v;
    }

    class ViewHolder{
        TextView jinETextView;
        TextView nameTextView;
        TextView timeTextView;
        CheckBox checkBox;
    }

}
