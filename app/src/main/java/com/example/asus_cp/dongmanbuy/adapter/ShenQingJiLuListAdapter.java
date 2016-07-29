package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.ShenQingJiLuModel;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;

import java.util.List;

/**
 * 申请记录列表的适配器
 * Created by asus-cp on 2016-06-28.
 */
public class ShenQingJiLuListAdapter extends BaseAdapter{

    private Context context;
    private List<ShenQingJiLuModel> models;
    private LayoutInflater inflater;

    public ShenQingJiLuListAdapter(Context context, List<ShenQingJiLuModel> models) {
        this.context = context;
        this.models = models;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
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
            v=inflater.inflate(R.layout.shen_qing_ji_lu_list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.typeTextView= (TextView) v.findViewById(R.id.text_jiao_yi_fang_shi_shen_qing_ji_lu_list_item);
            viewHolder.timeTextView= (TextView) v.findViewById(R.id.text_time_shen_qing_ji_lu_list_item);
            viewHolder.moneyTextView= (TextView) v.findViewById(R.id.text_jin_e_shen_qing_ji_lu_list_item);
            viewHolder.statusTextView= (TextView) v.findViewById(R.id.text_status_shen_qing_ji_lu_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        ShenQingJiLuModel model=models.get(position);
        viewHolder.typeTextView.setText(model.getType());
        viewHolder.timeTextView.setText(model.getTime());
        viewHolder.moneyTextView.setText(FormatHelper.getMoneyFormat(model.getJinE()));
        viewHolder.statusTextView.setText(model.getStatus());

        return v;
    }


    class ViewHolder{
        TextView typeTextView;
        TextView timeTextView;
        TextView moneyTextView;
        TextView statusTextView;
    }
}
