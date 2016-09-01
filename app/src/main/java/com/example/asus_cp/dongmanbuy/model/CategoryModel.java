package com.example.asus_cp.dongmanbuy.model;

/**
 * 类别的实体
 * Created by asus-cp on 2016-05-25.
 */
public class CategoryModel {
    private String categoryId;
    private String categoryName;
    private int categoryImage;

    public CategoryModel() {
    }

    public CategoryModel(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public CategoryModel(String categoryId, String categoryName, int categoryImage) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
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

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }
}
