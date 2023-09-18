package com.devinsight.vegiedo.view.mypage;

public class StampBookItem {
    private Long storeId;
    private String storeName;
    private int reviewCount;
    private String address;
    private int stars;
    private String images;

    public StampBookItem(Long storeId, String name, int stars, String address, int reviewCount, String images) {
        this.storeId = storeId;
        this.storeName = name;
        this.stars = stars;
        this.address = address;
        this.reviewCount = reviewCount;
        this.images = images;
    }
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
