package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Intent;
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

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.UserModel;

/**
 * 订单的界面
 * Created by asus-cp on 2016-06-15.
 */
public class DingDanActivity extends Activity implements View.OnClickListener{
    private RelativeLayout shouHuoAddressRelativeLayout;//收货地址
    private TextView peopleNameTextView;//人名
    private TextView phoneTextView;//电话
    private TextView addressTextView;//收货地址
    private ImageView firstPicImageView;//第一个商品的图片
    private ImageView secondPicImageView;//第二个商品的图片
    private ImageView threePicImageView;//第三个商品的图片
    private LinearLayout displayAllProductLineatLayout;//展示所有商品
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
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        shouHuoAddressRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_shou_huo_address_ding_dan);
        peopleNameTextView= (TextView) findViewById(R.id.text_shou_huo_ren_name_ding_dan);
        phoneTextView= (TextView) findViewById(R.id.text_shou_huo_ren_phone_ding_dan);
        addressTextView= (TextView) findViewById(R.id.text_shou_huo_address_ding_dan);
        firstPicImageView= (ImageView) findViewById(R.id.img_one_ding_dan);
        secondPicImageView= (ImageView) findViewById(R.id.img_two_ding_dan);
        threePicImageView= (ImageView) findViewById(R.id.img_three_ding_dan);
        displayAllProductLineatLayout= (LinearLayout) findViewById(R.id.ll_dispaly_all_product_ding_dan);
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
