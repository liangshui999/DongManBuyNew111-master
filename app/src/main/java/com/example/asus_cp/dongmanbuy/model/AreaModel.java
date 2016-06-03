package com.example.asus_cp.dongmanbuy.model;

/**
 * 地区的实体
 * Created by asus-cp on 2016-06-03.
 */
public class AreaModel {
    private String id;//地区id
    private String name;//地区名称

    public AreaModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public AreaModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
