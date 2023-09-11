package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostInquiryResponseDTO {
    @Expose
    @SerializedName("postId")private Long postId;
    @Expose
    @SerializedName("postTitle")private String postTitle;
    @Expose
    @SerializedName("postContent")private String content;
    @Expose
    @SerializedName("userName")private String userName;
    @Expose
    @SerializedName("userImageUrl")private String userImageUrl;
    @Expose
    @SerializedName("likeReceiveCnt")private String likeReceiveCnt;
    @Expose
    @SerializedName("images")private List<String> images;
    @Expose
    @SerializedName("commentList")private List<CommentListData> commentList;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getLikeReceiveCnt() {
        return likeReceiveCnt;
    }

    public void setLikeReceiveCnt(String likeReceiveCnt) {
        this.likeReceiveCnt = likeReceiveCnt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<CommentListData> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListData> commentList) {
        this.commentList = commentList;
    }

    public PostInquiryResponseDTO(Long postId, String postTitle, String content, String userName, String userImageUrl, String likeReceiveCnt, List<String> images, List<CommentListData> commentList) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.content = content;
        this.userName = userName;
        this.userImageUrl = userImageUrl;
        this.likeReceiveCnt = likeReceiveCnt;
        this.images = images;
        this.commentList = commentList;
    }
}
