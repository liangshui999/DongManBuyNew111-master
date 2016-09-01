package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.CategoryModel;

import java.util.List;

/**
 * 展示小分类所用的适配器
 * Created by asus-cp on 2016-09-01.
 */
public class CategoryAdapter extends BaseAdapter{

    private Context context;
    private List<CategoryModel> models;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, List<CategoryModel> models) {
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
            v=inflater.inflate(R.layout.category_grid_view_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) v.findViewById(R.id.img_category_item);
            viewHolder.textView= (TextView) v.findViewById(R.id.text_category_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        CategoryModel model=models.get(position);
        viewHolder.imageView.setImageResource(model.getCategoryImage());
        viewHolder.textView.setText(model.getCategoryName());
        return v;
    }


    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
