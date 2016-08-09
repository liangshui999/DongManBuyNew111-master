package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;

import java.util.List;

/**
 * 订单号列表的适配器
 * Created by asus-cp on 2016-08-09.
 */
public class DingDanHaoListAdapter extends BaseAdapter{

    private Context context;
    private List<String> dingDanHaos;
    private LayoutInflater inflater;

    public DingDanHaoListAdapter(Context context, List<String> dingDanHaos) {
        this.context = context;
        this.dingDanHaos = dingDanHaos;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dingDanHaos.size();
    }

    @Override
    public Object getItem(int position) {
        return dingDanHaos.get(position);
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
            v=inflater.inflate(R.layout.ding_dan_hao_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.textView= (TextView) v.findViewById(R.id.text_ding_dan_hao_ding_dan_hao_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        viewHolder.textView.setText(dingDanHaos.get(position));
        return v;
    }

    class ViewHolder{
        TextView textView;
    }
}
