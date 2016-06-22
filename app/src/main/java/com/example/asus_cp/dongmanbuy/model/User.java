package com.example.asus_cp.dongmanbuy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 个人中心，用户的所有信息
 * Created by asus-cp on 2016-06-22.
 */
public class User implements Parcelable{
    private String Id;//用户id
    private String name;
    private String email;
    private String money;//余额
    private String jiFen;//积分
    private String youHuiQuanShu;//优惠券数目
    private String phone;//手机号码
    private String pic;//头像
    private String sex;//性别
    private String bankCards;//银行卡张数
    private String hongBao;//红包个数
    private String rankName;//等级名称
    private String shouCangShu;//收藏数
    private String guanZhuShu;//关注数
    private String awaitPay;//待支付
    private String awaitShip;//待收货
    private String awaitComment;//待评论

    public User(){}

    protected User(Parcel in) {
        Id = in.readString();
        name = in.readString();
        email = in.readString();
        money = in.readString();
        jiFen = in.readString();
        youHuiQuanShu = in.readString();
        phone = in.readString();
        pic = in.readString();
        sex = in.readString();
        bankCards = in.readString();
        hongBao = in.readString();
        rankName = in.readString();
        shouCangShu = in.readString();
        guanZhuShu = in.readString();
        awaitPay = in.readString();
        awaitShip = in.readString();
        awaitComment = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getJiFen() {
        return jiFen;
    }

    public void setJiFen(String jiFen) {
        this.jiFen = jiFen;
    }

    public String getYouHuiQuanShu() {
        return youHuiQuanShu;
    }

    public void setYouHuiQuanShu(String youHuiQuanShu) {
        this.youHuiQuanShu = youHuiQuanShu;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBankCards() {
        return bankCards;
    }

    public void setBankCards(String bankCards) {
        this.bankCards = bankCards;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHongBao() {
        return hongBao;
    }

    public void setHongBao(String hongBao) {
        this.hongBao = hongBao;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getShouCangShu() {
        return shouCangShu;
    }

    public void setShouCangShu(String shouCangShu) {
        this.shouCangShu = shouCangShu;
    }

    public String getGuanZhuShu() {
        return guanZhuShu;
    }

    public void setGuanZhuShu(String guanZhuShu) {
        this.guanZhuShu = guanZhuShu;
    }

    public String getAwaitPay() {
        return awaitPay;
    }

    public void setAwaitPay(String awaitPay) {
        this.awaitPay = awaitPay;
    }

    public String getAwaitShip() {
        return awaitShip;
    }

    public void setAwaitShip(String awaitShip) {
        this.awaitShip = awaitShip;
    }

    public String getAwaitComment() {
        return awaitComment;
    }

    public void setAwaitComment(String awaitComment) {
        this.awaitComment = awaitComment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(money);
        dest.writeString(jiFen);
        dest.writeString(youHuiQuanShu);
        dest.writeString(phone);
        dest.writeString(pic);
        dest.writeString(sex);
        dest.writeString(bankCards);
        dest.writeString(hongBao);
        dest.writeString(rankName);
        dest.writeString(shouCangShu);
        dest.writeString(guanZhuShu);
        dest.writeString(awaitPay);
        dest.writeString(awaitShip);
        dest.writeString(awaitComment);
    }
}
