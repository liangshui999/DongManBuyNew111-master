package com.example.asus_cp.dongmanbuy.fragment.category_item_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.search.GoodSearchResultActivity;
import com.example.asus_cp.dongmanbuy.adapter.CategoryAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.fragment.BaseFragment;
import com.example.asus_cp.dongmanbuy.model.CategoryModel;
import com.example.asus_cp.dongmanbuy.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 箱包内容展示
 * Created by asus-cp on 2016-05-25.
 */
public class XiangBaoFragment extends BaseFragment{
    private Context context;
    private MyGridViewA xiangBaoGridView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.xiang_bao_fragment_layout,null);
        xiangBaoGridView= (MyGridViewA) v.findViewById(R.id.grid_view_xiang_bao);
        init();
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init() {
        context=getActivity();
        final List<CategoryModel> models=new ArrayList<CategoryModel>();
        CategoryModel model=new CategoryModel("1513","旅行箱",R.mipmap.lv_xing_xiang);
        CategoryModel mode2=new CategoryModel("1512","钱包",R.mipmap.qian_bao);
        CategoryModel mode3=new CategoryModel("1510","双肩包",R.mipmap.shuang_jian_bao);
        models.add(model);
        models.add(mode2);
        models.add(mode3);
        CategoryAdapter adapter=new CategoryAdapter(context,models);
        xiangBaoGridView.setAdapter(adapter);
        xiangBaoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, GoodSearchResultActivity.class);
                intent.putExtra(MyConstant.CATEGORY_ID_KEY,models.get(position).getCategoryId());
                startActivity(intent);
            }
        });

    }
}
