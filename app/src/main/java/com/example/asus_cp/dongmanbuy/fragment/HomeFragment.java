package com.example.asus_cp.dongmanbuy.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.adapter.CaiNiXiHuanAdapter;
import com.example.asus_cp.dongmanbuy.adapter.JingPinAdapter;
import com.example.asus_cp.dongmanbuy.adapter.XianShiAdapter;
import com.example.asus_cp.dongmanbuy.customview.MyGridView;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.model.Binner;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页的展示界面
 * Created by asus-cp on 2016-05-19.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private String tag="HomeFragment";
    //第一个广告条
    private ViewPager firstViewPager;
    private LinearLayout firstPointGroup;
    public static final int SCROLL__FIRST_BINNER =1;//自动滚动消息的what
    public static final int REFRESH_FIRST_BINNER = 2;//更新第一个位置的广告的
    private static final int FIRST_BINNER_FLAG =3 ;//第一幅广告的标记
    private List<View> firstImageViews;//装载imageview的集合

    //第二个广告条
    private ViewPager secondViewPager;
    private LinearLayout secondPointGroup;
    public static final int SCROLL__SECOND_BINNER =4;//自动滚动消息的what
    public static final int REFRESH_SECOND_BINNER = 5;//更新第一个位置的广告的
    private static final int SECOND_BINNER_FLAG =6 ;//第二幅广告的标记
    private List<View> secondImageViews;//装载imageview的集合

    //第三个广告条
    private ViewPager threeViewPager;
    private LinearLayout threePointGroup;
    public static final int SCROLL__THREE_BINNER =7;//自动滚动消息的what
    public static final int REFRESH_THREE_BINNER = 8;//更新第一个位置的广告的
    private static final int THREE_BINNER_FLAG =9 ;//第三幅广告的标记
    private List<View> threeImageViews;//装载imageview的集合



    private int[] imageIds={R.drawable.guanggao1,R.drawable.guanggao2,R.drawable.guanggao3};//装imagview图片id的数组
    private Context context;
    private View v;//fragment的布局
    protected int lastPosition;//上一个页面的位置
    private boolean isRunning = true;//判断是否自动滚动


    //我的钱包，我的订单等8个按钮
    private LinearLayout myWalletll;
    private LinearLayout myOrderll;
    private LinearLayout browseHistoryll;
    private LinearLayout shipAddressll;
    private LinearLayout xianShiTeYoull;
    private LinearLayout helpll;
    private LinearLayout brandPagell;
    private LinearLayout youHuiHuoDongll;

    //限时秒杀的gridview
    private MyGridView xianShiMiaoShaGridView;
    private ImageView xianShiMiaoShaImagView;
    public static final int REFRESH_XIAN_SHI_MIAO_SHA=10;

    //精品推荐的gridview
    private MyGridView jingPinTuiJianGridview;

    //猜你喜欢的gridview
    private MyGridViewA caiNiXiHuanGridView;

    //更多按钮
    private TextView xianShiMoreTextView;
    private TextView jingPinMoreTextView;
    private TextView dianPuJieMoreTextView;

    private JsonHelper jsonHelper;//json解析的帮助类

    private RequestQueue requestQueue;//请求队列


    private Handler handler = new MyHandler();

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SCROLL__FIRST_BINNER:
                    //让viewPager 滑动到下一页
                    if(firstImageViews.size()>1){
                        firstViewPager.setCurrentItem(firstViewPager.getCurrentItem() + 1);
                        handler.sendEmptyMessageDelayed(SCROLL__FIRST_BINNER, 2000);
                    }
                    //Log.d(tag, "接收定时消息");
                    break;
                case REFRESH_FIRST_BINNER://更新首页第一个广告位置的广告
                    //firstPointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_first);
                    //指示点的初始化
                    initPoint(firstPointGroup,firstImageViews.size());
                    firstViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - 1);
                    firstViewPager.setAdapter(new MyPagerAdapter(firstImageViews));
                    break;
                case SCROLL__SECOND_BINNER:
                    //让viewPager 滑动到下一页
                    if(secondImageViews.size()>1){
                        secondViewPager.setCurrentItem(secondViewPager.getCurrentItem()+1);
                        handler.sendEmptyMessageDelayed(SCROLL__SECOND_BINNER, 2000);
                    }

                    //Log.d(tag, "接收定时消息");
                    break;
                case REFRESH_SECOND_BINNER://更新首页第二个广告位置的广告
                    //secondPointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_second);
                    //指示点的初始化
                    initPoint(secondPointGroup,secondImageViews.size());
                    secondViewPager.setCurrentItem(0);
                    secondViewPager.setAdapter(new MyPagerAdapter(secondImageViews));
                    break;
                case SCROLL__THREE_BINNER:
                    //让viewPager 滑动到下一页
                    if(threeImageViews.size()>1){
                        threeViewPager.setCurrentItem(threeViewPager.getCurrentItem()+1);
                        handler.sendEmptyMessageDelayed(SCROLL__THREE_BINNER, 2000);
                    }
                    //Log.d(tag, "接收定时消息");
                    break;
                case REFRESH_THREE_BINNER://更新首页第三个广告位置的广告
                    //threePointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_three);
                    //指示点的初始化
                    initPoint(threePointGroup,threeImageViews.size());
                    threeViewPager.setCurrentItem(0);
                    threeViewPager.setAdapter(new MyPagerAdapter(threeImageViews));
                    break;
                case REFRESH_XIAN_SHI_MIAO_SHA://更新限时秒杀
                    Bitmap bitmap= (Bitmap) msg.obj;
                    xianShiMiaoShaImagView.setImageBitmap(bitmap);
                    break;
            }
        }
    }

    /**
     * 初始化指示点
     * @param pointGroup 装指示点的layout
     * @param size 指示点的数量
     */
    private void initPoint(LinearLayout pointGroup,int size) {
        for(int i=0;i< size;i++){
            //添加指示点
            ImageView point =new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 30;
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.point);
            if(i==0){
                point.setEnabled(true);
            }else{
                point.setEnabled(false);
            }
            pointGroup.addView(point);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        v=inflater.inflate(R.layout.home_fragment_layout,null);
        initView();
        return v;
    }

    /**
     * 初始化view
     */
    private void initView() {
        requestQueue= MyApplication.getRequestQueue();
        firstPointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_first);
        secondPointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_second);
        threePointGroup = (LinearLayout) v.findViewById(R.id.ll_point_group_three);

        //-----------------第一个广告的初始化动作-----------------------------------
        firstViewPager = (ViewPager) v.findViewById(R.id.viewpager_binner_first);
        firstImageViews =new ArrayList<View>();
        String binnerUrl="http://www.zmobuy.com/PHP/index.php?url=/home/data";
        StringRequest binnerOneRequest=new StringRequest(Request.Method.GET, binnerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                getBinnerImageFromIntenet(s,FIRST_BINNER_FLAG,firstImageViews,REFRESH_FIRST_BINNER,SCROLL__FIRST_BINNER);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(binnerOneRequest);//加入到队列
        firstViewPager.addOnPageChangeListener(new MyPageChangeListener(firstImageViews, firstPointGroup));
        firstViewPager.setOnTouchListener(new MyPageTouchListener(SCROLL__FIRST_BINNER,firstViewPager));


        //--------------第二个广告的初始化----------------------------------------------

        secondViewPager = (ViewPager) v.findViewById(R.id.viewpager_binner_second);
        secondImageViews =new ArrayList<View>();
        String secondBinnerUrl="http://www.zmobuy.com/PHP/index.php?url=/home/data";
        StringRequest binnerSecondRequest=new StringRequest(Request.Method.GET, binnerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                getBinnerImageFromIntenet(s,SECOND_BINNER_FLAG,secondImageViews,REFRESH_SECOND_BINNER,SCROLL__SECOND_BINNER);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(binnerSecondRequest);//加入到队列
        secondViewPager.addOnPageChangeListener(new MyPageChangeListener(secondImageViews, secondPointGroup));
        secondViewPager.setOnTouchListener(new MyPageTouchListener(SCROLL__SECOND_BINNER,secondViewPager));


        //---------------------------------第三个广告的初始化-------------------------------------
        threeViewPager = (ViewPager) v.findViewById(R.id.viewpager_binner_three);
        threeImageViews =new ArrayList<View>();
        String threebinnerUrl="http://www.zmobuy.com/PHP/index.php?url=/home/data";
        StringRequest binnerThreeRequest=new StringRequest(Request.Method.GET, binnerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                getBinnerImageFromIntenet(s,THREE_BINNER_FLAG,threeImageViews,REFRESH_THREE_BINNER,SCROLL__THREE_BINNER);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(binnerThreeRequest);//加入到队列
        threeViewPager.addOnPageChangeListener(new MyPageChangeListener(threeImageViews,threePointGroup));
        threeViewPager.setOnTouchListener(new MyPageTouchListener(SCROLL__THREE_BINNER,threeViewPager));



        //-----------初始化我的钱包，我的按钮等8个组件-------------------
        myWalletll= (LinearLayout) v.findViewById(R.id.ll_my_wallet);
        myOrderll= (LinearLayout) v.findViewById(R.id.ll_my_order);
        browseHistoryll= (LinearLayout) v.findViewById(R.id.ll_browse_history);
        shipAddressll= (LinearLayout) v.findViewById(R.id.ll_ship_address);
        xianShiTeYoull= (LinearLayout) v.findViewById(R.id.ll_xian_shi_te_you);
        helpll= (LinearLayout) v.findViewById(R.id.ll_help);
        brandPagell= (LinearLayout) v.findViewById(R.id.ll_brand_page);
        youHuiHuoDongll= (LinearLayout) v.findViewById(R.id.ll_you_hui_huo_dong);

        myWalletll.setOnClickListener(this);
        myOrderll.setOnClickListener(this);
        browseHistoryll.setOnClickListener(this);
        shipAddressll.setOnClickListener(this);
        xianShiTeYoull.setOnClickListener(this);
        helpll.setOnClickListener(this);
        brandPagell.setOnClickListener(this);
        youHuiHuoDongll.setOnClickListener(this);

        //接收json数据
        jsonHelper=new JsonHelper();


        //-----------------------限时秒杀部分---------------------------------
        String shanShiUrl="http://www.zmobuy.com/PHP/index.php?url=/home/grab";
        StringRequest xianShiStringRequest=new StringRequest(Request.Method.GET, shanShiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag,s);
                List<Good> goods = new ArrayList<Good>();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Good good = new Good();
                        JSONObject js = jsonArray.getJSONObject(i);
                        good.setGoodId(js.getString("goods_id"));
                        good.setGoodsImg(js.getString("goods_img"));
                        good.setGoodsThumb(js.getString("goods_thumb"));
                        good.setPromoteEndDate(js.getString("promote_end_date"));
                        good.setPromotePrice(js.getString("promote_price"));
                        good.setIsPromote(js.getString("is_promote"));
                        good.setPromoteStartDate(js.getString("promote_start_date"));
                        good.setGoodName(JsonHelper.decodeUnicode(js.getString("goods_name")));
                        good.setMarket_price(js.getString("market_price"));
                        good.setShopPrice(JsonHelper.decodeUnicode(js.getString("shop_price")));
                        good.setGoodsNumber(js.getString("goods_number"));
                        goods.add(good);
                    }
                    xianShiMiaoShaImagView= (ImageView) v.findViewById(R.id.img_xian_shi_miao_sha_content);
                    final String urlString=goods.get(0).getGoodsImg();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpURLConnection conn=null;
                            try {
                                URL url=new URL(urlString);
                                conn= (HttpURLConnection) url.openConnection();
                                InputStream in=conn.getInputStream();
                                Bitmap bitmap=BitmapFactory.decodeStream(in);
                                Message message=handler.obtainMessage(REFRESH_XIAN_SHI_MIAO_SHA);
                                message.obj=bitmap;
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }finally {
                                if(conn!=null){
                                    conn.disconnect();
                                }
                            }

                        }
                    }).start();

                    //限时秒杀的gridview
                    xianShiMiaoShaGridView = (MyGridView) v.findViewById(R.id.grid_view_xian_shi_miao_sha);
                    if (goods.size() > 0) {
                        XianShiAdapter xianShiAdapter = new XianShiAdapter(context, getElementsFromList(goods, 4));
                        xianShiMiaoShaGridView.setAdapter(xianShiAdapter);
                        xianShiMiaoShaGridView.setOnItemClickListener(new XianShiOnItemClickListener());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(xianShiStringRequest);



        //----------------------用viewpager做的精品推荐部分-----------------------
        final ViewPager jingPinViewPager= (ViewPager) v.findViewById(R.id.viewpager_jing_pin);
        final List<View> gridViews=new ArrayList<View>();
        String jingPinRequestUrl="http://www.zmobuy.com/PHP/index.php?url=/home/bestgoods";
        StringRequest jingPinStringRequest=new StringRequest(Request.Method.GET, jingPinRequestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                List<Good> goods = parseCaiNiLikeAndJingPin(s);
               // Good[] goodArray= (Good[]) goods.toArray();
                int size=goods.size();
                int count=0;//取的次数
                if(size%3==0){
                    count=size;
                }else{
                    count=size/3+1;
                }
                for(int i=0;i<count;i++){
                    List<Good> goodItems=new ArrayList<Good>();
                    for(int j=3*i;j<3*i+3;j++){
                        if(j<size){
                            goodItems.add(goods.get(j));
                        }
                    }
                    JingPinAdapter jingPinAdapter=new JingPinAdapter(context,goodItems);
                    MyGridView gridView=new MyGridView(context);
                    //gridView.setColumnWidth(230);
                    gridView.setHorizontalSpacing(5);
                    gridView.setVerticalSpacing(5);
                    gridView.setGravity(Gravity.CENTER);
                    gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                    gridView.setNumColumns(3);
                    gridView.setAdapter(jingPinAdapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
                        }
                    });
                    gridViews.add(gridView);
                }
                jingPinViewPager.setAdapter(new MyPagerAdapter(gridViews));
                LinearLayout jingPinPointGroup= (LinearLayout) v.findViewById(R.id.ll_point_group_jing_pin);
                initPoint(jingPinPointGroup,gridViews.size());
                jingPinViewPager.addOnPageChangeListener(new MyPageChangeListener(gridViews,jingPinPointGroup));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(jingPinStringRequest);


        //-----------------------精品推荐部分---------------------------------
        /*String jingPinRequestUrl="http://www.zmobuy.com/PHP/index.php?url=/home/bestgoods";
        StringRequest jingPinStringRequest=new StringRequest(Request.Method.GET, jingPinRequestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                List<Good> goods = parseCaiNiLikeAndJingPin(s);
                    //精品推荐的gridview
                jingPinTuiJianGridview = (MyGridView) v.findViewById(R.id.grid_view_jing_pin_tui_jian);
                if (goods.size() > 0) {
                    JingPinAdapter jingPinAdapter = new JingPinAdapter(context, getElementsFromList(goods, 3));
                    jingPinTuiJianGridview.setAdapter(jingPinAdapter);
                    jingPinTuiJianGridview.setOnItemClickListener(new JingPinOnItemClickListener());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(jingPinStringRequest);*/


        //-----------------------猜你喜欢部分---------------------------------
        caiNiXiHuanGridView= (MyGridViewA) v.findViewById(R.id.grid_view_cai_ni_xi_huan);
        String caiNiUrl="http://www.zmobuy.com/PHP/index.php?url=/home/hotgoods";
        StringRequest caiNiXiHuanRequest=new StringRequest(Request.Method.GET, caiNiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag,"猜你喜欢部分"+s);
                List<Good> goods=parseCaiNiLikeAndJingPin(s);
                CaiNiXiHuanAdapter caiNiXiHuanAdapter=new CaiNiXiHuanAdapter(context,goods);
                caiNiXiHuanGridView.setAdapter(caiNiXiHuanAdapter);
                //CategoryImageLoadHelper.setGridViewViewHeightBasedOnChildren(caiNiXiHuanGridView);
                caiNiXiHuanGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(caiNiXiHuanRequest);

        //3个更多按钮的初始化和点击事件
        xianShiMoreTextView= (TextView) v.findViewById(R.id.text_xian_shi_more);
        jingPinMoreTextView= (TextView) v.findViewById(R.id.text_jing_pin_more);
        dianPuJieMoreTextView= (TextView) v.findViewById(R.id.text_dian_pu_jie_more);
        xianShiMoreTextView.setOnClickListener(this);
        jingPinMoreTextView.setOnClickListener(this);
        dianPuJieMoreTextView.setOnClickListener(this);
    }


    /**
     * 猜你喜欢和精品推荐的json数据解析
     */
    public List<Good> parseCaiNiLikeAndJingPin(String s){
        List<Good> goods = new ArrayList<Good>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Good good = new Good();
                JSONObject js = jsonArray.getJSONObject(i);
                good.setGoodId(js.getString("goods_id"));
                good.setUserId(js.getString("user_id"));
                good.setGoodName(JsonHelper.decodeUnicode(js.getString("goods_name")));
                good.setWarehousePrice(js.getString("warehouse_price"));
                good.setWarehousePromotePrice(js.getString("warehouse_promote_price"));
                good.setRegionPrice(js.getString("region_price"));
                good.setPromotePrice(js.getString("region_promote_price"));
                good.setModel_price(js.getString("model_price"));
                good.setModel_attr(js.getString("model_attr"));
                good.setGoods_name_style(js.getString("goods_name_style"));
                good.setCommentsNumber(js.getString("comments_number"));
                good.setSalesVolume(js.getString("sales_volume"));
                good.setMarket_price(js.getString("market_price"));
                good.setIsNew(js.getString("is_new"));
                good.setIsBest(js.getString("is_best"));
                good.setIsHot(js.getString("is_hot"));
                good.setGoodsNumber(js.getString("goods_number"));
                good.setOrgPrice(js.getString("org_price"));
                good.setShopPrice(JsonHelper.decodeUnicode(js.getString("shop_price")));
                good.setPromotePrice(JsonHelper.decodeUnicode(js.getString("promote_price")));
                good.setGoodType(js.getString("goods_type"));
                good.setPromoteStartDate(js.getString("promote_start_date"));
                good.setPromoteEndDate(js.getString("promote_end_date"));
                good.setIsPromote(js.getString("is_promote"));
                good.setGoodsBrief(js.getString("goods_brief"));
                good.setGoodsThumb(js.getString("goods_thumb"));
                good.setGoodsImg(js.getString("goods_img"));
                goods.add(good);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return goods;
    }




    /**
     * 从网络获取广告图片
     * @param s 从网络返回的json数据
     * @param binnerPositionFlag 广告的位置，是最上面的，还是中间的，还是最下面的
     * @param imageViews 将广告图片添加到的view的集合
     * @param refreshFlag 需要刷新的view的消息标记
     * @param scrollFlag 需要滚动的view的标记
     */
    private void getBinnerImageFromIntenet(String s,int binnerPositionFlag, final List<View> imageViews, final int refreshFlag, final int scrollFlag) {
        MyLog.d(tag, "广告位的:" + s);
        final List<Binner> binners=new ArrayList<Binner>();
        try {
            JSONObject jsonObject1=new JSONObject(s);
            JSONObject jsonObject2=jsonObject1.getJSONObject("data");
            JSONArray jsonArray=jsonObject2.getJSONArray("player");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                Binner binner=new Binner(jsonObject.getString("id"),jsonObject.getString("photo"));
                switch (binnerPositionFlag){
                    case FIRST_BINNER_FLAG:
                        if("7".equals(binner.getId())||"6".equals(binner.getId())){
                            binners.add(binner);
                        }
                        break;
                    case SECOND_BINNER_FLAG:
                        if("2".equals(binner.getId())){
                            binners.add(binner);
                        }
                        break;
                    case THREE_BINNER_FLAG:
                        if("16".equals(binner.getId())){
                            binners.add(binner);
                        }
                        break;
                }

            }
            new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(final Binner binner:binners){
                            ImageView imageView=new ImageView(context);
                            URL url= null;
                            try {
                                url = new URL(binner.getImg());
                                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                                Bitmap bitmap= BitmapFactory.decodeStream(conn.getInputStream());
                                imageView.setImageBitmap(bitmap);
                                imageViews.add(imageView);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(refreshFlag);
                        handler.sendEmptyMessage(scrollFlag);
                    }
                }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从good集合里面取出前面n个元素
     *
     */
    public List<Good> getElementsFromList(List<Good> goods, int num) {
        List<Good> list = new ArrayList<Good>();
        for (int i = 1; i < num; i++) {
            list.add(goods.get(i));
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_my_wallet:
                Intent intent=new Intent(context, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_my_order:
                Toast.makeText(context, "点击了我的订单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_browse_history:
                Toast.makeText(context, "点击了浏览记录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_ship_address:
                Toast.makeText(context, "点击了收货地址", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_xian_shi_te_you:
                Toast.makeText(context, "点击了限时特优", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_help:
                Toast.makeText(context, "点击了帮助中心", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_brand_page:
                Toast.makeText(context, "点击了品牌页面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_you_hui_huo_dong:
                Toast.makeText(context, "点击了优惠活动", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_xian_shi_more://限时秒杀的更多按钮
                Toast.makeText(context, "点击了限时秒杀更多", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_jing_pin_more://精品推荐的更多按钮
                Toast.makeText(context, "点击了精品推荐更多", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_dian_pu_jie_more://店铺街的更多按钮
                Toast.makeText(context, "点击了店铺街更多", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    @Override
    public void onStop() {
        super.onStop();
        handler.removeMessages(SCROLL__FIRST_BINNER);
    }


    /**
     * 广告viewpager的滚动监听器
     */
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener{
        private List<View> imageViews;
        private LinearLayout pointGroup;
        private int lastPosition;

        public MyPageChangeListener(List<View> imageViews, LinearLayout pointGroup) {
            this.imageViews = imageViews;
            this.pointGroup = pointGroup;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        /**
         * 页面切换后调用
         * position  新的页面位置
         */
        public void onPageSelected(int position) {
            position = position % imageViews.size();
            //改变指示点的状态
            //把当前点enbale 为true
            if(imageViews.size()>1){
                pointGroup.getChildAt(position).setEnabled(true);
                //把上一个点设为false
                pointGroup.getChildAt(lastPosition).setEnabled(false);
                lastPosition = position;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    /**
     * 广告viewpager的触摸监听器
     */
    public class MyPageTouchListener implements View.OnTouchListener{
        private boolean isStoped = false;
        private int scrollFlag;
        private ViewPager viewPager;

        /**
         *
         * @param scrollFlag 向handler里面发送消息的标记
         * @param viewPager
         */
        public MyPageTouchListener(int scrollFlag, ViewPager viewPager) {
            this.scrollFlag = scrollFlag;
            this.viewPager = viewPager;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float firstX = 0;//手指按下时的x值
            float firstY = 0;
            float distance = 0f;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    firstX = event.getX();
                    firstY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float lastX = event.getX();
                    float lastY = event.getY();
                    distance = (float) Math.sqrt((lastX - firstX) * (lastX - firstX) +
                            (lastY - firstY) * (lastY - firstY));
                    if (distance > 10) {
                        handler.removeMessages(scrollFlag);//暂停轮播
                        isStoped = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (isStoped) {
                        handler.sendEmptyMessageDelayed(scrollFlag, 2000);//重新开始轮播
                        isStoped=false;
                    } else { //这种情况下属于点击事件
                        Toast.makeText(context, "" + viewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;//这里必须返回false，否则viewpager的ontouchenevnt接收不到事件
        }
    }

    /**
     * 限时秒杀gridview的项目点击监听器
     */
    public class XianShiOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(context,position+"",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 精品推荐gridview的项目点击监听器
     */
    public class JingPinOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(context,position+"",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 广告viewpager的适配器
     */
    private class MyPagerAdapter extends PagerAdapter {
        private List<View> imageViews;
        public MyPagerAdapter(List<View> imageViews){
            this.imageViews=imageViews;
        }
        @Override
        /**
         * 获得页面的总数
         */
        public int getCount() {
            if(imageViews.size()==1){
                return 1;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        /**
         * 获得相应位置上的view
         * container  view的容器，其实就是viewpager自身
         * position 	相应的位置
         */
        public Object instantiateItem(ViewGroup container, int position) {
            View view=null;
            //对ViewPager页号求模取出View列表中要显示的项
            if(imageViews.size()>0){
                position %= imageViews.size();
                if (position<0 ){
                    position = imageViews.size()+position;
                }

                view =imageViews.get(position);
//如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                ViewParent vp =view.getParent();
                if (vp!= null ){
                    ViewGroup parent = (ViewGroup)vp;
                    parent.removeView(view);
                }
                container.addView(view);
            }

            return view;
        }

        @Override
        /**
         * 判断 view和object的对应关系
         */
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        /**
         * 销毁对应位置上的object
         */
        public void destroyItem(ViewGroup container, int position, Object object) {
            //container.removeView(firstImageViews.get(position % firstImageViews.size()));
        }
    }
}
