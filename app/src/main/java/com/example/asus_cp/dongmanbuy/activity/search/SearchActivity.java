package com.example.asus_cp.dongmanbuy.activity.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.ShopStreetSpinnerAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.customview.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索界面
 * Created by asus-cp on 2016-06-29.
 */
public class SearchActivity extends Activity implements View.OnClickListener{

    private ImageView daoHangImageView;//导航
    private Spinner spinner;
    private EditText searchEditText;//搜索框
    private TextView searchTextView;//搜索按钮
    private MyGridViewA hotSearchGridView;//热门搜索
    private ImageView deleteImageView;//删除搜索记录
    private MyListView recentSearchListView;//最近搜索
    private TextView closeTextView;//点击关闭

    private String category;//搜索的类别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_activity_layout);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        daoHangImageView= (ImageView) findViewById(R.id.img_dao_hang_search);
        spinner= (Spinner) findViewById(R.id.spinner_search);
        searchEditText= (EditText) findViewById(R.id.edit_search);
        searchTextView= (TextView) findViewById(R.id.text_search);
        hotSearchGridView= (MyGridViewA) findViewById(R.id.grid_view_hot_search);
        deleteImageView= (ImageView) findViewById(R.id.img_delete_recent_search);
        recentSearchListView= (MyListView) findViewById(R.id.list_view_recent_search);
        closeTextView= (TextView) findViewById(R.id.text_close_search);


        category="商品";
        //设置spinner
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
        });


        //设置gridview
        List<String> hots=new ArrayList<String>();
        hots.add("海贼王");
        hots.add("排球少年");
        hots.add("fate卫衣");
        hots.add("动漫T恤");

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.hot_search_list_item_layout,R.id.text_hot_search_list_item,hots);
        hotSearchGridView.setAdapter(arrayAdapter);

        //清除搜索框的焦点
        searchEditText.clearFocus();


        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        searchTextView.setOnClickListener(this);
        deleteImageView.setOnClickListener(this);
        closeTextView.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_dao_hang_search://点击了导航按钮
                finish();
                break;
            case R.id.text_search://点击了搜索按钮
                if("商品".equals(category)){  //搜索商品用search接口
                    String searchContent=searchEditText.getText().toString();
                    Intent toResultIntent=new Intent(this,GoodSearchResultActivity.class);
                    toResultIntent.putExtra(MyConstant.SEARCH_CONTENT_KEY,searchContent);
                    startActivity(toResultIntent);
                }else if("店铺".equals(category)){    //搜索店铺需要用index接口

                }

                break;
            case R.id.img_delete_recent_search://点击了删除按钮
                Toast.makeText(this,"点击了删除按钮",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_close_search://点击了关闭搜索按钮
                Toast.makeText(this,"点击了关闭搜索按钮",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
