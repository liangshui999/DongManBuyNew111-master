package com.example.asus_cp.dongmanbuy.fragment.category_item_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.activity.search.GoodSearchResultActivity;
import com.example.asus_cp.dongmanbuy.adapter.CategoryAdapter;
import com.example.asus_cp.dongmanbuy.adapter.CategoryGridViewAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.model.CategoryModel;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.CategoryImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 宅品的内容
 * Created by asus-cp on 2016-05-25.
 */
public class ZaiPingFragment extends Fragment{

    private String tag="ZaiPingFragment";

    private MyGridViewA zaiPinGridView;//

    private Context context;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.zai_ping_fragment_layout,null);
        zaiPinGridView= (MyGridViewA) v.findViewById(R.id.grid_view_zai_pin);
        init();
        return v;
    }

    /**
     * 初始化的方法
     * @param v
     */
    private void init() {
        context= getActivity();//注意这个context不能使用Myapplication.getconxt(),否则会报异常;
        final List<CategoryModel> models=new ArrayList<CategoryModel>();
        CategoryModel model1=new CategoryModel("1515","明信片",R.mipmap.ming_xin_pian);
        CategoryModel model2=new CategoryModel("1535","扑克牌",R.mipmap.pu_ke_pai);
        CategoryModel model3=new CategoryModel("1554","台历",R.mipmap.tai_li);
        CategoryModel model4=new CategoryModel("1800","音箱",R.mipmap.yin_xiang);
        CategoryModel model5=new CategoryModel("1793","雨伞",R.mipmap.yu_shan);
        models.add(model1);
        models.add(model2);
        models.add(model3);
        models.add(model4);
        models.add(model5);
        CategoryAdapter adapter=new CategoryAdapter(context,models);
        zaiPinGridView.setAdapter(adapter);
        zaiPinGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, GoodSearchResultActivity.class);
                intent.putExtra(MyConstant.CATEGORY_ID_KEY,models.get(position).getCategoryId());
                startActivity(intent);
            }
        });

    }







}
