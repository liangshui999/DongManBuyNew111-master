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
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.MyScreenInfoHelper;

import java.util.List;

/**
 * 精品推荐gridview的adapter
 * Created by asus-cp on 2016-05-21.
 */
public class CaiNiXiHuanAdapter extends BaseAdapter{

    private String tag="CaiNiXiHuanAdapter";

    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    public CaiNiXiHuanAdapter(Context context, List<Good> goods) {
        this.context = context;
        this.goods = goods;
        inflater=LayoutInflater.from(context);
        imageLoader=new ImageLoadHelper().getImageLoader();
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
        MyLog.d(tag,"getView");
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.cai_ni_xi_huan_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) v.findViewById(R.id.img_cai_ni_xi_huan);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_cai_ni_xi_huan_name);
            viewHolder.shopPriceTextView= (TextView) v.findViewById(R.id.text_cai_ni_xi_huan_shop_price);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        Good good=goods.get(position);

        viewHolder.nameTextView.setText(goods.get(position).getGoodName());
        //设置商品店铺价格,不带人民币符号
        String zheKouPrice = FormatHelper.getNumberFromRenMingBi(good.getPromotePrice());
        if (zheKouPrice==null || "0.00".equals(zheKouPrice) || "0".equals(zheKouPrice)) {
            viewHolder.shopPriceTextView.setText(FormatHelper.getMoneyFormat(goods.get(position).getShopPrice()));//使用shopprice
        }else{
            viewHolder.shopPriceTextView.setText(FormatHelper.getMoneyFormat(goods.get(position).getPromotePrice()));//使用折扣价格
        }
        //设置imageview的高度，使他能充满每一个item
        int screenWidth= MyScreenInfoHelper.getScreenWidth();
        int densty=MyScreenInfoHelper.getScreenDpi();
        int imgWidth=(screenWidth-densty*15/160)/2;//左边5，右边5，中间5，总共是15dp
        ViewGroup.LayoutParams params=viewHolder.imageView.getLayoutParams();
        params.height=imgWidth;
        viewHolder.imageView.setLayoutParams(params);


        ImageLoader.ImageListener imageListener=imageLoader.getImageListener(viewHolder.imageView, R.mipmap.yu_jia_zai,
                R.mipmap.yu_jia_zai);
        imageLoader.get(goods.get(position).getGoodsImg(), imageListener,500,500);

        return v;
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView nameTextView;
        private TextView shopPriceTextView;
    }

}
