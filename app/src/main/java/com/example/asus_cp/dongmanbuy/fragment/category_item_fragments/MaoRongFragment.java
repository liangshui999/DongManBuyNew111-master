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
 * 毛绒的内容
 * Created by asus-cp on 2016-05-25.
 */
public class MaoRongFragment extends Fragment {
    private ListView maoRongListView;
    private List<String> maoRongs;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.mao_rong_fragment_layout,null);
        maoRongListView= (ListView) v.findViewById(R.id.list_view_mao_rong);
        init();
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init() {
        context= MyApplication.getContext();
        maoRongs=new ArrayList<String>();
        maoRongs.add("玩偶");
        maoRongs.add("靠垫");
        maoRongs.add("抱枕");
        maoRongs.add("暖手捂");
        maoRongs.add("垫子");
        maoRongs.add("公仔");
        ArrayAdapter arrayAdapter=new ArrayAdapter(context,R.layout.category_tong_yong_item_layout,maoRongs);
        maoRongListView.setAdapter(arrayAdapter);
        maoRongListView.setDividerHeight(0);
        maoRongListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,maoRongs.get(position),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
