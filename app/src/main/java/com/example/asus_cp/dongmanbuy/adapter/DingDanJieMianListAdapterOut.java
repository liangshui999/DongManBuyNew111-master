package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.gou_wu.DingDanActivity;
import com.example.asus_cp.dongmanbuy.activity.gou_wu.DingDanGoodDispalyListActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.FocuesableListView;
import com.example.asus_cp.dongmanbuy.customview.MyListView;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.KuaiDiModel;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
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
 * 订单界面外部的适配器，展示各个店铺列表的
 * Created by asus-cp on 2016-08-03.
 */
public class DingDanJieMianListAdapterOut extends BaseAdapter implements View.OnClickListener{

    private String tag="DingDanJieMianListAdapterOut";

    private Context context;
    private List<ShopModel> shopModles;
    private ArrayList<List<Integer>> itemGoodCounts;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;
    private View parentView;//所有view的父view
    private PopupWindow peiSongFangShiWindow;//配送方式的弹出窗口
    //配送方式弹出窗口的组件
    private CheckBox shangJiaPeiSongCheckBox;
    private CheckBox menDianZiTiChcekBox;
    private DingDanActivity dingDanActivity;
    private int densty;

    private String shipIdUrl="http://api.zmobuy.com/JK/base/model.php";
    private RequestQueue requestQueue;

    public DingDanJieMianListAdapterOut(Context context, List<ShopModel> shopModles,
                                        ArrayList<List<Integer>> itemGoodCounts) {
        this.context = context;
        dingDanActivity= (DingDanActivity) context;
        this.shopModles = shopModles;
        this.itemGoodCounts=itemGoodCounts;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();
        parentView= LayoutInflater.from(context).inflate(R.layout.ding_dan_activity_layout,null);
        densty=dingDanActivity.getDensty();
        requestQueue= MyApplication.getRequestQueue();
    }

    @Override
    public int getCount() {
        return shopModles.size();
    }

