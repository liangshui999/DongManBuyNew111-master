package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.DingDanListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyListView;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.UserModel;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单的界面
 * Created by asus-cp on 2016-06-15.
 */
public class DingDanActivity extends Activity implements View.OnClickListener{

    private String tag="DingDanActivity";
    private RelativeLayout shouHuoAddressRelativeLayout;//收货地址
    private TextView peopleNameTextView;//人名
    private TextView phoneTextView;//电话
    private TextView addressTextView;//收货地址
    private LinearLayout productDisplayLinearLayoutOrignal;//商品的展示区
    private ImageView firstPicImageView;//第一个商品的图片
    private ImageView secondPicImageView;//第二个商品的图片
    private ImageView threePicImageView;//第三个商品的图片
    private LinearLayout displayAllProductLineatLayout;//展示所有商品
    private TextView gongJiJianTextView;//共几件商品
    private LinearLayout productListLinearLayoutZhanKai;//包含商品列表的容器
    private MyListView listView;//展示商品列表
    private ImageView downImageView;//向下的箭头
    private RelativeLayout peiSongFangShiRelaytiveLayout;//配送方式
    private TextView peiSongFangShiTextView;//配送方式
    private RelativeLayout ziTiShiJianRelativeLayout;//自提时间
    private TextView ziTiRiQiTextView;//自提日期
    private TextView ziTiXingQiTextView;//自提星期
    private RelativeLayout ziTiAreaRelativeLayout;//自提地点
    private TextView ziTiAreaTextView;//自提地点
    private EditText maiJiaLiuYanEditeText;//买家留言
    private TextView productSumTextView;//商品总数
    private TextView productSumPriceTextView;//商品总价格
    private RelativeLayout zhiFuFangShiRelativeLayout;//支付方式
    private TextView zhiFuFangShiTextView;//支付方式
    private TextView shouXuFeiTextView;//手续费
    private RelativeLayout faPiaoXinXiRelativeLayout;//发票信息
    private RelativeLayout youHuiQuanRelativeLayout;//优惠券
    private TextView youHuiQuanTextView;//优惠券
    private RelativeLayout jiFenRelaytiveLayout;//积分
    private TextView jiFenTextView;//积分
    private ImageView jiFenImageView;//积分
    private TextView productSumPriceTextBottomView;//商品总价
    private TextView shiFuKuanTextView;//实付款
    private Button tiJiaoDingDanButton;//提交订单

    public static final int SHOU_HUO_REN_XIN_XI_REQUEST_KEY=0;//startactivityforresult的key，跳转到收货人信息

    public static final String KONG_GE=" ";

    private String shouHuoAddressListUrl="http://www.zmobuy.com/PHP/index.php?url=/address/list";//收货地址列表的接口

    private RequestQueue requestQueue;

    private List<Good> goods;//从购物车传递过来的商品列表

    private List<Integer> itemProductCount;//从购物车传递过来的每个小项的商品数目

    private ImageLoadHelper helper;

    private String uid;
    private String sid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ding_dan_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        requestQueue= MyApplication.getRequestQueue();
        helper=new ImageLoadHelper();
        initView();

        //获取uid和sid
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        getShouHuoAddressList();

