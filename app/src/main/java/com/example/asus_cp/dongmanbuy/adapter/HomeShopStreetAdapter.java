package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;

import java.util.List;

/**
 * 首页店铺街的适配器
 * Created by asus-cp on 2016-07-05.
 */
public class HomeShopStreetAdapter extends BaseAdapter{

    private Context context;
    private List<ShopModel> shops;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    public HomeShopStreetAdapter(Context context, List<ShopModel> shops) {
        this.context = context;
        this.shops = shops;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();
    }

    @Override
    public int getCount() {
        return shops.size();
    }

    @Override
    public Object getItem(int position) {
        return shops.get(position);
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
            v=inflater.inflate(R.layout.home_shop_street_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) v.findViewById(R.id.img_home_shop_street);
            viewHolder.imgOne= (ImageView) v.findViewById(R.id.img_shop_street_one);
            viewHolder.imgTwo= (ImageView) v.findViewById(R.id.img_shop_street_two);
            viewHolder.imgThree= (ImageView) v.findViewById(R.id.img_shop_street_three);
            v.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) v.getTag();
        }

        ShopModel shopModel=shops.get(position);
        List<Good> goods=shopModel.getGoods();
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.imageView,R.mipmap.yu_jia_zai_shop_street,
                R.mipmap.yu_jia_zai_shop_street);
        imageLoader.get(shops.get(position).getStreetThumb(),listener);

        if(goods!=null){
            if(goods.size()>=3){
                setValueToImageView(viewHolder, goods,viewHolder.imgOne,0);
                setValueToImageView(viewHolder, goods,viewHolder.imgTwo,1);
                setValueToImageView(viewHolder, goods,viewHolder.imgThree,2);

            }else if(goods.size()==2){
                setValueToImageView(viewHolder, goods,viewHolder.imgOne,0);
                setValueToImageView(viewHolder, goods,viewHolder.imgTwo,1);
            }else if(goods.size()==1){
                setValueToImageView(viewHolder, goods,viewHolder.imgOne,0);
            }
        }


        return v;
    }


    /**
     * 给imageview设置值
     * @param viewHolder
     * @param goods
     */
    private void setValueToImageView(ViewHolder viewHolder, List<Good> goods,ImageView view,int i) {
        ImageLoader imageLoader1=helper.getImageLoader();
        ImageLoader.ImageListener listener1=imageLoader1.getImageListener(view, R.mipmap.yu_jia_zai,
                R.mipmap.yu_jia_zai);
        imageLoader1.get(goods.get(i).getGoodsThumb(),listener1);
    }


    class ViewHolder{
        ImageView imageView;
        ImageView imgOne;
        ImageView imgTwo;
        ImageView imgThree;
    }
}
