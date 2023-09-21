package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.request.CommentModifyRequestDTO;
import com.devinsight.vegiedo.data.request.CommentRegisterRequestDTO;
import com.devinsight.vegiedo.data.request.CommentReportRequestDTO;
import com.devinsight.vegiedo.data.request.ReportRequestDTO;
import com.devinsight.vegiedo.data.response.CommentInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.CommentListData;
import com.devinsight.vegiedo.data.response.CommentModifyResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentApiService {

    //댓글 등록
    @POST("/posts/{postId}/comments")
    Call<List<CommentListData>> addComment(
            @Path("postId") Long postId,
            @Header("Authorization") String token,
            @Body CommentRegisterRequestDTO commentRegisterRequestDTO
    );

    //댓글 삭제
    @DELETE("/posts/{postId}/comments/{commentId}")
    Call<Void> deleteComment(
            @Header("Authorization") String token,
            @Path("postId") Long postId,
            @Path("commentId") Long commentId
    );


    //댓글 신고
    @POST("/posts/{postId}/comments/{commentId}/reports")
    Call<Void> reportComment(
            @Header("Authorization") String token,
            @Path("postId") Long postId,
            @Path("commentId") Long commentId,
            @Body ReportRequestDTO requestDTO
    );

}
