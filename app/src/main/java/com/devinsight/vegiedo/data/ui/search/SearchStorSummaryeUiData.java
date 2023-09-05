package com.devinsight.vegiedo.data.ui.search;

public class SearchStorSummaryeUiData {
    private String storeImage;
    private String storeName;
    private String storeAddress;

    public SearchStorSummaryeUiData() {}
    public SearchStorSummaryeUiData(String storeImage, String storeName, String storeAddress) {
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }
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

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public void setData(String storeImage, String storeName, String storeAddress) {
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }



}
