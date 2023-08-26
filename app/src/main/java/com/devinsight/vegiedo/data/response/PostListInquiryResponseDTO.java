package com.devinsight.vegiedo.data.response;

import java.util.Date;
import java.util.List;

// 1. 게시글 리스트 조회 응답
public class PostListInquiryResponseDTO {
    private List<Post> posts;

    public static class Post {
        private Long postId;
        private String postTitle;
        private String userName;
        private Date createdAt;
        private Boolean like;
        // Getters, Setters, Constructors...
    }

    // Getters, Setters, Constructors...
}