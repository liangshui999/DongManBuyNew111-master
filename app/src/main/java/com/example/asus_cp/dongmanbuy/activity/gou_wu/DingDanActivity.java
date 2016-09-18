package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.adapter.DingDanJieMianListAdapterOut;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.model.UserModel;
import com.example.asus_cp.dongmanbuy.model.YouHuiQuanModel;
import com.example.asus_cp.dongmanbuy.model.my_json_model.ShopModelJson;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订单的界面
 * Created by asus-cp on 2016-06-15.
 */
public class DingDanActivity extends BaseActivity implements View.OnClickListener{

    private String tag="DingDanActivity";
    private RelativeLayout shouHuoAddressRelativeLayout;//收货地址
    private TextView peopleNameTextView;//人名
    private TextView phoneTextView;//电话
    private TextView addressTextView;//收货地址
    private ListView listviewOut;//展示各个店铺的listview
    private RelativeLayout zhiFuFangShiRelativeLayout;//支付方式
    private TextView zhiFuFangShiTextView;//支付方式
    private TextView shouXuFeiTextView;//手续费
    private RelativeLayout faPiaoXinXiRelativeLayout;//发票信息
    private TextView faPiaoTaiTouTextView;//发票抬头
    private TextView faPiaocontentTextView;//发票内容
    private RelativeLayout youHuiQuanRelativeLayout;//优惠券
    private TextView youHuiQuanTextView;//优惠券
    private TextView youHuiQuanShuMuTextView;//优惠券数目
    private RelativeLayout jiFenRelaytiveLayout;//积分
    private TextView jiFenTextView;//积分
    private ImageView jiFenImageView;//积分
    private TextView productSumPriceTextBottomView;//商品总价
    private TextView youHuiQuanDiKouTextView;//优惠券抵扣
    private TextView shiFuKuanTextView;//实付款
    private Button tiJiaoDingDanButton;//提交订单

    public static final int SHOU_HUO_REN_XIN_XI_REQUEST_KEY=0;//startactivityforresult的key，跳转到收货人信息
    public static final int FA_PIAO_REQUEST_KEY=1;//跳转到发票
    public static final int YOU_HUI_QUAN_REQUEST_KEY=2;//跳转到优惠券

    public static final String KONG_GE=" ";

    private String shouHuoAddressListUrl="http://www.zmobuy.com/PHP/index.php?url=/address/list";//收货地址列表的接口

    private String tiJiaoDingDanUrl="http://api.zmobuy.com/JK/base/model.php";//提交订单的接口

    private String yuEZhiFuUrl="http://api.zmobuy.com/JK/base/model.php";//余额支付的接口


    private ArrayList<List<Integer>> itemGoodCounts;//从购物车传递过来的数据
    private List<ShopModel> shopModels;//从购物车传递过来的数据

    private ImageLoadHelper helper;

    private View parentView;//所有弹出窗口的父view

    private LayoutInflater inflater;

    private String month;
    private String day;
    private String hour;
    private String xingQi;


    private PopupWindow peiSongFangShiWindow;//配送方式的弹出窗口
    private PopupWindow ziTiShiJianWindow;//自提时间的弹出窗口
    private PopupWindow ziTiAreaWindow;//自提地点的弹出窗口
    private PopupWindow zhiFuFangShiWindow;//支付方式的弹出窗口
    private PopupWindow faPiaoXinXiWindow;//发票信息的弹出窗口
    private PopupWindow youHuiQuanWindow;//优惠券的弹出窗口

    //配送方式弹出窗口的组件
    private CheckBox shangJiaPeiSongCheckBox;
    private CheckBox menDianZiTiChcekBox;

    //支付方式弹出窗口的组件
    private CheckBox zhiFuBaoCheckBox;
    private CheckBox yuEZhiFuCheckBox;

    //商品总价格和商品总数
    private String productSumPrice;//总价格
    private String productSumCount;//总数

    //屏幕像素密度
    private int densty;

