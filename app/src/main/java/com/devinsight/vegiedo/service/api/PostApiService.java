package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.request.PostRegisterRequestDTO;
import com.devinsight.vegiedo.data.request.PostReportRequestDTO;
import com.devinsight.vegiedo.data.response.PostInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.PostListInquiryResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostApiService {

    //게시글 리스트 조회
    @GET("/posts")
    Call<PostListInquiryResponseDTO> getPostList(
            @Query("count") int count,
            @Query("cursor") int cursor,
            @Query("type") int type
    );

    //게시글 등록
    @POST("/posts")
    Call<Void> addPost(
            @Body PostRegisterRequestDTO postRegisterRequestDTO
    );

    //게시글 조회
    @GET("/posts/{postId}")
    Call<PostInquiryResponseDTO> getPost(
            @Path("postId") Long postId
    );

    //게시글 수정
    @PATCH("/posts/{postId}")
    Call<Void> updatePost(
            @Path("postId") Long postId,
            @Body PostRegisterRequestDTO updatePost
    );

    //게시글 삭제
    @DELETE("/posts/{postId}")
    Call<Void> deletePost(
            @Path("postId") Long postId
    );

    //게시글 추천
    @PATCH("/posts/{postId}/recommendations")
    Call<Void> recommendPost(
            @Path("postId") Long postId
    );

    //게시글 신고
    @POST("/posts/{postId}/reports")
    Call<Void> reportPost(
            @Path("postId") Long postId,
            @Body PostReportRequestDTO postReportRequestDTO
    );


}
