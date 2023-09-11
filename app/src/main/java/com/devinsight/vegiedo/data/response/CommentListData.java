package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentListData {

    @Expose
    @SerializedName("commentId") private Long commentId;
    @Expose
    @SerializedName("commentContent")private String commentContent;
    @Expose
    @SerializedName("userName")private String userName;
    @Expose
    @SerializedName("createdAt")private String createdAt;
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
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

    public CommentListData(Long commentId, String content, String userName, String createdAt) {
        this.commentId = commentId;
        this.commentContent = content;
        this.userName = userName;
        this.createdAt = createdAt;
    }
}
