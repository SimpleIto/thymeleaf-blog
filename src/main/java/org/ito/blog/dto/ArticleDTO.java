package org.ito.blog.dto;

import java.util.Date;

/**
 * 说是DTO，其实因为前后端糅合的项目
 * 该DTO并未用在前后端ajax数据传输上，而是thymeleaf渲染页面时，就使用了该类，其实也可以直接使用Article类
 * 创建DTO只是为了更分离一点，万一以后想要前后端分离呢，总需要一个单独的DTO返回给前端。为什么需要呢？看下面。
 * 顺带一提，当前后端分离后，用上ajax后，DTO作用也就出来了，在前后端传输时只传前端需要的字段，可以减少网络需求量
 */
public class ArticleDTO {
    private Integer id;
    private String title;
    private String content;
    private Date createdAt;
    private Integer commentNumber;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Integer getCommentNumber() {
        return commentNumber;
    }
    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
