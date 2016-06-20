package com.example.asus_cp.dongmanbuy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 优惠券实体
 * Created by asus-cp on 2016-06-17.
 */
public class YouHuiQuanModel implements Parcelable{
    private String youHuiQuanJinE;//优惠券金额
    private String youHuiQuanName;//优惠券名称
    private String youHuiQuanTime;//优惠券时间

    protected YouHuiQuanModel(Parcel in) {
        youHuiQuanJinE = in.readString();
        youHuiQuanName = in.readString();
        youHuiQuanTime = in.readString();
    }

    public YouHuiQuanModel(){}

    public static final Creator<YouHuiQuanModel> CREATOR = new Creator<YouHuiQuanModel>() {
        @Override
        public YouHuiQuanModel createFromParcel(Parcel in) {
            return new YouHuiQuanModel(in);
        }

        @Override
        public YouHuiQuanModel[] newArray(int size) {
            return new YouHuiQuanModel[size];
        }
    };

    public String getYouHuiQuanJinE() {
        return youHuiQuanJinE;
    }

    public void setYouHuiQuanJinE(String youHuiQuanJinE) {
        this.youHuiQuanJinE = youHuiQuanJinE;
    }

    public String getYouHuiQuanName() {
        return youHuiQuanName;
    }

    public void setYouHuiQuanName(String youHuiQuanName) {
        this.youHuiQuanName = youHuiQuanName;
    }

    public String getYouHuiQuanTime() {
        return youHuiQuanTime;
    }

    public void setYouHuiQuanTime(String youHuiQuanTime) {
        this.youHuiQuanTime = youHuiQuanTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(youHuiQuanJinE);
        dest.writeString(youHuiQuanName);
        dest.writeString(youHuiQuanTime);
    }
}
