package com.devinsight.vegiedo.view.community;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.request.CommentRegisterRequestDTO;
import com.devinsight.vegiedo.service.api.CommentApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;

public class PostCommentViewModel extends ViewModel {

    CommentApiService commentApiService = RetrofitClient.getCommentApiService();

    public void getCommentContent(String input){
        CommentRegisterRequestDTO commentRegisterRequestDTO = new CommentRegisterRequestDTO(input);
        commentApiService.addComment()

    }
}
