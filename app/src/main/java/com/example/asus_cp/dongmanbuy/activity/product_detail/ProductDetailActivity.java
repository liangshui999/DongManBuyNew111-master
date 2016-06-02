package com.example.asus_cp.dongmanbuy.activity.product_detail;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.asus_cp.dongmanbuy.fragment.HomeFragment;
import com.example.asus_cp.dongmanbuy.model.Commet;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品详情
 * Created by asus-cp on 2016-06-01.
 */
public class ProductDetailActivity extends Activity implements View.OnClickListener {
    private ImageView productBigPicImageView;//商品的大图
    private TextView productPicCountsTextView;//商品的图片数量
    private TextView isZiYingTextView;//自营还是他营
    private TextView productNameTextView;//商品名称
    private LinearLayout shouCangLinearLayout;//收藏
    private TextView benDianJiaGeTextView;//本店价格
    private TextView zheKouTextView;//折扣
    private TextView marketPriceTextView;//市场价格
    private TextView xiaoLiangTextView;//销量
    private TextView kuCunTextView;//库存
    private LinearLayout lingQuYouHuiQuanLinearLayout;//领取优惠券
    private TextView youHuiQuanCountsTextView;//优惠券数量
    private TextView youHuiQuanOneTextView;//优惠券1
    private TextView youHuiQuanTwoTextView;//优惠券2
    private RelativeLayout suoZaiDiQuLayout;//所在地区
    private TextView suoZaiDiQuTextView;//所在地区
    private TextView yunFeiTextView;//运费信息
    private RelativeLayout yiXuanRelaytiveLayout;//已选
    private TextView yiXuanProductTextView;//已选商品
    private LinearLayout fuWuLineatLayout;//服务
    private RelativeLayout seeProductDetailReLativeLayout;//查看商品详情
    private RelativeLayout userCommentReLativeLayout;//用户评论
    private TextView haoPingLvTextView;//好评率
    private TextView pingLunShuTextView;//评论数
    private ImageView xingOneImageView;//星一
    private ImageView xingTwoImageView;//星二
    private ImageView xingThreeImageView;//星三
    private ImageView xingFourImageView;//星四
    private ImageView xingFiveImageView;//星五
    private TextView songXingPepleName;//送星人的名字
    private TextView commentTimeTextView;//评论时间
    private TextView commentContentTextView;//评论内容
    private Button youTuPingJiaButton;//有图评价的按钮
    private Button quanBuPingJiaButton;//全部评价的按钮
    private LinearLayout lianXiKeFuLinearLayout;//联系客服
    private RecyclerView baoKuanXinPinRecyclerView;//爆款新品
    private LinearLayout commentQuYu;//评论的内容区域，可能没有

    //固定栏
    private LinearLayout keFuLinearLayout;//客服
    private LinearLayout shoppingCarLinearLayout;//购物车
    private Button addToShoppingCarButton;//加入购物车
    private Button buyAtOnceButton;//立即购买

    private RequestQueue requestQueue;

    private String productPicUrl = "http://www.zmobuy.com/PHP/index.php?url=/goods/desc";//商品大图的接口地址
    private String userCommentUrl = "http://www.zmobuy.com/PHP/index.php?url=/comments";//用户评论的接口

    private Good good;//其他活动传进来的商品

    private String tag = "ProductDetailActivity";

    private ImageLoader imageLoader;

    private LayoutInflater inflater;

    private View parentView;//所有popu的父布局

    private PopupWindow youHuiQuanWindow;//优惠券的弹出窗口

    private PopupWindow yiXuanWindow;//已选的弹出窗口

    private PopupWindow fuWuWindow;//服务的弹出窗口

    private ImageLoadHelper helper;

