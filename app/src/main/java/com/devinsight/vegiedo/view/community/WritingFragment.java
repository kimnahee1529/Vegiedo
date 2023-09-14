package com.devinsight.vegiedo.view.community;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.PostRegisterRequestDTO;
import com.devinsight.vegiedo.data.response.PostRegisterResponseDTO;
import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;
import com.devinsight.vegiedo.service.api.PostApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.view.PermissionUtils;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private List<Uri> imageUrilist;

    private String token;
    private Long postId;

    /* 수정을 위해 전달 받은 데이터를 담을 변수 */
    private String postTitle;
    private String postContent;
    private String imageUrl;
    private List<String> imageListForModify;
    /* 수정하기 버튼으로부터 열린 페이지인지 */
    private boolean isModify;
    private int imageListSizeForModify;
    private List<Uri> imageUriListForModify;
    private List<String> imageUrlListForModify;

    ActivityViewModel activityViewModel;

    PostApiService postApiService = RetrofitClient.getPostApiService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        if (getArguments() != null) {
            postTitle = getArguments().getString("postTitle");
            postContent = getArguments().getString("postContent");
            imageUrl = getArguments().getString("imageList");
            imageListSizeForModify = getArguments().getInt("imageListSize");
            imageListForModify = new ArrayList<>();
            isModify = getArguments().getBoolean("isModify");
            postId = getArguments().getLong("postId");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_writing, container, false);


        imageUrilist = new ArrayList<>();
        imageUriListForModify = new ArrayList<>();
        imageUrlListForModify = new ArrayList<>();
