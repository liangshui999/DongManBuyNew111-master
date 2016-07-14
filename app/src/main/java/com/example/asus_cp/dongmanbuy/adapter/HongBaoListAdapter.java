package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.YouHuiQuanModel;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;

import java.util.List;

/**
 * 红包列表的适配器
 * Created by asus-cp on 2016-06-27.
 */
public class HongBaoListAdapter extends BaseAdapter{
    private Context context;
    private List<YouHuiQuanModel> models;
    private LayoutInflater inflater;
    private boolean isGray;

    public HongBaoListAdapter(Context context, List<YouHuiQuanModel> models,boolean isGray) {
        this.context = context;
        this.models = models;
        inflater=LayoutInflater.from(context);
        this.isGray=isGray;
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
           v=inflater.inflate(R.layout.hong_bao_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.jinETextView= (TextView) v.findViewById(R.id.text_jin_e_hong_bao_list_item);
            viewHolder.suoShuDianPuTextView= (TextView) v.findViewById(R.id.text_dian_pu_hong_bao_list_item);
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_name_hong_bao_list_item);
            viewHolder.startTimeTextView= (TextView) v.findViewById(R.id.text_start_time_hong_bao_list_item);
            viewHolder.endTimeTextView= (TextView) v.findViewById(R.id.text_end_time_hong_bao_list_item);
            viewHolder.jinERelativeLayout= (RelativeLayout) v.findViewById(R.id.re_layout_jin_e_hong_bao_list_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        YouHuiQuanModel youHuiQuanModel=models.get(position);
        viewHolder.jinETextView.setText(FormatHelper.getMoneyFormat(youHuiQuanModel.getJinE()));
        viewHolder.suoShuDianPuTextView.setText(youHuiQuanModel.getSuoShuDianPu());
        viewHolder.nameTextView.setText(youHuiQuanModel.getName());
        viewHolder.startTimeTextView.setText(youHuiQuanModel.getStartTime());
        viewHolder.endTimeTextView.setText(youHuiQuanModel.getEndTime());

        if(isGray){
            viewHolder.jinERelativeLayout.setBackgroundResource(R.color.gray);
        }else{
            viewHolder.jinERelativeLayout.setBackgroundResource(R.color.you_hui_quan_huang_se);
        }
        return v;
    }


    class ViewHolder{
        TextView jinETextView;//金额
        TextView suoShuDianPuTextView;//所属店铺
        TextView nameTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;
        RelativeLayout jinERelativeLayout;
    }
}
