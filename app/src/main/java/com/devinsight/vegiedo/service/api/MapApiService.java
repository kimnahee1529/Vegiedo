package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.response.MapInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.MapStoreListData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapApiService {

    //지도 가게 조회
    @GET("maps/stores")
    Call<List<MapStoreListData>> getStoresOnMap(
            @Query("latitude") float latitude,
            @Query("longitude") float longitude,
            @Query("distance") Integer distance
    );
}
