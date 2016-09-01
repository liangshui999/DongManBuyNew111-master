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
import com.example.asus_cp.dongmanbuy.util.MyNetHelper;

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
public class ShangZhuangFragment extends Fragment{

    private MyGridViewA shangZhuangGridView;//上装
    private TextView shangZhuangTextView;


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
        shangZhuangGridView = (MyGridViewA) v.findViewById(R.id.grid_view_shang_zhuang);
        shangZhuangTextView = (TextView) v.findViewById(R.id.text_wei_yi);

        final List<CategoryModel> models=new ArrayList<CategoryModel>();
        CategoryModel model=new CategoryModel("1498","T恤",R.mipmap.t_xu);
        CategoryModel mode2=new CategoryModel("1699","衬衫",R.mipmap.chen_shan);
        CategoryModel mode3=new CategoryModel("1496","外套",R.mipmap.wai_tao);
        CategoryModel mode4=new CategoryModel("1477","卫衣",R.mipmap.wei_yi);
        CategoryModel mode5=new CategoryModel("1488","针织衫",R.mipmap.zhen_zhi_shan);
        models.add(model);
        models.add(mode2);
        models.add(mode3);
        models.add(mode4);
        models.add(mode5);
        CategoryAdapter adapter=new CategoryAdapter(context,models);
        shangZhuangGridView.setAdapter(adapter);
        shangZhuangGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, GoodSearchResultActivity.class);
                intent.putExtra(MyConstant.CATEGORY_ID_KEY,models.get(position).getCategoryId());
                startActivity(intent);
            }
        });

    }


    /**
     * 获取小分类的ctegoryId
     */
    private void getXiaoFenLeiCatId() {
        //以下内容是测试的内容
        helper.asynLoadCatgory(shangZhuangGridView,"T恤","3", R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"衬衫","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"外套","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"卫衣","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"针织纱","3",R.mipmap.yu_jia_zai);

        helper.asynLoadCatgory(shangZhuangGridView,"牛仔裤","3",R.mipmap.yu_jia_zai);

        helper.asynLoadCatgory(shangZhuangGridView,"旅行箱","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"钱包","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"双肩包","3",R.mipmap.yu_jia_zai);

        helper.asynLoadCatgory(shangZhuangGridView,"明信片","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"扑克牌","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"台历","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"音箱","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"雨伞","3",R.mipmap.yu_jia_zai);

        helper.asynLoadCatgory(shangZhuangGridView,"抱枕","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"公仔","3",R.mipmap.yu_jia_zai);

        helper.asynLoadCatgory(shangZhuangGridView,"挂件","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"帽子","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"手套","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"头饰发饰","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"袜子","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"围巾","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"鞋子","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"腰带","3",R.mipmap.yu_jia_zai);

        helper.asynLoadCatgory(shangZhuangGridView,"漫画","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"小说","3",R.mipmap.yu_jia_zai);

        helper.asynLoadCatgory(shangZhuangGridView,"模型","3",R.mipmap.yu_jia_zai);
        helper.asynLoadCatgory(shangZhuangGridView,"手办","3",R.mipmap.yu_jia_zai);
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
                            MyLog.d(tag,"小类别:"+model.getCategoryName()+"....."+"categoryId:"+categoryId);
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

