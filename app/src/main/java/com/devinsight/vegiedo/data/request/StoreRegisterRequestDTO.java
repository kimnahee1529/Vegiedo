package com.devinsight.vegiedo.data.request;

import com.devinsight.vegiedo.utill.VeganTag;

import java.util.List;

public class StoreRegisterRequestDTO {
    private String storeName;
    private String address;
    private List<String> images;
    private List<VeganTag> tags;
    private float latitude;
    private float longitude;

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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<VeganTag> getTags() {
        return tags;
    }

    public void setTags(List<VeganTag> tags) {
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

    public StoreRegisterRequestDTO(String storeName, String address, List<String> images, List<VeganTag> tags, float latitude, float longitude) {
        this.storeName = storeName;
        this.address = address;
        this.images = images;
        this.tags = tags;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public StoreRegisterRequestDTO(String storeName, String address, List<VeganTag> tags) {
        this.storeName = storeName;
        this.address = address;
        this.tags = tags;
    }
}
