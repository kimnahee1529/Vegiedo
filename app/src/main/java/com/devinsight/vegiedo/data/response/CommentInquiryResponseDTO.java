package com.devinsight.vegiedo.data.response;

import java.util.List;

public class CommentInquiryResponseDTO {

    private List<CommentListData> comments;

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
