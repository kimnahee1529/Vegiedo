package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.response.HomeBannerResponseDTO;
import com.devinsight.vegiedo.data.ui.home.HomeBannerData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface AdminApiService {

    @GET("/banners")
    Call<List<HomeBannerData>> getBanner (
            @Header("Authorization") String token
    );

    @GET("/banners")
    Call<HomeBannerResponseDTO> getBanner2 (
            @Header("Authorization") String token
    );
}
