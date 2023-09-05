package com.devinsight.vegiedo.view.store;

import static com.devinsight.vegiedo.utill.RetrofitClient.getStoreApiService;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.StoreInquiryResponseDTO;
import com.devinsight.vegiedo.service.api.StoreApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreDetailPageFragment extends Fragment {

    StoreApiService storeApiService = getStoreApiService();
    private static final int ITEMS_COUNT = 10;

    private RecyclerView recyclerView;
    private UserReviewItemAdapter adapter;
    private List<UserReviewItem> userReviewItems;
    TextView storeDetail_store_name;
    TextView storeDetail_store_address;
    RatingBar ratingBar;
    RelativeLayout stampBtn;
    ImageView greenStamp;
    ImageView greenStampBackground;
    ImageView whiteStamp;
    ImageView whiteStampBackground;
    Boolean isClickedStamp = true;


    public StoreDetailPageFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_detail_page, container, false);

        initializeComponents(view);
        populateData();
        setupRecyclerView();
        loadStoreData();

        return view;
    }

    private void initializeComponents(View view) {
        recyclerView = view.findViewById(R.id.store_recycle_view);
        storeDetail_store_name = view.findViewById(R.id.storeDetail_store_name);
        storeDetail_store_address = view.findViewById(R.id.storeDetail_store_address);
        ratingBar = view.findViewById(R.id.storeDetail_ratingbar);
        stampBtn = view.findViewById(R.id.storeDetail_stamp_btn);
        greenStamp = view.findViewById(R.id.storeDetail_green_stamp_btn);
        whiteStampBackground = view.findViewById(R.id.storeDetail_white_background_stamp_btn);
        whiteStamp = view.findViewById(R.id.storeDetail_white_stamp_btn);
        greenStampBackground = view.findViewById(R.id.storeDetail_green_background_stamp_btn);
        stampBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStampBtnClicked();
            }
        });
    }

    private void onStampBtnClicked() {
        Toast.makeText(getActivity(), "스탬프 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();

        if(isClickedStamp){
            isClickedStamp = false;
            // Green background with white stamp
            whiteStampBackground.setVisibility(View.INVISIBLE);
            whiteStamp.setVisibility(View.INVISIBLE);
            greenStampBackground.setVisibility(View.VISIBLE);
            greenStamp.setVisibility(View.VISIBLE); // Change this line
        }else{
            isClickedStamp = true;
            // White background with green stamp
            greenStampBackground.setVisibility(View.INVISIBLE);
            greenStamp.setVisibility(View.INVISIBLE);
            whiteStampBackground.setVisibility(View.VISIBLE);
            whiteStamp.setVisibility(View.VISIBLE); // Change this line
        }
    }

    private void populateData() {
        userReviewItems = new ArrayList<>();
        for (int i = 0; i < ITEMS_COUNT; i++) {
            userReviewItems.add(createItem(i));
        }
    }

    private UserReviewItem createItem(int index) {
        UserReviewItem.ItemType itemType = UserReviewItem.ItemType.STORE_DETAIL_PAGE;
        String title = "Title " + index;
        String description = "Description 1";
        String content = "Some content"; // 임시로 넣었습니다. 필요에 따라 수정하세요.
        ArrayList<String> userReviewImageUrlList = new ArrayList<>();
        // 이미지 URL을 ArrayList에 추가합니다. 여기서는 임의의 Drawable 리소스 ID를 사용했는데, 실제로는 이미지의 URL을 추가해야 합니다.
        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
        int ratingBar = 4; // 예를 들어 4를 줬습니다. 실제 필요한 값을 넣어주세요.

        return new UserReviewItem(itemType, title, description, content, userReviewImageUrlList, ratingBar);
    }

    private void setupRecyclerView() {
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new UserReviewItemAdapter(userReviewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadStoreData() {
        Log.d("API", "loadStoreData안");

        for (long i = 1; i <= 22; i++) {
            final long storeId = i;

            storeApiService.readStore(storeId).enqueue(new Callback<StoreInquiryResponseDTO>() {
                @Override
                public void onResponse(Call<StoreInquiryResponseDTO> call, Response<StoreInquiryResponseDTO> response) {
                    Log.d("APILOG", String.valueOf(response));
                    if (response.isSuccessful()) {
                        StoreInquiryResponseDTO storeData = response.body();
                        Log.d("APILOG", String.valueOf(storeData));
                        storeDetail_store_name.setText(response.body().getStoreName());
                        storeDetail_store_address.setText(response.body().getAddress());

//                        ratingBar.setRating(response.body().getStars());
                        Log.d("API ID:" + storeId, "이름:" + response.body().getStoreName());
                        Log.d("API ID:" + storeId, "주소:" + response.body().getAddress());
                        Log.d("API ID:" + storeId, "태그:" + response.body().getTags());
                        Log.d("API ID:" + storeId, "위도:" + response.body().getLatitude());
                        Log.d("API ID:" + storeId, "경도:" + response.body().getLongitude());
                        Log.d("API ID:" + storeId, "별점:" + response.body().getStars());
                    } else {
                        // API 응답이 오류 상태일 때
                        Log.e("APIError", "Error Code: " + response.code() + ", Message: " + response.message());
                        try {
                            Log.e("APIErrorBody", "Error Body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StoreInquiryResponseDTO> call, Throwable t) {
                    // API 호출 자체가 실패했을 때 (예: 인터넷 연결 문제, 시간 초과 등)
                    Log.e("APIFailure", "Call Failed for storeId " + storeId + ": " + t.getMessage(), t);
                }
            });
//            storeApiService.getStoreList(VEGAN, 2, )
        }
    }


}