package com.devinsight.vegiedo.view.mypage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
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
import com.devinsight.vegiedo.service.api.ReviewApiService;
import com.devinsight.vegiedo.service.api.StoreApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.view.community.RealPathUtil;
import com.devinsight.vegiedo.view.community.WritingFragment;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
            "식물성 베이커리", "완전 비건", "대체육", "락토", "락토 오보", "페스코테리언", "폴로", "키토식단", "글루텐프리"
    };

    //Geocoding 사용
    private OkHttpClient client = new OkHttpClient();
    private String client_id = "g3rcpfm3dn";
    private String client_secret = "V7Kfjw9sRIutvedYLPpXbCRpOo9ysiFgnSWuhBin";
    private float longitude;
    private float latitude;
    Uri selectedImageUri;
    ActivityViewModel viewModel;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_store);
        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");  // 토큰 값을 검색
        Log.d("가게등록 넘어온 토큰", token);


        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);

        toggleButtons = new ToggleButton[] {
                findViewById(R.id.mypage_vegan_bakery),
                findViewById(R.id.mypage_all_vegan),
                findViewById(R.id.mypage_fake_meat),
                findViewById(R.id.mypage_lacto),
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
        mEtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 텍스트가 변경되기 전에 호출됩니다.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 텍스트가 변경되는 동안 호출됩니다.
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 텍스트가 변경된 후에 위도, 경도를 받아옴.
                fetchLatLngFromAddress(mEtAddress.getText().toString().trim());
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

                Log.d("태그 리스트", selectedDietList.toString()+", "+selectedDietList.size());
                if(storeName.isEmpty()) {
                    // 상호명이 비어있는 경우에 대한 처리 (예: Toast 메시지 표시)
                    showDialog(DialogType.ALLCONTENT);
                    Toast.makeText(RegisterNewStoreActivity.this, "상호명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(address.isEmpty()){
                    // 주소가 비어있는 경우에 대한 처리 (예: Toast 메시지 표시)
                    showDialog(DialogType.ALLCONTENT);
                    Toast.makeText(RegisterNewStoreActivity.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(selectedDietList.size()==0){
                    showDialog(DialogType.TAG);
                    Toast.makeText(RegisterNewStoreActivity.this, "태그를 1개이상 선택해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    showDialog(DialogType.COMPLETE);
                    Log.d("가게등록", "상호명:"+storeName+", 주소:"+address+", uri:"+selectedImageUri+", 태그:"+selectedDietList+", 위도:"+latitude+", 경도:"+longitude);
                    registerStoreOnServer(storeName,address, String.valueOf(selectedImageUri),selectedDietList,latitude,longitude);
                }
            }
        });

    }

    private void registerStoreOnServer(String storeName, String address, String imageUri, List<String> tags, float latitude, float longitude) {
        Log.d("가게등록", "registerStoreOnServer로 들어오는 거임?");
        Log.d("가게등록", "상호명:"+storeName+", 주소:"+address+", uri:"+imageUri+", 태그:"+tags+", 위도:"+latitude+", 경도:"+longitude);
        StoreApiService storeApiService = RetrofitClient.getStoreApiService();
        RequestBody storeNameBody = RequestBody.create(storeName, MediaType.parse("text/plain"));
        RequestBody addressBody = RequestBody.create(address, MediaType.parse("text/plain"));

        // 태그 리스트를 하나의 문자열로 연결
        String tagsString = TextUtils.join(", ", tags);
        RequestBody tagsBody = RequestBody.create(tagsString, MediaType.parse("text/plain"));

        RequestBody latitudeBody = RequestBody.create(String.valueOf(latitude), MediaType.parse("text/plain"));
        RequestBody longitudeBody = RequestBody.create(String.valueOf(longitude), MediaType.parse("text/plain"));

        // 이미지 처리
        Uri uri = Uri.parse(imageUri);
        String filePath = getFilePath(this, uri);
        MultipartBody.Part imagePart = null;
        if (filePath != null) {
            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
            imagePart = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
        }


        Log.d("가게등록", "상호명:"+storeNameBody+", 주소:"+addressBody+", uri:"+imagePart+", 태그:"+tagsBody+", 위도:"+latitudeBody+", 경도:"+longitudeBody);
        storeApiService.createStore("Bearer " + token, storeNameBody, addressBody, imagePart, tagsBody, latitudeBody, longitudeBody)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // 요청 성공
                            Log.d("가게등록성공", "Store registration success!");
                        } else {
                            // 서버에서 에러 응답
                            Log.d("가게등록실패", "Server error: " + response.errorBody() + ", 에러코드" + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // 네트워크 에러 또는 요청 실패
                        Log.d("가게등록에러", "Request failure: " + t.getMessage());
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
                        longitude = Float.parseFloat(x);
                        latitude = Float.parseFloat(y);

                        Log.d("가게등록", "경도: " + x + ", 위도: " + y);
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

    private void showDialog(DialogType type) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.setContentView(R.layout.dialog_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        closeIcon.setOnClickListener(v -> {
            if(type == DialogType.COMPLETE) {
                finish();
            }
            dialog.dismiss();
        });
        dialog.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Log.d("가게 등록 이미지uri", String.valueOf(selectedImageUri));
            storeImage.setImageURI(selectedImageUri);
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

}