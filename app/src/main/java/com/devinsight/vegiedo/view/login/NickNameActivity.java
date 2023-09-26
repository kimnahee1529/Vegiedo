package com.devinsight.vegiedo.view.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.NickNameDTO;
import com.devinsight.vegiedo.data.response.StatusResponseDTO;
import com.devinsight.vegiedo.data.ui.login.NickNameStatus;
import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;
import com.devinsight.vegiedo.service.api.UserApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.utill.UserInfoTag;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NickNameActivity extends AppCompatActivity {

    private TextView btn_next;
    private EditText et_input_nick_name;
    /* 뷰모델 */
    private NickNameViewModel viewModel;
    boolean isAvaiableName;
    private Dialog dialog;
    private UserPrefRepository userPrefRepository;
    private AuthPrefRepository authPrefRepository;

    private String nickName;
    private int count;
    private String token;
    private  NickNameDTO nickNameDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nickname);
        btn_next = findViewById(R.id.tt_next);
        et_input_nick_name = findViewById(R.id.et_input_nick_name);

        userPrefRepository = new UserPrefRepository(NickNameActivity.this);
        authPrefRepository = new AuthPrefRepository(NickNameActivity.this);



        /* 한글,영어,숫자만 입력받는 EditText 필터 생성 */
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // 정규식을 사용하여 한글, 숫자, 영어만을 허용
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9가-힣]*$");
                for (int i = start; i < end; i++) {
                    if (!pattern.matcher(Character.toString(source.charAt(i))).matches()) {
                        return "";
                    }
                }
                return null;
            }
        };

        /* 필터 적용*/
        et_input_nick_name.setFilters(new InputFilter[]{filter});


        /* 뷰모델 */
        viewModel = new ViewModelProvider(this).get(NickNameViewModel.class);

        String social;
        if( authPrefRepository.getAuthToken("KAKAO") != null) {
            social = "KAKAO";
        } else {
            social = "GOOGLE";
        }
       token = authPrefRepository.getAuthToken(social);
        viewModel.getToken(token);


        /* text 내용 및 길이 입력받기*/
        et_input_nick_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            /* 입력 받은 닉네임 내용 및 길이 뷰모델로 전달 */
            @Override
            public void afterTextChanged(Editable editable) {
                nickName = editable.toString();
                count = editable.length();
                nickNameDTO = new NickNameDTO(nickName);

            }
        });



        /* 사용 가능한 닉네임인지 확인 */
        isAvaiableName = false;
//        viewModel.getNickNameLiveData().observe(this, new Observer<NickNameStatus>() {
//            @Override
//            public void onChanged(NickNameStatus nickNameStatus) {
//                if(nickNameStatus.getIsNicknameAvailable() == 0) {
//                    isAvaiableName = true;
//                } else if ( nickNameStatus.getIsNicknameAvailable() == 1 ||  nickNameStatus.getIsNicknameAvailable() == 2 ){
//                    isAvaiableName = false;
//                    /* 닉네임 불가 조건에 따른 메시지 입력 */
//                    if( nickNameStatus.getIsNicknameAvailable() == 1 ) {
//                        setDialog(nickNameStatus.getNickNameMessage());
//                    } else if ( nickNameStatus.getIsNicknameAvailable() == 2 ) {
//                        setDialog(nickNameStatus.getNickNameMessage());
//                    }
//
//                }
//            }
//        });


        btn_next.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nickNameDTO = new NickNameDTO(nickName);
            UserApiService userApiService = RetrofitClient.getUserApiService();
            userApiService.sendNickName("Bearer " + token, nickNameDTO).enqueue(new Callback<StatusResponseDTO>() {
                @Override
                public void onResponse(Call<StatusResponseDTO> call, Response<StatusResponseDTO> response) {
                    Log.d("닉네임","닉네임" + nickNameDTO.getNickName() + nickName);
                    if(response.isSuccessful()){
                        viewModel.setName(nickName, count, response.code());
                        viewModel.getNickNameLiveData().observe(NickNameActivity.this, new Observer<NickNameStatus>() {
                            @Override
                            public void onChanged(NickNameStatus nickNameStatus) {
                                if(nickNameStatus.getIsNicknameAvailable() == 0) {
                                    isAvaiableName = true;
                                    String userName = et_input_nick_name.getText().toString();
                                    userPrefRepository.saveUserInfo(UserInfoTag.USER_NICKNAME.getInfoType(), userName);
                                    Intent intent = new Intent(getApplicationContext(), SelectTagActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Log.d("사용자 이름","사용자 이름 : " + userName);
                                } else if ( nickNameStatus.getIsNicknameAvailable() == 1 ||  nickNameStatus.getIsNicknameAvailable() == 2 ){
                                    isAvaiableName = false;
                                    /* 닉네임 불가 조건에 따른 메시지 입력 */
                                    if( nickNameStatus.getIsNicknameAvailable() == 1 ) {
                                        setDialog(nickNameStatus.getNickNameMessage());
                                        dialog.show();
                                    } else if ( nickNameStatus.getIsNicknameAvailable() == 2 ) {
                                        setDialog(nickNameStatus.getNickNameMessage());
                                        dialog.show();
                                    }

                                }
                            }
                        });
                        Log.d("닉네임 관련","" + nickName + ", " + count);
                        Log.d("RetrofitRequestURL 성공", "Requested URL: " + call.request().url());
                        Log.d("닉네임 중복 체크 api 호출 성공 ","성공" + response);
                    }else{
                        Log.d("RetrofitRequestURL 실패", "Requested URL: " + call.request().url());
                        Log.e("닉네임 중복 체크 api 호출 실패 1 ","실패1" + response);
                        if(response.code() == 409){
                            viewModel.setName(nickName, count, response.code());
//                            setDialog("사용 중인 닉네임 입니다. 다시 적어 주세요!");
//                            dialog.show();
                        }
                        try {
                            Log.e("닉네임 중복 체크 실패 1", "Error Body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusResponseDTO> call, Throwable t) {
                    Log.e("닉네임 중복 체크 api 호출 실패 2 ","실패2" + t.getMessage());
                }
            });

        }
    });
}
    public void setDialog(String message) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_custom);

        TextView msg = dialog.findViewById(R.id.dialog);
        msg.setText(message);
    }

}