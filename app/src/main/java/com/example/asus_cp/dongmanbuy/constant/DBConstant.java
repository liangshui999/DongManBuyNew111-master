package com.example.asus_cp.dongmanbuy.constant;

/**
 * 存放数据库里面的常量
 * Created by asus-cp on 2016-06-28.
 */
public interface DBConstant {

    public static final String DB_NAME="zhouMoBuy";//数据库名字
    public static final int DB_VERSION=1;//数据库版本
    public static final String KONG_GE=" ";//空格


    /**
     * 商品表里面的常量
     */
    public interface Good{

        public static final String TABLE_NAME="good";//表名

        //字段名
        public static final String GOOD_ID="goodId";
        public static final String BIG_IMAG="bigImage";
        public static final String THUM_IMAG="thumImage";
        public static final String SMALL_IMAG="smallImage";
        public static final String GOOD_NAME="name";
        public static final String SHOP_PRICE="shopPrice";
        public static final String MARKET_PRICE="marketPrice";
        public static final String SALE_VOLUME="saleVolume";
        public static final String GOODS_NUMBER="goodsNumber";


        //建表语句
        public static final String CREATE_TABLE="create table if not exists"+KONG_GE+TABLE_NAME+"("
                +"id integer primary key AUTOINCREMENT,"
                +GOOD_ID+KONG_GE+"text unique not null,"
                +BIG_IMAG+KONG_GE+"text,"
                +THUM_IMAG+KONG_GE+"text,"
                +SMALL_IMAG+KONG_GE+"text,"
                +GOOD_NAME+KONG_GE+"text not null,"
                +SHOP_PRICE+KONG_GE+"text not null,"
                +MARKET_PRICE+KONG_GE+"text not null,"
                +SALE_VOLUME+KONG_GE+"text not null,"
                +GOODS_NUMBER+KONG_GE+"text not null"
                +");";

        //查询所有商品的语句
        public static final String SELETED_ALL_GOODS="select * from"+KONG_GE+TABLE_NAME+";";

        //根据id查询指定商品的语句
        public static final String SELECT_GOOD_BY_ID="select * from"+KONG_GE+TABLE_NAME+KONG_GE+"where"+KONG_GE+
                GOOD_ID+"=?;";

        //查询前20条商品，按id进行倒序排列
        public static final String SELECT_PART_GOOD_ORDER_BY_ID="select * from"+KONG_GE+TABLE_NAME+KONG_GE
                +"order by id desc limit ?,?;";

        //根据goodId删除商品
        public static final String DELETE_GOOD_BY_ID="delete from"+KONG_GE+TABLE_NAME+KONG_GE+
                "where"+KONG_GE+GOOD_ID+"=?;";

        //删除所有商品
        public static final String DELETE_ALL_GOODS="delete from"+KONG_GE+TABLE_NAME;
    }



    /**
     * 查询记录的表
     */
    public interface  SearchRecord{

        //表名
        public static final String TABLE_NAME="searchRecord";

        //字段名
        public static final String KEY_WORD="keyWord";//查询的关键字

        //建表语句
        public static final String CREATE_TABLE="create table if not exists"+KONG_GE+TABLE_NAME+"("
                +"id integer primary key AUTOINCREMENT,"
                +KEY_WORD+KONG_GE+"text unique not null"
                +");";

        //根据关键字查询某条记录
        public static final String QUERY_BY_KEY_WORD="select * from "+TABLE_NAME+KONG_GE+"where"+KONG_GE+KEY_WORD+"=?";

        //查询某个区间的记录
        public static final String QUERY_PART ="select * from "+TABLE_NAME+KONG_GE+"order by id desc limit ?,?";

        //根据关键字删除某条记录
        public static final String DELETE_BY_KEY_WORD="delete from "+TABLE_NAME+KONG_GE+"where"+KONG_GE+KEY_WORD+"=?";

        //删除所有的记录
        public static final String DELETE_ALL="delete from "+TABLE_NAME;
    }
}
