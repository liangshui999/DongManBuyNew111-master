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
 * 配饰的内容
 * Created by asus-cp on 2016-05-25.
 */
public class PeiShiFragment extends Fragment {
    private ListView peiShiListView;
    private List<String> peiShies;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.pei_shi_fragment_layout,null);
        peiShiListView= (ListView) v.findViewById(R.id.list_view_pei_shi);
        init();
        return v;
    }

    /**
     * 初始化方法
     */
    private void init() {
        context= MyApplication.getContext();
        peiShies=new ArrayList<String>();
        peiShies.add("帽子");
        peiShies.add("挂件");
        peiShies.add("围巾");
        peiShies.add("手套");
        peiShies.add("鞋子");
        ArrayAdapter arrayAdapter=new ArrayAdapter(context,R.layout.category_tong_yong_item_layout,peiShies);
        peiShiListView.setAdapter(arrayAdapter);
        peiShiListView.setDividerHeight(0);
        peiShiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,peiShies.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
