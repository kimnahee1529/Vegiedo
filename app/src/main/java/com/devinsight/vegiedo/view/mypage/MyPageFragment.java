package com.devinsight.vegiedo.view.mypage;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.SplashActivity;
import com.devinsight.vegiedo.service.api.UserApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.view.MainActivity;
import com.devinsight.vegiedo.view.PermissionUtils;
import com.devinsight.vegiedo.view.search.ActivityViewModel;
import com.devinsight.vegiedo.view.login.LoginMainActivity;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO : 로그아웃 기능 구현해야 함
public class MyPageFragment extends Fragment {
    private static final int REQUEST_IMAGE_PICK = 101;
    private static final int PERMISSION_REQUEST_CODE = 200; // 권한 요청 코드
    private TextView MyPage_nickname;
    private ImageView MyPage_profile_image;
    private TextView mypage_inquiry_text;
    private TextView mypage_policy_text;
    private TextView mypage_open_source_text;
    private TextView withdrawal_text;
    ActivityViewModel viewModel;
    UserApiService userApiService = RetrofitClient.getUserApiService();

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showToolbar(false);  // Toolbar 숨기기
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).showToolbar(true);  // Toolbar 표시
    }
    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            File file = new File(getActivity().getFilesDir(), "profileImage.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_page, container, false);
        MyPage_nickname = rootView.findViewById(R.id.MyPage_nickname);
        MyPage_profile_image = rootView.findViewById(R.id.MyPage_profile_image);
        viewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        // SharedPreferences 객체 얻기(회원가입 때 정한 닉네임, 태그 얻기 위함)
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "기본값"); // "기본값"은 userName이 없을 때 반환되는 기본값입니다.
        String userProfile = sharedPreferences.getString("userProfile","null"); //디폴트는 null

        MyPage_nickname.setText(userName);

        String savedImagePath = loadImagePathFromPreferences();

        //내부 저장소에 저장된 사용자의 프로필 이미지 경로가 있는지 확인
        if (savedImagePath != null) { //만약 null이 아니라면 사용자가 앱에서 프로필 이미지를 설정한 적이 있음
            //저장된 이미지 경로로부터 이미지를 로드
            Glide.with(getContext())
                    .load(savedImagePath)
                    .circleCrop()
                    .into(MyPage_profile_image);
        }
        //저장된 이미지가 없고 img_sheep일 때 양 이미지를 로드
        else if (userProfile.equals("img_sheep")) {
            MyPage_profile_image.setImageResource(R.drawable.img_sheep);
        }
        //저장된 이미지가 없고 기본 이미지가 아니라면 저장된 경로에서 이미지를 가져옴
        else {
            Glide.with(getContext())
                    .load(userProfile)
                    .circleCrop()
                    .into(MyPage_profile_image);
        }

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
                Log.d("닉네임 클릭", "MyPageFragment");
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
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, stampBookFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //신규 가게 등록 텍스트 클릭
        View newStoreRegisterText = rootView.findViewById(R.id.new_store_register_text);

        newStoreRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = viewModel.getToken().getValue();
                Intent intent = new Intent(getActivity(), RegisterNewStoreActivity.class);
                intent.putExtra("TOKEN", token);  // 토큰 값을 Intent에 추가
                startActivity(intent);
            }
        });


        //문의기능 텍스트 클릭
        mypage_inquiry_text = rootView.findViewById(R.id.mypage_inquiry_text);
        mypage_inquiry_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "one.month.one.project@gmail.com";  // 받는 사람의 이메일 주소를 여기에 입력하세요.

                Intent intent = new Intent(Intent.ACTION_SENDTO); // 이 액션은 기본 메일 앱만을 대상으로 합니다.
                intent.setData(Uri.parse("mailto:" + email));
//                intent.putExtra(Intent.EXTRA_SUBJECT, "베지도 메일 문의 드립니다"); // 메일 제목을 설정합니다.

                // 사용자의 장치에 이메일 클라이언트가 설치되어 있는지 확인합니다.
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "No email client available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //약관정책 텍스트 클릭
        mypage_policy_text = rootView.findViewById(R.id.mypage_policy_text);
        mypage_policy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://mammoth-tortoise-7d3.notion.site/60347bd4ed18422188e283ae90b788b6";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        //오픈소스 텍스트 클릭
        mypage_open_source_text = rootView.findViewById(R.id.mypage_open_source_text);
        mypage_open_source_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OssLicensesMenuActivity.class);
                intent.putExtra("activityTitle", "오픈소스 라이선스 목록");
