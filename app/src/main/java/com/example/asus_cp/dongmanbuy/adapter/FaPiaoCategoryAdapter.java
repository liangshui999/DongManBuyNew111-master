package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 发票种类的adapter
 * Created by asus-cp on 2016-06-17.
 */
public class FaPiaoCategoryAdapter extends BaseAdapter {
    private Context context;
    private List<String> categories;
    private LayoutInflater inflater;

    private List<Boolean> checks;//记录选中状态的集合

    public FaPiaoCategoryAdapter(Context context, List<String> categories) {
        this.context = context;
        this.categories = categories;
        inflater=LayoutInflater.from(context);
        initChecks();
    }

    /**
     * 初始化checks
     */
    private void initChecks() {
        checks=new ArrayList<Boolean>();
        checks.add(true);
        for(int i=1;i<categories.size();i++){
            checks.add(false);
        }
    }

    /**
     *都不选中
     */
    public void allBuXuanZhong(){
        for(int i=0;i<categories.size();i++){
            checks.set(i,false);
        }
    }

    public List<Boolean> getChecks() {
        return checks;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
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
            v=inflater.inflate(R.layout.fa_piao_category_item,null);
            viewHolder=new ViewHolder();
            viewHolder.textView= (TextView) v.findViewById(R.id.text_fa_piao_category);
            viewHolder.checkBox= (CheckBox) v.findViewById(R.id.check_box_fa_piao_category);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }

        viewHolder.textView.setText(categories.get(position));
        //viewHolder.textView.setTextColor(context.getResources().getColor(R.color.black));
        viewHolder.checkBox.setChecked(checks.get(position));

        return v;
    }


    class ViewHolder{
        TextView textView;
        CheckBox checkBox;
    }
}
