package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;

import java.util.List;
import java.util.Map;

/**
 * 县区名的adapter
 * Created by asus-cp on 2016-06-03.
 */
public class XianQuMingAdapter extends BaseExpandableListAdapter {
    private List<String> shiMings;//市名的列表
    private Map<String,List<String>> xianQuMings;//县区名的列表
    private Context context;
    private LayoutInflater inflater;

    public XianQuMingAdapter(Context context, Map<String, List<String>> xianQuMings, List<String> shiMings) {
        this.context = context;
        this.xianQuMings = xianQuMings;
        this.shiMings = shiMings;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return shiMings.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String shiMing=shiMings.get(groupPosition);
        return xianQuMings.get(shiMing).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return shiMings.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String shiMing=shiMings.get(groupPosition);
        return xianQuMings.get(shiMing).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v=convertView;
        ViewHolderParent viewHolderParent=null;
        if(convertView==null){
            v=inflater.inflate(R.layout.shi_ming_item_layout,null);
            viewHolderParent=new ViewHolderParent();
            viewHolderParent.shiMingTextView= (TextView) v.findViewById(R.id.text_shi_ming_item);
            viewHolderParent.shiMingRightImageView = (ImageView) v.findViewById(R.id.img_shi_ming_right);
            v.setTag(viewHolderParent);
        }else{
            viewHolderParent= (ViewHolderParent) v.getTag();
        }
        viewHolderParent.shiMingTextView.setText(shiMings.get(groupPosition));
        if(isExpanded){
            //viewHolderParent.shiMingRightImageView.setBackground(context.getDrawable(R.mipmap.down));
            viewHolderParent.shiMingRightImageView.setImageResource(R.mipmap.right);
        }else{
            //viewHolderParent.shiMingRightImageView.setBackground(context.getDrawable(R.mipmap.right));
            viewHolderParent.shiMingRightImageView.setImageResource(R.mipmap.down);
        }
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v=convertView;
        ViewHolderChilder viewHolderChilder;
        if(v==null){
            v=inflater.inflate(R.layout.xian_qu_item_layout,null);
            viewHolderChilder=new ViewHolderChilder();
            viewHolderChilder.xianQuTextView= (TextView) v.findViewById(R.id.text_xian_qu_ming);
            v.setTag(viewHolderChilder);
        }else{
            viewHolderChilder= (ViewHolderChilder) v.getTag();
        }
        String xianQuMing=xianQuMings.get(shiMings.get(groupPosition)).get(childPosition);
        viewHolderChilder.xianQuTextView.setText(xianQuMing);
        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class ViewHolderParent{
        private TextView shiMingTextView;
        private ImageView shiMingRightImageView;
    }

    class ViewHolderChilder{
        private TextView xianQuTextView;
    }
}
