package com.example.asus_cp.dongmanbuy.fragment;

import android.support.v4.app.Fragment;

import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.umeng.analytics.MobclickAgent;

/**
 * 所有fragment的基类，主要是友盟统计用
 * Created by asus-cp on 2016-10-19.
 */
public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面，"MainScreen"为页面名称，可自定义
        MyLog.d(TAG,this.getClass().getSimpleName());
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}
