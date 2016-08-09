package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.personal_center.PersonalCenterActivity;
import com.example.asus_cp.dongmanbuy.adapter.DingDanHaoListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;

import java.io.Serializable;
import java.util.List;

/**
 * 余额支付成功后的界面
 * Created by asus-cp on 2016-06-20.
 */
public class YuEZhiFuSuccessedActivity extends Activity{

    private ListView listView;
    private TextView huiYuanZhongXinTextView;

    private List<String> dingDanHaos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.yu_e_zhi_fu_successed_layout);

        listView= (ListView) findViewById(R.id.my_list_ding_dan_hao_list_yu_e);
        huiYuanZhongXinTextView = (TextView) findViewById(R.id.text_see_ding_dan_yu_e_zhi_fu);

        //设置订单号
        dingDanHaos= (List<String>) getIntent().getSerializableExtra(MyConstant.DING_DAN_BIAN_HAO_LIST_KEY);
        DingDanHaoListAdapter adapter=new DingDanHaoListAdapter(this,dingDanHaos);
        listView.setAdapter(adapter);

        //设置点击事件
        huiYuanZhongXinTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(YuEZhiFuSuccessedActivity.this, PersonalCenterActivity.class);
                startActivity(intent);
            }
        });

    }
}