    //收货地址id
    private String addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ding_dan_activity_layout);
        setTitle(R.string.order_confirm);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        //获取屏幕像素密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        densty = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）

        helper=new ImageLoadHelper();
        inflater=LayoutInflater.from(this);
        initView();

        //获取收获地址列表
        getShouHuoAddressList();

        //获取购物车页面传递过来的商品列表
        shopModels= (List<ShopModel>) getIntent().getSerializableExtra(MyConstant.SHOP_MODE_LIST_KEY);
        itemGoodCounts= (ArrayList<List<Integer>>) getIntent().getSerializableExtra(MyConstant.XUAN_ZHONG_COUNT_KEY);


        MyLog.d(tag,"shopModels的个数："+shopModels.size());
        //MyLog.d(tag,"第二个商店的商品数量是："+itemGoodCounts.get(1).size());
        productSumPrice=getIntent().getStringExtra(MyConstant.PRODUCT_PRICE_SUM_KEY);
        productSumCount=getIntent().getStringExtra(MyConstant.PRODUCT_SHU_MU_SUM_KEY);

        //给外部的listview设置适配器
        DingDanJieMianListAdapterOut adapterOut=new DingDanJieMianListAdapterOut(this,shopModels,itemGoodCounts);

        //动态设置listview的高度，这个很重要，关于为什么是320的解释，因为我自己设置的小项的高度就是320
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                320*densty/160*shopModels.size());
        listviewOut.setLayoutParams(params);
        listviewOut.setAdapter(adapterOut);


        //设置商品总价
        productSumPriceTextBottomView.setText(productSumPrice);

        //设置实际付款
        setShiFuKuan();

    }


    //设置实付款
    private void setShiFuKuan() {
        double sunPrice=Double.parseDouble(FormatHelper.getNumberFromRenMingBi(productSumPrice));
        double youHuiQuan=Double.parseDouble(FormatHelper.getNumberFromRenMingBi(youHuiQuanDiKouTextView.getText().toString()));
        double shiFuKuan=sunPrice-youHuiQuan;
        shiFuKuanTextView.setText(FormatHelper.getMoneyFormat(shiFuKuan + ""));
    }

    /**
     * 获取屏幕像素密度
     * @return
     */
    public int getDensty() {
        return densty;
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
                        addressId=userModel.getId();//默认地址的id
                        peopleNameTextView.setText(userModel.getUserName()+"");
                        phoneTextView.setText(userModel.getUserPhone()+"");
                        MyLog.d(tag,"countryName="+userModel.getCountryName());
                        if(userModel.getCountryName()==null || userModel.getCountryName().isEmpty() || userModel.getCountryName().equals("null")){
                            userModel.setCountryName("中国");
                            MyLog.d(tag, "countryName=" + userModel.getCountryName());
                        }
                        if(userModel.getProvinceName()==null || userModel.getProvinceName().isEmpty()){
                            userModel.setProvinceName("");
                        }
                        if(userModel.getCityName()==null || userModel.getCityName().isEmpty()){
                            userModel.setCityName("");
                        }
                        if(userModel.getDistrictName()==null || userModel.getDistrictName().isEmpty()){
                            userModel.setDistrictName("");
                        }
                        if(userModel.getShouHuoArea()==null || userModel.getShouHuoArea().isEmpty()){
                            userModel.setShouHuoArea("");
                        }
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
                    String moRenString=ziJsObj.getString("default_address");//默认地址
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
    public void initView() {

        parentView= LayoutInflater.from(this).inflate(R.layout.ding_dan_activity_layout,null);

        shouHuoAddressRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_shou_huo_address_ding_dan);
        peopleNameTextView= (TextView) findViewById(R.id.text_shou_huo_ren_name_ding_dan);
        phoneTextView= (TextView) findViewById(R.id.text_shou_huo_ren_phone_ding_dan);
        addressTextView= (TextView) findViewById(R.id.text_shou_huo_address_ding_dan);
        listviewOut= (ListView) findViewById(R.id.list_shop_list_ding_dan);
        zhiFuFangShiRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_zhi_fu_fang_shi_ding_dan);
        zhiFuFangShiTextView= (TextView) findViewById(R.id.text_zhi_fu_fang_shi_ding_dan);
        shouXuFeiTextView= (TextView) findViewById(R.id.text_shou_xu_fei_ding_dan);
        faPiaoXinXiRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_fa_piao_xin_xi_ding_dan);
        faPiaoTaiTouTextView= (TextView) findViewById(R.id.text_fa_piao_tai_tou_ding_dan);
        faPiaocontentTextView= (TextView) findViewById(R.id.text_fa_piao_content_ding_dan);
        youHuiQuanRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_you_hui_quan_ding_dan);
        youHuiQuanTextView= (TextView) findViewById(R.id.text_you_hui_quan_ding_dan);
        youHuiQuanShuMuTextView= (TextView) findViewById(R.id.text_you_hui_quan_shu_mu_ding_dan);
        jiFenRelaytiveLayout= (RelativeLayout) findViewById(R.id.re_layout_ji_fen_ding_dan);
        jiFenTextView= (TextView) findViewById(R.id.text_ji_fen_ding_dan);
        jiFenImageView= (ImageView) findViewById(R.id.img_ji_fen_ding_dan);
        productSumPriceTextBottomView= (TextView) findViewById(R.id.text_product_sum_price_ding_dan);
        youHuiQuanDiKouTextView= (TextView) findViewById(R.id.text_you_hui_quan_di_kou_ding_dan);
        shiFuKuanTextView= (TextView) findViewById(R.id.text_shi_fu_kuan_ding_dan);
        tiJiaoDingDanButton= (Button) findViewById(R.id.btn_ti_jiao_ding_dan);


        //设置点击事件
        shouHuoAddressRelativeLayout.setOnClickListener(this);
        zhiFuFangShiRelativeLayout.setOnClickListener(this);
        faPiaoXinXiRelativeLayout.setOnClickListener(this);
        youHuiQuanRelativeLayout.setOnClickListener(this);
        jiFenRelaytiveLayout.setOnClickListener(this);
        tiJiaoDingDanButton.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_layout_shou_huo_address_ding_dan://收货地址
                Intent addresListIntent=new Intent(this,ShouHuoRenXinXiListActivity.class);
                startActivityForResult(addresListIntent, SHOU_HUO_REN_XIN_XI_REQUEST_KEY);
                break;
            case R.id.re_layout_zi_ti_shi_jian_ding_dan://点击了自提时间
                ziTiShiJianClickChuLi();
                break;
            case R.id.re_layout_zi_ti_area_ding_dan://点击了自提地点
                Intent ziTiDiDianIntent=new Intent(this,ZiTiAreaActivity.class);
                startActivity(ziTiDiDianIntent);
                break;
            case R.id.re_layout_zhi_fu_fang_shi_ding_dan://点击了支付方式
                zhiFuFangShiClickChuLi();
                break;
            case R.id.re_layout_fa_piao_xin_xi_ding_dan://点击了发票信息
                Intent toFaPiaoIntent=new Intent(this,FaPiaoActiity.class);
                startActivityForResult(toFaPiaoIntent, FA_PIAO_REQUEST_KEY);
                break;
            case R.id.re_layout_you_hui_quan_ding_dan://点击了优惠券
                //Toast.makeText(this,"点击了优惠券",Toast.LENGTH_SHORT).show();
                Intent toYouHuiQuanIntent=new Intent(this,YouHuiQuanActivity.class);
                startActivityForResult(toYouHuiQuanIntent, YOU_HUI_QUAN_REQUEST_KEY);
                break;
            case R.id.re_layout_ji_fen_ding_dan://点击了积分
                Toast.makeText(this,"点击了积分",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_ti_jiao_ding_dan://点击了提交订单
                tiJiaoDingDanClickChuLi();
                break;


            //-----------------------配送方式窗口的点击事件-----------------------------------------
            case R.id.img_close_pei_song_fang_shi://点击了关闭配送方式
                peiSongFangShiWindow.dismiss();
                break;


            //-----------自提时间的弹出窗口------------------------------------------
            case R.id.text_cancel_date_picker://取消
                //Toast.makeText(this,"点击了取消",Toast.LENGTH_SHORT).show();
                ziTiShiJianWindow.dismiss();
                break;

            //-----------------------支付方式窗口的点击事件-----------------------------------------
            case R.id.img_close_zhi_fu_fang_shi://点击了关闭支付方式
                zhiFuFangShiWindow.dismiss();
                break;
            case R.id.re_layout_zhi_fu_bao://点击了支付宝支付
                zhiFuBaoCheckBox.setChecked(true);
                yuEZhiFuCheckBox.setChecked(false);
                zhiFuFangShiTextView.setText("支付宝");
                zhiFuFangShiWindow.dismiss();
                //shouXuFeiTextView.setText("0.00");

                break;
            case R.id.re_layout_yu_e_zhi_fu://点击了余额支付
                zhiFuBaoCheckBox.setChecked(false);
                yuEZhiFuCheckBox.setChecked(true);
                zhiFuFangShiTextView.setText("余额支付");
                zhiFuFangShiWindow.dismiss();
                break;
        }
    }


    /**
     * 提交订单的点击事件处理
     */
    private void tiJiaoDingDanClickChuLi() {
        final String shopValue=getJsonFromShopModels();//上传到服务器的shop键所对应的值
        MyLog.d(tag, "shopValue的值是：" + shopValue);
        String zhiFuFangShi=zhiFuFangShiTextView.getText().toString();
        if("支付宝".equals(zhiFuFangShi)){
            tiJiaoDingDanIntenetRequest(shopValue,"12");
        }else if("余额支付".equals(zhiFuFangShi)){
            tiJiaoDingDanIntenetRequest(shopValue,"11");
        }

    }


    /**
     * 提交订单的网络请求
     * @param shopValue
     */
    private void tiJiaoDingDanIntenetRequest(final String shopValue, final String zhiFuType) {
        StringRequest dingDanRequest=new StringRequest(Request.Method.POST, tiJiaoDingDanUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "提交订单返回的数据是：" + s);
                        try {
                            List<String> dingDans=new ArrayList<String>();
                            JSONArray jsonArray=new JSONArray(s);
                            for(int i=0;i<jsonArray.length();i++){
                                dingDans.add(jsonArray.getString(i));
                            }
                            if("12".equals(zhiFuType)){ //支付宝
                                Intent intent=new Intent(DingDanActivity.this,AfterTiJiaoDingDanActivity.class);
                                intent.putExtra(MyConstant.DING_DAN_BIAN_HAO_LIST_KEY, (Serializable) dingDans);
                                intent.putExtra(MyConstant.SHI_FU_KUAN_KEY,FormatHelper.getNumberFromRenMingBi(shiFuKuanTextView.getText().toString()));
                                intent.putExtra(MyConstant.DING_DAN_SUBJECT_KEY,shopModels.get(0).getGoods().get(0).getGoodName());
                                intent.putExtra(MyConstant.DING_DAN_DESC_KEY,shopModels.get(0).getGoods().get(0).getGoodName());
                                startActivity(intent);
                            }else if("11".equals(zhiFuType)){  //余额
                                yuEZhiFuJieKouChuLi(dingDans);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DingDanActivity.this,"提交订单失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                /*service	create_order_info
                user_id	4189
                sid	333333333333
                address_id	14
                pay_id	12
                goods_amount	399.00
                order_amount	398.00
                inv_payee
                inv_content*/
                MyLog.d(tag,"addressId="+addressId);
                MyLog.d(tag,"zhiFuType="+zhiFuType);
                MyLog.d(tag,"实际付款："+FormatHelper.getOneXiaoShuFormat(shiFuKuanTextView.getText().toString()));
                MyLog.d(tag,"总金额:"+FormatHelper.getOneXiaoShuFormat(productSumPriceTextBottomView.getText().toString()));
                MyLog.d(tag,"抬头："+faPiaoTaiTouTextView.getText().toString());
                MyLog.d(tag, "内容" + faPiaocontentTextView.getText().toString());
                MyLog.d(tag, "店铺内容json是：" + shopValue);

                Map<String,String> map=new HashMap<String,String>();
                map.put("service","create_order_info");
                map.put("user_id",uid);
                map.put("sid",sid);
                map.put("address_id",addressId);
                map.put("pay_id",zhiFuType);
                map.put("goods_amount",FormatHelper.getOneXiaoShuFormat(productSumPriceTextBottomView.getText().toString()));
                map.put("real_pay", FormatHelper.getOneXiaoShuFormat(shiFuKuanTextView.getText().toString()));
                map.put("inv_payee",faPiaoTaiTouTextView.getText().toString());
                map.put("inv_content",faPiaocontentTextView.getText().toString());
                map.put("shops",shopValue);
                return map;
            }
        };
        requestQueue.add(dingDanRequest);
    }


    /**
     * 余额支付接口处理
     */
    private void yuEZhiFuJieKouChuLi(final List<String> dingDans) {
        StringRequest yuERequest=new StringRequest(Request.Method.POST, yuEZhiFuUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag,"余额支付返回的数据是："+s);
                Intent intent=new Intent(DingDanActivity.this,YuEZhiFuSuccessedActivity.class);
                intent.putExtra(MyConstant.DING_DAN_BIAN_HAO_LIST_KEY, (Serializable) dingDans);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                /*service	userMoneyPay
                user_id	1523
                goods_price	19.0*/
                Map<String,String> map=new HashMap<String,String>();
                map.put("service","userMoneyPay");
                map.put("user_id",uid);
                map.put("goods_price",FormatHelper.getNumberFromRenMingBi(shiFuKuanTextView.getText().toString()));
                return map;
            }
        };
        requestQueue.add(yuERequest);
    }


    /**
     * 从shopmodel里面获取json数据
     */
    private String getJsonFromShopModels() {
        String result=null;
        List<ShopModelJson> shopModelJsons=new ArrayList<ShopModelJson>();
        for(int i=0;i<shopModels.size();i++){
            ShopModel shopModel=shopModels.get(i);
            ShopModelJson shopModelJson=new ShopModelJson();
            shopModelJson.setShipping_id(shopModel.getShippingId());//设置快递方式
            shopModelJson.setPostscript(shopModel.getMaiJiaLiuYan());//设置买家留言
            shopModelJson.setGoods_amount(shopModel.getSumPrice());//设置该店铺所有商品的总价格
            shopModelJson.setShipping_fee("0.00");//这个是暂时的
            List<String> goodIds=new ArrayList<String>();
            List<Good> tempGoods=shopModel.getGoods();
            for(int j=0;j<tempGoods.size();j++){
                goodIds.add(tempGoods.get(j).getGoodId());
            }
            shopModelJson.setGoods_id(goodIds);
            shopModelJsons.add(shopModelJson);
        }

        //没有必要再建下面这样的实体
        //DingDanModleJson dingDanModleJson=new DingDanModleJson();
        //dingDanModleJson.setShipping_fee("10.00");
        //dingDanModleJson.setGoods_amount(FormatHelper.getOneXiaoShuFormat(productSumPriceTextBottomView.getText().toString()));
        //dingDanModleJson.setAll_goods(shopModelJsons);

        Gson gson = new Gson();
        MyLog.d(tag, gson.toJson(shopModelJsons));//将上述设置的代码转为json
        result=gson.toJson(shopModelJsons);
        return result;
    }


    /**
     * 支付方式的点击事件处理
     */
    private void zhiFuFangShiClickChuLi() {
        View zhiFuFangShiView=inflater.inflate(R.layout.zhi_fu_fang_shi_tan_chu_layout,null);
        ImageView closeZhiFuFangShiImageView= (ImageView) zhiFuFangShiView.findViewById(R.id.img_close_zhi_fu_fang_shi);
        RelativeLayout zhiFuBaoRelativeLayout= (RelativeLayout) zhiFuFangShiView.findViewById(R.id.re_layout_zhi_fu_bao);
        RelativeLayout yuEZhiFuRelaytiveLayout= (RelativeLayout) zhiFuFangShiView.findViewById(R.id.re_layout_yu_e_zhi_fu);
        zhiFuBaoCheckBox= (CheckBox) zhiFuFangShiView.findViewById(R.id.check_box_zhi_fu_bao);
        yuEZhiFuCheckBox = (CheckBox) zhiFuFangShiView.findViewById(R.id.check_box_yu_e_zhi_fu);
        TextView zhiFuBaoShouXuFeiTextView= (TextView) zhiFuFangShiView.findViewById(R.id.text_zhi_fu_bao_shou_xu_fei);//支付宝手续费
        TextView yuEZhiFuShouXuFeiTextView= (TextView) zhiFuFangShiView.findViewById(R.id.text_yu_e_zhi_fu_shou_xu_fei);//余额支付手续费
        closeZhiFuFangShiImageView.setOnClickListener(this);
        zhiFuBaoRelativeLayout.setOnClickListener(this);
        yuEZhiFuRelaytiveLayout.setOnClickListener(this);
        zhiFuFangShiWindow=new PopupWindow(zhiFuFangShiView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        //zhiFuFangShiWindow.setBackgroundDrawable(new ColorDrawable());
        zhiFuFangShiWindow.setFocusable(true);
        zhiFuFangShiWindow.setOutsideTouchable(false);
        zhiFuFangShiWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);//最后才是show,顺序很重要
        setBackgroundAlpha(0.5f);
        zhiFuFangShiWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
    }


    /**
     * 自提时间的点击事件处理
     */
    private void ziTiShiJianClickChuLi() {
        //Toast.makeText(this,"点击了自提时间",Toast.LENGTH_SHORT).show();
        View ziTiShiJianView=inflater.inflate(R.layout.date_picker_layout_tan_chu,null);
        TextView cancelTextView= (TextView) ziTiShiJianView.findViewById(R.id.text_cancel_date_picker);
        TextView confirmTextView= (TextView) ziTiShiJianView.findViewById(R.id.text_confirm_date_picker);
//        WheelView wheelViewDay= (WheelView) ziTiShiJianView.findViewById(R.id.whe_day);
//        WheelView wheelViewHour= (WheelView) ziTiShiJianView.findViewById(R.id.whe_hour);

        //设置点击事件
        cancelTextView.setOnClickListener(this);
        confirmTextView.setOnClickListener(this);
        final String[] riQis=new String[8];
        String[] xingQiS={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        for(int i=0;i<8;i++){
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH,i);
            String month=FormatHelper.convertStringToTwoString(String.valueOf(calendar.get(Calendar.MONTH)+1));
            String day=FormatHelper.convertStringToTwoString(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            String xingQi=xingQiS[calendar.get(Calendar.DAY_OF_WEEK)-1];
            String riQi=month+"月"+day+"日"+" "+xingQi;
            riQis[i]=riQi;
        }

        final String[] hours={"9:00-12:00"};

       /* OnWheelChangedListener listener=new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                switch (wheel.getId()){
                    case R.id.whe_day:
                        day=riQis[newValue];
                        MyLog.d(tag,day+"");
                        break;
                    case R.id.whe_hour:
                        hour=hours[newValue];
                        MyLog.d(tag,hour+"");
                        break;
                }
            }
        };*/


      /*  wheelViewDay.setViewAdapter(new MyArrayAdapter(this, riQis));
        wheelViewDay.setCurrentItem(0);
        wheelViewDay.setCyclic(false);//设置是否需要循环
        wheelViewDay.addChangingListener(listener);

        wheelViewHour.setViewAdapter(new MyArrayAdapter(this, hours));
        wheelViewHour.setCurrentItem(0);
        wheelViewHour.setCyclic(false);
        wheelViewHour.addChangingListener(listener);*/



        ziTiShiJianWindow=new PopupWindow(ziTiShiJianView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        ziTiShiJianWindow.setBackgroundDrawable(new ColorDrawable());
        ziTiShiJianWindow.setOutsideTouchable(true);
        ziTiShiJianWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);//最后才是show,顺序很重要
        setBackgroundAlpha(0.5f);
        ziTiShiJianWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
    }


    /**
     * 配送方式的点击处理
     */
    private void peiSongFangShiClickChuLi() {
        View peiSongFangShiView=inflater.inflate(R.layout.pei_song_fang_shi_layout,null);
        ImageView closePeiSongFangShi= (ImageView) peiSongFangShiView.findViewById(R.id.img_close_pei_song_fang_shi);
        RelativeLayout shangJiaPeiSongRelativeLayout= (RelativeLayout) peiSongFangShiView.findViewById(R.id.re_layout_shang_jia_pei_song);
        RelativeLayout menDianZitIRelaytiveLayout= (RelativeLayout) peiSongFangShiView.findViewById(R.id.re_layout_men_dian_zi_ti);
        shangJiaPeiSongCheckBox= (CheckBox) peiSongFangShiView.findViewById(R.id.check_box_shang_jia_pei_song);
        menDianZiTiChcekBox= (CheckBox) peiSongFangShiView.findViewById(R.id.check_box_men_dian_zi_ti);
        closePeiSongFangShi.setOnClickListener(this);
        shangJiaPeiSongRelativeLayout.setOnClickListener(this);
        menDianZitIRelaytiveLayout.setOnClickListener(this);
        peiSongFangShiWindow=new PopupWindow(peiSongFangShiView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        //peiSongFangShiWindow.setBackgroundDrawable(new ColorDrawable());
        peiSongFangShiWindow.setFocusable(true);
        peiSongFangShiWindow.setOutsideTouchable(false);
        peiSongFangShiWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);//最后才是show,顺序很重要
        setBackgroundAlpha(0.5f);
        peiSongFangShiWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
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


    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
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

                    addressId=userModel.getId();//需要上传到服务器的接口里面
                }
                break;
            case FA_PIAO_REQUEST_KEY://从发票活动返回的数据
                if(resultCode==RESULT_OK){
                    String faPiaoTaiTou=data.getStringExtra(MyConstant.FA_PIAO_TAI_TOU_KEY);
                    String faPiaoContent=data.getStringExtra(MyConstant.FA_PIAO_CONTENT_KEY);
                    faPiaoTaiTouTextView.setText(faPiaoTaiTou);
                    faPiaocontentTextView.setText(faPiaoContent);
                }
                break;
            case YOU_HUI_QUAN_REQUEST_KEY://从优惠券活动返回的数据
                if(resultCode==RESULT_OK){
                    YouHuiQuanModel model=data.getParcelableExtra(MyConstant.YOU_HUI_QUAN_MODEL_KEY);
                    String shuMu=data.getStringExtra(MyConstant.YOU_HUI_QUAN_SHU_MU_KEY);
                    if (Integer.parseInt(shuMu)>0){
                        youHuiQuanTextView.setText(FormatHelper.getMoneyFormat(model.getJinE()));
                        youHuiQuanDiKouTextView.setText("-"+FormatHelper.getMoneyFormat(model.getJinE()));
                        youHuiQuanShuMuTextView.setText(shuMu);
                    }else{
                        youHuiQuanTextView.setText(FormatHelper.getMoneyFormat(0.00+""));
                        youHuiQuanDiKouTextView.setText("-"+FormatHelper.getMoneyFormat(0.00+""));
                        youHuiQuanShuMuTextView.setText(0.00+"");
                    }
                    setShiFuKuan();//设置实付款
                }
                break;
        }
    }


   /* *//**
     * wheelview的适配器，继承自numerricwheelapter，不需要往里面传入数组，只需要传最大值，最小值即可
     *//*
    public class MyWheelAdapter extends NumericWheelAdapter {

        *//**
         * Constructor
         * @param minValue 传入的数字的最小值
         * @param maxValue 传入的数字的最大值
         *
         *//*
        public MyWheelAdapter(Context context, int minValue, int maxValue) {
            super(context, minValue, maxValue);
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTypeface(Typeface.MONOSPACE);
        }

    }


    *//**
     * Adapter for string based wheel. Highlights the current value.
     * 如果是非纯数字，就必须使用这种适配器，往里面传入数组
     *//*
    private class MyArrayAdapter extends ArrayWheelAdapter<String> {
        *//**
         * Constructor
         *//*
        public MyArrayAdapter(Context context, String[] items) {
            super(context, items);
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTypeface(Typeface.MONOSPACE);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            return super.getItem(index, cachedView, parent);
        }
    }*/

}
