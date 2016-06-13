package com.example.asus_cp.dongmanbuy.activity.dian_pu_jie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.adapter.ShopProductBigListAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopProductGridAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopProductSmallListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.Good;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺商品排序的界面
 * Created by asus-cp on 2016-06-12.
 */
public class ShopProdcutSortActivity extends Activity implements View.OnClickListener{
    private ImageView daoHangImageView;//导航的imageview
    private SearchView searchView;//搜索框
    private TextView categoryTextView;//分类的按钮
    private TextView zongHeTextView;//综合
    private TextView xinPinTextView;//新品
    private TextView xiaoLiangTextView;//销量
    private TextView priceTextView;//价格
    private ImageView displayYangShiImageView;//分类方式
    private GridView productGridView;//商品列表
    private ListView productListViewSmall;//商品列表
    private ListView productListViewBig;//大的商品列表

    private int count=1;//展示样式
    public static final int KA_PIAN=1;//卡片的样式
    public static final int LIST_SMALL=2;
    public static final int LIST_BIG=0;

    private ArrayList<Good> goods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);setContentView(R.layout.shop_product_sort_activity_layout);
        init();

    }

    /**
     * 初始化的方法
     */
    private void init() {
        goods= (ArrayList<Good>) getIntent().getSerializableExtra(MyConstant.FROM_SHOP_HOME_TO_SHOP_PRODUCT_SORT_KEY);

        daoHangImageView= (ImageView) findViewById(R.id.img_shop_product_sort_dao_hang);
        searchView= (SearchView) findViewById(R.id.search_view_shop_product_sort);
        categoryTextView= (TextView) findViewById(R.id.text_category_shop_product_sort);
        zongHeTextView= (TextView) findViewById(R.id.text_zong_he_sort);
        xinPinTextView= (TextView) findViewById(R.id.text_xin_pin_sort);
        xiaoLiangTextView= (TextView) findViewById(R.id.text_xiao_liang_sort);
        priceTextView= (TextView) findViewById(R.id.text_price_sort);
        displayYangShiImageView= (ImageView) findViewById(R.id.img_display_style_sort);
        productGridView= (GridView) findViewById(R.id.grid_view_product_sort);
        productListViewSmall= (ListView) findViewById(R.id.list_view_product_sort_small);
        productListViewBig= (ListView) findViewById(R.id.list_view_product_sort_big);

        ShopProductGridAdapter shopProductGridAdapter=new ShopProductGridAdapter(this,goods);
        productGridView.setAdapter(shopProductGridAdapter);
        productGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShopProdcutSortActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                Intent gridIntent=new Intent(ShopProdcutSortActivity.this, ProductDetailActivity.class);
                gridIntent.putExtra(MyConstant.GOOD_KEY,goods.get(position));
                startActivity(gridIntent);
            }
        });


        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        categoryTextView.setOnClickListener(this);
        zongHeTextView.setOnClickListener(this);
        xinPinTextView.setOnClickListener(this);
        xiaoLiangTextView.setOnClickListener(this);
        priceTextView.setOnClickListener(this);
        displayYangShiImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_shop_product_sort_dao_hang://导航
                //Toast.makeText(this,"点击了导航",Toast.LENGTH_SHORT).show();
                finish();
            break;
            case R.id.text_category_shop_product_sort://分类
                Toast.makeText(this,"点击了分类",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_zong_he_sort://综合
                Toast.makeText(this,"点击了综合",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_xin_pin_sort://新品
                Toast.makeText(this,"点击了新品",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_xiao_liang_sort://销量
                Toast.makeText(this,"点击了销量",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_price_sort://价格
                Toast.makeText(this,"点击了价格",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_display_style_sort://展示样式
                //Toast.makeText(this,"点击了展示样式",Toast.LENGTH_SHORT).show();
                count++;
                if(count%3==KA_PIAN){
                    displayYangShiImageView.setBackgroundResource(R.mipmap.kapian);
                    productListViewSmall.setVisibility(View.GONE);
                    productListViewBig.setVisibility(View.GONE);
                    productGridView.setVisibility(View.VISIBLE);
                    ShopProductGridAdapter shopProductGridAdapter=new ShopProductGridAdapter(this,goods);
                    productGridView.setAdapter(shopProductGridAdapter);
                    productGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(ShopProdcutSortActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                            Intent gridIntent=new Intent(ShopProdcutSortActivity.this, ProductDetailActivity.class);
                            gridIntent.putExtra(MyConstant.GOOD_KEY,goods.get(position));
                            startActivity(gridIntent);
                        }
                    });
                }else if(count%3==LIST_SMALL){
                    displayYangShiImageView.setBackgroundResource(R.mipmap.liebiao);
                    productGridView.setVisibility(View.GONE);
                    productListViewBig.setVisibility(View.GONE);
                    productListViewSmall.setVisibility(View.VISIBLE);
                    ShopProductSmallListAdapter shopProductSmallListAdapter=new ShopProductSmallListAdapter(this,goods);
                    productListViewSmall.setAdapter(shopProductSmallListAdapter);
                    productListViewSmall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(ShopProdcutSortActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                            Intent smallIntent=new Intent(ShopProdcutSortActivity.this, ProductDetailActivity.class);
                            smallIntent.putExtra(MyConstant.GOOD_KEY,goods.get(position));
                            startActivity(smallIntent);
                        }
                    });
                }else if(count%3==LIST_BIG){
                    displayYangShiImageView.setBackgroundResource(R.mipmap.fangxing);
                    productGridView.setVisibility(View.GONE);
                    productListViewSmall.setVisibility(View.GONE);
                    productListViewBig.setVisibility(View.VISIBLE);
                    ShopProductBigListAdapter shopProductBigListAdapter=new ShopProductBigListAdapter(this,goods);
                    productListViewBig.setAdapter(shopProductBigListAdapter);
                    productListViewBig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(ShopProdcutSortActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                            Intent bigIntent=new Intent(ShopProdcutSortActivity.this, ProductDetailActivity.class);
                            bigIntent.putExtra(MyConstant.GOOD_KEY,goods.get(position));
                            startActivity(bigIntent);
                        }
                    });
                }
                break;
        }
    }
}
