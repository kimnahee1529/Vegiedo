package com.devinsight.vegiedo.view.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.devinsight.vegiedo.ConstLoginTokenType;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.utill.UserInfoTag;
import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;


import java.io.IOException;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMainActivity extends AppCompatActivity {

    private ImageView btn_kakaoLogin;
    private ImageView btn_googleLogin;
    private static final String TAG = "LOGIN";
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount googleSignInAccount;
    private FirebaseAuth googleAuth;

    /* 로그인 토큰 저장 관련*/
    ConstLoginTokenType constLoginTokenType;
    AuthPrefRepository authPrefRepository;
    UserPrefRepository userPrefRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);


        btn_kakaoLogin = findViewById(R.id.btn_kakaoLogin);
        btn_googleLogin = findViewById(R.id.btn_googleLogin);

        authPrefRepository = new AuthPrefRepository(this);
        userPrefRepository = new UserPrefRepository(this);


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
                    getKakaoAuth(oAuthToken.getAccessToken());
                    Log.d("카카오 토큰 ", " 카카오 토큰 : " + oAuthToken.getAccessToken().toString());
                    Intent intent = new Intent(getApplicationContext(), NickNameActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d("intent 완료 입니다", " 닉네임 입력 ");

                } else {
                    //로그인 실패
                    Log.e(TAG, "invoke: login fail");
                }

                return null;
            }
        };

        // 카카오 로그인 버튼 클릭 리스너
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

        /* 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정한다. */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.GOOGLE_CLIENT_KEY))
                .requestEmail()
                .build();

        /* GoogleSignInOption을 사용해 GoogleSignClient 객체 생성*/
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleAuth = FirebaseAuth.getInstance();




        /* 구글 로그인 */
        btn_googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInAccount = GoogleSignIn.getLastSignedInAccount(LoginMainActivity.this);
                googleSignIn();
                Log.d("구글 로그인 1", "googleSignIn(): 성공 ");
            }
        });

        PeriodicWorkRequest refreshTokenWorkRequest =
                new PeriodicWorkRequest.Builder(RefreshTokenWorker.class, 30, TimeUnit.DAYS)
                        .build();

        WorkManager.getInstance(this).enqueue(refreshTokenWorkRequest);
    }

    private void googleSignIn() {
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, 123);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                handleSignInResult(task);
            } catch (ApiException e) {
                Log.d("실패", "실패!!!!!!!!!!!");
            }
        }
    }

    /* 파이어베이스를 통해 구글로부터 idToken을 받아옵니다. */
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        googleAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String firebaseToken = task.getResult().getUser().getIdToken(false).getResult().getToken();
//                            FirebaseUser user = googleAuth.getCurrentUser();
//                            authPrefRepository.saveAuthToken("GOOGLE", firebaseToken);
                            /* 서버에 파이어베이스에서 인증 받은 토큰을 보냅니다. */
                            sendTokenToServer(firebaseToken);
                            Log.d("구글 토큰 보내기", "구글 토큰 보내기" + firebaseToken);
                        }
                    }
                });
    }


    /* 서버에 파이어베이스 토큰을 보내고 커스텀 토큰을 받아옵니다. */
    private void sendTokenToServer(String token) {

        Call<Void> call = RetrofitClient.getUserApiService().registerUser(token, "GOOGLE");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                /* 서버로부터 커스텀 토큰을 받아옵니다 */
                String customToken = response.headers().get("Authorization");
                /* 커스텀 토큰을 진짜 로그인을 위해 전달합니다.*/
                getGoogleLogin(customToken);

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("구글 파이어 베이스 토큰 보내기", "실패" + t.getMessage());
            }
        });

    }

    /* 서버로부터 받은 커스텀 토큰으로 로그인 */
    private void getGoogleLogin(String customToken) {
        googleAuth.signInWithCustomToken(customToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("토큰:커스텀 토큰 요청1", "성공" + customToken);
                            FirebaseUser user = googleAuth.getCurrentUser();
                            Log.e("토큰:커스텀 토큰 요청2","성공" + user);
                            String firebaseToken2 = task.getResult().getUser().getIdToken(false).getResult().getToken();
                            Log.e("토큰:커스텀 토큰 요청2","성공" + firebaseToken2);
                            Call<Void> call = RetrofitClient.getUserApiService().sendUserGoogleToken(firebaseToken2);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    /* 최종 사용자 토큰을 받아옵니다. */
                                    String userGoogleToken = response.headers().get("Authorization");
                                    Log.e("토큰:최종 구글 로그인1","성공" + response.headers());
                                    /* 최종적으로 구글 로그인 유저의 정보를 담은 토큰을 로컬에 저장합니다.*/
                                    authPrefRepository.saveAuthToken("GOOGLE", userGoogleToken);
                                    Log.e("토큰:최종 구글 로그인2","성공" + userGoogleToken);
                                }
                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e("토큰:최종 구글 로그인", "실패" + t.getMessage());
                                }
                            });

                            Intent intent = new Intent(getApplicationContext(), NickNameActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.e("커스텀 토큰 요청", "실패" + task.getException());
                        }
                    }
                });
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount act = completedTask.getResult(ApiException.class);
            if (act != null) {
                firebaseAuthWithGoogle(act.getIdToken());
                Uri userProfile = act.getPhotoUrl();
                Log.d("사용자 프로필", "구글 프로필 " + userProfile);
                userPrefRepository.saveUserInfo(UserInfoTag.USER_PROFILE.getInfoType(), userProfile.toString());
            }
        } catch (ApiException e) {

        }
    }

    public void getKakaoAuth(String kakaoAuth) {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (throwable != null) {
                    Log.e("Login error", throwable.toString());
                    return null;
                }

                String userProfile = user.getKakaoAccount().getProfile().getProfileImageUrl();
                userPrefRepository.saveUserInfo(UserInfoTag.USER_PROFILE.getInfoType(), userProfile);
                Log.d("this is auth", "kakao auth is = " + kakaoAuth);

                Call<Void> call = RetrofitClient.getUserApiService().registerUser(kakaoAuth, "KAKAO");
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            String userKaKAoToken = response.headers().get("Authorization");
                            authPrefRepository.saveAuthToken("KAKAO",userKaKAoToken);
                            Log.e("KAKAO"," 서버토큰 " + userKaKAoToken);
                            Log.e("KAKAO", "연동성공" + kakaoAuth);
                        }else {
                            // API 응답이 오류 상태일 때
                            Log.e("일반 포스트 요청 실패1 ", "Error Code: " + response.code() + ", Message: " + response.message());
                            try {
                                Log.e("일반 포스트 요청 실패1", "Error Body: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("KAKAO", "연동 실패" + t.getMessage());
                    }
                });

                return null;
            }
        });
    }


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

    @Override
    protected void onStart() {
        super.onStart();

    }
}