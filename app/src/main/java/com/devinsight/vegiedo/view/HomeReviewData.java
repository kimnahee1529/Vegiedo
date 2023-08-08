package com.devinsight.vegiedo.view;

public class HomeReviewData {

    private int storeImage;
    private String storeName;
    private int storeTag1;
    private int storeTag2;
    private int storeTag3;

    public HomeReviewData(int storeImage, String storeName, int storeTag1, int storeTag2, int storeTag3) {
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeTag1 = storeTag1;
        this.storeTag2 = storeTag2;
        this.storeTag3 = storeTag3;
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

    public int getStoreTag1() {
        return storeTag1;
    }

    public void setStoreTag1(int storeTag1) {
        this.storeTag1 = storeTag1;
    }

    public int getStoreTag2() {
        return storeTag2;
    }

    public void setStoreTag2(int storeTag2) {
        this.storeTag2 = storeTag2;
    }

    public int getStoreTag3() {
        return storeTag3;
    }

    public void setStoreTag3(int storeTag3) {
        this.storeTag3 = storeTag3;
    }
}
