package com.example.asus_cp.dongmanbuy.fragment.comments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asus_cp.dongmanbuy.R;

/**
 * 差评
 * Created by asus-cp on 2016-06-02.
 */
public class ChaCommentFragment extends Fragment {
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.cha_comments_fragment_layout,null);
        listView= (ListView) v.findViewById(R.id.list_cha_comment);
        return v;
    }
}
