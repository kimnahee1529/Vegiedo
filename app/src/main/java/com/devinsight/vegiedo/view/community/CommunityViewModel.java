package com.devinsight.vegiedo.view.community;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.response.PostListData;
import com.devinsight.vegiedo.data.response.PostListInquiryResponseDTO;
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
        List<PostListData> postList = new ArrayList<>();
        for(long i = 0; i < 9; i++) {
            String title = String.format("인기게시글%d", i + 1);  // 제목1, 제목2, ...
            String name = String.format("인기이름%d", i + 1);   // 이름1, 이름2, ...
            String date = String.format("인기게시글날짜%d", i + 1);   // 날짜1, 날짜2, ...
            String image = String.format("인기게시글이미지%d", i + 1); // 이미지1, 이미지2, ...

            postList.add(new PostListData(i, title, name, date, null, 3, 4));
        }
        popularPostLiveData.setValue(postList);

    }


    public void loadGeneralPostList(){

//        List<PostListData> postList = new ArrayList<>();
//        for(long i = 0; i < 9; i++) {
//            String title = String.format("제목%d", i + 1);  // 제목1, 제목2, ...
//            String name = String.format("이름%d", i + 1);   // 이름1, 이름2, ...
//            String date = String.format("날짜%d", i + 1);   // 날짜1, 날짜2, ...
//            String image = String.format("이미지%d", i + 1); // 이미지1, 이미지2, ...
//
//            postList.add(new PostListData(i, title, name, date, null, 3, 4));
//        }
//        generalPostLiveData.setValue(postList);

        Log.d("포스트 요청을 위한 토큰"," 포스트 요청 토큰" + token);
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
