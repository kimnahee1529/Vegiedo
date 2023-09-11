package com.devinsight.vegiedo.view.community.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.request.CommentRegisterRequestDTO;
import com.devinsight.vegiedo.data.response.PostInquiryResponseDTO;
import com.devinsight.vegiedo.service.api.CommentApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCommentViewModel extends ViewModel {

    private String token;
    private Long postId;
    CommentApiService commentApiService = RetrofitClient.getCommentApiService();

    public void getToken(String token){
        this.token = token;
    }

    public void getPostId(Long postId){
        this.postId = postId;
    }

    public void getCommentContent(String input){
        CommentRegisterRequestDTO commentRegisterRequestDTO = new CommentRegisterRequestDTO(input);
        commentApiService.addComment(postId, token, commentRegisterRequestDTO).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("comment api 호출 성공 ","성공" + response);
                }else{
                    Log.e("comment api 호출 실패 ","실패1" + response);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("comment api 호출 실패 ","실패2" + t.getMessage());
            }
        });
    }
}
