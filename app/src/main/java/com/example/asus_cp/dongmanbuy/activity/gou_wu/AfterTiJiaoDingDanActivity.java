package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.zhi_fu_bao_util.PayResult;
import com.example.asus_cp.dongmanbuy.util.zhi_fu_bao_util.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提交订单之后出现的页面
 * Created by asus-cp on 2016-06-20.
 */
public class AfterTiJiaoDingDanActivity extends Activity implements View.OnClickListener{
    private TextView priceTextView;
    private TextView dingDanHaoTextView;
    private Button zhiFuBaoZhiFuButton;
    private TextView seeDingDanTextView;

    private String price;//付款金额
    private String bianHao;//订单编号
    private String subject;//订单标题
    private String desc;//订单描述
    private String dingDanId;//订单id

    private String tag="AfterTiJiaoDingDanActivity";


    private String payUrl="http://api.zmobuy.com/JK/alipay/alipayapi.php";//支付宝url
    private RequestQueue requestQueue;





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
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
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
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(AfterTiJiaoDingDanActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(AfterTiJiaoDingDanActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(AfterTiJiaoDingDanActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.after_ti_jiao_order_activity_layout);
        priceTextView= (TextView) findViewById(R.id.text_fu_kuan_jin_e_after_ti_jiao_ding_dan);
        dingDanHaoTextView= (TextView) findViewById(R.id.text_ding_dan_hao_after_ti_jiao_ding_dan);
        zhiFuBaoZhiFuButton= (Button) findViewById(R.id.btn_zhi_fu_bao_zhi_fu_after_ti_jiao_ding_dan);
        seeDingDanTextView= (TextView) findViewById(R.id.text_see_ding_dan_after_ti_jiao_ding_dan);

        requestQueue= MyApplication.getRequestQueue();
        dingDanId=getIntent().getStringExtra(MyConstant.DING_DAN_ID_KEY);
        //设置付款金额
        price=getIntent().getStringExtra(MyConstant.SHI_FU_KUAN_KEY);
        priceTextView.setText(price);

        //设置订单编号
        bianHao=getIntent().getStringExtra(MyConstant.DING_DAN_BIAN_HAO_KEY);
        dingDanHaoTextView.setText(bianHao);

        subject=getIntent().getStringExtra(MyConstant.DING_DAN_SUBJECT_KEY);
        desc=getIntent().getStringExtra(MyConstant.DING_DAN_DESC_KEY);
        MyLog.d(tag,"subject="+subject);
        MyLog.d(tag,"desc="+desc);
        MyLog.d(tag,"price="+price);

        //设置点击事件
        zhiFuBaoZhiFuButton.setOnClickListener(this);
        seeDingDanTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_zhi_fu_bao_zhi_fu_after_ti_jiao_ding_dan://点击了支付宝支付
                //Toast.makeText(this,"点击了支付宝支付",Toast.LENGTH_SHORT).show();
                //pay();//调用支付宝支付
                zhiFuClickChuLi();
                break;
            case R.id.text_see_ding_dan_after_ti_jiao_ding_dan://点击了查看订单
                /*Intent seeIntent=new Intent(this,DingDanDetailActivity.class);
                seeIntent.putExtra(MyConstant.TO_DING_DAN_LIST_KEY,MyConstant.ALL_DING_DAN);
                startActivity(seeIntent);*/
                Intent intent=new Intent(AfterTiJiaoDingDanActivity.this,DingDanDetailActivity.class);
                intent.putExtra(MyConstant.DING_DAN_BIAN_HAO_KEY,bianHao);
                intent.putExtra(MyConstant.DING_DAN_ID_KEY,dingDanId);
                startActivity(intent);
                break;
        }
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo(subject, desc, FormatHelper.getNumberFromRenMingBi(price));

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
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
                PayTask alipay = new PayTask(AfterTiJiaoDingDanActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

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
        orderInfo += "&_input_charset=\"utf-8\"";

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
     * 支付的click处理
     */
    public void zhiFuClickChuLi(){
        StringRequest zhiFuRequest=new StringRequest(Request.Method.POST, payUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag,"返回的数据是："+s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                String json="";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(zhiFuRequest);
    }
}
