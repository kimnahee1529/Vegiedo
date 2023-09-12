package com.devinsight.vegiedo.view.community;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.PostRegisterRequestDTO;
import com.devinsight.vegiedo.view.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

//TODO : moveToCommunityFragment에 인기게시글->글쓰기->일반게시글로 돌아오는 부분 수정
public class WritingFragment extends Fragment {

    private enum DialogType {
        TITLE,
        CONTENT
    }

    private EditText communityWritingContentText;
    private TextView communityStringLength;
    private EditText communityWritingTitleText;
    private ImageView backwardBtn;
    private List<String> selectedImageUris = new ArrayList<>();
    private Button registerBtn;
    private ImageView mainImage;
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int PERMISSION_REQUEST_CODE = 200; // 권한 요청 코드
    private static final int MAX_IMAGE_COUNT = 5;
    private View rootView;
    private String previousFragment;
    private ImageView currentlySelectedImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_writing, container, false);
        /* 초기화 합니다 */
        initializeViews(rootView);


        setTitleTextWatcher();
        setContentTextWatcher();
        setRegisterButtonListener();
        restoreSelectedImages();

        if (getArguments() != null) {
            previousFragment = getArguments().getString("previousFragment");
        }


        return rootView;
    }

    private void initializeViews(View rootView) {
        communityWritingContentText = rootView.findViewById(R.id.community_writing_content_text);
        communityStringLength = rootView.findViewById(R.id.community_string_length);
        communityWritingTitleText = rootView.findViewById(R.id.community_writing_title_text);
        registerBtn = rootView.findViewById(R.id.community_writing_register_button);
        backwardBtn = rootView.findViewById(R.id.backward_btn);
        backwardBtn.setOnClickListener(v -> goBack());

        mainImage = rootView.findViewById(R.id.main_image1);
        mainImage.setOnClickListener(v -> selectImageForView((ImageView) v));

        ImageView mainImage2 = rootView.findViewById(R.id.main_image2);
        mainImage2.setOnClickListener(v -> selectImageForView((ImageView) v));

        ImageView mainImage3 = rootView.findViewById(R.id.main_image3);
        mainImage3.setOnClickListener(v -> selectImageForView((ImageView) v));

        ImageView mainImage4 = rootView.findViewById(R.id.main_image4);
        mainImage4.setOnClickListener(v -> selectImageForView((ImageView) v));

        ImageView mainImage5 = rootView.findViewById(R.id.main_image5);
        mainImage5.setOnClickListener(v -> selectImageForView((ImageView) v));
    }

    private void restoreSelectedImages() {
        int[] imageViews = {R.id.main_image1, R.id.main_image2, R.id.main_image3, R.id.main_image4, R.id.main_image5};
        for (int i = 0; i < selectedImageUris.size(); i++) {
            ImageView imageView = rootView.findViewById(imageViews[i]);
            imageView.setImageURI(Uri.parse(selectedImageUris.get(i)));
            imageView.setBackground(null);
        }
    }

    private void setTitleTextWatcher() {
        setTextWatcher(communityWritingTitleText, 20, "최대 20자까지 입력 가능합니다.");
    }

    private void setTextWatcher(EditText editText, int maxLength, String message) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if (length > maxLength) {
                    editable.delete(maxLength, length);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void setContentTextWatcher() {
        communityWritingContentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length > 1000) {
                    s.delete(1000, length);
                    Toast.makeText(getContext(), "최대 1000자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                }

                String lengthString = s.length() + "자/1000자";
                communityStringLength.setText(lengthString);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void setRegisterButtonListener() {
        registerBtn.setOnClickListener(v -> {
            String titleText = communityWritingTitleText.getText().toString();
            String contentText = communityWritingContentText.getText().toString();

            if (TextUtils.isEmpty(titleText)) {
                showDialog(DialogType.TITLE);
                return;
            }

            if (TextUtils.isEmpty(contentText)) {
                showDialog(DialogType.CONTENT);
                return;
            }

            PostRegisterRequestDTO postRegisterRequestDTO = selectedImageUris.isEmpty() ?
                    new PostRegisterRequestDTO(titleText, contentText) :
                    new PostRegisterRequestDTO(titleText, contentText, selectedImageUris);

            Log.d("LOG 등록버튼", "제목: " + postRegisterRequestDTO.getPostTitle() +
                    " 내용: " + postRegisterRequestDTO.getContent() +
                    " 이미지: " + postRegisterRequestDTO.getImages());

            sendPostRequest(postRegisterRequestDTO);


//            moveToCommunityFragment();

        });
    }

    private void sendPostRequest(PostRegisterRequestDTO postRegisterRequestDTO) {
//        Call<Void> call = RetrofitClient.getRetrofit("").create(PostApiService.class).addPost(postRegisterRequestDTO);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {}
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {}
//        });
    }

    private void showDialog(DialogType type) {
        int layoutId = (type == DialogType.TITLE) ? R.layout.request_input_title_dialog : R.layout.request_input_content_dialog;

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(layoutId, null);
        ImageView closeIcon = dialogView.findViewById(R.id.green_x_circle);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        closeIcon.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


//    private void moveToCommunityFragment() {
//        Fragment targetFragment;
//
//        if ("GeneralPostFragment".equals(previousFragment)) {
//            targetFragment = new GeneralPostFragment();
//        } else if ("PopuralPostFragment".equals(previousFragment)) {
//            targetFragment = new PopuralPostFragment();
//        } else {
//            targetFragment = new GeneralPostFragment(); // 기본값
//        }
//
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frame, targetFragment)
//                .commit();
//    }


//    private void selectImagesFromGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
//    }

    private void selectImageForView(ImageView imageView) {
        // Check if permission is already granted
//        if (checkPermission()) {
//            // If permission is already granted, allow user to select an image
//            currentlySelectedImageView = imageView;
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
//        } else {
//            // If permission is not granted, request for permission
//            requestPermission();
//        }

        if (PermissionUtils.checkPermission(getActivity())) {
            // 권한이 이미 허용된 상태: 바로 관련 작업 수행
            currentlySelectedImageView = imageView;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
        } else {
            // 권한이 허용되지 않은 상태: 권한 요청
            PermissionUtils.requestPermission(getActivity(), PERMISSION_REQUEST_CODE);
//            requestPermission();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = null;

            if (data.getData() != null) {
                selectedImageUri = data.getData();
            }

            if (currentlySelectedImageView != null && selectedImageUri != null) {
                currentlySelectedImageView.setImageURI(selectedImageUri);
                currentlySelectedImageView.setBackground(null);

                // If you want to save this image URI to your selectedImageUris list:
                int tag = Integer.parseInt((String) currentlySelectedImageView.getTag());
                if (tag < selectedImageUris.size()) {
                    selectedImageUris.set(tag, selectedImageUri.toString());
                } else {
                    selectedImageUris.add(selectedImageUri.toString());
                }
            }
        }
    }

    private void updateSelectedImages(List<Uri> imageUris) {
        selectedImageUris.clear();

        int[] imageViews = {R.id.main_image1, R.id.main_image2, R.id.main_image3, R.id.main_image4, R.id.main_image5};
        for (int i = 0; i < Math.min(imageUris.size(), MAX_IMAGE_COUNT); i++) {
            selectedImageUris.add(imageUris.get(i).toString());
            ImageView imageView = getView().findViewById(imageViews[i]);
            imageView.setImageURI(imageUris.get(i));
            imageView.setBackground(null);
        }
    }

    private void goBack() {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // Open the gallery now as permission is granted
                    currentlySelectedImageView = getView().findViewById(R.id.main_image1); // default view
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
