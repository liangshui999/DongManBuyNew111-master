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

import com.android.volley.RequestQueue;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.customview.MyGridView;
import com.example.asus_cp.dongmanbuy.util.CategoryImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;

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

/**
 * 上装的具体内容
 * Created by asus-cp on 2016-05-25.
 */
public class ShangZhuangFragment extends Fragment implements View.OnClickListener{
    private MyGridView weiYiGridView;//卫衣的
    private MyGridView zhenZhiShanGridView;//针织衫
    private MyGridView waiTaoGridView;//外套
    private MyGridView tXueGridView;//T恤

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.shang_zhuang_fragment_layout,null);
        init();
        return v;
    }

    /**
     * 初始化方法
     */
    private void init() {
        context= MyApplication.getContext();
        helper=new CategoryImageLoadHelper();
        requestQueue=MyApplication.getRequestQueue();
        //控件的初始化
        weiYiGridView= (MyGridView) v.findViewById(R.id.grid_view_wei_yi);
        zhenZhiShanGridView= (MyGridView) v.findViewById(R.id.grid_view_zhen_zhi_shan);
        waiTaoGridView= (MyGridView) v.findViewById(R.id.grid_view_wai_tao);
        tXueGridView= (MyGridView) v.findViewById(R.id.grid_view_t_xue);


        //卫衣部分
        helper.asynLoadCatgory(weiYiGridView,"卫衣","9");
        //针织衫部分
        helper.asynLoadCatgory(zhenZhiShanGridView,"针织衫","6");
        //外套部分
        helper.asynLoadCatgory(waiTaoGridView,"外套","3");
        //T恤部分
        helper.asynLoadCatgory(tXueGridView,"T恤","3");



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

