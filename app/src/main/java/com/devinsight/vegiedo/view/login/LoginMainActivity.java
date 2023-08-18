package com.devinsight.vegiedo.view.login;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.security.MessageDigest;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class LoginMainActivity extends AppCompatActivity {

    private ImageView btn_kakaoLogin;
    private ImageView btn_googleLogin;
    private static final String TAG = "LOGIN";
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        btn_kakaoLogin = findViewById(R.id.btn_kakaoLogin);
        btn_googleLogin = findViewById(R.id.btn_googleLogin);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        // 카카오톡이 설치되어 있는지 확인하는 메서드 , 카카오에서 제공함. 콜백 객체를 이용합.
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            // 콜백 메서드 ,
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                Log.e(TAG, "CallBack Method");
                //oAuthToken != null 이라면 로그인 성공
                if (oAuthToken != null) {
                    // 토큰이 전달된다면 로그인이 성공한 것이고 토큰이 전달되지 않으면 로그인 실패한다.
//                    updateKakaoLoginUi();
                    Intent intent = new Intent(getApplicationContext(), NickNameActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    //로그인 실패
                    Log.e(TAG, "invoke: login fail");
                }

                return null;
            }
        };

        // 로그인 버튼 클릭 리스너
        btn_kakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 해당 기기에 카카오톡이 설치되어 있는 확인
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginMainActivity.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginMainActivity.this, callback);
                } else {
                    // 카카오톡이 설치되어 있지 않다면
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginMainActivity.this, callback);
                }
            }
        });

        btn_googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                googleLogin();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                googleLoginLauncher.launch(signInIntent);
                Log.d("로그인 성공","");
                Intent intent = new Intent(getApplicationContext(), NickNameActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // 로그아읏 버튼
//        btn_kakaoLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
//                    @Override
//                    public Unit invoke(Throwable throwable) {
//                        updateKakaoLoginUi();
//                        return null;
//                    }
//                });
//            }
//        });
//
//        updateKakaoLoginUi();
    }

//    private void updateKakaoLoginUi() {
//
//        // 로그인 여부에 따른 UI 설정
//        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
//            @Override
//            public Unit invoke(User user, Throwable throwable) {
//
//                if (user != null) {
//
//                    // 유저의 아이디
//                    Log.d(TAG, "invoke: id =" + user.getId());
//                    // 유저의 이메일
//                    Log.d(TAG, "invoke: email =" + user.getKakaoAccount().getEmail());
//                    // 유저의 닉네임
//                    Log.d(TAG, "invoke: nickname =" + user.getKakaoAccount().getProfile().getNickname());
//                    // 유저의 성별
//                    Log.d(TAG, "invoke: gender =" + user.getKakaoAccount().getGender());
//                    // 유저의 연령대
//                    Log.d(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());
//
//
//                    // 유저 닉네임 세팅해주기
////                    nickName.setText(user.getKakaoAccount().getProfile().getNickname());
////                    // 유저 프로필 사진 세팅해주기
////                    Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImage);
////                    Log.d(TAG, "invoke: profile = "+user.getKakaoAccount().getProfile().getThumbnailImageUrl());
////
////                    // 로그인이 되어있으면
////                    loginButton.setVisibility(View.GONE);
////                    logoutButton.setVisibility(View.VISIBLE);
//                } else {
//                    // 로그인 되어있지 않으면
////                    nickName.setText(null);
////                    profileImage.setImageBitmap(null);
////
////                    loginButton.setVisibility(View.VISIBLE);
////                    logoutButton.setVisibility(View.GONE);
//                }
//                return null;
//            }
//        });
//    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String somthing = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", somthing);
            }
        } catch (Exception e) {
            Log.e("Name not found", e.toString());
        }
    }

    public void googleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleLoginLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> googleLoginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        getGoogleInfo(task);
                    }
                }
            });

    private void getGoogleInfo(Task<GoogleSignInAccount> completedTask) {
        String TAG = "구글 로그인 결과";
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, account.getId());
            Log.d(TAG, account.getFamilyName());
            Log.d(TAG, account.getGivenName());
            Log.d(TAG, account.getEmail());

//            name.setText(account.getEmail());
//            Toast.makeText(getApplicationContext(),"로그인 성공!",Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

}


