package com.example.asus_cp.dongmanbuy.fragment.product_detail_and_gui_ge;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductPicAndGuiGeActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.fragment.BaseFragment;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.net.MyImageRequest;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
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
public class ProductPicFragment extends BaseFragment{
    private String productUrl="http://www.zmobuy.com/PHP/index.php?url=/goods/desc";
    private RequestQueue requestQueue;
    private Context context;
    private Good good;
    private String tag="ProductPicFragment";
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
        Bundle bundle=getArguments();
        good=bundle.getParcelable(MyConstant.GOOD_KEY);
        MyLog.d(tag,"商品id"+good.getGoodId());

        //弹出正在加载的对话框
        DialogHelper.showDialog(context);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, productUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DialogHelper.dissmisDialog();//关闭正在加载的对话框
                MyLog.d(tag,"返回的数据为："+s);
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        String url=jsonArray.getString(i);
                        final ImageView imageView=new ImageView(context);
                        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity= Gravity.CENTER;
                        imageView.setLayoutParams(layoutParams);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setImageResource(R.mipmap.yu_jia_zai);
                        final MyImageRequest myImageRequest=new MyImageRequest(url, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                imageView.setImageBitmap(response);
                            }
                        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                imageView.setImageResource(R.mipmap.yu_jia_zai);
                            }
                        },imageView);
                        requestQueue.add(myImageRequest);
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
