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
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyScreenInfoHelper;

import java.util.List;

/**
 * 店铺首页的热门商品网格的适配器
 * Created by asus-cp on 2016-06-08.
 */
public class ShopHomeHotProductAdapter extends BaseAdapter{
    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    public ShopHomeHotProductAdapter(Context context, List<Good> goods) {
        this.context = context;
        this.goods = goods;
        inflater=LayoutInflater.from(context);
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
            v=inflater.inflate(R.layout.shop_home_hot_product_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.picImageView= (ImageView) v.findViewById(R.id.img_shop_home_hot_product_pic);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_shop_home_hot_product_name);
            viewHolder.priceTextView= (TextView) v.findViewById(R.id.text_shop_home_hot_product_price);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        Good good=goods.get(position);

        //动态设置imageview，使它能够铺满全屏
        int screenWidth= MyScreenInfoHelper.getScreenWidth();
        int dpi=MyScreenInfoHelper.getScreenDpi();
        int tempWidth=(screenWidth-15*dpi/160)/2;
        ViewGroup.LayoutParams layoutParams=viewHolder.picImageView.getLayoutParams();
        layoutParams.height=tempWidth;
        viewHolder.picImageView.setLayoutParams(layoutParams);

        //给imageview设置值
        viewHolder.picImageView.setTag(good.getGoodsImg());
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.picImageView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
        imageLoader.get(good.getGoodsImg(), listener,500,500);

        viewHolder.nameTextView.setText(good.getGoodName());
        viewHolder.priceTextView.setText(FormatHelper.getMoneyFormat(good.getShopPrice()));
        return v;
    }

    class ViewHolder{
        ImageView picImageView;
        TextView nameTextView;
        TextView priceTextView;
    }
}
