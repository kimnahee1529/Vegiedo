package com.devinsight.vegiedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;
import com.devinsight.vegiedo.view.login.LoginMainActivity;
import com.kakao.sdk.common.KakaoSdk;

public class SplashActivity extends AppCompatActivity {

    private AuthPrefRepository authPrefRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginMainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);


//        loginCheck();

    }

//    private void loginCheck(){
//        String kakaoAuth = authPrefRepository.getAuthToken("KAKAO");
//        if(kakaoAuth != null) {
//            Intent intent = new Intent(this, )
//        }
//    }

    protected void onPause() {
        super.onPause();
    }
}