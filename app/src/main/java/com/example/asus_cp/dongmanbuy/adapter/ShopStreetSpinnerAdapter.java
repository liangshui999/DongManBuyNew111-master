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
 * 店铺街首页的3个spinner的通用adapter
 * Created by asus-cp on 2016-06-07.
 */
public class ShopStreetSpinnerAdapter extends BaseAdapter{
    private Context context;
    private List<String> names;
    private LayoutInflater inflater;

    public ShopStreetSpinnerAdapter(Context context, List<String> names) {
        this.context = context;
        this.names = names;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
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
            v=inflater.inflate(R.layout.shop_street_adapter_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.textView= (TextView) v.findViewById(R.id.text_shop_street_adapter_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        viewHolder.textView.setText(names.get(position));
        return v;
    }

    class ViewHolder{
        private TextView textView;
    }
}
