package com.devinsight.vegiedo.view.store;

import static com.devinsight.vegiedo.view.store.UserReviewItem.ItemType.AD_BANNER;
import static com.devinsight.vegiedo.view.store.UserReviewItem.ItemType.REVIEW_RC;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.ReviewListInquiryResponseDTO;
import com.devinsight.vegiedo.view.community.WritingFragment;
import com.devinsight.vegiedo.view.search.ActivityViewModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.kakao.sdk.user.model.User;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StoreReviewFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<UserReviewItem> userReviewItems;
    private UserReviewItemAdapter adapter;
    ActivityViewModel viewModel;
    private static final String ARG_STORE_ID = "storeId";
    private Long mStoreId;
//    public boolean canWriteReview = true;

    public static StoreReviewFragment newInstance(Long storeId) {
        Log.d("리뷰", "리뷰화면에 들어옴");
        StoreReviewFragment fragment = new StoreReviewFragment();
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
        return inflater.inflate(R.layout.fragment_store_review, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ViewModel 초기화
//        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        recyclerView = view.findViewById(R.id.store_review_recycler_view);
        setupRecyclerView();

        Button review_more_btn = view.findViewById(R.id.Review_moreButton);
        TextView storeReview_modify_text = view.findViewById(R.id.storeReview_modify_text);
        TextView storeReview_delete_text = view.findViewById(R.id.storeReview_delete_text);
        ImageView storeReview_report_btn = view.findViewById(R.id.btn_comment_report);


        //TODO 전체 리스트 사이즈 알아내서 이 부분 수정해야 할 듯,
//        if(adapter.getItemCount() < 4){
//            review_more_btn.setVisibility(View.GONE);  // "더보기" 버튼 숨기기
//            Log.d("review더보기", "getItemCount<2");
//        }

        adapter.setMoreItemsListener(new UserReviewItemAdapter.MoreItemsListener() {
            @Override
            public void onHideMoreButton() {
                review_more_btn.setVisibility(View.GONE);  // "더보기" 버튼 숨기기
                Log.d("review더보기", "onHideMoreButton");
            }
        });

        review_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.showMoreItems();
            }
        });

        callReviewAPI();
    }

    private void setupRecyclerView() {
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new UserReviewItemAdapter(userReviewItems, viewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    private void callReviewAPI() {
        Log.d("리뷰", "리뷰 api 호출");
        viewModel.getReviewLiveData().observe(getViewLifecycleOwner(), new Observer<ReviewListInquiryResponseDTO>() {
            @Override
            public void onChanged(ReviewListInquiryResponseDTO data) {
                if (data != null) {
                    Log.d("리뷰", "리뷰 api "+data.getReviews());
                    List<UserReviewItem> updatedItems = new ArrayList<>();
                    int reviewsSize = data.getReviews().size();
                    Log.d("리뷰 개수", String.valueOf(reviewsSize));

                    for (int i = 0; i < reviewsSize; i++) {
//                        Log.d("LOGAPIreviews", String.valueOf(data.getReviews().get(i)));
                        String userName = data.getReviews().get(i).getUserName();
                        Integer star = data.getReviews().get(i).getStar();
                        String content = data.getReviews().get(i).getContent();
                        List<String> images = data.getReviews().get(i).getImages();
                        ArrayList<String> userReviewImageUrlList = new ArrayList<>(images);
                        Long reviewId = data.getReviews().get(i).getReviewId();
                        boolean isMine = data.getReviews().get(i).isMine();
                        //만약 내가 쓴 리뷰가 있을 경우
                        if(isMine){
                            Log.d("내가 쓴 리뷰가 있음 reviewF", String.valueOf(isMine));
                            viewModel.setCanWriteReview(false);
                            Log.d("리뷰 작성 가능한지? reviewF", String.valueOf(viewModel.getCanWriteReview().getValue()));
                        }
//                        Log.d("리뷰 확인용 로그", "userName:"+userName.toString()+" star:"+star+" content:"+content+" images:"+images+"reviewId:"+reviewId + " "+isMine);

                        UserReviewItem item = new UserReviewItem(reviewId, REVIEW_RC, userName, star, content, userReviewImageUrlList, isMine);
//                        Log.d("리뷰 item", String.valueOf(item));
//                        item.setStoreId(reviewId);  // storeId 값을 설정합니다.
                        updatedItems.add(item);
                    }

                    for(int i = 0; i < reviewsSize / 3; i++){
                        int index = i * 4 + 3;
                        Log.d("리뷰", String.valueOf(index));
                        updatedItems.add(index, new UserReviewItem(UserReviewItem.ItemType.AD_BANNER));
                    }

//                    updatedItems.add(3, new UserReviewItem(UserReviewItem.ItemType.AD_BANNER));

                    Log.d("어댑터로 보내는 updatedItems", updatedItems.toString());
                    adapter.setReviewItems(updatedItems);  // 어댑터에 데이터 전달
                }
            }

        });
        // 데이터 로드
        viewModel.ReviewInquiryData(mStoreId, 100, 0, false);
        // 상세페이지를 보여주는 가게의 storeId
        viewModel.setStoreId(mStoreId);
    }

}