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
    @SerializedName("stars")private Integer stars;
    @Expose
    @SerializedName("content")private String content;
    @Expose
    @SerializedName("images")private List<String> images;

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

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
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

    public ReviewListData(Long reviewId, String userName, Integer stars, String content, List<String> images) {
        this.reviewId = reviewId;
        this.userName = userName;
        this.stars = stars;
        this.content = content;
        this.images = images;
    }
}
