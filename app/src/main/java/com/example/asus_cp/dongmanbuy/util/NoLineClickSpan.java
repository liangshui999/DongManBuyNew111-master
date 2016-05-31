package com.example.asus_cp.dongmanbuy.util;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.example.asus_cp.dongmanbuy.R;

/**
 * Created by asus-cp on 2016-05-30.
 */
//无下划线超链接，使用textColorLink、textColorHighlight分别修改超链接前景色和按下时的颜色
class NoLineClickSpan extends ClickableSpan {
    public NoLineClickSpan() {
        super();
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(MyApplication.getContext().getResources().getColor(R.color.bottom_lable_color));
        ds.setUnderlineText(false); //去掉下划线</span>
    }

    @Override
    public void onClick(View widget) {
    }
}
