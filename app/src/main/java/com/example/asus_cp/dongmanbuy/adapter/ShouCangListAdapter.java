package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.asus_cp.dongmanbuy.activity.product_detail.ProductDetailActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏列表的适配器
 * Created by asus-cp on 2016-06-24.
 */
public class ShouCangListAdapter extends BaseAdapter{

    private String tag="ShouCangListAdapter";

    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    private String deleteShouCangUrl="http://www.zmobuy.com/PHP/?url=/user/collect/delete";//取消收藏的接口
    private RequestQueue requestQueue;
    private String uid;
    private String sid;

    public ShouCangListAdapter(Context context, List<Good> goods) {
        this.context = context;
        this.goods = goods;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();

        requestQueue= MyApplication.getRequestQueue();
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,Context.MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);

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
            v=inflater.inflate(R.layout.shou_cang_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.picImageView= (ImageView) v.findViewById(R.id.img_pic_shou_cang_list_item);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_product_name_shou_cang_list_item);
            viewHolder.kuCunTextView= (TextView) v.findViewById(R.id.text_product_ku_cun_shou_cang_list_item);
            viewHolder.priceTextView= (TextView) v.findViewById(R.id.text_product_price_shou_cang_list_item);
            viewHolder.deleteImageView= (ImageView) v.findViewById(R.id.img_delete_shou_cang_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        final Good good=goods.get(position);
        ImageLoader imageLoader=helper.getImageLoader();
        ImageLoader.ImageListener listener=imageLoader.getImageListener(viewHolder.picImageView,R.mipmap.yu_jia_zai,
                R.mipmap.yu_jia_zai);
        imageLoader.get(good.getGoodsThumb(), listener, 200, 200);

        viewHolder.nameTextView.setText(good.getGoodName());
        viewHolder.kuCunTextView.setText(good.getGoodsNumber());
        viewHolder.priceTextView.setText(FormatHelper.getMoneyFormat(good.getShopPrice()));

        //设置图片点击事件
        viewHolder.picImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ProductDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(MyConstant.GOOD_KEY,good);
                context.startActivity(intent);

            }
        });

        //设置删除按钮的点击事件
        viewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest deleteRequest=new StringRequest(Request.Method.POST, deleteShouCangUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                MyLog.d(tag,"返回的数据是:"+s);
                                try {
                                    JSONObject jsonObject=new JSONObject(s);
                                    JSONObject jsonObject1=jsonObject.getJSONObject("status");
                                    String succedd=jsonObject1.getString("succeed");
                                    if("1".equals(succedd)){
                                        goods.remove(position);
                                        notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
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
                        Map<String,String> map=new HashMap<String, String>();
                        String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"rec_id\":\""+good.getRecId()+"\"}";
                        map.put("json",json);
                        return map;
                    }
                };
                requestQueue.add(deleteRequest);
            }
        });

        return v;
    }


    class ViewHolder{
        ImageView picImageView;
        TextView nameTextView;
        TextView kuCunTextView;
        TextView priceTextView;
        ImageView deleteImageView;
    }
}
