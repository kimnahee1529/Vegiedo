package com.devinsight.vegiedo.data.response;

import com.devinsight.vegiedo.utill.VeganTag;

import java.util.List;

public class StoreInquiryResponseDTO {

    private Long storeId;
    private String storeName;
    private String address;
    private Integer stars;
    private List<String> tags;
    private boolean like;
    private boolean stamp;
    private List<String> images;
    private float latitude;
    private float longitude;

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

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean isStamp() {
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

    public StoreInquiryResponseDTO(Long storeId, String storeName, String address, Integer stars, List<String> tags, boolean like, boolean stamp, List<String> images, float latitude, float longitude) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
        this.stars = stars;
        this.tags = tags;
        this.like = like;
        this.stamp = stamp;
        this.images = images;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
