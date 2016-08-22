package com.example.asus_cp.dongmanbuy.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.asus_cp.dongmanbuy.constant.DBConstant;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.FormatHelper;
import com.example.asus_cp.dongmanbuy.util.MyGoodHelper;
import com.example.asus_cp.dongmanbuy.util.MyLog;

/**
 * 数据库操作的帮助类
 * Created by asus-cp on 2016-06-28.
 */
public class BookDBOperateHelper {

    private String tag="BookDBOperateHelper";

    private DBCreateHelper helper;

    public BookDBOperateHelper() {
        helper=DBCreateHelper.getDBCreateHelper();
    }

    /**
     * 向数据库中插入一条商品数据的方法
     */
    public void insert(Good good){
        SQLiteDatabase db=null;
        try{
            db=helper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(DBConstant.Good.GOOD_ID,good.getGoodId());
            contentValues.put(DBConstant.Good.BIG_IMAG,good.getGoodsImg());
            contentValues.put(DBConstant.Good.THUM_IMAG,good.getGoodsThumb());
            contentValues.put(DBConstant.Good.SMALL_IMAG, good.getGoodsSmallImag());
            contentValues.put(DBConstant.Good.GOOD_NAME,good.getGoodName());
            contentValues.put(DBConstant.Good.SHOP_PRICE, MyGoodHelper.getRealPrice(good));
            contentValues.put(DBConstant.Good.MARKET_PRICE,good.getMarket_price());
            contentValues.put(DBConstant.Good.SALE_VOLUME,good.getSalesVolume());
            contentValues.put(DBConstant.Good.GOODS_NUMBER,good.getGoodsNumber());
            db.insert(DBConstant.Good.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
    }


    /**
     * 向数据库中删除一条商品数据
     */
    public void delete(Good good){
        SQLiteDatabase db=null;
        try{
            db=helper.getWritableDatabase();
            MyLog.d(tag, "删除的语句是：" + DBConstant.Good.DELETE_GOOD_BY_ID);
            db.execSQL(DBConstant.Good.DELETE_GOOD_BY_ID, new String[]{good.getGoodId()});
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
    }

    /**
     * 删除数据库中的所有商品记录
     */
    public void deleteAll(){
        SQLiteDatabase db=null;
        try{
            db=helper.getWritableDatabase();
            MyLog.d(tag, "删除的语句是：" + DBConstant.Good.DELETE_ALL_GOODS);
            db.execSQL(DBConstant.Good.DELETE_ALL_GOODS);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
    }




    /**
     * 查询指定区间的商品
     * @param start 查询的第一个商品的位置
     * @param end 查询的最后一个商品的位置
     * @param handler 对查询结果进行处理时用的
     */
    public Object queryGoods(String start,String end,CursorHandler handler){
        SQLiteDatabase db=null;
        Object obj=null;
        try{
            db=helper.getWritableDatabase();
            MyLog.d(tag, "查询的语句是：" + DBConstant.Good.SELECT_PART_GOOD_ORDER_BY_ID);
            Cursor cursor=db.rawQuery(DBConstant.Good.SELECT_PART_GOOD_ORDER_BY_ID, new String[]{start, end});
            obj= handler.handleCursor(cursor);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
        return obj;
    }


    /**
     * 根据id查询商品的方法
     */
    public Object queryGoodById(String goodId,CursorHandler handler){
        SQLiteDatabase db=null;
        Object obj=null;
        try{
            db=helper.getWritableDatabase();
            MyLog.d(tag, "根据id查询的语句是：" + DBConstant.Good.SELECT_GOOD_BY_ID);
            Cursor cursor=db.rawQuery(DBConstant.Good.SELECT_GOOD_BY_ID, new String[]{goodId});
            obj= handler.handleCursor(cursor);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
        return obj;
    }
}
