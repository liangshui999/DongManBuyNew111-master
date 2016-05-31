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
 * 分类列表的adapter
 * Created by asus-cp on 2016-05-26.
 */
public class CategoryListAdapter extends BaseAdapter {
    private List<String> categories;
    private Context context;
    private LayoutInflater inflater;

    public CategoryListAdapter(Context context, List<String> categories) {
        this.context = context;
        this.categories = categories;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
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
            v=inflater.inflate(R.layout.category_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.textView= (TextView) v.findViewById(R.id.text_category_item_list);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        if(position==0){
            v.setBackgroundResource(R.color.white_my);
        }
        viewHolder.textView.setText(categories.get(position));
        return v;
    }

    class ViewHolder{
        private TextView textView;
    }
}
