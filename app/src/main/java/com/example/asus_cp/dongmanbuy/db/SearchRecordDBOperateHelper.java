package com.example.asus_cp.dongmanbuy.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.asus_cp.dongmanbuy.constant.DBConstant;
import com.example.asus_cp.dongmanbuy.util.MyLog;

/**
 * 搜索记录数据库操作的帮助类
 * Created by asus-cp on 2016-06-30.
 */
public class SearchRecordDBOperateHelper {

    private String tag="SearchRecordDBOperateHelper";

    private DBCreateHelper helper;

    public SearchRecordDBOperateHelper() {
        helper=DBCreateHelper.getDBCreateHelper();
    }

    /**
     * 向数据库中插入一条数据
     */
    public void insert(String s){
        SQLiteDatabase db=null;
        try{
            db=helper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(DBConstant.SearchRecord.KEY_WORD,s);
            db.insert(DBConstant.SearchRecord.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
    }


    /**
     * 从数据库中删除一条数据,根据关键字删除
     */
    public void deleteByKeyWord(String s){
        SQLiteDatabase db=null;
        try{
            db=helper.getWritableDatabase();
            db.execSQL(DBConstant.SearchRecord.DELETE_BY_KEY_WORD, new String[]{s});
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
    }


    /**
     * 从数据库中删除所有数据
     */
    public void deleteAll(){
        SQLiteDatabase db=null;
        try{
            db=helper.getWritableDatabase();
            db.execSQL(DBConstant.SearchRecord.DELETE_ALL);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
    }


    /**
     * 查询某个区间的数据
     */
    public Object queryAll(String start,String end,CursorHandler handler){
        SQLiteDatabase db=null;
        Object object=null;
        try{
            db=helper.getWritableDatabase();
            MyLog.d(tag,"查询语句是:"+DBConstant.SearchRecord.QUERY_PART);
            Cursor cursor=db.rawQuery(DBConstant.SearchRecord.QUERY_PART, new String[]{start,end});
            object=handler.handleCursor(cursor);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
        return object;
    }


    /**
     * 根据关键字查询是否存在某条记录
     */
    public boolean queryByKeyWord(String keyWord){
        SQLiteDatabase db=null;
        try{
            db=helper.getWritableDatabase();
            Cursor cursor=db.rawQuery(DBConstant.SearchRecord.QUERY_BY_KEY_WORD, new String[]{keyWord});
            if(cursor.moveToNext()){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
        return false;
    }


}
