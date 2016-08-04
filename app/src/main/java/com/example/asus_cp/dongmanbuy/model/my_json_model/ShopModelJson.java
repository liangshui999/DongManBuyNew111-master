package com.example.asus_cp.dongmanbuy.model.my_json_model;

import java.util.List;

/**
 * 转换成json用的shopmodel
 * Created by asus-cp on 2016-08-03.
 */
public class ShopModelJson {
    private String shipping_id;
    private String postscript;
    private String goods_amount;//该店铺所有商品的总价格
    private List<String> goods_id;
    private String shipping_fee;

    public String getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(String shipping_id) {
        this.shipping_id = shipping_id;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }

    public List<String> getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(List<String> goods_id) {
        this.goods_id = goods_id;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }
}
