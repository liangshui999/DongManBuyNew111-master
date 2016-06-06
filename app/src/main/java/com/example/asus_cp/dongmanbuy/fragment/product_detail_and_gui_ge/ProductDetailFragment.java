package com.example.asus_cp.dongmanbuy.fragment.product_detail_and_gui_ge;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductPicAndGuiGeActivity;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品详情图片所在的碎片,需要动态构建imageview，因为返回的图片数目不定
 * Created by asus-cp on 2016-06-02.
 */
public class ProductDetailFragment extends Fragment{
    private String productUrl="http://www.zmobuy.com/PHP/index.php?url=/goods/desc";
    private RequestQueue requestQueue;
    private Context context;
    private Good good;
    private String tag="ProductDetailFragment";
    private LinearLayout bufLinearLayout;
    private ImageLoadHelper helper;
    private ImageLoader imageLoader;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.product_detail_fragment_layout, null);
        bufLinearLayout= (LinearLayout) v.findViewById(R.id.ll_product_pic_detail);
        context=getActivity();
        helper=new ImageLoadHelper();
        imageLoader=helper.getImageLoader();
        requestQueue=MyApplication.getRequestQueue();
        ProductPicAndGuiGeActivity productPicAndGuiGeActivity= (ProductPicAndGuiGeActivity) getActivity();
        good=productPicAndGuiGeActivity.getGood();
        MyLog.d(tag,"商品id"+good.getGoodId());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, productUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag,"返回的数据为："+s);
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        String url=jsonArray.getString(i);
                        ImageView imageView=new ImageView(context);
                        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity= Gravity.CENTER;

                        imageView.setLayoutParams(layoutParams);
                        ImageLoader.ImageListener imageListener=imageLoader.getImageListener(imageView,R.mipmap.yu_jia_zai,
                                R.mipmap.yu_jia_zai);
                        imageLoader.get(url,imageListener,500,500);
                        bufLinearLayout.addView(imageView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                map.put("goods_id",good.getGoodId());
                return map;
            }
        };
        requestQueue.add(stringRequest);
        return v;
    }
}