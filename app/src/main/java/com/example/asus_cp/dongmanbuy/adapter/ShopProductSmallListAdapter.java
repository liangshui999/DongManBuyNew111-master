package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.example.asus_cp.dongmanbuy.activity.product_detail.QueHuoDengJiActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺商品以小列表展示
 * Created by asus-cp on 2016-06-12.
 */
public class ShopProductSmallListAdapter  extends BaseAdapter{

    private String tag="ShopProductSmallListAdapter";

    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    private String addToShoppingCarUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/create";//加入购物车
    private String shoppingCarListUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/list";//购物车列表
    private String updateShoppingCarUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/update";//更改购物车的商品数量
    private RequestQueue requestQueue;

    private int shoppingCarGoodCount;//购物车里面的商品数量，等于选中的商品数量乘以购物车的点击次数
    private int shoppingCarClickCount;//购物车的点击次数

    public ShopProductSmallListAdapter(Context context, List<Good> goods) {
        this.context = context;
        this.goods = goods;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();
        requestQueue= MyApplication.getRequestQueue();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.shop_product_list_small,null);
            viewHolder=new ViewHolder();
            viewHolder.picImageView= (ImageView) v.findViewById(R.id.img_product_pic_small_list);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_product_name_list_small);
            viewHolder.kuCunTextView= (TextView) v.findViewById(R.id.text_ku_cun_small_list);
            viewHolder.xiaoLiangTextView= (TextView) v.findViewById(R.id.text_xiao_liang_small_list);
            viewHolder.shopPriceTextView= (TextView) v.findViewById(R.id.text_shop_price_small_list);
            viewHolder.marketPriceTextView= (TextView) v.findViewById(R.id.text_market_price_small_list);
            viewHolder.shoppingCarImageView= (ImageView) v.findViewById(R.id.img_shopping_car_small_list);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        final Good good=goods.get(position);
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.picImageView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
        imageLoader.get(good.getGoodsThumb(), listener, 200, 200);

        viewHolder.nameTextView.setText(good.getGoodName());
        viewHolder.kuCunTextView.setText(good.getGoodsNumber());
        viewHolder.xiaoLiangTextView.setText(good.getSalesVolume());
        viewHolder.shopPriceTextView.setText(FormatHelper.getMoneyFormat(good.getShopPrice()));
        viewHolder.marketPriceTextView.setText(good.getMarket_price());
        viewHolder.marketPriceTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG| Paint.ANTI_ALIAS_FLAG);

        //设置点击事件
        viewHolder.shoppingCarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击了购物车",Toast.LENGTH_SHORT).show();
                //addToShoppingCarChuLi(good);
            }
        });
        return v;
    }

    /**
     * 加入购物车的点击事件处理
     */
    private void addToShoppingCarChuLi(final Good good) {
        //Toast.makeText(this, "加入购物车", Toast.LENGTH_SHORT).show();
        int kuCun=Integer.parseInt(good.getGoodsNumber());
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, context.MODE_APPEND);
        final String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        final String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        if(uid!=null && !uid.isEmpty()){
            if(shoppingCarGoodCount <kuCun){
                shoppingCarClickCount++;
                shoppingCarGoodCount = shoppingCarGoodCount +1;
                if(shoppingCarClickCount==1){//加入购物车
                    StringRequest addToShoppingCarRequest=new StringRequest(Request.Method.POST
                            , addToShoppingCarUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag, "加入购物车返回的数据:" + s);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<String,String>();
                            String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"goods_id\":\""+good.getGoodId()+"\",\"number\":\""+"1"+"\",\"spec\":\"\"}";
                            map.put("json",json);
                            return map;
                        }
                    };
                    requestQueue.add(addToShoppingCarRequest);
                }else if(shoppingCarGoodCount >1){//更改购物车的商品数量
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
                                            String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"rec_id\":\""+ finalRecId +"\",\"new_number\":\""+ shoppingCarGoodCount +"\"}";
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
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("");
                builder.setMessage("对不起，该商品已经库存不足暂停销售。你现在要进行缺货登记来预定该商品吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(uid!=null && !uid.isEmpty()){
                            Intent intent=new Intent(context,QueHuoDengJiActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }else{
                            Intent queHuoIntent=new Intent(context,LoginActivity.class);
                            queHuoIntent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"queHuo");
                            queHuoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(queHuoIntent);
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
            Intent intent=new Intent(context,LoginActivity.class);
            intent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"shoppingCar");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }





    class ViewHolder{
        ImageView picImageView;
        TextView nameTextView;
        TextView kuCunTextView;
        TextView xiaoLiangTextView;
        TextView shopPriceTextView;
        TextView marketPriceTextView;
        ImageView shoppingCarImageView;
    }
}
