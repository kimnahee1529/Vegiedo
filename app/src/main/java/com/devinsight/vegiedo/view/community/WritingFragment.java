package com.devinsight.vegiedo.view.community;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.PostRegisterRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class WritingFragment extends Fragment {

    private enum DialogType {
        TITLE,
        CONTENT
    }

    private EditText communityWritingContentText;
    private TextView communityStringLength;
    private EditText communityWritingTitleText;
    private List<String> selectedImageUris = new ArrayList<>();
    private Button registerBtn;
    private ImageView mainImage;
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int MAX_IMAGE_COUNT = 5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_writing, container, false);

        initializeViews(rootView);
        setTitleTextWatcher();
        setContentTextWatcher();
        setRegisterButtonListener();
        restoreSelectedImages();

        return rootView;
    }

    private void initializeViews(View rootView) {
        communityWritingContentText = rootView.findViewById(R.id.community_writing_content_text);
        communityStringLength = rootView.findViewById(R.id.community_string_length);
        communityWritingTitleText = rootView.findViewById(R.id.community_writing_title_text);
        registerBtn = rootView.findViewById(R.id.community_writing_register_button);

        // Initialize mainImage
        mainImage = rootView.findViewById(R.id.main_image1);
        mainImage.setOnClickListener(v -> selectImagesFromGallery());
    }

    private void restoreSelectedImages() {
        int[] imageViews = {R.id.main_image1, R.id.main_image2, R.id.main_image3, R.id.main_image4, R.id.main_image5};
        for (int i = 0; i < selectedImageUris.size(); i++) {
            ImageView imageView = getView().findViewById(imageViews[i]);
            imageView.setImageURI(Uri.parse(selectedImageUris.get(i)));
            imageView.setBackground(null);
        }
    }

    private void setTitleTextWatcher() {
        setTextWatcher(communityWritingTitleText, 20, "최대 20자까지 입력 가능합니다.");
    }

    //제목 입력
    private void setTextWatcher(EditText editText, int maxLength, String message) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if (length > maxLength) {
                    editable.delete(maxLength, length);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }

                Log.d("LOG제목", "제목: "+editable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    //내용 입력
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


            if (TextUtils.isEmpty(communityWritingTitleText.getText().toString())) {
                showDialog(DialogType.TITLE);
            } else if (TextUtils.isEmpty(communityWritingContentText.getText().toString())) {
                showDialog(DialogType.CONTENT);
            } else {
                if(!selectedImageUris.isEmpty()){
                    PostRegisterRequestDTO postRegisterRequestDTO= new PostRegisterRequestDTO(titleText, contentText, selectedImageUris);
                    Log.d("LOG 등록버튼", "제목 : "+postRegisterRequestDTO.getPostTitle() + " 내용 : " + postRegisterRequestDTO.getContent() + " 이미지 : " + postRegisterRequestDTO.getImages());
                } else{
                    PostRegisterRequestDTO postRegisterRequestDTO= new PostRegisterRequestDTO(titleText, contentText);
                    Log.d("LOG 등록버튼", "제목 : "+postRegisterRequestDTO.getPostTitle() + " 내용 : " + postRegisterRequestDTO.getContent() + " 이미지 : " + postRegisterRequestDTO.getImages());
                }
                moveToCommunityFragment();
            }
        });
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

    private void moveToCommunityFragment() {
        // Create a new instance of CommunityFragment
        CommunityFragment communityFragment = new CommunityFragment();

        // Replace the current fragment with the new one
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, communityFragment)  // 'container' is the ID of your FrameLayout in which the fragment resides.
                .commit();
    }

    private void selectImagesFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), GALLERY_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            List<Uri> imageUris = new ArrayList<>();

            // Handle single image selection
            if (data.getData() != null) {
                Uri imageUri = data.getData();
                Log.d("LOGIMAGE", "uir:" + imageUri);
                imageUris.add(imageUri);
            }
            // Handle multiple image selection
            else if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                int itemCount = Math.min(clipData.getItemCount(), MAX_IMAGE_COUNT);
                for (int i = 0; i < itemCount; i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    imageUris.add(imageUri);
                }

                for (Uri uri : imageUris) {
                    selectedImageUris.add(uri.toString());
                }
            }

            // Set the selected images to multiple ImageViews
            if (!imageUris.isEmpty()) {
                int[] imageViews = {R.id.main_image1, R.id.main_image2, R.id.main_image3, R.id.main_image4, R.id.main_image5};
                for (int i = 0; i < imageUris.size(); i++) {
                    ImageView imageView = getView().findViewById(imageViews[i]);
                    imageView.setImageURI(imageUris.get(i));
                    imageView.setBackground(null);
                }
            }
        }
    }


}
