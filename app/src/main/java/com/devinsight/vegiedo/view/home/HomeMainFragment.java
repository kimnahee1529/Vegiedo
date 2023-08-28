package com.devinsight.vegiedo.view.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.devinsight.vegiedo.data.ui.home.HomeBannerData;
import com.devinsight.vegiedo.data.ui.home.HomeReviewUiData;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeMainFragment extends Fragment implements HomeReviewAdapter.reviewItemListner {

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

//      홈화면 상단 배너
        viewPager = view.findViewById(R.id.viewpager_home);

        bannerList = new ArrayList<>();
        bannerList.add(new HomeBannerData("https://cdn2.thecatapi.com/images/4gd.gif"));
        bannerList.add(new HomeBannerData("https://cdn2.thecatapi.com/images/OmNwBvvUm.jpg"));
        bannerList.add(new HomeBannerData("https://cdn2.thecatapi.com/images/MTUwMjU0OQ.jpg"));
        bannerList.add(new HomeBannerData("https://cdn2.thecatapi.com/images/fG-wtktoL.jpg"));

        bannerAdapter = new HomeBannerAdapter(getContext(), bannerList);
        viewPager.setAdapter(bannerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                CircleIndicator circleIndicator = view.findViewById(R.id.home_indicator);
                circleIndicator.selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

            }
        });

        CircleIndicator circleIndicator = view.findViewById(R.id.home_indicator);
        circleIndicator.createDotPanel(bannerList.size(), R.drawable.indicator_dot_off, R.drawable.indicator_dot_on, 0);



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

        reviewList.add(new HomeReviewUiData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));
        reviewList.add(new HomeReviewUiData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));
        reviewList.add(new HomeReviewUiData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));
        reviewList.add(new HomeReviewUiData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));
        reviewList.add(new HomeReviewUiData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));
        reviewList.add(new HomeReviewUiData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));


        reviewAdapter = new HomeReviewAdapter(getContext(), reviewList, this);
        recyclerView.setAdapter(reviewAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }


    @Override
    public void onItemClick(HomeReviewUiData item) {
        Toast.makeText(getContext(), item.getStoreName() + " is clicked ", Toast.LENGTH_SHORT).show();
    }

}