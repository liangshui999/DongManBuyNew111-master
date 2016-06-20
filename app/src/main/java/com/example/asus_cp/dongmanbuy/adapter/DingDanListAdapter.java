package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.DingDanModel;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;

import java.util.List;

/**
 * 订单列表界面的适配器
 * Created by asus-cp on 2016-06-20.
 */
public class DingDanListAdapter extends BaseAdapter{
    private Context context;
    private List<DingDanModel> models;
    private LayoutInflater inflater;

    public DingDanListAdapter(Context context, List<DingDanModel> models) {
        this.context = context;
        this.models = models;
        inflater=LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.ding_dan_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.guiShuTextView= (TextView) v.findViewById(R.id.text_gui_shu_ding_dan_list_item);
            viewHolder.faHuoYuFouTextView= (TextView) v.findViewById(R.id.text_fu_kuan_yu_fou_ding_dan_list_item);
            viewHolder.dingDanHaoTextView= (TextView) v.findViewById(R.id.text_ding_dan_hao_ding_dan_list_item);
            viewHolder.dingDanTimeTextView= (TextView) v.findViewById(R.id.text_time_ding_dan_list_item);
            viewHolder.picGridView= (GridView) v.findViewById(R.id.grid_view_product_pic_ding_dan_list_item);
            viewHolder.productCountLineatLayout= (LinearLayout) v.findViewById(R.id.ll_product_count_ding_dan_list_item);
            viewHolder.productCountTextView= (TextView) v.findViewById(R.id.text_product_count_ding_dan_list_item);
            viewHolder.heJiTextView= (TextView) v.findViewById(R.id.text_sum_price_ding_dan_list_item);
            viewHolder.quXiaoDingDanTextView= (TextView) v.findViewById(R.id.text_qu_xiao_ding_dan_ding_dan_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        DingDanModel dingDanModel=models.get(position);
        viewHolder.dingDanHaoTextView.setText(dingDanModel.getOrderBianHao());
        viewHolder.dingDanTimeTextView.setText(FormatHelper.getDate(dingDanModel.getOrderTime()));
        viewHolder.productCountTextView.setText("共"+dingDanModel.getGoods().size()+"款");
        viewHolder.heJiTextView.setText(FormatHelper.getMoneyFormat(dingDanModel.getSumPrice()));

        DingDanListGridAdapter adapter=new DingDanListGridAdapter(context,dingDanModel.getGoods());
        viewHolder.picGridView.setAdapter(adapter);

        return v;
    }



    class ViewHolder{
        TextView guiShuTextView;//是自营还是他营
        TextView faHuoYuFouTextView;//发货了吗
        TextView dingDanHaoTextView;
        TextView dingDanTimeTextView;
        GridView picGridView;
        LinearLayout productCountLineatLayout;
        TextView productCountTextView;
        TextView heJiTextView;
        TextView quXiaoDingDanTextView;//取消订单
    }
}