//        files = new ArrayList<>();


        initializeViews(rootView);
        setTitleTextWatcher();
        setContentTextWatcher();
        setRegisterButtonListener();
        if (!isModify) {
            restoreSelectedImages();
        }

        AuthPrefRepository authPrefRepository = new AuthPrefRepository(getContext());

        String social;
        if (authPrefRepository.getAuthToken("KAKAO") != null) {
            social = "KAKAO";
        } else {
            social = "GOOGLE";
        }
        this.token = authPrefRepository.getAuthToken(social);


        if (getArguments() != null) {
            previousFragment = getArguments().getString("previousFragment");
        }


        return rootView;
    }

    /* 뷰 초기화 */
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

        if (isModify) {
            communityWritingTitleText.setText(postTitle);
            communityWritingContentText.setText(postContent);
            int[] imageViews = {R.id.main_image1, R.id.main_image2, R.id.main_image3, R.id.main_image4, R.id.main_image5};
            activityViewModel.getImageUrlListForModifyLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> imageUrlList) {
                    for (int i = 0; i < imageViews.length; i++) {
                        if (i < imageUrlList.size()) {
                            ImageView imageView = rootView.findViewById(imageViews[i]);
                            Glide.with(getActivity()).load(imageUrlList.get(i)).into(imageView);
                            imageView.setTag(imageUrlList.get(i));
                            Log.d("수정을 위해 넘어온 image url 4 ", "this is url" + imageUrlList.get(i));
                        }
                    }

                }
            });

        }
    }

    /* */
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
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


            List<MultipartBody.Part> files = new ArrayList<>();
            List<MultipartBody.Part> imageUrlFromServer = new ArrayList<>();
            imageUrlListForModify.clear();
            if (isModify) {
                for( int i = 0 ; i < imageUriListForModify.size(); i ++ ) {
                    String addedFilePath = getFilePath(getActivity(), imageUriListForModify.get(i));
                    if(addedFilePath != null) {
                        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), new File(addedFilePath));
                        String fileName = "photo" + i + ".jpg";
                        MultipartBody.Part filePart = MultipartBody.Part.createFormData("images", fileName, fileBody);
                        files.add(filePart);
                    }

                }
                RequestBody modifyTitle = RequestBody.create(MediaType.parse("text/plain"), titleText);
                RequestBody modifyContent = RequestBody.create(MediaType.parse("text/plain"), contentText);

                activityViewModel.getImageUrlListForModifyLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> urlListFromServer) {
                        for( String url : urlListFromServer ) {
                            if ( url.equals(currentlySelectedImageView.getTag())) {
                                imageUrlListForModify.add(url);
                            }
                        }
                    }
                });

                for ( int i = 0 ; i < imageUrlListForModify.size() ; i ++ ) {
                    String imageUrl = imageUrlListForModify.get(i);
                    RequestBody urlBody = RequestBody.create(MediaType.parse("text/plain"), imageUrl);
                    String urlName = "url" + i;
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("imageUrls", urlName, urlBody);
                    imageUrlFromServer.add(filePart);
                }

                postApiService.updatePost("Bearer" + token, postId, modifyTitle, modifyContent, files, imageUrlFromServer).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("post 수정 api 호출 성공 ", "성공" + response);
                        } else {
                            Log.e("post 수정 api 호출 실패 ", "실패1" + response);

                            // 예외처리 및 오류 로그
                            try {
                                // 오류 응답에서 오류 메시지를 가져와서 로그에 기록
                                String errorBody = response.errorBody().string();
                                Log.e("오류 응답 본문", errorBody);
                            } catch (IOException e) {
                                // 오류 본문을 읽어오지 못하는 경우에 대한 예외처리
                                Log.e("오류 응답 본문 읽기 실패", e.getMessage(), e);
                            }
                            Log.e("post 수정 api 호출 실패 ", "실패1" + response);
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("post 수정 api 호출 실패 ", "실패2" + t.getMessage());

                    }
                });





            } else {
                for (int i = 0; i < imageUrilist.size(); i++) {
                    String filePath = getFilePath(getActivity(), imageUrilist.get(i));

                    if (filePath != null) {
//                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), new File(filePath));
                        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), new File(filePath));
                        String fileName = "photo" + i + ".jpg";
                        MultipartBody.Part filePart = MultipartBody.Part.createFormData("images", fileName, fileBody);
                        files.add(filePart);
                    }
                }

                Log.d("files 리스트 크기", "크기: " + files.size());
                RequestBody titleRequestBody = RequestBody.create(MediaType.parse("text/plain"), titleText);
                MultipartBody.Part titlePart = MultipartBody.Part.createFormData("postTitle", titleText, titleRequestBody);

                RequestBody contentRequestBody = RequestBody.create(MediaType.parse("text/plain"), contentText);
                MultipartBody.Part contentPart = MultipartBody.Part.createFormData("content", contentText, contentRequestBody);

                Log.d("this is content", "this is content : " + contentRequestBody);


                Log.d("토큰", "토큰" + token);
                postApiService.addPost("Bearer " + token, files, titleRequestBody, contentRequestBody).enqueue(new Callback<PostRegisterResponseDTO>() {
                    @Override
                    public void onResponse(Call<PostRegisterResponseDTO> call, Response<PostRegisterResponseDTO> response) {
                        if (response.isSuccessful()) {
                            PostRegisterResponseDTO data = response.body();
                            String imageUrl = data.getImages().toString();
                            Log.d("이미지 url", "url" + imageUrl);
                            Log.d("내용", "content" + data.getContent());
                            Log.d("내용", "title" + data.getPostTitle());

                            Log.d("post 등록 api 호출 성공 ", "성공" + response);
                        } else {
                            Log.e("post 등록 api 호출 실패 ", "실패1" + response);

                            // 예외처리 및 오류 로그
                            try {
                                // 오류 응답에서 오류 메시지를 가져와서 로그에 기록
                                String errorBody = response.errorBody().string();
                                Log.e("오류 응답 본문", errorBody);
                            } catch (IOException e) {
                                // 오류 본문을 읽어오지 못하는 경우에 대한 예외처리
                                Log.e("오류 응답 본문 읽기 실패", e.getMessage(), e);
                            }
                            Log.e("post 등록 api 호출 실패 ", "실패1" + response);
                        }
                    }

                    @Override
                    public void onFailure(Call<PostRegisterResponseDTO> call, Throwable t) {
                        Log.e("post 등록 api 호출 실패 ", "실패2" + t.getMessage());
                    }
                });
            }


            Log.d("files", "files" + files.size());

        });
    }

    private String getFilePath(Context context, Uri uri) {
        String filePath = null;
        if (Build.VERSION.SDK_INT < 11) {
            filePath = RealPathUtil.getRealPathFromURI_BelowAPI11(context, uri);
        } else if (Build.VERSION.SDK_INT < 19) {
            filePath = RealPathUtil.getRealPathFromURI_API11to18(context, uri);
        } else {
            filePath = RealPathUtil.getRealPathFromURI_API19(context, uri);
        }
        return filePath;
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


    private void selectImageForView(ImageView imageView) {

        currentlySelectedImageView = imageView;

        if (PermissionUtils.checkPermission(getActivity())) {
            // 권한이 이미 허용된 상태: 바로 관련 작업 수행
//            currentlySelectedImageView = imageView;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
        } else {
            // 권한이 허용되지 않은 상태: 권한 요청
//            PermissionUtils.requestPermission(getActivity(), PERMISSION_REQUEST_CODE);
            requestGalleryPermission();
//            requestPermission();
        }
    }

    private void requestGalleryPermission() {
        // 권한을 요청합니다.
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            /* Uri 를 담을 변수를 초기화 해줍니다. */
            Uri selectedImageUri = null;

            if (data.getData() != null) {
                /* Uri를 담아줍니다.*/
                selectedImageUri = data.getData();
            }

            /* 수정 요청을 위한 iamgeUri를 가져옵니다.*/
            if(isModify){
                if (currentlySelectedImageView != null && selectedImageUri != null) {
                    currentlySelectedImageView.setImageURI(selectedImageUri);
                    currentlySelectedImageView.setBackground(null);
                    int tag = Integer.parseInt((String) currentlySelectedImageView.getTag());

                    if (tag < selectedImageUris.size()) {
                        imageUriListForModify.set(tag, selectedImageUri);
                        Log.d("imageUri for modify 1 ", "imageUri for modify 1 : " + selectedImageUri);
                    } else {
                        imageUriListForModify.add(selectedImageUri);
                        Log.d("imageUri for modify 2 ", "imageUri for modify 2 : " + selectedImageUri);
                    }
                }
            } else {
                /* 이미지뷰가 null이 아니고, uri도 null이 아니라면 Uri를 이미지뷰에 그려줍니다. */
                if (currentlySelectedImageView != null && selectedImageUri != null) {
                    currentlySelectedImageView.setImageURI(selectedImageUri);
                    currentlySelectedImageView.setBackground(null);

                    /* 이미지뷰의 태그가 0-4까지, 해당 태그 위치의 값과 동일한 리스트상의 인덱스에 Uri를 넣어줍니다. */
                    int tag = Integer.parseInt((String) currentlySelectedImageView.getTag());
                    if (tag < selectedImageUris.size()) {
                        selectedImageUris.set(tag, selectedImageUri.toString());
                        imageUrilist.set(tag, selectedImageUri);
                    } else {
                        selectedImageUris.add(selectedImageUri.toString());
                        imageUrilist.add(selectedImageUri);
                    }
                }
            }
        }
    }

    private void goBack() {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
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