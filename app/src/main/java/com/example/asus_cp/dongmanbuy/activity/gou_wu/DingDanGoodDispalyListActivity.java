package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.DingDanJieMianListAdapterIn;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyListView;
import com.example.asus_cp.dongmanbuy.model.Good;

import java.util.List;

/**
 * 订单商品列表展示的界面（点击之后展示）
 * Created by asus-cp on 2016-08-09.
 */
public class DingDanGoodDispalyListActivity extends Activity{

    private MyListView listView;
    private TextView goodCountTextView;//商品件数
    private ImageView daoHangImageView;//导航
    private List<Good> goods;
    private List<Integer> itemGoodCounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        goods= (List<Good>) getIntent().getSerializableExtra(MyConstant.GOOD_LIST_KEY);
        itemGoodCounts= (List<Integer>) getIntent().getSerializableExtra(MyConstant.ITEM_PRODUCT_COUNT_KEY);
        setContentView(R.layout.ding_dan_good_display_list_layout);
        listView= (MyListView) findViewById(R.id.my_list_view_ding_dan_good_display);
        DingDanJieMianListAdapterIn adapterIn=new DingDanJieMianListAdapterIn(this,goods,itemGoodCounts);
        listView.setAdapter(adapterIn);

        goodCountTextView= (TextView) findViewById(R.id.text_good_count);
        daoHangImageView= (ImageView) findViewById(R.id.img_dao_hang_good_qing_dan);

        //计算商品的总件数
        int sum=0;
        for(int i=0;i<itemGoodCounts.size();i++){
            sum=sum+itemGoodCounts.get(i);
        }
        goodCountTextView.setText("共"+sum+"件");

        //设置点击事件
        daoHangImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
