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

public class StoreReviewFragment extends Fragment {
    public static final int ITEMS_PER_AD = 3;
    private RecyclerView recyclerView;
    private List<UserReviewItem> userReviewItems;
    private UserReviewItemAdapter adapter;
    ActivityViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_review, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ViewModel 초기화
        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);

//        AdView mAdView = view.findViewById(R.id.ad_banner_item_view);  // 여기서 뷰를 초기화합니다.
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        recyclerView = view.findViewById(R.id.store_review_recycler_view);
        setupRecyclerView();
//        initAdMobAdsSDK();
//        addAdMobBannerAds();
//        loadBannerAds();
        callReviewAPI();
    }

    private void setupRecyclerView() {
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new UserReviewItemAdapter(userReviewItems, viewModel);
//        adapter = new TestAdMobAdapter(userReviewItems, viewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void initAdMobAdsSDK() {
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void addAdMobBannerAds() {
        for (int i = ITEMS_PER_AD; i <= userReviewItems.size(); i += ITEMS_PER_AD) {
            userReviewItems.add(i, new UserReviewItem(AD_BANNER));
        }
    }
    private void loadBannerAds()
    {
        //Load the first banner ad in the items list (subsequent ads will be loaded automatically in sequence).
        loadBannerAd(ITEMS_PER_AD);
    }

    private void loadBannerAd(final int index)
    {
        if (index >= userReviewItems.size())
        {
            return;
        }

        Object item = userReviewItems.get(index);
        if (!(item instanceof AdView))
        {
            throw new ClassCastException("Expected item at index " + index + " to be a banner ad" + " ad.");
        }

        final AdView adView = (AdView) item;

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                super.onAdLoaded();
                // The previous banner ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadBannerAd(index + ITEMS_PER_AD);
            }

//            @Override
//            public void onAdFailedToLoad(int errorCode)
//            {
//                // The previous banner ad failed to load. Call this method again to load
//                // the next ad in the items list.
//                Log.e("MainActivity", "The previous banner ad failed to load. Attempting to"
//                        + " load the next banner ad in the items list.");
//                loadBannerAd(index + ITEMS_PER_AD);
//            }
        });

        // Load the banner ad.
        adView.loadAd(new AdRequest.Builder().build());
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
                        Integer stars = data.getReviews().get(i).getStars();
                        String content = data.getReviews().get(i).getContent();
                        List<String> images = data.getReviews().get(i).getImages();
                        ArrayList<String> userReviewImageUrlList = new ArrayList<>(images);
                        Long reviewId = data.getReviews().get(i).getReviewId();
                        Log.d("LOGAPIreviewsId", String.valueOf(reviewId));

                        UserReviewItem item = new UserReviewItem(reviewId, REVIEW_RC, userName, stars, content, userReviewImageUrlList);
                        Log.d("LOGAPIitem", String.valueOf(item));
//                        item.setStoreId(reviewId);  // storeId 값을 설정합니다.
                        updatedItems.add(item);
                    }

                    for(int i = 0; i < reviewsSize / 3; i++){
                        int index = i * 4 + 3;
                        Log.d("LOGAPI", String.valueOf(index));
                        updatedItems.add(index, new UserReviewItem(UserReviewItem.ItemType.AD_BANNER));
                    }

//                    updatedItems.add(3, new UserReviewItem(UserReviewItem.ItemType.AD_BANNER));

                    Log.d("어댑터로 보내는 updatedItems", updatedItems.toString());
                    adapter.setReviewItems(updatedItems);  // 어댑터에 데이터 전달
                }
            }

        });
        // 데이터 로드
        viewModel.ReviewInquiryData(1L, 10, 0, false);
        // 상세페이지를 보여주는 가게의 storeId
        viewModel.setStoreId(1L);
    }

}