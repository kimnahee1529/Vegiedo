package com.devinsight.vegiedo.view.mypage;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.view.login.LoginMainActivity;
import com.devinsight.vegiedo.view.login.NickNameActivity;

//TODO : 로그아웃 기능 구현해야 함
public class MyPageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_page, container, false);

        // Find the mypage_camera view and set a click listener on it
        View mypageCamera = rootView.findViewById(R.id.mypage_camera);
        View mypagePencil = rootView.findViewById(R.id.change_nickname_image);

        //프로필 사진 변경
        mypageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileImageDialog();
            }
        });

        //닉네임 변경
        mypagePencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeNicknameFragment changeNicknameFragment = new ChangeNicknameFragment();

                // FragmentTransaction 시작
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace the current fragment with the new one
                transaction.replace(R.id.frame, changeNicknameFragment); // 'fragment_container'는 해당 Fragment를 추가하거나 교체할 레이아웃의 ID입니다. 이 이름을 실제 사용하는 이름으로 변경해야 합니다.

                // Optional: Add transaction to the back stack (if you want the user to navigate back)
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        //스탬프북 텍스트 클릭
        View stampBookText = rootView.findViewById(R.id.stamp_book_text);

        stampBookText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StampBookFragment stampBookFragment = new StampBookFragment();

                // FragmentTransaction 시작
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // 현재 fragment를 StampBookFragment로 교체
                transaction.replace(R.id.frame, stampBookFragment); // 'fragment_container'는 해당 Fragment를 추가하거나 교체할 레이아웃의 ID입니다. 이 이름을 실제 사용하는 이름으로 변경해야 합니다.

                // (선택사항) 백스택에 추가
                transaction.addToBackStack(null);

                // 변경사항 커밋
                transaction.commit();
            }
        });

        //신규 가게 등록 텍스트 클릭
        View newStoreRegisterText = rootView.findViewById(R.id.new_store_register_text);

        newStoreRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterNewStoreActivity.class);
                startActivity(intent);
            }
        });


        //로그아웃 텍스트 클릭
        TextView logoutText = rootView.findViewById(R.id.logout); // 가정: logout 텍스트의 ID는 logout입니다.

        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃 로직 (예: 사용자 정보, 세션, 토큰 삭제 등)
                logoutUser();


                // LoginMainActivity로 이동
                Activity activity = getActivity();
                if (activity != null) {
                    Intent intent = new Intent(activity, LoginMainActivity.class);
                    startActivity(intent);
                    activity.finish();
                }
//                Intent intent = new Intent(getActivity(), LoginMainActivity.class);
//                startActivity(intent);

                // (선택사항) 현재 Activity 종료
//                finish();
            }
        });

        TextView withdrawal = rootView.findViewById(R.id.withdrawal);  // 가정: withdrawal의 ID는 withdrawal입니다.

        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWithdrawalDialog();
            }
        });

        return rootView;
    }

    private void showProfileImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.profile_image_dialog, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명하게 설정

        // Manually set the dialog size
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = 400;
        layoutParams.height = 800;
//      layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  // Or use specific size in pixels
//        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Or use specific size in pixels
        window.setAttributes(layoutParams);

        // Find the buttons and set click listeners on them
        Button defualt_image = dialogView.findViewById(R.id.defualt_image);
        Button album_image = dialogView.findViewById(R.id.album_image);
        ImageView closeImageView = dialogView.findViewById(R.id.green_x_circle);

        defualt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "기본이미지 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        album_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "앨범에서 이미지를 선택하세요.", Toast.LENGTH_SHORT).show();
                 dialog.dismiss();  // Uncomment if you want the dialog to close after selecting album image
            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void logoutUser() {
        // 여기에서 실제 로그아웃 처리를 합니다.
        // 예를 들어 SharedPreferences에 저장된 사용자 정보나 토큰을 삭제하거나
        // 서버에 로그아웃 요청을 보내는 로직 등을 추가할 수 있습니다.
        // SharedPreferences 예제:

//        SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear(); // 모든 데이터 삭제
//        editor.apply();
    }

    private void showWithdrawalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // Fragment의 경우 getActivity()
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.withdrawal_dialog, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        Button yesBtn = dialogView.findViewById(R.id.yesBtn);
        Button noBtn = dialogView.findViewById(R.id.noBtn);
        ImageView closeImageView = dialogView.findViewById(R.id.green_x_circle);

        // 다이얼로그에 버튼 추가 (예: 확인, 취소)
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "예 버튼입니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "아니오 버튼입니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();  // Uncomment if you want the dialog to close after selecting album image
            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}