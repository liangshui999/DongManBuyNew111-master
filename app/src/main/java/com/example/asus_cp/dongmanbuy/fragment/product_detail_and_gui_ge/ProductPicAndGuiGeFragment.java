package com.example.asus_cp.dongmanbuy.fragment.product_detail_and_gui_ge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.GuideViewPagerAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.fragment.BaseFragment;
import com.example.asus_cp.dongmanbuy.model.Good;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情的界面
 * Created by asus-cp on 2016-11-03.
 */
public class ProductPicAndGuiGeFragment extends BaseFragment implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private TextView prodcutDetailTextView;//商品详情选项
    private TextView productGuiGeTextView;//商品规格选项
    //private FrameLayout bufFrameLayout;//装载碎片的容器
    private ViewPager viewPager;
    private Good good;
    private List<Fragment> fragments;
    private ProductPicFragment productPicFragment;
    private ProductGuiGeFragment productGuiGeFragment;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.product_xiangqing_and_gui_ge, null);
        initView(v);

        Bundle bundle1=getArguments();
        good= bundle1.getParcelable(MyConstant.GOOD_KEY);
        prodcutDetailTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        productPicFragment =new ProductPicFragment();
        productGuiGeFragment=new ProductGuiGeFragment();
        fragments=new ArrayList<>();
        fragments.add(productPicFragment);
        fragments.add(productGuiGeFragment);
        //向规格碎片传递数据
        Bundle bundle=new Bundle();
        bundle.putString(MyConstant.GOOD_ID_KEY, good.getGoodId());
        productGuiGeFragment.setArguments(bundle);
        fragmentManager=getChildFragmentManager();

        //向详情图片列表传递数据
        productPicFragment.setArguments(bundle1);

        MyViewPagerAdapter viewPagerAdapter=new MyViewPagerAdapter(fragmentManager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);

//        android.support.v4.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
//        transaction.add(R.id.frame_layout_product_detail_and_gui_ge, productPicFragment);
//        transaction.commit();
        return v;
    }



    public void initView(View v) {
        prodcutDetailTextView = (TextView) v.findViewById(R.id.text_product_detail);
        productGuiGeTextView = (TextView) v.findViewById(R.id.text_product_gui_ge);
        viewPager= (ViewPager) v.findViewById(R.id.view_pager_layout_product_detail_and_gui_ge);
        //bufFrameLayout= (FrameLayout) v.findViewById(R.id.frame_layout_product_detail_and_gui_ge);

        //设置点击事件
        prodcutDetailTextView.setOnClickListener(this);
        productGuiGeTextView.setOnClickListener(this);
    }

    /**
     * 返回good
     */
    public Good getGood(){
        return good;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_product_detail:
                reset();
                prodcutDetailTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                viewPager.setCurrentItem(0);
//                android.support.v4.app.FragmentTransaction fragmentTransaction1=fragmentManager.beginTransaction();
//                fragmentTransaction1.replace(R.id.frame_layout_product_detail_and_gui_ge, productPicFragment);
//                fragmentTransaction1.commit();
                break;
            case R.id.text_product_gui_ge:
                reset();
                productGuiGeTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                viewPager.setCurrentItem(1);
//                android.support.v4.app.FragmentTransaction fragmentTransaction2=fragmentManager.beginTransaction();
//                fragmentTransaction2.replace(R.id.frame_layout_product_detail_and_gui_ge, productGuiGeFragment);
//                fragmentTransaction2.commit();
                break;
        }
    }

    /**
     * 将textcolor的颜色设置成黑色
     */
    public void reset(){
        prodcutDetailTextView.setTextColor(getResources().getColor(R.color.myblack));
        productGuiGeTextView.setTextColor(getResources().getColor(R.color.myblack));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                reset();
                prodcutDetailTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
            case 1:
                reset();
                productGuiGeTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * viewpager的适配器
     */
    class MyViewPagerAdapter extends FragmentPagerAdapter{

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

}
