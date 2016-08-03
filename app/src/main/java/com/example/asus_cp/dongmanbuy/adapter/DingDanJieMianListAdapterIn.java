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

import java.util.List;

/**
 * 订单界面的listview的适配器
 * Created by asus-cp on 2016-06-16.
 */
public class DingDanJieMianListAdapterIn extends BaseAdapter{
    private Context context;
    private List<Good> goods;
    private List<Integer> itemProductCount;//小项的商品数目
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    public DingDanJieMianListAdapterIn(Context context, List<Good> goods, List<Integer> itemProductCount) {
        this.context = context;
        this.goods = goods;
        this.itemProductCount = itemProductCount;
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
            v=inflater.inflate(R.layout.ding_dan_list_view_item_in_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.picImageView= (ImageView) v.findViewById(R.id.img_pic_ding_dan_list_item);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_product_name_ding_dan_list_item);
            viewHolder.priceTextView= (TextView) v.findViewById(R.id.text_product_price_ding_dan_list_item);
            viewHolder.chiMaTextView= (TextView) v.findViewById(R.id.text_product_chi_ma_ding_dan_list_item);
            viewHolder.countTextView= (TextView) v.findViewById(R.id.text_product_count_ding_dan_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        Good good=goods.get(position);
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.picImageView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
        imageLoader.get(good.getGoodsImg(), listener, 200, 200);

        viewHolder.nameTextView.setText(good.getGoodName());
        if("".equals(good.getShopPrice()) ||good.getShopPrice()==null){
            double sumPrice=Double.parseDouble(FormatHelper.getNumberFromRenMingBi(good.getDingDanSumPrice()));
            int productCount=Integer.parseInt(good.getDingDanNumber());
            double danJia=sumPrice/productCount;
            viewHolder.priceTextView.setText(FormatHelper.getMoneyFormat(""+danJia));
        }else{
            viewHolder.priceTextView.setText(FormatHelper.getMoneyFormat(good.getShopPrice()));
        }
        viewHolder.countTextView.setText("×"+itemProductCount.get(position));

        return v;

    }


    class ViewHolder{
        ImageView picImageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView chiMaTextView;
        TextView countTextView;
    }
}
