package com.devinsight.vegiedo.view.search;

public class SummaryData {

    private String storeImage;
    private String storeName;
    private String storeAddress;

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

    public SummaryData(String storeImage, String storeName, String storeAddress) {
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }

    public SummaryData () {

    }
}
