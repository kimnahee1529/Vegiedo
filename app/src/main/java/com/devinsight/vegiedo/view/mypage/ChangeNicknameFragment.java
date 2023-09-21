package com.devinsight.vegiedo.view.mypage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.UserNicknameModifyRequestDTO;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

public class ChangeNicknameFragment extends Fragment {
    private EditText changeNicknameText;
    private Button changeNickname_complete_btn;
    private TextView changeNickname_rule;
    ActivityViewModel viewModel;

    private Observer nicknameObserver = new Observer() {
        @Override
        public void onChanged(Object o) {
            Log.d("받은 닉네임 api","code:"+o.toString());
            if("200".equals(o.toString())){
                Log.d("닉네임 api성공","code:"+o);
                String updatedNickname = changeNicknameText.getText().toString();
                Log.d("바꾸려는 닉네임", "ChangeNicknameFragment"+updatedNickname);
                updateNickname(updatedNickname);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack();
            } else {
                showNicknameAlertDialog();
                Log.d("닉네임 api실패","code:"+o);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        View view = inflater.inflate(R.layout.fragment_change_nickname, container, false);

        changeNicknameText = view.findViewById(R.id.ChangeNickname_text);
        changeNickname_complete_btn = view.findViewById(R.id.ChangeNickname_complete_btn);
        changeNickname_rule = view.findViewById(R.id.ChangeNickname_rule);

        changeNickname_complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredText = changeNicknameText.getText().toString();
                UserNicknameModifyRequestDTO userNicknamedto = new UserNicknameModifyRequestDTO(enteredText);
                Log.d("닉네임", String.valueOf(userNicknamedto.getNickName()));

                if (enteredText.matches(".*[^a-zA-Z0-9가-힣].*")) {
                    Toast.makeText(getActivity(), "특수문자를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    changeNickname_rule.setTextColor(getResources().getColor(R.color.nickname_alert_red));
                    return;
                }

                if (enteredText.length() < 2 || enteredText.length() > 10) {
                    Toast.makeText(getActivity(), "닉네임은 2~10 글자 사이이어야 합니다.", Toast.LENGTH_SHORT).show();
                    changeNickname_rule.setTextColor(getResources().getColor(R.color.nickname_alert_red));
                    return;
                }

                Log.d("서버에 공통된 닉네임있는지 확인하기 전", "ChangeNicknameFragment");

                if (!viewModel.getUserNickNameDataLiveData().hasObservers()) {
                    viewModel.getUserNickNameDataLiveData().observe(getViewLifecycleOwner(), nicknameObserver);
                }

                viewModel.UserNicknameChange(userNicknamedto);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getUserNickNameDataLiveData().removeObserver(nicknameObserver);
    }

    private void showNicknameAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.already_use_nickname_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        ImageView greenXCircle = dialogView.findViewById(R.id.green_x_circle);
        greenXCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 다이얼로그 닫기
            }
        });
    }

    private void updateNickname(String newNickname) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", newNickname); // "userName" 키에 새로운 닉네임을 저장
        Log.d("저장된 닉네임", "ChangeNicknameFragment"+newNickname);
        editor.apply();
    }
}