        //获取购物车页面传递过来的商品列表
        goods= (List<Good>) getIntent().getSerializableExtra(MyConstant.GOOD_LIST_KEY);//注意这儿接收的时候不要接收错了,不是parceable
        if(goods.size()==1){
            ImageLoader imageLoader1=helper.getImageLoader();
            ImageLoader.ImageListener listener1=imageLoader1.getImageListener(firstPicImageView,R.mipmap.yu_jia_zai,
                    R.mipmap.yu_jia_zai);
            imageLoader1.get(goods.get(0).getGoodsImg(),listener1,200,200);
        }else if(goods.size()==2){
            ImageLoader imageLoader1=helper.getImageLoader();
            ImageLoader.ImageListener listener1=imageLoader1.getImageListener(firstPicImageView,R.mipmap.yu_jia_zai,
                    R.mipmap.yu_jia_zai);
            imageLoader1.get(goods.get(0).getGoodsImg(),listener1,200,200);

            ImageLoader imageLoader2=helper.getImageLoader();
            ImageLoader.ImageListener listener2=imageLoader2.getImageListener(secondPicImageView,R.mipmap.yu_jia_zai,
                    R.mipmap.yu_jia_zai);
            imageLoader2.get(goods.get(1).getGoodsImg(), listener2, 200, 200);

        }else if(goods.size()>=3){
            ImageLoader imageLoader1=helper.getImageLoader();
            ImageLoader.ImageListener listener1=imageLoader1.getImageListener(firstPicImageView,R.mipmap.yu_jia_zai,
                    R.mipmap.yu_jia_zai);
            imageLoader1.get(goods.get(0).getGoodsImg(),listener1,200,200);

            ImageLoader imageLoader2=helper.getImageLoader();
            ImageLoader.ImageListener listener2=imageLoader2.getImageListener(secondPicImageView, R.mipmap.yu_jia_zai,
                    R.mipmap.yu_jia_zai);
            imageLoader2.get(goods.get(1).getGoodsImg(),listener2,200,200);

            ImageLoader imageLoader3=helper.getImageLoader();
            ImageLoader.ImageListener listener3=imageLoader3.getImageListener(threePicImageView,R.mipmap.yu_jia_zai,
                    R.mipmap.yu_jia_zai);
            imageLoader3.get(goods.get(2).getGoodsImg(),listener3,200,200);
        }
        gongJiJianTextView.setText("共"+goods.size()+"件");


