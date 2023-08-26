package com.devinsight.vegiedo.data.request;

import com.devinsight.vegiedo.utill.VeganTag;

import java.util.List;

public class StoreRegisterRequestDTO {
    private String storeName;
    private String address;
    private List<String> images;
    private List<VeganTag> tags;
    private float x;
    private float y;

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

    public StoreRegisterRequestDTO(String storeName, String address, List<String> images, List<VeganTag> tags, float x, float y) {
        this.storeName = storeName;
        this.address = address;
        this.images = images;
        this.tags = tags;
        this.x = x;
        this.y = y;
    }
}
