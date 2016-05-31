package com.example.asus_cp.dongmanbuy.fragment.category_item_fragments;

import android.content.Context;
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
import com.example.asus_cp.dongmanbuy.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 箱包内容展示
 * Created by asus-cp on 2016-05-25.
 */
public class XiangBaoFragment extends Fragment{
    private ListView xiangBaoListView;
    private List<String> xiangBaos;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.xiang_bao_fragment_layout,null);
        xiangBaoListView= (ListView) v.findViewById(R.id.list_view_xiang_bao);
        init();
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init() {
        context= MyApplication.getContext();
        xiangBaos=new ArrayList<String>();
        xiangBaos.add("双肩包");
        xiangBaos.add("单肩包");
        xiangBaos.add("电脑包");
        xiangBaos.add("钱包");
        xiangBaos.add("手包");
        xiangBaos.add("旅行箱");
        ArrayAdapter arrayAdapter=new ArrayAdapter(context,R.layout.category_tong_yong_item_layout,xiangBaos);
        xiangBaoListView.setAdapter(arrayAdapter);
        xiangBaoListView.setDividerHeight(0);
        xiangBaoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,xiangBaos.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
