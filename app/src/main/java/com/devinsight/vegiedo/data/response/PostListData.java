package com.devinsight.vegiedo.data.response;

import java.util.Date;

public class PostListData {
    private Long postId;
    private String postTitle;
    private String userName;
    private Date createdAt;
    private Boolean like;

    public PostListData(Long postId, String postTitle, String userName, Date createdAt, Boolean like) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.userName = userName;
        this.createdAt = createdAt;
        this.like = like;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }
}
