package com.example.asus_cp.dongmanbuy.model;

/**
 * 商品规格的实体
 * Created by asus-cp on 2016-09-09.
 */
public class GuiGeModel {
    private String name;
    private String value;

    public GuiGeModel(){}

    public GuiGeModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
