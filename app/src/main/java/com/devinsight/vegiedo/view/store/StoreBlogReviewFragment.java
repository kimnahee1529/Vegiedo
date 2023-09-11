package com.devinsight.vegiedo.view.store;

import static com.devinsight.vegiedo.view.store.UserReviewItem.ItemType.REVIEW_RC;
import static com.devinsight.vegiedo.view.store.UserReviewItem.ItemType.STORE_DETAIL_BLOG_REVIEW_PAGE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.ReviewListInquiryResponseDTO;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class StoreBlogReviewFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<UserReviewItem> userReviewItems;
    private static final int ITEMS_COUNT = 20;
    private UserReviewItemAdapter adapter;
    ActivityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_blog_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ViewModel 초기화
        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);

        recyclerView = view.findViewById(R.id.store_review_blog_recycler_view);

//        populateData();
        setupRecyclerView();

        callReviewAPI();
    }

    // ITEMS_COUNT(10)개 만큼의 아이템 생성
//    private void populateData() {
//        userReviewItems = new ArrayList<>();
//        for (int i = 0; i < ITEMS_COUNT; i++) {
//            userReviewItems.add(createItem(i));
//        }
//    }


//    // 리사이클러뷰에 들어갈 아이템 추가
//    private UserReviewItem createItem(int index) {
//        UserReviewItem.ItemType itemType = STORE_DETAIL_BLOG_REVIEW_PAGE;
//        String userName = "Title " + index;
//        String content = "3천원 발렛 주차가능하며, 건물 지하 주차장에 주로 차를 댑니다.출차는 신속한 이동이 요구됩니다. 실내 분위기가 많이 활기차 보이며, 일반적인 레스토랑 대비 음식 종류별로 1만원가량 비싸지만 분위기로 대신하고 있습니다.";
//        ArrayList<String> userReviewImageUrlList = new ArrayList<>();
//
//        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
//        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
//        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
//        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
//        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
//        int ratingBar = 0;
//
//        return new UserReviewItem(reviewId, itemType, userName, ratingBar, content, userReviewImageUrlList);
//    }

    private void setupRecyclerView() {
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new UserReviewItemAdapter(userReviewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void callReviewAPI() {
        viewModel.getReviewLiveData().observe(getViewLifecycleOwner(), new Observer<ReviewListInquiryResponseDTO>() {
            @Override
            public void onChanged(ReviewListInquiryResponseDTO data) {
                if (data != null) {
                    List<UserReviewItem> updatedItems = new ArrayList<>();
                    int reviewsSize = data.getReviews().size();
                    Log.d("LOGAPIreviewsSize", String.valueOf(reviewsSize));

                    for (int i = 0; i < reviewsSize; i++) {
                        String userName = data.getReviews().get(i).getUserName();
                        String content = data.getReviews().get(i).getContent();
                        List<String> images = data.getReviews().get(i).getImages();
                        ArrayList<String> userReviewImageUrlList = new ArrayList<>(images);
                        Long reviewId = data.getReviews().get(i).getReviewId();

                        UserReviewItem item = new UserReviewItem(reviewId, STORE_DETAIL_BLOG_REVIEW_PAGE, userName, content, userReviewImageUrlList);
                        updatedItems.add(item);
                    }

                    adapter.setReviewItems(updatedItems);  // 어댑터에 데이터 전달
                }
            }

        });
        // 데이터 로드
        viewModel.ReviewInquiryData(1L, 15, 0, true);
    }
}