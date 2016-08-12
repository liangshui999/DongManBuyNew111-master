package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.MainActivity;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺商品以大列表展示
 * Created by asus-cp on 2016-06-12.
 */
public class ShopProductBigListAdapter extends BaseAdapter{

    private String tag="ShopProductBigListAdapter";

    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    private String addToShoppingCarUrl="http://www.zmobuy.com/PHP/index.php?url=/cart/create";//加入购物车
    private RequestQueue requestQueue;

    public ShopProductBigListAdapter(Context context, List<Good> goods) {
        this.context = context;
        requestQueue= MyApplication.getRequestQueue();
        this.goods = goods;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();
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
            v=inflater.inflate(R.layout.shop_product_list_big,null);
            viewHolder=new ViewHolder();
            viewHolder.picImagView= (ImageView) v.findViewById(R.id.img_shop_product_pic_big_list);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_shop_product_name_big_list);
            viewHolder.shopPriceTextView= (TextView) v.findViewById(R.id.text_shop_product_price_big_list);
            viewHolder.marketPriceTextView= (TextView) v.findViewById(R.id.text_shop_product_market_price_big_list);
            viewHolder.shoppingCarImageView= (ImageView) v.findViewById(R.id.img_shopping_car_big_list);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        final Good good=goods.get(position);
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.picImagView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
        imageLoader.get(good.getGoodsThumb(),listener,200,200);

        viewHolder.nameTextView.setText(good.getGoodName());
        viewHolder.shopPriceTextView.setText(FormatHelper.getMoneyFormat(good.getShopPrice()));
        viewHolder.marketPriceTextView.setText(FormatHelper.getMoneyFormat(good.getMarket_price()));
        viewHolder.marketPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        viewHolder.shoppingCarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"点击了购物车",Toast.LENGTH_SHORT).show();
                buyAtOnceClickChuLi(good);
            }
        });
        return v;
    }


    /**
     * 立即购买的点击事件处理
     */
    public void buyAtOnceClickChuLi(final Good good){
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, Context.MODE_APPEND);
        final String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        final String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        if(uid!=null && !uid.isEmpty()){
            addToShoppingCarToIntenetBuyAtOnce(uid,sid,good);
        }else{//用户未登录，跳转到登陆界面
            Intent intent=new Intent(context,LoginActivity.class);
            intent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY,"shoppingCar");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //goodSearchResultActivity.startActivityForResult(intent,REQUEST_CODE_GOOD_SEARCH_SMALL_LIST_KEY);
            context.startActivity(intent);
        }
    }


    /**
     * 添加商品上传到服务器
     * @param uid
     * @param sid
     */
    private void addToShoppingCarToIntenetBuyAtOnce(final String uid, final String sid, final Good good) {
        StringRequest addToShoppingCarRequest=new StringRequest(Request.Method.POST
                , addToShoppingCarUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag, "加入购物车返回的数据:" + s);
                toShoppingCarList(good);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"goods_id\":\""+good.getGoodId()+"\",\"number\":\""+ 1 +"\",\"spec\":\"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(addToShoppingCarRequest);
    }


    /**
     * 跳转到购物车列表的界面
     */
    private void toShoppingCarList(Good good) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, Context.MODE_APPEND);
        String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        if(uid!=null && !uid.isEmpty()) {
            Intent shoppingCatIntent = new Intent(context, MainActivity.class);
            shoppingCatIntent.putExtra(MyConstant.KU_CUN_KEY, good.getGoodsNumber());
            shoppingCatIntent.putExtra(MyConstant.MAIN_ACTIVITY_LABLE_FALG_KEY, MyConstant.SHOPPING_CAR_FLAG_KEY);
            shoppingCatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(shoppingCatIntent);
        }
    }


    class ViewHolder{
        ImageView picImagView;
        TextView nameTextView;
        TextView shopPriceTextView;
        TextView marketPriceTextView;
        ImageView shoppingCarImageView;
    }
}
