package com.example.asus_cp.dongmanbuy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商品实体
 * Created by asus-cp on 2016-05-20.
 */
public class Good implements Parcelable{
    private String goodId;
    private String categoryId;//类别的id
    private String userId;
    private String recId;
    private String goodName;
    private String warehousePrice;
    private String warehousePromotePrice;
    private String regionPrice;
    private String regionPromotePrice;
    private String model_price;
    private String model_attr;
    private String goods_name_style;
    private String commentsNumber;//评论数
    private String salesVolume;//销量
    private String market_price;//市场价
    private String isNew;
    private String isBest;
    private String isHot;
    private String goodsNumber;//库存
    private String orgPrice;//原始价
    private String shopPrice;//商店价
    private String promotePrice;//促销价
    private String goodType;
    private String promoteStartDate;//促销开始时间
    private String promoteEndDate;//促销结束时间
    private String isPromote;
    private String goodsBrief;
    private String goodsThumb;//缩略图
    private String goodsImg;//大图
    private String goodsSmallImag;//小图
    private String shoppingCarNumber;//购物车中该商品数量
    private String dingDanNumber;//在订单中该商品的数量
    private String dingDanSumPrice;//在订单列表中该商品的总价

    public Good(){}
    protected Good(Parcel in) {
        goodId = in.readString();
        categoryId=in.readString();
        userId = in.readString();
        recId=in.readString();
        goodName = in.readString();
        warehousePrice = in.readString();
        warehousePromotePrice = in.readString();
        regionPrice = in.readString();
        regionPromotePrice = in.readString();
        model_price = in.readString();
        model_attr = in.readString();
        goods_name_style = in.readString();
        commentsNumber = in.readString();
        salesVolume = in.readString();
        market_price = in.readString();
        isNew = in.readString();
        isBest = in.readString();
        isHot = in.readString();
        goodsNumber = in.readString();
        orgPrice = in.readString();
        shopPrice = in.readString();
        promotePrice = in.readString();
        goodType = in.readString();
        promoteStartDate = in.readString();
        promoteEndDate = in.readString();
        isPromote = in.readString();
        goodsBrief = in.readString();
        goodsThumb = in.readString();
        goodsImg = in.readString();
        goodsSmallImag = in.readString();
        shoppingCarNumber=in.readString();
        dingDanNumber=in.readString();
        dingDanSumPrice=in.readString();
    }

    public static final Creator<Good> CREATOR = new Creator<Good>() {
        @Override
        public Good createFromParcel(Parcel in) {
            return new Good(in);
        }

        @Override
        public Good[] newArray(int size) {
            return new Good[size];
        }
    };

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getWarehousePrice() {
        return warehousePrice;
    }

    public void setWarehousePrice(String warehousePrice) {
        this.warehousePrice = warehousePrice;
    }

    public String getWarehousePromotePrice() {
        return warehousePromotePrice;
    }

    public void setWarehousePromotePrice(String warehousePromotePrice) {
        this.warehousePromotePrice = warehousePromotePrice;
    }

    public String getRegionPrice() {
        return regionPrice;
    }

    public void setRegionPrice(String regionPrice) {
        this.regionPrice = regionPrice;
    }

    public String getRegionPromotePrice() {
        return regionPromotePrice;
    }

    public void setRegionPromotePrice(String regionPromotePrice) {
        this.regionPromotePrice = regionPromotePrice;
    }

    public String getModel_price() {
        return model_price;
    }

    public void setModel_price(String model_price) {
        this.model_price = model_price;
    }

    public String getGoods_name_style() {
        return goods_name_style;
    }

    public void setGoods_name_style(String goods_name_style) {
        this.goods_name_style = goods_name_style;
    }

    public String getModel_attr() {
        return model_attr;
    }

    public void setModel_attr(String model_attr) {
        this.model_attr = model_attr;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(String commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getIsBest() {
        return isBest;
    }

    public void setIsBest(String isBest) {
        this.isBest = isBest;
    }

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getOrgPrice() {
        return orgPrice;
    }

    public void setOrgPrice(String orgPrice) {
        this.orgPrice = orgPrice;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getPromotePrice() {
        return promotePrice;
    }

    public void setPromotePrice(String promotePrice) {
        this.promotePrice = promotePrice;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public String getPromoteStartDate() {
        return promoteStartDate;
    }

    public void setPromoteStartDate(String promoteStartDate) {
        this.promoteStartDate = promoteStartDate;
    }

    public String getPromoteEndDate() {
        return promoteEndDate;
    }

    public void setPromoteEndDate(String promoteEndDate) {
        this.promoteEndDate = promoteEndDate;
    }

    public String getIsPromote() {
        return isPromote;
    }

    public void setIsPromote(String isPromote) {
        this.isPromote = isPromote;
    }

    public String getGoodsBrief() {
        return goodsBrief;
    }

    public void setGoodsBrief(String goodsBrief) {
        this.goodsBrief = goodsBrief;
    }

    public String getGoodsThumb() {
        return goodsThumb;
    }

    public void setGoodsThumb(String goodsThumb) {
        this.goodsThumb = goodsThumb;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsSmallImag() {
        return goodsSmallImag;
    }

    public void setGoodsSmallImag(String goodsSmallImag) {
        this.goodsSmallImag = goodsSmallImag;
    }

    public String getShoppingCarNumber() {
        return shoppingCarNumber;
    }

    public void setShoppingCarNumber(String shoppingCarNumber) {
        this.shoppingCarNumber = shoppingCarNumber;
    }

    public String getDingDanNumber() {
        return dingDanNumber;
    }

    public void setDingDanNumber(String dingDanNumber) {
        this.dingDanNumber = dingDanNumber;
    }

    public String getDingDanSumPrice() {
        return dingDanSumPrice;
    }

    public void setDingDanSumPrice(String dingDanSumPrice) {
        this.dingDanSumPrice = dingDanSumPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goodId);
        dest.writeString(categoryId);
        dest.writeString(userId);
        dest.writeString(recId);
        dest.writeString(goodName);
        dest.writeString(warehousePrice);
        dest.writeString(warehousePromotePrice);
        dest.writeString(regionPrice);
        dest.writeString(regionPromotePrice);
        dest.writeString(model_price);
        dest.writeString(model_attr);
        dest.writeString(goods_name_style);
        dest.writeString(commentsNumber);
        dest.writeString(salesVolume);
        dest.writeString(market_price);
        dest.writeString(isNew);
        dest.writeString(isBest);
        dest.writeString(isHot);
        dest.writeString(goodsNumber);
        dest.writeString(orgPrice);
        dest.writeString(shopPrice);
        dest.writeString(promotePrice);
        dest.writeString(goodType);
        dest.writeString(promoteStartDate);
        dest.writeString(promoteEndDate);
        dest.writeString(isPromote);
        dest.writeString(goodsBrief);
        dest.writeString(goodsThumb);
        dest.writeString(goodsImg);
        dest.writeString(goodsSmallImag);
        dest.writeString(shoppingCarNumber);
        dest.writeString(dingDanNumber);
        dest.writeString(dingDanSumPrice);
    }

}
