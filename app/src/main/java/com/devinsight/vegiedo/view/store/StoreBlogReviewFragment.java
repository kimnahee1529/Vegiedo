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

        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        recyclerView = view.findViewById(R.id.store_review_blog_recycler_view);
        setupRecyclerView();
        callReviewAPI();
    }

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
        viewModel.setStoreId(1L);
    }
}