package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostRegisterResponseDTO {

    @Expose
    @SerializedName("postId") private Long postId;
    @Expose
    @SerializedName("postTitle") private String postTitle;
    @Expose
    @SerializedName("userName") private String userName;
    @Expose
    @SerializedName("content") private String content;
    @Expose
    @SerializedName("createdAt") private String createdAt;
    @Expose
    @SerializedName("images") private List<String> images;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public PostRegisterResponseDTO(Long postId, String postTitle, String userName, String content, String createdAt, List<String> images) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.userName = userName;
        this.content = content;
        this.createdAt = createdAt;
        this.images = images;
    }
}
