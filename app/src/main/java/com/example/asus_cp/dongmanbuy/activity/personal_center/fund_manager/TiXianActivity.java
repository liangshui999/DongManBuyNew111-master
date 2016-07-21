package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.TiXianTanChuListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.CardModel;
import com.example.asus_cp.dongmanbuy.model.User;
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
 * 提现的界面
 * Created by asus-cp on 2016-06-27.
 */
public class TiXianActivity extends Activity implements View.OnClickListener{

    private String tag="TiXianActivity";

    private EditText tiXianJinEEditText;//提现金额
    private EditText beiZhuEditText;//备注
    private TextView tiXianFangShiBankNameTextView;//银行名
    private TextView tiXianFangShiKaHaoTextView;//卡号
    private RelativeLayout tiXianFangShiRelativeLayout;//提现方式
    private Button tiJiaoShenQingButton;//提交申请

    private User user;//接收对方传递过来的user

    private PopupWindow tiXianFangShiWindow;//提现的弹出窗口
    private View parentView;

    private String cardListUrl="http://www.zmobuy.com/PHP/?url=/user/card_list";//银行卡列表的接口
    private String tiXianUrl="http://www.zmobuy.com/PHP/?url=/user/account_account";//提现的接口

    private String uid;
    private String sid;

    private RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ti_xian_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {

        requestQueue= MyApplication.getRequestQueue();
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);

        user=getIntent().getParcelableExtra(MyConstant.USER_KEY);
        parentView=LayoutInflater.from(this).inflate(R.layout.ti_xian_activity_layout, null);

        tiXianJinEEditText= (EditText) findViewById(R.id.edit_ti_xian_jin_e_ti_xian);
        beiZhuEditText= (EditText) findViewById(R.id.edit_bei_zhu_ti_xian);
        tiXianFangShiBankNameTextView = (TextView) findViewById(R.id.text_ti_xian_fang_shi_bank_name_ti_xian);
        tiXianFangShiKaHaoTextView= (TextView) findViewById(R.id.text_ti_xian_fang_shi_ka_hao_ti_xian);
        tiXianFangShiRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_ti_xian_fang_shi_ti_xian);
        tiJiaoShenQingButton= (Button) findViewById(R.id.btn_ti_jiao_shen_qing_ti_xian);

        //设置初始值
        tiXianJinEEditText.setHint("本次最大提现金额¥" + user.getMoney());

        //设置点击事件
        tiXianFangShiRelativeLayout.setOnClickListener(this);
        tiJiaoShenQingButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_layout_ti_xian_fang_shi_ti_xian://点击了提现方式
                tiXianFangShiClickChuLi();
                break;
            case R.id.btn_ti_jiao_shen_qing_ti_xian://点击了提交申请
                tiJiaoShenQingClickChuLi();
                break;
            case R.id.img_close_ti_xian_tan_chu://关闭弹出窗口
                tiXianFangShiWindow.dismiss();
                break;
        }
    }


    /**
     * 提交申请的点击事件处理
     */
    private void tiJiaoShenQingClickChuLi() {
        final String tiXianJinE=tiXianJinEEditText.getText().toString();
        double tiXianDouble=0.00;
        if(!tiXianJinE.isEmpty()){
            tiXianDouble=Double.parseDouble(tiXianJinE);
        }
        final String beiZhu=beiZhuEditText.getText().toString();
        final String kaHao=tiXianFangShiKaHaoTextView.getText().toString();

        if("".equals(tiXianJinE) || tiXianJinE.isEmpty()){
            Toast.makeText(this,"请输入提现金额",Toast.LENGTH_SHORT).show();
        }else if(tiXianDouble>Double.parseDouble(user.getMoney())){
            Toast.makeText(this,"提现金额大于余额",Toast.LENGTH_SHORT).show();
        }else if("".equals(kaHao) || kaHao.isEmpty()){
            Toast.makeText(this,"请输入卡号",Toast.LENGTH_SHORT).show();
        }else{
            StringRequest tiXianRequest=new StringRequest(Request.Method.POST, tiXianUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag,"提现返回的数据:"+s);
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                JSONObject jsonObject1=jsonObject.getJSONObject("status");
                                String succed=jsonObject1.getString("succeed");
                                if("1".equals(succed)){
                                    Toast.makeText(TiXianActivity.this,"提现成功",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(TiXianActivity.this,ShenQingJiLuActivity.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(TiXianActivity.this,"提现失败",Toast.LENGTH_SHORT).show();
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
                    String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"amount\":\""+tiXianJinE+"\",\"bank_number\":\""+kaHao+"\",\"real_name\":\""+user.getName()+"\",\"surplus_type\":\""+"1"+"\",\"user_note\":\""+beiZhu+"\"}";
                    map.put("json",json);
                    return map;
                }
            };
            requestQueue.add(tiXianRequest);
        }
    }


    /**
     * 提现方式的点击事件处理
     */
    private void tiXianFangShiClickChuLi() {
        View tiXianFangShiView= LayoutInflater.from(this).inflate(R.layout.ti_xian_fang_shi_tan_chu_layout, null);
        ImageView closeTiXianFangShiImageView= (ImageView) tiXianFangShiView.findViewById(R.id.img_close_ti_xian_tan_chu);
        final ListView cardList= (ListView) tiXianFangShiView.findViewById(R.id.list_view_ti_xian_tan_chu);

        StringRequest getCardListRequest=new StringRequest(Request.Method.POST, cardListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        final List<CardModel> models=parseJson(s);
                        TiXianTanChuListAdapter adapter=new TiXianTanChuListAdapter(TiXianActivity.this,models);
                        cardList.setAdapter(adapter);

                        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                tiXianFangShiBankNameTextView.setText(models.get(position).getBankName());
                                tiXianFangShiKaHaoTextView.setText(models.get(position).getKaHao());
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"}}";
                map.put("json",json);
                return map;
            }
        };

        requestQueue.add(getCardListRequest);

        closeTiXianFangShiImageView.setOnClickListener(this);

        tiXianFangShiWindow=new PopupWindow(tiXianFangShiView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        tiXianFangShiWindow.setBackgroundDrawable(new ColorDrawable());
        tiXianFangShiWindow.setOutsideTouchable(true);
        tiXianFangShiWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);//最后才是show,顺序很重要
        setBackgroundAlpha(0.5f);
        tiXianFangShiWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
    }


    /**
     * 解析json数据
     * @param s
     */
    private List<CardModel> parseJson(String s) {
        MyLog.d(tag, "返回的数据是：" + s);
        List<CardModel> models=new ArrayList<CardModel>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject ziJsonObj=jsonArray.getJSONObject(i);
                CardModel cardModel=new CardModel();
                cardModel.setId(ziJsonObj.getString("id"));
                cardModel.setBankName(JsonHelper.decodeUnicode(ziJsonObj.getString("bank_name")));
                cardModel.setKaHao(ziJsonObj.getString("bank_card"));
                cardModel.setBankRegion(JsonHelper.decodeUnicode(ziJsonObj.getString("bank_region")));
                cardModel.setUserName(JsonHelper.decodeUnicode(ziJsonObj.getString("bank_user_name")));
                cardModel.setUserId(ziJsonObj.getString("user_id"));
                models.add(cardModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return models;
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

}
