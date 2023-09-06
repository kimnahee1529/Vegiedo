package com.devinsight.vegiedo.view.store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devinsight.vegiedo.R;

import java.util.ArrayList;
import java.util.List;

public class StoreBlogReviewFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<UserReviewItem> userReviewItems;
    private static final int ITEMS_COUNT = 10;
    private UserReviewItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_blog_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.store_review_blog_recycler_view);

        populateData();
        setupRecyclerView();
    }

    // ITEMS_COUNT(10)개 만큼의 아이템 생성
    private void populateData() {
        userReviewItems = new ArrayList<>();
        for (int i = 0; i < ITEMS_COUNT; i++) {
            userReviewItems.add(createItem(i));
        }
    }

    // 리사이클러뷰에 들어갈 아이템 추가
    private UserReviewItem createItem(int index) {
        UserReviewItem.ItemType itemType = UserReviewItem.ItemType.STORE_DETAIL_BLOG_REVIEW_PAGE;
        String title = "Title " + index;
        String description = "Description 1";
        String content = "3천원 발렛 주차가능하며, 건물 지하 주차장에 주로 차를 댑니다.출차는 신속한 이동이 요구됩니다. 실내 분위기가 많이 활기차 보이며, 일반적인 레스토랑 대비 음식 종류별로 1만원가량 비싸지만 분위기로 대신하고 있습니다.";
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
}