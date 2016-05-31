package com.example.asus_cp.dongmanbuy.fragment.category_item_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.customview.MyGridView;
import com.example.asus_cp.dongmanbuy.util.CategoryImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;

/**
 * 模型的内容
 * Created by asus-cp on 2016-05-25.
 */
public class MoXingFragment extends Fragment implements View.OnClickListener{
    private TextView shouBanTextView;
    private TextView moXingTextView;
    private MyGridView shouBanGridView;
    private MyGridView moXingGridView;
    private Context context;
    private CategoryImageLoadHelper helper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.mo_xing_fragment_layout,null);
        init(v);
        return v;
    }

    private void init(View v) {
        context= MyApplication.getContext();
        helper=new CategoryImageLoadHelper();
        shouBanTextView= (TextView) v.findViewById(R.id.text_shou_ban);
        moXingTextView= (TextView) v.findViewById(R.id.text_mo_xing);
        shouBanGridView= (MyGridView) v.findViewById(R.id.grid_view_shou_ban);
        moXingGridView= (MyGridView) v.findViewById(R.id.grid_view_mo_xing);

        shouBanTextView.setOnClickListener(this);
        moXingTextView.setOnClickListener(this);

        helper.asynLoadCatgory(shouBanGridView, "手办", "9");
        helper.asynLoadCatgory(moXingGridView,"模型","12");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_shou_ban:
                Toast.makeText(context,"点击了手办",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_mo_xing:
                Toast.makeText(context,"点击了模型",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
