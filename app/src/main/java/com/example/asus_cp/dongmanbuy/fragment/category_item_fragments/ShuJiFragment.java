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
 * 书籍的内容
 * Created by asus-cp on 2016-05-25.
 */
public class ShuJiFragment extends Fragment {
    private ListView shuJiListView;
    private List<String> shuJis;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.shu_ji_fragment_layout,null);
        shuJiListView= (ListView) v.findViewById(R.id.list_view_shu_ji);
        init();
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init() {
        context= MyApplication.getContext();
        shuJis=new ArrayList<String>();
        shuJis.add("小说");
        shuJis.add("漫画");
        shuJis.add("画集");
        shuJis.add("线稿");
        ArrayAdapter arrayAdapter=new ArrayAdapter(context,R.layout.category_tong_yong_item_layout,shuJis);
        shuJiListView.setAdapter(arrayAdapter);
        shuJiListView.setDividerHeight(0);
        shuJiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,shuJis.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
