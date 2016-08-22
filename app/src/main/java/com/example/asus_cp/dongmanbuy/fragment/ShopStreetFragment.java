package com.example.asus_cp.dongmanbuy.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.MainActivity;
import com.example.asus_cp.dongmanbuy.adapter.ShopStreetShopListAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ShopStreetSpinnerAdapter;
import com.example.asus_cp.dongmanbuy.customview.MyFocuseableListView;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.customview.MyListView;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺街的界面
 * Created by asus-cp on 2016-05-19.
 */
public class ShopStreetFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private String tag="ShopStreetFragment";
    private Spinner paiLieShunXunSpinner;//排列顺序
    private Spinner productCategorySpinner;//商品类型
    private Spinner shopPostionSpinner;//店铺位置
    private PullToRefreshListView shopListListView;//店铺列表
    private LinearLayout typeBackGroudLinearLayout;//在listview的上面

    private LinearLayout goodTypeLinearLayout;
    private TextView goodTypeTextView;
    private ImageView leftImageView;
    private ImageView rightImageView;
    private View parentView;

    private PopupWindow typePopuWindow;

    private LayoutInflater inflater;

    //弹出窗口view里面的数据
    private List<String> oneList;
    private List<String> twoList;

    //点击类型之后弹窗窗口里面的view
    private View typeView;
    private MyGridViewA typeGridView;
//    private MyFocuseableListView oneListView;
//    private MyFocuseableListView twoListView;
    private Button resetButton;
    private Button confirmButton;
    private ArrayAdapter oneAdapter;
