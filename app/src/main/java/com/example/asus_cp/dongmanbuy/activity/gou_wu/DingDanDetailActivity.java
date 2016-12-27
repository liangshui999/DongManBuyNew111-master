package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyListView;
import com.example.asus_cp.dongmanbuy.model.DingDanModel;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.zhi_fu_bao_util.Base64;
import com.example.asus_cp.dongmanbuy.util.zhi_fu_bao_util.PayResult;
import com.example.asus_cp.dongmanbuy.util.zhi_fu_bao_util.SignUtils;
import com.example.asuscp.dongmanbuy.util.ZhiFuBaoHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单详情的界面
 * Created by asus-cp on 2016-06-21.
 */
public class DingDanDetailActivity extends BaseActivity implements View.OnClickListener{

    private String tag="DingDanDetailActivity";

    private TextView peopleNameTextView;//人名
    private TextView phoneTextView;//电话
    private TextView shengAddressTextView;//收货地址
    private TextView shiAddressTextView;//收货地址
    private TextView xianAddressTextView;//收货地址
    private TextView detailAddressTextView;//收货地址
    private TextView shopNameTextView;//店铺名称
    private TextView dingDanHaoTextView;//订单号
    private TextView dingDanTimeTextView;//订单时间
    private LinearLayout productDisplayLinearLayoutOrignal;//商品的展示区
    private ImageView firstPicImageView;//第一个商品的图片
    private ImageView secondPicImageView;//第二个商品的图片
    private ImageView threePicImageView;//第三个商品的图片
    private LinearLayout displayAllProductLineatLayout;//展示所有商品
    private TextView gongJiJianTextView;//共几件商品
    private LinearLayout productListLinearLayoutZhanKai;//包含商品列表的容器
    private MyListView listView;//展示商品列表
    private ImageView downImageView;//向下的箭头
    private TextView peiSongFangShiTextView;//配送方式
    private TextView yunFeiTextView;//配送方式旁边的运费
    private TextView maiJiaLiuYanTextView;//买家留言
    private TextView zhiFuFangShiTextView;//支付方式
    private TextView shouXuFeiTextView;//手续费
    private TextView faPiaoTaiTouTextView;//发票抬头
    private TextView faPiaocontentTextView;//发票内容
    private TextView productSumPriceTextView;//商品金额
    private TextView youHuiQuanDiKouTextView;//商品优惠
    private TextView yunFeiBottomTextView;//运费
    private TextView yingFuZongETextView;//应付总额
    private Button cancelOrderButton;//取消订单
    private Button zhiFuBaoZhiFuButton;//支付宝支付
    private RelativeLayout cancelAndPayRelativeLayout;//支付宝支付和取消订单
    private RelativeLayout yiQueRenRelaytiveLayout;//已确认

    private DingDanModel dingDanModel;//其他活动传递过来的订单实体
    private ArrayList<Good> goods;
    private ArrayList<Integer> itemProductCount;

    private ImageLoadHelper helper;

    private String cancelOrderUrl="http://www.zmobuy.com/PHP/?url=/order/cancel";//取消订单的接口
    private String orderInfoUrl="http://www.zmobuy.com/PHP/?url=/order/info";//订单详细信息的接口
    private String regionUrl="http://www.zmobuy.com/PHP/?url=/region";//地区的接口

    private String bianHao;//订单编号
    private String id;//订单id
    private String subject;//订单标题
    private String desc;//订单描述
    private String price;//订单价格

    private String payUrl="http://api.zmobuy.com/JK/alipay/alipayapi.php";//支付宝url
    private String checkUrl="http://mv.zmobuy.com/order/check.action";//验签的url
    private ZhiFuBaoHelper zhiFuBaoHelper;

//    public static  String PARTNER;
//    public static  String SELLER;
//    public static  String RSA_PRIVATE;

    public static String DAI_FU_KUAN="0";//待付款
    public static String DAI_SHOU_HUO="2";//待收货


