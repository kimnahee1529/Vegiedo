package com.devinsight.vegiedo.data.response;

import java.util.List;

public class StoreListData {

    private String storeId;
    private String storeName;
    private String address;
    private String detailAddress;
    private int distance;
    private List<String> tags;
    private int stars;
    private boolean like;
    private float x;
    private float y;

    public StoreListData(String storeId, String storeName, String address, String detailAddress, int distance, List<String> tags, int stars, boolean like, float x, float y) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.distance = distance;
        this.tags = tags;
        this.stars = stars;
        this.like = like;
        this.x = x;
        this.y = y;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
