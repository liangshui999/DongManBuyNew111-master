package com.example.asus_cp.dongmanbuy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 优惠券实体
 * Created by asus-cp on 2016-06-17.
 */
public class YouHuiQuanModel implements Parcelable{
    private String jinE;//优惠券金额
    private String name;//优惠券名称
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String suoShuDianPu;//优惠券所属的店铺
    private String status;//状态

    public YouHuiQuanModel(){}


    public String getJinE() {
        return jinE;
    }

    public void setJinE(String jinE) {
        this.jinE = jinE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSuoShuDianPu() {
        return suoShuDianPu;
    }

    public void setSuoShuDianPu(String suoShuDianPu) {
        this.suoShuDianPu = suoShuDianPu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    protected YouHuiQuanModel(Parcel in) {
        jinE = in.readString();
        name = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        suoShuDianPu = in.readString();
        status = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jinE);
        dest.writeString(name);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(suoShuDianPu);
        dest.writeString(status);
    }
}
