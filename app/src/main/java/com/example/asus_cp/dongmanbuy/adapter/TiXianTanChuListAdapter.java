package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.CardModel;

import java.util.List;

/**
 * 提现界面弹出，的选择银行卡列表
 * Created by asus-cp on 2016-06-27.
 */
public class TiXianTanChuListAdapter extends BaseAdapter{

    private Context context;
    private List<CardModel> models;
    private LayoutInflater inflater;

    public TiXianTanChuListAdapter(Context context, List<CardModel> models) {
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
            v=inflater.inflate(R.layout.ti_xian_tan_chu_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.bankNameTextView= (TextView) v.findViewById(R.id.text_bank_name_ti_xian_tan_chu_list_item);
            viewHolder.kaHaoTextView= (TextView) v.findViewById(R.id.text_ka_hao_ti_xian_tan_chu_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        CardModel cardModel=models.get(position);
        viewHolder.bankNameTextView.setText(cardModel.getBankName());
        viewHolder.kaHaoTextView.setText(cardModel.getKaHao());

        return v;

    }

    class ViewHolder{
        TextView bankNameTextView;
        TextView kaHaoTextView;
    }
}
