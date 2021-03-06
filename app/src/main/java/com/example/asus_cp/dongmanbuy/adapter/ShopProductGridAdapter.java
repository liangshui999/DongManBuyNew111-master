package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.List;

/**
 * 店铺商品以卡片状展示
 * Created by asus-cp on 2016-06-12.
 */
public class ShopProductGridAdapter extends BaseAdapter{

    private String tag="ShopProductGridAdapter";
    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;
    private boolean isGridViewIdle=true;//gridview是否停止了滚动,true说明是静止的
    private float firstY;
    private float lastY;


    public ShopProductGridAdapter(Context context, List<Good> goods,PullToRefreshGridView gridView) {
        this.context = context;
        this.goods = goods;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();
       /* gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if(scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    isGridViewIdle=true;
                    notifyDataSetChanged();
                }else{
                    isGridViewIdle=false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/




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
        PullToRefreshGridView.InternalGridViewSDK9 tempGridView= (PullToRefreshGridView.InternalGridViewSDK9) parent;
        MyLog.d(tag,"isOnMeasure:"+tempGridView.isOnMesure);

        ViewHolder viewHolder = null;
        if (v == null) {
            v = inflater.inflate(R.layout.shop_product_ka_pian_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.imgView = (ImageView) v.findViewById(R.id.img_shop_product_pic_grid);
            viewHolder.nameTextView = (TextView) v.findViewById(R.id.text_shop_product_name_grid);
            viewHolder.priceTextView = (TextView) v.findViewById(R.id.text_shop_product_price_grid);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        //动态设置imageview的高度，使他能铺满全屏
        int screenWidth= MyScreenInfoHelper.getScreenWidth();
        int dpi=MyScreenInfoHelper.getScreenDpi();
        int tempWidth=(screenWidth-15*dpi/160)/2;
        ViewGroup.LayoutParams layoutParams=viewHolder.imgView.getLayoutParams();
        layoutParams.height=tempWidth;
        viewHolder.imgView.setLayoutParams(layoutParams);

        if(tempGridView.isOnMesure){    //正在测量的时候，就直接返回v，不用给item的小项设置值
            return  v;
        }

        Good good = goods.get(position);


        viewHolder.imgView.setImageResource(R.mipmap.yu_jia_zai);//注意这一步很关键，先把复用的图片换成预加载
        viewHolder.imgView.setTag(good.getGoodsThumb());
        ImageLoader imageLoader = helper.getImageLoader();
        ImageLoader.ImageListener listener = imageLoader.getImageListener(viewHolder.imgView, R.mipmap.yu_jia_zai,
                R.mipmap.yu_jia_zai);
        imageLoader.get(good.getGoodsThumb(), listener, 200, 200);

        viewHolder.nameTextView.setText(good.getGoodName());

        //设置商品店铺价格,不带人民币符号
        String zheKouPrice = FormatHelper.getNumberFromRenMingBi(good.getPromotePrice());
        if (zheKouPrice == null || "0.00".equals(zheKouPrice) || "0".equals(zheKouPrice)) {
            viewHolder.priceTextView.setText(FormatHelper.getMoneyFormat(good.getShopPrice()));
        } else {
            viewHolder.priceTextView.setText(FormatHelper.getMoneyFormat(zheKouPrice));
        }



        return v;
    }

    class ViewHolder{
        ImageView imgView;
        TextView nameTextView;
        TextView priceTextView;
    }
}
