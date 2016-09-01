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
import com.example.asus_cp.dongmanbuy.model.CategoryModel;
import com.example.asus_cp.dongmanbuy.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 下装内容
 * Created by asus-cp on 2016-05-25.
 */
public class XiaZhuangFragment extends Fragment{
    private Context context;

    private MyGridViewA xiaZhuangGridView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.xia_zhuang_fragment_layout,null);
        xiaZhuangGridView= (MyGridViewA) v.findViewById(R.id.grid_view_xia_zhuang);
        //init();
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init() {
        context=getActivity();
        final List<CategoryModel> models=new ArrayList<CategoryModel>();
        CategoryModel model=new CategoryModel("1503","牛仔裤",R.mipmap.niu_zi_ku);
        models.add(model);
        CategoryAdapter adapter=new CategoryAdapter(context,models);
        xiaZhuangGridView.setAdapter(adapter);
        xiaZhuangGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, GoodSearchResultActivity.class);
                intent.putExtra(MyConstant.CATEGORY_ID_KEY,models.get(position).getCategoryId());
                startActivity(intent);
            }
        });

    }
}
