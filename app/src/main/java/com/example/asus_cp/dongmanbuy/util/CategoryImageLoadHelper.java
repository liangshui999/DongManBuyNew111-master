package com.example.asus_cp.dongmanbuy.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.adapter.CategoryGridViewAdapter;
import com.example.asus_cp.dongmanbuy.customview.MyGridView;
import com.example.asus_cp.dongmanbuy.model.CategoryModel;
import com.example.asus_cp.dongmanbuy.model.Good;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类别标签里面gridview数据加载的帮助
 * Created by asus-cp on 2016-05-26.
 */
public class CategoryImageLoadHelper {

    private static int densty;

    private RequestQueue requestQueue;
    private Context context;
    private static String tag="CategoryImageLoadHelper";

    private String getAllCategoryUrl="http://www.zmobuy.com/PHP/index.php?url=/home/categorylist";//获取所有类别的url

    private String searchUrl="http://www.zmobuy.com/PHP/index.php?url=/search";//申请具体类别数据时的url，post请求

    public CategoryImageLoadHelper() {
        requestQueue=MyApplication.getRequestQueue();
        context=MyApplication.getContext();
    }

    public CategoryImageLoadHelper(int densty1){
        densty=densty1;
    }

    /**
     * 给gridview异步加载数据
     * @param gridView 需要异步加载图片的gridview
     * @param cateGory 商品的类别
     * @param length 需要加载的数据长度
     */
    public void asynLoadCatgory(final MyGridView gridView, final String cateGory, final String length) {
        StringRequest allRequest = new StringRequest(Request.Method.GET, getAllCategoryUrl, new Response.Listener<String>() {
            private ArrayList<CategoryModel> categoryModels = new ArrayList<CategoryModel>();

            @Override
            public void onResponse(String s) {
                // MyLog.d(tag,s);
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
                        if (cateGory.equals(model.getCategoryName())) {
                            categoryId = model.getCategoryId();
                        }
                    }
                    //再发送一次post请求
                    final String finalCategoryId = categoryId;


                    StringRequest weiYiRequest=new StringRequest(Request.Method.POST, searchUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag, "post请求的数据：" + s);
                            List<Good> goods=new ArrayList<Good>();
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
                                    goods.add(good);
                                }
                                CategoryGridViewAdapter adapter=new CategoryGridViewAdapter(goods,context);
                                gridView.setAdapter(adapter);
                                setGridViewViewHeightBasedOnChildren(gridView);
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
     * 动态设置GridView的高度
     * @param gridView
     */
    public static void setGridViewViewHeightBasedOnChildren(GridView gridView) {
        MyLog.d(tag,"像素密度"+densty);
        if(gridView == null) return;
        Adapter gridAdapter = gridView.getAdapter();
        if (gridAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int count=gridAdapter.getCount();
        if(count%3==0){
            count=count/3;
        }else{
            count=count/3+1;
        }
        for (int i = 0; i < count; i++) {
            View listItem = gridAdapter.getView(3*i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        switch (densty){
            case 120:
                totalHeight= (int) (2.4*totalHeight);
                break;
            case 160:
                totalHeight= (int) (3.2*totalHeight);
                break;
            case 240:
                totalHeight= (int) (2.4*totalHeight);
                break;
            case 320:
                totalHeight= (int) (1.9*totalHeight);
                break;
            case 480:
                totalHeight= (int) (1.4*totalHeight);
                break;

        }
        //totalHeight= (int) (2.4*totalHeight);//这真是神来之笔啊,这里根据不同的屏幕做个适配就好了

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight ;
        gridView.setLayoutParams(params);
    }

}
