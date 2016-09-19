package com.example.asus_cp.dongmanbuy.activity.personal_center.data_set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.User;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 性别选择的界面
 * Created by asus-cp on 2016-06-22.
 */
public class SexSelectActivity extends BaseActivity implements View.OnClickListener{

    private String tag="SexSelectActivity";

    private LinearLayout nanLinearLayout;//男
    private LinearLayout nvLinearLayout;//女
    private ImageView nanImageView;
    private ImageView nvImageView;
    private TextView nanTextView;
    private TextView nvTextView;
    private Button confirmButton;

    private String editInfoUrl="http://www.zmobuy.com/PHP/?url=/user/editinfo";

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.sex_select_activity_layout);
        setTitle(R.string.change_sex);
        initView();
        init();
    }

    @Override
    public void initView() {
        nanLinearLayout= (LinearLayout) findViewById(R.id.ll_nan_sex_select);
        nvLinearLayout= (LinearLayout) findViewById(R.id.ll_nv_sex_select);
        nanImageView= (ImageView) findViewById(R.id.img_nan_sex_select);
        nvImageView= (ImageView) findViewById(R.id.img_nv_sex_select);
        nanTextView= (TextView) findViewById(R.id.text_nan_sex_select);
        nvTextView= (TextView) findViewById(R.id.text_nv_sex_select);
        confirmButton= (Button) findViewById(R.id.btn_confirm_sex_select);

        //设置点击事件
        nanLinearLayout.setOnClickListener(this);
        nvLinearLayout.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
    }

    /**
     * 初始化的方法
     */
    private void init() {
        user=getIntent().getParcelableExtra(MyConstant.USER_KEY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_nan_sex_select:
                reset();
                nanTextView.setTextColor(getResources().getColor(R.color.man_backgroud));
                nanImageView.setImageResource(R.drawable.man_selected);
                break;
            case R.id.ll_nv_sex_select:
                reset();
                nvTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                nvImageView.setImageResource(R.drawable.woman_selected);
                break;
            case R.id.btn_confirm_sex_select:
                conirmClickChuLi();
                break;
        }
    }


    /**
     * 确认的点击事件处理
     */
    private void conirmClickChuLi() {
        final String sex;
        if(nanTextView.getCurrentTextColor()==getResources().getColor(R.color.man_backgroud)){
            sex="1";
        }else {
            sex="2";
        }
        StringRequest changSexRequest=new StringRequest(Request.Method.POST, editInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "返回的数据是:" + s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            String str=jsonObject1.getString("succeed");
                            if("1".equals(str)){
                                Toast.makeText(SexSelectActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent();
                                if("1".equals(sex)){
                                    intent.putExtra(MyConstant.SEX_KEY,"男");
                                }else if("2".equals(sex)){
                                    intent.putExtra(MyConstant.SEX_KEY,"女");
                                }
                                setResult(RESULT_OK, intent);
                            }else{
                                Toast.makeText(SexSelectActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SexSelectActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"sex\":\""+sex+"\",\"email\":\""+user.getEmail()+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(changSexRequest);
    }

    /**
     * 重置颜色
     */
    public void reset(){
        nanTextView.setTextColor(getResources().getColor(R.color.myblack));
        nvTextView.setTextColor(getResources().getColor(R.color.myblack));
        nanImageView.setImageResource(R.drawable.man_normal);
        nvImageView.setImageResource(R.drawable.woman_normal);
    }
}
