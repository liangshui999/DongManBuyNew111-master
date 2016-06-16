package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.ImageLoadHelper;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车列表的适配器,此类作废，直接在购物车d的活动里面寻找即可
 * Created by asus-cp on 2016-06-13.
 */
public class ShoppingCarListAdapter extends BaseAdapter implements View.OnClickListener{
    private String tag="ShoppingCarListAdapter";
    private Context context;
    private List<Good> goods;
    private LayoutInflater inflater;
    private ImageLoadHelper helper;

    private List<Boolean> checks;//记录选中状态的集合

    private TextView heJiTextView;//由购物车活动传递过来
    private TextView jieSuanShuMuTextView;

    private double heJi;//合计的值
    private int jieSuan;//结算数目的值

    public ShoppingCarListAdapter(Context context, List<Good> goods,TextView heJiTextView,
                                  TextView jieSuanShuMuTextView) {
        this.context = context;
        this.goods = goods;
        inflater=LayoutInflater.from(context);
        helper=new ImageLoadHelper();
        init();
        this.heJiTextView=heJiTextView;
        this.jieSuanShuMuTextView=jieSuanShuMuTextView;
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
        checks=new ArrayList<>();
        for(int i=0;i<goods.size();i++){
            checks.add(false);
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
            private int prodcutCount=Integer.parseInt(good.getShoppingCarNumber());
            private int kuCun=Integer.parseInt(good.getGoodsNumber());;
            @Override
            public void onClick(View v) {
                heJiTextView.setText(2222+"");
                jieSuanShuMuTextView.setText(222+"");
                /*prodcutCount++;
                if(prodcutCount<= kuCun){
                    Toast.makeText(context,"库存数量:"+kuCun,Toast.LENGTH_SHORT).show();
                    //heJiTextView.setText(3333+"");
                    //jieSuanShuMuTextView.setText(3333+"");
                    finalViewHolder.productCountTextView.setText(prodcutCount+"");
                }else{
                    Toast.makeText(context,"超过了库存数量",Toast.LENGTH_SHORT).show();
                    finalViewHolder.productCountTextView.setText(kuCun + "");
                }*/
            }
        });


        //减号的点击事件
        viewHolder.jianHaoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heJiTextView.setText("123");
                jieSuanShuMuTextView.setText("123");
            }
        });



//        viewHolder.picImageView.setOnClickListener(this);
//        viewHolder.nameTextView.setOnClickListener(this);
//        viewHolder.jianHaoImageView.setOnClickListener(this);
//        viewHolder.jiaHaoImageView.setOnClickListener(this);
        viewHolder.deleteImageView.setOnClickListener(this);
        return v;
    }

    /**
     * 从集合中取出check状态
     */
    private void getCheckStateAndSetTextView() {
        heJi=0;//注意这里需要清零
        jieSuan=0;//注意这里需要清零
        for(int i=0;i<checks.size();i++){
            if(checks.get(i)){
                int productCount=Integer.parseInt(goods.get(i).getShoppingCarNumber());
                heJi=heJi+Double.parseDouble(FormatHelper.getNumberFromRenMingBi(goods.get(i).getShopPrice()))*productCount;
                jieSuan=jieSuan+productCount;
            }
        }
        heJiTextView.setText(heJi+"");
        jieSuanShuMuTextView.setText(jieSuan+"");
    }

    /**
     * 从集合中取出check状态
     */
    private void getCheckStateAndSetTextView(int productCount) {
        heJi=0;//注意这里需要清零
        jieSuan=0;//注意这里需要清零
        for(int i=0;i<checks.size();i++){
            if(checks.get(i)){
                //int productCount=Integer.parseInt(goods.get(i).getShoppingCarNumber());
                heJi=heJi+Double.parseDouble(FormatHelper.getNumberFromRenMingBi(goods.get(i).getShopPrice()))*productCount;
                jieSuan=jieSuan+productCount;
                MyLog.d(tag,"product="+productCount+"heJI="+heJi+"jieSuan="+jieSuan);
            }
        }
        heJiTextView.setText(2+"");
        jieSuanShuMuTextView.setText(2+"");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.img_pic_shopping_car_list://点击了图片
                Toast.makeText(context,"点击了图片",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_product_name_shopping_car_list://点击了名字
                Toast.makeText(context,"点击了名字",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_jian_hao_shopping_car_list://点击了减号
                Toast.makeText(context,"点击了减号",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_jia_hao_shopping_car_list://点击了加号
                Toast.makeText(context,"点击了加号",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_delete_shopping_car_list://点击了删除
                Toast.makeText(context,"点击了删除",Toast.LENGTH_SHORT).show();
                heJiTextView.setText(111+"");
                jieSuanShuMuTextView.setText(222 + "");
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
