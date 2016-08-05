package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import java.util.List;

/**
 * 购物车为空时，你可能想要的适配器
 * Created by asus-cp on 2016-05-27.
 */
public class MayBeYouWantAdapter extends RecyclerView.Adapter {

    private String tag="MayBeYouWantAdapter";

    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    private OnItemClickLitener mOnItemClickLitener;

    public MayBeYouWantAdapter(Context context, List<Good> goods) {
        this.context = context;
        this.goods = goods;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();
        MyLog.d(tag,"商品数量是："+goods.size());
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.may_be_you_want_item_layout,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v);
        myViewHolder.imageView= (ImageView) v.findViewById(R.id.img_may_be_you_want_pic);
        myViewHolder.nameTextView= (TextView) v.findViewById(R.id.text_may_be_you_want_name);
        myViewHolder.shopPriceTextView= (TextView) v.findViewById(R.id.text_may_be_you_want_shop_price);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder mViewHolder= (MyViewHolder) holder;//这里需要进行强制类型转换
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=ImageLoader.getImageListener(mViewHolder.imageView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
        imageLoader.get(goods.get(position).getGoodsThumb(),listener,300,300);
        mViewHolder.nameTextView.setText(goods.get(position).getGoodName());
        mViewHolder.shopPriceTextView.setText(goods.get(position).getShopPrice());
        MyLog.d(tag,"onBindViewHolder");

        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(mViewHolder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    /**
     * viewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nameTextView;
        TextView shopPriceTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }


    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }
}
