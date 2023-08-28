package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreListData {

    @Expose
    @SerializedName("storeId")private Long storeId;
    @Expose
    @SerializedName("storeName")private String storeName;
    @Expose
    @SerializedName("address")private String address;
    @Expose
    @SerializedName("latitude")private float latitude;
    @Expose
    @SerializedName("longitude")private float longitude;
    @Expose
    @SerializedName("distance")private Integer distance;
    @Expose
    @SerializedName("stars")private Integer stars;
    @Expose
    @SerializedName("tags")private List<String> tags;
    @Expose
    @SerializedName("like")private boolean like;
    @Expose
    @SerializedName("stamp")private boolean stamp;
    @Expose
    @SerializedName("reviewCount")private Integer reviewCount;
    @Expose
    @SerializedName("images")private String images;

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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
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

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public StoreListData(Long storeId, String storeName, String address, float latitude, float longitude, Integer distance, Integer stars, List<String> tags, boolean like, boolean stamp, Integer reviewCount, String images) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.stars = stars;
        this.tags = tags;
        this.like = like;
        this.stamp = stamp;
        this.reviewCount = reviewCount;
        this.images = images;
    }
}
