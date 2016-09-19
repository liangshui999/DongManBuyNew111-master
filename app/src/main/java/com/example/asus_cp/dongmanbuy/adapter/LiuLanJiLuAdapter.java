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
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;

import java.util.List;

/**
 * 浏览记录的适配器
 * Created by asus-cp on 2016-06-28.
 */
public class LiuLanJiLuAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    public LiuLanJiLuAdapter(Context context, List<Good> goods) {
        this.context = context;
        this.goods=goods;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.shop_content_item_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        viewHolder.picImagView= (ImageView) v.findViewById(R.id.img_shop_content_pic);
        viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_shop_content_name);
        viewHolder.priceTextView= (TextView) v.findViewById(R.id.text_shop_content_price);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder= (ViewHolder) holder;
        viewHolder.nameTextView.setText(goods.get(position).getGoodName());
        viewHolder.priceTextView.setText(FormatHelper.getMoneyFormat(goods.get(position).getShopPrice()));
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.picImagView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
        imageLoader.get(goods.get(position).getGoodsImg(),listener,500,500);

        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.getView(), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        ImageView picImagView;
        TextView nameTextView;
        TextView priceTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
        }

        public View getView(){
            return view;
        }

    }

    /**
     * ItemClick的回调接口
     *
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
