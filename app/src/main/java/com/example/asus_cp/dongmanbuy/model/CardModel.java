package com.example.asus_cp.dongmanbuy.model;

/**
 * 银行卡的实体
 * Created by asus-cp on 2016-06-27.
 */
public class CardModel {

    private String id;
    private String bankName;//银行名称
    private String kaHao;//卡号
    private String bankRegion;//银行所在的地区
    private String userName;//银行卡所属的用户名
    private String userId;//用户id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getKaHao() {
        return kaHao;
    }

    public void setKaHao(String kaHao) {
        this.kaHao = kaHao;
    }

    public String getBankRegion() {
        return bankRegion;
    }

    public void setBankRegion(String bankRegion) {
        this.bankRegion = bankRegion;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
