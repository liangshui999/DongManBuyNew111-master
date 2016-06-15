package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.customview.MyListView;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.JsonHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 有商品之后的购物车
 * Created by asus-cp on 2016-06-06.
 */
public class ShoppingCarActivity extends Activity implements View.OnClickListener{

    private String tag="ShoppingCarActivity";

    private ImageView daoHangImageView;//导航
    public CheckBox ziYingCheckBox;//自营店的chekcbox
    private TextView lingQuanTextView;//领券
    public CheckBox quanXuanCheckBox;//全选
    public TextView priceTextView;//总结算价格
    private LinearLayout editLinearLayout;//编辑按钮
    private LinearLayout jieSuanLinearLayout;//结算的按钮
    public TextView jieSuanShuMuTextView;//结算的数目
    private MyListView myListView;//购物车列表
    private MyGridViewA tuiJianProductGridView;//推荐商品列表

    private ShoppingCarListAdapter adapter;
    private int ziYingCount;//自营店的选中状态
    private int quanXuanCount;

    private String updateShoppingCarUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/update";//更改购物车的商品数量
    private String shoppingCarListUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/list";//购物车列表
    private String deleteShoppingCarUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/delete";//删除购物车
    private String gooDescUrl="http://www.zmobuy.com/PHP/?url=/goods";//商品详情的接口
    private RequestQueue requestQueue;

    private String uid;
    private String sid;

    private SharedPreferences sharedPreferences;

    public static final int REQUEST_CODE_TO_LOGIN_ACTIVITY=1;


    private View parentView;//所有popu的父布局

    private PopupWindow youHuiQuanWindow;//优惠券的弹出窗口

    private int kuCunNumBer;//库存数量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shopping_car_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        parentView=LayoutInflater.from(this).inflate(R.layout.shopping_car_activity_layout,null);
        daoHangImageView= (ImageView) findViewById(R.id.img_shoping_car_activity_dao_hang);
        ziYingCheckBox= (CheckBox) findViewById(R.id.check_box_zi_ying_dian);
        lingQuanTextView= (TextView) findViewById(R.id.text_ling_quan);
        quanXuanCheckBox= (CheckBox) findViewById(R.id.check_box_quan_xuan);
        priceTextView= (TextView) findViewById(R.id.text_he_ji_price);
        editLinearLayout= (LinearLayout) findViewById(R.id.ll_edit);
        jieSuanLinearLayout= (LinearLayout) findViewById(R.id.ll_jie_suan);
        jieSuanShuMuTextView = (TextView) findViewById(R.id.text_jie_suan_su_mu);
        myListView= (MyListView) findViewById(R.id.my_list_view_shopping_car_list);
        tuiJianProductGridView= (MyGridViewA) findViewById(R.id.my_grid_view_tui_jian_product);

        //设置点击事件
        daoHangImageView.setOnClickListener(this);
        ziYingCheckBox.setOnClickListener(this);
        quanXuanCheckBox.setOnClickListener(this);
        lingQuanTextView.setOnClickListener(this);
        editLinearLayout.setOnClickListener(this);
        jieSuanLinearLayout.setOnClickListener(this);



