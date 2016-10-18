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
    private String shouHuoRenName;//收货人姓名
    private String phone;//收获人电话
    private String sheng;//省
    private String shi;//市
    private String xian;//县
    private String detailAddress;//详细地址
    private String orderStatus;
    private String shippingStatus;
    private String payStatus;
    private String shipName;//快递方式
    private String zhiFuFangShi;//支付方式
    private String faPiaoTaiTou;//发票抬头
    private String faPiaoContent;//发票内容
    private String goodsSumPrice;//goods总价,不包含运费
    private String maiJiaLiuYan;//买家留言
    private String shopName;//店铺名称

    public DingDanModel(){}


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
        shouHuoRenName = in.readString();
        phone = in.readString();
        sheng = in.readString();
        shi = in.readString();
        xian = in.readString();
        detailAddress = in.readString();
        orderStatus = in.readString();
        shippingStatus = in.readString();
        payStatus = in.readString();
        shipName = in.readString();
        zhiFuFangShi = in.readString();
        faPiaoTaiTou = in.readString();
        faPiaoContent = in.readString();
        goodsSumPrice = in.readString();
        maiJiaLiuYan = in.readString();
        shopName=in.readString();
    }

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
        dest.writeString(shouHuoRenName);
        dest.writeString(phone);
        dest.writeString(sheng);
        dest.writeString(shi);
        dest.writeString(xian);
        dest.writeString(detailAddress);
        dest.writeString(orderStatus);
        dest.writeString(shippingStatus);
        dest.writeString(payStatus);
        dest.writeString(shipName);
        dest.writeString(zhiFuFangShi);
        dest.writeString(faPiaoTaiTou);
        dest.writeString(faPiaoContent);
        dest.writeString(goodsSumPrice);
        dest.writeString(maiJiaLiuYan);
        dest.writeString(shopName);
    }

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

    public String getShouHuoRenName() {
        return shouHuoRenName;
    }

    public void setShouHuoRenName(String shouHuoRenName) {
        this.shouHuoRenName = shouHuoRenName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSheng() {
        return sheng;
    }

    public void setSheng(String sheng) {
        this.sheng = sheng;
    }

    public String getShi() {
        return shi;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    public String getXian() {
        return xian;
    }

    public void setXian(String xian) {
        this.xian = xian;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getZhiFuFangShi() {
        return zhiFuFangShi;
    }

    public void setZhiFuFangShi(String zhiFuFangShi) {
        this.zhiFuFangShi = zhiFuFangShi;
    }

    public String getFaPiaoTaiTou() {
        return faPiaoTaiTou;
    }

    public void setFaPiaoTaiTou(String faPiaoTaiTou) {
        this.faPiaoTaiTou = faPiaoTaiTou;
    }

    public String getFaPiaoContent() {
        return faPiaoContent;
    }

    public void setFaPiaoContent(String faPiaoContent) {
        this.faPiaoContent = faPiaoContent;
    }

    public String getGoodsSumPrice() {
        return goodsSumPrice;
    }

    public void setGoodsSumPrice(String goodsSumPrice) {
        this.goodsSumPrice = goodsSumPrice;
    }

    public String getMaiJiaLiuYan() {
        return maiJiaLiuYan;
    }

    public void setMaiJiaLiuYan(String maiJiaLiuYan) {
        this.maiJiaLiuYan = maiJiaLiuYan;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
