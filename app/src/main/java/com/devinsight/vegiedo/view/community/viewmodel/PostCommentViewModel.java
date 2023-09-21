package com.devinsight.vegiedo.view.community.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.request.CommentRegisterRequestDTO;
import com.devinsight.vegiedo.data.response.CommentInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.CommentListData;
import com.devinsight.vegiedo.data.response.PostInquiryResponseDTO;
import com.devinsight.vegiedo.service.api.CommentApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.google.gson.Gson;

import org.w3c.dom.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCommentViewModel extends ViewModel {

    private String token;
    private Long postId;
    CommentApiService commentApiService = RetrofitClient.getCommentApiService();

    private MutableLiveData<List<CommentListData>> commentListLiveData = new MutableLiveData<>();

    public void getToken(String token){
        this.token = token;
    }

    public void getPostId(Long postId){
        this.postId = postId;
    }

    public void getCommentContent(String input){
        CommentRegisterRequestDTO commentRegisterRequestDTO = new CommentRegisterRequestDTO(input);
        commentRegisterRequestDTO.setContent(input);
        commentApiService.addComment(postId, "Bearer " + token, commentRegisterRequestDTO).enqueue(new Callback<List<CommentListData>>() {
            @Override
            public void onResponse(Call<List<CommentListData>> call, Response<List<CommentListData>> response) {
                if(response.isSuccessful()){
                    Log.d("comment 등록 api 호출 성공 ","성공" + response);

                    List<CommentListData> data = response.body();
                    Gson gson = new Gson();
                    String jsonData = gson.toJson(data);

                    commentListLiveData.setValue(data);
                    Log.d("postList json data","" + jsonData);
                }else{
                    Log.e("comment 등록 api 호출 실패 ","실패1" + response);
                }
            }
            @Override
            public void onFailure(Call<List<CommentListData>> call, Throwable t) {
                Log.e("comment 등록 api 호출 실패 ","실패2" + t.getMessage());
            }
        });
    }

    public LiveData<List<CommentListData>> getCommentListLiveData(){
        return commentListLiveData;
    }
}