//    private ArrayAdapter twoAdapter;




    private RequestQueue requestQueue;

    private String indexUrl="http://www.zmobuy.com/PHP/?url=/store/index";//店铺分类的url

    public static final int ALL=0;
    public static final int SHANG_ZHUANG=1;
    public static final int XIA_ZHUANG=2;
    public static final int XIANG_BAO=3;
    public static final int ZHAI_PIN=4;
    public static final int DIY=5;
    public static final int MO_WAN=6;
    public static final int MAO_RONG=7;
    public static final int PEI_SHI=8;

    private ShopStreetShopListAdapter allAdapter;
    private ShopStreetShopListAdapter shuJiAdapter;
    private ShopStreetShopListAdapter moWanAdapter;
    private ShopStreetShopListAdapter DIYAdapter;
    private ShopStreetShopListAdapter shangZhuangAdapter;
    private ShopStreetShopListAdapter xiaZhuangAdapter;
    private ShopStreetShopListAdapter xiangBaoAdapter;
    private ShopStreetShopListAdapter zhaiPinAdapter;
    private ShopStreetShopListAdapter maoRongAdapter;
    private ShopStreetShopListAdapter peiShiAdapter;


    private List<ShopModel> allModles=new ArrayList<ShopModel>();
    private List<ShopModel> shuJiModles=new ArrayList<ShopModel>();
    private List<ShopModel> moWanModles=new ArrayList<ShopModel>();
    private List<ShopModel> DIYModles=new ArrayList<ShopModel>();
    private List<ShopModel> shangZhuangModles=new ArrayList<ShopModel>();
    private List<ShopModel> xiaZhuangModles=new ArrayList<ShopModel>();
    private List<ShopModel> xiangBaoModles=new ArrayList<ShopModel>();
    private List<ShopModel> zhaiPinModles=new ArrayList<ShopModel>();
    private List<ShopModel> maoRongModles=new ArrayList<ShopModel>();
    private List<ShopModel> peiShiModles=new ArrayList<ShopModel>();


    private int count0=1;
    private int count1=1;
    private int count2=1;
    private int count3=1;
    private int count4=1;
    private int count5=1;
    private int count6=1;
    private int count7=1;
    private int count8=1;
    private int count9=1;

    private int location;//用于上拉加载的判断

    private MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.shop_street_fragment_layout,null);
        init(v);
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init(View v) {
        context=getActivity();
        mainActivity= (MainActivity) getActivity();
        requestQueue= MyApplication.getRequestQueue();

        inflater=LayoutInflater.from(context);
        parentView=v.findViewById(R.id.ll_popu_parent);

        goodTypeLinearLayout= (LinearLayout) v.findViewById(R.id.ll_good_type);
        goodTypeTextView= (TextView) v.findViewById(R.id.text_good_type);
        leftImageView= (ImageView) v.findViewById(R.id.img_left_good_type);
        rightImageView= (ImageView) v.findViewById(R.id.img_right_good_type);
        paiLieShunXunSpinner= (Spinner) v.findViewById(R.id.spin_pai_lie_shun_xu);
        productCategorySpinner= (Spinner) v.findViewById(R.id.spin_product_category);
        shopPostionSpinner= (Spinner) v.findViewById(R.id.spin_shop_street_position);
        typeBackGroudLinearLayout= (LinearLayout) v.findViewById(R.id.ll_type_backgroud);
        shopListListView= (PullToRefreshListView) v.findViewById(R.id.list_view_shop_list);
        shopListListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//设置模式是上拉加载
        shopListListView.setOnRefreshListener(new MyOnrefreshListener());

        mainActivity.menu.addIgnoredView(shopListListView);//将listview添加到忽略里面

        //点击类型之后弹出窗口数据的初始化
        typeView=inflater.inflate(R.layout.good_type_layout,null);
        typeGridView= (MyGridViewA) typeView.findViewById(R.id.grid_view_good_type);
//        oneListView= (MyFocuseableListView) typeView.findViewById(R.id.list_type_one);
//        twoListView= (MyFocuseableListView) typeView.findViewById(R.id.list_type_two);
        resetButton= (Button) typeView.findViewById(R.id.btn_reset);
        confirmButton= (Button) typeView.findViewById(R.id.btn_confirm);
        oneList=new ArrayList<String>();
        twoList=new ArrayList<String>();
        oneList.add("全部分类");
        oneList.add("上装");
        oneList.add("下装");
        oneList.add("箱包");
        oneList.add("宅品");
        oneList.add("DIY定制");
        oneList.add("模玩手办");
        oneList.add("毛绒");
        oneList.add("配饰");
        oneAdapter=new ArrayAdapter(context,R.layout.good_type_item_layout,R.id.text_type_item,oneList);

        DialogHelper.showDialog(context);
        location=ALL;
        tongYongClickChuLi("", ALL);//初始状态

        //设置点击事件
        goodTypeLinearLayout.setOnClickListener(this);



        //twoAdapter=new ArrayAdapter(context,R.layout.good_type_item_layout,R.id.text_type_item,twoList);


        /*
        List<String> paiLies=new ArrayList<String>();
        paiLies.add("排列顺序");
        List<String> shopPostions=new ArrayList<String>();
        shopPostions.add("店铺位置");
        final List<String> productCategories=new ArrayList<String>();
        productCategories.add("商品类型");
        productCategories.add("书籍");
        productCategories.add("模玩 手办");
        productCategories.add("DIY定制");
        productCategories.add("上装");
        productCategories.add("下装");
        productCategories.add("箱包");
        productCategories.add("宅品");
        productCategories.add("毛绒");
        productCategories.add("配饰");

        ShopStreetSpinnerAdapter paiLieAdapter=new ShopStreetSpinnerAdapter(context,paiLies);
        ShopStreetSpinnerAdapter shopPostionAdapter=new ShopStreetSpinnerAdapter(context,shopPostions);
        ShopStreetSpinnerAdapter productCategroyAdapter=new ShopStreetSpinnerAdapter(context,productCategories);

        paiLieShunXunSpinner.setAdapter(paiLieAdapter);
        shopPostionSpinner.setAdapter(shopPostionAdapter);
        productCategorySpinner.setAdapter(productCategroyAdapter);
        productCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MyLog.d(tag, "点击了：" + productCategories.get(position));
                switch (position) {
                    case ALL:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi("", ALL);
                        location = position;
                        count0 = 1;//将count设为初始值
                        break;
                    case SHU_JI:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1625 + "", SHU_JI);
                        location = position;
                        count1 = 1;
                        break;
                    case MO_WAN:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1661 + "", MO_WAN);
                        location = position;
                        count2 = 1;
                        break;
                    case DIY:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1647 + "", DIY);
                        location = position;
                        count3 = 1;
                        break;
                    case SHANG_ZHUANG:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1464 + "", SHANG_ZHUANG);
                        location = position;
                        count4 = 1;
                        break;
                    case XIA_ZHUANG:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1479 + "", XIA_ZHUANG);
                        location = position;
                        count5 = 1;
                        break;
                    case XIANG_BAO:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1486 + "", XIANG_BAO);
                        location = position;
                        count6 = 1;
                        break;
                    case ZHAI_PIN:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1492 + "", ZHAI_PIN);
                        location = position;
                        count7 = 1;
                        break;
                    case MAO_RONG:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1501 + "", MAO_RONG);
                        location = position;
                        count8 = 1;
                        break;
                    case PEI_SHI:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1506 + "", PEI_SHI);
                        location = position;
                        count9 = 1;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


    }

    /**
     * 点击事件处理,主要用于点击
     * @param category 类别的代号，比如书籍是1625
     */
    private void tongYongClickChuLi(final String category, final int position) {
        StringRequest moWanRequest=new StringRequest(Request.Method.POST, indexUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                switch (position) {
                    case ALL:
                        DialogHelper.dissmisDialog();
                        allModles.clear();
                        allModles.addAll(parseJson(s));
                        allAdapter=new ShopStreetShopListAdapter(mainActivity,allModles);
                        shopListListView.setAdapter(allAdapter);
                        break;
                    case SHANG_ZHUANG:
                        DialogHelper.dissmisDialog();
                        shangZhuangModles.clear();
                        shangZhuangModles.addAll(parseJson(s));
                        shangZhuangAdapter=new ShopStreetShopListAdapter(mainActivity,shangZhuangModles);
                        shopListListView.setAdapter(shangZhuangAdapter);
                        break;
                    case XIA_ZHUANG:
                        DialogHelper.dissmisDialog();
                        xiaZhuangModles.clear();
                        xiaZhuangModles.addAll(parseJson(s));
                        xiaZhuangAdapter=new ShopStreetShopListAdapter(mainActivity,xiaZhuangModles);
                        shopListListView.setAdapter(xiaZhuangAdapter);
                        break;
                    case XIANG_BAO:
                        DialogHelper.dissmisDialog();
                        xiangBaoModles.clear();
                        xiangBaoModles.addAll(parseJson(s));
                        xiangBaoAdapter=new ShopStreetShopListAdapter(mainActivity,xiangBaoModles);
                        shopListListView.setAdapter(xiangBaoAdapter);
                        break;
                    case ZHAI_PIN:
                        DialogHelper.dissmisDialog();
                        zhaiPinModles.clear();
                        zhaiPinModles.addAll(parseJson(s));
                        zhaiPinAdapter=new ShopStreetShopListAdapter(mainActivity,zhaiPinModles);
                        shopListListView.setAdapter(zhaiPinAdapter);
                        break;
                    case DIY:
                        DialogHelper.dissmisDialog();
                        DIYModles.clear();
                        DIYModles.addAll(parseJson(s));
                        DIYAdapter=new ShopStreetShopListAdapter(mainActivity,DIYModles);
                        shopListListView.setAdapter(DIYAdapter);
                        break;
                    case MO_WAN:
                        DialogHelper.dissmisDialog();
                        moWanModles.clear();
                        moWanModles.addAll(parseJson(s));
                        moWanAdapter=new ShopStreetShopListAdapter(mainActivity,moWanModles);
                        shopListListView.setAdapter(moWanAdapter);
                        break;
                    case MAO_RONG:
                        DialogHelper.dissmisDialog();
                        maoRongModles.clear();
                        maoRongModles.addAll(parseJson(s));
                        maoRongAdapter=new ShopStreetShopListAdapter(mainActivity,maoRongModles);
                        shopListListView.setAdapter(maoRongAdapter);
                        break;
                    case PEI_SHI:
                        DialogHelper.dissmisDialog();
                        peiShiModles.clear();
                        peiShiModles.addAll(parseJson(s));
                        peiShiAdapter=new ShopStreetShopListAdapter(mainActivity,peiShiModles);
                        shopListListView.setAdapter(peiShiAdapter);
                        break;
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
                String json="{\"page\":\"1\",\"where\":\""+category+"\",\"type\":\"1\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(moWanRequest);
    }

    /**
     * 主要用于上拉加载
     * @param category
     * @param page
     */
    private void tongYongClickChuLi(final String category, final String page){
        StringRequest moWanRequest=new StringRequest(Request.Method.POST, indexUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag,"category="+category+"page="+page+"s="+s);
                if(parseJson(s)==null || parseJson(s).size()==0){
                    Toast.makeText(context,"已经是最后一项了",Toast.LENGTH_SHORT).show();
                    shopListListView.onRefreshComplete();//通知刷新完毕
                }else{
                    switch (location) {
                        case ALL:
                            allModles.addAll(parseJson(s));
                            allAdapter.notifyDataSetChanged();
                            break;
                        case MO_WAN:
                            moWanModles.addAll(parseJson(s));
                            moWanAdapter.notifyDataSetChanged();
                            break;
                        case DIY:
                            DIYModles.addAll(parseJson(s));
                            DIYAdapter.notifyDataSetChanged();
                            break;
                        case SHANG_ZHUANG:
                            shangZhuangModles.addAll(parseJson(s));
                            shangZhuangAdapter.notifyDataSetChanged();
                            break;
                        case XIA_ZHUANG:
                            xiaZhuangModles.addAll(parseJson(s));
                            xiaZhuangAdapter.notifyDataSetChanged();
                            break;
                        case XIANG_BAO:
                            xiangBaoModles.addAll(parseJson(s));
                            xiangBaoAdapter.notifyDataSetChanged();
                            break;
                        case ZHAI_PIN:
                            zhaiPinModles.addAll(parseJson(s));
                            zhaiPinAdapter.notifyDataSetChanged();
                            break;
                        case MAO_RONG:
                            maoRongModles.addAll(parseJson(s));
                            maoRongAdapter.notifyDataSetChanged();
                            break;
                        case PEI_SHI:
                            peiShiModles.addAll(parseJson(s));
                            peiShiAdapter.notifyDataSetChanged();
                            break;
                    }
                    shopListListView.onRefreshComplete();//通知刷新完毕
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
                String json="{\"page\":\""+page+"\",\"where\":\""+category+"\",\"type\":\"1\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(moWanRequest);
    }

    /**
     *将json数据解析出来放到集合里面
     */
    private List<ShopModel> parseJson(String s) {
        MyLog.d(tag, "书籍返回的数据是" + s);
        List<ShopModel> shopModels=new ArrayList<ShopModel>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONArray jsonArray=jsonObject1.getJSONArray("list");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject ziJsonObj=jsonArray.getJSONObject(i);
                ShopModel shopModel=new ShopModel();
                shopModel.setShopId(ziJsonObj.getString("shop_id"));
                shopModel.setUserId(ziJsonObj.getString("user_id"));
                shopModel.setShopName(JsonHelper.decodeUnicode(ziJsonObj.getString("shop_name")));
                shopModel.setShopLogo(ziJsonObj.getString("shop_logo"));
                shopModel.setLogoThumb(ziJsonObj.getString("logo_thumb"));
                shopModel.setStreetThumb(ziJsonObj.getString("street_thumb"));
                shopModel.setBrandThumb(ziJsonObj.getString("brand_thumb"));
                shopModel.setCommenTrank(ziJsonObj.getString("commentrank_font"));
                shopModel.setCommentServer(ziJsonObj.getString("commentserver_font"));
                shopModel.setCommentDelivery(ziJsonObj.getString("commentdelivery_font"));
                shopModel.setCommenTrankScore(ziJsonObj.getString("commentrank"));
                shopModel.setCommentServerScore(ziJsonObj.getString("commentserver"));
                shopModel.setCommentDeliveryScore(ziJsonObj.getString("commentdelivery"));
                shopModel.setGazeNumber(ziJsonObj.getString("gaze_number"));
                shopModel.setGazeStatus(ziJsonObj.getString("gaze_status"));
                JSONArray goodsArray=null;
                try{
                    goodsArray=ziJsonObj.getJSONArray("goods");//注意这里的处理方法，这句话崩了，不至于让整个程序都崩掉
                }catch (Exception e){
                    e.printStackTrace();
                }
                List<Good> goods=new ArrayList<Good>();
                if(goodsArray!=null){
                    for(int j=0;j<goodsArray.length();j++){
                        JSONObject goodJsonObject=goodsArray.getJSONObject(j);
                        Good good=new Good();
                        good.setGoodId(goodJsonObject.getString("goods_id"));
                        good.setGoodName(JsonHelper.decodeUnicode(goodJsonObject.getString("goods_name")));
                        good.setGoodsNumber(goodJsonObject.getString("goods_number"));
                        good.setMarket_price(JsonHelper.decodeUnicode(goodJsonObject.getString("market_price")));
                        good.setShopPrice(goodJsonObject.getString("shop_price"));
                        good.setGoodsThumb(goodJsonObject.getString("goods_thumb"));
                        good.setGoodsImg(goodJsonObject.getString("goods_img"));
                        good.setSalesVolume(goodJsonObject.getString("sales_volume"));
                        good.setCommentsNumber(goodJsonObject.getString("comments_number"));
                        goods.add(good);
                    }
                    shopModel.setGoods(goods);
                }
                shopModels.add(shopModel);
            }
            MyLog.d(tag, "集合的大小:" + shopModels.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopModels;
    }


    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_good_type://商品类型按钮的点击事件
                count0 = 1;//将count设为初始值
                location=ALL;
                showPopuWindow();
                break;

            //----------------类型弹出窗口的点击事件---------------------
            case R.id.btn_reset://重置按钮
                Toast.makeText(context,"重置按钮",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_confirm://点击了确认按钮
                Toast.makeText(context,"点击了确认按钮",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /**
     * 显示popuwindow
     */
    private void showPopuWindow() {
//        oneListView.setAdapter(oneAdapter);
//        twoListView.setAdapter(twoAdapter);
        typeGridView.setAdapter(oneAdapter);
        typeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goodTypeTextView.setText(oneList.get(position));
                typePopuWindow.dismiss();
                switch (position) {
                    case ALL:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi("", ALL);
                        location = position;
                        count0 = 1;//将count设为初始值
                        break;
                    case SHANG_ZHUANG:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1464 + "", SHANG_ZHUANG);
                        location = position;
                        count4 = 1;
                        break;
                    case XIA_ZHUANG:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1479 + "", XIA_ZHUANG);
                        location = position;
                        count5 = 1;
                        break;
                    case XIANG_BAO:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1486 + "", XIANG_BAO);
                        location = position;
                        count6 = 1;
                        break;
                    case ZHAI_PIN:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1492 + "", ZHAI_PIN);
                        location = position;
                        count7 = 1;
                        break;
                    case DIY:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1647 + "", DIY);
                        location = position;
                        count3 = 1;
                        break;
                    case MO_WAN:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1661 + "", MO_WAN);
                        location = position;
                        count2 = 1;
                        break;
                    case MAO_RONG:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1501 + "", MAO_RONG);
                        location = position;
                        count8 = 1;
                        break;
                    case PEI_SHI:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1506 + "", PEI_SHI);
                        location = position;
                        count9 = 1;
                        break;
                }
            }
        });
        /*//设置点击事件
        oneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context, "点击的位置是：" + position, Toast.LENGTH_SHORT).show();

            }
        });*/

        //重置和确认的点击事件
        resetButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);

        typePopuWindow=new PopupWindow(typeView,ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        typePopuWindow.setBackgroundDrawable(new ColorDrawable());
        typePopuWindow.setOutsideTouchable(true);
        typePopuWindow.setFocusable(true);
        //typePopuWindow.showAtLocation(parentView, Gravity.CENTER,0,0);
        typePopuWindow.showAsDropDown(goodTypeLinearLayout,0,10);
        typeBackGroudLinearLayout.setVisibility(View.VISIBLE);
        setBackgroundAlpha(0.5f);
        typePopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                typeBackGroudLinearLayout.setVisibility(View.GONE);
                //setBackgroundAlpha(1f);
            }
        });

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void setBackgroundAlpha(float bgAlpha) {
//        WindowManager.LayoutParams lp = mainActivity.getWindow().getAttributes();
//        lp.alpha = bgAlpha; //0.0-1.0
//        mainActivity.getWindow().setAttributes(lp);

        typeBackGroudLinearLayout.setAlpha(bgAlpha);


    }






    /**
     * 自定义的刷新监听器
     */
    class MyOnrefreshListener implements PullToRefreshBase.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatedDate=simpleDateFormat.format(new Date());
            // Update the LastUpdatedLabel
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(formatedDate);
            PullToRefreshBase.Mode mode= shopListListView.getCurrentMode();//注意是currentmode，不是mode
            if(mode== PullToRefreshBase.Mode.PULL_FROM_START){      //下拉刷新
                refreshView.getLoadingLayoutProxy().setPullLabel("下拉可以刷新");
                refreshView.getLoadingLayoutProxy().setReleaseLabel("释放刷新");
                refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新...");

            }else if(mode== PullToRefreshBase.Mode.PULL_FROM_END){      //向上加载
                refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在加载...");
                refreshView.getLoadingLayoutProxy().setPullLabel("上拉可以加载");
                refreshView.getLoadingLayoutProxy().setReleaseLabel("释放刷新");
                count0++;
                count1++;
                count2++;
                count3++;
                count4++;
                count5++;
                count6++;
                count7++;
                count8++;
                count9++;
                switch (location) {
                    case ALL:
                        tongYongClickChuLi("", count0 + "");
                        break;
                    case MO_WAN:
                        tongYongClickChuLi(1661+"", count2 + "");
                        break;
                    case DIY:
                        tongYongClickChuLi(1647+"", count3 + "");
                        break;
                    case SHANG_ZHUANG:
                        tongYongClickChuLi(1464+"", count4 + "");
                        break;
                    case XIA_ZHUANG:
                        tongYongClickChuLi(1479+"", count5 + "");
                        break;
                    case XIANG_BAO:
                        tongYongClickChuLi(1486+"", count6+ "");
                        break;
                    case ZHAI_PIN:
                        tongYongClickChuLi(1492+"", count7 + "");
                        break;
                    case MAO_RONG:
                        tongYongClickChuLi(1501+"", count8 + "");
                        break;
                    case PEI_SHI:
                        tongYongClickChuLi(1506+"", count9 + "");
                        break;
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mainActivity.menu.removeIgnoredView(shopListListView);
    }
}
