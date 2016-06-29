package com.example.asus_cp.dongmanbuy.activity.product_detail;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.example.asus_cp.dongmanbuy.activity.gou_wu.ShoppingCarActivity;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.db.CursorHandler;
import com.example.asus_cp.dongmanbuy.db.DBOperateHelper;
import com.example.asus_cp.dongmanbuy.fragment.HomeFragment;
import com.example.asus_cp.dongmanbuy.model.Comment;
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
    private ImageView daoHangImagView;//导航
    private ImageView productBigPicImageView;//商品的大图
    private TextView productPicCountsTextView;//商品的图片数量
    private TextView isZiYingTextView;//自营还是他营
    private TextView productNameTextView;//商品名称
    private LinearLayout shouCangLinearLayout;//收藏
    private ImageView shouCangImageView;//收藏的按钮
    private TextView shouCangTextView;//收藏的文字
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
    private TextView shoppingCarCountTextView;//购物车数量

    private RequestQueue requestQueue;

    private String productPicUrl = "http://www.zmobuy.com/PHP/index.php?url=/goods/desc";//商品大图的接口地址
    private String userCommentUrl = "http://www.zmobuy.com/PHP/index.php?url=/comments";//用户评论的接口

    private String addToShouCangUrl="http://www.zmobuy.com/PHP/index.php?url=/user/collect/create";//添加到收藏
    private String quXiaoShouCangUrl="http://www.zmobuy.com/PHP/index.php?url=/user/collect/delete";//取消收藏
    private String getShouCangListUrl="http://www.zmobuy.com/PHP/index.php?url=/user/collect/list";//获取收藏列表

    private String addToShoppingCarUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/create";//加入购物车
    private String updateShoppingCarUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/update";//更改购物车的商品数量
    private String shoppingCarListUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/list";//购物车列表
    private String deleteShoppingCarUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/delete";//删除购物车

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
    private int produtCount = 1;//选中的商品数量
    private String productChiCun;//选中的商品尺寸


    public static final int REQUEST_CODE_FOR_AREA_ACTIVTY=1;
    public static final int REQUEST_LOGIN_ACTIVITY=2;//登陆login活动时的返回码

    private int shouCangYanSeFlag=0;//收藏的颜色的标记，点击一次后变红，再点击变灰

    private int shoppingCarCount;//购物车里面的商品数量，等于选中的商品数量乘以购物车的点击次数
    private int shoppingCarClickCount;//购物车的点击次数

    private DBOperateHelper dbHelper;//数据库操作的帮助类




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
        good = getIntent().getParcelableExtra(MyConstant.GOOD_KEY);
        liuLanJiLu();//向数据库中插入数据，做浏览记录

        MyLog.d(tag, "商品id" + good.getGoodId());
        helper = new ImageLoadHelper();
        imageLoader = helper.getImageLoader();
        inflater = LayoutInflater.from(this);
        parentView = inflater.inflate(R.layout.prodcut_detail_layout, null);
        initView();

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

        //设置商品店铺价格,不带人民币符号
        MyLog.d(tag,"店铺价格为："+good.getShopPrice());
        benDianJiaGeTextView.setText(FormatHelper.getMoneyFormat(good.getShopPrice()));

        //设置商品市场价格,带人民币符号
        marketPriceTextView.setText(FormatHelper.getMoneyFormat(good.getMarket_price()));
        marketPriceTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        double shopPrice = Double.parseDouble(FormatHelper.getNumberFromRenMingBi(good.getShopPrice()));
        double marketPrice = Double.parseDouble(FormatHelper.getNumberFromRenMingBi(good.getMarket_price()));
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
                MyLog.d(tag, "评论返回的数据是" + s);
                Comment commet = new Comment();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    try{
                        JSONObject ziJsonObject = jsonArray.getJSONObject(0);
                        commet.setId(ziJsonObject.getString("id"));
                        commet.setAuthor(ziJsonObject.getString("author"));
                        commet.setContent(ziJsonObject.getString("content"));
                        commet.setCreateTime(ziJsonObject.getString("create"));
                        commet.setReContent(ziJsonObject.getString("re_content"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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
                    commentQuYu.setVisibility(View.GONE);
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
        daoHangImagView.setOnClickListener(this);
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


    /**
     * 做浏览记录时用
     */
    private void liuLanJiLu() {
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        if(uid !=null && !uid.isEmpty()){
            dbHelper=new DBOperateHelper();
            Object obj=dbHelper.queryGoodById(good.getGoodId(), new CursorHandler() {
                @Override
                public Object handleCursor(Cursor cursor) {
                    if (cursor.moveToNext()) {
                        return "have value";
                    } else {
                        return null;
                    }
                }
            });
            if(obj==null){
                dbHelper.insert(good);
            }else{
                dbHelper.delete(good);
                dbHelper.insert(good);
            }
        }
    }



    /**
     * 初始化view
     */
    private void initView() {
        //初始化view
        daoHangImagView= (ImageView) findViewById(R.id.img_dao_hang_product_detail);
        productBigPicImageView = (ImageView) findViewById(R.id.img_product_big_pic);
        productPicCountsTextView = (TextView) findViewById(R.id.text_product_pic_counts);
        isZiYingTextView = (TextView) findViewById(R.id.text_is_zi_ying);
        productNameTextView = (TextView) findViewById(R.id.text_product_name);
        shouCangLinearLayout = (LinearLayout) findViewById(R.id.ll_shou_cang);
        shouCangImageView= (ImageView) findViewById(R.id.img_shou_cang);
        shouCangTextView= (TextView) findViewById(R.id.text_shou_cang);
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
        shoppingCarCountTextView= (TextView) findViewById(R.id.text_gou_wu_che_count);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_dao_hang_product_detail:
                finish();
                break;
            case R.id.ll_shou_cang://收藏
                shouCangShiJianChuLi();
                break;
            case R.id.ll_ling_qu_you_hui_quan://领取优惠券
                youHuiQuanClickChuLi();
                break;
            case R.id.re_layout_suo_zai_di_qu://所在地区
                //Toast.makeText(this, "所在地区", Toast.LENGTH_SHORT).show();
                Intent areaIntent=new Intent(this,AreaActivity.class);
                startActivityForResult(areaIntent, REQUEST_CODE_FOR_AREA_ACTIVTY);//地区选择结束后会向本活动返回数据
                break;
            case R.id.re_layout_yi_xuan://已选
                yiXuanClickChuLi();
                break;
            case R.id.ll_fu_wu://服务
                fuWuClickChuLi();
                break;
            case R.id.re_layout_product_detail://查看商品详情
                //Toast.makeText(this, "查看商品详情", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,ProductPicAndGuiGeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelable(HomeFragment.GOOD_KEY,good);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.re_layout_user_commet://用户评价
                //Toast.makeText(this, "用户评价", Toast.LENGTH_SHORT).show();
                Intent userCommentIntent=new Intent(this,CommetnActivity.class);
                Bundle userBundle=new Bundle();
                userBundle.putParcelable(HomeFragment.GOOD_KEY,good);
                userCommentIntent.putExtras(userBundle);
                startActivity(userCommentIntent);
                break;
            case R.id.btn_you_tu_ping_jia://有图评价
                //Toast.makeText(this, "有图评价", Toast.LENGTH_SHORT).show();
                Intent youTuCommentIntent=new Intent(this,CommetnActivity.class);
                Bundle youTuBundle=new Bundle();
                youTuBundle.putParcelable(HomeFragment.GOOD_KEY,good);
                youTuCommentIntent.putExtras(youTuBundle);
                youTuCommentIntent.putExtra(MyConstant.YOU_TU_PING_JIA_KEY, MyConstant.YOU_TU_PING_JIA_CONTENT);
                startActivity(youTuCommentIntent);
                break;
            case R.id.btn_quan_bu_ping_jia://全部评价
                //Toast.makeText(this, "全部评价", Toast.LENGTH_SHORT).show();
                Intent allCommentIntent=new Intent(this,CommetnActivity.class);
                Bundle allBundle=new Bundle();
                allBundle.putParcelable(HomeFragment.GOOD_KEY,good);
                allCommentIntent.putExtras(allBundle);
                startActivity(allCommentIntent);
                break;
            case R.id.ll_lian_xi_ke_fu://联系客服
                Toast.makeText(this, "联系客服", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_ke_fu://客服
                Toast.makeText(this, "客服", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_shopping_car_product_detail://购物车
                //Toast.makeText(this, "购物车", Toast.LENGTH_SHORT).show();
                Intent shoppingCatIntent=new Intent(this,ShoppingCarActivity.class);
                shoppingCatIntent.putExtra(MyConstant.KU_CUN_KEY,good.getGoodsNumber());
                startActivity(shoppingCatIntent);
                break;
            case R.id.btn_add_to_shopping_car://加入购物车
                addToShoppingCarChuLi();
                break;
            case R.id.btn_buy_at_once://立即购买
                //Toast.makeText(this, "立即购买", Toast.LENGTH_SHORT).show();
                yiXuanClickChuLi();
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
                yiXuanProductTextView.setText(productChiCun+","+produtCount+"个");
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
                addToShoppingCarChuLi();
                Toast.makeText(this, "已加入购物车", Toast.LENGTH_SHORT).show();
                yiXuanWindow.dismiss();
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
     * 加入购物车的点击事件处理
     */
    private void addToShoppingCarChuLi() {
        //Toast.makeText(this, "加入购物车", Toast.LENGTH_SHORT).show();
        int kuCun=Integer.parseInt(good.getGoodsNumber());
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, MODE_APPEND);
        final String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        final String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        if(uid!=null && !uid.isEmpty()){
            if(shoppingCarCount<kuCun){
                shoppingCarClickCount++;
                shoppingCarCount=shoppingCarCount+produtCount;
                shoppingCarCountTextView.setText(shoppingCarCount+"");
                if(shoppingCarClickCount==1){//加入购物车
                    StringRequest addToShoppingCarRequest=new StringRequest(Request.Method.POST
                            , addToShoppingCarUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag,"加入购物车返回的数据:"+s);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<String,String>();
                            String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"goods_id\":\""+good.getGoodId()+"\",\"number\":\""+produtCount+"\",\"spec\":\"\"}";
                            map.put("json",json);
                            return map;
                        }
                    };
                    requestQueue.add(addToShoppingCarRequest);
                }else if(shoppingCarCount>1){//更改购物车的商品数量
                    //获取购物车的商品数量
                    StringRequest getProductListRequest=new StringRequest(Request.Method.POST, shoppingCarListUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    MyLog.d(tag,"购物车列表的数据:"+s);
                                    String recId=null;
                                    try {
                                        JSONObject jsonObject=new JSONObject(s);
                                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                        JSONArray jsonArray=jsonObject1.getJSONArray("goods_list");
                                        for(int i=0;i<jsonArray.length();i++){
                                            JSONObject ziJsObj=jsonArray.getJSONObject(i);
                                            String goodId=ziJsObj.getString("goods_id");
                                            if(good.getGoodId().equals(goodId)){
                                                recId=ziJsObj.getString("rec_id");
                                                break;
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //更改商品数量
                                    final String finalRecId = recId;
                                    StringRequest upDateShoppingCarCountRequest=new StringRequest(Request.Method.POST, updateShoppingCarUrl,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    MyLog.d(tag,"更改商品数量返回的数据："+s);
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {

                                        }
                                    }){
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String,String> map=new HashMap<String,String>();
                                            String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"rec_id\":\""+ finalRecId +"\",\"new_number\":\""+shoppingCarCount+"\"}";
                                            map.put("json",json);
                                            return map;
                                        }
                                    };
                                    requestQueue.add(upDateShoppingCarCountRequest);
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
                    requestQueue.add(getProductListRequest);
                }
            }else{
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("");
                builder.setMessage("对不起，该商品已经库存不足暂停销售。你现在要进行缺货登记来预定该商品吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(uid!=null && !uid.isEmpty()){
                            Intent intent=new Intent(ProductDetailActivity.this,QueHuoDengJiActivity.class);
                            startActivity(intent);
                        }else{
                            Intent queHuoIntent=new Intent(ProductDetailActivity.this,LoginActivity.class);
                            queHuoIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"queHuo");
                            startActivity(queHuoIntent);
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog=builder.show();
                alertDialog.show();
            }
        }else{//用户未登录，跳转到登陆界面
            Intent intent=new Intent(this,LoginActivity.class);
            intent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"shoppingCar");
            startActivity(intent);
        }
    }

    /**
     * 收藏的点击事件处理
     */
    private void shouCangShiJianChuLi() {
        //Toast.makeText(this, "点击了收藏", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,
                MODE_APPEND);
        final String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        final String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        if(uid!=null && !uid.isEmpty()){//有值
            if(shouCangYanSeFlag%2==1){//注意这里为什么是1，因为这里加了2次，本来点就要加一次，登陆之后又加一次
                shouCangImageView.setImageResource(R.mipmap.like_selected);
                shouCangTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                StringRequest addToShouCangRequest=new StringRequest(Request.Method.POST, addToShouCangUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                MyLog.d(tag, "添加到收藏返回的数据" + s);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map=new HashMap<String,String>();
                        String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"goods_id\":\""+good.getGoodId()+"\"}";
                        map.put("json",json);
                        return map;
                    }
                };
                requestQueue.add(addToShouCangRequest);
            }else{
                shouCangImageView.setImageResource(R.mipmap.like);
                shouCangTextView.setTextColor(getResources().getColor(R.color.black));

                //获取收藏列表
                StringRequest getListRequest=new StringRequest(Request.Method.POST, getShouCangListUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                MyLog.d(tag,"收藏列表的数据"+s);
                                try {
                                    String recId=null;
                                    JSONObject jsonObject=new JSONObject(s);
                                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject ziJsonObj=jsonArray.getJSONObject(i);
                                        String goodIdJ=ziJsonObj.getString("goods_id");
                                        if(goodIdJ.equals(good.getGoodId())){
                                            recId=ziJsonObj.getString("rec_id");
                                            break;
                                        }
                                    }
                                    //取消收藏,取消收藏之前必须先获取列表
                                    final String finalRecId = recId;
                                    StringRequest quXiaoShouCangRequest=new StringRequest(Request.Method.POST, quXiaoShouCangUrl,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    MyLog.d(tag,"取消收藏返回的数据"+s);
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                        }
                                    }){
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String,String> map=new HashMap<String,String>();
                                            String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"rec_id\":\""+ finalRecId +"\"}";
                                            map.put("json",json);
                                            return map;
                                        }
                                    };
                                    requestQueue.add(quXiaoShouCangRequest);

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
                        String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"pagination\":{\"page\":\"1\",\"count\":\"100\"}}";
                        map.put("json",json);
                        return map;
                    }
                };
                requestQueue.add(getListRequest);
            }
        }else{
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle(R.string.please_login_shou_cang);
            builder.setPositiveButton("立即登陆", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent toLoginActivity=new Intent(ProductDetailActivity.this, LoginActivity.class);
                    toLoginActivity.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY, "shouCang");
                    startActivityForResult(toLoginActivity, REQUEST_LOGIN_ACTIVITY);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog shouCangDialog=builder.show();
            shouCangDialog.show();
        }
        shouCangYanSeFlag++;
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
               // Toast.makeText(ProductDetailActivity.this, "选中的尺码" + text, Toast.LENGTH_SHORT).show();
                productChiCun=text;
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

    /**
     * 接受从别的活动返回的数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_FOR_AREA_ACTIVTY://地址活动返回的数据
                if(resultCode==RESULT_OK){
                    String shiMing=data.getStringExtra(AreaActivity.SHI_MING_KEY);
                    String xianMing=data.getStringExtra(AreaActivity.XIAN_MING_KEY);
                    MyLog.d(tag,"市民"+shiMing);
                    MyLog.d(tag,"县名"+xianMing);
                    suoZaiDiQuTextView.setText(shiMing+" "+xianMing);
                }
                break;
            case REQUEST_LOGIN_ACTIVITY://登陆活动返回的数据
                SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,
                        MODE_APPEND);
                final String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
                final String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
                shouCangImageView.setImageResource(R.mipmap.like_selected);
                shouCangTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                shouCangYanSeFlag++;

                    StringRequest addToShouCangRequest=new StringRequest(Request.Method.POST, addToShouCangUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    MyLog.d(tag,"登陆到收藏返回的数据"+s);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<String,String>();
                            String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"goods_id\":\""+good.getGoodId()+"\"}";
                            map.put("json",json);
                            return map;
                        }
                    };
                    requestQueue.add(addToShouCangRequest);
        }

    }
}
