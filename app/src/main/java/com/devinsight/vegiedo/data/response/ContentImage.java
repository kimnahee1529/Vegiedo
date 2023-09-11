package com.devinsight.vegiedo.data.response;

import com.kakao.sdk.template.model.Content;

import java.util.List;

public class ContentImage {
    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ContentImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ContentImage() {

    }
}
