package com.devinsight.vegiedo.data.request;

public class PostRecommendRequestDTO {
    private Long postId;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public PostRecommendRequestDTO(Long postId) {
        this.postId = postId;
    }
}
