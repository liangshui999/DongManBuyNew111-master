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
    public static final String SHOP_USER_ID_KEY ="categoryHome";//从店铺的分类到店铺主页传值用的key
    public static final String SHOP_MODEL_KEY ="shopDetail";//从店铺的主页到店铺的详情的传值
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
    public static final String DING_DAN_BIAN_HAO_LIST_KEY="";//传递订单编号集合用的key
    public static final String DING_DAN_BIAN_HAO_KEY="dingDanBianHao";//传递订单编号用的key
    public static final String DING_DAN_SUBJECT_KEY="dingDansubject";//传递订单编号用的key
    public static final String DING_DAN_DESC_KEY="dingDandesc";//传递订单编号用的key
    public static final String DING_DAN_ID_KEY="dingDanId";//传递订单id所用的key
    public static final String DING_DAN_MODEL_KEY="dingDanMoDS";//传递订单实体时使用的key
    public static final String SHOP_ID_KEY="shopIdKey";//店铺id的key
    public static final String USER_KEY="userKey";//传递user对象时使用的key
    public static final String SEX_KEY="sexKy";//性别key
    public static final String EMAIL_KEY="emailKey";//邮箱的key
    public static final String PHONE_KEY="phoneKey";//传递电话的key
    public static final String START_ADD_SHOU_HUO_ADDRESS_ACTIVTIY_KEY="addshouhuoaddress";//跳转到收获地址，传过去的key
    public static final String ADAPTER_KEY="adapterKey";//传递适配器的key
    public static final String TO_DING_DAN_LIST_KEY ="fromPersonalToDingDan";//跳转到订单列表界面传递的key
    public static final String ALL_DING_DAN="allDingDan";
    public static final String DAI_FU_KUAN_DING_DAN="daiFuKuanDingDan";//待付款的订单key
    public static final String DAI_SHOU_HUO_DING_DAN="daiShouHuoDingDan";
    public static final String DAI_PING_JIA_DING_DAN="daiPingJiaDingDan";
    public static final String SHEN_QING_JI_LU_MODEL_KEY="shenqingjilumodel";//传递申请记录modle的key
    public static final String SEARCH_CONTENT_KEY="searchKey";//传递搜索关键字用
    public static final String SEARCH_CATEGORY_KEY="searchCategoryKey";//传递搜索的类别用
    public static final String SAO_MIAO_RESULT_KEY="saoMiaoResult";//传递扫描结果的key
    public static final String YU_MING="http://www.zmobuy.com/";//网站的域名
    public static final String MAIN_ACTIVITY_LABLE_FALG_KEY="MAINactivityflag";//决定mainactivity切换到那个界面的标记
    public static final String SHOPPING_CAR_FLAG_KEY="shoppingCarFlagKey";//购物车的标记
    public static final String SHOPPING_STRRET_FLAG_KEY="shoppingStrret";//店铺街的标记
    public static final String HOME_FLAG_KEY="homeflagkey";//首页的标记
    public static final String USER_NAME="userName";//用户名
    public static final String PASS_WORD="password";//密码
    public static final String GOOD_TYPE_KEY="goodTypeKey";//商品类型
    public static final String DATA_SET_ACTIVITY_LABLE_FLAG_KEY="dataSetActivity";//跳转到资料设置界面传递的key
    public static final String SHENG_MING_KEY="shengMing";//省名key
    public static final String MY_LOCATION_KEY="myLocation";//我的位置的key
    public static final String DIAN_PU_LOCATION_KEY="dianPuLocation";//店铺位置的key
    public static final String ROUTE_LINE_KEY="routeLineKey";//传递线路规划的线路的key
    public static final String JIAO_TONG_KEY="jiaoTongKey";//交通方式的key
    public static final String GONG_JIAO_JIAO_TONG="gongjiao";//公交
    public static final String JIA_CHE_JIAO_TONG="jiaChe";//驾车
    public static final String BU_XING_JIAO_TONG="buXing";//步行
    public static final String XUAN_ZHONG_COUNT_KEY="xuanzhongcount";//从购物车向订单列表传递数据时，传递每个商品选中数据的集合
    public static final String SHOP_MODE_LIST_KEY="shopmodellistkey";//传递shopmodle集合时用
    public static final String GUIDE_SHAREPRENCE_NAME="guide";//引导页保存数据的shareprefrence的名字
    public static final String IS_FIRST_KEY="isfirst";//是否是第一次启动应用程序
    public static final String HOUR_KEY="hour";
    public static final String MINUTE_KEY="minute";
    public static final String SECOND_KEY="second";
    public static final String SESSION_ID_KEY="sessionId";//sessionId的key
    public static final String VOLLEY_LOG_TAG="MyVolley";//volley的tag
    public static final String CATEGORY_ID_KEY="categoryIdKey";//商品搜索时传递cateId的key
    public static final String SCREEN_WIDTH_KEY="widthKey";//屏幕宽度的key
    public static final String SCREEN_HEIGHT_KEY="heightKey";//屏幕高度的key
    public static final String SCREEN_DENSTY_DPI_KEY="dpiKey";//屏幕像素密度的key
    public static final String SCREEN_INFO_SHARE_NAME="screensharename";//存储屏幕信息的shareprefrence的名字
    public static final String GOOD_ID_KEY="goodidkey";//goodidkey
    public static final String CACH_SHAREPREFERENCE_NAME="cachShareName";//缓存首页，店铺街，购物车json数据
    public static final String GUANG_GAO_ONE_CACH_KEY="guangOne";//第一个广告条缓存
    public static final String GUANG_GAO_TWO_CACH_KEY="guangTwo";//第二个广告条缓存
    public static final String GUANG_GAO_THREE_CACH_KEY="guangThree";//第三个广告条缓存
    public static final String XIAN_SHI_MIAO_SHA_CACH_KEY="xianShiCach";//限时秒杀缓存
    public static final String JING_PING_TUI_JIAN_CACH_KEY="jingPinCach";//精品推荐缓存
    public static final String HOME_DIAN_PU_JIE_CACH_KEY="homeDianPuCach";//首页店铺街缓存
    public static final String CAI_NI_XI_HUAN_CACH_KEY="caiNiCach";//猜你喜欢缓存
    public static final String DIAN_PU_JIE_SHOPS_CACH_KEY="dianPuShopsCach";//店铺街的店铺列表缓存
    public static final String SHOPPING_CAR_LIST_CACH_KEY="shoppingCarListCach";//购物车列表缓存
    public static final String TUI_JIAN_GOOD_CACH_KEY="tuiJianCach";//购物车推荐商品缓存
    public static final String ADD_PIC_IMAGE_PATH="000000";//添加图片的按钮默认的path
    public static final String PREVIEW_INIT_POSTION_KEY="previewInitPositon";//预览图片时，初始页面的位置
    public static final String PREVIEW_PATHS_KEY="previewPathKey";//传递到预览图片界面路径的key



}
