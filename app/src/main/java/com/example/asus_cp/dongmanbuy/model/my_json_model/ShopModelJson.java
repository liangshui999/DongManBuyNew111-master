package com.example.asus_cp.dongmanbuy.model.my_json_model;

import java.util.List;

/**
 * 转换成json用的shopmodel
 * Created by asus-cp on 2016-08-03.
 */
public class ShopModelJson {
    private String shipping_id;
    private List<String> goods_id;

    public String getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(String shipping_id) {
        this.shipping_id = shipping_id;
    }

    public List<String> getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(List<String> goods_id) {
        this.goods_id = goods_id;
    }
}
