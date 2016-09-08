package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.gou_wu.DingDanDetailActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;
import com.example.asus_cp.dongmanbuy.model.DingDanModel;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.MyScreenInfoHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单列表界面的适配器
 * Created by asus-cp on 2016-06-20.
 */
public class DingDanListAdapter extends BaseAdapter{

    private String tag="DingDanListAdapter";

    private Context context;
    private List<DingDanModel> models;
    private LayoutInflater inflater;

    private String cancelOrderUrl="http://www.zmobuy.com/PHP/?url=/order/cancel";//取消订单的接口

    private RequestQueue requestQueue;

    private String uid;
    private String sid;

    public DingDanListAdapter(Context context, List<DingDanModel> models) {
        this.context = context;
        this.models = models;
        inflater=LayoutInflater.from(context);
        requestQueue= MyApplication.getRequestQueue();
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,Context.MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
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
            v=inflater.inflate(R.layout.ding_dan_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.guiShuTextView= (TextView) v.findViewById(R.id.text_gui_shu_ding_dan_list_item);
            viewHolder.faHuoYuFouTextView= (TextView) v.findViewById(R.id.text_fu_kuan_yu_fou_ding_dan_list_item);
            viewHolder.dingDanHaoTextView= (TextView) v.findViewById(R.id.text_ding_dan_hao_ding_dan_list_item);
            viewHolder.dingDanTimeTextView= (TextView) v.findViewById(R.id.text_time_ding_dan_list_item);
            viewHolder.picGridView= (MyGridViewA) v.findViewById(R.id.grid_view_product_pic_ding_dan_list_item);
            viewHolder.productCountLineatLayout= (LinearLayout) v.findViewById(R.id.ll_product_count_ding_dan_list_item);
            viewHolder.productCountTextView= (TextView) v.findViewById(R.id.text_product_count_ding_dan_list_item);
            viewHolder.heJiTextView= (TextView) v.findViewById(R.id.text_sum_price_ding_dan_list_item);
            viewHolder.quXiaoDingDanTextView= (TextView) v.findViewById(R.id.text_qu_xiao_ding_dan_ding_dan_list_item);
            viewHolder.picDisplayLinearLayout= (LinearLayout) v.findViewById(R.id.ll_pic_display_ding_dan_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        final DingDanModel dingDanModel=models.get(position);
        viewHolder.dingDanHaoTextView.setText(dingDanModel.getOrderBianHao());
        viewHolder.dingDanTimeTextView.setText(FormatHelper.getDate(dingDanModel.getOrderTime()));
        //viewHolder.productCountTextView.setText("共" + dingDanModel.getGoods().size() + "款");
        viewHolder.heJiTextView.setText(FormatHelper.getMoneyFormat(dingDanModel.getSumPrice()));

        //设置商品数量，这个需要进行计算
        List<Good> tempGoods=dingDanModel.getGoods();
        int sumCount=0;
        for(int i=0;i<tempGoods.size();i++){
            sumCount=sumCount+Integer.parseInt(tempGoods.get(i).getDingDanNumber());
        }
        viewHolder.productCountTextView.setText("共" + sumCount + "款");

//        //动态设置gridview的高度
//        int size=tempGoods.size();
//        if(size%3==0){
//            size=size/3;
//        }else{
//            size=size/3+1;
//        }
//        int dpi= MyScreenInfoHelper.getScreenDpi();
//        ViewGroup.LayoutParams layoutParams=viewHolder.picGridView.getLayoutParams();
//        layoutParams.height=size*160*dpi/160;
//        viewHolder.picGridView.setLayoutParams(layoutParams);
        DingDanListGridAdapter adapter=new DingDanListGridAdapter(context,dingDanModel.getGoods());
        viewHolder.picGridView.setAdapter(adapter);
        viewHolder.picGridView.setFocusable(false);
        viewHolder.picGridView.setClickable(false);
        viewHolder.picGridView.setPressed(false);
        viewHolder.picGridView.setEnabled(false);

        //设置点击事件
        viewHolder.quXiaoDingDanTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrderClickChuLi(position, dingDanModel);
            }
        });

       /* viewHolder.picDisplayLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // MyLog.d(tag, "点击了查看订单详情");
                startDingDanDetail(dingDanModel);
            }
        });*/

        viewHolder.picGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startDingDanDetail(dingDanModel);
            }
        });

        return v;
    }

    /**
     * 跳转到订单详情界面
     */
    private void startDingDanDetail(DingDanModel dingDanModel) {
        Intent intent=new Intent(context, DingDanDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MyConstant.DING_DAN_MODEL_KEY, dingDanModel);
        context.startActivity(intent);
    }

    /**
     * 取消订单的点击事件处理
     * @param position
     * @param dingDanModel
     */
    private void cancelOrderClickChuLi(final int position, final DingDanModel dingDanModel) {
        StringRequest cacelOrderRequest=new StringRequest(Request.Method.POST, cancelOrderUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag, "返回的数据是：" + s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("status");
                            String flag=jsonObject1.getString("succeed");
                            if("1".equals(flag)){
                                models.remove(position);
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(context,"客户端暂时不支持取消订单",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,"客户端暂时不支持取消订单",Toast.LENGTH_SHORT).show();
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
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"},\"order_id\":\""+dingDanModel.getOrderId()+"\"}";
                map.put("json",json);
                return map;
            }
        };

        requestQueue.add(cacelOrderRequest);
    }


    class ViewHolder{
        TextView guiShuTextView;//是自营还是他营
        TextView faHuoYuFouTextView;//发货了吗
        TextView dingDanHaoTextView;
        TextView dingDanTimeTextView;
        MyGridViewA picGridView;
        LinearLayout productCountLineatLayout;
        TextView productCountTextView;
        TextView heJiTextView;
        TextView quXiaoDingDanTextView;//取消订单
        LinearLayout picDisplayLinearLayout;//图片的展示区域
    }
}
