package com.example.asus_cp.dongmanbuy.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 主要是解决listview嵌套在scrollview里面显示不全的问题
 * Created by asus-cp on 2016-06-06.
 */
public class MyListView extends ListView {
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE /2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
