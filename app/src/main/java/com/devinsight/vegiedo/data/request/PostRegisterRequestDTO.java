package com.devinsight.vegiedo.data.request;

import java.util.List;

// 2. 게시글 등록 요청
public class PostRegisterRequestDTO {

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


    public PostRegisterRequestDTO(String postTitle, String content) {
        this.postTitle = postTitle;
        this.content = content;
        this.images = null; // 또는 new ArrayList<>() 빈 리스트로 초기화
    }

    public PostRegisterRequestDTO(String postTitle, String content, List<String> images) {
        this.postTitle = postTitle;
        this.content = content;
        this.images = images;
    }

    public PostRegisterRequestDTO(String content, List<String> images) {
        this.content = content;
        this.images = images;
    }

    public PostRegisterRequestDTO(String content) {
        this.postTitle = null;
        this.content = content;
        this.images = null; // 또는 new ArrayList<>() 빈 리스트로 초기화
    }
}
