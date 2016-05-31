package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;

import java.util.List;

/**
 * 类别里面gridview的通用适配器
 * Created by asus-cp on 2016-05-25.
 */
public class CategoryGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;

    public CategoryGridViewAdapter(List<Good> goods, Context context) {
        this.goods = goods;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return goods.size();
    }

    @Override
    public Object getItem(int position) {
        return goods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        CategoryGridViewAdapter.ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.category_grid_view_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) v.findViewById(R.id.img_category_item);
            viewHolder.textView= (TextView) v.findViewById(R.id.text_category_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        ImageLoadHelper imageLoadHelper=new ImageLoadHelper();
        ImageLoader imageLoader=imageLoadHelper.getImageLoader();
        ImageLoader.ImageListener imageListener=ImageLoader.getImageListener(viewHolder.imageView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
        imageLoader.get(goods.get(position).getGoodsSmallImag(),imageListener,200,200);
        viewHolder.textView.setText(goods.get(position).getGoodName());
        return v;
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }
}
