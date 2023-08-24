package com.devinsight.vegiedo.view.mypage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.devinsight.vegiedo.R;

public class RegisterNewStoreActivity extends AppCompatActivity {

    private EditText mEtAddress;

    private static final int REQUEST_IMAGE_PICK = 101;
    private ImageView storeImage;
    private ImageView plusBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_store);

        //주소 입력
        mEtAddress = findViewById(R.id.address_edit_text);
        mEtAddress.setFocusable(false);
        mEtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주소 검색 웹뷰 화면으로 이동
                Intent intent = new Intent(RegisterNewStoreActivity.this, SearchActivity.class);
                getSearchResult.launch(intent);

            }
        });

        // 사진 추가
        storeImage = findViewById(R.id.storeImageView);
        plusBtn = findViewById(R.id.store_image_plus_btn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePickerUtil.selectImageFromGallery(RegisterNewStoreActivity.this, REQUEST_IMAGE_PICK);
            }
        });

    }

    // 화면으로부터 결과값을 받아오는 로직을 만드는 것
    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Search Activity 로부터의 결과 값이 이곳으로 전달된다. (setResult에 의해)
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        String data = result.getData().getStringExtra("data");
                        mEtAddress.setText(data);
                    }
                }
            }
    );

//    private void selectImageFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, REQUEST_IMAGE_PICK);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            storeImage.setImageURI(selectedImageUri);
        }
    }
}