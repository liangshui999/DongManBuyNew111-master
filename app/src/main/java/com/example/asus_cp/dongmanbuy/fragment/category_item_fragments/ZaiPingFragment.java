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
import com.example.asus_cp.dongmanbuy.adapter.CategoryGridViewAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridView;
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
public class ZaiPingFragment extends Fragment implements View.OnClickListener{

    private String tag="ZaiPingFragment";

    private MyGridViewA mingXinPianGridView;//明信片
    private MyGridViewA puKePaiGridView;//扑克牌
    private MyGridViewA taiLiGridView;//台历

    private TextView mingXinPianTextView;
    private TextView daLiBaoTextView;
    private TextView puKePaiTextView;
    private TextView taiLiTextView;
    private TextView qianBaoTextView;
    private TextView biJiBenTextView;
    private TextView tongRenTextView;

    private Context context;

    private RequestQueue requestQueue;

    private String getAllCategoryUrl="http://www.zmobuy.com/PHP/index.php?url=/home/categorylist";//获取所有类别的url

    private String searchUrl="http://www.zmobuy.com/PHP/index.php?url=/search";//申请具体类别数据时的url，post请求

    private CategoryImageLoadHelper helper;

    private ProgressDialog progressDialog;

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
        context= getActivity();//注意这个context不能使用Myapplication.getconxt(),否则会报异常;
        requestQueue=MyApplication.getRequestQueue();
        helper=new CategoryImageLoadHelper();
        mingXinPianGridView= (MyGridViewA) v.findViewById(R.id.grid_view_ming_xin_pian);
        puKePaiGridView= (MyGridViewA) v.findViewById(R.id.grid_view_pu_ke_pai);
        taiLiGridView= (MyGridViewA) v.findViewById(R.id.grid_view_tai_li);


        //显示进度框
        DialogHelper.showDialog(context);

        asynLoadCatgory(mingXinPianGridView, "明信片", "18", R.mipmap.yu_jia_zai);//用自己内部的方法
        helper.asynLoadCatgory(puKePaiGridView,"扑克牌","18",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(taiLiGridView,"台历","18",R.mipmap.yu_jia_zai);

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




    /**
     * 给gridview异步加载数据
     * @param gridView 需要异步加载图片的gridview
     * @param cateGory 商品的类别
     * @param length 需要加载的数据长度
     * @param yuJiaZaiPic 预加载图片
     */
    public void asynLoadCatgory(final MyGridViewA gridView, final String cateGory, final String length, final int yuJiaZaiPic) {
        StringRequest allRequest = new StringRequest(Request.Method.GET, getAllCategoryUrl, new Response.Listener<String>() {
            private ArrayList<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
            @Override
            public void onResponse(String s) {
                MyLog.d(tag, "类别的数据是：" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        CategoryModel categoryModel = new CategoryModel(jObject.getString("cat_id"),
                                JsonHelper.decodeUnicode(jObject.getString("cat_name")));
                        categoryModels.add(categoryModel);
                    }

                    String categoryId = null;
                    for (CategoryModel model : categoryModels) {
                        if (cateGory.equals(model.getCategoryName())) { //如果有了的话，就直接退出，不用再循环了
                            categoryId = model.getCategoryId();
                            MyLog.d(tag,"小类别"+model.getCategoryName());
                            break;
                        }
                    }
                    //再发送一次post请求
                    final String finalCategoryId = categoryId;


                    StringRequest weiYiRequest=new StringRequest(Request.Method.POST, searchUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            DialogHelper.dissmisDialog();
                            MyLog.d(tag, "post请求的数据：" + s);
                            final List<Good> goods=new ArrayList<Good>();
                            try {
                                JSONObject postJsonObject=new JSONObject(s);
                                JSONArray postJsArray=postJsonObject.getJSONArray("data");
                                for(int i=0;i<postJsArray.length();i++){
                                    JSONObject jsObject=postJsArray.getJSONObject(i);
                                    Good good=new Good();
                                    good.setGoodId(jsObject.getString("goods_id"));
                                    good.setGoodName(JsonHelper.decodeUnicode(jsObject.getString("name")));
                                    good.setGoodsImg(jsObject.getJSONObject("img").getString("url"));
                                    good.setGoodsSmallImag(jsObject.getJSONObject("img").getString("small"));
                                    good.setGoodsThumb(jsObject.getJSONObject("img").getString("thumb"));
                                    good.setSalesVolume(jsObject.getString("sales_volume"));
                                    good.setMarket_price(jsObject.getString("market_price"));
                                    good.setShopPrice(jsObject.getString("shop_price"));
                                    goods.add(good);
                                }
                                CategoryGridViewAdapter adapter=new CategoryGridViewAdapter(goods,context,yuJiaZaiPic);
                                gridView.setAdapter(adapter);
                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent=new Intent(context, ProductDetailActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra(MyConstant.GOOD_KEY,goods.get(position));
                                        context.startActivity(intent);
                                    }
                                });
                                //setGridViewViewHeightBasedOnChildren(gridView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            MyLog.d(tag, "失败的原因：" + volleyError.getMessage());

                        }
                    }){
                        //注意这里post传参数的方法，键是json，值是json数据体
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("json","{\"filter\":{\"keywords\":\"\",\"category_id\":\""+finalCategoryId+"\",\"price_range\":\"\",\"brand_id\":\"\",\"intro\":\"\",\"sort_by\":\"\"},\"pagination\":{\"page\":\""+1+"\",\"count\":\""+length+"\"}}");
                            return params;
                        }
                    };
                    requestQueue.add(weiYiRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(allRequest);
    }


}
