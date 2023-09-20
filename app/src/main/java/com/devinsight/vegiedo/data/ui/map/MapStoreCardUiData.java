package com.devinsight.vegiedo.data.ui.map;

import com.devinsight.vegiedo.data.ui.home.HomeReviewUiData;

public class MapStoreCardUiData {
    private Long storeId;
    private String storeImage;
    private String storeTag1;
    private String storeTag2;
    private int reviewNum;
    private int starRating;
    private int distance;
    private String storeName;
    private String address;
    private boolean like;
    private float latitude;
    private float longitude;

    public MapStoreCardUiData() {

    }
    public MapStoreCardUiData(Long storeId, String storeImage, String storeTag1, String storeTag2, int reviewer, int starRating, int distance, String storeName, String address, boolean like, float latitude, float longitude) {
        this.storeId = storeId;
        this.storeImage = storeImage;
        this.storeTag1 = storeTag1;
        this.storeTag2 = storeTag2;
        this.reviewNum = reviewer;
        this.starRating = starRating;
        this.distance = distance;
        this.storeName = storeName;
        this.address = address;
        this.like = like;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setData(Long storeId, String storeImage, String storeTag1, String storeTag2, int reviewer, int starRating,
                        int distance, String storeName, String address, boolean like, float latitude, float longitude) {
        this.storeId = storeId;
        this.storeImage = storeImage;
        this.storeTag1 = storeTag1;
        this.storeTag2 = storeTag2;
        this.reviewNum = reviewer;
        this.starRating = starRating;
        this.distance = distance;
        this.storeName = storeName;
        this.address = address;
        this.like = like;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getStoreTag1() {
        return storeTag1;
    }

    public void setStoreTag1(String storeTag1) {
        this.storeTag1 = storeTag1;
    }

    public String getStoreTag2() {
        return storeTag2;
    }

    public void setStoreTag2(String storeTag2) {
        this.storeTag2 = storeTag2;
    }

    public int getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(int reviewNum) {
        this.reviewNum = reviewNum;
    }

    public int getStarlating() {
        return starRating;
    }

    public void setStarlating(int starlating) {
        this.starRating = starlating;
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
