package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;

import java.util.List;

/**
 * 爆款新品的适配器
 * Created by asus-cp on 2016-06-01.
 */
public class BaoKuanXinPinAdapter extends RecyclerView.Adapter {
    private List<Good> goods;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    public BaoKuanXinPinAdapter(Context context, List<Good> goods) {
        this.context = context;
        this.goods = goods;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.bao_kuan_xin_pin_item_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        viewHolder.picImageView= (ImageView) v.findViewById(R.id.img_bao_kuan_xin_pin);
        viewHolder.nameText= (TextView) v.findViewById(R.id.text_bao_kuan_xin_pin_name);
        viewHolder.priceText= (TextView) v.findViewById(R.id.text_bao_kun_xin_pin_shop_price);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        viewHolder.nameText.setText(goods.get(position).getGoodName());
        viewHolder.priceText.setText(goods.get(position).getShopPrice());
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.picImageView,R.mipmap.yu_jia_zai,
                R.mipmap.yu_jia_zai);
        imageLoader.get(goods.get(position).getGoodsImg(),listener,300,300);
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }


    /**
     * viewhodler
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView picImageView;
        private TextView nameText;
        private TextView priceText;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
