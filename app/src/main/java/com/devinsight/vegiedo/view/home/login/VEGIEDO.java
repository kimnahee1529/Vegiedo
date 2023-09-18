package com.devinsight.vegiedo.view.home.login;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class VEGIEDO extends Application {

    private VEGIEDO instance;

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this,"988056d0eaff3cc6abe70cdb9da63b80");
    }

}
