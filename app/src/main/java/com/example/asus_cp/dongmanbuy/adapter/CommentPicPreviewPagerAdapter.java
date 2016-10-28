package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 订单评论里面图片预览的viewpager的适配器
 * Created by asus-cp on 2016-10-28.
 */
public class CommentPicPreviewPagerAdapter extends PagerAdapter{

    private Context mContext;
    private List<ImageView> mImageVies;

    public CommentPicPreviewPagerAdapter(Context mContext, List<ImageView> mImageVies) {
        this.mContext = mContext;
        this.mImageVies = mImageVies;
    }

    @Override
    public int getCount() {
        return mImageVies.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mImageVies.get(position));
        return mImageVies.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageVies.get(position));
    }
}
