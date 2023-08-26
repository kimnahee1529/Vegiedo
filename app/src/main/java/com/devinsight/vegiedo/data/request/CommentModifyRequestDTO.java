package com.devinsight.vegiedo.data.request;

public class CommentModifyRequestDTO {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommentModifyRequestDTO(String content) {
        this.content = content;
    }
}
