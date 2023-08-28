package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentInquiryResponseDTO {

    @Expose
    @SerializedName("comments")private List<CommentListData> comments;

    public List<CommentListData> getComments() {
        return comments;
    }

    public void setComments(List<CommentListData> comments) {
        this.comments = comments;
    }

    public CommentInquiryResponseDTO(List<CommentListData> comments) {
        this.comments = comments;
    }
}
