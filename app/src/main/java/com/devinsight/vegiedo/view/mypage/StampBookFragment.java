package com.devinsight.vegiedo.view.mypage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.StampBookInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.StoreInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.StoreStampDetailDTO;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class StampBookFragment extends Fragment {
    private ImageView backwardBtn;
    private RecyclerView recyclerView;
    private StampBookAdapter stampBookAdapter;
    private List<StampBookItem> stampBookItemList;
    ActivityViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stamp_book, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        backwardBtn = view.findViewById(R.id.backward_btn);
        callStampBookAPI();

        stampBookItemList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.stampBook_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stampBookAdapter = new StampBookAdapter(stampBookItemList);
        recyclerView.setAdapter(stampBookAdapter);

        backwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        stampBookAdapter.setOnXCircleClickListener(position -> {
            // 가져온 storeId를 함수에 전달
            StampBookItem clickedItem = stampBookItemList.get(position);
            Long clickedStoreId = clickedItem.getStoreId();

            // 클릭한 아이템의 storeId 가져오기
            Toast.makeText(getContext(), "storeId가 "+clickedStoreId+"인 스탬프 취소", Toast.LENGTH_SHORT).show();

            viewModel.StoreInactiveStampData(clickedStoreId);
            callStampBookAPI();
        });

        return view;
    }

    public interface OnXCircleClickListener {
        void onXCircleClick(int position);
    }

    private void callStampBookAPI() {
        viewModel.getStampBookDataLiveData().observe(getViewLifecycleOwner(), new Observer<StampBookInquiryResponseDTO>() {
            @Override
            public void onChanged(StampBookInquiryResponseDTO responseDTO) {
                Log.d("stampAPI", "뷰모델에서 넘어옴");
                List<StampBookItem> newStampBookList = new ArrayList<>();

                for (StoreStampDetailDTO storeDetail : responseDTO.getStoreStampDetailDtos()) {
                    Long storeId = storeDetail.getStoreId();
                    String storeName = storeDetail.getStoreName();
                    String address = storeDetail.getAddress();
                    int stars = storeDetail.getStars();
                    int reviewCount = storeDetail.getReviewCount();
                    String images = storeDetail.getImages(); // 이 경우 이미지 URL을 가지고 있으므로, 이미지 로딩을 위한 추가 작업이 필요합니다. (예: Glide 라이브러리 사용)

                    // StampBookItem 리스트에 새로운 아이템 추가
                    newStampBookList.add(new StampBookItem(storeId, storeName, stars, address, reviewCount, images)); // 가정: images는 int 대신 String 유형이어야 하며, 이미지 로딩 라이브러리를 사용하여 로드해야 합니다.

                    Log.d("stampAPI", "가게 이름: " + storeName);
                    Log.d("stampAPI", "주소: " + address);
                    Log.d("stampAPI", "별점: " + stars);
                    Log.d("stampAPI", "리뷰 수: " + reviewCount);
                    Log.d("stampAPI", "이미지 URL: " + images);
                }

                // 리사이클러뷰 어댑터에 새로운 데이터 전달
                stampBookAdapter.updateData(newStampBookList); // 'updateData' 메서드는 StampBookAdapter 내에 정의되어야 합니다.
            }
        });
        viewModel.MyPageStampBookData();
    }

}
