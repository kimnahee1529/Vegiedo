package com.devinsight.vegiedo.data.request;

public class PostDeleteRequestDTO {
    private Long postId;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public PostDeleteRequestDTO(Long postId) {
        this.postId = postId;
    }
}
