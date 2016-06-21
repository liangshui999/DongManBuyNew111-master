package com.example.asus_cp.dongmanbuy.constant;

/**
 * 主要用于存放跨类的常量
 * Created by asus-cp on 2016-06-03.
 */
public interface MyConstant {
    public static final String USER_SHAREPREFRENCE_NAME="user";
    public static final String UID_KEY="uidkey";
    public static final String SID_KEY="sidkey";
    public static final String START_LOGIN_ACTIVITY_FLAG_KEY="startloginkey";//开启登陆活动时，其他活动传过去的标志
    public static final String YOU_TU_PING_JIA_KEY="youTuPingJia";//有图评价的key
    public static final String YOU_TU_PING_JIA_CONTENT="youTu";//有图评价的内容
    public static final String FROM_SHOP_CATEGORY_TO_CATEGORY_HOME_KEY="categoryHome";//从店铺的分类到店铺主页传值用的key
    public static final String FROM_SHOP_HOME_TO_SHOP_DETAIL_KEY="shopDetail";//从店铺的主页到店铺的详情的传值
    public static final String FROM_SHOP_HOME_TO_SHOP_PRODUCT_SORT_KEY="productSort";//从店铺主页跳到店铺商品排序的界面的key
    public static final String GOOD_KEY="good_key";//向商品详情传数据时所用的key
    public static final String KU_CUN_KEY="kuCunKey";//从商品详情向购物车传递库存数量时使用
    public static final String USER_MODLE_KEY="userModel";//传递usermodel对象时使用的key
    public static final String KONG_GE=" ";//空格
    public static final String GOOD_LIST_KEY="goodListKey";//传递商品集合的key
    public static final String ITEM_PRODUCT_COUNT_KEY="itemProductCount";//小项的商品数目的key，购物车传递到订单里面
    public static final String FA_PIAO_TAI_TOU_KEY="fapiaotaitoukey";//发票活动向订单返回数据时的key
    public static final String FA_PIAO_CONTENT_KEY="fapiaocontentkey";//发票活动向订单返回数据时的key
    public static final String YOU_HUI_QUAN_MODEL_KEY ="youHuiQuanKey";//传递优惠券实体时使用
    public static final String YOU_HUI_QUAN_SHU_MU_KEY="youhuishumu";//传递优惠券数目时使用
    public static final String PRODUCT_PRICE_SUM_KEY="pricesumkey";//传递商品总价格时的key
    public static final String PRODUCT_SHU_MU_SUM_KEY="shumucount";//传递商品总数时的key
    public static final String SHI_FU_KUAN_KEY="shifukuan";//传递实付款时所用的key
    public static final String DING_DAN_BIAN_HAO_KEY="dingDanBianHao";//传递订单编号用的key
    public static final String DING_DAN_MODEL_KEY="dingDanMoDS";//传递订单实体时使用的key
    public static final String SHOP_ID_KEY="shopIdKey";//店铺id的key
}
