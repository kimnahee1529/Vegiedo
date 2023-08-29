package com.devinsight.vegiedo.view.store;

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

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.MapInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.StoreInquiryResponseDTO;
import com.devinsight.vegiedo.service.api.MapApiService;
import com.devinsight.vegiedo.service.api.StoreApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreDetailPageFragment extends Fragment {

    StoreApiService storeApiService = RetrofitClient.getStoreApiService();
    private static final int ITEMS_COUNT = 10;

    private RecyclerView recyclerView;
    private StoreDetailPageAdapter adapter;
    private List<UserReviewItem> userReviewItems;

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
    }

    private void populateData() {
        userReviewItems = new ArrayList<>();
        for (int i = 0; i < ITEMS_COUNT; i++) {
            userReviewItems.add(createItem(i));
        }
    }

    private UserReviewItem createItem(int index) {
        return new UserReviewItem(UserReviewItem.ItemType.STORE_DETAIL_PAGE, "Title " + index,
                R.drawable.full_star, R.drawable.full_star, R.drawable.full_star, R.drawable.full_star, R.drawable.empty_star,
                "Description 1", R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background);
    }

    private void setupRecyclerView() {
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new StoreDetailPageAdapter(userReviewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadStoreData() {
        Log.d("API", "loadStoreData안");

        for (long i = 1; i <= 2; i++) {
            final long storeId = i;

            storeApiService.readStore(storeId).enqueue(new Callback<StoreInquiryResponseDTO>() {
                @Override
                public void onResponse(Call<StoreInquiryResponseDTO> call, Response<StoreInquiryResponseDTO> response) {
                    if (response.isSuccessful()) {
                        Log.d("API ID:" + storeId, "주소:" + response.body().getAddress());
                        Log.d("API ID:" + storeId, "사진:" + response.body().getImages());
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
        }
    }


}