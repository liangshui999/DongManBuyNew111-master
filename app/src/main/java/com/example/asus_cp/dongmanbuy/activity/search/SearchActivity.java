package com.example.asus_cp.dongmanbuy.activity.search;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.adapter.ShopStreetSpinnerAdapter;
import com.example.asus_cp.dongmanbuy.constant.DBConstant;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.customview.MyListView;
import com.example.asus_cp.dongmanbuy.db.CursorHandler;
import com.example.asus_cp.dongmanbuy.db.SearchRecordDBOperateHelper;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索界面
 * Created by asus-cp on 2016-06-29.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener{

    private String tag="SearchActivity";
    private ImageView daoHangImageView;//导航
    private TextView searchCategoryTextView;//选择搜索的类目，是店铺还是商品
    //private Spinner spinner;
    private EditText searchEditText;//搜索框
    private TextView searchTextView;//搜索按钮
    private MyGridViewA hotSearchGridView;//热门搜索
    private ImageView deleteImageView;//删除搜索记录
    private MyListView recentSearchListView;//最近搜索
    private TextView closeTextView;//点击关闭

    private String category;//搜索的类别

    private SearchRecordDBOperateHelper dbHelper;

    private ArrayAdapter listAdapter;

    private List<String> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_layout);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        initView();

        dbHelper=new SearchRecordDBOperateHelper();
        category="商品";
       /* //设置spinner
        final List<String> caterories=new ArrayList<String>();
        caterories.add("商品");
        caterories.add("店铺");
        ShopStreetSpinnerAdapter spinnerAdapter=new ShopStreetSpinnerAdapter(this,caterories);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = caterories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        //设置gridview
        final List<String> hots=new ArrayList<String>();
        hots.add("海贼王");
        hots.add("排球少年");
        hots.add("fate卫衣");
        hots.add("动漫T恤");

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.hot_search_list_item_layout,R.id.text_hot_search_list_item,hots);
        hotSearchGridView.setAdapter(arrayAdapter);
        hotSearchGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toSearchResultActivity(hots.get(position));
            }
        });


        //查询搜索记录
        records= (List<String>) dbHelper.queryAll("0", "20", new CursorHandler() {
            private List<String> recordsN=new ArrayList<String>();
            @Override
            public Object handleCursor(Cursor cursor) {
                while(cursor.moveToNext()){
                    String s=cursor.getString(cursor.getColumnIndex(DBConstant.SearchRecord.KEY_WORD));
                    recordsN.add(s);
                }
                return recordsN;
            }
        });


        listAdapter=new ArrayAdapter(this,R.layout.recent_search_item,R.id.text_recent_search_item,records);
        recentSearchListView.setAdapter(listAdapter);
        recentSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toSearchResultActivity(records.get(position));
            }
        });

        //清除搜索框的焦点
        searchEditText.clearFocus();


        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        searchCategoryTextView.setOnClickListener(this);
        searchTextView.setOnClickListener(this);
        deleteImageView.setOnClickListener(this);
        closeTextView.setOnClickListener(this);
    }


    /**
     * 初始化视图
     */
    public void initView() {
        daoHangImageView= (ImageView) findViewById(R.id.img_dao_hang_search);
        searchCategoryTextView= (TextView) findViewById(R.id.text_search_category);
        //spinner= (Spinner) findViewById(R.id.spinner_search);
        searchEditText= (EditText) findViewById(R.id.edit_search);
        searchTextView= (TextView) findViewById(R.id.text_search);
        hotSearchGridView= (MyGridViewA) findViewById(R.id.grid_view_hot_search);
        deleteImageView= (ImageView) findViewById(R.id.img_delete_recent_search);
        recentSearchListView= (MyListView) findViewById(R.id.list_view_recent_search);
        closeTextView= (TextView) findViewById(R.id.text_close_search);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_dao_hang_search://点击了导航按钮
                finish();
                break;
            case R.id.text_search_category://点击了搜索类别
                if("商品".equals(searchCategoryTextView.getText().toString())){
                    searchCategoryTextView.setText("店铺");
                    category="店铺";
                }else if("店铺".equals(searchCategoryTextView.getText().toString())){
                    searchCategoryTextView.setText("商品");
                    category="商品";
                }
                break;
            case R.id.text_search://点击了搜索按钮
                String searchContent=searchEditText.getText().toString();
                toSearchResultActivity(searchContent);
                break;
            case R.id.img_delete_recent_search://点击了删除按钮
                dbHelper.deleteAll();
                flushView();
                break;
            case R.id.text_close_search://点击了关闭搜索按钮
                finish();
                break;
        }
    }


    /**
     * 刷新试图,注意为什么要先清空，再添加，还要弄出个临时的
     */
    private void flushView() {
        List<String> temp= (List<String>) dbHelper.queryAll("0", "20", new CursorHandler() {
            private List<String> recordsN=new ArrayList<String>();
            @Override
            public Object handleCursor(Cursor cursor) {
                while(cursor.moveToNext()){
                    String s=cursor.getString(cursor.getColumnIndex(DBConstant.SearchRecord.KEY_WORD));
                    recordsN.add(s);
                }
                return recordsN;
            }
        });
        records.clear();
        records.addAll(temp);
        MyLog.d(tag,"集合的大小是："+records.size());
        listAdapter.notifyDataSetChanged();
    }


    /**
     * 跳转到搜索结果的界面
     * @param searchContent 搜索的关键字
     */
    private void toSearchResultActivity(String searchContent) {
        if("商品".equals(category)){  //搜索商品用search接口
            Intent toResultIntent=new Intent(this,GoodSearchResultActivity.class);
            toResultIntent.putExtra(MyConstant.SEARCH_CONTENT_KEY,searchContent);
            startActivityForResult(toResultIntent, 1);
        }else if("店铺".equals(category)){    //搜索店铺需要用index接口
            Intent toShopResultIntent=new Intent(this,ShopSearchResultActivity.class);
            toShopResultIntent.putExtra(MyConstant.SEARCH_CONTENT_KEY,searchContent);
            startActivityForResult(toShopResultIntent, 1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        flushView();
    }
}
