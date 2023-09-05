package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapStoreListData {
    @Expose
    @SerializedName("storeId") private Long storeId;
    @Expose
    @SerializedName("storeName")private String storeName;
    @Expose
    @SerializedName("address")private String address;
    @Expose
    @SerializedName("latitude")private float latitude;      // x 좌표
    @Expose
    @SerializedName("longitude")private float longitude;    // y 좌표
    @Expose
    @SerializedName("distance") private Integer distance;  // 이 데이터 타입은 'm' 단위로 제공되는 거리를 나타내기 위해 사용됩니다.
    @Expose
    @SerializedName("stars") private Integer stars;  // 별점은 소수점이 포함될 수 있으므로 Double로 표현합니다.
    @Expose
    @SerializedName("tags")private List<String> tags;
    @Expose
    @SerializedName("like") private Boolean like;
    @Expose
    @SerializedName("stamp") private Boolean stamp;

    @Expose
    @SerializedName("reviewCount") private Integer reviewCount;


    @Expose
    @SerializedName("images") private String images;


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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
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


    public Integer getReviewCount() {
        return reviewCount;
    }


    public String getImages() {
        return images;
    }

    public MapStoreListData(Long storeId, String storeName, String address, float latitude, float longitude, Integer distance, List<String> tags, Integer stars, Boolean like, Boolean stamp, Integer reviewCount, String images) {
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
