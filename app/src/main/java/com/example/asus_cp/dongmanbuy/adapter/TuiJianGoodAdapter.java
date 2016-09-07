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
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyGoodHelper;
import com.example.asus_cp.dongmanbuy.util.MyScreenInfoHelper;

import java.util.List;

/**
 * 购物车下面推荐商品的adapter
 * Created by asus-cp on 2016-05-21.
 */
public class TuiJianGoodAdapter extends BaseAdapter{
    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    public TuiJianGoodAdapter(Context context, List<Good> goods) {
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
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.tui_jian_good_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) v.findViewById(R.id.img_cai_ni_xi_huan);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_cai_ni_xi_huan_name);
            viewHolder.shopPriceTextView= (TextView) v.findViewById(R.id.text_cai_ni_xi_huan_shop_price);
            viewHolder.shoppingCarImageView= (ImageView) v.findViewById(R.id.img_shopping_car);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        viewHolder.nameTextView.setText(goods.get(position).getGoodName());
        viewHolder.shopPriceTextView.setText(FormatHelper.getMoneyFormat(MyGoodHelper.getRealPrice(goods.get(position))));


        //动态设置imageview的高度，使他能铺满全屏
        int screenWidth= MyScreenInfoHelper.getScreenWidth();
        int dpi=MyScreenInfoHelper.getScreenDpi();
        int tempWidth=(screenWidth-15*dpi/160)/2;
        ViewGroup.LayoutParams layoutParams=viewHolder.imageView.getLayoutParams();
        layoutParams.height=tempWidth;
        viewHolder.imageView.setLayoutParams(layoutParams);
        ImageLoader.ImageListener imageListener=imageLoader.getImageListener(viewHolder.imageView, R.mipmap.yu_jia_zai_cai_ni_xi_huan,
                R.mipmap.yu_jia_zai_cai_ni_xi_huan);
        imageLoader.get(goods.get(position).getGoodsImg(), imageListener,500,500);
        //设置点击事件
        viewHolder.shoppingCarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击了购物车",Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView nameTextView;
        private TextView shopPriceTextView;
        private ImageView shoppingCarImageView;
    }

}
