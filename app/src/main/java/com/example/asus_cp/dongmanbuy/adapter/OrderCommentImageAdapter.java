package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.MyScreenInfoHelper;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

/**
 * 订单评价详情里面展示图片的gridview使用的适配器
 * Created by asus-cp on 2016-10-27.
 */
public class OrderCommentImageAdapter extends BaseAdapter{

    private List<String> paths;
    private Context context;
    private LayoutInflater inflater;

    public OrderCommentImageAdapter(Context context, List<String> paths) {
        this.context = context;
        this.paths = paths;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
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
            v=inflater.inflate(R.layout.item_order_comment_pic_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) v.findViewById(R.id.img_pic_order_comment_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }


        int dpi=MyScreenInfoHelper.getScreenDpi();
        int screenWidth=MyScreenInfoHelper.getScreenWidth();
        int width=(screenWidth-15*dpi/160)/3;
        ViewGroup.LayoutParams layoutParams=viewHolder.imageView.getLayoutParams();
        layoutParams.width=width;
        layoutParams.height=width;
        viewHolder.imageView.setLayoutParams(layoutParams);

        String path=paths.get(position);

        String filePath = "file://" + paths.get(position);
        Picasso.with(context).load(filePath).error(R.mipmap.yu_jia_zai).resize(width,width).centerCrop().into(viewHolder.imageView);

        return v;
    }


    class ViewHolder{
        ImageView imageView;
    }
}
