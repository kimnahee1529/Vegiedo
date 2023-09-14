package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.request.StoreModifyRequestDTO;
import com.devinsight.vegiedo.data.request.StoreRegisterRequestDTO;
import com.devinsight.vegiedo.data.request.StoreReportRequestDTO;
import com.devinsight.vegiedo.data.response.StoreInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.StoreListData;
import com.devinsight.vegiedo.data.response.StoreListData;
import com.devinsight.vegiedo.data.response.StoreListInquiryResponseDTO;
import com.devinsight.vegiedo.view.store.StoreDetailData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StoreApiService {

    //가게 리스트 조회
    @GET("/test/stores")
    Call<List<StoreListData>> getStoreLists(
            @Query("tags") List<String> tags,
            @Query("latitude") float latitude,
            @Query("longitude") float longitude,
            @Query("distance") int distance,
            @Query("keyword") String keyword,
            @Query("count") int count,
            @Query("cursor") int cursor,
            @Header("Authorization") String token
    );

    //가게 등록
    @POST("stores")
    Call<Void> createStore(
            @Body StoreRegisterRequestDTO createStore
    );


    //가게 조회
    @GET("stores/{storeId}")
    Call<StoreInquiryResponseDTO> readStore(
            @Path("storeId") Long storeId
    );

    //가게 수정
    @PATCH("stores/{storeId}")
    Call<Void> updateStore(
            @Path("storeId") Long storeId,
            @Body StoreModifyRequestDTO modifyStore
    );

    //가게 삭제
    @DELETE("stores/{storeId}")
    Call<Void> deleteStore(
            @Path("storeId") Long storeId
    );

    //가게 신고
    @POST("stores/{storeId}/reports")
    Call<Void> reportStore(
            @Path("storeId") Long storeId,
            @Body StoreReportRequestDTO reportData
    );

    //TODO 반환값이 없는지 보고 수정해야 함
    //가게 좋아요
    @POST("stores/{storeId}/likes")
    Call<Void> likeStore(
            @Path("storeId") Long storeId
    );

    //가게 좋아요 취소
    @DELETE("stores/{storeId}/likes")
    Call<Void> cancleLikeStore(
            @Path("storeId") Long storeId
    );

    //가게 스탬프
    @POST("stores/{storeId}/stamps")
    Call<Void> activeStamp(
            @Path("storeId") Long storeId
    );

    //가게 스탬프 취소
    @DELETE("stores/{storeId}/stamps")
    Call<Void> inactiveStamp(
            @Path("storeId") Long storeId
    );

    //TODO
    //마이페이지
    //검색어 추천
}
