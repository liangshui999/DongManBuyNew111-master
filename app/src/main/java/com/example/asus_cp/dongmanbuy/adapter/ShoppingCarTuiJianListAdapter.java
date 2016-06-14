package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
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
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;

import java.util.List;

/**
 * 购物车推荐列表的适配器
 * Created by asus-cp on 2016-06-13.
 */
public class ShoppingCarTuiJianListAdapter extends BaseAdapter{
    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    public ShoppingCarTuiJianListAdapter(Context context, List<Good> goods) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.shopping_car_tui_jian_list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.picImageView= (ImageView) v.findViewById(R.id.img_pic_shopping_car_tui_jian);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_product_name_shopping_car_tui_jian);
            viewHolder.priceTextView= (TextView) v.findViewById(R.id.text_product_price_shopping_car_tui_jian);
            viewHolder.shoppingCarImageView= (ImageView) v.findViewById(R.id.img_shopping_car_tui_jian);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        Good good=goods.get(position);
        //给控件赋值
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.picImageView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
        imageLoader.get(good.getGoodsImg(),listener);

        viewHolder.nameTextView.setText(good.getGoodName());
        viewHolder.priceTextView.setText(good.getShopPrice());

        viewHolder.shoppingCarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击的位置是"+position,Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    class ViewHolder{
        ImageView picImageView;
        TextView nameTextView;
        TextView priceTextView;
        ImageView shoppingCarImageView;
    }
}
