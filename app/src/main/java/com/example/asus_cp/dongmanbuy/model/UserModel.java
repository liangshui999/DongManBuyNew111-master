package com.example.asus_cp.dongmanbuy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户实体
 * Created by asus-cp on 2016-06-15.
 */
public class UserModel implements Parcelable{
    private String id;
    private String userName;
    private String userPhone;
    private String shouHuoArea;
    private String countryName;
    private String provinceName;
    private String cityName;
    private String districtName;//区名
    private String defaultAddress;//是否是默认地区，1表示是默认地址，0表示不是


    public UserModel() {
    }

    protected UserModel(Parcel in) {
        id = in.readString();
        userName = in.readString();
        userPhone = in.readString();
        shouHuoArea = in.readString();
        countryName = in.readString();
        provinceName = in.readString();
        cityName = in.readString();
        districtName = in.readString();
        defaultAddress = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getShouHuoArea() {
        return shouHuoArea;
    }

    public void setShouHuoArea(String shouHuoArea) {
        this.shouHuoArea = shouHuoArea;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userName);
        dest.writeString(userPhone);
        dest.writeString(shouHuoArea);
        dest.writeString(countryName);
        dest.writeString(provinceName);
        dest.writeString(cityName);
        dest.writeString(districtName);
        dest.writeString(defaultAddress);
    }
}
