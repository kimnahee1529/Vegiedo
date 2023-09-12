package com.devinsight.vegiedo.view.mypage;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.view.PermissionUtils;
import com.devinsight.vegiedo.view.login.LoginMainActivity;
import com.devinsight.vegiedo.view.login.NickNameActivity;

// TODO : 로그아웃 기능 구현해야 함
public class MyPageFragment extends Fragment {
    private static final int REQUEST_IMAGE_PICK = 101;
    private static final int PERMISSION_REQUEST_CODE = 200; // 권한 요청 코드
    private TextView MyPage_nickname;
    private ImageView MyPage_profile_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_page, container, false);
        MyPage_nickname = rootView.findViewById(R.id.MyPage_nickname);
        MyPage_profile_image = rootView.findViewById(R.id.MyPage_profile_image);

        // SharedPreferences 객체 얻기(회원가입 때 정한 닉네임, 태그 얻기 위함)
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        // userName 값 읽기
        String userName = sharedPreferences.getString("userName", "기본값"); // "기본값"은 userName이 없을 때 반환되는 기본값입니다.
        String userProfile = sharedPreferences.getString("userProfile","null"); //디폴트는 null

        MyPage_nickname.setText(userName);
        Glide.with(getContext())
                .load(userProfile)
                .circleCrop()
                .into(MyPage_profile_image);

        ImageView mypageCamera = rootView.findViewById(R.id.my_page_camera_image);
        ImageView mypagePencil = rootView.findViewById(R.id.my_page_change_nickname_image);

        mypageCamera.setVisibility(View.VISIBLE);


        //프로필 사진 변경
        mypageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileImageDialog();
//                if (checkPermission()) {
//                    // 권한이 이미 허용된 상태: 바로 관련 작업 수행
//                    showProfileImageDialog();
//                } else {
//                    // 권한이 허용되지 않은 상태: 권한 요청
//                    requestPermission();
//                }
            }
        });

        //닉네임 변경
        mypagePencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeNicknameFragment changeNicknameFragment = new ChangeNicknameFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, changeNicknameFragment);
                transaction.addToBackStack(null);
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
        TextView logoutText = rootView.findViewById(R.id.logout_text); // 가정: logout 텍스트의 ID는 logout입니다.

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

        TextView withdrawal = rootView.findViewById(R.id.withdrawal_text);  // 가정: withdrawal의 ID는 withdrawal입니다.

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
        window.setAttributes(layoutParams);

        // Find the buttons and set click listeners on them
        Button defualt_image = dialogView.findViewById(R.id.defualt_image);
        Button album_image = dialogView.findViewById(R.id.album_image);
        ImageView closeImageView = dialogView.findViewById(R.id.green_x_circle);

        defualt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "기본이미지 선택하셨습니다.", Toast.LENGTH_SHORT).show();

                ImageView myImageView = getActivity().findViewById(R.id.MyPage_profile_image);
                // 해당 ImageView에 img_sheep 이미지를 설정
                myImageView.setImageResource(R.drawable.img_sheep);

                dialog.dismiss();
            }
        });

        album_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "앨범에서 이미지를 선택하세요.", Toast.LENGTH_SHORT).show();
                if (checkPermission()) {
                    // 권한이 이미 허용된 상태: 바로 관련 작업 수행
                    ImagePickerUtil.selectImageFromGallery(MyPageFragment.this, REQUEST_IMAGE_PICK);
                    dialog.dismiss();  // Uncomment if you want the dialog to close after selecting album image
                } else {
                    // 권한이 허용되지 않은 상태: 권한 요청
                    requestPermission();
                }

//                if (PermissionUtils.checkPermission(getActivity())) {
//                    // 권한이 이미 허용된 상태: 바로 관련 작업 수행
//                    Toast.makeText(getActivity(), "앨범에서 이미지를 선택하세요.", Toast.LENGTH_SHORT).show();
//                    ImagePickerUtil.selectImageFromGallery(MyPageFragment.this, REQUEST_IMAGE_PICK);
//                    dialog.dismiss();  // Uncomment if you want the dialog to close after selecting album image
//                } else {
//                    // 권한이 허용되지 않은 상태: 권한 요청
//                    PermissionUtils.requestPermission(getActivity(), PERMISSION_REQUEST_CODE);
//                    requestPermission();
//                }
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

//    private void selectImageFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, REQUEST_IMAGE_PICK);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {

            if (data != null) {

                ImageView myImageView = getActivity().findViewById(R.id.MyPage_profile_image);
                android.net.Uri selectedImageUri = data.getData();
//                myImageView.setImageURI(selectedImageUri);
                Glide.with(this)
                        .load(selectedImageUri)
                        .circleCrop()  // 원형 이미지로 변환
                        .into(myImageView);
            }
        }
    }

    //갤러리 접근 권한
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            // grantResults 배열에는 요청한 권한들의 허용 여부가 저장되어 있습니다.
            // 권한이 허용되었는지 확인하기 위해 grantResults[0]을 체크합니다.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용되었으므로 바로 갤러리를 엽니다.
                ImagePickerUtil.selectImageFromGallery(MyPageFragment.this, REQUEST_IMAGE_PICK);
            } else {
                Toast.makeText(getActivity(), "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}