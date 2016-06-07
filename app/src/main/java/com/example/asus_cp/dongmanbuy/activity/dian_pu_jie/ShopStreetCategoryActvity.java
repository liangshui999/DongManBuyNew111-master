package com.example.asus_cp.dongmanbuy.activity.dian_pu_jie;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus_cp.dongmanbuy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类作废
 * 店铺街分类的activity
 * Created by asus-cp on 2016-06-06.
 */
public class ShopStreetCategoryActvity extends Activity {
    private LinearLayout categoriesLinearLayout;//店铺街分类的名字
    private ImageView downImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dian_pu_jie_category_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {
        categoriesLinearLayout= (LinearLayout) findViewById(R.id.ll_dian_pu_jie_categories);
        downImageView= (ImageView) findViewById(R.id.img_category_down);

    }
}
