package com.example.asus_cp.dongmanbuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.asus_cp.dongmanbuy.activity.dian_pu_jie.ShopHomeActivity;
import com.example.asus_cp.dongmanbuy.activity.login.LoginActivity;
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
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
 * 店铺列表的adapter
 * Created by asus-cp on 2016-06-07.
 */
public class ShopStreetShopListAdapter extends BaseAdapter {
    private String tag="ShopStreetShopListAdapter";
    private Context context;
    private List<ShopModel> shopModels;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;
    private AlertDialog loginDialog;//登陆的对话框

    private String shopInfoUrl="http://www.zmobuy.com/PHP/?url=/store/shopinfo";//店铺详细信息的接口

    private String guanZhuUrl="http://www.zmobuy.com/PHP/?url=/store/addcollect";//关注的接口

    private String guanZhuListUrl="http://www.zmobuy.com/PHP/?url=/user/storelist";//获取关注列表的数据

    private RequestQueue requestQueue;

    //private MainActivity mainActivity;

    public ShopStreetShopListAdapter(Activity context, List<ShopModel> shopModels) {
        this.context = context;
        //mainActivity= (MainActivity) context;
        this.shopModels = shopModels;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();
        requestQueue= MyApplication.getRequestQueue();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.shop_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.logoImagView= (ImageView) v.findViewById(R.id.img_shop_logo);
            viewHolder.nameAndGuanZhuLinearLayout= (LinearLayout) v.findViewById(R.id.ll_name_and_guan_zhu_shu);
            viewHolder.shopNameTextView= (TextView) v.findViewById(R.id.text_shop_name);
            viewHolder.guanZhuRenShuTextView= (TextView) v.findViewById(R.id.text_guan_zhu_ren_shu);
            viewHolder.guanZhuTextView= (TextView) v.findViewById(R.id.text_guan_zhu);
            viewHolder.productScore= (TextView) v.findViewById(R.id.text_product_score);
            viewHolder.fuWuScore= (TextView) v.findViewById(R.id.text_fu_wu_score);
            viewHolder.shiXiaoScore= (TextView) v.findViewById(R.id.text_shi_xiao_score);
            viewHolder.productPingJi= (TextView) v.findViewById(R.id.text_product_ping_ji);
            viewHolder.fuWuPingJi= (TextView) v.findViewById(R.id.text_fu_wu_ping_ji);
            viewHolder.shiXiaoPingJi= (TextView) v.findViewById(R.id.text_shi_xiao_ping_ji);
            viewHolder.shopContentRecyClView= (RecyclerView) v.findViewById(R.id.recycle_view_shop_content);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        final ShopModel shopModel=shopModels.get(position);
        viewHolder.logoImagView.setTag(shopModel.getLogoThumb());//设置tag
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.logoImagView,
                R.mipmap.yu_jia_zai,R.mipmap.yu_jia_zai);
        imageLoader.get(shopModel.getLogoThumb(), listener);


        viewHolder.shopNameTextView.setText(shopModel.getShopName());
        viewHolder.guanZhuRenShuTextView.setText("已经有" + shopModel.getGazeNumber() + "人关注");

        //设置评分和评级
        final ViewHolder finalViewHolder = viewHolder;
        StringRequest stringRequest=new StringRequest(Request.Method.POST, shopInfoUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag,"返回的数据为："+s);
                ShopModel inShopModel=new ShopModel();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    inShopModel.setCommenTrankScore(jsonObject1.getString("commentrank"));
                    inShopModel.setCommentServerScore(jsonObject1.getString("commentserver"));
                    inShopModel.setCommentDeliveryScore(jsonObject1.getString("commentdelivery"));
                    inShopModel.setCommenTrank(jsonObject1.getString("commentrank_font"));
                    inShopModel.setCommentServer(jsonObject1.getString("commentserver_font"));
                    inShopModel.setCommentDelivery(jsonObject1.getString("commentdelivery_font"));
                    finalViewHolder.productScore.setText(inShopModel.getCommenTrankScore());
                    finalViewHolder.fuWuScore.setText(inShopModel.getCommentServerScore());
                    finalViewHolder.shiXiaoScore.setText(inShopModel.getCommentDeliveryScore());

