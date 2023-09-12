package com.devinsight.vegiedo.view.store;

import java.util.ArrayList;
import java.util.List;

public class UserReviewItem {
    public enum ItemType {
        STORE_DETAIL_REVIEW_PAGE,
        STORE_DETAIL_BLOG_REVIEW_PAGE,
        REPORT_COMPELETE,
        AD_TYPE,
        REVIEW_RC,
        AD_BANNER
    }
    private ItemType itemType;
    private String userName;
    private String content;
    private Integer ratingBar;
    private Long storeId;
    private Long reviewId;
    private ArrayList<String> userReviewImageUrlList;

    //별점 있는 리뷰
    public UserReviewItem(Long reviewId, ItemType itemType, String userName, int ratingBar, String content, ArrayList<String> userReviewImageUrlList) {
        this.reviewId = reviewId;
        this.itemType = itemType;
        this.userName = userName;
        this.ratingBar = ratingBar;
        this.content = content;
        this.userReviewImageUrlList = userReviewImageUrlList;
    }

    //별점 없는 블로그 리뷰
    public UserReviewItem(Long reviewId, ItemType itemType, String userName, String content, ArrayList<String> userReviewImageUrlList) {
        this.reviewId = reviewId;
        this.itemType = itemType;
        this.userName = userName;
        this.content = content;
        this.userReviewImageUrlList = userReviewImageUrlList;
    }

    // For AdMob items
    public UserReviewItem(ItemType itemType) {
        this.itemType = itemType;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }
    public String getTitle() {
        return userName;
    }

    public void setTitle(String title) {
        this.userName = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getUserReviewImageUrlList() {
        return userReviewImageUrlList;
    }

    public void setUserReviewImageUrlList(ArrayList<String> userReviewImageUrlList) {
        this.userReviewImageUrlList = userReviewImageUrlList;
    }

    public int getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(int ratingBar) {
        this.ratingBar = ratingBar;
    }


}