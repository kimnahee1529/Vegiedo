package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PostListData {
    @Expose
    @SerializedName("postId")private Long postId;
    @Expose
    @SerializedName("postTitle")private String postTitle;
    @Expose
    @SerializedName("userName")private String userName;
    @Expose
    @SerializedName("createdAt")private String createdAt;
    @Expose
    @SerializedName("like")private Boolean like;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public PostListData(Long postId, String postTitle, String userName, String createdAt, Boolean like) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.userName = userName;
        this.createdAt = createdAt;
        this.like = like;
    }
}
