package com.devinsight.vegiedo.view.mypage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.SplashActivity;
import com.devinsight.vegiedo.data.request.StoreRegisterRequestDTO;
import com.devinsight.vegiedo.view.community.WritingFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterNewStoreActivity extends AppCompatActivity {
    private enum DialogType {
        ALLCONTENT,
        COMPLETE,
        TAG
    }
    private EditText mEtAddress;

    private static final int REQUEST_IMAGE_PICK = 101;
    private ImageView storeImage;
    private ImageView plusBtn;
    private TextView btnCheckAddress;
    private ImageView backwardBtn;
//    private ToggleButton tbFruittarian, tbVegan, tbLacto, tbOvo, tbLactoOvo, tbPescatarian, tbPollo, tbKeto, tbGlutenFree;
//    private List<String> selectedDietList = new ArrayList<>();
    private ToggleButton[] toggleButtons;
    private List<String> selectedDietList = new ArrayList<>();
    private String[] dietNames = {
            "Fruittarian", "Vegan", "Lacto", "Ovo", "LactoOvo", "Pescatarian", "Pollo", "Keto", "GlutenFree"
    };

    //Geocoding 사용
    private OkHttpClient client = new OkHttpClient();
    private String client_id = "g3rcpfm3dn";
    private String client_secret = "V7Kfjw9sRIutvedYLPpXbCRpOo9ysiFgnSWuhBin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_store);

        toggleButtons = new ToggleButton[] {
                findViewById(R.id.mypage_tbFruittarian),
                findViewById(R.id.mypage_tbVegan),
                findViewById(R.id.mypage_tbLacto),
                findViewById(R.id.mypage_tbOvo),
                findViewById(R.id.mypage_tbLactoOvo),
                findViewById(R.id.mypage_tbPescatarian),
                findViewById(R.id.mypage_tbPollo),
                findViewById(R.id.mypage_tbKeto),
                findViewById(R.id.mypage_tbGlutenFree)
        };

        for (int i = 0; i < toggleButtons.length; i++) {
            int finalI = i;
            toggleButtons[i].setOnCheckedChangeListener((buttonView, isChecked) -> updateDietList(dietNames[finalI], isChecked));
        }

        //상호명 입력
        final EditText storeNameEditText = findViewById(R.id.store_name_edit_text);
        storeNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 텍스트 변경 전에 호출되는 메서드
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 텍스트가 변경될 때마다 호출되는 메서드
                if (s.length() > 20) {
                    // 텍스트 길이가 20자를 초과하면, 글자 수를 20자로 제한하고 토스트 메시지를 표시
                    storeNameEditText.setText(s.subSequence(0, 20));
                    storeNameEditText.setSelection(20);  // 커서를 텍스트 끝으로 이동
                    Toast.makeText(RegisterNewStoreActivity.this, "상호명은 20자를 초과할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 텍스트가 변경된 후에 호출되는 메서드
            }
        });


        backwardBtn = findViewById(R.id.backward_btn);
        backwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // 현재 액티비티를 종료하고 이전 액티비티로 돌아갑니다.
            }
        });

        //주소 입력
        mEtAddress = findViewById(R.id.address_edit_text);
        mEtAddress.setFocusable(false);
        mEtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEtAddress.getText().toString().trim().isEmpty()) {
                    //주소 검색 웹뷰 화면으로 이동
                    Intent intent = new Intent(RegisterNewStoreActivity.this, SearchActivity.class);
                    getSearchResult.launch(intent);
                }else {
                    // 값이 있을 경우, EditText를 수정 가능하게 함
                    mEtAddress.setFocusable(true);
                    mEtAddress.setFocusableInTouchMode(true);
                    mEtAddress.requestFocus();
                }
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

        btnCheckAddress = findViewById(R.id.StoreRegister_compelete_btn);
        btnCheckAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = mEtAddress.getText().toString().trim();
                String storeName = storeNameEditText.getText().toString().trim();

                if(!address.isEmpty()) {
                    fetchLatLngFromAddress(address);
                }
                else if(storeName.isEmpty()) {
                    // 상호명이 비어있는 경우에 대한 처리 (예: Toast 메시지 표시)
                    showDialog(DialogType.ALLCONTENT);
                    Toast.makeText(RegisterNewStoreActivity.this, "상호명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(address.isEmpty()){
                    // 주소가 비어있는 경우에 대한 처리 (예: Toast 메시지 표시)
                    showDialog(DialogType.ALLCONTENT);
                    Toast.makeText(RegisterNewStoreActivity.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    showDialog(DialogType.COMPLETE);
                    finish();
                }



//                Log.d("주소 등록", "상호명:"+storeNameEditText.getText()+", 주소:"+mEtAddress.getText());
//                String storeName = storeNameEditText.getText().toString();
//                String storeAddress = mEtAddress.getText().toString();

//                StoreRegisterRequestDTO requestDTO = new StoreRegisterRequestDTO(storeName,storeAddress,);
//                requestDTO.setStoreName(storeName);
//                requestDTO.setAddress(storeAddress);
//                requestDTO.setTags(selectedDietList);
//                requestDTO.setReportType(reportType.get());
//                requestDTO.setOpinion(opinion.get());

//                Log.d("주소 등록", requestDTO.getStoreName() + ", " + requestDTO.getAddress());
                Log.d("태그 리스트", selectedDietList.toString());
            }
        });

    }

    private void updateDietList(String diet, boolean isChecked) {
        if (isChecked) {
            selectedDietList.add(diet);
        } else {
            selectedDietList.remove(diet);
        }
    }

    //Geocoding : 주소로 위도, 경도 구하기
    private void fetchLatLngFromAddress(String address) {
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + address;

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("X-NCP-APIGW-API-KEY-ID", client_id)
                .addHeader("X-NCP-APIGW-API-KEY", client_secret)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("LOGAPI", "onRes 성공");
                    String responseData = response.body().string();
                    try {
                        // JSON 객체 생성
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONObject firstAddress = jsonObject.getJSONArray("addresses").getJSONObject(0);

                        String x = firstAddress.getString("x");
                        String y = firstAddress.getString("y");

                        Log.d("APIJSONParsing", "경도: " + x + ", 위도: " + y);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("APIError", "Error Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Handle the error
                Log.d("LOGAPI", "onFail 안");
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(RegisterNewStoreActivity.this, "위도, 경도 변환 실패", Toast.LENGTH_SHORT).show();
                });
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

//    private void showWithdrawalDialog() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);  // 수정됨
//        LayoutInflater inflater = this.getLayoutInflater();  // 수정됨
//        View dialogView = inflater.inflate(R.layout.withdrawal_dialog, null);
//        builder.setView(dialogView);
//        final AlertDialog dialog = builder.create();
//
//        dialog.setContentView(R.layout.dialog_custom);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        dialog.show();
//    }

    private void showDialog(DialogType type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView;

        if (type == DialogType.ALLCONTENT) {
            dialogView = inflater.inflate(R.layout.request_input_blank_dialog, null);
        } else if (type == DialogType.TAG) {
            dialogView = inflater.inflate(R.layout.request_push_1tag_dialog, null);
        } else {
            dialogView = inflater.inflate(R.layout.complete_new_store_registration_dialog, null);
        }

        ImageView closeIcon = dialogView.findViewById(R.id.green_x_circle);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.setContentView(R.layout.dialog_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        closeIcon.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            storeImage.setImageURI(selectedImageUri);
        }
    }
}