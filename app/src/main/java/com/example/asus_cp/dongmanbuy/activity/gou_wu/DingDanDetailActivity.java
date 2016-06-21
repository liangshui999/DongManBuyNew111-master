package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.example.asus_cp.dongmanbuy.adapter.DingDanJieMianListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyListView;
import com.example.asus_cp.dongmanbuy.model.DingDanModel;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单详情的界面
 * Created by asus-cp on 2016-06-21.
 */
public class DingDanDetailActivity extends Activity implements View.OnClickListener{

    private String tag="DingDanDetailActivity";

    private TextView peopleNameTextView;//人名
    private TextView phoneTextView;//电话
    private TextView addressTextView;//收货地址
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
    private TextView zhiFuFangShiTextView;//支付方式
    private TextView shouXuFeiTextView;//手续费
    private TextView faPiaoTaiTouTextView;//发票抬头
    private TextView faPiaocontentTextView;//发票内容
    private TextView productSumPriceTextView;//商品金额
    private TextView youHuiQuanDiKouTextView;//商品优惠
    private TextView yingFuZongETextView;//应付总额
    private Button cancelOrderButton;//取消订单
    private Button zhiFuBaoZhiFuButton;//支付宝支付

    private DingDanModel dingDanModel;//其他活动传递过来的订单实体
    private List<Good> goods;
    private List<Integer> itemProductCount;

    private ImageLoadHelper helper;


    private String cancelOrderUrl="http://www.zmobuy.com/PHP/?url=/order/cancel";//取消订单的接口

    private RequestQueue requestQueue;

    private String uid;
    private String sid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_detail_activity_layout);
        init();

    }

    /**
     * 初始化的方法
     */
    private void init() {
       dingDanModel=getIntent().getParcelableExtra(MyConstant.DING_DAN_MODEL_KEY);
        helper=new ImageLoadHelper();
        requestQueue= MyApplication.getRequestQueue();
        SharedPreferences sharedPreferences = getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, Context.MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        initView();

    }

    /**
     * 初始化view
     */
    private void initView() {
        peopleNameTextView= (TextView) findViewById(R.id.text_shou_huo_ren_name_order_detail);
        phoneTextView= (TextView) findViewById(R.id.text_shou_huo_ren_phone_order_detail);
        addressTextView= (TextView) findViewById(R.id.text_shou_huo_address_order_detail);
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
        zhiFuFangShiTextView= (TextView) findViewById(R.id.text_zhi_fu_fang_shi_order_detail);
        shouXuFeiTextView= (TextView) findViewById(R.id.text_shou_xu_fei_order_detail);
        productSumPriceTextView= (TextView) findViewById(R.id.text_product_sum_price_order_detail);
        youHuiQuanDiKouTextView= (TextView) findViewById(R.id.text_you_hui_quan_di_kou_order_detail);
        yingFuZongETextView= (TextView) findViewById(R.id.text_ying_fu_price_order_detail);
        cancelOrderButton= (Button) findViewById(R.id.btn_cancel_order_order_detail);
        zhiFuBaoZhiFuButton= (Button) findViewById(R.id.btn_zhi_fu_bao_zhi_fu_order_detail);


        dingDanHaoTextView.setText(dingDanModel.getOrderBianHao());
        dingDanTimeTextView.setText(FormatHelper.getDate(dingDanModel.getOrderTime()));
        productSumPriceTextView.setText(FormatHelper.getMoneyFormat(dingDanModel.getSumPrice()));
        yingFuZongETextView.setText(FormatHelper.getMoneyFormat(dingDanModel.getSumPrice()));

        goods=dingDanModel.getGoods();
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


        //计算总共有多少件商品
        int sum=0;
        itemProductCount=new ArrayList<Integer>();
        for(int i=0;i<goods.size();i++){
            itemProductCount.add(Integer.parseInt(goods.get(i).getDingDanNumber()));
            sum=sum+Integer.parseInt(goods.get(i).getDingDanNumber());
        }
        gongJiJianTextView.setText("共"+sum+"件");

        //设置点击事件
        displayAllProductLineatLayout.setOnClickListener(this);
        downImageView.setOnClickListener(this);
        cancelOrderButton.setOnClickListener(this);
        zhiFuBaoZhiFuButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_dispaly_all_product_ding_dan_order_detail://展示商品列表
                //Toast.makeText(this,"展示商品列表",Toast.LENGTH_SHORT).show();
                productDisplayLinearLayoutOrignal.setVisibility(View.GONE);
                productListLinearLayoutZhanKai.setVisibility(View.VISIBLE);
                DingDanJieMianListAdapter adapter=new DingDanJieMianListAdapter(this,goods,itemProductCount);
                listView.setAdapter(adapter);
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
                Toast.makeText(this,"点击了支付宝支付",Toast.LENGTH_SHORT).show();
                break;

        }
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
                                finish();
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
                Map<String,String> map=new HashMap<String, String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"order_id\":\""+dingDanModel.getOrderId()+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(cacelOrderRequest);
    }
}
