package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.os.Bundle;
import android.widget.ListView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.adapter.AwaitPayAdapter;
import com.example.asus_cp.dongmanbuy.model.Good;

import java.util.ArrayList;
import java.util.List;

/**
 * 待评价的界面
 * Created by asus-cp on 2016-10-24.
 */
public class AwaitCommentListActivity extends BaseActivity{

    private static final String TAG = "AwaitCommentListActivity";
    private ListView mListView;
    private AwaitPayAdapter mAdapter;
    private List<Good> goods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.acttivity_await_comment_layout);
        setTitle(R.string.dai_ping_jia);
        initView();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        goods=new ArrayList<Good>();
        mAdapter=new AwaitPayAdapter(this,goods);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void initView() {
        mListView= (ListView) findViewById(R.id.list_view_await_comment);
    }
}
