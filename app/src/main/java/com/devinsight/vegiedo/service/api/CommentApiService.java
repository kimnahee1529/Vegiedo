package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.request.CommentModifyRequestDTO;
import com.devinsight.vegiedo.data.request.CommentRegisterRequestDTO;
import com.devinsight.vegiedo.data.request.CommentReportRequestDTO;
import com.devinsight.vegiedo.data.response.CommentInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.CommentModifyResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentApiService {

    //댓글 조회
    @GET("/posts/{postId}/comments")
    Call<CommentInquiryResponseDTO> getCommentList(
            @Path("postId") Long postId
    );
    //댓글 등록
    @POST("/posts/{postId}/comments")
    Call<Void> addComment(
            @Path("postId") Long postId,
            @Body CommentRegisterRequestDTO registerComment
    );

    //댓글 수정
    @PATCH("/posts/{postId}/comments/{commentId}")
    Call<CommentModifyResponseDTO> updateComment(
            @Path("postId") Long postId,
            @Path("commentId") Long commentId,
            @Body CommentModifyRequestDTO updateComment
    );

    //댓글 삭제
    @DELETE("/posts/{postId}/comments/{commentId}")
    Call<Void > deleteComment(
            @Path("postId") Long postId,
            @Path("commentId") Long commentId
    );


    //댓글 신고
    @POST("/posts/{postId}/comments/{commentsId}/reports")
    Call<Void> reportComment(
            @Path("postId") Long postId,
            @Path("commentId") Long commentId,
            @Body CommentReportRequestDTO reportComment
    );

}
