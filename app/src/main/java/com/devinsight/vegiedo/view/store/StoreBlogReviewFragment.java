package com.devinsight.vegiedo.view.store;

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
import android.widget.Button;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.ReviewListInquiryResponseDTO;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class StoreBlogReviewFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<UserReviewItem> userReviewItems;
//    private static final int ITEMS_COUNT = 20;
    private UserReviewItemAdapter adapter;
    ActivityViewModel viewModel;

    private static final String ARG_STORE_ID = "storeId";
    private Long mStoreId;
    Button blog_review_more_btn;
    public static StoreBlogReviewFragment newInstance(Long storeId) {
        Log.d("리뷰", "블로그 리뷰화면에 들어옴");
        StoreBlogReviewFragment fragment = new StoreBlogReviewFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_STORE_ID, storeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStoreId = getArguments().getLong(ARG_STORE_ID);
            // 이제 mStoreId 변수에 storeId 값이 저장되어 있습니다.
            // 필요에 따라 이 변수를 사용하면 됩니다.
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        return inflater.inflate(R.layout.fragment_store_blog_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.store_review_blog_recycler_view);
        setupRecyclerView();

        blog_review_more_btn = view.findViewById(R.id.BlogReview_moreButton);

//        if(adapter.getItemCount() < 2){
//            blog_review_more_btn.setVisibility(View.GONE);  // "더보기" 버튼 숨기기
//            Log.d("blog더보기", "getItemCount<2");
//        }

        //어댑터에서의 더보기 버튼 리스너
        adapter.setMoreItemsListener(new UserReviewItemAdapter.MoreItemsListener() {
            @Override
            public void onHideMoreButton() {
                blog_review_more_btn.setVisibility(View.GONE);  // "더보기" 버튼 숨기기
                Log.d("blog더보기", "onHideMoreButton");
            }
        });

        blog_review_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.showMoreItems();
            }
        });

        callBlogReviewAPI();
    }

    private void setupRecyclerView() {
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new UserReviewItemAdapter(userReviewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void callBlogReviewAPI() {
        Log.d("블로그리뷰", "블로그 리뷰 api 호출");
        viewModel.getBlogReviewLiveData().observe(getViewLifecycleOwner(), new Observer<ReviewListInquiryResponseDTO>() {
            @Override
            public void onChanged(ReviewListInquiryResponseDTO data) {
                if (data != null) {
                    Log.d("블로그리뷰", "블로그 리뷰 api "+data);
                    List<UserReviewItem> updatedItems = new ArrayList<>();
                    int reviewsSize = data.getReviews().size();
                    Log.d("블로그리뷰 size", String.valueOf(reviewsSize));

                    for (int i = 0; i < reviewsSize; i++) {
                        String userName = data.getReviews().get(i).getUserName();
                        String content = data.getReviews().get(i).getContent();
                        List<String> images = data.getReviews().get(i).getImages();
                        ArrayList<String> userReviewImageUrlList = new ArrayList<>(images);
                        Long reviewId = data.getReviews().get(i).getReviewId();

                        UserReviewItem item = new UserReviewItem(reviewId, STORE_DETAIL_BLOG_REVIEW_PAGE, userName, content, userReviewImageUrlList);
                        updatedItems.add(item);
                    }

                    //98-102까지가 블로그리뷰에 애드몹 추가 코드
                    for(int i = 0; i < reviewsSize / 3; i++){
                        int index = i * 4 + 3;
                        Log.d("블로그리뷰", String.valueOf(index));
                        updatedItems.add(index, new UserReviewItem(UserReviewItem.ItemType.AD_BANNER));
                    }

                    adapter.setReviewItems(updatedItems);  // 어댑터에 데이터 전달
                }
            }

        });
        // 데이터 로드
        viewModel.ReviewInquiryData(mStoreId, 100, 0, true);
        viewModel.setStoreIdLiveData(mStoreId);
    }
}