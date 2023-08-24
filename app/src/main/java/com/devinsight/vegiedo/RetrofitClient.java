package com.devinsight.vegiedo;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String GOOGLE_BASE_URL = "https://www.googleapis.com";

    private static Retrofit retrofit;

    public static Retrofit getRetrofit(String baseurl) {
        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(baseurl);
            builder.client(new OkHttpClient());
            builder.addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));

            retrofit = builder.build();
        }
        return retrofit;
    }

    public static GoogleApiService getGoogleApiService(){
        return getRetrofit(GOOGLE_BASE_URL).create(GoogleApiService.class);
    }
}
