package com.example.asus_cp.dongmanbuy.fragment.category_item_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.CategoryListAdapter;
import com.example.asus_cp.dongmanbuy.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示商品列表，比如上装，下装箱包等选项
 * Created by asus-cp on 2016-05-25.
 */
public class CategoryListFragment extends Fragment {
    private ListView categoryListListView;
    private List<String> categories;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.category_list_fragment_layout,null);
        categoryListListView= (ListView) v.findViewById(R.id.list_view_category_list);
        init();
        return v;
    }

    private void init() {
        context= MyApplication.getContext();
        categories=new ArrayList<String>();
        categories.add("上装");
        categories.add("下装");
        categories.add("箱包");
        categories.add("宅品");
        categories.add("毛绒");
        categories.add("配饰");
        categories.add("书籍");
        categories.add("DIY定制");
        categories.add("模玩，手");
        CategoryListAdapter categoryListAdapter=new CategoryListAdapter(context,categories);
        categoryListListView.setAdapter(categoryListAdapter);
    }

    /**
     * 返回该碎片所持有的listview
     */
    public ListView getListView(){
        return categoryListListView;
    }
}
