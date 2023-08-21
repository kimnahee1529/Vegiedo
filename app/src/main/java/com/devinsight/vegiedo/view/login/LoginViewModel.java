package com.devinsight.vegiedo.view.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel {

    private MutableLiveData<String> authLiveData = new MutableLiveData<>();

    public void getAuthTokenData(){

    }

    public LiveData<String> getAuthTokenLiveData(){
        return authLiveData;
    }
}
