package com.example.asus_cp.dongmanbuy.util;

import com.example.asus_cp.dongmanbuy.model.Good;

/**
 * 商品的帮助类
 * Created by asus-cp on 2016-08-22.
 */
public class MyGoodHelper {
    private static String tag="MyGoodHelper";

    /**
     * 获取商品的真实价格，有折扣价格就用折扣价格，没有折扣价格就用shopprice
     */
    public static String getRealPrice(Good good){
        MyLog.d(tag,"折扣价是："+good.getPromotePrice());
        MyLog.d(tag,"市场价是："+good.getShopPrice());
        String zheKouPrice = FormatHelper.getNumberFromRenMingBi(good.getPromotePrice());
        if (zheKouPrice==null || "0.00".equals(zheKouPrice) || "0".equals(zheKouPrice)) {
            return good.getShopPrice();
        } else {
            return good.getPromotePrice();
        }
    }
}
