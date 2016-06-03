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
 * 省名listview的适配器
 * Created by asus-cp on 2016-06-03.
 */
public class ShengMingListViewAdapter extends BaseAdapter{
    private Context context;
    private List<String> shengMings;
    private LayoutInflater inflater;

    public ShengMingListViewAdapter(Context context, List<String> shengMings) {
        this.context = context;
        this.shengMings = shengMings;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return shengMings.size();
    }

    @Override
    public Object getItem(int position) {
        return shengMings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        ShengMingListViewAdapter.ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.sheng_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.shengMingTextView= (TextView) v.findViewById(R.id.text_sheng_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        viewHolder.shengMingTextView.setText(shengMings.get(position));
        return v;
    }

    class ViewHolder{
        private TextView shengMingTextView;
    }
}
