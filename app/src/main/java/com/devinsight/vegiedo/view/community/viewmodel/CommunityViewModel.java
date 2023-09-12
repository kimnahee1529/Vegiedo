package com.devinsight.vegiedo.view.community.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.response.PostListData;
import com.devinsight.vegiedo.data.response.PostListInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.response;
import com.devinsight.vegiedo.service.api.PostApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityViewModel extends ViewModel {
    /* post api 서비스 초기화 */
    PostApiService postApiService = RetrofitClient.getPostApiService();
    /* 일반 게시글 라이브 데이터 */
    private MutableLiveData<List<PostListData>> generalPostLiveData = new MutableLiveData<>();
    /* 인기 게시글 라이브 데이터 */
    private MutableLiveData<List<PostListData>> popularPostLiveData = new MutableLiveData<>();
    private String token;

    public void getToken(String token){
        this.token = token;
    }

    public void loadPopularPostList(){
        Log.d("인기 포스트 요청을 위한 토큰"," 포스트 요청 토큰" + token);

        postApiService.getPopularPostList(5,1, "Bearer " + token).enqueue(new Callback<List<PostListData>>() {
            @Override
            public void onResponse(Call<List<PostListData>> call, Response<List<PostListData>> response) {
                if(response.isSuccessful() && response.body() != null ){
                    Log.d("인기 포스트 요청 성공","인기 포스트 요청 성공" + response.isSuccessful());
                    List<PostListData> data = response.body();
                    popularPostLiveData.setValue(data);
//                    Log.d("성공","this is post : " + data.get(0).getPostId());
                }else {
                    // API 응답이 오류 상태일 때
                    Log.e("APIError", "Error Code: " + response.code() + ", Message: " + response.message());
                    try {
                        Log.e("APIErrorBody", "Error Body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<PostListData>> call, Throwable t) {
                Log.e("실패", "실패" + t.getMessage());
            }
        });
    }


    public void loadGeneralPostList(){

        Log.d("일반 포스트 요청을 위한 토큰"," 포스트 요청 토큰" + token);
        postApiService.getGeneralPostList(5,1,token).enqueue(new Callback<List<PostListData>>() {
            @Override
            public void onResponse(Call<List<PostListData>> call, Response<List<PostListData>> response) {
                if(response.isSuccessful() && response.body() != null ){
                    Log.d("성공","성공" + response.isSuccessful());
                    List<PostListData> data = response.body();
                    generalPostLiveData.setValue(data);
//                    Log.d("성공","this is post : " + data.get(0).getPostId());
                }else {
                    // API 응답이 오류 상태일 때
                    Log.e("APIError", "Error Code: " + response.code() + ", Message: " + response.message());
                    try {
                        Log.e("APIErrorBody", "Error Body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<PostListData>> call, Throwable t) {
                Log.e("실패", "실패" + t.getMessage());
            }
        });

    }

    public LiveData<List<PostListData>> getGeneralPostList(){
        return generalPostLiveData;
    }

    public LiveData<List<PostListData>> getPopularPostList(){
        return popularPostLiveData;
    }


}
