package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;

import java.util.List;

/**
 * 店铺商品以大列表展示
 * Created by asus-cp on 2016-06-12.
 */
public class ShopProductBigListAdapter extends BaseAdapter{
    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    public ShopProductBigListAdapter(Context context, List<Good> goods) {
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
            v=inflater.inflate(R.layout.shop_product_list_big,null);
            viewHolder=new ViewHolder();
            viewHolder.picImagView= (ImageView) v.findViewById(R.id.img_shop_product_pic_big_list);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_shop_product_name_big_list);
            viewHolder.shopPriceTextView= (TextView) v.findViewById(R.id.text_shop_product_price_big_list);
            viewHolder.marketPriceTextView= (TextView) v.findViewById(R.id.text_shop_product_market_price_big_list);
            viewHolder.shoppingCarImageView= (ImageView) v.findViewById(R.id.img_shopping_car_big_list);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        Good good=goods.get(position);
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.picImagView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
        imageLoader.get(good.getGoodsThumb(),listener,200,200);

        viewHolder.nameTextView.setText(good.getGoodName());
        viewHolder.shopPriceTextView.setText(FormatHelper.getMoneyFormat(good.getShopPrice()));
        viewHolder.marketPriceTextView.setText(good.getMarket_price());
        viewHolder.marketPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        viewHolder.shoppingCarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击了购物车",Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    class ViewHolder{
        ImageView picImagView;
        TextView nameTextView;
        TextView shopPriceTextView;
        TextView marketPriceTextView;
        ImageView shoppingCarImageView;
    }
}
