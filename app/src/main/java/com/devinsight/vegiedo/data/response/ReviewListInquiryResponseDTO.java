package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// ReviewInquiryResponseDTO (리뷰 조회 응답 데이터)
public class ReviewListInquiryResponseDTO {
    @Expose
    @SerializedName("storeId") private List<ReviewListData> reviews;

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
