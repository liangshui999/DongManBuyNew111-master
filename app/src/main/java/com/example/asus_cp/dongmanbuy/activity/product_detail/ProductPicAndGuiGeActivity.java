package com.example.asus_cp.dongmanbuy.activity.product_detail;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.fragment.HomeFragment;
import com.example.asus_cp.dongmanbuy.fragment.product_detail_and_gui_ge.ProductPicFragment;
import com.example.asus_cp.dongmanbuy.fragment.product_detail_and_gui_ge.ProductGuiGeFragment;
import com.example.asus_cp.dongmanbuy.model.Good;

/**
 * 商品详情的图片和规格参数,此类作废，由对应的碎片代替
 * Created by asus-cp on 2016-06-02.
 */
@Deprecated
public class ProductPicAndGuiGeActivity{

//    private TextView prodcutDetailTextView;//商品详情选项
//    private TextView productGuiGeTextView;//商品规格选项
//    private FrameLayout bufFrameLayout;//装载碎片的容器
//    private Good good;
//    private ProductPicFragment productDetailFragment;
//    private ProductGuiGeFragment productGuiGeFragment;
//    private FragmentManager fragmentManager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentLayout(R.layout.product_xiangqing_and_gui_ge);
//        setTitle(R.string.gui_ge);
//        initView();
//
//        good=getIntent().getParcelableExtra(HomeFragment.GOOD_KEY);
//        prodcutDetailTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
//        productDetailFragment=new ProductPicFragment();
//        productGuiGeFragment=new ProductGuiGeFragment();
//        //向规格碎片传递数据
//        Bundle bundle=new Bundle();
//        bundle.putString(MyConstant.GOOD_ID_KEY, good.getGoodId());
//        productGuiGeFragment.setArguments(bundle);
//        fragmentManager=getSupportFragmentManager();
//
//        android.support.v4.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
//        transaction.add(R.id.frame_layout_product_detail_and_gui_ge, productDetailFragment);
//        transaction.commit();
//
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        setIsNeedTongJiPage(false);
//    }
//
//    @Override
//    public void initView() {
//        prodcutDetailTextView = (TextView) findViewById(R.id.text_product_detail);
//        productGuiGeTextView = (TextView) findViewById(R.id.text_product_gui_ge);
//        bufFrameLayout= (FrameLayout) findViewById(R.id.frame_layout_product_detail_and_gui_ge);
//
//        //设置点击事件
//        prodcutDetailTextView.setOnClickListener(this);
//        productGuiGeTextView.setOnClickListener(this);
//    }
//
//    /**
//     * 返回good
//     */
//    public Good getGood(){
//        return good;
//    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.text_product_detail:
//                reset();
//                prodcutDetailTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
//                android.support.v4.app.FragmentTransaction fragmentTransaction1=fragmentManager.beginTransaction();
//                fragmentTransaction1.replace(R.id.frame_layout_product_detail_and_gui_ge,productDetailFragment);
//                fragmentTransaction1.commit();
//                break;
//            case R.id.text_product_gui_ge:
//                reset();
//                productGuiGeTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
//                android.support.v4.app.FragmentTransaction fragmentTransaction2=fragmentManager.beginTransaction();
//                fragmentTransaction2.replace(R.id.frame_layout_product_detail_and_gui_ge, productGuiGeFragment);
//                fragmentTransaction2.commit();
//                break;
//        }
//    }
//
//    /**
//     * 将textcolor的颜色设置成黑色
//     */
//    public void reset(){
//        prodcutDetailTextView.setTextColor(getResources().getColor(R.color.myblack));
//        productGuiGeTextView.setTextColor(getResources().getColor(R.color.myblack));
//    }
}
