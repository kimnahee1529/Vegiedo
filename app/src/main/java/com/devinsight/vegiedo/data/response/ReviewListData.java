package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewListData {

    @Expose
    @SerializedName("reviewId")private Long reviewId;
    @Expose
    @SerializedName("userName")private String userName;
    @Expose
    @SerializedName("star")private Integer star;
    @Expose
    @SerializedName("content")private String content;
    @Expose
    @SerializedName("images")private List<String> images;
    @Expose
    @SerializedName("isMine")private boolean isMine;

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public ReviewListData(Long reviewId, String userName, Integer star, String content, List<String> images) {
        this.reviewId = reviewId;
        this.userName = userName;
        this.star = star;
        this.content = content;
        this.images = images;
    }
}
