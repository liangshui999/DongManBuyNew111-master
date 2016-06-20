package com.example.asus_cp.dongmanbuy.model;

import java.util.List;

/**
 * 订单实体
 * Created by asus-cp on 2016-06-20.
 */
public class DingDanModel {
    private String orderId;//订单id
    private String orderBianHao;//订单编号
    private String orderTime;//订单时间
    private String sumPrice;//总价格
    private List<Good> goods;//商品列表
    private String shipFee;//快递费
    private String hongBao;//红包
    private String shouXuFei;//手续费
    private String payCode;//paycode
    private String dingDanDesc;//订单描述

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderBianHao() {
        return orderBianHao;
    }

    public void setOrderBianHao(String orderBianHao) {
        this.orderBianHao = orderBianHao;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(String sumPrice) {
        this.sumPrice = sumPrice;
    }

    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(List<Good> goods) {
        this.goods = goods;
    }

    public String getShipFee() {
        return shipFee;
    }

    public void setShipFee(String shipFee) {
        this.shipFee = shipFee;
    }

    public String getHongBao() {
        return hongBao;
    }

    public void setHongBao(String hongBao) {
        this.hongBao = hongBao;
    }

    public String getShouXuFei() {
        return shouXuFei;
    }

    public void setShouXuFei(String shouXuFei) {
        this.shouXuFei = shouXuFei;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getDingDanDesc() {
        return dingDanDesc;
    }

    public void setDingDanDesc(String dingDanDesc) {
        this.dingDanDesc = dingDanDesc;
    }
}
