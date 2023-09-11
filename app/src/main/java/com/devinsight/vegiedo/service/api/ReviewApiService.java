package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.request.ReviewModifyrRequestDTO;
import com.devinsight.vegiedo.data.request.ReviewRegisterRequestDTO;
import com.devinsight.vegiedo.data.request.ReviewReportRequestDTO;
import com.devinsight.vegiedo.data.response.ReviewListInquiryResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewApiService {

    // 리뷰 조회
    @GET("stores/{storeId}/reviews")
    Call<ReviewListInquiryResponseDTO> getReviews(
            @Path("storeId") Long storeId,
            @Query("count") int count,
            @Query("cursor") int cursor,
            @Query("blogReviewBoolean") boolean blogReviewBoolean
    );

    // 리뷰 등록
    @POST("stores/{storeId}/reviews")
    Call<Void> postReview(
            @Path("storeId") Long storeId,
            @Body ReviewRegisterRequestDTO reviewRegisterRequestDTO
    );

    // 리뷰 삭제
    @DELETE("stores/{storeId}/reviews/{reviewId}")
    Call<Void> deleteReview(
            @Path("storeId") Long storeId,
            @Path("reviewId") Long reviewId
    );

    // 리뷰 수정
    @PUT("stores/{storeId}/reviews/{reviewId}")
    Call<Void> modifyReview(
            @Path("storeId") Long storeId,
            @Path("reviewId") Long reviewId,
            @Body ReviewModifyrRequestDTO reviewModifyrRequestDTO
    );

    // 리뷰 신고
    @POST("stores/{storeId}/reviews/{reviewId}/reports")
    Call<Void> reportReview(
            @Path("storeId") Long storeId,
            @Path("reviewId") Long reviewId,
            @Body ReviewReportRequestDTO reviewReportRequestDTO
    );
}
