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
 * 毛绒的内容
 * Created by asus-cp on 2016-05-25.
 */
public class MaoRongFragment extends Fragment {
    private Context context;

    private MyGridViewA maoRongGridView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.mao_rong_fragment_layout,null);
        maoRongGridView= (MyGridViewA) v.findViewById(R.id.grid_view_mao_rong);
        //init();
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init() {
        context= getActivity();
        final List<CategoryModel> models=new ArrayList<CategoryModel>();
        CategoryModel model1=new CategoryModel("1582","抱枕",R.mipmap.bao_zhen);
        CategoryModel model2=new CategoryModel("1584","公仔",R.mipmap.gong_zi);
        models.add(model1);
        models.add(model2);
        CategoryAdapter adapter=new CategoryAdapter(context,models);
        maoRongGridView.setAdapter(adapter);
        maoRongGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, GoodSearchResultActivity.class);
                intent.putExtra(MyConstant.CATEGORY_ID_KEY,models.get(position).getCategoryId());
                startActivity(intent);
            }
        });

    }
}
