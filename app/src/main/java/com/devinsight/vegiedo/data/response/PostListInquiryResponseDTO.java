package com.devinsight.vegiedo.data.response;

import java.util.Date;
import java.util.List;

// 1. 게시글 리스트 조회 응답
public class PostListInquiryResponseDTO {
    private List<PostListData> posts;

    public PostListInquiryResponseDTO(List<PostListData> posts) {
        this.posts = posts;
    }

    public List<PostListData> getPosts() {
        return posts;
    }

    public void setPosts(List<PostListData> posts) {
        this.posts = posts;
    }
}