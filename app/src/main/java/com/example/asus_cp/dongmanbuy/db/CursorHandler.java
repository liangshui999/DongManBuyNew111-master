package com.example.asus_cp.dongmanbuy.db;

import android.database.Cursor;

/**
 * 处理cursor对象时回调的接口
 * Created by asus-cp on 2016-06-28.
 */
public interface CursorHandler {
    public Object handleCursor(Cursor cursor);
}
