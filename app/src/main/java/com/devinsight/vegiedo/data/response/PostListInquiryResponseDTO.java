package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

// 1. 게시글 리스트 조회 응답
public class PostListInquiryResponseDTO {
    @Expose
    @SerializedName("posts")private List<PostListData> posts;

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