        //从购物车列表获取记录每个小项商品数目的集合
        itemProductCount= (List<Integer>) getIntent().getSerializableExtra(MyConstant.ITEM_PRODUCT_COUNT_KEY);


    }


    /**
     * 获取商品地址列表
     */
    private void getShouHuoAddressList() {
        //获取收货地址列表
        StringRequest shouHuoAddressListRequset=new StringRequest(Request.Method.POST, shouHuoAddressListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        UserModel userModel=parseJson(s);
                        peopleNameTextView.setText(userModel.getUserName());
                        phoneTextView.setText(userModel.getUserPhone());
                        addressTextView.setText(userModel.getCountryName()+userModel.getProvinceName()
                                +userModel.getCityName()+userModel.getDistrictName()+" "+userModel.getShouHuoArea());
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
        requestQueue.add(shouHuoAddressListRequset);
    }



    /**
     * 解析json数据
     * @param s
     */
    private UserModel parseJson(String s) {
        UserModel userModel=new UserModel();
        MyLog.d(tag, "返回的数据是：" + s);
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=null;
            try{
                jsonArray=jsonObject.getJSONArray("data");
            }catch (Exception e){
                e.printStackTrace();
            }

            if(jsonArray!=null && jsonArray.length()>0){
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject ziJsObj=jsonArray.getJSONObject(i);
                    String moRenString=ziJsObj.getString("default_address");
                    if("1".equals(moRenString)){
                        userModel.setId(ziJsObj.getString("id"));
                        userModel.setUserName(JsonHelper.decodeUnicode(ziJsObj.getString("consignee")));
                        userModel.setShouHuoArea(JsonHelper.decodeUnicode(ziJsObj.getString("address")));
                        userModel.setUserPhone(ziJsObj.getString("mobile"));
                        userModel.setCountryName(JsonHelper.decodeUnicode(ziJsObj.getString("country_name")));
                        userModel.setProvinceName(JsonHelper.decodeUnicode(ziJsObj.getString("province_name")));
                        userModel.setCityName(JsonHelper.decodeUnicode(ziJsObj.getString("city_name")));
                        userModel.setDistrictName(JsonHelper.decodeUnicode(ziJsObj.getString("district_name")));
                        userModel.setDefaultAddress(ziJsObj.getString("default_address"));
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userModel;
    }

    /**
     * 初始化view
     */
    private void initView() {
        shouHuoAddressRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_shou_huo_address_ding_dan);
        peopleNameTextView= (TextView) findViewById(R.id.text_shou_huo_ren_name_ding_dan);
        phoneTextView= (TextView) findViewById(R.id.text_shou_huo_ren_phone_ding_dan);
        addressTextView= (TextView) findViewById(R.id.text_shou_huo_address_ding_dan);
        productDisplayLinearLayoutOrignal = (LinearLayout) findViewById(R.id.ll_product_display_area);
        firstPicImageView= (ImageView) findViewById(R.id.img_one_ding_dan);
        secondPicImageView= (ImageView) findViewById(R.id.img_two_ding_dan);
        threePicImageView= (ImageView) findViewById(R.id.img_three_ding_dan);
        displayAllProductLineatLayout= (LinearLayout) findViewById(R.id.ll_dispaly_all_product_ding_dan);
        gongJiJianTextView= (TextView) findViewById(R.id.text_product_sum_ding_dan);
        productListLinearLayoutZhanKai = (LinearLayout) findViewById(R.id.ll_list_view_ding_dan);
        downImageView= (ImageView) findViewById(R.id.img_down_ding_dan);
        listView= (MyListView) findViewById(R.id.my_list_view_ding_dan);
        peiSongFangShiRelaytiveLayout= (RelativeLayout) findViewById(R.id.re_layout_pei_song_fang_shi_ding_dan);
        peiSongFangShiTextView= (TextView) findViewById(R.id.text_pei_song_fang_shi_ding_dan);
        ziTiShiJianRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_zi_ti_shi_jian_ding_dan);
        ziTiRiQiTextView= (TextView) findViewById(R.id.text_zi_ti_time_ri_qi_ding_dan);
        ziTiXingQiTextView= (TextView) findViewById(R.id.text_zi_ti_time_xing_qi_ding_dan);
        ziTiAreaRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_zi_ti_area_ding_dan);
        ziTiAreaTextView= (TextView) findViewById(R.id.text_zi_ti_area_ding_dan);
        maiJiaLiuYanEditeText= (EditText) findViewById(R.id.edit_mai_jia_liu_yan_ding_dan);
        productSumTextView= (TextView) findViewById(R.id.text_product_sum_he_ji_ding_dan);
        productSumPriceTextView= (TextView) findViewById(R.id.text_he_ji_price_ding_dan);
        zhiFuFangShiRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_zhi_fu_fang_shi_ding_dan);
        zhiFuFangShiTextView= (TextView) findViewById(R.id.text_zhi_fu_fang_shi_ding_dan);
        shouXuFeiTextView= (TextView) findViewById(R.id.text_shou_xu_fei_ding_dan);
        faPiaoXinXiRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_fa_piao_xin_xi_ding_dan);
        youHuiQuanRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_you_hui_quan_ding_dan);
        youHuiQuanTextView= (TextView) findViewById(R.id.text_you_hui_quan_ding_dan);
        jiFenRelaytiveLayout= (RelativeLayout) findViewById(R.id.re_layout_ji_fen_ding_dan);
        jiFenTextView= (TextView) findViewById(R.id.text_ji_fen_ding_dan);
        jiFenImageView= (ImageView) findViewById(R.id.img_ji_fen_ding_dan);
        productSumPriceTextBottomView= (TextView) findViewById(R.id.text_product_sum_price_ding_dan);
        shiFuKuanTextView= (TextView) findViewById(R.id.text_shi_fu_kuan_ding_dan);
        tiJiaoDingDanButton= (Button) findViewById(R.id.btn_ti_jiao_ding_dan);


        maiJiaLiuYanEditeText.clearFocus();//让买家留言取消聚焦
        //给view设置点击事件
        shouHuoAddressRelativeLayout.setOnClickListener(this);
        firstPicImageView.setOnClickListener(this);
        secondPicImageView.setOnClickListener(this);
        threePicImageView.setOnClickListener(this);
        displayAllProductLineatLayout.setOnClickListener(this);
        downImageView.setOnClickListener(this);
        peiSongFangShiRelaytiveLayout.setOnClickListener(this);
        ziTiShiJianRelativeLayout.setOnClickListener(this);
        ziTiAreaRelativeLayout.setOnClickListener(this);
        zhiFuFangShiRelativeLayout.setOnClickListener(this);
        faPiaoXinXiRelativeLayout.setOnClickListener(this);
        youHuiQuanRelativeLayout.setOnClickListener(this);
        jiFenRelaytiveLayout.setOnClickListener(this);
        tiJiaoDingDanButton.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_layout_shou_huo_address_ding_dan://收获地址
                //Toast.makeText(this,"点击了收货地址",Toast.LENGTH_SHORT).show();
                Intent addresListIntent=new Intent(this,ShouHuoRenXinXiListActivity.class);
                startActivityForResult(addresListIntent, SHOU_HUO_REN_XIN_XI_REQUEST_KEY);
                break;
            case R.id.img_one_ding_dan://点击了第一个商品图片
                Toast.makeText(this,"点击了第一个商品图片",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_two_ding_dan://点击了第二个商品图片
                Toast.makeText(this,"点击了第二个商品图片",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_three_ding_dan://点击了第三个商品图片
                Toast.makeText(this,"点击了第三个商品图片",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_dispaly_all_product_ding_dan://点击了展示所有图片
                Toast.makeText(this,"点击了展示所有图片",Toast.LENGTH_SHORT).show();
                productDisplayLinearLayoutOrignal.setVisibility(View.GONE);
                productListLinearLayoutZhanKai.setVisibility(View.VISIBLE);
                DingDanListAdapter adapter=new DingDanListAdapter(this,goods,itemProductCount);
                listView.setAdapter(adapter);

                break;
            case R.id.img_down_ding_dan://点击了下拉,收起
                Toast.makeText(this,"点击了下拉",Toast.LENGTH_SHORT).show();
                productDisplayLinearLayoutOrignal.setVisibility(View.VISIBLE);
                productListLinearLayoutZhanKai.setVisibility(View.GONE);
                break;
            case R.id.re_layout_pei_song_fang_shi_ding_dan://点击了配送方式
                Toast.makeText(this,"点击了配送方式",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_zi_ti_shi_jian_ding_dan://点击了自提时间
                Toast.makeText(this,"点击了自提时间",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_zi_ti_area_ding_dan://点击了自提地点
                Toast.makeText(this,"点击了自提地点",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_zhi_fu_fang_shi_ding_dan://点击了支付方式
                Toast.makeText(this,"点击了支付方式",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_fa_piao_xin_xi_ding_dan://点击了发票信息
                Toast.makeText(this,"点击了发票信息",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_you_hui_quan_ding_dan://点击了优惠券
                Toast.makeText(this,"点击了优惠券",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_ji_fen_ding_dan://点击了积分
                Toast.makeText(this,"点击了积分",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_ti_jiao_ding_dan://点击了提交订单
                Toast.makeText(this,"点击了提交订单",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SHOU_HUO_REN_XIN_XI_REQUEST_KEY://从收货人信息活动返回的数据
                if(resultCode==RESULT_OK){
                    UserModel userModel=data.getParcelableExtra(MyConstant.USER_MODLE_KEY);
                    peopleNameTextView.setText(userModel.getUserName());
                    phoneTextView.setText(userModel.getUserPhone());
                    addressTextView.setText(userModel.getProvinceName()+KONG_GE+userModel.getCityName()
                    +KONG_GE+userModel.getDistrictName()+KONG_GE+userModel.getShouHuoArea());
                }
                break;
        }
    }
}
