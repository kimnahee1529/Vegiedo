package com.devinsight.vegiedo.data.response;

import java.util.List;

public class ReviewListData {

    private Long reviewId;
    private String userName;
    private Integer stars;
    private String content;
    private List<String> images;

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
