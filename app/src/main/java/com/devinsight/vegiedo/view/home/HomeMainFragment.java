package com.devinsight.vegiedo.view.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.HomeReviewResponseDTO;
import com.devinsight.vegiedo.data.ui.home.HomeBannerData;
import com.devinsight.vegiedo.data.ui.home.HomeReviewUiData;
import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;
import com.devinsight.vegiedo.service.api.ReviewApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.view.login.NickNameActivity;
import com.devinsight.vegiedo.view.search.ActivityViewModel;
import com.devinsight.vegiedo.view.store.StoreDetailPageFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeMainFragment extends Fragment implements HomeReviewAdapter.reviewItemListner, HomeBannerAdapter.BannerItemListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    //  뷰페이저
    private ViewPager2 viewPager;
    private HomeBannerAdapter bannerAdapter;
    List<HomeBannerData> bannerList;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    //  리사이클러뷰
    private RecyclerView recyclerView;
    private HomeReviewAdapter reviewAdapter;
    private ArrayList<HomeReviewUiData> reviewList;

    ActivityViewModel viewModel;
    private AuthPrefRepository authPrefRepository;
    private String token;

    public HomeMainFragment() {

    }

    public static HomeMainFragment newInstance(String param1, String param2) {
        HomeMainFragment fragment = new HomeMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("main home frag","onCreateView");
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);
        authPrefRepository = new AuthPrefRepository(getContext());
        String social;
        if( authPrefRepository.getAuthToken("KAKAO") != null) {
            social = "KAKAO";
        } else {
            social = "GOOGLE";
        }
        token = authPrefRepository.getAuthToken(social);

        viewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

//      홈화면 상단 배너
        viewPager = view.findViewById(R.id.viewpager_home);

        bannerList = new ArrayList<>();
        bannerAdapter = new HomeBannerAdapter(getContext(), bannerList, this);
        viewPager.setAdapter(bannerAdapter);
        CircleIndicator circleIndicator = view.findViewById(R.id.home_indicator);

        viewModel.getHomeBanner();
        viewModel.getHomeBannerListLiveData().observe(getViewLifecycleOwner(), new Observer<List<HomeBannerData>>() {
            @Override
            public void onChanged(List<HomeBannerData> homeBannerData) {
                bannerAdapter.setBannerList(homeBannerData);
                bannerAdapter.notifyDataSetChanged();
                if (bannerList.size() > 0) {
                    circleIndicator.createDotPanel(bannerList.size(), R.drawable.indicator_dot_off, R.drawable.indicator_dot_on, 0);
                }
            }
        });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                circleIndicator.selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

            }
        });

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == bannerList.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);




//      리뷰 항목
        recyclerView = view.findViewById(R.id.recycler_review_home);
        reviewList = new ArrayList<>();
        reviewAdapter = new HomeReviewAdapter(getContext(), reviewList, this);
        recyclerView.setAdapter(reviewAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        viewModel.getHomeReview();
        viewModel.getHomeReviewListLiveData().observe(getViewLifecycleOwner(), new Observer<HomeReviewResponseDTO>() {
            @Override
            public void onChanged(HomeReviewResponseDTO homeReviewResponseDTO) {
                reviewAdapter.setReviewList(homeReviewResponseDTO.getReviews());
                reviewAdapter.notifyDataSetChanged();
            }
        });


        return view;
    }

    @Override
    public void onBannerClicked(View view, HomeBannerData homeBannerData, int position) {
        viewModel.setStoreIdLiveData(bannerList.get(position).getStoreId());

        Log.d("배너 클릭 가게 아이디","" + bannerList.get(position).getStoreId());


        StoreDetailPageFragment detailFragment = new StoreDetailPageFragment();
        // 프래그먼트 트랜잭션 시작
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, detailFragment);  // R.id.container는 당신의 FrameLayout 또는 호스트 뷰의 ID여야 합니다.
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onReviewItemClick(View view, HomeReviewUiData item, int position) {
        viewModel.setStoreIdLiveData(reviewList.get(position).getStoreId());

        Log.d("배너 클릭 가게 아이디","" + reviewList.get(position).getStoreId());


        StoreDetailPageFragment detailFragment = new StoreDetailPageFragment();
        // 프래그먼트 트랜잭션 시작
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, detailFragment);  // R.id.container는 당신의 FrameLayout 또는 호스트 뷰의 ID여야 합니다.
        transaction.addToBackStack(null);
        transaction.commit();

    }


}