package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.MyApplication;

/**
 * 账户明细的界面
 * Created by asus-cp on 2016-06-27.
 */
public class AccountDetailActivity extends Activity{

    private ListView listView;

    private String accountDetailUrl="http://www.zmobuy.com/PHP/?url=/user/account_detail";//账户明细的接口

    private RequestQueue requestQueue;
    private String uid;
    private String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_detail_activity_layout);
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {

        requestQueue= MyApplication.getRequestQueue();
        SharedPreferences sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,MODE_APPEND);
        uid=sharedPreferences.getString(MyConstant.UID_KEY,null);
        sid=sharedPreferences.getString(MyConstant.SID_KEY,null);



        listView= (ListView) findViewById(R.id.list_view_account_detail);
    }
}
