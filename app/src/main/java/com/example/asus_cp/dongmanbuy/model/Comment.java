package com.example.asus_cp.dongmanbuy.model;

/**
 * 评论的实体
 * Created by asus-cp on 2016-06-01.
 */
public class Comment {
    private String id;//评论的id
    private String author;
    private String content;
    private String createTime;//评论创建的时间
    private String reContent;//再次评论的内容

    public Comment(String id, String author, String content, String reContent, String createTime) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.reContent = reContent;
        this.createTime = createTime;
    }

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReContent() {
        return reContent;
    }

    public void setReContent(String reContent) {
        this.reContent = reContent;
    }
}
