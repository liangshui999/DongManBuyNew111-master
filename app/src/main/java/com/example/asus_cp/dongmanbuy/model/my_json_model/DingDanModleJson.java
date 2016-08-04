package com.example.asus_cp.dongmanbuy.model.my_json_model;

import java.util.List;

/**
 * 提交订单接口里面上传给服务器的shop里面的内容,应该只有一个店铺列表的集合,不能有其他的字段
 * Created by asus-cp on 2016-08-03.
 */
public class DingDanModleJson {

    /*{"shipping_fee":"20.00","postscript":"蹇彂璐?,"goods_amount":"222"," +
            ""all_goods":[{"shipping_id":"2","goods_id":["533","534"]}," +
            "{"shipping_id":"14","goods_id":["533","534"]}]}*/
    /*private String shipping_fee;
    private String postscript;
    private String goods_amount;*/
    private List<ShopModelJson> all_goods;

    /*public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
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
    }*/

    public List<ShopModelJson> getAll_goods() {
        return all_goods;
    }

    public void setAll_goods(List<ShopModelJson> all_goods) {
        this.all_goods = all_goods;
    }
}
