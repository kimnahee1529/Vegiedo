package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.response.MapInquiryResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapApiService {

    //지도 가게 조회
    @GET("/maps/stores")
    Call<MapInquiryResponseDTO> getStoresOnMap(
            @Query("tags") List<String> tags,
            @Query("x") Double x,
            @Query("y") Double y,
            @Query("distance") String distance,
            @Query("keyword") String keyword
    );
}
