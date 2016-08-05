package com.example.asus_cp.dongmanbuy.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.example.asus_cp.dongmanbuy.activity.MainActivity;
import com.example.asus_cp.dongmanbuy.activity.gou_wu.DingDanActivity;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.adapter.MayBeYouWantAdapter;
import com.example.asus_cp.dongmanbuy.adapter.ProductDetailYouHuiQuanListAdapter;
import com.example.asus_cp.dongmanbuy.adapter.TuiJianGoodAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.model.YouHuiQuanModel;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车的内容
 * Created by asus-cp on 2016-05-19.
 */
public class ShoppingCarFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private String tag="ShoppingCarFragment";

    private ImageView daoHangImageView;//导航
    public CheckBox quanXuanCheckBox;//全选
    public TextView priceTextView;//总结算价格
    private LinearLayout editLinearLayout;//编辑按钮
    private LinearLayout jieSuanLinearLayout;//结算的按钮
    public TextView jieSuanShuMuTextView;//结算的数目
    private ListView myListViewOut;//购物车列表
    private MyGridViewA tuiJianProductGridView;//推荐商品列表

    private ShoppingCarListAdapterOut adapterOut;//外部适配器做为全局
    private int ziYingCount;//自营店的选中状态
    private int quanXuanCount;

    private String updateShoppingCarUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/update";//更改购物车的商品数量
    private String shoppingCarListUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/list";//购物车列表
    private String deleteShoppingCarUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/delete";//删除购物车
    private String gooDescUrl="http://www.zmobuy.com/PHP/?url=/goods";//商品详情的接口
    private String shopInfoUrl = "http://www.zmobuy.com/PHP/?url=/store/shopinfo";//店铺详细信息的接口

    private String tuiJianUrl="http://api.zmobuy.com/JK/base/model.php";//推荐商品列表的接口
    private RequestQueue requestQueue;

    private String uid;
    private String sid;

    private SharedPreferences sharedPreferences;

    public static final int REQUEST_CODE_TO_Ding_Dan_Activity=12;

    private View parentView;//所有popu的父布局

    private PopupWindow youHuiQuanWindow;//优惠券的弹出窗口

    private List<ShopModel> shopModels;

    private List<Good> tuiJianGoods;//推荐商品列表

    private MainActivity mainActivity;

    private int densty;//屏幕像素密度

    private ProgressDialog progressDialog;

    //和购物车有内容和无内容相关的控件
    private LinearLayout hasContentLinearLayout;
    private LinearLayout hasNoContentLinearLayout;
    private Button quGuangGuangButton;//去逛一逛
    private RecyclerView mayBeYouWantRecycler;//你可能想要

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.shopping_car_activity_layout,null);
        init(v);
        return v;
    }

    /**
     * 初始化的方法
     */
    private void init(View v) {

        context=getActivity();
        mainActivity= (MainActivity) getActivity();
        densty=mainActivity.getDensty();

        parentView=LayoutInflater.from(context).inflate(R.layout.shopping_car_activity_layout, null);
        daoHangImageView= (ImageView) v.findViewById(R.id.img_shoping_car_activity_dao_hang);
        quanXuanCheckBox= (CheckBox) v.findViewById(R.id.check_box_quan_xuan);
        priceTextView= (TextView) v.findViewById(R.id.text_he_ji_price);
        editLinearLayout= (LinearLayout) v.findViewById(R.id.ll_edit);
        jieSuanLinearLayout= (LinearLayout) v.findViewById(R.id.ll_jie_suan);
        jieSuanShuMuTextView = (TextView) v.findViewById(R.id.text_jie_suan_su_mu);
        myListViewOut = (ListView) v.findViewById(R.id.my_list_view_shopping_car_list_out);
        tuiJianProductGridView= (MyGridViewA) v.findViewById(R.id.my_grid_view_tui_jian_product);
        hasContentLinearLayout= (LinearLayout) v.findViewById(R.id.ll_when_has_content);
        hasNoContentLinearLayout= (LinearLayout) v.findViewById(R.id.ll_has_no_content);
        quGuangGuangButton= (Button) v.findViewById(R.id.btn_qu_guang_guang);
        mayBeYouWantRecycler= (RecyclerView) v.findViewById(R.id.recycle_view_may_be_you_want);

        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        quanXuanCheckBox.setOnClickListener(this);
        editLinearLayout.setOnClickListener(this);
        jieSuanLinearLayout.setOnClickListener(this);
        quGuangGuangButton.setOnClickListener(this);
        tuiJianProductGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context,"点击的位置是："+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra(MyConstant.GOOD_KEY, tuiJianGoods.get(position));
                startActivity(intent);
            }
        });

        requestQueue= MyApplication.getRequestQueue();
        sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, Context.MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        sid = sharedPreferences.getString(MyConstant.SID_KEY, null);



        //显示进度框
        DialogHelper.showDialog(context);
        //获取购物车列表
        getShoppingCarListFromIntetnt();

        //获取推荐商品列表
        getTuiJianGoodList();

    }


    /**
     * 获取推荐商品列表
     */
    private void getTuiJianGoodList() {
        StringRequest tuiJianRequest=new StringRequest(Request.Method.POST, tuiJianUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                tuiJianGoods=parseJsonTuiJian(s);
                TuiJianGoodAdapter adapter=new TuiJianGoodAdapter(context,tuiJianGoods);
                tuiJianProductGridView.setAdapter(adapter);

                //设置布局管理器(recyleview必须设置布局管理器，不然是显示不出来的)
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                mayBeYouWantRecycler.setLayoutManager(linearLayoutManager);
                MayBeYouWantAdapter adapter1=new MayBeYouWantAdapter(context,tuiJianGoods);
                mayBeYouWantRecycler.setAdapter(adapter1);

                //将recyclview添加到忽略里面
                mainActivity.menu.addIgnoredView(mayBeYouWantRecycler);
                adapter1.setOnItemClickLitener(new MayBeYouWantAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent=new Intent(context,ProductDetailActivity.class);
                        intent.putExtra(MyConstant.GOOD_KEY,tuiJianGoods.get(position));
                        startActivity(intent);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                /*service	want*/
                Map<String,String> map=new HashMap<String,String>();
                map.put("service","want");
                return map;
            }
        };
        requestQueue.add(tuiJianRequest);
    }


    /**
     * 解析推荐商品接口返回的数据
     * @param s
     */
    private List<Good> parseJsonTuiJian(String s) {
        MyLog.d(tag, "推荐商品列表返回的数据是：" + s);
        List<Good> goods=new ArrayList<Good>();
        s= FormatHelper.removeBom(s);
        try {
            JSONArray jsonArray=new JSONArray(s);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                Good good=new Good();
                good.setGoodId(jsonObject.getString("goods_id"));
                good.setGoodName(jsonObject.getString("goods_name"));
                good.setShopPrice(jsonObject.getString("shop_price"));
                good.setGoodsThumb(jsonObject.getString("goods_thumb"));
                good.setGoodsImg(jsonObject.getString("goods_img"));
                good.setGoodsSmallImag(jsonObject.getString("original_img"));
                goods.add(good);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goods;
    }

    /**
     * 从网络请求购物车列表的数据，给外层的listview设置适配器
     */
    public void getShoppingCarListFromIntetnt() {
        StringRequest shoppingCarListRequest=new StringRequest(Request.Method.POST, shoppingCarListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogHelper.dissmisDialog();//加载完成之后将进度条关掉
                        MyLog.d(tag, "返回的数据是" + s);
                        shopModels=parseJson(s);
                        if(shopModels.size()>0){    //说明购物车里面有内容
                            hasContentLinearLayout.setVisibility(View.VISIBLE);
                            hasNoContentLinearLayout.setVisibility(View.GONE);
                            int len=0;
                            for(int i=0;i<shopModels.size();i++){
                                ShopModel shopModel=shopModels.get(i);
                                List<Good> goods=shopModel.getGoods();
                                len=len+goods.size()*150*densty/160+40*densty/160;//主要是店铺名称所在的布局要占40，所以给他设置成40
                            }
                            MyLog.d(tag,"计算出的外部listview的高度是"+len);
                            //动态设置外部listview的高度
                            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    len);
                            myListViewOut.setLayoutParams(params);
                            adapterOut=new ShoppingCarListAdapterOut(context,shopModels);
                            myListViewOut.setAdapter(adapterOut);
                        }else{
                            hasContentLinearLayout.setVisibility(View.GONE);
                            hasNoContentLinearLayout.setVisibility(View.VISIBLE);
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
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"}}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(shoppingCarListRequest);
    }

    /**
     * 主要用于解析json数据
     */
    private List<ShopModel> parseJson(String s) {
        List<ShopModel> shopModels=new ArrayList<ShopModel>();

        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONArray jsonArray=jsonObject1.getJSONArray("goods_list");

            for (int i = 0; i < jsonArray.length(); i++) {
                final Good good = new Good();
                JSONObject ziJsonObj = jsonArray.getJSONObject(i);
                good.setRecId(ziJsonObj.getString("rec_id"));
                good.setGoodId(ziJsonObj.getString("goods_id"));
                good.setGoodName(JsonHelper.decodeUnicode(ziJsonObj.getString("goods_name")));
                good.setMarket_price(JsonHelper.decodeUnicode(ziJsonObj.getString("market_price")));
                good.setShopPrice(JsonHelper.decodeUnicode(ziJsonObj.getString("goods_price")));
                good.setShoppingCarNumber(ziJsonObj.getString("goods_number"));
                JSONObject imgJson = ziJsonObj.getJSONObject("img");
                good.setGoodsImg(imgJson.getString("url"));
                good.setGoodsThumb(imgJson.getString("thumb"));
                good.setGoodsSmallImag(imgJson.getString("small"));

                String ruId=ziJsonObj.getString("ru_id");
                if(shopModels.size()==0){
                    ShopModel shopModel=new ShopModel();
                    List<Good> goods=new ArrayList<Good>();
                    shopModel.setGoods(goods);
                    shopModel.getGoods().add(good);
                    shopModel.setUserId(ruId);
                    shopModels.add(shopModel);
                }else{
                    int count=0;
                    for(int j=0;j<shopModels.size();j++){
                        if(ruId.equals(shopModels.get(j).getUserId())){
                            shopModels.get(j).getGoods().add(good);
                            break;
                        }
                        count++;
                    }
                    if(count==shopModels.size()){
                        ShopModel shopModel=new ShopModel();
                        List<Good> goods=new ArrayList<Good>();
                        shopModel.setGoods(goods);
                        shopModel.getGoods().add(good);
                        shopModel.setUserId(ruId);
                        shopModels.add(shopModel);
                    }
                }
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopModels;
    }


/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_TO_LOGIN_ACTIVITY://从登陆界面回来的
                uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
                sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
                getShoppingCarListFromIntetnt();
                break;
        }
    }*/


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.check_box_zi_ying_dian://自营店
                if(goods.size()>0){
                    if(ziYingCount%2==0){
                        adapter.allXuanZhong();
                        adapter.notifyDataSetChanged();
                        quanXuanCheckBox.setChecked(true);
                    }else{
                        adapter.allBuXuanZhong();
                        adapter.notifyDataSetChanged();
                        quanXuanCheckBox.setChecked(false);
                    }
                    ziYingCount++;
                }
                break;*/
            case R.id.check_box_quan_xuan://全选
                if(shopModels.size()>0){
                    if(quanXuanCount%2==0){
                        adapterOut.allXuanZhong();
                        adapterOut.notifyDataSetChanged();
                        //ziYingCheckBox.setChecked(true);

                    }else{
                        adapterOut.allBXuanZhong();
                        adapterOut.notifyDataSetChanged();
                        //ziYingCheckBox.setChecked(false);
                    }
                    quanXuanCount++;
                }
                break;
            case R.id.img_shoping_car_activity_dao_hang://导航
                mainActivity.finish();
                break;
           /* case R.id.text_ling_quan://领券
                //Toast.makeText(this,"领券",Toast.LENGTH_SHORT).show();
                youHuiQuanClickChuLi();
                break;*/
            case R.id.ll_edit://编辑
                Toast.makeText(context,"编辑",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_jie_suan://结算
                /*//Toast.makeText(this,"结算...",Toast.LENGTH_SHORT).show();
                List<Boolean> checks=adapter.getChecksIn();
                ArrayList<Integer> itemProductCounts= (ArrayList<Integer>) adapter.getItemProductCountsIn();//返回小项的商品数目
                ArrayList<Good> passGoods=new ArrayList<Good>();//需要传递给订单界面的
                ArrayList<Integer> passItemProductCount=new ArrayList<Integer>();
                for(int i=0;i<checks.size();i++){
                    if(checks.get(i)){
                        passGoods.add(goods.get(i));
                        passItemProductCount.add(itemProductCounts.get(i));
                    }
                }
                if(passGoods.size()>0){
                    Intent intent=new Intent(context,DingDanActivity.class);
                    intent.putExtra(MyConstant.GOOD_LIST_KEY,passGoods);
                    intent.putExtra(MyConstant.ITEM_PRODUCT_COUNT_KEY,passItemProductCount);
                    intent.putExtra(MyConstant.PRODUCT_PRICE_SUM_KEY,priceTextView.getText().toString());
                    intent.putExtra(MyConstant.PRODUCT_SHU_MU_SUM_KEY,jieSuanShuMuTextView.getText().toString());
                    startActivityForResult(intent, REQUEST_CODE_TO_Ding_Dan_Activity);
                }else{
                    Toast.makeText(context,"请至少选择一个商品",Toast.LENGTH_SHORT).show();
                }*/

                ArrayList<List<Integer>> passItemGoodCount=new ArrayList<List<Integer>>();//需要传递给订单界面的
                List<ShopModel> passShopModel=new ArrayList<ShopModel>();//需要传递给订单界面的

                ArrayList<List<Boolean>> checksOut=adapterOut.getChecksOut();
                ArrayList<List<Integer>> itemGoodsCountOut=adapterOut.getItemGoodsCountOut();
                for(int i=0;i<shopModels.size();i++){
                    List<Boolean> tempCheck=checksOut.get(i);
                    List<Integer> tempItemGoods=itemGoodsCountOut.get(i);
                    ShopModel shopModel=shopModels.get(i);

                    List<Integer> zhongZhuanItemGoods=new ArrayList<Integer>();//中转用的，会将她们添加到pass里面,相当于pass里面的小项
                    ShopModel zhongZhuanShopModel=new ShopModel();
                    List<Good> zhongZhuanGoods=new ArrayList<Good>();
                    for(int j=0;j<tempCheck.size();j++){
                        if(tempCheck.get(j)){
                            zhongZhuanItemGoods.add(tempItemGoods.get(j));
                            zhongZhuanGoods.add(shopModel.getGoods().get(j));
                        }
                    }
                    if(zhongZhuanItemGoods.size()>0){   //说明小项里面有选中的
                        zhongZhuanShopModel.setGoods(zhongZhuanGoods);
                        zhongZhuanShopModel.setUserId(shopModel.getUserId());
                        zhongZhuanShopModel.setShopName(shopModel.getShopName());
                        passItemGoodCount.add(zhongZhuanItemGoods);
                        passShopModel.add(zhongZhuanShopModel);
                    }
                }

                if(passShopModel.size()>0){
                    Intent intent=new Intent(context, DingDanActivity.class);
                    intent.putExtra(MyConstant.SHOP_MODE_LIST_KEY, (Serializable) passShopModel);
                    intent.putExtra(MyConstant.XUAN_ZHONG_COUNT_KEY,passItemGoodCount);
                    intent.putExtra(MyConstant.PRODUCT_PRICE_SUM_KEY,priceTextView.getText().toString());
                    intent.putExtra(MyConstant.PRODUCT_SHU_MU_SUM_KEY,jieSuanShuMuTextView.getText().toString());
                    startActivityForResult(intent, REQUEST_CODE_TO_Ding_Dan_Activity);
                }else{
                    Toast.makeText(context,"请至少选择一个商品",Toast.LENGTH_SHORT).show();
                }
                break;


            //---------------------------------领券的弹出窗口的点击事件---------------------------
            case R.id.img_close_ling_qu_you_hui_quan:
                //Toast.makeText(this,"关闭优惠券",Toast.LENGTH_SHORT).show();
                youHuiQuanWindow.dismiss();
                break;
            //----------------------去逛一逛的点击事件-----------------------
            case R.id.btn_qu_guang_guang://去逛一逛的点击事件
                //Toast.makeText(context,"点击了去逛一逛",Toast.LENGTH_SHORT).show();
                Intent toMainActivityIntent=new Intent(context,MainActivity.class);
                toMainActivityIntent.putExtra(MyConstant.MAIN_ACTIVITY_LABLE_FALG_KEY,MyConstant.HOME_FLAG_KEY);
                startActivity(toMainActivityIntent);
                break;
        }
    }


    /**
     * 领券的点击事件处理
     */
    private void youHuiQuanClickChuLi() {
        //Toast.makeText(this,"点击了优惠券",Toast.LENGTH_SHORT).show();
        View youHuiQuanView = LayoutInflater.from(context).inflate(R.layout.you_hui_quan_layout, null);
        ImageView closeYouHuiQuanImagView = (ImageView) youHuiQuanView.findViewById
                (R.id.img_close_ling_qu_you_hui_quan);
        closeYouHuiQuanImagView.setOnClickListener(this);

        ListView youHuiQuanListView= (ListView) youHuiQuanView.findViewById(R.id.list_you_quan_list_product_detail);
        YouHuiQuanModel model=new YouHuiQuanModel();
        model.setJinE("10");
        model.setUseConditon("99");
        model.setStartTime("2022.00.00");
        model.setEndTime("2050.00.00");
        List<YouHuiQuanModel> youHuiQuanModelList=new ArrayList<YouHuiQuanModel>();
        youHuiQuanModelList.add(model);
        ProductDetailYouHuiQuanListAdapter adapter=new ProductDetailYouHuiQuanListAdapter(context,youHuiQuanModelList);
        youHuiQuanListView.setAdapter(adapter);

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
        WindowManager.LayoutParams lp = mainActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mainActivity.getWindow().setAttributes(lp);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyLog.d(tag, "onActivityResult");
        switch (requestCode){
            case REQUEST_CODE_TO_Ding_Dan_Activity: //从订单界面返回的数据
                getShoppingCarListFromIntetnt();
                priceTextView.setText(0.00 + "");
                jieSuanShuMuTextView.setText("("+"0"+")");
                quanXuanCheckBox.setChecked(false);
                quanXuanCount=0;
                MyLog.d(tag, "onActivityResult内部执行了吗");
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        mainActivity.menu.removeIgnoredView(mayBeYouWantRecycler);
    }

    /**
     * 外部listview的适配器
     */
    public class ShoppingCarListAdapterOut extends BaseAdapter{

        private ArrayList<List<Boolean>> checksOut;//记录选中状态的集合
        private ArrayList<List<Integer>> itemGoodsCountOut;//记录每一小项商品数目的集合
        private ArrayList<Boolean> checksHead;//记录店铺上面选中的集合

        private Context context;
        private List<ShopModel> shopModels;
        private LayoutInflater inflater;

        public ShoppingCarListAdapterOut(Context context, List<ShopModel> shopModels) {
            this.context = context;
            this.shopModels = shopModels;
            inflater=LayoutInflater.from(context);
            initChecksAndCount();
        }

        //初始化
        private void initChecksAndCount() {
            checksOut =new ArrayList<List<Boolean>>();
            itemGoodsCountOut =new ArrayList<List<Integer>>();
            checksHead=new ArrayList<Boolean>();
            for(int i=0;i<shopModels.size();i++){
                List<Boolean> bo=new ArrayList<Boolean>();
                List<Integer> item=new ArrayList<Integer>();
                List<Good> goods=shopModels.get(i).getGoods();
                for(int j=0;j<goods.size();j++){
                    bo.add(false);
                    item.add(Integer.parseInt(goods.get(j).getShoppingCarNumber()));
                }
                checksOut.add(bo);
                itemGoodsCountOut.add(item);
                checksHead.add(false);
            }

        }

        //让checks都选中
        public void allXuanZhong(){
            for(int i=0;i< checksOut.size();i++){
                List<Boolean> check= checksOut.get(i);
                for(int j=0;j<check.size();j++){
                    check.set(j,true);
                }
            }
            for(int j=0;j<checksHead.size();j++){
                checksHead.set(j,true);
            }
        }

        //让checks都不选中
        public void allBXuanZhong(){
            for(int i=0;i< checksOut.size();i++){
                List<Boolean> check= checksOut.get(i);
                for(int j=0;j<check.size();j++){
                    check.set(j,false);
                }
            }
            for(int j=0;j<checksHead.size();j++){
                checksHead.set(j,false);
            }
        }

        public ArrayList<List<Boolean>> getChecksOut() {
            return checksOut;
        }

        public ArrayList<List<Integer>> getItemGoodsCountOut() {
            return itemGoodsCountOut;
        }

        @Override
        public int getCount() {
            return shopModels.size();
        }

        @Override
        public Object getItem(int position) {
            return shopModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v=convertView;
            ViewHolderOut viewHolderOut =null;
            if(v==null){
                v=inflater.inflate(R.layout.shopping_car_list_item_layout_out,null);
                viewHolderOut =new ViewHolderOut();
                viewHolderOut.myListView= (ListView) v.findViewById(R.id.list_shopping_car_item_out);
                viewHolderOut.checkBox= (CheckBox) v.findViewById(R.id.check_box_head);
                viewHolderOut.nameTextView= (TextView) v.findViewById(R.id.text_shop_name_head);
                v.setTag(viewHolderOut);
            }else{
                viewHolderOut = (ViewHolderOut) v.getTag();
            }

            //给checkhead设置选中状态
            viewHolderOut.checkBox.setChecked(checksHead.get(position));

            //给内部的listview设置适配器
            MyLog.d(tag,"densty="+densty);
            //动态设置listview的高度，这个很重要
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    150*densty/160*shopModels.get(position).getGoods().size());
            viewHolderOut.myListView.setLayoutParams(params);
            final ShopModel shopModel=shopModels.get(position);
            final ShoppingCarListAdapterIn adapterIn=new ShoppingCarListAdapterIn(context,shopModel.getGoods(),
                    checksOut.get(position), itemGoodsCountOut.get(position),viewHolderOut.checkBox);
            viewHolderOut.myListView.setAdapter(adapterIn);

            //给店铺名字设置值
            final ViewHolderOut finalViewHolderOut = viewHolderOut;
            setDianPuName(shopModel, finalViewHolderOut);

            //给checkbox设置点击事件
            viewHolderOut.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!checksHead.get(position)) { //选中
                        adapterIn.allXuanZhong();
                        adapterIn.notifyDataSetChanged();
                        checksHead.set(position,true);
                    } else {
                        adapterIn.allBuXuanZhong();
                        adapterIn.notifyDataSetChanged();
                        checksHead.set(position,false);
                    }

                }
            });
            return v;
        }


        /**
         * 给店铺名称设置值
         * @param shopModel
         * @param finalViewHolderOut
         */
        private void setDianPuName(final ShopModel shopModel, final ViewHolderOut finalViewHolderOut) {
            StringRequest getShopInfoRequest = new StringRequest(Request.Method.POST, shopInfoUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //MyLog.d(tag, "求取店铺名称返回的数据是：" + s);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String shopName = jsonObject1.getString("shop_name");
                        String ruId = jsonObject1.getString("ru_id");
                        MyLog.d(tag, "ruid=" + ruId);
                        if ("0".equals(shopModel.getUserId())) {
                            finalViewHolderOut.nameTextView.setText("周末自营");
                            shopModel.setShopName("周末自营");
                            //MyLog.d(tag, "周末自营执行了吗");
                        } else {
                            //给textview设置店铺名称
                            finalViewHolderOut.nameTextView.setText(shopName);
                            shopModel.setShopName(shopName);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //这个地方的设置才是关键
                    if ("0".equals(shopModel.getUserId())) {
                        finalViewHolderOut.nameTextView.setText("周末自营");
                        shopModel.setShopName("周末自营");
                        //MyLog.d(tag, "周末自营执行了吗");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    String json = "{\"id\":\"" + shopModel.getUserId() + "\"}";
                    map.put("json", json);
                    return map;
                }
            };
            requestQueue.add(getShopInfoRequest);

            if(shopModel.getUserId().equals("0")){
                finalViewHolderOut.nameTextView.setText("周末自营");
            }
        }

        class ViewHolderOut {
            ListView myListView;
            CheckBox checkBox;
            TextView nameTextView;
        }





        /**
         * 购物车列表的适配器(内部listview的适配器)
         * Created by asus-cp on 2016-06-13.
         */
        public class ShoppingCarListAdapterIn extends BaseAdapter implements View.OnClickListener{
            private String tag="ShoppingCarListAdapterIn";
            private Context context;
            private List<Good> goods;
            private LayoutInflater inflater;
            private ImageLoadHelper helper;

            private List<Boolean> checksIn;//记录选中状态的集合
            private double heJi;//合计的值
            private int jieSuan;//结算数目的值

            private CheckBox checkBoxOut;//外部listview上面的checkbox

            private List<Integer> itemProductCountsIn;//记录每一个item在购物车中的数量

            public ShoppingCarListAdapterIn(Context context, List<Good> goods,List<Boolean> checksIn,
                                            List<Integer> itemProductCountsIn,CheckBox checkBoxOut) {
                this.context = context;
                this.goods = goods;
                inflater = LayoutInflater.from(context);
                helper = new ImageLoadHelper();
                this.checksIn = checksIn;
                this.itemProductCountsIn = itemProductCountsIn;
                this.checkBoxOut=checkBoxOut;
                //init();
            }

            /**
             *返回记录选中状态的集合
             */
            public List<Boolean> getChecksIn() {
                return checksIn;
            }

            /**
             *返回每一个小项的商品数目
             */
            public List<Integer> getItemProductCountsIn() {
                return itemProductCountsIn;
            }

            /**
             * 都选中
             */
            public void allXuanZhong(){
                for(int i=0;i<checksIn.size();i++){
                    checksIn.set(i, true);
                    MyLog.d(tag, "all选中执行了吗？");
                    MyLog.d(tag,"all选中"+checksIn.get(i));
                }
            }

            /**
             * 都不选中
             */
            public void allBuXuanZhong(){
                for(int i=0;i<checksIn.size();i++){
                    checksIn.set(i, false);
                }
            }


            /**
             * 初始化checks
             */
            public void init(){
                checksIn.clear();
                itemProductCountsIn.clear();
                for(int i=0;i<goods.size();i++){
                    checksIn.add(false);
                    itemProductCountsIn.add(Integer.parseInt(goods.get(i).getShoppingCarNumber()));
                }

                MyLog.d(tag,"init执行了");
            }


            @Override
            public int getCount() {
                return goods.size();
            }

            @Override
            public Object getItem(int position) {
                return goods.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v=convertView;
                ViewHolderIn viewHolderIn =null;
                if(v==null){
                    v=inflater.inflate(R.layout.shopping_car_list_item_layout_in,null);
                    viewHolderIn =new ViewHolderIn();
                    viewHolderIn.checkBox= (CheckBox) v.findViewById(R.id.check_box_shopping_car_list_item);
                    viewHolderIn.picImageView= (ImageView) v.findViewById(R.id.img_pic_shopping_car_list);
                    viewHolderIn.nameTextView= (TextView) v.findViewById(R.id.text_product_name_shopping_car_list);
                    viewHolderIn.priceTextView= (TextView) v.findViewById(R.id.text_product_price_shopping_car_list);
                    viewHolderIn.chiMaTextView= (TextView) v.findViewById(R.id.text_product_chi_ma_shopping_car_list);
                    viewHolderIn.jianHaoImageView= (ImageView) v.findViewById(R.id.img_jian_hao_shopping_car_list);
                    viewHolderIn.productCountTextView= (TextView) v.findViewById(R.id.text_product_count_jia_jian_shopping_car_list);
                    viewHolderIn.jiaHaoImageView= (ImageView) v.findViewById(R.id.img_jia_hao_shopping_car_list);
                    viewHolderIn.deleteImageView= (ImageView) v.findViewById(R.id.img_delete_shopping_car_list);
                    v.setTag(viewHolderIn);
                }else{
                    viewHolderIn = (ViewHolderIn) v.getTag();
                }

                final Good good=goods.get(position);
                final int[] prodcutCount = {Integer.parseInt(good.getShoppingCarNumber())};//加号和减号公用的商品数量,不能放在全局里面，也不能放在onclicklistener厘米那

                //给控件设置值
                ImageLoader imageLoader=helper.getImageLoader();
                ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolderIn.picImageView,
                        R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
                imageLoader.get(good.getGoodsSmallImag(), listener);
                viewHolderIn.nameTextView.setText(good.getGoodName());
                viewHolderIn.priceTextView.setText(FormatHelper.getMoneyFormat(good.getShopPrice()));
                if(checksIn.size()>0){
                    viewHolderIn.checkBox.setChecked(checksIn.get(position));//设置选择框的选中状态
                    viewHolderIn.productCountTextView.setText(itemProductCountsIn.get(position) + "");
                    MyLog.d(tag,"checksIn的选中状态："+checksIn.get(position)+"......."+"position="+position);
                }

                //未点击时，也需要设置，在adapter外面弄全选的时候需要用到
                getCheckStateAndSetSumPriceAndJieSuanShuMu();

                //给checkBox设置点击事件
                viewHolderIn.checkBox.setOnClickListener(new View.OnClickListener() {
                   // private int count;//点击选择框的次数,不是同一个checkbox时，count的计数是不一样的
                    @Override
                    public void onClick(View v) {
                        //MyLog.d(tag,"checkbox的点击事件执行了吗?");
                        if (!checksIn.get(position)) {  //非选中状态，就让他选中
                            checksIn.set(position, true);
                        } else {
                            checksIn.set(position, false);
                        }
                        getCheckStateAndSetSumPriceAndJieSuanShuMu();
                        //MyLog.d(tag,"checkbox中的count"+count);
                    }
                });


                final ViewHolderIn finalViewHolderIn = viewHolderIn;

                //加号的点击事件
            viewHolderIn.jiaHaoImageView.setOnClickListener(new View.OnClickListener() {
                //private int kuCun=Integer.parseInt(good.getGoodsNumber());;
                @Override
                public void onClick(View v) {
                    prodcutCount[0]++;
                    /*if(prodcutCount[0] <= kuCun){
                        Toast.makeText(context,"库存数量:"+kuCun,Toast.LENGTH_SHORT).show();
                        finalViewHolderIn.productCountTextView.setText(prodcutCount[0] +"");
                    }else{
                        Toast.makeText(context,"超过了库存数量",Toast.LENGTH_SHORT).show();
                        prodcutCount[0]=kuCun;
                        finalViewHolderIn.productCountTextView.setText(kuCun + "");
                    }*/
//                    itemProductCountsIn.set(position,prodcutCount[0]);
//                    getCheckStateAndSetSumPriceAndJieSuanShuMu();
                    updateShoppingCar(good,prodcutCount[0]+"",finalViewHolderIn.productCountTextView,position);
                }
            });



                //减号的点击事件
                viewHolderIn.jianHaoImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prodcutCount[0]--;
                        if(prodcutCount[0]>= 1){
                            //Toast.makeText(context,"商品数量:"+prodcutCount[0],Toast.LENGTH_SHORT).show();
                            finalViewHolderIn.productCountTextView.setText(prodcutCount[0]+"");
                        }else{
                            Toast.makeText(context,"不能比1小",Toast.LENGTH_SHORT).show();
                            finalViewHolderIn.productCountTextView.setText("1");
                            prodcutCount[0]=1;
                        }
                        itemProductCountsIn.set(position, prodcutCount[0]);
                        getCheckStateAndSetSumPriceAndJieSuanShuMu();
                        updateShoppingCar(good, prodcutCount[0] + "",finalViewHolderIn.productCountTextView,position);
                    }
                });




                //删除的点击事件
                viewHolderIn.deleteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goods.remove(position);
                        notifyDataSetChanged();
                        checksIn.remove(position);
                        itemProductCountsIn.remove(position);
                        getCheckStateAndSetSumPriceAndJieSuanShuMu();

                        StringRequest deleteRequest=new StringRequest(Request.Method.POST, deleteShoppingCarUrl,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        MyLog.d(tag,"删除返回的数据是："+s);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map=new HashMap<String,String>();
                                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"rec_id\":\""+good.getRecId()+"\"}";
                                map.put("json",json);
                                return map;
                            }
                        };
                        requestQueue.add(deleteRequest);
                    }
                });


                //图片的点击事件
                viewHolderIn.picImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, ProductDetailActivity.class);
                        intent.putExtra(MyConstant.GOOD_KEY,goods.get(position));
                        startActivity(intent);
                    }
                });

                viewHolderIn.nameTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, ProductDetailActivity.class);
                        intent.putExtra(MyConstant.GOOD_KEY,goods.get(position));
                        startActivity(intent);
                    }
                });

                return v;
            }


            /**
             * 更改购物车商品数量
             */
            public void updateShoppingCar(final Good good, final String shoppingCarCount, final TextView textView, final int position){
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
                                                try {
                                                    JSONObject jsonObject=new JSONObject(s);
                                                    JSONObject jsonObject1=jsonObject.getJSONObject("status");
                                                    String succed=jsonObject1.getString("succeed");
                                                    if("1".equals(succed)){ //说明添加成功了
                                                        itemProductCountsIn.set(position,Integer.parseInt(shoppingCarCount));
                                                        getCheckStateAndSetSumPriceAndJieSuanShuMu();
                                                        textView.setText(shoppingCarCount);
                                                    }else if("0".equals(succed)){
                                                        String error=JsonHelper.decodeUnicode(jsonObject1.getString("error_desc"));
                                                        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
//                                                        itemProductCountsIn.set(position, Integer.parseInt(shoppingCarCount) - 1);
//                                                        getCheckStateAndSetSumPriceAndJieSuanShuMu();
//                                                        textView.setText((Integer.parseInt(shoppingCarCount) - 1)+"");
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

            /**
             * 从集合中取出check状态,每次改完数据之后都会重新调用该方法
             */
            private void getCheckStateAndSetSumPriceAndJieSuanShuMu() {
                int count=0;//记录check数目的
                heJi=0;//注意这里需要清零
                jieSuan=0;//注意这里需要清零
                for(int i=0;i< checksIn.size();i++){
                    if(checksIn.get(i)){
//                        int productCount= itemProductCountsIn.get(i);
//                        heJi=heJi+Double.parseDouble(FormatHelper.getNumberFromRenMingBi(goods.get(i).getShopPrice()))*productCount;
//                        jieSuan = jieSuan+productCount;
                        count++;
                    }
                }
                for(int i=0;i<checksOut.size();i++){
                    List<Boolean> tempCheck=checksOut.get(i);
                    List<Integer> tempGoodsCount=itemGoodsCountOut.get(i);
                    List<Good> tempGoods=shopModels.get(i).getGoods();
                    for(int j=0;j<tempCheck.size();j++){
                        if(tempCheck.get(j)){
                            int productCount= tempGoodsCount.get(j);
                            heJi=heJi+Double.parseDouble(FormatHelper.getNumberFromRenMingBi(tempGoods.get(j).getShopPrice()))*productCount;
                            jieSuan = jieSuan+productCount;
                        }
                    }
                }
                priceTextView.setText(FormatHelper.getMoneyFormat(heJi + ""));
                jieSuanShuMuTextView.setText("(" + jieSuan + ")");

                MyLog.d(tag, "checksIn的大小:" + checksIn.size());
                MyLog.d(tag,"count="+count);

                if(count== checksIn.size()){
                    //ziYingCheckBox.setChecked(true);
                    //quanXuanCheckBox.setChecked(true);
                    checkBoxOut.setChecked(true);
                }else {
                    //ziYingCheckBox.setChecked(false);
                    //quanXuanCheckBox.setChecked(false);
                    checkBoxOut.setChecked(false);
                }
            }



            /**
             * 从集合中取出check状态,每次改完数据之后都会重新调用该方法(此方法作废)
             */
            private void getCheckStateAndSetSumPriceAndJieSuanShuMu(int position) {
                int count=0;//记录check数目的
                heJi=0;//注意这里需要清零
                jieSuan=0;//注意这里需要清零
                for(int i=0;i< checksIn.size();i++){
                    if(checksIn.get(i)){
                        int productCount= itemProductCountsIn.get(i);
                        heJi=heJi+Double.parseDouble(FormatHelper.getNumberFromRenMingBi(goods.get(i).getShopPrice()))*productCount;
                        jieSuan = jieSuan+productCount;
                        count++;
                    }
                }
                priceTextView.setText(FormatHelper.getMoneyFormat(heJi + ""));
                jieSuanShuMuTextView.setText("("+jieSuan + ")");

                View v=adapterOut.getView(position,null,null);
                CheckBox checkBox= (CheckBox) v.findViewById(R.id.check_box_head);

                if(count== checksIn.size()){
                    //ziYingCheckBox.setChecked(true);
                    //quanXuanCheckBox.setChecked(true);
                    checkBox.setChecked(true);
                }else {
                    //ziYingCheckBox.setChecked(false);
                    //quanXuanCheckBox.setChecked(false);
                    checkBox.setChecked(false);
                }
            }



            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.img_pic_shopping_car_list://点击了图片
                        Toast.makeText(context, "点击了图片", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.text_product_name_shopping_car_list://点击了名字
                        Toast.makeText(context,"点击了名字",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.img_delete_shopping_car_list://点击了删除
                        Toast.makeText(context,"点击了删除",Toast.LENGTH_SHORT).show();

                        break;
                }
            }

            class ViewHolderIn {
                CheckBox checkBox;
                ImageView picImageView;
                TextView nameTextView;
                TextView priceTextView;
                TextView chiMaTextView;
                ImageView jianHaoImageView;
                TextView productCountTextView;
                ImageView jiaHaoImageView;
                ImageView deleteImageView;
            }
        }





    }








}
