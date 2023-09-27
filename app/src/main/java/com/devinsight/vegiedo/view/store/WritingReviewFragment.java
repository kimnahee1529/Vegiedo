package com.devinsight.vegiedo.view.store;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
//import com.devinsight.vegiedo.Manifest;
import android.Manifest;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.PostRegisterRequestDTO;
import com.devinsight.vegiedo.data.request.ReviewModifyrRequestDTO;
import com.devinsight.vegiedo.data.request.ReviewRegisterRequestDTO;

import com.devinsight.vegiedo.service.api.ReviewApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.view.PermissionUtils;
import com.devinsight.vegiedo.view.community.ImageViewData;
import com.devinsight.vegiedo.view.community.RealPathUtil;
import com.devinsight.vegiedo.view.search.ActivityViewModel;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WritingReviewFragment extends Fragment {
    private static final String ARG_STORE_ID = "storeId";
    private static final String ARG_REVIEW_ID = "reviewId";
    private static final String ARG_CONTENT = "content";
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int PERMISSION_REQUEST_CODE = 200; // 권한 요청 코드
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Long storeId; // 가게 ID 저장
    private Long reviewId; // 리뷰 ID 저장
    private float originalRatingBarValue; // 원래 별점 값 저장
    private View view;
    private ImageView backwardBtn;
    private RatingBar starRating;
    private EditText storeDetail_review_content;
    private TextView storeDetailReviewStringLength;
    private Button registerBtn;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageView currentlySelectedImageView;
    private List<String> selectedImageUris = new ArrayList<>();
    ActivityViewModel viewModel;
    private static boolean isModify;
    public WritingReviewFragment() {
        // Required empty public constructor
    }

    //수정 버튼을 눌렀을 때
    public static WritingReviewFragment newInstance(Long storeId, Long reviewId, String content, List<String> imageUrls, float ratingBar) {
        WritingReviewFragment fragment = new WritingReviewFragment();
        isModify = true;
        Bundle args = new Bundle();
        args.putLong(ARG_STORE_ID, storeId);
        args.putLong(ARG_REVIEW_ID, reviewId);
        args.putString(ARG_CONTENT, content);
        // imageUrls는 ArrayList로 변환하여 넣는다.
        args.putStringArrayList("imageUrls", new ArrayList<>(imageUrls));
        args.putFloat("ratingBar", ratingBar);
        fragment.setArguments(args);
        return fragment;
    }

    //리뷰 작성하기 버튼을 눌렀을 때
    public static WritingReviewFragment newInstance(Long storeId, float ratingBar) {
        WritingReviewFragment fragment = new WritingReviewFragment();
        isModify = false;
        Bundle args = new Bundle();
        args.putLong(ARG_STORE_ID, storeId);
        args.putFloat("ratingBar", ratingBar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_writing_review, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);


        initializeViews(view);
        showReviewValues();
        setContentTextWatcher();
        setRegisterButtonListener();
//        restoreSelectedImages();
        return view;
    }
    private void initializeViews(View view) {

        backwardBtn = view.findViewById(R.id.backwardBtn);
        backwardBtn.setOnClickListener(v -> goBack());
        storeDetail_review_content = view.findViewById(R.id.StoreDetail_review_content);
        registerBtn = view.findViewById(R.id.registerBtn);
        storeDetailReviewStringLength = view.findViewById(R.id.StoreDetail_review_string_length);
        starRating = view.findViewById(R.id.StoreDetail_ratingbar_star); // XML 레이아웃에 있는 RatingBar의 ID를 사용하십시오.

        imageView1 = view.findViewById(R.id.StoreDetail_image1);
        imageView1.setOnClickListener(v -> {
            selectImageForView((ImageView) v);
//            setLongClickListenerForImageView(mainImage2);

            checkAndRequestPermissions();

        });
        imageView2 = view.findViewById(R.id.StoreDetail_image2);
        imageView2.setOnClickListener(v -> {
            selectImageForView((ImageView) v);
//            setLongClickListenerForImageView(mainImage2);

            checkAndRequestPermissions();

        });

        imageView3 = view.findViewById(R.id.StoreDetail_image3);
        imageView3.setOnClickListener(v -> {
            selectImageForView((ImageView) v);
//            setLongClickListenerForImageView(mainImage3);

            checkAndRequestPermissions();

        });

        imageView4 = view.findViewById(R.id.StoreDetail_image4);
        imageView4.setOnClickListener(v -> {
            selectImageForView((ImageView) v);
//            setLongClickListenerForImageView(mainImage4);

            checkAndRequestPermissions();

        });

        imageView5 = view.findViewById(R.id.StoreDetail_image5);
        imageView5.setOnClickListener(v -> {
            selectImageForView((ImageView) v);
//            setLongClickListenerForImageView(mainImage5);

            checkAndRequestPermissions();

        });

        RatingBar ratingBar = view.findViewById(R.id.StoreDetail_ratingbar_star);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating < 1.0f) {
                    ratingBar.setRating(1.0f);  // 별점이 0으로 설정되려고 할 때 별점을 1로 재설정
                }
            }
        });


    }

    public void showReviewValues() {
        storeId = getArguments().getLong(ARG_STORE_ID);
        reviewId = getArguments().getLong(ARG_REVIEW_ID);
        String content = getArguments().getString(ARG_CONTENT);
        ArrayList<String> imageUrls = getArguments().getStringArrayList("imageUrls");
        float ratingBar = getArguments().getFloat("ratingBar");
        Log.d("LOGAPI아이디 확인", ""+storeId+" "+ reviewId);

        storeDetail_review_content.setText(content);
        starRating.setRating(ratingBar);
        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<ImageView> imageViews = new ArrayList<>();
            imageViews.add(imageView1);
            imageViews.add(imageView2);
            imageViews.add(imageView3);
            imageViews.add(imageView4);
            imageViews.add(imageView5);

            for (int i = 0; i < Math.min(imageUrls.size(), imageViews.size()); i++) {
                ImageView imageView = imageViews.get(i);
                Glide.with(this)
                        .load(imageUrls.get(i))
                        .into(imageView);

                // 이미지가 설정되면 백그라운드를 제거
                imageView.setBackground(null);
            }
        }

    }



    private void goBack() {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
    }

    private void setContentTextWatcher() {
        storeDetail_review_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length > 300) {
                    s.delete(300, length);
                    Toast.makeText(getContext(), "최대 300자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                }

                String lengthString = s.length() + "자/300자";
                Log.d("LOGAPI자", ""+lengthString);
                storeDetailReviewStringLength.setText(lengthString);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void setRegisterButtonListener() {
        registerBtn.setOnClickListener(v -> {
            String contentText = storeDetail_review_content.getText().toString();
            float updatedRatingBarValue = starRating.getRating();

//            if (TextUtils.isEmpty(contentText)) {
//                showDialog();
//                return;
//            }

            if(contentText.length() < 15){
                showDialog();
                return;
            }

            PostRegisterRequestDTO postRegisterRequestDTO = selectedImageUris.isEmpty() ?
                    new PostRegisterRequestDTO(contentText) :
                    new PostRegisterRequestDTO(contentText, selectedImageUris);

            Log.d("LOG 등록버튼", "제목: " + postRegisterRequestDTO.getPostTitle() +
                    " 내용: " + postRegisterRequestDTO.getContent() +
                    " 이미지: " + postRegisterRequestDTO.getImages());

            Log.d("LOGAPI수정됐나??", ""+contentText+", "+ updatedRatingBarValue);

            // 예시: 리뷰 업데이트를 위한 메서드 호출 (실제로는 서버 요청 보내는 방법에 따라 구현 필요)
            if(isModify){
                Log.d("LOGAPIisModify", "리뷰 수정");
                updateReviewOnServer(storeId, reviewId, contentText, updatedRatingBarValue, selectedImageUris);
            }else {
                Log.d("LOGAPIisModify", "리뷰 새로 작성");
                writeReviewOnServer(storeId, contentText, updatedRatingBarValue, selectedImageUris);
            }

//            goBack();
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            fragmentManager.popBackStack();

        });

    }

    private void showDialog() {
        int layoutId = R.layout.short_text_alert_dialog;

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(layoutId, null);
        ImageView closeIcon = dialogView.findViewById(R.id.green_x_circle);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_custom);
        closeIcon.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void updateReviewOnServer(Long storeId, Long reviewId, String content, float rating, List<String> imageUris) {// TODO: 서버에 리뷰 업데이트 요청 보내는 로직을 구현하세요.
        ReviewApiService reviewApiService = RetrofitClient.getReviewApiService();
        Log.d("LOGAPIupdate!", ""+storeId+", "+reviewId+", "+content+", "+rating+", "+imageUris);

//        ReviewModifyrRequestDTO requestDTO = new ReviewModifyrRequestDTO();
//        requestDTO.setContent(content);
//        requestDTO.setStars((int) rating); // float 값을 Integer로 변환
//        requestDTO.setImages(imageUris);
//        viewModel.ReviewModifyData(storeId, reviewId, requestDTO);


        RequestBody contentBody = RequestBody.create(content, MediaType.parse("text/plain"));
        RequestBody starsBody = RequestBody.create(String.valueOf((int) rating), MediaType.parse("text/plain"));

        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (String uriString : imageUris) {
            Uri uri = Uri.parse(uriString);
            String filePath = getFilePath(getContext(), uri);
            Log.d("리뷰작성이미지경로", filePath);
            if (filePath != null) {
                File file = new File(filePath);
                RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
                MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
                imageParts.add(part);
            }
        }

        String token = viewModel.getToken().getValue(); // 토큰 값을 가져오세요.
        Log.d("리뷰수정토큰", token);
        Log.d("리뷰수정이미지에 뭐 들어가냐?", imageParts.toString());
        Log.d("LOGAPIupdate!", "contentBody:" + contentBody  + ", starsBody:" + starsBody + ", imageParts:" + imageParts);

        reviewApiService.modifyReview("Bearer " + token, storeId, reviewId, contentBody, starsBody, imageParts).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 요청 성공
                    Log.d("리뷰수정성공", "Review update success!");
                } else {
                    // 서버에서 에러 응답
                    Log.d("리뷰수정실패", "Server error: " + response.errorBody()+", 에러코드"+response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 네트워크 에러 또는 요청 실패
                Log.d("리뷰수정에러", "Request failure: " + t.getMessage());
            }
        });

    }

    //리뷰 등록
    private void writeReviewOnServer(Long storeId, String content, float rating, List<String> imageUris) {
        ReviewApiService reviewApiService = RetrofitClient.getReviewApiService();
        Log.d("LOGAPIupdate!", "" + storeId  + ", " + content + ", " + rating + ", " + imageUris);

        RequestBody contentBody = RequestBody.create(content, MediaType.parse("text/plain"));
        RequestBody starsBody = RequestBody.create(String.valueOf((int) rating), MediaType.parse("text/plain"));

        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (String uriString : imageUris) {
            Uri uri = Uri.parse(uriString);
            String filePath = getFilePath(getContext(), uri);
            Log.d("리뷰작성이미지경로", filePath);
            if (filePath != null) {
                File file = new File(filePath);
                RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
                MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
                imageParts.add(part);
            }
        }

        String token = viewModel.getToken().getValue(); // 토큰 값을 가져오세요.
        Log.d("리뷰작성토큰", token);
        Log.d("리뷰작성이미지에 뭐 들어가냐?", imageParts.toString());
        Log.d("LOGAPIupdate!", "contentBody:" + contentBody  + ", starsBody:" + starsBody + ", imageParts:" + imageParts);

        reviewApiService.postReview("Bearer " + token, storeId, contentBody, starsBody, imageParts).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 요청 성공
                    Log.d("리뷰작성성공", "Review update success!");
                } else {
                    // 서버에서 에러 응답
                    Log.d("리뷰작성실패", "Server error: " + response.errorBody()+", 에러코드"+response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 네트워크 에러 또는 요청 실패
                Log.d("리뷰작성에러", "Request failure: " + t.getMessage());
            }
        });
    }

        private void selectImageForView(ImageView imageView) {

        currentlySelectedImageView = imageView;

        if (PermissionUtils.checkPermission(getActivity())) {
            // 권한이 이미 허용된 상태: 바로 관련 작업 수행
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
        } else {
            // 권한이 허용되지 않은 상태: 권한 요청
            requestGalleryPermission();
        }
    }
    private void requestGalleryPermission() {
        // 권한을 요청합니다.
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // requestCode를 체크하여 이 결과가 기대했던 요청에 대한 결과인지 확인합니다.
        if (requestCode == GALLERY_REQUEST_CODE) {

            // resultCode를 체크하여 요청이 성공적으로 완료되었는지 확인합니다.
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

                // 선택한 이미지의 URI를 가져옵니다.
                Uri selectedImageUri = data.getData();

                // URI를 사용하여 현재 선택된 ImageView에 이미지를 로드합니다.
                if (currentlySelectedImageView != null) {
                    Glide.with(this)
                            .load(selectedImageUri)
                            .into(currentlySelectedImageView);

                    // 선택한 이미지의 URI를 selectedImageUris 리스트에 추가합니다.
                    selectedImageUris.add(selectedImageUri.toString());
                }
            }
        }
    }


    /*  Uri 로 부터 파일의 실제 경로를 구하기 위한 함수 */
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

    public void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없으면 사용자에게 권한을 요청합니다.
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    // 권한 요청 결과를 처리하는 콜백
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되었을 때의 처리
            } else {
                // 권한이 거부되었을 때의 처리
            }
        }
    }


}
