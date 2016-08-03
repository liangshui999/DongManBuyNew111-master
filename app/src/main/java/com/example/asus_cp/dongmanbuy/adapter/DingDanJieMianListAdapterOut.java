package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.gou_wu.DingDanActivity;
import com.example.asus_cp.dongmanbuy.activity.gou_wu.DingDanDetailActivity;
import com.example.asus_cp.dongmanbuy.customview.MyListView;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import java.util.ArrayList;
import java.util.List;

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
            viewHolder.maiJiaLiuYanEditText= (EditText) v.findViewById(R.id.edit_mai_jia_liu_yan_list_item_out);
            viewHolder.goodNumberDownTextView= (TextView) v.findViewById(R.id.text_product_sum_he_ji_list_item_out);
            viewHolder.sumPriceTextView= (TextView) v.findViewById(R.id.text_he_ji_price_list_item_out);
            viewHolder.listViewIn= (MyListView) v.findViewById(R.id.my_list_view_list_item_out);
            viewHolder.productDisplayLinearLayoutOrignal= (LinearLayout) v.findViewById(R.id.ll_product_display_area_list_item_out);
            viewHolder.productListLinearLayoutZhanKai= (LinearLayout) v.findViewById(R.id.ll_list_view_hide_list_item_out);
            viewHolder.hideImageView= (ImageView) v.findViewById(R.id.img_down_ding_dan);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        ShopModel shopModel=shopModles.get(position);

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

        //设置点击事件
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.dispalyAllLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.productDisplayLinearLayoutOrignal.setVisibility(View.GONE);
                finalViewHolder.productListLinearLayoutZhanKai.setVisibility(View.VISIBLE);
                DingDanJieMianListAdapterIn adapter=new DingDanJieMianListAdapterIn(context,goods,itemGoodCounts.get(position));
                //动态设置listview的高度，这个很重要，关于为什么是150的解释，因为我自己设置的小项的高度就是150
//                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        100*densty/160*goods.size());
//                finalViewHolder.listViewIn.setLayoutParams(params);
                finalViewHolder.listViewIn.setAdapter(adapter);
            }
        });
        viewHolder.peiSongFangShiRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peiSongFangShiClickChuLi();
            }
        });
        viewHolder.hideImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.productDisplayLinearLayoutOrignal.setVisibility(View.VISIBLE);
                finalViewHolder.productListLinearLayoutZhanKai.setVisibility(View.GONE);
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
     * 配送方式的点击处理
     */
    private void peiSongFangShiClickChuLi() {
        View peiSongFangShiView=inflater.inflate(R.layout.pei_song_fang_shi_layout,null);
        ImageView closePeiSongFangShi= (ImageView) peiSongFangShiView.findViewById(R.id.img_close_pei_song_fang_shi);
        RelativeLayout shangJiaPeiSongRelativeLayout= (RelativeLayout) peiSongFangShiView.findViewById(R.id.re_layout_shang_jia_pei_song);
        RelativeLayout menDianZitIRelaytiveLayout= (RelativeLayout) peiSongFangShiView.findViewById(R.id.re_layout_men_dian_zi_ti);
        shangJiaPeiSongCheckBox= (CheckBox) peiSongFangShiView.findViewById(R.id.check_box_shang_jia_pei_song);
        menDianZiTiChcekBox= (CheckBox) peiSongFangShiView.findViewById(R.id.check_box_men_dian_zi_ti);
        closePeiSongFangShi.setOnClickListener(this);
        shangJiaPeiSongRelativeLayout.setOnClickListener(this);
        menDianZitIRelaytiveLayout.setOnClickListener(this);
        peiSongFangShiWindow=new PopupWindow(peiSongFangShiView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        //peiSongFangShiWindow.setBackgroundDrawable(new ColorDrawable());
        peiSongFangShiWindow.setFocusable(true);
        peiSongFangShiWindow.setOutsideTouchable(false);
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
            case R.id.img_close_pei_song_fang_shi://点击了关闭配送方式
                peiSongFangShiWindow.dismiss();
                break;
            case R.id.re_layout_shang_jia_pei_song://点击了商家配送
                shangJiaPeiSongCheckBox.setChecked(true);
                menDianZiTiChcekBox.setChecked(false);
                //peiSongFangShiWindow.dismiss();
                break;
            case R.id.re_layout_men_dian_zi_ti://点击了门店自提
                shangJiaPeiSongCheckBox.setChecked(false);
                menDianZiTiChcekBox.setChecked(true);
                //peiSongFangShiWindow.dismiss();
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
        EditText maiJiaLiuYanEditText;
        TextView goodNumberDownTextView;//商品数量下面
        TextView sumPriceTextView;//总价格
        MyListView listViewIn;
        ImageView hideImageView;//隐藏商品列表
        LinearLayout productDisplayLinearLayoutOrignal;//原始的
        LinearLayout productListLinearLayoutZhanKai;//展开之后的
    }
}
