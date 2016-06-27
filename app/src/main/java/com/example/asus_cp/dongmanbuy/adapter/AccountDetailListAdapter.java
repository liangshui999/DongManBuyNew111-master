package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.DingDanModel;

import java.util.List;

/**
 * 订单详情的适配器
 * Created by asus-cp on 2016-06-27.
 */
public class AccountDetailListAdapter extends BaseAdapter{

    private Context context;
    private List<DingDanModel> models;
    private LayoutInflater inflater;

    public AccountDetailListAdapter(Context context, List<DingDanModel> models) {
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
            v=inflater.inflate(R.layout.account_detail_list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.dingDanHaoTextView= (TextView) v.findViewById(R.id.text_ding_dan_hao_account_detail_list_item);
            viewHolder.moneyTextView= (TextView) v.findViewById(R.id.text_money_account_detail_list_item);
            viewHolder.timeTextView= (TextView) v.findViewById(R.id.text_time_account_detail_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        DingDanModel dingDanModel=models.get(position);
        viewHolder.dingDanHaoTextView.setText(dingDanModel.getOrderBianHao());
        viewHolder.moneyTextView.setText(dingDanModel.getSumPrice());
        viewHolder.timeTextView.setText(dingDanModel.getOrderTime());

        return v;
    }

    class ViewHolder{
        TextView dingDanHaoTextView;//订单号
        TextView moneyTextView;//金额
        TextView timeTextView;//时间
    }
}
