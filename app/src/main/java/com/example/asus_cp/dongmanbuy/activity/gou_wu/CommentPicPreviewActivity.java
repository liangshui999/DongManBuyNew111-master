package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.adapter.CommentPicPreviewPagerAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.MyScreenInfoHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论图片预览的界面
 * Created by asus-cp on 2016-10-28.
 */
public class CommentPicPreviewActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    private static final String TAG = "CommentPicPreviewActivity";
    private ViewPager mViewPager;
    private LinearLayout mPointGroupLinearLayout;//指示点所在的布局
    private List<String> mPaths;//图片路径的集合
    private List<ImageView> mImageViews;//imageview的集合
    private int mInitPostion;//初始页面的位置

    private int mScreenWidth;//屏幕宽
    private int mScreenHeight;//屏幕高
    private int mDpi;//屏幕像素密度

    private CommentPicPreviewPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_comment_pic_preview_layout);
        setTitle(R.string.pic_preiview);
        initView();
        init();
    }


    @Override
    public void initView() {
        mViewPager= (ViewPager) findViewById(R.id.view_pager_comment_pic_preview);
        mPointGroupLinearLayout= (LinearLayout) findViewById(R.id.ll_point_group_comment_pic_preview);
    }

    private void init() {
        mInitPostion=getIntent().getIntExtra(MyConstant.PREVIEW_INIT_POSTION_KEY, 0);
        mPaths= (List<String>) getIntent().getSerializableExtra(MyConstant.PREVIEW_PATHS_KEY);

        //获取屏幕宽高
        mScreenWidth= MyScreenInfoHelper.getScreenWidth();
        mScreenHeight=MyScreenInfoHelper.getScreenHeight();
        mDpi=MyScreenInfoHelper.getScreenDpi();

        //初始化imageviews
        mImageViews=new ArrayList<>();
        for(int i=0;i<mPaths.size();i++){
            ImageView imageView=new ImageView(this);
            ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(mScreenWidth,mScreenHeight);
            imageView.setLayoutParams(layoutParams);
            Picasso.with(this).load("file://"+mPaths.get(i)).error(R.mipmap.yu_jia_zai).
                    resize(mScreenWidth, mScreenHeight).centerCrop().into(imageView);
            mImageViews.add(imageView);

            //初始化指示点
            ImageView pointImageView=new ImageView(this);
            LinearLayout.LayoutParams pointLayoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            pointLayoutParams.leftMargin=10*mDpi/160;
            pointImageView.setLayoutParams(pointLayoutParams);
            pointImageView.setImageResource(R.drawable.point_normal);
            mPointGroupLinearLayout.addView(pointImageView);
        }


        //给viewpager设置适配器和监听器
        mAdapter=new CommentPicPreviewPagerAdapter(this,mImageViews);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        //将页面和指示点跳转到指定位置
        mViewPager.setCurrentItem(mInitPostion);
        ImageView tempImageView= (ImageView) mPointGroupLinearLayout.getChildAt(mInitPostion);
        tempImageView.setImageResource(R.drawable.point_focused);


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i=0;i<mPointGroupLinearLayout.getChildCount();i++){
            ImageView child= (ImageView) mPointGroupLinearLayout.getChildAt(i);
            child.setImageResource(R.drawable.point_normal);
        }
        ImageView tempImageView= (ImageView) mPointGroupLinearLayout.getChildAt(position);
        tempImageView.setImageResource(R.drawable.point_focused);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
