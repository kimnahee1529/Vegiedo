package com.devinsight.vegiedo.data.request;

public class CommentRegisterRequestDTO {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommentRegisterRequestDTO(String content) {
        this.content = content;
    }
}
