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
 * 待评价的适配器
 * Created by asus-cp on 2016-10-24.
 */
public class AwaitCommentAdapter extends BaseAdapter{

    private static final String TAG = "AwaitCommentAdapter";
    private Context mContext;
    private List<Good> goods;
    private LayoutInflater mInflater;
    private ImageLoadHelper helper;

    public AwaitCommentAdapter(Context mContext, List<Good> goods) {
        this.mContext = mContext;
        this.goods = goods;
        mInflater=LayoutInflater.from(mContext);
        helper=new ImageLoadHelper();
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
        ViewHolder viewHolder=null;
        if(v==null){
            v=mInflater.inflate(R.layout.item_layout_await_comment,null);
            viewHolder=new ViewHolder();
            viewHolder.picImageView= (ImageView) v.findViewById(R.id.img_good_pic_await_comment);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_good_name_await_comment);
            viewHolder.commentTextView= (TextView) v.findViewById(R.id.text_comment_await_comment);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        Good good=goods.get(position);

        //给view设置值
        viewHolder.nameTextView.setText(good.getGoodName());

        //给图片设置值
        viewHolder.picImageView.setTag(good.getGoodsThumb());
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=ImageLoader.getImageListener(viewHolder.picImageView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
//        imageLoader.get(good.getGoodsThumb(),listener,400,400);

        //设置点击事件
        viewHolder.commentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return v;


    }


    class ViewHolder{
        ImageView picImageView;
        TextView nameTextView;
        TextView commentTextView;
    }
}
