package com.devinsight.vegiedo.data.response;

import java.util.List;

public class StoreInquiryResponseDTO {

    private Long storeId;
    private String storeName;
    private String address;
    private String detailAddress;
    private int stars;
    private boolean like;
    private boolean stamp;
    private List<String> images;
    private float x;
    private float y;

    public StoreInquiryResponseDTO(Long storeId, String storeName, String address, String detailAddress, int stars, boolean like, boolean stamp, List<String> images, float x, float y) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.stars = stars;
        this.like = like;
        this.stamp = stamp;
        this.images = images;
        this.x = x;
        this.y = y;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
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

    public boolean getStamp() {
        return stamp;
    }

    public void setStamp(boolean stamp) {
        this.stamp = stamp;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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
