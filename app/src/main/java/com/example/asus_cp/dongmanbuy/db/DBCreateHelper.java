package com.example.asus_cp.dongmanbuy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asus_cp.dongmanbuy.constant.DBConstant;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

/**
 * 创建数据库的帮助类
 * Created by asus-cp on 2016-06-28.
 */
public class DBCreateHelper extends SQLiteOpenHelper{

    private String tag="DBCreateHelper";

    public static DBCreateHelper helper=new DBCreateHelper(MyApplication.getContext(),DBConstant.DB_NAME,
            null,DBConstant.DB_VERSION);

    public static DBCreateHelper getDBCreateHelper(){
        return helper;
    }

    /**
     * 将构造方法进行私有化
     */
    private DBCreateHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MyLog.d(tag,"建表语句是："+DBConstant.Good.CREATE_TABLE);
        db.execSQL(DBConstant.Good.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
