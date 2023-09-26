package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.request.ReviewModifyrRequestDTO;
import com.devinsight.vegiedo.data.request.ReviewRegisterRequestDTO;
import com.devinsight.vegiedo.data.request.ReviewReportRequestDTO;
import com.devinsight.vegiedo.data.response.HomeReviewResponseDTO;
import com.devinsight.vegiedo.data.response.PostInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.ReviewListInquiryResponseDTO;
import com.devinsight.vegiedo.data.ui.home.HomeReviewUiData;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewApiService {

    // 리뷰 조회
    @GET("stores/{storeId}/reviews")
    Call<ReviewListInquiryResponseDTO> getReviews(
            @Header("Authorization") String token,
            @Path("storeId") Long storeId,
            @Query("count") int count,
            @Query("cursor") int cursor,
            @Query("blogReviewBoolean") boolean blogReviewBoolean
    );

    // 리뷰 등록
    @Multipart
    @POST("stores/{storeId}/reviews")
    Call<Void> postReview(
            @Header("Authorization") String token,
            @Path("storeId") Long storeId,
            @Part("content") RequestBody content,
            @Part("stars") RequestBody stars,
            @Part List<MultipartBody.Part> images
    );

    // 리뷰 삭제
    @DELETE("stores/{storeId}/reviews/{reviewId}")
    Call<Void> deleteReview(
            @Header("Authorization") String token,
            @Path("storeId") Long storeId,
            @Path("reviewId") Long reviewId
    );

//    // 리뷰 수정
//    @PUT("stores/{storeId}/reviews/{reviewId}")
//    Call<Void> modifyReview(
//            @Header("Authorization") String token,
//            @Path("storeId") Long storeId,
//            @Path("reviewId") Long reviewId,
//            @Body ReviewModifyrRequestDTO reviewModifyrRequestDTO
//    );

    // 리뷰 수정
    @Multipart
    @PUT("stores/{storeId}/reviews/{reviewId}")
    Call<Void> modifyReview(
            @Header("Authorization") String token,
            @Path("storeId") Long storeId,
            @Path("reviewId") Long reviewId,
            @Part("content") RequestBody content,
            @Part("stars") RequestBody stars,
            @Part List<MultipartBody.Part> images
    );

//    Call<PostInquiryResponseDTO> updatePost2(
//            @Header("Authorization") String token,
//            @Path("postId") Long postId,
//            @Part("postTitle") RequestBody postTitle,
//            @Part("postContent") RequestBody postContent,
//            @Part List<MultipartBody.Part> images,
//            @Part("imageUrlsJson") RequestBody imageUrls
//
//    );

    // 리뷰 신고
    @POST("stores/{storeId}/reviews/{reviewId}/reports")
    Call<Void> reportReview(
            @Header("Authorization") String token,
            @Path("storeId") Long storeId,
            @Path("reviewId") Long reviewId,
            @Body ReviewReportRequestDTO reviewReportRequestDTO
    );

    @GET("/reviews/random")
    Call<HomeReviewResponseDTO> getHomeReview(
            @Header("Authorization") String token,
            @Query("count") Integer count,
            @Query("cursor") Integer cursor
    );
}
