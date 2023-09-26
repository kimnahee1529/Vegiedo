package com.devinsight.vegiedo.view.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.response.NickNameDTO;
import com.devinsight.vegiedo.data.response.StatusResponseDTO;
import com.devinsight.vegiedo.data.response.response;
import com.devinsight.vegiedo.data.ui.login.NickNameStatus;
import com.devinsight.vegiedo.service.api.UserApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NickNameViewModel extends ViewModel {

    private MutableLiveData<NickNameStatus> nickNameLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> codeLiveData = new MutableLiveData<>();
    private String token;
    private int code;


    public void getToken(String token){
        this.token = token;
    }

    public void getStatusCode(int code){
        this.code = code;
        codeLiveData.setValue(code);
    }

    /* input : 입력받은 닉네임, count : 글자 수 */
    public void setName(String input, int count, int code) {

        Log.d("코드","코드" + code);

        NickNameStatus nickNameStatus = new NickNameStatus(input, count, input);

        if (count <= 2 && code == 200 ) {
            nickNameStatus.setNickNameMessage("닉네임은 최소 2글자 이상 입력하셔야 해요 !");
            nickNameStatus.setIsNicknameAvailable(2);

            nickNameLiveData.setValue(nickNameStatus);

        } else if (count >=11 && code == 200){
            nickNameStatus.setNickNameMessage("닉네임은 최대  10글자 까지 가능 합니다. ");
            nickNameStatus.setIsNicknameAvailable(3);

            nickNameLiveData.setValue(nickNameStatus);

        }else if (input != null && code == 409 ) {
            nickNameStatus.setNickNameMessage(" 사용 중인 닉네임 입니다. 다시 적어주세요!");
            nickNameStatus.setIsNicknameAvailable(1);

            nickNameLiveData.setValue(nickNameStatus);

        } else {
            nickNameStatus.setNickName(input);
            nickNameStatus.setIsNicknameAvailable(0);

            nickNameLiveData.setValue(nickNameStatus);
        }

    }

    public LiveData<NickNameStatus> getNickNameLiveData() {
        return nickNameLiveData;
    }
    public LiveData<Integer> getCodeLiveData() {
        return codeLiveData;
    }


}