package com.example.asus_cp.dongmanbuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.model.Comment;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;

import java.util.List;

/**
 * 评论的adapter
 * Created by asus-cp on 2016-06-02.
 */
public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> commets;
    private LayoutInflater inflater;

    public CommentAdapter(Context context, List<Comment> commets) {
        this.context = context;
        this.commets = commets;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return commets.size();
    }

    @Override
    public Object getItem(int position) {
        return commets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        CommentAdapter.ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.comment_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.nameTextView= (TextView) v.findViewById(R.id.text_song_xing_people_name_item);
            viewHolder.timeTextView= (TextView) v.findViewById(R.id.text_comment_time_item);
            viewHolder.contentTextView= (TextView) v.findViewById(R.id.text_commet_content_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        viewHolder.nameTextView.setText(commets.get(position).getAuthor());
        viewHolder.timeTextView.setText(FormatHelper.getDate(commets.get(position).getCreateTime()));
        viewHolder.contentTextView.setText(commets.get(position).getContent());
        return v;
    }

    class ViewHolder{
        private TextView nameTextView;
        private TextView timeTextView;
        private TextView contentTextView;
    }
}
