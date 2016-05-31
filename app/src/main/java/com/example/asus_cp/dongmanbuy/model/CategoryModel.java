package com.example.asus_cp.dongmanbuy.model;

/**
 * 类别的实体
 * Created by asus-cp on 2016-05-25.
 */
public class CategoryModel {
    private String categoryId;
    private String categoryName;

    public CategoryModel(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