        requestQueue= MyApplication.getRequestQueue();
        sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        if(uid!=null && !uid.isEmpty()){//说明已经登陆了
            sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
            getShoppingCarListFromIntetnt();
        }else{
            Intent toLoginActivityIntent=new Intent(this, LoginActivity.class);
            toLoginActivityIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"shoppingCarActivity");
            startActivityForResult(toLoginActivityIntent, REQUEST_CODE_TO_LOGIN_ACTIVITY);
        }
    }

    /**
     * 从网络请求购物车列表的数据
     */
    private void getShoppingCarListFromIntetnt() {
        StringRequest shoppingCarListRequest=new StringRequest(Request.Method.POST, shoppingCarListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag,"返回的数据是"+s);
                        List<Good> goods = parseJson(s);
//                        adapter=new ShoppingCarListAdapter(ShoppingCarActivity.this,
//                                goods);
//                        myListView.setAdapter(adapter);
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
    private List<Good> parseJson(String s) {
        final List<Good> goods=new ArrayList<Good>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONArray jsonArray=jsonObject1.getJSONArray("goods_list");
            for(int i=0;i<jsonArray.length();i++){
                final Good good=new Good();
                JSONObject ziJsonObj=jsonArray.getJSONObject(i);
                good.setRecId(ziJsonObj.getString("rec_id"));
                good.setGoodId(ziJsonObj.getString("goods_id"));
                good.setGoodName(JsonHelper.decodeUnicode(ziJsonObj.getString("goods_name")));
                good.setMarket_price(JsonHelper.decodeUnicode(ziJsonObj.getString("market_price")));
                good.setShopPrice(JsonHelper.decodeUnicode(ziJsonObj.getString("goods_price")));
                good.setShoppingCarNumber(ziJsonObj.getString("goods_number"));
                JSONObject imgJson=ziJsonObj.getJSONObject("img");
                good.setGoodsImg(imgJson.getString("url"));
                good.setGoodsThumb(imgJson.getString("thumb"));
                good.setGoodsSmallImag(imgJson.getString("small"));

                StringRequest kuCunRequest=new StringRequest(Request.Method.POST, gooDescUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag,"请求库存返回的数据"+s);
                        try {
                            JSONObject kuCunJs=new JSONObject(s);
                            JSONObject ku=kuCunJs.getJSONObject("data");
                            good.setGoodsNumber(ku.getString("goods_number"));
                            goods.add(good);

                            adapter=new ShoppingCarListAdapter(ShoppingCarActivity.this,
                                    goods);
                            myListView.setAdapter(adapter);
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
                        String json="{\"goods_id\":\""+good.getGoodId()+"\",\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"}}";
                        map.put("json",json);
                        return map;
                    }
                };
                requestQueue.add(kuCunRequest);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goods;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_TO_LOGIN_ACTIVITY://从登陆界面回来的
                uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
                sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
                getShoppingCarListFromIntetnt();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_box_zi_ying_dian://自营店
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
                break;
            case R.id.check_box_quan_xuan://全选
                if(quanXuanCount%2==0){
                    adapter.allXuanZhong();
                    adapter.notifyDataSetChanged();
                    ziYingCheckBox.setChecked(true);
                }else{
                    adapter.allBuXuanZhong();
                    adapter.notifyDataSetChanged();
                    ziYingCheckBox.setChecked(false);
                }
                quanXuanCount++;
                break;
            case R.id.img_shoping_car_activity_dao_hang://导航
                finish();
                break;
            case R.id.text_ling_quan://领券
                //Toast.makeText(this,"领券",Toast.LENGTH_SHORT).show();
                youHuiQuanClickChuLi();
                break;
            case R.id.ll_edit://编辑
                Toast.makeText(this,"编辑",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_jie_suan://结算
                //Toast.makeText(this,"结算...",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ShoppingCarActivity.this,DingDanActivity.class);
                startActivity(intent);
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
        }
    }


    /**
     * 优惠券的点击事件处理
     */
    private void youHuiQuanClickChuLi() {
        //Toast.makeText(this,"点击了优惠券",Toast.LENGTH_SHORT).show();
        View youHuiQuanView = LayoutInflater.from(this).inflate(R.layout.you_hui_quan_layout, null);
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
     * 购物车列表的适配器
     * Created by asus-cp on 2016-06-13.
     */
    public class ShoppingCarListAdapter extends BaseAdapter implements View.OnClickListener{
        private String tag="ShoppingCarListAdapter";
        private Context context;
        private List<Good> goods;
        private LayoutInflater inflater;
        private ImageLoadHelper helper;

        private List<Boolean> checks;//记录选中状态的集合
        private double heJi;//合计的值
        private int jieSuan;//结算数目的值

        private List<Integer> itemProductCounts;//记录每一个item在购物车中的数量

        public ShoppingCarListAdapter(Context context, List<Good> goods
                                      ) {
            this.context = context;
            this.goods = goods;
            inflater=LayoutInflater.from(context);
            helper=new ImageLoadHelper();
            init();
        }

        /**
         *返回记录选中状态的集合
         */
        public List<Boolean> getChecks() {
            return checks;
        }

        /**
         * 都选中
         */
        public void allXuanZhong(){
            for(int i=0;i<goods.size();i++){
                checks.set(i,true);
            }
        }

        /**
         * 都不选中
         */
        public void allBuXuanZhong(){
            for(int i=0;i<goods.size();i++){
                checks.set(i, false);
            }
        }


        /**
         * 初始化checks
         */
        public void init(){
            checks=new ArrayList<Boolean>();
            itemProductCounts=new ArrayList<Integer>();
            for(int i=0;i<goods.size();i++){
                checks.add(false);
                itemProductCounts.add(Integer.parseInt(goods.get(i).getShoppingCarNumber()));
            }
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
            ViewHolder viewHolder=null;
            if(v==null){
                v=inflater.inflate(R.layout.shopping_car_list_item_layout,null);
                viewHolder=new ViewHolder();
                viewHolder.checkBox= (CheckBox) v.findViewById(R.id.check_box_shopping_car_list_item);
                viewHolder.picImageView= (ImageView) v.findViewById(R.id.img_pic_shopping_car_list);
                viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_product_name_shopping_car_list);
                viewHolder.priceTextView= (TextView) v.findViewById(R.id.text_product_price_shopping_car_list);
                viewHolder.chiMaTextView= (TextView) v.findViewById(R.id.text_product_chi_ma_shopping_car_list);
                viewHolder.jianHaoImageView= (ImageView) v.findViewById(R.id.img_jian_hao_shopping_car_list);
                viewHolder.productCountTextView= (TextView) v.findViewById(R.id.text_product_count_jia_jian_shopping_car_list);
                viewHolder.jiaHaoImageView= (ImageView) v.findViewById(R.id.img_jia_hao_shopping_car_list);
                viewHolder.deleteImageView= (ImageView) v.findViewById(R.id.img_delete_shopping_car_list);
                v.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) v.getTag();
            }

            final Good good=goods.get(position);
            final int[] prodcutCount = {Integer.parseInt(good.getShoppingCarNumber())};//加号和减号公用的商品数量,不能放在全局里面，也不能放在onclicklistener厘米那

            //给控件设置值
            ImageLoader imageLoader=helper.getImageLoader();
            ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.picImageView,
                    R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
            imageLoader.get(good.getGoodsImg(), listener, 200, 200);
            viewHolder.nameTextView.setText(good.getGoodName());
            viewHolder.priceTextView.setText(FormatHelper.getMoneyFormat(good.getShopPrice()));
            viewHolder.checkBox.setChecked(checks.get(position));//设置选择框的选中状态
            viewHolder.productCountTextView.setText(good.getShoppingCarNumber());

            //未点击时，也需要设置，在adapter外面弄全选的时候需要用到
            getCheckStateAndSetTextView();

            //给checkBox设置点击事件
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                private int count;//点击选择框的次数,不是同一个checkbox时，count的计数是不一样的
                @Override
                public void onClick(View v) {
                    if (count % 2 == 0) {
                        checks.set(position, true);
                    } else {
                        checks.set(position, false);
                    }
                    getCheckStateAndSetTextView();
                    count++;
                }
            });


            final ViewHolder finalViewHolder=viewHolder;

            //加号的点击事件
            viewHolder.jiaHaoImageView.setOnClickListener(new View.OnClickListener() {

                private int kuCun=Integer.parseInt(good.getGoodsNumber());;
                @Override
                public void onClick(View v) {
                prodcutCount[0]++;
                if(prodcutCount[0] <= kuCun){
                    Toast.makeText(context,"库存数量:"+kuCun,Toast.LENGTH_SHORT).show();
                    finalViewHolder.productCountTextView.setText(prodcutCount[0] +"");
                }else{
                    Toast.makeText(context,"超过了库存数量",Toast.LENGTH_SHORT).show();
                    prodcutCount[0]=kuCun;
                    finalViewHolder.productCountTextView.setText(kuCun + "");
                }
                    itemProductCounts.set(position,prodcutCount[0]);
                    getCheckStateAndSetTextView();
                    updateShoppingCar(good,prodcutCount[0]+"");
                }
            });


            //减号的点击事件
            viewHolder.jianHaoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prodcutCount[0]--;
                    if(prodcutCount[0]>= 1){
                        Toast.makeText(context,"商品数量:"+prodcutCount[0],Toast.LENGTH_SHORT).show();
                        finalViewHolder.productCountTextView.setText(prodcutCount[0]+"");
                    }else{
                        Toast.makeText(context,"不能比1小",Toast.LENGTH_SHORT).show();
                        finalViewHolder.productCountTextView.setText("1");
                        prodcutCount[0]=1;
                    }
                    itemProductCounts.set(position,prodcutCount[0]);
                    getCheckStateAndSetTextView();
                    updateShoppingCar(good, prodcutCount[0] + "");
                }
            });




            //删除的点击事件
            viewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goods.remove(position);
                    notifyDataSetChanged();
                    checks.remove(position);
                    itemProductCounts.remove(position);
                    getCheckStateAndSetTextView();

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


        viewHolder.picImageView.setOnClickListener(this);
        viewHolder.nameTextView.setOnClickListener(this);
            //viewHolder.deleteImageView.setOnClickListener(this);
            return v;
        }


        /**
         * 更改购物车商品数量
         */
        public void updateShoppingCar(final Good good, final String shoppingCarCount){
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

        /**
         * 从集合中取出check状态,每次改完数据之后都会重新调用该方法
         */
        private void getCheckStateAndSetTextView() {
            int count=0;//记录check数目的
            heJi=0;//注意这里需要清零
            jieSuan=0;//注意这里需要清零
            for(int i=0;i<checks.size();i++){
                if(checks.get(i)){
                    int productCount=itemProductCounts.get(i);
                    heJi=heJi+Double.parseDouble(FormatHelper.getNumberFromRenMingBi(goods.get(i).getShopPrice()))*productCount;
                    jieSuan = jieSuan+productCount;
                    count++;
                }
            }
            priceTextView.setText(heJi+"");
            jieSuanShuMuTextView.setText(jieSuan + "");

            if(count==checks.size()){
                ziYingCheckBox.setChecked(true);
                quanXuanCheckBox.setChecked(true);
            }else {
                ziYingCheckBox.setChecked(false);
                quanXuanCheckBox.setChecked(false);
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

        class ViewHolder{
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
