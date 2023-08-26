package com.devinsight.vegiedo.data.request;

import com.devinsight.vegiedo.utill.VeganTag;

import java.util.List;

public class StoreModifyRequestDTO {

    private String storeName;
    private String address;
    private String detailAddress;
    private List<VeganTag> tags;
    private float x;
    private float y;

    public StoreModifyRequestDTO(String storeName, String address, String detailAddress, List<VeganTag> tags, float x, float y) {
        this.storeName = storeName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.tags = tags;
        this.x = x;
        this.y = y;
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
}