    // 商户PID
    public static final String PARTNER = "2088121021289716";
    // 商户收款账号
    public static final String SELLER = "postmaster@zmobuy.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALxwd6wP1QVwzkOB" +
            "F3nH2lzKbMybXWyrY0OJpt9XPVAyiLL8loevP9xJBI7n6LpxYXfXOcT7G0KxWbSK" +
            "RKz34r1zJ0eK2tDwSz/q8PLR/LryoF8r4NhqUiwcgnBsx9pTw/nV5u+XudQB/1bS" +
            "qR3Z099dim9Nto+YPLMzvtSuFVn/AgMBAAECgYAp8e7xgeSs/Vssc4PCO9ZDaVum" +
            "f77f/ZZu5ika9dRUEauUC92F/mB8rFQzazPGrI5BmsrlKe/7fHa3VT/MMLFrkFVY" +
            "UyDsXMoZksdEqPcSJ1JwOt8Pn0UBlWa7uzJGW3kRiYpKUcpp5IefHz/Bg83js/V6" +
            "C4+9BK/zpKNBoU+l4QJBAOf0McKdaJSP+zpijXHy4ZZv9irinbNn02EcoO2rosqa" +
            "F3hsY1IlefPOvFGbMspqH6ATHK0+M5LMC+f0ZOWl2dMCQQDP+XGWqRhgZC95uCvz" +
            "qaCrx4wUBz2fLbfxpxJbsmgCeFgbJdNOsSMcjZ4alv648ZWXQm5V5GD/YuO5LGN9" +
            "KhelAkB1v5QHHIs7Z3/8Wo09PDPif4GysYmmyl0W3kafgFLKkMC7ZCJjUB0BdIBK" +
            "2QNGl+roAuu60mmx6p1cqccSgUf7AkBhWZz+7fYYIK1MZ5ZDP1KTNhvuwBjrKsZg" +
            "mljwjUk8ZsKvKnyH6EjMM8ofHjDrt0HThOgK0pVI1ixMYGfNjed1AkEA5IOsn7jP" +
            "Ef3uJnorl15rhzY8uEopIWwdWasBk14RkoYwgsJxR4cOU5KLbNgm5EPXekA5C1SE" +
            "tLTyJx7aWI6SLw==";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8cHesD9UFcM5DgRd5x9pcymzM" +
            "m11sq2NDiabfVz1QMoiy/JaHrz/cSQSO5+i6cWF31znE+xtCsVm0ikSs9+K9cydH" +
            "itrQ8Es/6vDy0fy68qBfK+DYalIsHIJwbMfaU8P51ebvl7nUAf9W0qkd2dPfXYpv" +
            "TbaPmDyzM77UrhVZ/wIDAQAB";
    private static final int SDK_PAY_FLAG = 1;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {

                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = (String) msg.obj;// 同步返回需要验证的信息
                    checkResultInfoFromIntenet(payResult);

                    MyLog.d(tag,"支付之后支付宝返回的信息："+resultInfo);



                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(DingDanDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(DingDanDetailActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(DingDanDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.order_detail_activity_layout);
        setTitle(R.string.order_detail);
        init();

    }

    /**
     * 初始化的方法
     */
    private void init() {
       dingDanModel=getIntent().getParcelableExtra(MyConstant.DING_DAN_MODEL_KEY);//从订单列表传递过来的，因为那边没有收货地址的数据，所以还是需要重新访问一次网络
        bianHao=getIntent().getStringExtra(MyConstant.DING_DAN_BIAN_HAO_KEY);
        id=getIntent().getStringExtra(MyConstant.DING_DAN_ID_KEY);
        if(bianHao==null){
            bianHao=dingDanModel.getOrderBianHao();
            id=dingDanModel.getOrderId();
        }
        helper=new ImageLoadHelper();
        initView();
        setValueToView();
    }

    /**
     * 初始化view
     */
    public void initView() {
        peopleNameTextView= (TextView) findViewById(R.id.text_shou_huo_ren_name_order_detail);
        phoneTextView= (TextView) findViewById(R.id.text_shou_huo_ren_phone_order_detail);
        shengAddressTextView= (TextView) findViewById(R.id.text_shou_huo_address_sheng_order_detail);
        shiAddressTextView= (TextView) findViewById(R.id.text_shou_huo_address_shi_order_detail);
        xianAddressTextView= (TextView) findViewById(R.id.text_shou_huo_address_xian_order_detail);
        detailAddressTextView= (TextView) findViewById(R.id.text_shou_huo_address_order_detail);
        shopNameTextView= (TextView) findViewById(R.id.text_shop_name_order_detail);
        dingDanHaoTextView= (TextView) findViewById(R.id.text_ding_dan_hao_ding_dan_order_detail);
        dingDanTimeTextView= (TextView) findViewById(R.id.text_time_ding_dan_order_detail);
        productDisplayLinearLayoutOrignal = (LinearLayout) findViewById(R.id.ll_product_display_area_order_detail);
        firstPicImageView= (ImageView) findViewById(R.id.img_one_ding_dan_order_detail);
        secondPicImageView= (ImageView) findViewById(R.id.img_two_ding_dan_order_detail);
        threePicImageView= (ImageView) findViewById(R.id.img_three_ding_dan_order_detail);
        displayAllProductLineatLayout= (LinearLayout) findViewById(R.id.ll_dispaly_all_product_ding_dan_order_detail);
        gongJiJianTextView= (TextView) findViewById(R.id.text_product_sum_ding_dan_order_detail);
        productListLinearLayoutZhanKai = (LinearLayout) findViewById(R.id.ll_list_view_ding_dan_order_detail);
        downImageView= (ImageView) findViewById(R.id.img_down_ding_dan_order_detail);
        listView= (MyListView) findViewById(R.id.my_list_view_ding_dan_order_detail);
        peiSongFangShiTextView= (TextView) findViewById(R.id.text_pei_song_fang_shi_order_detail);
        yunFeiTextView= (TextView) findViewById(R.id.text_yun_fei_order_detail);
        maiJiaLiuYanTextView= (TextView) findViewById(R.id.text_mai_jia_liu_yan_order_detail);//买家留言
        zhiFuFangShiTextView= (TextView) findViewById(R.id.text_zhi_fu_fang_shi_order_detail);
        shouXuFeiTextView= (TextView) findViewById(R.id.text_shou_xu_fei_order_detail);
        productSumPriceTextView= (TextView) findViewById(R.id.text_product_sum_price_order_detail);
        youHuiQuanDiKouTextView= (TextView) findViewById(R.id.text_you_hui_quan_di_kou_order_detail);
        yunFeiBottomTextView= (TextView) findViewById(R.id.text_yun_fei_bottom_order_detail);//最底下的运费
        yingFuZongETextView= (TextView) findViewById(R.id.text_ying_fu_price_order_detail);
        cancelOrderButton= (Button) findViewById(R.id.btn_cancel_order_order_detail);
        zhiFuBaoZhiFuButton= (Button) findViewById(R.id.btn_zhi_fu_bao_zhi_fu_order_detail);
        faPiaoTaiTouTextView= (TextView) findViewById(R.id.text_fa_piao_tai_tou_order_detail);
        faPiaocontentTextView= (TextView) findViewById(R.id.text_fa_piao_content_order_detail);
        cancelAndPayRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_cancel_and_pay_gu_ding);
        yiQueRenRelaytiveLayout= (RelativeLayout) findViewById(R.id.re_layout_yi_que_ren);

       // getDataFromIntenet();

        //设置点击事件
        displayAllProductLineatLayout.setOnClickListener(this);
        downImageView.setOnClickListener(this);
        cancelOrderButton.setOnClickListener(this);
        zhiFuBaoZhiFuButton.setOnClickListener(this);
    }


    /**
     * 对支付宝的支付结果进行验签(验签成功之后，服务端应该改变该订单的订单状态)
     */
    private void checkResultInfoFromIntenet(final PayResult payResult) {
        StringRequest checkRrequest=new StringRequest(Request.Method.POST, checkUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyLog.d(tag,"验签返回的数据："+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map=new HashMap<>();
                String resultStatus=payResult.getResultStatus();
                String result=payResult.getResult();
                map.put("resultStatus",resultStatus);
                map.put("result",result);

                MyLog.d(tag, "resultStatus:" + resultStatus);
                MyLog.d(tag,"result:"+result);
                return map;
            }
        };
        requestQueue.add(checkRrequest);
    }




    /**
     * 此方法作废，订单详情的接口已经没有使用了
     */
    private void getDataFromIntenet() {
        StringRequest orderInfoRequest = new StringRequest(Request.Method.POST, orderInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        dingDanModel = parseJson(s);
                        setValueToView();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String json = "{\"session\":{\"uid\":\"" + uid + "\",\"sid\":\"" + sid + "\"},\"order_id\":\"" + id + "\",\"order_sn\":\"" + bianHao + "\",\"pay_id\":\"\",\"order_time\":\"\",\"pay_code\":\"\"}";
                map.put("json", json);
                return map;
            }
        };
        requestQueue.add(orderInfoRequest);
    }


