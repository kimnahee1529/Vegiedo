package com.devinsight.vegiedo.data.ui.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeReviewUiData {

    @Expose
    @SerializedName("images")private String storeImage;
    @Expose
    @SerializedName("storeName")private String storeName;
    @Expose
    @SerializedName("tags")private List<String> tags;
    @Expose
    @SerializedName("storeId")private Long storeId;

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public HomeReviewUiData(String storeImage, String storeName, List<String> tags, Long storeId) {
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.tags = tags;
        this.storeId = storeId;
    }
}
