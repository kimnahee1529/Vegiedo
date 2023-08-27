package com.devinsight.vegiedo.data.response;

public class CommentModifyResponseDTO {

    private Long commentId;
    private String content;
    private String userName;
    private String createdAt;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public CommentModifyResponseDTO(Long commentId, String content, String userName, String createdAt) {
        this.commentId = commentId;
        this.content = content;
        this.userName = userName;
        this.createdAt = createdAt;
    }
}
