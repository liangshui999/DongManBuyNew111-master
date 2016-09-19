package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.DingDanHaoProduceHelper;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.zhi_fu_bao_util.PayResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 充值界面
 * Created by asus-cp on 2016-06-24.
 */
public class ChongZhiActivity extends BaseActivity implements View.OnClickListener{

    private String tag="ChongZhiActivity";

    private EditText chongZhiJinEEditeText;//充值金额
    private EditText beiZhuEditeText;//备注
    private RelativeLayout chongZhiFangShiRelativeLayout;//充值方式
    private Button tiJiaoShenQingButton;//提交申请
    private TextView chongZhiFangShiTextView;//充值方式
    private TextView shouXuFeiTextView;//手续费

    private View parentView;//弹出窗口的父窗口
    private LayoutInflater inflater;

    private PopupWindow chongZhiFangShiWindow;
    private CheckBox zhiFuBaoCheckBox;
    private TextView zhiFuBaoShouXuFeiTanChuTextView;//弹出窗口中的

    private String payUrl="http://api.zmobuy.com/JK/alipay/alipayapi.php";//支付宝url
    private static final int SDK_PAY_FLAG = 1;

    private String chongZhiUrl="http://api.zmobuy.com/JK/base/model.php";//充值的接口，充值之后通知服务器，我充了多少钱


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
                        Toast.makeText(ChongZhiActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                        afterZhiFuBaoChongZhi();
                        MyLog.d(tag,"充值成功");
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ChongZhiActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ChongZhiActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

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
        setContentLayout(R.layout.chong_zhi_activity_layout);
        setTitle(R.string.chong_zhi);
        initView();
    }

    @Override
    public void initView() {
        inflater=LayoutInflater.from(this);
        parentView=inflater.inflate(R.layout.chong_zhi_activity_layout, null);

        chongZhiJinEEditeText= (EditText) findViewById(R.id.edit_chong_zhi_jin_e_chong_zhi);
        beiZhuEditeText= (EditText) findViewById(R.id.edit_bei_zhu_chong_zhi);
        chongZhiFangShiRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_chong_zhi_fang_shi_chong_zhi);
        tiJiaoShenQingButton= (Button) findViewById(R.id.btn_ti_jiao_shen_qing_chong_zhi);
        chongZhiFangShiTextView= (TextView) findViewById(R.id.text_zhi_fu_fang_shi_chong_zhi);
        shouXuFeiTextView= (TextView) findViewById(R.id.text_shou_xu_fei_chong_zhi);

        //设置点击事件
        chongZhiFangShiRelativeLayout.setOnClickListener(this);
        tiJiaoShenQingButton.setOnClickListener(this);
    }



    /**
     * 支付宝充值成功之后，通知服务器将余额更改掉
     */
    private void afterZhiFuBaoChongZhi() {
        StringRequest afterChongRequest=new StringRequest(Request.Method.POST, chongZhiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "充值之后返回的数据：" + s);
                        Intent intent=new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                /*service	user_account
                user_id	4156
                recharge_money	0.01*/
                MyLog.d(tag,"uid="+uid+"....."+"金额:"+FormatHelper.getNumberFromRenMingBi(chongZhiJinEEditeText.getText().toString()));
                Map<String,String> map=new HashMap<String,String>();
                map.put("service","user_account");
                map.put("user_id",uid);
                map.put("recharge_money",FormatHelper.getNumberFromRenMingBi(chongZhiJinEEditeText.getText().toString()));
                return map;
            }
        };
        requestQueue.add(afterChongRequest);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_layout_chong_zhi_fang_shi_chong_zhi://点击了充值方式
                chongZhiFangShiClickChuLi();
                break;
            case R.id.btn_ti_jiao_shen_qing_chong_zhi://点击了提交申请
                String jinE=chongZhiJinEEditeText.getText().toString();
                String chongZhiFangShi=chongZhiFangShiTextView.getText().toString();
                if("".equals(jinE) || jinE.isEmpty()){
                    Toast.makeText(this,"充值金额为空",Toast.LENGTH_SHORT).show();
                }else if("".equals(chongZhiFangShi) || chongZhiFangShi.isEmpty()){
                    Toast.makeText(this,"请选择支付方式",Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(this,"此功能暂时没有接口",Toast.LENGTH_SHORT).show();
                    chongZhi(DingDanHaoProduceHelper.getOutTradeNo(), "充值",
                            FormatHelper.getNumberFromRenMingBi(jinE), "充值");
                    //afterZhiFuBaoChongZhi();
                }
                break;


            //----------充值方式弹出窗口的点击事件-----------------------------------
            case R.id.img_close_chong_zhi_tan_chu://关闭
                chongZhiFangShiWindow.dismiss();
                break;
            case R.id.re_layout_zhi_fu_bao_chong_zhi_fang_shi_tan_chu://点击了支付宝
                String text=zhiFuBaoShouXuFeiTanChuTextView.getText().toString();
                shouXuFeiTextView.setText(text);
                zhiFuBaoCheckBox.setChecked(true);
                chongZhiFangShiWindow.dismiss();
                break;
        }
    }


    /**
     * 充值的方法
     */
    public void chongZhi(final String bianHao, final String subject, final String price, final String desc){
        StringRequest zhiFuRequest=new StringRequest(Request.Method.POST, payUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String s) {
                        MyLog.d(tag, "支付处理返回的数据是：" + s);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MyLog.d(tag,"支付处理返回的数据是："+s);
                                PayTask alipay = new PayTask(ChongZhiActivity.this);
                                String result = alipay.pay(s, true);
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
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                map.put("out_trade_no",bianHao);
                map.put("subject",subject);
                map.put("total_fee", FormatHelper.getNumberFromRenMingBi(price));
                map.put("body",desc);
                MyLog.d(tag,"编号="+bianHao);
                return map;
            }
        };
        requestQueue.add(zhiFuRequest);
    }


    /**
     * 充值方式的点击事件处理
     */
    private void chongZhiFangShiClickChuLi() {
        View chongZhiFangShiView=inflater.inflate(R.layout.chong_zhi_fang_shi_tan_chu_layout,null);
        ImageView closeChongZhiFangShiImageView= (ImageView) chongZhiFangShiView.findViewById(R.id.img_close_chong_zhi_tan_chu);
        RelativeLayout zhiFuBaoRelativeLayout= (RelativeLayout) chongZhiFangShiView.findViewById(R.id.re_layout_zhi_fu_bao_chong_zhi_fang_shi_tan_chu);
        zhiFuBaoCheckBox= (CheckBox) chongZhiFangShiView.findViewById(R.id.check_box_chong_zhi_tan_chu);
        zhiFuBaoShouXuFeiTanChuTextView= (TextView) chongZhiFangShiView.findViewById(R.id.text_zhi_fu_bao_shou_xu_fei_chong_zhi_tan_chu);//支付宝手续费

        closeChongZhiFangShiImageView.setOnClickListener(this);
        zhiFuBaoRelativeLayout.setOnClickListener(this);

        chongZhiFangShiWindow=new PopupWindow(chongZhiFangShiView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        chongZhiFangShiWindow.setBackgroundDrawable(new ColorDrawable());
        chongZhiFangShiWindow.setOutsideTouchable(true);
        chongZhiFangShiWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);//最后才是show,顺序很重要
        setBackgroundAlpha(0.5f);
        chongZhiFangShiWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
        //super.onBackPressed();
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
