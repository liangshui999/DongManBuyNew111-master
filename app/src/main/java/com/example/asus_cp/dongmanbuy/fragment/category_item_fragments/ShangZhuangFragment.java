package com.example.asus_cp.dongmanbuy.fragment.category_item_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上装的具体内容
 * Created by asus-cp on 2016-05-25.
 */
public class ShangZhuangFragment extends Fragment implements View.OnClickListener{
    private MyGridViewA weiYiGridView;//卫衣的
    private MyGridViewA zhenZhiShanGridView;//针织衫
    private MyGridViewA waiTaoGridView;//外套
    private MyGridViewA tXueGridView;//T恤

    private TextView weiYiTextView;
    private TextView zhenZhiShanTextView;
    private TextView waiTaoTextView;
    private TextView tXueTextView;

    private View v;//fragment的布局文件

    private Context context;

    private RequestQueue requestQueue;

    private String tag="ShangZhuangFragment";

    private String getAllCategoryUrl="http://www.zmobuy.com/PHP/index.php?url=/home/categorylist";//获取所有类别的url

    private String searchUrl="http://www.zmobuy.com/PHP/index.php?url=/search";//申请具体类别数据时的url，post请求

    private CategoryImageLoadHelper helper;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.shang_zhuang_fragment_layout,null);
        init(v);
        return v;
    }

    /**
     * 初始化方法
     */
    private void init(View v) {
        context= getActivity();//注意这个context不能使用Myapplication.getconxt(),否则会报异常
        helper=new CategoryImageLoadHelper();
        requestQueue=MyApplication.getRequestQueue();
        //控件的初始化
        weiYiGridView= (MyGridViewA) v.findViewById(R.id.grid_view_wei_yi);
        zhenZhiShanGridView= (MyGridViewA) v.findViewById(R.id.grid_view_zhen_zhi_shan);
        waiTaoGridView= (MyGridViewA) v.findViewById(R.id.grid_view_wai_tao);
        tXueGridView= (MyGridViewA) v.findViewById(R.id.grid_view_t_xue);


        //显示进度框
        DialogHelper.showDialog(context);

        //卫衣部分
        asynLoadCatgory(weiYiGridView, "卫衣", "9", R.mipmap.yu_jia_zai);
        //针织衫部分
        helper.asynLoadCatgory(zhenZhiShanGridView,"针织衫","6",R.mipmap.yu_jia_zai);
        //外套部分
        helper.asynLoadCatgory(waiTaoGridView,"外套","3",R.mipmap.yu_jia_zai);
        //T恤部分
        helper.asynLoadCatgory(tXueGridView,"T恤","3",R.mipmap.yu_jia_zai);



        weiYiTextView= (TextView) v.findViewById(R.id.text_wei_yi);
        zhenZhiShanTextView= (TextView) v.findViewById(R.id.text_zhen_zhi_shan);
        waiTaoTextView= (TextView) v.findViewById(R.id.text_wai_tao);
        tXueTextView= (TextView) v.findViewById(R.id.text_t_xue);

        weiYiTextView.setOnClickListener(this);
        zhenZhiShanTextView.setOnClickListener(this);
        waiTaoTextView.setOnClickListener(this);
        tXueTextView.setOnClickListener(this);
    }



    /**
     *点击事件处理
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_wei_yi:
                Toast.makeText(context,"点击了卫衣",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_zhen_zhi_shan:
                Toast.makeText(context,"点击了针织衫",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_wai_tao:
                Toast.makeText(context,"点击了外套",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_t_xue:
                Toast.makeText(context,"点击了T恤",Toast.LENGTH_SHORT).show();
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








    /**
     * 此方法作废
     * @param requestUrl
     * @param parameters
     * @return
     */
    public static String post(String requestUrl,String parameters){
        StringBuffer buffer = new StringBuffer();
        URL url = null;

        HttpURLConnection conn = null;
        try {
            url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");// post
            conn.setRequestProperty("Content-Type", "application/json; charset=" + "utf-8");//格式一定要设置好

            conn.setUseCaches(false);
            //conn.connect();
            // DataOutputStream out = new
            // DataOutputStream(conn.getOutputStream());
            PrintWriter outprint = new PrintWriter(new OutputStreamWriter(
                    conn.getOutputStream(), "UTF-8"));
            outprint.write(parameters);
            outprint.flush();
            outprint.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (conn != null) {
            conn.disconnect();
        }

        return buffer.toString();

    }



    }

