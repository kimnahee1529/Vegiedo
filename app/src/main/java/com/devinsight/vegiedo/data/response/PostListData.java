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
    @SerializedName("imageUrl")private String imageUrl;
    @Expose
    @SerializedName("likeReceiveCount")private int like;
    @Expose
    @SerializedName("commentCount")private int commentCount;

    @Expose
    @SerializedName("totalPage") private int totalPage;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public PostListData(Long postId, String postTitle, String userName, String createdAt, String imageUrl, int like, int commentCount, int totalPage) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.userName = userName;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
        this.like = like;
        this.commentCount = commentCount;
        this.totalPage = totalPage;
    }
}
