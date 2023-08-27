package com.devinsight.vegiedo.data.request;

import com.devinsight.vegiedo.utill.VeganTag;

import java.util.List;

public class StoreModifyRequestDTO {

    private String storeName;
    private String address;
    private String detailAddress;
    private List<String> tags;
    private float latitude;
    private float longitude;

    public StoreModifyRequestDTO(String storeName, String address, String detailAddress, List<String> tags, float latitude, float longitude) {
        this.storeName = storeName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.tags = tags;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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
