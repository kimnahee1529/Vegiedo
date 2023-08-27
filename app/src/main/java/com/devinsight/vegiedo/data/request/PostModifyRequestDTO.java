package com.devinsight.vegiedo.data.request;

import java.util.List;

public class PostModifyRequestDTO {
    private String postTitle;
    private String content;
    private List<String> images;

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public PostModifyRequestDTO(String postTitle, String content, List<String> images) {
        this.postTitle = postTitle;
        this.content = content;
        this.images = images;
    }
}