    /**
     * 解析json数据
     * @param s
     */
    private DingDanModel parseJson(String s) {
        MyLog.d(tag, "返回的数据是：" + s);
        DingDanModel dingDanModel=new DingDanModel();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONObject jsonObject2=jsonObject1.getJSONObject("order");
            dingDanModel.setOrderId(jsonObject2.getString("order_id"));
            dingDanModel.setOrderBianHao(jsonObject2.getString("order_sn"));
            dingDanModel.setOrderStatus(jsonObject2.getString("order_status"));
            dingDanModel.setShippingStatus(jsonObject2.getString("shipping_status"));
            dingDanModel.setPayStatus(jsonObject2.getString("pay_status"));
            dingDanModel.setShouHuoRenName(JsonHelper.decodeUnicode(jsonObject2.getString("consignee")));
            dingDanModel.setSheng(jsonObject2.getString("province"));
            dingDanModel.setShi(jsonObject2.getString("city"));
            dingDanModel.setXian(jsonObject2.getString("district"));
            dingDanModel.setDetailAddress(JsonHelper.decodeUnicode(jsonObject2.getString("address")));
            dingDanModel.setPhone(jsonObject2.getString("tel"));
            dingDanModel.setShipName(JsonHelper.decodeUnicode(jsonObject2.getString("shipping_name")));//配送方式
            dingDanModel.setShouXuFei(jsonObject2.getString("pay_fee"));
            dingDanModel.setSumPrice(jsonObject2.getString("total_fee"));
            dingDanModel.setOrderTime(jsonObject2.getString("formated_add_time"));
            dingDanModel.setZhiFuFangShi(FormatHelper.getStrFromHtmlBiaoQian(jsonObject2.getString("pay_name")));
            dingDanModel.setFaPiaoTaiTou(JsonHelper.decodeUnicode(jsonObject2.getString("inv_payee")));
            dingDanModel.setFaPiaoContent(JsonHelper.decodeUnicode(jsonObject2.getString("inv_content")));
            dingDanModel.setGoodsSumPrice(jsonObject2.getString("goods_amount"));
            dingDanModel.setShipFee(jsonObject2.getString("shipping_fee"));
            MyLog.d(tag,"转换前支付方式："+jsonObject2.getString("pay_name"));
            MyLog.d(tag,"转换后支付方式"+FormatHelper.getStrFromHtmlBiaoQian(jsonObject2.getString("pay_name")));

            List<Good> goods=new ArrayList<Good>();
            JSONArray jsonArray=jsonObject1.getJSONArray("goods_list");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject ziJson=jsonArray.getJSONObject(i);
                Good good=new Good();
                good.setGoodId(ziJson.getString("goods_id"));
                good.setGoodName(JsonHelper.decodeUnicode(ziJson.getString("goods_name")));
                good.setDingDanNumber(ziJson.getString("goods_number"));
                good.setMarket_price(JsonHelper.decodeUnicode(ziJson.getString("market_price")));
                good.setShopPrice(JsonHelper.decodeUnicode(ziJson.getString("goods_price")));
                good.setGoodsImg(ziJson.getString("goods_thumb"));
               goods.add(good) ;
            }

