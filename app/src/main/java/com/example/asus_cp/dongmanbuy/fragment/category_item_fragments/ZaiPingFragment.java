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
 * 宅品的内容
 * Created by asus-cp on 2016-05-25.
 */
public class ZaiPingFragment extends Fragment implements View.OnClickListener{
    private MyGridView mingXinPianGridView;//明信片
    private MyGridView puKePaiGridView;//扑克牌
    private MyGridView taiLiGridView;//台历

    private TextView mingXinPianTextView;
    private TextView daLiBaoTextView;
    private TextView puKePaiTextView;
    private TextView taiLiTextView;
    private TextView qianBaoTextView;
    private TextView biJiBenTextView;
    private TextView tongRenTextView;

    private Context context;

    private CategoryImageLoadHelper helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.zai_ping_fragment_layout,null);
        init(v);
        return v;
    }

    /**
     * 初始化的方法
     * @param v
     */
    private void init(View v) {
        context= MyApplication.getContext();
        helper=new CategoryImageLoadHelper();
        mingXinPianGridView= (MyGridView) v.findViewById(R.id.grid_view_ming_xin_pian);
        puKePaiGridView= (MyGridView) v.findViewById(R.id.grid_view_pu_ke_pai);
        taiLiGridView= (MyGridView) v.findViewById(R.id.grid_view_tai_li);

        helper.asynLoadCatgory(mingXinPianGridView,"明信片","18");
        helper.asynLoadCatgory(puKePaiGridView,"扑克牌","18");
        helper.asynLoadCatgory(taiLiGridView,"台历","18");

        mingXinPianTextView= (TextView) v.findViewById(R.id.text_ming_xin_pian);
        daLiBaoTextView= (TextView) v.findViewById(R.id.text_da_li_bao);
        puKePaiTextView= (TextView) v.findViewById(R.id.text_pu_ke_pai);
        taiLiTextView= (TextView) v.findViewById(R.id.text_tai_li);
        qianBaoTextView= (TextView) v.findViewById(R.id.text_qian_bao);
        biJiBenTextView= (TextView) v.findViewById(R.id.text_bi_ji_ben);
        tongRenTextView= (TextView) v.findViewById(R.id.text_tong_ren);

        mingXinPianTextView.setOnClickListener(this);
        daLiBaoTextView.setOnClickListener(this);
        puKePaiTextView.setOnClickListener(this);
        taiLiTextView.setOnClickListener(this);
        qianBaoTextView.setOnClickListener(this);
        biJiBenTextView.setOnClickListener(this);
        tongRenTextView.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.text_ming_xin_pian:
                Toast.makeText(context,"点击了明信片",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_da_li_bao:
                Toast.makeText(context,"点击了大礼包",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_pu_ke_pai:
                Toast.makeText(context,"点击了扑克牌",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_tai_li:
                Toast.makeText(context,"点击了台历",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_qian_bao:
                Toast.makeText(context,"点击了钱包",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_bi_ji_ben:
                Toast.makeText(context,"点击了笔记本",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_tong_ren:
                Toast.makeText(context,"点击了同人",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
