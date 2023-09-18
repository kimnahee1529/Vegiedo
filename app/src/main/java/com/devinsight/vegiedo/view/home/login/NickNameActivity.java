package com.devinsight.vegiedo.view.home.login;

import androidx.appcompat.app.AppCompatActivity;
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
import com.devinsight.vegiedo.data.ui.login.NickNameStatus;
import com.devinsight.vegiedo.utill.UserInfoTag;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;

import java.util.regex.Pattern;

public class NickNameActivity extends AppCompatActivity {

    private TextView btn_next;
    private EditText et_input_nick_name;
    /* 뷰모델 */
    private NickNameViewModel viewModel;
    boolean isAvaiableName;
    private Dialog dialog;
    private UserPrefRepository userPrefRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nickname);
        btn_next = findViewById(R.id.tt_next);
        et_input_nick_name = findViewById(R.id.et_input_nick_name);

        userPrefRepository = new UserPrefRepository(this);

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
                viewModel.setName(editable.toString(), editable.length());
            }
        });

        /* 사용 가능한 닉네임인지 확인 */
        isAvaiableName = false;
        viewModel.getNickNameLiveData().observe(this, new Observer<NickNameStatus>() {
            @Override
            public void onChanged(NickNameStatus nickNameStatus) {
                if(nickNameStatus.getIsNicknameAvailable() == 0) {
                    isAvaiableName = true;
                } else if ( nickNameStatus.getIsNicknameAvailable() == 1 ||  nickNameStatus.getIsNicknameAvailable() == 2 ){
                    isAvaiableName = false;
                    /* 닉네임 불가 조건에 따른 메시지 입력 */
                    if( nickNameStatus.getIsNicknameAvailable() == 1 ) {
                        setDialog(nickNameStatus.getNickNameMessage());
                    } else if ( nickNameStatus.getIsNicknameAvailable() == 2 ) {
                        setDialog(nickNameStatus.getNickNameMessage());
                    }

                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAvaiableName == true) {
                    String userName = et_input_nick_name.getText().toString();
                    userPrefRepository.saveUserInfo(UserInfoTag.USER_NICKNAME.getInfoType(), userName);
                    Intent intent = new Intent(getApplicationContext(), SelectTagActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d("사용자 이름","사용자 이름 : " + userName);
                }else if(isAvaiableName == false) {
                    if(dialog != null && !dialog.isShowing()){
                        dialog.show();
                    }
                }
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