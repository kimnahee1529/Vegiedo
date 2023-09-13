package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.request.PostRegisterRequestDTO;
import com.devinsight.vegiedo.data.request.PostReportRequestDTO;
import com.devinsight.vegiedo.data.response.PostInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.PostListData;
import com.devinsight.vegiedo.data.response.PostListInquiryResponseDTO;
//import com.devinsight.vegiedo.data.response.
import com.devinsight.vegiedo.data.response.PostRegisterResponseDTO;

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
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostApiService {

    //게시글 리스트 조회
    @GET("/posts/list")
    Call<List<PostListData>> getGeneralPostList(
            @Query("pageSize") int pageSize,
            @Query("pageNumber") int pageNumber,
            @Header("Authorization") String token
    );

    @GET("/posts/popularList")
    Call<List<PostListData>> getPopularPostList(
            @Query("pageSize") int count,
            @Query("pageNumber") int cursor,
            @Header("Authorization") String token
    );

    //게시글 등록
    @Multipart
    @POST("/posts")
    Call<Void> addPost(
            @Header("Authorization") String token,
            @Part List<MultipartBody.Part> images,
            @Part("postTitle") MultipartBody.Part postTitle,
            @Part("content") MultipartBody.Part content
    );
    @Multipart
    @POST("/posts")
    Call<PostRegisterResponseDTO> addPost2(
            @Header("Authorization") String token,
            @Part List<MultipartBody.Part> images,
            @Part("postTitle") RequestBody postTitle,
            @Part("content") RequestBody postContent
    );

//    @Multipart
//    @POST("/posts")
//    Call<PostRegisterResponseDTO> addPost2(
//            @Header("Authorization") String token,
//            @Part List<MultipartBody.Part> images,
//            @Part MultipartBody.Part postTitle,
//            @Part MultipartBody.Part content
//    );

    //게시글 조회
    @GET("/posts/{postId}")
    Call<PostInquiryResponseDTO> getPost(
            @Header("Authorization") String token,
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
