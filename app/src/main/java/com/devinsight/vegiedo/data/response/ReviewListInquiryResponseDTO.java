package com.devinsight.vegiedo.data.response;

import java.util.List;

// ReviewInquiryResponseDTO (리뷰 조회 응답 데이터)
public class ReviewListInquiryResponseDTO {
    private List<ReviewListData> reviews;

    public List<ReviewListData> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewListData> reviews) {
        this.reviews = reviews;
    }

    public ReviewListInquiryResponseDTO(List<ReviewListData> reviews) {
        this.reviews = reviews;
    }
}
