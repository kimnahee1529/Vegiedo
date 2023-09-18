package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreStampDetailDTO {
    @Expose
    @SerializedName("storeId") private Long storeId;
    @Expose
    @SerializedName("storeName") private String storeName;
    @Expose
    @SerializedName("address") private String address;
    @Expose
    @SerializedName("stars") private int stars;
    @Expose
    @SerializedName("reviewCount") private int reviewCount;
    @Expose
    @SerializedName("images") private String images;

    // Constructor
    public StoreStampDetailDTO(Long storeId, String storeName, String address, int stars, int reviewCount, String images) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
        this.stars = stars;
        this.reviewCount = reviewCount;
        this.images = images;
    }

    // Getters & Setters
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

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