    @Override
    public Object getItem(int position) {
        return shopModles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyLog.d(tag,"getview调用了");
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.ding_dan_list_view_item_out_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.shopNameTextView= (TextView) v.findViewById(R.id.text_shop_name_list_item_out);
            viewHolder.onePicImageView= (ImageView) v.findViewById(R.id.img_one_ding_dan_list_item_out);
            viewHolder.twoPicImageView= (ImageView) v.findViewById(R.id.img_two_ding_dan_list_item_out);
            viewHolder.threePicImageView= (ImageView) v.findViewById(R.id.img_three_ding_dan_list_item_out);
            viewHolder.dispalyAllLinearLayout= (LinearLayout) v.findViewById(R.id.ll_dispaly_all_product_list_item_out);
            viewHolder.goodNumberTextView= (TextView) v.findViewById(R.id.text_good_sum_count_list_item_out);
            viewHolder.peiSongFangShiRelativeLayout= (RelativeLayout) v.findViewById(R.id.re_layout_pei_song_fang_shi_list_item_out);
            viewHolder.peiSongFangShiTextView= (TextView) v.findViewById(R.id.text_pei_song_fang_shi_ding_dan);
            viewHolder.maiJiaLiuYanEditText= (EditText) v.findViewById(R.id.edit_mai_jia_liu_yan_list_item_out);
            viewHolder.goodNumberDownTextView= (TextView) v.findViewById(R.id.text_product_sum_he_ji_list_item_out);
            viewHolder.sumPriceTextView= (TextView) v.findViewById(R.id.text_he_ji_price_list_item_out);
            //viewHolder.listViewIn= (MyListView) v.findViewById(R.id.my_list_view_list_item_out);
            viewHolder.productDisplayLinearLayoutOrignal= (LinearLayout) v.findViewById(R.id.ll_product_display_area_list_item_out);
            viewHolder.productListLinearLayoutZhanKai= (LinearLayout) v.findViewById(R.id.ll_list_view_hide_list_item_out);
            viewHolder.hideImageView= (ImageView) v.findViewById(R.id.img_down_ding_dan);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        final ShopModel shopModel=shopModles.get(position);

        //给view设置值
        viewHolder.shopNameTextView.setText(shopModel.getShopName());
        MyLog.d(tag,"店铺名称是："+shopModel.getShopName());
        final List<Good> goods=shopModel.getGoods();
        List<Integer> singleItemGoodCounts=itemGoodCounts.get(position);

        if(goods.size()==1){
            setValueToImageView(goods.get(0).getGoodsImg(),viewHolder.onePicImageView);
        }else if(goods.size()==2){
            setValueToImageView(goods.get(0).getGoodsImg(), viewHolder.onePicImageView);
            setValueToImageView(goods.get(1).getGoodsImg(), viewHolder.twoPicImageView);

        }else if(goods.size()>=3){
            setValueToImageView(goods.get(0).getGoodsImg(), viewHolder.onePicImageView);
            setValueToImageView(goods.get(1).getGoodsImg(),viewHolder.twoPicImageView);
            setValueToImageView(goods.get(2).getGoodsImg(),viewHolder.threePicImageView);
        }

        //设置商品的件数
        int jianShu=0;
        for(int i=0;i<singleItemGoodCounts.size();i++){
            jianShu=jianShu+singleItemGoodCounts.get(i);
        }
        viewHolder.goodNumberTextView.setText("共"+jianShu+"件");
        viewHolder.goodNumberDownTextView.setText(jianShu+"");

        //设置商品总价，必须进行计算
        double sumPrice=0.00;
        for(int j=0;j<singleItemGoodCounts.size();j++){
            int tempCount=singleItemGoodCounts.get(j);
            double singlePrice=Double.parseDouble(FormatHelper.getNumberFromRenMingBi(goods.get(j).getShopPrice()));
            sumPrice=sumPrice+tempCount*singlePrice;
        }
        viewHolder.sumPriceTextView.setText(FormatHelper.getOneXiaoShuFormat(""+sumPrice));
        shopModel.setSumPrice(FormatHelper.getOneXiaoShuFormat(""+sumPrice));//主要是为了上传用的

        //设置点击事件
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.dispalyAllLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DingDanGoodDispalyListActivity.class);
                intent.putExtra(MyConstant.GOOD_LIST_KEY, (Serializable) goods);
                intent.putExtra(MyConstant.ITEM_PRODUCT_COUNT_KEY, (Serializable) itemGoodCounts.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                /*finalViewHolder.productDisplayLinearLayoutOrignal.setVisibility(View.GONE);
                finalViewHolder.productListLinearLayoutZhanKai.setVisibility(View.VISIBLE);
                DingDanJieMianListAdapterIn adapter=new DingDanJieMianListAdapterIn(context,goods,itemGoodCounts.get(position));
                finalViewHolder.listViewIn.setAdapter(adapter);*/
            }
        });
        viewHolder.peiSongFangShiRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peiSongFangShiClickChuLi(shopModel,finalViewHolder);
            }
        });
        viewHolder.hideImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.productDisplayLinearLayoutOrignal.setVisibility(View.VISIBLE);
                finalViewHolder.productListLinearLayoutZhanKai.setVisibility(View.GONE);
            }
        });

        //给edittext设置监听事件
        viewHolder.maiJiaLiuYanEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MyLog.d(tag,"输入的内容是："+s);
                shopModel.setMaiJiaLiuYan(s.toString());
            }
        });

        return v;
    }


    /**
     * 给商品的imageview赋值
     */
    private void setValueToImageView(String url,ImageView imageView) {
        ImageLoader imageLoader3=helper.getImageLoader();
        ImageLoader.ImageListener listener3=imageLoader3.getImageListener(imageView, R.mipmap.yu_jia_zai,
                R.mipmap.yu_jia_zai);
        imageLoader3.get(url,listener3,200,200);
    }



    /**
     * 配送方式的点击事件处理
     */
    private void peiSongFangShiClickChuLi(final ShopModel shopModel, final ViewHolder viewHolder) {
        View tiXianFangShiView= inflater.inflate(R.layout.pei_song_fang_shi_tan_chu_layout, null);
        ImageView closeTiXianFangShiImageView= (ImageView) tiXianFangShiView.findViewById(R.id.img_close_ti_xian_tan_chu);
        final FocuesableListView peiSongListView= (FocuesableListView) tiXianFangShiView.findViewById(R.id.list_view_ti_xian_tan_chu);

        StringRequest getCardListRequest=new StringRequest(Request.Method.POST, shipIdUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        MyLog.d(tag,"配送方式返回的数据："+s);
                        final List <KuaiDiModel> peiSongFangShis=new ArrayList<KuaiDiModel>();
                        s=FormatHelper.removeBom(s);
                        try {
                            JSONArray jsonArray=new JSONArray(s);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                KuaiDiModel kuaiDiModel=new KuaiDiModel();
                                kuaiDiModel.setName(jsonObject.getString("shipping_name"));//快递名称必须通过json返回，而不是直接设置
                                kuaiDiModel.setId(jsonObject.getString("shipping_id"));
                                peiSongFangShis.add(kuaiDiModel);
                            }
                            PeiSongTanChuListAdapter adapter=new PeiSongTanChuListAdapter(context,peiSongFangShis);
                            peiSongListView.setAdapter(adapter);
                            peiSongListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    shopModel.setShippingId(peiSongFangShis.get(position).getId());
                                    viewHolder.peiSongFangShiTextView.setText(peiSongFangShis.get(position).getName());
                                    peiSongFangShiWindow.dismiss();
                                }
                            });
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
                /*service	get_shipping_id
                ru_id	4150*/
                Map<String,String> map=new HashMap<String,String>();
                map.put("service","get_shipping_id");
                map.put("ru_id",shopModel.getUserId());
                return map;
            }
        };
        requestQueue.add(getCardListRequest);

        closeTiXianFangShiImageView.setOnClickListener(this);

        peiSongFangShiWindow=new PopupWindow(tiXianFangShiView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        peiSongFangShiWindow.setBackgroundDrawable(new ColorDrawable());
        peiSongFangShiWindow.setOutsideTouchable(true);
        peiSongFangShiWindow.setFocusable(true);
        peiSongFangShiWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);//最后才是show,顺序很重要
        setBackgroundAlpha(0.5f);
        peiSongFangShiWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
        WindowManager.LayoutParams lp = dingDanActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        dingDanActivity.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //-----------------------配送方式窗口的点击事件-----------------------------------------
            case R.id.img_close_ti_xian_tan_chu://点击了关闭配送方式
                peiSongFangShiWindow.dismiss();
                break;
        }
    }


    class ViewHolder{
        TextView shopNameTextView;
        ImageView onePicImageView;
        ImageView twoPicImageView;
        ImageView threePicImageView;
        LinearLayout dispalyAllLinearLayout;//点击之后展示所有的商品
        TextView goodNumberTextView;//商品数量
        RelativeLayout peiSongFangShiRelativeLayout;//配送方式
        TextView peiSongFangShiTextView;//
        EditText maiJiaLiuYanEditText;
        TextView goodNumberDownTextView;//商品数量下面
        TextView sumPriceTextView;//总价格
        MyListView listViewIn;
        ImageView hideImageView;//隐藏商品列表
        LinearLayout productDisplayLinearLayoutOrignal;//原始的
        LinearLayout productListLinearLayoutZhanKai;//展开之后的
    }
}