            dingDanModel.setGoods(goods);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dingDanModel;
    }


    /**
     * 给view设置值
     */
    private void setValueToView() {
//        setAreaValue(1 + "", dingDanModel.getSheng() + "", shengAddressTextView);
//        setAreaValue(dingDanModel.getSheng(), dingDanModel.getShi(), shiAddressTextView);
//        setAreaValue(dingDanModel.getShi(), dingDanModel.getXian(), xianAddressTextView);

        peopleNameTextView.setText(dingDanModel.getShouHuoRenName());
        phoneTextView.setText(dingDanModel.getPhone());
        shengAddressTextView.setText(dingDanModel.getSheng());
        shiAddressTextView.setText(dingDanModel.getShi());
        xianAddressTextView.setText(dingDanModel.getXian());
        detailAddressTextView.setText(dingDanModel.getDetailAddress());
        shopNameTextView.setText(dingDanModel.getShopName());
        dingDanHaoTextView.setText(dingDanModel.getOrderBianHao());
        dingDanTimeTextView.setText(dingDanModel.getOrderTime());
        MyLog.d(tag, "总价=" + dingDanModel.getSumPrice());
        MyLog.d(tag,"格式化之后的总价："+FormatHelper.getMoneyFormat(dingDanModel.getSumPrice()));
        productSumPriceTextView.setText(FormatHelper.getMoneyFormat(dingDanModel.getSumPrice()));//设置商品金额
        yingFuZongETextView.setText(FormatHelper.getMoneyFormat(dingDanModel.getSumPrice()));
        peiSongFangShiTextView.setText(dingDanModel.getShipName());//设置配送方式
        yunFeiTextView.setText(dingDanModel.getShipFee());
        yunFeiBottomTextView.setText(dingDanModel.getShipFee());
        maiJiaLiuYanTextView.setText(dingDanModel.getMaiJiaLiuYan());//设置买家留言
        zhiFuFangShiTextView.setText(FormatHelper.getStrFromHtmlBiaoQian(dingDanModel.getZhiFuFangShi()));
        faPiaoTaiTouTextView.setText(dingDanModel.getFaPiaoTaiTou());
        faPiaocontentTextView.setText(dingDanModel.getFaPiaoContent());

        //设置支付状态，隐藏固定栏里面的项目
        if(DAI_SHOU_HUO.equals(dingDanModel.getPayStatus())){    //已支付过了
            cancelAndPayRelativeLayout.setVisibility(View.GONE);
            yiQueRenRelaytiveLayout.setVisibility(View.VISIBLE);
        }else if(DAI_FU_KUAN.equals(dingDanModel.getPayStatus())){  //未支付
            cancelAndPayRelativeLayout.setVisibility(View.VISIBLE);
            yiQueRenRelaytiveLayout.setVisibility(View.GONE);
        }

        MyLog.d(tag, "支付方式是：" + dingDanModel.getZhiFuFangShi());


        goods= (ArrayList<Good>) dingDanModel.getGoods();

        if (goods.size() == 1) {
            setValueToImageView(goods.get(0).getGoodsImg(), firstPicImageView);
        } else if (goods.size() == 2) {
            setValueToImageView(goods.get(0).getGoodsImg(), firstPicImageView);
            setValueToImageView(goods.get(1).getGoodsImg(), secondPicImageView);

        } else if (goods.size() >= 3) {
            setValueToImageView(goods.get(0).getGoodsImg(), firstPicImageView);
            setValueToImageView(goods.get(1).getGoodsImg(), secondPicImageView);
            setValueToImageView(goods.get(2).getGoodsImg(), threePicImageView);
        }


        price=dingDanModel.getSumPrice();//设置传递给支付宝的价格
        subject=goods.get(0).getGoodName();//设置传递给支付宝的标题
        desc=goods.get(0).getGoodName();//设置传递给支付宝的内容描述


        //计算总共有多少件商品
        int sum=0;
        itemProductCount=new ArrayList<Integer>();
        for(int i=0;i<goods.size();i++){
            itemProductCount.add(Integer.parseInt(goods.get(i).getDingDanNumber()));
            sum=sum+Integer.parseInt(goods.get(i).getDingDanNumber());
        }
        gongJiJianTextView.setText("共"+sum+"件");


    }



    /**
     * 给商品的imageview赋值
     */
    private void setValueToImageView(String url,ImageView imageView) {
        imageView.setTag(url);
        ImageLoader imageLoader3=helper.getImageLoader();
        ImageLoader.ImageListener listener3=imageLoader3.getImageListener(imageView, R.mipmap.yu_jia_zai,
                R.mipmap.yu_jia_zai);
        imageLoader3.get(url,listener3,400,400);
    }


    /**
     * 给地区的view设置值
     */
    private void setAreaValue(final String parentId, final String id, final TextView textView) {
        StringRequest shengRequest=new StringRequest(Request.Method.POST, regionUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "地区返回的数据" + s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            JSONArray jsonArray=jsonObject1.getJSONArray("regions");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject ziJson=jsonArray.getJSONObject(i);
                                if(id.equals(ziJson.getString("id"))){
                                    textView.setText(ziJson.getString("name"));
                                    break;
                                }
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
                String json="{\"parent_id\":\""+parentId+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(shengRequest);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_dispaly_all_product_ding_dan_order_detail://展示商品列表
                Intent intent=new Intent(this,DingDanGoodDispalyListActivity.class);
                intent.putExtra(MyConstant.GOOD_LIST_KEY,goods);
                intent.putExtra(MyConstant.ITEM_PRODUCT_COUNT_KEY,itemProductCount);
                startActivity(intent);
                break;
            case R.id.img_down_ding_dan_order_detail://点击了收起商品列表
                //Toast.makeText(this,"点击了收起商品列表",Toast.LENGTH_SHORT).show();
                productDisplayLinearLayoutOrignal.setVisibility(View.VISIBLE);
                productListLinearLayoutZhanKai.setVisibility(View.GONE);
                break;
            case R.id.btn_cancel_order_order_detail://点击了取消订单
                AlertDialog.Builder builder=new AlertDialog.Builder(DingDanDetailActivity.this);
                builder.setMessage("您确定需要取消订单吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelOrderClickChuLi(dingDanModel);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog=builder.show();
                dialog.show();
                break;
            case R.id.btn_zhi_fu_bao_zhi_fu_order_detail://点击了支付宝支付
                //zhiFuBaoZhiFuClickChuLi();
                jieKouZhiFuClickChuLi();
                //test();
                break;

        }
    }

    //用于测试的方法
    private void test() {
//        String s="service=\"mobile.securitypay.pay\"&partner=\"2088121021289716\"&seller_id=\"postmaster@zmobuy.com\"&payment_type=\"1\"&notify_url=\"http://api.zmobuy.com/JK/alipay/notify_url.php\"&return_url=\"m.alipay.com\"&_input_charset=\"utf-8\"&out_trade_no=\"20161025151932\"&subject=\"舰队Collection舰娘北方栖姬北酱动漫周边发卡发箍头饰\"&total_fee=\"0.01\"&body=\"舰队Collection舰娘北方栖姬北酱动漫周边发卡发箍头饰\"&it_b_pay=\"30m\"&success=\"true\"&sign_type=\"RSA\"&sign=\"cH4rNB5blOzPu3en9QPNd68WeGmviGSzAOB08KKpC75kJICJxPv9jcwaBAQBsq0hdTRgw6gt5xwMK49T/F+6HfBAah6G9mh8eGl5gETGTINAmOVOKwlDziXF2rIKL7ASXAQb7R5Gd7gWRzk26zmucn6DFa+ZkzMXVKomn26w8UQ=\"";
//        //String signType=PayResult.getSignType(s);
//        String sinContent=PayResult.getSignContent(s);
//        String yuanShi="{"+PayResult.getYuanShiContent(s)+"}";
//        //MyLog.d(tag,"signType:"+signType);
//        MyLog.d(tag,"sinContent:"+sinContent);
//        MyLog.d(tag,"yunshi:"+yuanShi);
//        MyLog.d(tag,"签名验证结果："+SignUtils.verify(yuanShi,sinContent,RSA_PUBLIC));
//        String content="hahahahhhhhhhhhhhhhhhhhh";
//        String result=sign(content);
//        MyLog.d(tag,"实验的结果:"+SignUtils.verify(content, result, RSA_PUBLIC)+"..."+result);
    }



    /**
     * 接口支付的click处理
     */
    public void jieKouZhiFuClickChuLi(){
        StringRequest zhiFuRequest=new StringRequest(Request.Method.POST, payUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String s) {
                        MyLog.d(tag,"使用接口签名返回的数据是："+s);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(DingDanDetailActivity.this);
                                String result=alipay.pay(s, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        }).start();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                MyLog.d(tag,volleyError.toString());
                volleyError.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                MyLog.d(tag,"价格="+price);
                map.put("out_trade_no",bianHao);
                map.put("subject",subject);
                map.put("total_fee",FormatHelper.getNumberFromRenMingBi(price));
                map.put("body",desc);
                MyLog.d(tag,"编号="+bianHao);
                return map;
            }
        };
        requestQueue.add(zhiFuRequest);
    }


    /**
     * 支付宝支付的点击事件处理
     */
    private void zhiFuBaoZhiFuClickChuLi() {
        List<Good> goods=dingDanModel.getGoods();
        subject="商品支付";
        for(int i=0;i<goods.size();i++){
            subject=goods.get(i).getGoodName()+" ";
        }
        price=dingDanModel.getSumPrice();
        pay();
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new android.app.AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo(subject, desc, FormatHelper.getNumberFromRenMingBi(price));
        orderInfo=PayResult.SortString(orderInfo);//对orderinfo进行排序

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);

        //这段是我加的
        String content=orderInfo;
        String resultM=sign(orderInfo);
        MyLog.d(tag,"我签名之前的内容："+content);
        MyLog.d(tag,"我签名之后的内容："+resultM);
        try {
            MyLog.d(tag,"我签名之后做urlencoder:"+URLEncoder.encode(resultM, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MyLog.d(tag,"用orderinfo做实验："+SignUtils.verify(content,resultM,RSA_PUBLIC));
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(DingDanDetailActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                MyLog.d(tag,"支付宝同步返回的结果："+result);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }





    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + bianHao + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }




    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }



    /**
     * 取消订单的点击事件处理
     * @param dingDanModel
     */
    private void cancelOrderClickChuLi(final DingDanModel dingDanModel) {
        StringRequest cacelOrderRequest=new StringRequest(Request.Method.POST, cancelOrderUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "返回的数据是：" + s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("status");
                            String flag=jsonObject1.getString("succeed");
                            if("1".equals(flag)){
                               Toast.makeText(DingDanDetailActivity.this,"取消成功",Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(DingDanDetailActivity.this,"取消失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DingDanDetailActivity.this,"取消失败",Toast.LENGTH_SHORT).show();
                        }
                        backEventChuLi();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"order_id\":\""+dingDanModel.getOrderId()+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(cacelOrderRequest);
    }


    @Override
    public void onBackPressed() {
        backEventChuLi();
    }

    /**
     * 返回按钮的点击事件处理
     */
    private void backEventChuLi() {
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void backImageViewClickChuLi() {
        getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backEventChuLi();
            }
        });
    }
}
