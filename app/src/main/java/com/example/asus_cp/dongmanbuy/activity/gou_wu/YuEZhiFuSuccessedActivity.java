package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.activity.personal_center.PersonalCenterActivity;
import com.example.asus_cp.dongmanbuy.adapter.DingDanHaoListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;

import java.util.List;

/**
 * 余额支付成功后的界面
 * Created by asus-cp on 2016-06-20.
 */
public class YuEZhiFuSuccessedActivity extends BaseActivity implements View.OnClickListener{

    private ListView listView;
    private TextView huiYuanZhongXinTextView;
    private TextView seeOrderTextView;

    private List<String> dingDanHaos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.yu_e_zhi_fu_successed_layout);
        setTitle(R.string.shou_yin_tai);
        initView();
        init();
    }

    private void init() {
        //设置订单号
        dingDanHaos= (List<String>) getIntent().getSerializableExtra(MyConstant.DING_DAN_BIAN_HAO_LIST_KEY);
        DingDanHaoListAdapter adapter=new DingDanHaoListAdapter(this,dingDanHaos);
        listView.setAdapter(adapter);
    }

    @Override
    public void initView() {
        listView= (ListView) findViewById(R.id.my_list_ding_dan_hao_list_yu_e);
        huiYuanZhongXinTextView = (TextView) findViewById(R.id.text_hui_yuan_center_yu_e_zhi_fu);
        seeOrderTextView= (TextView) findViewById(R.id.text_see_order_yu_e_zhi_fu);

        //设置点击事件
        huiYuanZhongXinTextView.setOnClickListener(this);
        seeOrderTextView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_see_order_yu_e_zhi_fu://查看订单
                break;
            case R.id.text_hui_yuan_center_yu_e_zhi_fu://会员中心
                Intent intent=new Intent(YuEZhiFuSuccessedActivity.this, PersonalCenterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
