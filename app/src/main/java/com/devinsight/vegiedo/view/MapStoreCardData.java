package com.devinsight.vegiedo.view;

public class MapStoreCardData {


    private int storeImage;
    private int storeTag1;
    private int storeTag2;
    private int reviewNum;
    private int starlating;
    private int distance;
    private String storeName;
    private String address;
    private boolean like;

    public MapStoreCardData(int storeImage, int storeTag1, int storeTag2, int reviewer, int starlating, int distance, String storeName, String address, boolean like) {
        this.storeImage = storeImage;
        this.storeTag1 = storeTag1;
        this.storeTag2 = storeTag2;
        this.reviewNum = reviewer;
        this.starlating = starlating;
        this.distance = distance;
        this.storeName = storeName;
        this.address = address;
        this.like = like;
    }

    public int getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(int storeImage) {
        this.storeImage = storeImage;
    }

    public int getStoreTag1() {
        return storeTag1;
    }

    public void setStoreTag1(int storeTag1) {
        this.storeTag1 = storeTag1;
    }

    public int getStoreTag2() {
        return storeTag2;
    }

    public void setStoreTag2(int storeTag2) {
        this.storeTag2 = storeTag2;
    }

    public int getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(int reviewNum) {
        this.reviewNum = reviewNum;
    }

    public int getStarlating() {
        return starlating;
    }

    public void setStarlating(int starlating) {
        this.starlating = starlating;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
