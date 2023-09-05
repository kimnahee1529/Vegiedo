package com.devinsight.vegiedo.view.store;

import java.util.ArrayList;

public class UserReviewItem {
    public enum ItemType {
        STORE_DETAIL_PAGE
    }
    private ItemType itemType;
    private String title;
    private String description;
    private String content;
    private ArrayList<String> userReviewImageUrlList;

    public UserReviewItem(ItemType itemType, String title, String description, String content, ArrayList<String> userReviewImageUrlList, int ratingBar) {
        this.itemType = itemType;
        this.title = title;
        this.description = description;
        this.content = content;
        this.userReviewImageUrlList = userReviewImageUrlList;
        this.ratingBar = ratingBar;
    }
    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    private int ratingBar;


}