package com.devinsight.vegiedo.service.api;

import com.devinsight.vegiedo.data.request.UserRegisterRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApiService {

    //로그인 한 회원정보 얻기
    @GET("/users/me")
    Call<Void> getUserInfo(
            @Header("Authorization") String token
    );

    //회원가입
    @POST("/users/{provider}")
    Call<Void> registerUser(
            @Path("provider") String provider
    );

    //회원정보 수정
    @PATCH("/users/{userId}")
    Call<Void> updateUser(
            @Header("Authorization") String token,
            @Path("userId") String userId
    );

    //로그아웃
    @DELETE("/session")
    Call<Void> logoutUser(
            @Header("Authorization") String token
    );

    //로그인
    @POST("/session")
    Call<Void> loginUser(
            @Header("Authorization") String token
    );

    //회원가입 시 닉네임 및 태그 받기
    @POST("/users/initialSetup")
    Call<Void> sendUserInfo(
            @Header("Authorization") String token,
            @Body UserRegisterRequestDTO userInfoData

    );

    //파이어베이스에서 인증받은 idToken 서버로 전송
    @POST("/users/googleLogin")
    Call<Void> sendUserIdToken(
            @Header("Authorization") String token
    );


}
