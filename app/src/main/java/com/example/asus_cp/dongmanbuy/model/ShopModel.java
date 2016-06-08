package com.example.asus_cp.dongmanbuy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 店铺实体
 * Created by asus-cp on 2016-06-07.
 */
public class ShopModel implements Parcelable{
    private String shopId;
    private String userId;//店铺的id
    private String shopName;
    private String shopLogo;//店铺log图片
    private String logoThumb;//logo的小图
    private String streetThumb;//街道的图片
    private String brandThumb;//品牌图片
    private String commenTrank;//商品评级
    private String commentServer;//服务评级
    private String commentDelivery;//时效评级
    private String commenTrankScore;//商品分数
    private String commentServerScore;//服务分数
    private String commentDeliveryScore;//时效分数
    private String allGoodsCount;//所有商品数量
    private String newGoodCount;//新品数量
    private String tuiJianGoodCount;//推荐商品数量
    private String cuXiaoGoodCount;//促销商品数量
    private String gazeNumber;//关注数
    private String gazeStatus;//关注状态
    private List<Good> goods;
    private String shopDesc;//店铺描述
    private String shopStartTime;//开店时间
    private String shopAddress;
    private String shopFlash;
    private String shopWangWang;
    private String shopQQ;
    private String shopTel;

    public ShopModel(){}


    protected ShopModel(Parcel in) {
        shopId = in.readString();
        userId = in.readString();
        shopName = in.readString();
        shopLogo = in.readString();
        logoThumb = in.readString();
        streetThumb = in.readString();
        brandThumb = in.readString();
        commenTrank = in.readString();
        commentServer = in.readString();
        commentDelivery = in.readString();
        commenTrankScore = in.readString();
        commentServerScore = in.readString();
        commentDeliveryScore = in.readString();
        allGoodsCount = in.readString();
        newGoodCount = in.readString();
        tuiJianGoodCount = in.readString();
        cuXiaoGoodCount = in.readString();
        gazeNumber = in.readString();
        gazeStatus = in.readString();
        goods = in.createTypedArrayList(Good.CREATOR);
        shopDesc = in.readString();
        shopStartTime = in.readString();
        shopAddress = in.readString();
        shopFlash = in.readString();
        shopWangWang = in.readString();
        shopQQ = in.readString();
        shopTel = in.readString();
    }

    public static final Creator<ShopModel> CREATOR = new Creator<ShopModel>() {
        @Override
        public ShopModel createFromParcel(Parcel in) {
            return new ShopModel(in);
        }

        @Override
        public ShopModel[] newArray(int size) {
            return new ShopModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopId);
        dest.writeString(userId);
        dest.writeString(shopName);
        dest.writeString(shopLogo);
        dest.writeString(logoThumb);
        dest.writeString(streetThumb);
        dest.writeString(brandThumb);
        dest.writeString(commenTrank);
        dest.writeString(commentServer);
        dest.writeString(commentDelivery);
        dest.writeString(commenTrankScore);
        dest.writeString(commentServerScore);
        dest.writeString(commentDeliveryScore);
        dest.writeString(allGoodsCount);
        dest.writeString(newGoodCount);
        dest.writeString(tuiJianGoodCount);
        dest.writeString(cuXiaoGoodCount);
        dest.writeString(gazeNumber);
        dest.writeString(gazeStatus);
        dest.writeTypedList(goods);
        dest.writeString(shopDesc);
        dest.writeString(shopStartTime);
        dest.writeString(shopAddress);
        dest.writeString(shopFlash);
        dest.writeString(shopWangWang);
        dest.writeString(shopQQ);
        dest.writeString(shopTel);
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getLogoThumb() {
        return logoThumb;
    }

    public void setLogoThumb(String logoThumb) {
        this.logoThumb = logoThumb;
    }

    public String getStreetThumb() {
        return streetThumb;
    }

    public void setStreetThumb(String streetThumb) {
        this.streetThumb = streetThumb;
    }

    public String getBrandThumb() {
        return brandThumb;
    }

    public void setBrandThumb(String brandThumb) {
        this.brandThumb = brandThumb;
    }

    public String getCommenTrank() {
        return commenTrank;
    }

    public void setCommenTrank(String commenTrank) {
        this.commenTrank = commenTrank;
    }

    public String getCommentServer() {
        return commentServer;
    }

    public void setCommentServer(String commentServer) {
        this.commentServer = commentServer;
    }

    public String getCommentDelivery() {
        return commentDelivery;
    }

    public void setCommentDelivery(String commentDelivery) {
        this.commentDelivery = commentDelivery;
    }

    public String getCommenTrankScore() {
        return commenTrankScore;
    }

    public void setCommenTrankScore(String commenTrankScore) {
        this.commenTrankScore = commenTrankScore;
    }

    public String getCommentServerScore() {
        return commentServerScore;
    }

    public void setCommentServerScore(String commentServerScore) {
        this.commentServerScore = commentServerScore;
    }

    public String getCommentDeliveryScore() {
        return commentDeliveryScore;
    }

    public void setCommentDeliveryScore(String commentDeliveryScore) {
        this.commentDeliveryScore = commentDeliveryScore;
    }

    public String getAllGoodsCount() {
        return allGoodsCount;
    }

    public void setAllGoodsCount(String allGoodsCount) {
        this.allGoodsCount = allGoodsCount;
    }

    public String getNewGoodCount() {
        return newGoodCount;
    }

    public void setNewGoodCount(String newGoodCount) {
        this.newGoodCount = newGoodCount;
    }

    public String getTuiJianGoodCount() {
        return tuiJianGoodCount;
    }

    public void setTuiJianGoodCount(String tuiJianGoodCount) {
        this.tuiJianGoodCount = tuiJianGoodCount;
    }

    public String getCuXiaoGoodCount() {
        return cuXiaoGoodCount;
    }

    public void setCuXiaoGoodCount(String cuXiaoGoodCount) {
        this.cuXiaoGoodCount = cuXiaoGoodCount;
    }

    public String getGazeNumber() {
        return gazeNumber;
    }

    public void setGazeNumber(String gazeNumber) {
        this.gazeNumber = gazeNumber;
    }

    public String getGazeStatus() {
        return gazeStatus;
    }

    public void setGazeStatus(String gazeStatus) {
        this.gazeStatus = gazeStatus;
    }

    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(List<Good> goods) {
        this.goods = goods;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public String getShopStartTime() {
        return shopStartTime;
    }

    public void setShopStartTime(String shopStartTime) {
        this.shopStartTime = shopStartTime;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopFlash() {
        return shopFlash;
    }

    public void setShopFlash(String shopFlash) {
        this.shopFlash = shopFlash;
    }

    public String getShopWangWang() {
        return shopWangWang;
    }

    public void setShopWangWang(String shopWangWang) {
        this.shopWangWang = shopWangWang;
    }

    public String getShopQQ() {
        return shopQQ;
    }

    public void setShopQQ(String shopQQ) {
        this.shopQQ = shopQQ;
    }

    public String getShopTel() {
        return shopTel;
    }

    public void setShopTel(String shopTel) {
        this.shopTel = shopTel;
    }
}
