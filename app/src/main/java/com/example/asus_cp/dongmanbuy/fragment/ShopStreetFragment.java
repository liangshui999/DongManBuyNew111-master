package com.example.asus_cp.dongmanbuy.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.MyNetHelper;
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
public class ShopStreetFragment extends BaseFragment implements View.OnClickListener{
    private Context context;
    private String tag="ShopStreetFragment";
    private Spinner paiLieShunXunSpinner;//排列顺序
    private Spinner productCategorySpinner;//商品类型
    private Spinner shopPostionSpinner;//店铺位置
    private PullToRefreshListView shopListListView;//店铺列表
    private LinearLayout typeBackGroudLinearLayout;//在listview的上面

    private LinearLayout goodTypeLinearLayout;
    private TextView goodTypeTextView;
    private View parentView;

    private PopupWindow typePopuWindow;

    private LayoutInflater inflater;

    //弹出窗口view里面的数据
    private List<String> oneList;


    //点击类型之后弹窗窗口里面的view
    private View typeView;
    private MyGridViewA typeGridView;

    private Button resetButton;
    private Button confirmButton;
    private ArrayAdapter oneAdapter;


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

    private ShopStreetShopListAdapter adapter;
    private List<ShopModel> shopModels;
    private int count=1;
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
        typeBackGroudLinearLayout= (LinearLayout) v.findViewById(R.id.ll_type_backgroud);
        shopListListView= (PullToRefreshListView) v.findViewById(R.id.list_view_shop_list);
        shopListListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//设置模式是上拉加载
        shopListListView.setOnRefreshListener(new MyOnrefreshListener());


        shopModels =new ArrayList<ShopModel>();
        adapter=new ShopStreetShopListAdapter(getActivity(),shopModels);
        shopListListView.setAdapter(adapter);


        if(!MyNetHelper.isNetworkAvailable()){
            Toast.makeText(context,"网络连接不可用",Toast.LENGTH_SHORT).show();
        }

        location=ALL;
        count=1;
        DialogHelper.showDialog(context);
        tongYongClickChuLi("");

        //设置缓存的代码,设置缓存会造成关注失效
       /* SharedPreferences cachSharePrefrences=context.getSharedPreferences(MyConstant.CACH_SHAREPREFERENCE_NAME,Context.MODE_APPEND);
        String shopStreetCach=cachSharePrefrences.getString(MyConstant.DIAN_PU_JIE_SHOPS_CACH_KEY,null);
        if(shopStreetCach!=null){
            setValueToListView(shopStreetCach);
        }else{
            DialogHelper.showDialog(context);
            StringRequest moWanRequest=new StringRequest(Request.Method.POST, indexUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    DialogHelper.dissmisDialog();
                    setValueToListView(s);
                    writeDataToCach(MyConstant.DIAN_PU_JIE_SHOPS_CACH_KEY,s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<String,String>();
                    String json="{\"page\":\""+1+"\",\"where\":\""+""+"\",\"type\":\"1\"}";
                    map.put("json",json);
                    return map;
                }
            };
            requestQueue.add(moWanRequest);
        }*/


        //设置点击事件
        goodTypeLinearLayout.setOnClickListener(this);


        //点击类型之后弹出窗口数据的初始化
        typeView=inflater.inflate(R.layout.good_type_layout,null);
        typeGridView= (MyGridViewA) typeView.findViewById(R.id.grid_view_good_type);
        resetButton= (Button) typeView.findViewById(R.id.btn_reset);
        confirmButton= (Button) typeView.findViewById(R.id.btn_confirm);
        oneList=new ArrayList<String>();
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

    }

    /**
     * 将json数据写入缓存中
     * @param key ,写入缓存时使用的key
     * @param data
     */
    private void writeDataToCach(String key,String data) {
        SharedPreferences cachSharePrefrences=context.getSharedPreferences(MyConstant.CACH_SHAREPREFERENCE_NAME,Context.MODE_APPEND);
        SharedPreferences.Editor editor=cachSharePrefrences.edit();
        editor.putString(key,data);
        editor.commit();
    }




    /**
     * 主要用于点击，显示第一页的数据
     * @param category
     */
    private void tongYongClickChuLi(String category){
        tongYongClickChuLi(category,1+"");
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
                MyLog.d(tag, "category=" + category + "page=" + page + "s=" + s);
                DialogHelper.dissmisDialog();

                MyLog.d(tag,""+parseJson(s).size()+"...."+""+parseJson(s));
                if(parseJson(s)==null || parseJson(s).size()==0){
                    Toast.makeText(context, "已经是最后一项了", Toast.LENGTH_SHORT).show();
                    shopListListView.onRefreshComplete();//通知刷新完毕
                    shopListListView.setIsHaveMoreData(false);
                }else{
                    setValueToListView(s);
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
     * 给listview设置值
     * @param s
     */
    private void setValueToListView(String s) {

        List<ShopModel> temp = parseJson(s);
        shopModels.addAll(temp);
        adapter.notifyDataSetChanged();
        shopListListView.onRefreshComplete();//通知刷新完毕
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
//                count = 1;//将count设为初始值
//                location=ALL;
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
                //恢复到初始状态,选择任何一个选项，都要先恢复初始状态
                count=1;
                shopModels.clear();

                switch (position) {
                    case ALL:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi("");
                        location = position;
                        break;
                    case SHANG_ZHUANG:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1464 + "");
                        location = position;
                        break;
                    case XIA_ZHUANG:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1479 + "");
                        location = position;
                        break;
                    case XIANG_BAO:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1486 + "");
                        location = position;
                        break;
                    case ZHAI_PIN:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1492 + "");
                        location = position;
                        break;
                    case DIY:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1647 + "");
                        location = position;
                        break;
                    case MO_WAN:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1661 + "");
                        location = position;
                        break;
                    case MAO_RONG:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1501 + "");
                        location = position;
                        break;
                    case PEI_SHI:
                        DialogHelper.showDialog(context);
                        tongYongClickChuLi(1506 + "");
                        location = position;
                        break;
                }
            }
        });


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
               count++;
                switch (location) {
                    case ALL:
                        tongYongClickChuLi("", count + "");
                        break;
                    case MO_WAN:
                        tongYongClickChuLi(1661+"", count + "");
                        break;
                    case DIY:
                        tongYongClickChuLi(1647+"", count + "");
                        break;
                    case SHANG_ZHUANG:
                        tongYongClickChuLi(1464+"", count + "");
                        break;
                    case XIA_ZHUANG:
                        tongYongClickChuLi(1479+"", count + "");
                        break;
                    case XIANG_BAO:
                        tongYongClickChuLi(1486+"", count+ "");
                        break;
                    case ZHAI_PIN:
                        tongYongClickChuLi(1492+"", count + "");
                        break;
                    case MAO_RONG:
                        tongYongClickChuLi(1501+"", count + "");
                        break;
                    case PEI_SHI:
                        tongYongClickChuLi(1506+"", count + "");
                        break;
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        mainActivity.menu.clearIgnoredViews();
//        mainActivity.menu.addIgnoredView(shopListListView);//将listview添加到忽略里面
    }


    @Override
    public void onStop() {
        super.onStop();
//        mainActivity.menu.clearIgnoredViews();
        //mainActivity.menu.removeIgnoredView(shopListListView);
    }
}
