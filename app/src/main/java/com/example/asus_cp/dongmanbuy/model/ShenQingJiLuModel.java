package com.example.asus_cp.dongmanbuy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 申请记录的实体
 * Created by asus-cp on 2016-06-28.
 */
public class ShenQingJiLuModel implements Parcelable{

    private String id;
    private String userId;
    private String jinE;//金额
    private String time;//添加时间
    private String managerNote;//管理员备注
    private String type;//交易方式
    private String userNote;//用户备注
    private String shouXuFei;//手续费
    private String status;//交易状态

    public ShenQingJiLuModel() {
    }

    protected ShenQingJiLuModel(Parcel in) {
        id = in.readString();
        userId = in.readString();
        jinE = in.readString();
        time = in.readString();
        managerNote = in.readString();
        type = in.readString();
        userNote = in.readString();
        shouXuFei = in.readString();
        status = in.readString();
    }

    public static final Creator<ShenQingJiLuModel> CREATOR = new Creator<ShenQingJiLuModel>() {
        @Override
        public ShenQingJiLuModel createFromParcel(Parcel in) {
            return new ShenQingJiLuModel(in);
        }

        @Override
        public ShenQingJiLuModel[] newArray(int size) {
            return new ShenQingJiLuModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJinE() {
        return jinE;
    }

    public void setJinE(String jinE) {
        this.jinE = jinE;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getManagerNote() {
        return managerNote;
    }

    public void setManagerNote(String managerNote) {
        this.managerNote = managerNote;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public String getShouXuFei() {
        return shouXuFei;
    }

    public void setShouXuFei(String shouXuFei) {
        this.shouXuFei = shouXuFei;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(jinE);
        dest.writeString(time);
        dest.writeString(managerNote);
        dest.writeString(type);
        dest.writeString(userNote);
        dest.writeString(shouXuFei);
        dest.writeString(status);
    }
}
