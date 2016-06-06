package com.example.asus_cp.dongmanbuy.activity.product_detail;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.fragment.HomeFragment;
import com.example.asus_cp.dongmanbuy.fragment.product_detail_and_gui_ge.ProductDetailFragment;
import com.example.asus_cp.dongmanbuy.fragment.product_detail_and_gui_ge.ProductGuiGeFragment;
import com.example.asus_cp.dongmanbuy.model.Good;

/**
 * 商品详情的图片和规格参数
 * Created by asus-cp on 2016-06-02.
 */
public class ProductPicAndGuiGeActivity extends FragmentActivity implements View.OnClickListener{
    private TextView prodcutDetailTextView;//商品详情选项
    private TextView productGuiGeTextView;//商品规格选项
    private FrameLayout bufFrameLayout;//装载碎片的容器
    private Good good;
    private ProductDetailFragment productDetailFragment;
    private ProductGuiGeFragment productGuiGeFragment;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.product_xiangqing_and_gui_ge);
        prodcutDetailTextView = (TextView) findViewById(R.id.text_product_detail);
        productGuiGeTextView = (TextView) findViewById(R.id.text_product_gui_ge);
        bufFrameLayout= (FrameLayout) findViewById(R.id.frame_layout_product_detail_and_gui_ge);
        good=getIntent().getParcelableExtra(HomeFragment.GOOD_KEY);

        prodcutDetailTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        productDetailFragment=new ProductDetailFragment();
        productGuiGeFragment=new ProductGuiGeFragment();
        fragmentManager=getSupportFragmentManager();

        android.support.v4.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.frame_layout_product_detail_and_gui_ge,productDetailFragment);
        transaction.commit();

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
                android.support.v4.app.FragmentTransaction fragmentTransaction1=fragmentManager.beginTransaction();
                fragmentTransaction1.replace(R.id.frame_layout_product_detail_and_gui_ge,productDetailFragment);
                fragmentTransaction1.commit();
                break;
            case R.id.text_product_gui_ge:
                reset();
                productGuiGeTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                android.support.v4.app.FragmentTransaction fragmentTransaction2=fragmentManager.beginTransaction();
                fragmentTransaction2.replace(R.id.frame_layout_product_detail_and_gui_ge, productGuiGeFragment);
                fragmentTransaction2.commit();
                break;
        }
    }

    /**
     * 将textcolor的颜色设置成黑色
     */
    public void reset(){
        prodcutDetailTextView.setTextColor(getResources().getColor(R.color.black));
        productGuiGeTextView.setTextColor(getResources().getColor(R.color.black));
    }
}