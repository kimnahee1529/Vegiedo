package com.devinsight.vegiedo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;
import com.devinsight.vegiedo.view.MainActivity;
import com.devinsight.vegiedo.view.home.HomeMainFragment;
import com.devinsight.vegiedo.view.login.LoginMainActivity;
import com.kakao.sdk.common.KakaoSdk;

public class SplashActivity extends AppCompatActivity {

    private AuthPrefRepository authPrefRepository;
    private UserPrefRepository userPrefRepository;
    Fragment homeMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        authPrefRepository = new AuthPrefRepository(this);
        userPrefRepository = new UserPrefRepository(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loginCheck();
            }
        }, 3000);


    }

    private void loginCheck() {
        String kakaoAuth = authPrefRepository.getAuthToken("KAKAO");
        homeMainFragment = new HomeMainFragment();
        Intent intent;
        if (kakaoAuth != null) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("home", R.id.nav_home);
        } else {
            intent = new Intent(getApplicationContext(), LoginMainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public void loadUserInfo(){

    }

    protected void onPause() {
        super.onPause();
    }
//

}