package com.example.asus_cp.dongmanbuy.activity.main_activity_xiang_guan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.adapter.LiuLanJiLuMainActivityAdapter;
import com.example.asus_cp.dongmanbuy.constant.DBConstant;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.db.BookDBOperateHelper;
import com.example.asus_cp.dongmanbuy.db.CursorHandler;
import com.example.asus_cp.dongmanbuy.model.Good;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页点击浏览记录之后浏览记录的展示列表
 * Created by asus-cp on 2016-07-21.
 */
public class LiuLanJiLuListActivity extends Activity implements View.OnClickListener{

    private ImageView daoHangImageView;//导航
    private LinearLayout qingKongLinearLayout;//清空按钮
    private ListView listView;
    private List<Good> goods;
    private BookDBOperateHelper dbHelper;
    private LiuLanJiLuMainActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.liu_lan_ji_lu_activity_layout);
        daoHangImageView= (ImageView) findViewById(R.id.img_dao_hang_liu_lan_ji_lu);
        qingKongLinearLayout= (LinearLayout) findViewById(R.id.ll_qing_kong_liu_lan_ji_lu);
        listView= (ListView) findViewById(R.id.list_liu_lan_ji_lu);

        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        qingKongLinearLayout.setOnClickListener(this);

        dbHelper=new BookDBOperateHelper();
        goods= (List<Good>) dbHelper.queryGoods("0", "20", new CursorHandler() {
            private List<Good> goodsN=new ArrayList<Good>();
            @Override
            public Object handleCursor(Cursor cursor) {
                while(cursor.moveToNext()){
                    Good good=new Good();
                    good.setGoodId(cursor.getString(cursor.getColumnIndex(DBConstant.Good.GOOD_ID)));
                    good.setGoodsImg(cursor.getString(cursor.getColumnIndex(DBConstant.Good.BIG_IMAG)));
                    good.setGoodsThumb(cursor.getString(cursor.getColumnIndex(DBConstant.Good.THUM_IMAG)));
                    good.setGoodsSmallImag(cursor.getString(cursor.getColumnIndex(DBConstant.Good.SMALL_IMAG)));
                    good.setGoodName(cursor.getString(cursor.getColumnIndex(DBConstant.Good.GOOD_NAME)));
                    good.setShopPrice(cursor.getString(cursor.getColumnIndex(DBConstant.Good.SHOP_PRICE)));
                    good.setMarket_price(cursor.getString(cursor.getColumnIndex(DBConstant.Good.MARKET_PRICE)));
                    good.setSalesVolume(cursor.getString(cursor.getColumnIndex(DBConstant.Good.SALE_VOLUME)));
                    good.setGoodsNumber(cursor.getString(cursor.getColumnIndex(DBConstant.Good.GOODS_NUMBER)));
                    goodsN.add(good);
                }
                return goodsN;
            }
        });
        adapter=new LiuLanJiLuMainActivityAdapter(this,goods);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(LiuLanJiLuListActivity.this, ProductDetailActivity.class);
                intent.putExtra(MyConstant.GOOD_KEY,goods.get(position));
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_dao_hang_liu_lan_ji_lu://点击了导航
                finish();
                break;
            case R.id.ll_qing_kong_liu_lan_ji_lu://点击了清空
                goods.clear();
                adapter.notifyDataSetChanged();
                dbHelper.deleteAll();
                break;
        }
    }
}
