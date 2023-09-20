package com.devinsight.vegiedo.view.community;

public class ImageViewData {
    String url;
    int index;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ImageViewData(String url, int index) {
        this.url = url;
        this.index = index;
    }

    public  ImageViewData(){

    }
}
