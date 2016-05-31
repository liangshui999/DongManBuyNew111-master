package com.example.asus_cp.dongmanbuy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.util.MyApplication;

/**
 * 购物车的内容
 * Created by asus-cp on 2016-05-19.
 */
public class ShoppingCarFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private RequestQueue requestQueue;

    private ImageView backImageView;//返回
    private Button quGuangGuangButton;//去逛一逛
    private RecyclerView mayBeYouWant;//你可能想要

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.shopping_car_fragment_layout,null);
        init(v);
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init(View v) {
        context=getActivity();
        requestQueue= MyApplication.getRequestQueue();
        backImageView= (ImageView) v.findViewById(R.id.img_shoping_car_dao_hang);
        quGuangGuangButton= (Button) v.findViewById(R.id.btn_qu_guang_guang);
        mayBeYouWant= (RecyclerView) v.findViewById(R.id.recycle_view_may_be_you_want);
        backImageView.setOnClickListener(this);
        quGuangGuangButton.setOnClickListener(this);

        //你可能想要部分,该部分暂时没有接口，适配器都已经做好了，maybeyouwantadapter，只要有接口之后就可以做了
        //StringRequest youWantRequst=new StringRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_shoping_car_dao_hang://返回按钮的点击事件
                Toast.makeText(context,"点击了返回按钮",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_qu_guang_guang://去逛一逛的点击事件
                Toast.makeText(context,"点击了去逛一逛",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
