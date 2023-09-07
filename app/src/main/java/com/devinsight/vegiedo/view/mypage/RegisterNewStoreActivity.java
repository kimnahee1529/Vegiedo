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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devinsight.vegiedo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterNewStoreActivity extends AppCompatActivity {

    private EditText mEtAddress;

    private static final int REQUEST_IMAGE_PICK = 101;
    private ImageView storeImage;
    private ImageView plusBtn;
    private TextView btnCheckAddress;
    private ImageView backwardBtn;

    //Geocoding 사용
    private OkHttpClient client = new OkHttpClient();
    private String client_id = "g3rcpfm3dn";
    private String client_secret = "V7Kfjw9sRIutvedYLPpXbCRpOo9ysiFgnSWuhBin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_store);

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
                if(!address.isEmpty()) {
                    fetchLatLngFromAddress(address);
                } else {
                    // 주소가 비어있는 경우에 대한 처리 (예: Toast 메시지 표시)
                    Toast.makeText(RegisterNewStoreActivity.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            storeImage.setImageURI(selectedImageUri);
        }
    }
}