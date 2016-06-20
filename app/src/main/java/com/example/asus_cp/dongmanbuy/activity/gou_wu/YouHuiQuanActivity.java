package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.YouHuiQuanListAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.YouHuiQuanModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券的界面
 * Created by asus-cp on 2016-06-17.
 */
public class YouHuiQuanActivity extends Activity{
    private ListView listView;
    private Button confirmButton;
    private List<YouHuiQuanModel> models;
    private List<Boolean> checks;
    private YouHuiQuanListAdapter adapter;
    private YouHuiQuanModel passModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.you_hui_quan_activty_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        listView= (ListView) findViewById(R.id.list_you_hui_quan_list);
        confirmButton= (Button) findViewById(R.id.btn_confirm_you_hui_quan);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra(MyConstant.YOU_HUI_QUAN_MODEL_KEY,passModel);
                intent.putExtra(MyConstant.YOU_HUI_QUAN_SHU_MU_KEY,models.size()+"");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        models=new ArrayList<YouHuiQuanModel>();
        YouHuiQuanModel youHuiQuanModel=new YouHuiQuanModel();
        youHuiQuanModel.setYouHuiQuanJinE(98+"");
        youHuiQuanModel.setYouHuiQuanName("ceshi");
        youHuiQuanModel.setYouHuiQuanTime("2016.5.18-2.016.6.18");
        models.add(youHuiQuanModel);

        YouHuiQuanModel youHuiQuanModel1=new YouHuiQuanModel();
        youHuiQuanModel1.setYouHuiQuanJinE(1 + "");
        youHuiQuanModel1.setYouHuiQuanName("dddsdd");
        youHuiQuanModel1.setYouHuiQuanTime("2017.5.18-2.017.6.18");
        models.add(youHuiQuanModel1);

        adapter=new YouHuiQuanListAdapter(this,models);
        checks=adapter.getChecks();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.allBuXuanZhong();
                checks.set(position, true);
                adapter.notifyDataSetChanged();
                passModel=models.get(position);
            }
        });
    }
}
