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
 * 书籍的内容
 * Created by asus-cp on 2016-05-25.
 */
public class ShuJiFragment extends BaseFragment {
    private Context context;

    private MyGridViewA shuJiGridView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.shu_ji_fragment_layout,null);
        shuJiGridView= (MyGridViewA) v.findViewById(R.id.grid_view_shu_ji);
        init();
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init() {
        context= getActivity();
        final List<CategoryModel> models=new ArrayList<CategoryModel>();
        CategoryModel model1=new CategoryModel("1595","漫画",R.mipmap.man_hua);
        CategoryModel model2=new CategoryModel("1594","小说",R.mipmap.xiao_shuo);
        models.add(model1);
        models.add(model2);

        CategoryAdapter adapter=new CategoryAdapter(context,models);
        shuJiGridView.setAdapter(adapter);
        shuJiGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, GoodSearchResultActivity.class);
                intent.putExtra(MyConstant.CATEGORY_ID_KEY,models.get(position).getCategoryId());
                startActivity(intent);
            }
        });

    }
}
