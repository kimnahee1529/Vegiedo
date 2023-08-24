package com.devinsight.vegiedo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GoogleApiService {

    @POST("oauth2/v4/token")
    Call<GoogleLoginResponseDTO> getGoogleAccessToken(@Body GoogleLoginRequestDTO googleLoginRequestDTO);


}