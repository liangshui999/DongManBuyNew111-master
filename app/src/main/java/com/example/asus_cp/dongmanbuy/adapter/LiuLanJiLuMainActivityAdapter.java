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
import com.example.asus_cp.dongmanbuy.db.BookDBOperateHelper;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;

import java.util.List;

/**
 * mainactivity中浏览记录用的适配器
 * Created by asus-cp on 2016-07-21.
 */
public class LiuLanJiLuMainActivityAdapter extends BaseAdapter{

    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;
    private BookDBOperateHelper dbOperateHelper;

    public LiuLanJiLuMainActivityAdapter(Context context, List<Good> goods) {
        this.context = context;
        this.goods = goods;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();
        dbOperateHelper=new BookDBOperateHelper();
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
            v=inflater.inflate(R.layout.liu_lan_ji_lu_mainactivity_adapter_item,null);
            viewHolder=new ViewHolder();
            viewHolder.picImageView= (ImageView) v.findViewById(R.id.img_pic_liu_lan_ji_lu_list);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_product_name_liu_lan_ji_lu_list);
            viewHolder.kuCunTextView= (TextView) v.findViewById(R.id.text_product_ku_cun_liu_lan_ji_lu_list);
            viewHolder.priceTextView= (TextView) v.findViewById(R.id.text_product_price_liu_lan_ji_lu_list);
            viewHolder.deleteImageView= (ImageView) v.findViewById(R.id.img_delete_liu_lan_ji_lu_list);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        final Good good=goods.get(position);
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.picImageView,R.mipmap.yu_jia_zai,
                R.mipmap.yu_jia_zai);
        imageLoader.get(good.getGoodsThumb(), listener, 300, 300);

        viewHolder.nameTextView.setText(good.getGoodName());
        viewHolder.priceTextView.setText(FormatHelper.getMoneyFormat(good.getShopPrice()));
        viewHolder.kuCunTextView.setText(good.getGoodsNumber());

        //删除按钮设置点击事件
        viewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goods.remove(good);
                notifyDataSetChanged();
                dbOperateHelper.delete(good);
            }
        });
        return v;
    }


    class ViewHolder{
        ImageView picImageView;//图像
        TextView nameTextView;
        TextView kuCunTextView;
        TextView priceTextView;
        ImageView deleteImageView;
    }
}
