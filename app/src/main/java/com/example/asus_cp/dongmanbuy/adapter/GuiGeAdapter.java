package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.GuiGeModel;

import java.util.List;

/**
 * 商品规格的适配器
 * Created by asus-cp on 2016-09-09.
 */
public class GuiGeAdapter extends BaseAdapter{
    private Context context;
    private List<GuiGeModel> models;
    private LayoutInflater inflater;

    public GuiGeAdapter(Context context, List<GuiGeModel> models) {
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
            v=inflater.inflate(R.layout.gui_ge_list_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_name_gui_ge_item);
            viewHolder.valueTextView= (TextView) v.findViewById(R.id.text_value_gui_ge_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        GuiGeModel guiGeModel=models.get(position);
        viewHolder.nameTextView.setText(guiGeModel.getName());
        viewHolder.valueTextView.setText(guiGeModel.getValue());
        return v;
    }

    class ViewHolder{
        TextView nameTextView;
        TextView valueTextView;
    }
}
