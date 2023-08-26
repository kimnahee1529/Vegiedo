package com.devinsight.vegiedo.data.response;

import java.util.List;

// ReviewInquiryResponseDTO (리뷰 조회 응답 데이터)
public class ReviewInquiryResponseDTO {
    private Long storeId;
    private List<ReviewDetailDTO> reviews;

    public static class ReviewDetailDTO {
        private Long reviewId;
        private String userName;
        private Integer stars;
        private String content;
        private List<String> images; // 사진 URL 리스트

        // ... getters, setters
    }

    // ... getters, setters
}