                    finalViewHolder.productPingJi.setText(inShopModel.getCommenTrank());
                    finalViewHolder.fuWuPingJi.setText(inShopModel.getCommentServer());
                    finalViewHolder.shiXiaoPingJi.setText(inShopModel.getCommentDelivery());
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
                String json="{\"id\":\""+shopModel.getUserId()+"\"}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(stringRequest);

        //给关注textview设置初始颜色和背景
        setGuanZhuTextViewFirstValue(viewHolder.guanZhuTextView, shopModel);

        //关注的点击事件
        guanZhuClickChuLi(viewHolder, shopModel, finalViewHolder);

        viewHolder.nameAndGuanZhuLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toShopHomeActivity(shopModel);
            }
        });
        /*viewHolder.shopNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "点击了店铺名称", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShopHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(MyConstant.SHOP_USER_ID_KEY, shopModel.getUserId());
                context.startActivity(intent);
            }
        });*/
        viewHolder.logoImagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "点击了店铺logo", Toast.LENGTH_SHORT).show();
                toShopHomeActivity(shopModel);
            }
        });

        if(shopModel.getGoods()!=null){
            viewHolder.shopContentRecyClView.setVisibility(View.VISIBLE);
            //店铺的商品内容展示
            ShopStreetShopContentAdapter adapter=new ShopStreetShopContentAdapter(context,shopModel.getGoods());
            //设置布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            viewHolder.shopContentRecyClView.setLayoutManager(linearLayoutManager);
            //设置适配器
            viewHolder.shopContentRecyClView.setAdapter(adapter);
            adapter.setOnItemClickLitener(new ShopStreetShopContentAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    //Toast.makeText(context,"点击的位置是"+position,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(MyConstant.GOOD_KEY, shopModel.getGoods().get(position));
                    context.startActivity(intent);
                }
            });
            //mainActivity.menu.setEnabled(false);
        }else{
            viewHolder.shopContentRecyClView.setVisibility(View.GONE);
        }
        return v;
    }

    /**
     * 跳转到店铺主页
     * @param shopModel
     */
    private void toShopHomeActivity(ShopModel shopModel) {
        Intent intent = new Intent(context, ShopHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MyConstant.SHOP_USER_ID_KEY, shopModel.getUserId());
        context.startActivity(intent);


    }


    /**
     * 关注的点击事件处理
     * @param viewHolder
     * @param shopModel
     * @param finalViewHolder
     */
    private void guanZhuClickChuLi(ViewHolder viewHolder, final ShopModel shopModel, final ViewHolder finalViewHolder) {
        //关注的点击事件
        viewHolder.guanZhuTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "点击了关注", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,
                        Context.MODE_APPEND);
                final String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
                final String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
                if(uid!=null && !uid.isEmpty()){
                    StringRequest guanZhuRequest=new StringRequest(Request.Method.POST, guanZhuUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    MyLog.d(tag, "关注接口返回的数据" + s);
                                    try {
                                        JSONObject jsonObject=new JSONObject(s);
                                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                        String erroeDesc= JsonHelper.decodeUnicode(jsonObject1.getString("error_desc"));
                                        if("已关注".equals(erroeDesc)){
                                            Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
                                            finalViewHolder.guanZhuTextView.setTextColor(context.getResources().getColor(R.color.white_my));
                                            finalViewHolder.guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_successed_background);
                                            int guanZhuRenShu=Integer.parseInt(shopModel.getGazeNumber());
                                            finalViewHolder.guanZhuRenShuTextView.setText("已经有"+(1+guanZhuRenShu)+"人关注");
                                            shopModel.setGazeNumber(""+(1+guanZhuRenShu));
                                        }else if("已取消关注".equals(erroeDesc)){
                                            Toast.makeText(context,"取消关注成功",Toast.LENGTH_SHORT).show();
                                            finalViewHolder.guanZhuTextView.setTextColor(context.getResources().getColor(R.color.bottom_lable_color));
                                            finalViewHolder.guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_background);
                                            int guanZhuRenShu=Integer.parseInt(shopModel.getGazeNumber());
                                            finalViewHolder.guanZhuRenShuTextView.setText( "已经有"+(guanZhuRenShu-1) + "人关注");
                                            shopModel.setGazeNumber(""+(guanZhuRenShu-1));
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
                            String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"shop_userid\":\""+shopModel.getUserId()+"\"}";
                            map.put("json",json);
                            return map;
                        }
                    };
                    requestQueue.add(guanZhuRequest);
                }else{//没有记录就跳转到登陆界面
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setMessage("请登录后关注该店铺");
                    builder.setPositiveButton("立即登陆", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.putExtra(MyConstant.START_LOGIN_ACTIVITY_FLAG_KEY, "guanZhu");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            loginDialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loginDialog.dismiss();
                        }
                    });
                    loginDialog=builder.show();
                    loginDialog.show();
                }
            }
        });
    }


    /**
     * 给关注textview设置初始颜色和背景
     * @param guanZhuTextView
     */
    private void setGuanZhuTextViewFirstValue(final TextView guanZhuTextView, final ShopModel shopModel) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, Context.MODE_APPEND);
        final String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        final String sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
        if(uid!=null && !uid.isEmpty()){
            StringRequest getGuanZhuListRequest=new StringRequest(Request.Method.POST, guanZhuListUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            MyLog.d(tag, "返回的数据是：" + s);
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                JSONArray jsonArray=jsonObject1.getJSONArray("store_list");
                                if(jsonArray.length()==0){  //关注列表的长度为空，说明没有关注
                                    guanZhuTextView.setTextColor(context.getResources().getColor(R.color.bottom_lable_color));
                                    guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_background);
                                }else{
                                    int count=0;
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject ziJsObj=jsonArray.getJSONObject(i);
                                        String shopId=ziJsObj.getString("shop_id");
                                        if(shopId.equals(shopModel.getUserId())){   //店铺在店铺列表里面，说明已经关注过了
                                            guanZhuTextView.setTextColor(context.getResources().getColor(R.color.white_my));
                                            guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_successed_background);
                                            break;
                                        }
                                        count++;
                                    }
                                    if(count==jsonArray.length()){  //店铺不在店铺列表里面
                                        guanZhuTextView.setTextColor(context.getResources().getColor(R.color.bottom_lable_color));
                                        guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_background);
                                    }
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
                    String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"page\":\""+"1"+"\"}";
                    map.put("json",json);
                    return map;
                }
            };
            requestQueue.add(getGuanZhuListRequest);
        }else { //用户没有登陆
            guanZhuTextView.setTextColor(context.getResources().getColor(R.color.bottom_lable_color));
            guanZhuTextView.setBackgroundResource(R.drawable.guan_zhu_background);
        }

    }


    /**
     * 解析json数据
     * @param s
     * @return
     */
    private List<ShopModel> parseJson(String s) {
        MyLog.d(tag, "返回的数据是：" + s);
        List<ShopModel> shopModels=new ArrayList<ShopModel>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONArray jsonArray=jsonObject1.getJSONArray("store_list");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject ziJsObj=jsonArray.getJSONObject(i);
                ShopModel shopModel=new ShopModel();
                shopModel.setUserId(ziJsObj.getString("shop_id"));
                shopModel.setShopName(JsonHelper.decodeUnicode(ziJsObj.getString("store_name")));
                shopModel.setShopLogo(ziJsObj.getString("shop_logo"));
                shopModel.setGazeNumber(ziJsObj.getString("count_store"));
                shopModel.setBrandThumb(ziJsObj.getString("brand_thumb"));
                shopModels.add(shopModel);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopModels;
    }

    class ViewHolder{
        ImageView logoImagView;//logo
        LinearLayout nameAndGuanZhuLinearLayout;
        TextView shopNameTextView;//店铺名称
        TextView guanZhuRenShuTextView;//关注人数
        TextView guanZhuTextView;//关注按钮
        TextView productScore;//商品分数
        TextView fuWuScore;//服务分数
        TextView shiXiaoScore;//时效分数
        TextView productPingJi;//商品评级
        TextView fuWuPingJi;//服务评级
        TextView shiXiaoPingJi;//时效评级
        RecyclerView shopContentRecyClView;//店铺的商品内容
    }
}
