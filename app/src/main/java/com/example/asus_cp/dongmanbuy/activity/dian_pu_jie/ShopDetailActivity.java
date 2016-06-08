package com.example.asus_cp.dongmanbuy.activity.dian_pu_jie;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;

/**
 * 店铺详情的界面
 * Created by asus-cp on 2016-06-08.
 */
public class ShopDetailActivity extends Activity implements View.OnClickListener{
    private ImageView logoImageView;//logo
    private TextView shopNameTextView;//店铺名字
    private TextView guanZhuRenShuTextView;//关注人数
    private TextView guanZhuTextView;//关注
    private TextView productScoreTextView;//商品分数
    private TextView fuWuScoreTextView;//服务分数
    private TextView shiXiaoScoreTextView;//时效分数
    private TextView productPingJiTextView;//商品评级
    private TextView fuWuPingJiTextView;//服务评级
    private TextView shiXiaoPingJiTextView;//时效评级
    private TextView allProductTextView;//所有商品
    private TextView newProductTextView;//新商品
    private TextView cuXiaoProductTextView;//促销商品
    private LinearLayout allProductLinearLayout;//所有商品
    private LinearLayout newProductLinearLayout;//新商品
    private LinearLayout cuXiaoProductLinearLayout;//促销商品
    private RelativeLayout keFuRelalltiveLayout;//客服
    private RelativeLayout erWeiMaRelativeLayout;//二维码
    private RelativeLayout shangJiaPhoneRelativeLayout;//商家电话
    private TextView shangJiaPhoneTextView;//商家电话
    private TextView shopDescTextView;//店铺简介
    private TextView gongSiNameTextView;//公司名字
    private TextView kaiDianTimeTextView;//开店时间
    private TextView suoZaiAreaTextView;//所在地区

    private ShopModel shopModel;

    private ImageLoadHelper helper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shop_detail_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        logoImageView= (ImageView) findViewById(R.id.img_shop_logo_shop_detail);
        shopNameTextView= (TextView) findViewById(R.id.text_shop_name_shop_detail);
        guanZhuRenShuTextView= (TextView) findViewById(R.id.text_guan_zhu_ren_shu_shop_detail);
        guanZhuTextView= (TextView) findViewById(R.id.text_guan_zhu_shop_detail);
        productScoreTextView= (TextView) findViewById(R.id.text_product_score_shop_detail);
        fuWuScoreTextView= (TextView) findViewById(R.id.text_fu_wu_score_shop_detail);
        shiXiaoScoreTextView= (TextView) findViewById(R.id.text_shi_xiao_score_shop_detail);
        productPingJiTextView= (TextView) findViewById(R.id.text_product_ping_ji_shop_detail);
        fuWuPingJiTextView= (TextView) findViewById(R.id.text_fu_wu_ping_ji_shop_detail);
        shiXiaoPingJiTextView= (TextView) findViewById(R.id.text_shi_xiao_ping_ji_shop_detail);
        allProductTextView= (TextView) findViewById(R.id.text_all_product_count_shop_detail);
        newProductTextView= (TextView) findViewById(R.id.text_new_product_count_shop_detail);
        cuXiaoProductTextView= (TextView) findViewById(R.id.text_cu_xiao_product_count_shop_detail);
        allProductLinearLayout= (LinearLayout) findViewById(R.id.ll_all_product_shop_detail);
        newProductLinearLayout= (LinearLayout) findViewById(R.id.ll_new_product_shop_detail);
        cuXiaoProductLinearLayout= (LinearLayout) findViewById(R.id.ll_cu_xiao_product_shop_detail);
        keFuRelalltiveLayout = (RelativeLayout) findViewById(R.id.re_layout_ke_fu);
        erWeiMaRelativeLayout = (RelativeLayout) findViewById(R.id.re_layout_shop_er_wei_ma);
        shangJiaPhoneRelativeLayout = (RelativeLayout) findViewById(R.id.re_layout_shang_jia_phone);
        shangJiaPhoneTextView = (TextView) findViewById(R.id.text_shang_jia_phone_shop_detail);
        shopDescTextView= (TextView) findViewById(R.id.text_shop_desc);
        gongSiNameTextView= (TextView) findViewById(R.id.text_gong_si_name);
        kaiDianTimeTextView= (TextView) findViewById(R.id.text_kai_dian_time);
        suoZaiAreaTextView= (TextView) findViewById(R.id.text_suo_zai_di_qu_shop_detail);

        shopModel=getIntent().getParcelableExtra(MyConstant.FROM_SHOP_HOME_TO_SHOP_DETAIL_KEY);
        if(shopModel!=null){
            helper=new ImageLoadHelper();
            ImageLoader imageLoader=helper.getImageLoader();
            ImageLoader.ImageListener listener=imageLoader.getImageListener(logoImageView,R.mipmap.yu_jia_zai,
                    R.mipmap.yu_jia_zai);
//            imageLoader.get(shopModel.getShopLogo(), listener, 200, 200);

            //给控件设置值
            shopNameTextView.setText(shopModel.getShopName());
            guanZhuRenShuTextView.setText("已经有"+shopModel.getGazeNumber()+"人关注");
            productScoreTextView.setText(shopModel.getCommenTrankScore());
            fuWuScoreTextView.setText(shopModel.getCommentServerScore());
            shiXiaoScoreTextView.setText(shopModel.getCommentDeliveryScore());
            productPingJiTextView.setText(shopModel.getCommenTrank());
            fuWuPingJiTextView.setText(shopModel.getCommentServer());
            shiXiaoPingJiTextView.setText(shopModel.getCommentDelivery());

            allProductTextView.setText(shopModel.getAllGoodsCount());
            newProductTextView.setText(shopModel.getNewGoodCount());
            cuXiaoProductTextView.setText(shopModel.getCuXiaoGoodCount());

            shangJiaPhoneTextView.setText(shopModel.getShopTel());
            shopDescTextView.setText(shopModel.getShopDesc());
            gongSiNameTextView.setText(shopModel.getShopName());
            kaiDianTimeTextView.setText(shopModel.getShopStartTime());
            suoZaiAreaTextView.setText(shopModel.getShopAddress());


            //给控件设置点击事件
            logoImageView.setOnClickListener(this);
            guanZhuTextView.setOnClickListener(this);
            allProductLinearLayout.setOnClickListener(this);
            newProductLinearLayout.setOnClickListener(this);
            cuXiaoProductLinearLayout.setOnClickListener(this);
            keFuRelalltiveLayout.setOnClickListener(this);
            erWeiMaRelativeLayout.setOnClickListener(this);
            shangJiaPhoneRelativeLayout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_shop_logo_shop_detail://点击了店铺logo
                Toast.makeText(this,"点击了logo",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_shop_name_shop_detail://点击了店铺名称
                Toast.makeText(this,"点击了店铺名称",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_guan_zhu_shop_detail://点击了关注
                Toast.makeText(this,"点击了关注",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_all_product_shop_detail://点击了全部商品
                Toast.makeText(this,"点击了全部商品",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_new_product_shop_detail://点击了新商品
                Toast.makeText(this,"点击了新商品",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_cu_xiao_product_shop_detail://点击了促销商品
                Toast.makeText(this,"点击了促销商品",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_ke_fu://点击了在线客服
                Toast.makeText(this,"点击了在线客服",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_shop_er_wei_ma://点击了二维码
                Toast.makeText(this,"点击了二维码",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_shang_jia_phone://点击了商家电话
                Toast.makeText(this,"点击了商家电话",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
