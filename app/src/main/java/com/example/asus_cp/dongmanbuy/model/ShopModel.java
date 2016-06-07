package com.example.asus_cp.dongmanbuy.model;

import java.util.List;

/**
 * 店铺实体
 * Created by asus-cp on 2016-06-07.
 */
public class ShopModel {
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

    public String getCommenTrankFont() {
        return commenTrankScore;
    }

    public void setCommenTrankFont(String commenTrankScore) {
        this.commenTrankScore = commenTrankScore;
    }

    public String getCommentServerFont() {
        return commentServerScore;
    }

    public void setCommentServerFont(String commentServerScore) {
        this.commentServerScore = commentServerScore;
    }

    public String getCommentDeliveryFont() {
        return commentDeliveryScore;
    }

    public void setCommentDeliveryFont(String commentDeliveryScore) {
        this.commentDeliveryScore = commentDeliveryScore;
    }

    private String gazeNumber;//关注数
    private String gazeStatus;
    private List<Good> goods;

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

    public void setShopLogo(String shopLog) {
        this.shopLogo = shopLog;
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

    public String getCommentServer() {
        return commentServer;
    }

    public void setCommentServer(String commentServer) {
        this.commentServer = commentServer;
    }

    public String getCommenTrank() {
        return commenTrank;
    }

    public void setCommenTrank(String commenTrank) {
        this.commenTrank = commenTrank;
    }

    public String getCommentDelivery() {
        return commentDelivery;
    }

    public void setCommentDelivery(String commentDelivery) {
        this.commentDelivery = commentDelivery;
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
}