//                OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스 목록"); //액티비티 제목 셋팅
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

        //회원탈퇴 텍스트 클릭
        TextView withdrawal_text = rootView.findViewById(R.id.withdrawal_text);  // 가정: withdrawal의 ID는 withdrawal입니다.

        withdrawal_text.setOnClickListener(new View.OnClickListener() {
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
//                Toast.makeText(getActivity(), "기본이미지 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                MyPage_profile_image.setImageResource(R.drawable.img_sheep);

                // SharedPreferences에 "img_sheep" 저장
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userProfile", "img_sheep");
                editor.apply();

                // img_sheep 리소스를 Bitmap으로 변환
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_sheep);

                // Bitmap을 ByteArrayOutputStream으로 변환
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteArray = baos.toByteArray();

                // byte[]을 RequestBody로 변환
//                RequestBody requestBody = RequestBody.create(
//                        MediaType.parse("image/jpeg"), byteArray);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), byteArray);

                // MultipartBody.Part 객체 생성
                // MultiPartBody 타입의 데이터를 넣어줄 multipartBody. 여기에 변경할 사진 파일 넣어줌
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("image", "img_sheep.jpg", requestBody);

                Log.d("이미지", "multipartBody:"+multipartBody.body().contentType());
                // API 호출을 위해 token 가져오기 (예시)
//                String token = sharedPreferences.getString("token", "");
                String token = viewModel.getToken().getValue();
                Log.d("이미지", token);
                // 이미지 업로드 API 호출
                Call<Void> call = userApiService.changeProfileImage("Bearer " + token, multipartBody);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            Log.d("이미지", response.body().toString());
                            Toast.makeText(getActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("이미지", String.valueOf(response.code()));
                            Toast.makeText(getActivity(), "Image upload failed: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("이미지", String.valueOf(t));
                        Toast.makeText(getActivity(), "Image upload failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                dialog.dismiss();
            }
        });

        album_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "앨범에서 이미지를 선택하세요.", Toast.LENGTH_SHORT).show();
                if (checkPermission()) {
                    // 권한이 이미 허용된 상태: 바로 관련 작업 수행
                    ImagePickerUtil.selectImageFromGallery(MyPageFragment.this, REQUEST_IMAGE_PICK);
                    dialog.dismiss();  // Uncomment if you want the dialog to close after selecting album image
                } else {
                    // 권한이 허용되지 않은 상태: 권한 요청
                    requestPermission();
                }
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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // 모든 데이터 삭제
        editor.apply();

        viewModel.LogoutUser();
    }

    private void showWithdrawalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // Fragment의 경우 getActivity()
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.withdrawal_dialog, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        dialog.setContentView(R.layout.dialog_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button yesBtn = dialogView.findViewById(R.id.yesBtn);
        Button noBtn = dialogView.findViewById(R.id.noBtn);
        ImageView closeImageView = dialogView.findViewById(R.id.green_x_circle);

        // 다이얼로그에 버튼 추가 (예: 확인, 취소)
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "예 버튼입니다.", Toast.LENGTH_SHORT).show();
                viewModel.DeleteUser();
                // SplashActivity로 이동
                Activity activity = getActivity();
                if (activity != null) {
                    Intent intent = new Intent(activity, LoginMainActivity.class);
                    startActivity(intent);
                    activity.finish();
                }
                dialog.dismiss();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "아니오 버튼입니다.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String savedImagePath = saveImageToInternalStorage(selectedImageUri);
            if (savedImagePath != null) {
                Glide.with(this)
                        .load(savedImagePath)
                        .circleCrop()
                        .into(MyPage_profile_image);
                saveImagePathToPreferences(savedImagePath);
            }
        }
    }
    private void saveImagePathToPreferences(String imagePath) {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedImagePath", imagePath);
        editor.apply();
    }

    private String loadImagePathFromPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
        return preferences.getString("selectedImagePath", null);
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