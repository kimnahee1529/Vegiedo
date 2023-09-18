package com.devinsight.vegiedo.view.community.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.response.PostListData;
import com.devinsight.vegiedo.service.api.PostApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;

import java.io.IOException;
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
    /* 게시글 커서 값 */
    private MutableLiveData<Integer> maxCursorLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLastItemLiveData = new MutableLiveData<>();
    private String token;
    private int maxCursor;

    private boolean isLastItem;

    public void getToken(String token){
        this.token = token;
    }

    public void getMaxCursor(){
        this.maxCursor = maxCursorLiveData.getValue();
    }

    public void getLastItem(boolean isLastItem){
        this.isLastItem = isLastItem;
    }

    public void loadPopularPostList( int cursor ){
        Log.d("인기 포스트 요청을 위한 토큰"," 포스트 요청 토큰" + token);

        postApiService.getPopularPostList(8,cursor, "Bearer " + token).enqueue(new Callback<List<PostListData>>() {
            @Override
            public void onResponse(Call<List<PostListData>> call, Response<List<PostListData>> response) {
                if(response.isSuccessful() && response.body() != null ){
                    Log.d("인기 포스트 요청 성공","인기 포스트 요청 성공" + response.isSuccessful());
                    List<PostListData> data = response.body();
                    popularPostLiveData.setValue(data);
                    if(data.size() == 0 ) {
                        maxCursorLiveData.setValue(1);
                    } else {
                        maxCursorLiveData.setValue(data.get(data.size() - 1).getTotalPage());
                    }
                    Log.d( "this is max cursor","max cursor" + data.get(data.size() - 1).getTotalPage());
                    Log.d("성공","this is post cursor : " + cursor);
                } else {
                    // API 응답이 오류 상태일 때
                    Log.e("인기 포스트 요청 실패 1", "Error Code: " + response.code() + ", Message: " + response.message());
                    try {
                        Log.e("인기 포스트 요청 실패 1", "Error Body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<PostListData>> call, Throwable t) {
                Log.e("인기 포스트 요청 실패 2", "실패 2" + t.getMessage());
            }
        });
    }


    public void loadGeneralPostList( int cursor ){

        Log.d("일반 포스트 요청을 위한 토큰"," 포스트 요청 토큰" + token);
        postApiService.getGeneralPostList(8,cursor,"Bearer " + token).enqueue(new Callback<List<PostListData>>() {
            @Override
            public void onResponse(Call<List<PostListData>> call, Response<List<PostListData>> response) {
                if(response.isSuccessful() && response.body() != null ){
                    Log.d("일반 포스트 요청 성공"," 일반 포스트 요청 성공" + response.isSuccessful());
                    List<PostListData> data = response.body();
                    generalPostLiveData.setValue(data);
                    if(!data.isEmpty()){
                        maxCursorLiveData.setValue(data.get(data.size() - 1).getTotalPage());
                        Log.d( "this is max cursor","max cursor" + data.get(data.size() - 1).getTotalPage());
                    }
                    Log.d("성공","this is post cursor : " + cursor);

//                    Log.d("일반 포스트 리스트 성공","this is post : " + data.get(4*cursor).toString());
//                    Log.d("일반 포스트 리스트 성공","this is post id: " + data.get(4*cursor).getPostId());
//                    Log.d("일반 포스트 리스트 성공","this is post userNAme: " + data.get(4*cursor).getUserName());
//                    Log.d("일반 포스트 리스트 성공","this is post time : " + data.get(4*cursor).getCreatedAt());
//                    Log.d("일반 포스트 리스트 성공","this is post like : " + data.get(4*cursor).getLike());
//                    Log.d("일반 포스트 리스트 성공","this is post imageUrl : " + data.get(4*cursor).getImageUrl());
//                    Log.d("일반 포스트 리스트 성공","this is post comment : " + data.get(4*cursor).getCommentCount());
//                    Log.d("일반 포스트 리스트 성공","this is post totalPage : " + data.get(4*cursor).getTotalPage());

                }else {
                    // API 응답이 오류 상태일 때
                    Log.e("일반 포스트 요청 실패1 ", "Error Code: " + response.code() + ", Message: " + response.message());
                    try {
                        Log.e("일반 포스트 요청 실패1", "Error Body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<PostListData>> call, Throwable t) {
                Log.e("일반 포스트 요청 실패2", "일반 포스트 요청 실패2" + t.getMessage());
            }
        });

    }

    public LiveData<List<PostListData>> getGeneralPostList(){
        return generalPostLiveData;
    }

    public LiveData<List<PostListData>> getPopularPostList(){
        return popularPostLiveData;
    }

    public LiveData<Integer> getMaxCursorLiveData(){
        return maxCursorLiveData;
    }

    public LiveData<Boolean> getIsLastItem(){
        return isLastItemLiveData;
    }


}
