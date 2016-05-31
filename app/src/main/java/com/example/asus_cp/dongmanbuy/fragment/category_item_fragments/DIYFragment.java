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
 * diy定制的内容
 * Created by asus-cp on 2016-05-25.
 */
public class DIYFragment extends Fragment{
    private ListView diyListView;
    private List<String> diys;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.diy_fragment_layout,null);
        diyListView= (ListView) v.findViewById(R.id.list_view_diy);
        init();
        return v;
    }

    private void init() {
        context= MyApplication.getContext();
        diys=new ArrayList<String>();
        diys.add("T恤");
        diys.add("水杯");
        diys.add("抱枕");
        diys.add("布挂画");
        diys.add("鼠标垫");
        diys.add("卡贴");
        diys.add("胸章");
        diys.add("眼镜布");
        diys.add("浴巾");
        diys.add("橡胶课桌垫");
        diys.add("木版画");
        ArrayAdapter arrayAdapter=new ArrayAdapter(context,R.layout.category_tong_yong_item_layout,diys);
        diyListView.setAdapter(arrayAdapter);
        diyListView.setDividerHeight(0);
        diyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,diys.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
