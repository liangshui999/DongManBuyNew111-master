package com.example.asus_cp.dongmanbuy.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 给首页的猜你喜欢用
 * 当gridview嵌套在scrollview中的时候，使用该自定义的gridview
 *
 * Created by asus-cp on 2016-05-21.
 */
public class MyGridViewA extends GridView {
    private boolean haveScrollbar = false;
    public MyGridViewA(Context context) {
        super(context);
    }
    public MyGridViewA(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyGridViewA(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    /**
     * 设置是否有ScrollBar，当要在ScollView中显示时，应当设置为false。 默认为 true
     *
     * @param haveScrollbar
     */
    public void setHaveScrollbar(boolean haveScrollbar) {
        this.haveScrollbar = haveScrollbar;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (haveScrollbar == false) {
//            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//            super.onMeasure(widthMeasureSpec, expandSpec);
//        } else {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >>2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
