package com.devinsight.vegiedo.data.ui.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeBannerData {
    @Expose
    @SerializedName("bannerImageUrl")private String images;
    @Expose
    @SerializedName("bannerId")private Long bannerId;
    @Expose
    @SerializedName("seqeunce")private  byte sequence;
    @Expose
    @SerializedName("storeId")private Long storeId;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }

    public byte getSequence() {
        return sequence;
    }

    public void setSequence(byte sequence) {
        this.sequence = sequence;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public HomeBannerData(String images, Long bannerId, byte sequence, Long storeId) {
        this.images = images;
        this.bannerId = bannerId;
        this.sequence = sequence;
        this.storeId = storeId;
    }
}
