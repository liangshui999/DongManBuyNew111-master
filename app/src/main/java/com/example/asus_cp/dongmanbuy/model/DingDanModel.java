package com.example.asus_cp.dongmanbuy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 订单实体
 * Created by asus-cp on 2016-06-20.
 */
public class DingDanModel implements Parcelable{
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

    protected DingDanModel(Parcel in) {
        orderId = in.readString();
        orderBianHao = in.readString();
        orderTime = in.readString();
        sumPrice = in.readString();
        goods = in.createTypedArrayList(Good.CREATOR);
        shipFee = in.readString();
        hongBao = in.readString();
        shouXuFei = in.readString();
        payCode = in.readString();
        dingDanDesc = in.readString();
    }

    public DingDanModel(){}

    public static final Creator<DingDanModel> CREATOR = new Creator<DingDanModel>() {
        @Override
        public DingDanModel createFromParcel(Parcel in) {
            return new DingDanModel(in);
        }

        @Override
        public DingDanModel[] newArray(int size) {
            return new DingDanModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(orderBianHao);
        dest.writeString(orderTime);
        dest.writeString(sumPrice);
        dest.writeTypedList(goods);
        dest.writeString(shipFee);
        dest.writeString(hongBao);
        dest.writeString(shouXuFei);
        dest.writeString(payCode);
        dest.writeString(dingDanDesc);
    }
}
