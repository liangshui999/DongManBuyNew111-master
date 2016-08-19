package com.example.asus_cp.dongmanbuy.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 在弹出窗口里面用，既能聚焦，又能完全显示
 * Created by asus-cp on 2016-08-19.
 */
public class MyFocuseableListView extends ListView{

    public MyFocuseableListView(Context context) {
        super(context);
    }

    public MyFocuseableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFocuseableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean hasWindowFocus() {
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE /2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
