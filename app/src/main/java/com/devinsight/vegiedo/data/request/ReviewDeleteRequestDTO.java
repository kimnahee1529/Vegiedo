package com.devinsight.vegiedo.data.request;

// TODO : request body가 없어서 Request인지 확신이 없음
public class ReviewDeleteRequestDTO {
    private Long storeId;
    private Long reviewId;

    // 기본 생성자
    public ReviewDeleteRequestDTO() {}

    // 모든 필드를 매개변수로 갖는 생성자
    public ReviewDeleteRequestDTO(Long storeId, Long reviewId) {
        this.storeId = storeId;
        this.reviewId = reviewId;
    }

    // Getter, Setter

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    // toString(), hashCode(), equals() 메서드 추가 가능
}
