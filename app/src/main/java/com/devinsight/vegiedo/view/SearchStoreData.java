package com.devinsight.vegiedo.view;

public class SearchStoreData {
    private int storeImage;
    private String storeName;
    private String storeAddress;

    public SearchStoreData(int storeImage, String storeName, String storeAddress) {
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }
    public int getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(int storeImage) {
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


}
