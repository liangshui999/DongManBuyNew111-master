package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.YouHuiQuanModel;

import java.util.List;

/**
 * 商品详情里面优惠券列表的适配器
 * Created by asus-cp on 2016-07-08.
 */
public class ProductDetailYouHuiQuanListAdapter extends BaseAdapter {

    private Context context;
    private List<YouHuiQuanModel> models;
    private LayoutInflater inflater;

    public ProductDetailYouHuiQuanListAdapter(Context context, List<YouHuiQuanModel> models) {
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
            v=inflater.inflate(R.layout.you_hui_quan_list_item_product_detail,null);
            viewHolder=new ViewHolder();
            viewHolder.jinETextView= (TextView) v.findViewById(R.id.text_jin_e_you_hui_quan_list_item);
            viewHolder.useConditionTextView= (TextView) v.findViewById(R.id.text_use_conditon_you_hui_quan_list_item);
            viewHolder.startDateTextView= (TextView) v.findViewById(R.id.text_start_date_you_hui_quan_list_item);
            viewHolder.endDateTextView= (TextView) v.findViewById(R.id.text_end_date_you_hui_quan_list_item);
            viewHolder.lingQuAtOnceRealtiveLayout= (RelativeLayout) v.findViewById(R.id.re_layout_ling_qu_at_once_you_hui_quan_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        YouHuiQuanModel model=models.get(position);
        viewHolder.jinETextView.setText(model.getJinE());
        viewHolder.useConditionTextView.setText("满"+model.getUseConditon()+"可以使用");
        viewHolder.startDateTextView.setText(model.getStartTime());
        viewHolder.endDateTextView.setText(model.getEndTime());

        viewHolder.lingQuAtOnceRealtiveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击了立即领取",Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }


    class ViewHolder{
        TextView jinETextView;
        TextView useConditionTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        RelativeLayout lingQuAtOnceRealtiveLayout;

    }
}
