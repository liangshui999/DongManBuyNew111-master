package com.example.asus_cp.dongmanbuy.model;

/**
 * 广告的实体
 * Created by asus-cp on 2016-05-23.
 */
public class Binner {
    private String id;
    private String img;//图片的url

    public Binner(String id, String img) {
        this.id = id;
        this.img = img;
    }

    public Binner() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
