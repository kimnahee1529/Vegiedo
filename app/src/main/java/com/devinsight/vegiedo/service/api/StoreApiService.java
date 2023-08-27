package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.request.StoreModifyRequestDTO;
import com.devinsight.vegiedo.data.request.StoreRegisterRequestDTO;
import com.devinsight.vegiedo.data.request.StoreReportRequestDTO;
import com.devinsight.vegiedo.data.response.StoreInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.StoreListInquiryResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StoreApiService {

    //가게 리스트 조회
    @GET("/stores")
    Call<StoreListInquiryResponseDTO> getStoreList(
            @Query("tags") String tags,
            @Query("x") float x,
            @Query("y") float y,
            @Query("distance") int distance,
            @Query("keyword") String keyword,
            @Query("count") int count,
            @Query("cursor") int cursor,
            @Query("isStampBook") boolean isStampBook
    );

    //가게 등록
    @POST("/stores")
    Call<Void> createStore(
            @Body StoreRegisterRequestDTO createStore
    );


    //가게 조회
    @GET("/stores/{storeId}")
    Call<StoreInquiryResponseDTO> readStore(
            @Path("storeId") Long storeId
    );

    //가게 수정
    @PATCH("/stores/{storeId}")
    Call<Void> updateStore(
            @Path("storeId") Long storeId,
            @Body StoreModifyRequestDTO modifyStore
    );

    //가게 삭제
    @DELETE("/stores/{storeId}")
    Call<Void> deleteStore(
            @Path("storeId") Long storeId
    );

    //가게 신고
    @POST("/stores/{storeId}/reports")
    Call<Void> reportStore(
            @Path("storeId") Long storeId,
            @Body StoreReportRequestDTO reportData
    );
}