    private TextView yiXuanProductCount;//已选商品加减数量
    private int produtCount = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prodcut_detail_layout);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        requestQueue = MyApplication.getRequestQueue();
        good = getIntent().getParcelableExtra(HomeFragment.GOOD_KEY);
        MyLog.d(tag, "商品id" + good.getGoodId());
        helper = new ImageLoadHelper();
        imageLoader = helper.getImageLoader();
        inflater = LayoutInflater.from(this);
        parentView = inflater.inflate(R.layout.prodcut_detail_layout, null);
        //初始化view
        productBigPicImageView = (ImageView) findViewById(R.id.img_product_big_pic);
        productPicCountsTextView = (TextView) findViewById(R.id.text_product_pic_counts);
        isZiYingTextView = (TextView) findViewById(R.id.text_is_zi_ying);
        productNameTextView = (TextView) findViewById(R.id.text_product_name);
        shouCangLinearLayout = (LinearLayout) findViewById(R.id.ll_shou_cang);
        benDianJiaGeTextView = (TextView) findViewById(R.id.text_ben_zhan_price);
        zheKouTextView = (TextView) findViewById(R.id.text_zhe_kou);
        marketPriceTextView = (TextView) findViewById(R.id.text_market_price);
        xiaoLiangTextView = (TextView) findViewById(R.id.text_xiao_liang);
        kuCunTextView = (TextView) findViewById(R.id.text_ku_cun);
        lingQuYouHuiQuanLinearLayout = (LinearLayout) findViewById(R.id.ll_ling_qu_you_hui_quan);
        youHuiQuanCountsTextView = (TextView) findViewById(R.id.text_you_hui_quan_counts);
        youHuiQuanOneTextView = (TextView) findViewById(R.id.text_you_hui_quan_one);
        youHuiQuanTwoTextView = (TextView) findViewById(R.id.text_you_hui_quan_two);
        suoZaiDiQuLayout = (RelativeLayout) findViewById(R.id.re_layout_suo_zai_di_qu);
        suoZaiDiQuTextView = (TextView) findViewById(R.id.text_suo_zai_di_qu);
        yunFeiTextView = (TextView) findViewById(R.id.text_yun_fei);
        yiXuanRelaytiveLayout = (RelativeLayout) findViewById(R.id.re_layout_yi_xuan);
        yiXuanProductTextView = (TextView) findViewById(R.id.text_yi_xuan_product);
        fuWuLineatLayout = (LinearLayout) findViewById(R.id.ll_fu_wu);
        seeProductDetailReLativeLayout = (RelativeLayout) findViewById(R.id.re_layout_product_detail);
        userCommentReLativeLayout = (RelativeLayout) findViewById(R.id.re_layout_user_commet);
        haoPingLvTextView = (TextView) findViewById(R.id.text_hao_ping_lv);
        pingLunShuTextView = (TextView) findViewById(R.id.text_comment_counts);
        xingOneImageView = (ImageView) findViewById(R.id.img_xing_one);
        xingTwoImageView = (ImageView) findViewById(R.id.img_xing_two);
        xingThreeImageView = (ImageView) findViewById(R.id.img_xing_three);
        xingFourImageView = (ImageView) findViewById(R.id.img_xing_four);
        xingFiveImageView = (ImageView) findViewById(R.id.img_xing_five);
        songXingPepleName = (TextView) findViewById(R.id.text_song_xing_people_name);
        commentTimeTextView = (TextView) findViewById(R.id.text_comment_time);
        commentContentTextView = (TextView) findViewById(R.id.text_commet_content);
        youTuPingJiaButton = (Button) findViewById(R.id.btn_you_tu_ping_jia);
        quanBuPingJiaButton = (Button) findViewById(R.id.btn_quan_bu_ping_jia);
        lianXiKeFuLinearLayout = (LinearLayout) findViewById(R.id.ll_lian_xi_ke_fu);
        baoKuanXinPinRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_bao_kuan_xin_pin);
        commentQuYu = (LinearLayout) findViewById(R.id.ll_comment_content);

        keFuLinearLayout = (LinearLayout) findViewById(R.id.ll_ke_fu);
        shoppingCarLinearLayout = (LinearLayout) findViewById(R.id.ll_shopping_car_product_detail);
        addToShoppingCarButton = (Button) findViewById(R.id.btn_add_to_shopping_car);
        buyAtOnceButton = (Button) findViewById(R.id.btn_buy_at_once);

        //给爆款新品设置布局管理器和适配器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        baoKuanXinPinRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器(暂时没有接口适配器已经写好了)

        //设置商品图片
        ImageLoader.ImageListener listener = imageLoader.getImageListener(productBigPicImageView, R.mipmap.yu_jia_zai,
                R.mipmap.yu_jia_zai);
        imageLoader.get(good.getGoodsImg(), listener, 300, 300);

        //设置商品名称
        productNameTextView.setText(good.getGoodName());

        //设置商品店铺价格,自带人民币符号
        benDianJiaGeTextView.setText(good.getShopPrice());

        //设置商品市场价格,不带人民币符号
        marketPriceTextView.setText(FormatHelper.getMoneyFormat(good.getMarket_price()));
        marketPriceTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        double shopPrice = Double.parseDouble(FormatHelper.getNumberFromRenMingBi(good.getShopPrice()));
        double marketPrice = Double.parseDouble(good.getMarket_price());
        double zheKou = shopPrice / marketPrice * 10;

        //设置商品折扣
        zheKouTextView.setText(FormatHelper.getOneXiaoShuFormat("" + zheKou) + "折");

        //设置商品销量
        xiaoLiangTextView.setText(good.getSalesVolume());

        //设置商品库存
        kuCunTextView.setText(good.getGoodsNumber());

        //设置用户评价的内容
        StringRequest userCommetRequest = new StringRequest(Request.Method.POST, userCommentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag, "返回的数据是" + s);
                Commet commet = new Commet();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject ziJsonObject = jsonArray.getJSONObject(0);
                    commet.setId(ziJsonObject.getString("id"));
                    commet.setAuthor(ziJsonObject.getString("author"));
                    commet.setContent(ziJsonObject.getString("content"));
                    commet.setCreateTime(ziJsonObject.getString("create"));
                    commet.setReContent(ziJsonObject.getString("re_content"));

                    JSONObject countJsonobj = jsonObject.getJSONObject("paginated");
                    String count = countJsonobj.getString("total");
                    int counts = Integer.parseInt(count);
                    if (counts > 0) {//有评论就让评论区域显示
                        commentQuYu.setVisibility(View.VISIBLE);
                        //设置评论数
                        pingLunShuTextView.setText(count);

                        //设置评论人的名字
                        songXingPepleName.setText(commet.getAuthor());

                        //设置评论的时间
                        commentTimeTextView.setText(FormatHelper.getDate(commet.getCreateTime()));

                        //设置评论的内容
                        commentContentTextView.setText(commet.getContent());

                    } else {//让评论区域消失
                        commentQuYu.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //json={"goods_id":"10","pagination":{"page":"1","count":"1"}}
                Map<String, String> map = new HashMap<String, String>();
                map.put("json", "{\"goods_id\":\"" + good.getGoodId() + "\",\"pagination\":{\"page\":\"1\",\"count\":\"1\"}}");
                return map;
            }
        };
        requestQueue.add(userCommetRequest);


        //给view设置点击事件
        shouCangLinearLayout.setOnClickListener(this);
        lingQuYouHuiQuanLinearLayout.setOnClickListener(this);
        suoZaiDiQuLayout.setOnClickListener(this);
        yiXuanRelaytiveLayout.setOnClickListener(this);
        fuWuLineatLayout.setOnClickListener(this);
        seeProductDetailReLativeLayout.setOnClickListener(this);
        userCommentReLativeLayout.setOnClickListener(this);
        youTuPingJiaButton.setOnClickListener(this);
        quanBuPingJiaButton.setOnClickListener(this);
        lianXiKeFuLinearLayout.setOnClickListener(this);

        keFuLinearLayout.setOnClickListener(this);
        shoppingCarLinearLayout.setOnClickListener(this);
        addToShoppingCarButton.setOnClickListener(this);
        buyAtOnceButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shou_cang://收藏
                Toast.makeText(this, "点击了收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_ling_qu_you_hui_quan://领取优惠券
                youHuiQuanClickChuLi();
                break;
            case R.id.re_layout_suo_zai_di_qu://所在地区
                Toast.makeText(this, "所在地区", Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_yi_xuan://已选
                yiXuanClickChuLi();
                break;
            case R.id.ll_fu_wu://服务
                fuWuClickChuLi();
                break;
            case R.id.re_layout_product_detail://查看商品详情
                Toast.makeText(this, "查看商品详情", Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_layout_user_commet://用户评价
                Toast.makeText(this, "用户评价", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_you_tu_ping_jia://有图评价
                Toast.makeText(this, "有图评价", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_quan_bu_ping_jia://全部评价
                Toast.makeText(this, "全部评价", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_lian_xi_ke_fu://联系客服
                Toast.makeText(this, "联系客服", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_ke_fu://客服
                Toast.makeText(this, "客服", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_shopping_car_product_detail://购物车
                Toast.makeText(this, "购物车", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_add_to_shopping_car://加入购物车
                Toast.makeText(this, "加入购物车", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_buy_at_once://立即购买
                Toast.makeText(this, "立即购买", Toast.LENGTH_SHORT).show();
                break;


            //---------------------------------优惠券的弹出窗口的点击事件---------------------------
            case R.id.img_close_ling_qu_you_hui_quan:
                //Toast.makeText(this,"关闭优惠券",Toast.LENGTH_SHORT).show();
                youHuiQuanWindow.dismiss();
                break;
            case R.id.ll_li_ji_ling_qu_98:
                Toast.makeText(this, "立即领取98", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_li_ji_ling_qu_21:
                Toast.makeText(this, "立即领取21", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_li_ji_ling_qu_1:
                Toast.makeText(this, "立即领取1", Toast.LENGTH_SHORT).show();
                break;


            //-------------------已选的弹出窗口的点击事件------------------------------------------
            case R.id.img_yi_xuan_close://关闭已选
                yiXuanWindow.dismiss();
                break;
            case R.id.img_yi_xuan_jian_shang_pin://减去商品数量
                produtCount = produtCount - 1;
                if (produtCount >= 1) {
                    yiXuanProductCount.setText(produtCount + "");
                } else {
                    produtCount = 1;
                    yiXuanProductCount.setText("" + produtCount);
                }
                break;
            case R.id.img_yi_xuan_jia_shang_pin://加商品数量
                produtCount = produtCount + 1;
                if (produtCount > Integer.parseInt(good.getGoodsNumber())) {
                    produtCount = Integer.parseInt(good.getGoodsNumber());
                }
                yiXuanProductCount.setText(produtCount + "");
                break;
            case R.id.btn_yi_xuan_add_to_shopping_car://加入购物车
                Toast.makeText(this, "点击了已选的加入购物车", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_yi_xuan_buy_at_once://立即购买
                Toast.makeText(this, "点击了已选的立即购买", Toast.LENGTH_SHORT).show();
                break;

            //-----------服务弹出窗口的点击事件------------------------------------------
            case R.id.img_fu_wu_close://关闭服务窗口
                fuWuWindow.dismiss();
                break;
        }
    }

    /**
     * 服务的事件处理
     */
    private void fuWuClickChuLi() {
        //Toast.makeText(this, "服务", Toast.LENGTH_SHORT).show();
        View fuWuView=inflater.inflate(R.layout.fu_wu_layout,null);
        ImageView closeFuwu= (ImageView) fuWuView.findViewById(R.id.img_fu_wu_close);
        closeFuwu.setOnClickListener(this);
        fuWuWindow=new PopupWindow(fuWuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        fuWuWindow.setBackgroundDrawable(new ColorDrawable());
        fuWuWindow.setOutsideTouchable(true);
        fuWuWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);//最后才是show,顺序很重要
        setBackgroundAlpha(0.5f);
        fuWuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
    }


    /**
     * 已选的点击事件处理
     */
    private void yiXuanClickChuLi() {
        //Toast.makeText(this,"已选",Toast.LENGTH_SHORT).show();
        final View yiXuanView = inflater.inflate(R.layout.yi_xuan_layout, null);
        ImageView yiXuanPic = (ImageView) yiXuanView.findViewById(R.id.img_yi_xuan_product_pic);
        ImageView yiXuanClose = (ImageView) yiXuanView.findViewById(R.id.img_yi_xuan_close);
        TextView yiXuanProductname = (TextView) yiXuanView.findViewById(R.id.text_yi_xuan_productName);
        TextView yiXuanPrice = (TextView) yiXuanView.findViewById(R.id.text_yi_xuan_price);
        TextView yiXuanKuCun = (TextView) yiXuanView.findViewById(R.id.text_yi_xuan_ku_cun);
        ImageView yiXuanJian = (ImageView) yiXuanView.findViewById(R.id.img_yi_xuan_jian_shang_pin);
        ImageView yiXuanJia = (ImageView) yiXuanView.findViewById(R.id.img_yi_xuan_jia_shang_pin);
        yiXuanProductCount = (TextView) yiXuanView.findViewById(R.id.text_yi_xuan_jia_jian_shang_pin_count);
        Button yiXuanAddToShoppingCar = (Button) yiXuanView.findViewById(R.id.btn_yi_xuan_add_to_shopping_car);
        Button yiXuanBuyAtOnce = (Button) yiXuanView.findViewById(R.id.btn_yi_xuan_buy_at_once);
        RadioGroup yiXuanChiMa = (RadioGroup) yiXuanView.findViewById(R.id.radio_group_yi_xuan_chi_ma);

        yiXuanChiMa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) yiXuanView.findViewById(group.getCheckedRadioButtonId());
                String text = radioButton.getText().toString();
                MyLog.d(tag, "选中的尺码为：" + text);
                Toast.makeText(ProductDetailActivity.this, "选中的尺码" + text, Toast.LENGTH_SHORT).show();
            }
        });

        //给已选控件赋值
        ImageLoader yiXuanImageLoader = helper.getImageLoader();
        ImageLoader.ImageListener yiXuanListener = yiXuanImageLoader.getImageListener(yiXuanPic,
                R.mipmap.yu_jia_zai, R.mipmap.yu_jia_zai);
        yiXuanImageLoader.get(good.getGoodsImg(), yiXuanListener, 200, 200);

        yiXuanProductname.setText(good.getGoodName());
        yiXuanPrice.setText(good.getShopPrice());
        yiXuanKuCun.setText(good.getGoodsNumber());
        yiXuanProductCount.setText("1");

        //给已选的控件设置点击事件
        yiXuanClose.setOnClickListener(this);
        yiXuanJian.setOnClickListener(this);
        yiXuanJia.setOnClickListener(this);
        yiXuanAddToShoppingCar.setOnClickListener(this);
        yiXuanBuyAtOnce.setOnClickListener(this);

        yiXuanWindow = new PopupWindow(yiXuanView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        yiXuanWindow.setBackgroundDrawable(new ColorDrawable());
        yiXuanWindow.setOutsideTouchable(true);
        yiXuanWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);//最后才是show,顺序很重要
        setBackgroundAlpha(0.5f);
        yiXuanWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
    }

    /**
     * 优惠券的点击事件处理
     */
    private void youHuiQuanClickChuLi() {
        //Toast.makeText(this,"点击了优惠券",Toast.LENGTH_SHORT).show();
        View youHuiQuanView = inflater.inflate(R.layout.you_hui_quan_layout, null);
        ImageView closeYouHuiQuanImagView = (ImageView) youHuiQuanView.findViewById
                (R.id.img_close_ling_qu_you_hui_quan);
        LinearLayout liJiLingQu98 = (LinearLayout) youHuiQuanView.findViewById(R.id.ll_li_ji_ling_qu_98);
        LinearLayout liJiLingQu21 = (LinearLayout) youHuiQuanView.findViewById(R.id.ll_li_ji_ling_qu_21);
        LinearLayout liJiLingQu1 = (LinearLayout) youHuiQuanView.findViewById(R.id.ll_li_ji_ling_qu_1);
        closeYouHuiQuanImagView.setOnClickListener(this);
        liJiLingQu98.setOnClickListener(this);
        liJiLingQu21.setOnClickListener(this);
        liJiLingQu1.setOnClickListener(this);

        youHuiQuanWindow = new PopupWindow(youHuiQuanView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        youHuiQuanWindow.setBackgroundDrawable(new ColorDrawable());
        youHuiQuanWindow.setOutsideTouchable(true);
        youHuiQuanWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);//最后才是show,顺序很重要
        setBackgroundAlpha(0.5f);
        youHuiQuanWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
}
