package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.adapter.FaPiaoCategoryAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 发票的界面
 * Created by asus-cp on 2016-06-17.
 */
public class FaPiaoActiity extends BaseActivity {

    private EditText faPiaoNeiRongEditText;
    private MyListView listView;
    private Button confirmButton;
    private List<String> categories;

    private FaPiaoCategoryAdapter adapter;
    private List<Boolean> checks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.fa_piao_activity_layout);
        setTitle(R.string.fa_piao);
        initView();
        init();
    }

    @Override
    public void initView() {
        faPiaoNeiRongEditText= (EditText) findViewById(R.id.edit_fa_piao_tai_tou);
        listView= (MyListView) findViewById(R.id.my_list_view_fa_piao_list);
        confirmButton= (Button) findViewById(R.id.btn_confirm_fa_piao);
    }

    /**
     * 初始化的方法
     */
    private void init() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(FaPiaoActiity.this, "点击了确认按钮", Toast.LENGTH_SHORT).show();
                for(int i=0;i<checks.size();i++){
                    if(checks.get(i)){
                        String faPiao=faPiaoNeiRongEditText.getText().toString();
                        if(faPiao.equals("") || faPiao.isEmpty()){
                            faPiao="个人";
                        }
                        //String fanHui=faPiao+"\n"+categories.get(i);
                        Intent intent=new Intent();
                        intent.putExtra(MyConstant.FA_PIAO_TAI_TOU_KEY, faPiao);
                        intent.putExtra(MyConstant.FA_PIAO_CONTENT_KEY, categories.get(i));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        categories=new ArrayList<String>();
        categories.add("不开发票");
        categories.add("明细");
        adapter=new FaPiaoCategoryAdapter(this,categories);
        checks=adapter.getChecks();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.allBuXuanZhong();
                checks.set(position, true);
                adapter.notifyDataSetChanged();
                for(int i=0;i<checks.size();i++){
                    View v=parent.getChildAt(i);
                    TextView textView = (TextView) v.findViewById(R.id.text_fa_piao_category);
                    textView.setTextColor(getResources().getColor(R.color.myblack));
                }
                TextView textView = (TextView) view.findViewById(R.id.text_fa_piao_category);
                textView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
            }
        });
    }

}
