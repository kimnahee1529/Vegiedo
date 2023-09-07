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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.PostRegisterRequestDTO;
import com.devinsight.vegiedo.view.community.GeneralPostFragment;
import com.devinsight.vegiedo.view.community.PopuralPostFragment;
import com.devinsight.vegiedo.view.community.WritingFragment;

import java.util.ArrayList;
import java.util.List;

public class WritingReviewFragment extends Fragment {

    private View view;
    private ImageView backwardBtn;
    private EditText storeDetail_review_content;
    private TextView storeDetailReviewStringLength;
    private Button registerBtn;
    private List<String> selectedImageUris = new ArrayList<>();

    public WritingReviewFragment() {
        // Required empty public constructor
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

        initializeViews(view);
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

            sendPostRequest(postRegisterRequestDTO);


            goBack();
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



}
