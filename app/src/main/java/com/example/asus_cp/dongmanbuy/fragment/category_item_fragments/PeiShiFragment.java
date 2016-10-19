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
 * 配饰的内容
 * Created by asus-cp on 2016-05-25.
 */
public class PeiShiFragment extends BaseFragment {
    private Context context;

    private MyGridViewA peiShiGridView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.pei_shi_fragment_layout,null);
        peiShiGridView= (MyGridViewA) v.findViewById(R.id.grid_view_pei_shi);
        init();
        return v;
    }

    /**
     * 初始化方法
     */
    private void init() {
        context=getActivity();
        final List<CategoryModel> models=new ArrayList<CategoryModel>();
        CategoryModel model=new CategoryModel("1589","挂件",R.mipmap.gua_jian);
        CategoryModel mode2=new CategoryModel("1588","帽子",R.mipmap.mao_zi);
        CategoryModel mode3=new CategoryModel("1591","手套",R.mipmap.shou_tao);
        CategoryModel mode4=new CategoryModel("1804","头饰发饰",R.mipmap.tou_shi_fa_shi);
        CategoryModel mode5=new CategoryModel("1771","袜子",R.mipmap.wa_zi);
        CategoryModel mode6=new CategoryModel("1590","围巾",R.mipmap.wei_jin);
        CategoryModel mode7=new CategoryModel("1592","鞋子",R.mipmap.xie_zi);
        CategoryModel mode8=new CategoryModel("1802","腰带",R.mipmap.yao_dai);

        models.add(model);
        models.add(mode2);
        models.add(mode3);
        models.add(mode4);
        models.add(mode5);
        models.add(mode6);
        models.add(mode7);
        models.add(mode8);

        CategoryAdapter adapter=new CategoryAdapter(context,models);
        peiShiGridView.setAdapter(adapter);
        peiShiGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, GoodSearchResultActivity.class);
                intent.putExtra(MyConstant.CATEGORY_ID_KEY,models.get(position).getCategoryId());
                startActivity(intent);
            }
        });
    }
}
