package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.adapter.AwaitCommentAdapter;
import com.example.asus_cp.dongmanbuy.model.Good;

import java.util.ArrayList;
import java.util.List;

/**
 * 待评价列表的界面
 * Created by asus-cp on 2016-10-24.
 */
public class AwaitCommentListActivity extends BaseActivity{

    private static final String TAG = "AwaitCommentListActivity";
    private ListView mListView;
    private AwaitCommentAdapter mAdapter;
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
        Good good=new Good();
        good.setGoodName("测试评价商品");
        goods.add(good);
        mAdapter=new AwaitCommentAdapter(this,goods);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AwaitCommentListActivity.this,DingDanCommentActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void initView() {
        mListView= (ListView) findViewById(R.id.list_view_await_comment);
    }
}
