package com.devinsight.vegiedo.view.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.request.login.NickNameStatus;

public class NickNameViewModel extends ViewModel {

    private MutableLiveData<NickNameStatus> nickNameLiveData = new MutableLiveData<>();


    /* input : 입력받은 닉네임, count : 글자 수 */
    public void setName(String input, int count) {
        NickNameStatus nickNameStatus = new NickNameStatus(input, count, input);
        if (count <= 2) {
            nickNameStatus.setNickNameMessage("닉네임은 최소 2글자 이상 입력하셔야 해요 !");
            nickNameStatus.setIsNicknameAvailable(2);

            nickNameLiveData.setValue(nickNameStatus);

        } else if (input != null && input.equals("list")) {
            nickNameStatus.setNickNameMessage(" 사용중인 닉네임 입니다. 다시 적어주세요!");
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

}