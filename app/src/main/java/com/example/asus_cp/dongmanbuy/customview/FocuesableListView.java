package com.example.asus_cp.dongmanbuy.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 让listview可以获取焦点
 * Created by asus-cp on 2016-07-22.
 */
public class FocuesableListView extends ListView{
    public FocuesableListView(Context context) {
        super(context);
    }

    public FocuesableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocuesableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean hasWindowFocus() {
        return true;
    }
}
