package com.devinsight.vegiedo.view.store;

import android.app.AlertDialog;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.PostRegisterRequestDTO;
import com.devinsight.vegiedo.data.request.ReviewModifyrRequestDTO;
import com.devinsight.vegiedo.data.request.ReviewRegisterRequestDTO;

import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class WritingReviewFragment extends Fragment {
    private static final String ARG_STORE_ID = "storeId";
    private static final String ARG_REVIEW_ID = "reviewId";
    private static final String ARG_CONTENT = "content";
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
        imageView2 = view.findViewById(R.id.StoreDetail_image2);
        imageView3 = view.findViewById(R.id.StoreDetail_image3);
        imageView4 = view.findViewById(R.id.StoreDetail_image4);
        imageView5 = view.findViewById(R.id.StoreDetail_image5);
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

            if (TextUtils.isEmpty(contentText)) {
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

            goBack();

        });

    }

    private void showDialog() {
        int layoutId = R.layout.request_input_content_dialog;

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(layoutId, null);
        ImageView closeIcon = dialogView.findViewById(R.id.green_x_circle);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        closeIcon.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void updateReviewOnServer(Long storeId, Long reviewId, String content, float rating, List<String> imageUris) {// TODO: 서버에 리뷰 업데이트 요청 보내는 로직을 구현하세요.
        Log.d("LOGAPIupdate!", ""+storeId+", "+reviewId+", "+content+", "+rating+", "+imageUris);

        ReviewModifyrRequestDTO requestDTO = new ReviewModifyrRequestDTO();
        requestDTO.setContent(content);
        requestDTO.setStars((int) rating); // float 값을 Integer로 변환
        requestDTO.setImages(imageUris);
        viewModel.ReviewModifyData(storeId, reviewId, requestDTO);
    }

    private void writeReviewOnServer(Long storeId, String content, float rating, List<String> imageUris) {
        Log.d("LOGAPIupdate!", ""+storeId+", "+reviewId+", "+content+", "+rating+", "+imageUris);

        ReviewRegisterRequestDTO requestDTO = new ReviewRegisterRequestDTO();
        requestDTO.setContent(content);
        requestDTO.setStars((int) rating); // float 값을 Integer로 변환
        requestDTO.setImages(imageUris);
        viewModel.ReviewRegisterData(storeId, requestDTO);
    }



}
