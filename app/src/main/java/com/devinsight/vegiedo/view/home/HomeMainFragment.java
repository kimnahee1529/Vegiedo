package com.devinsight.vegiedo.view.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.home.HomeReviewData;

import java.util.ArrayList;

public class HomeMainFragment extends Fragment implements HomeReviewAdapter.reviewItemListner {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    //  뷰페이저
    private ViewPager2 viewPager;
    private HomeBannerAdapter bannerAdapter;
    //  리사이클러뷰
    private RecyclerView recyclerView;
    private HomeReviewAdapter reviewAdapter;
    private ArrayList<HomeReviewData> reviewList;

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
        bannerAdapter = new HomeBannerAdapter(this);
        viewPager.setAdapter(bannerAdapter);

//      리뷰 항목
        recyclerView = view.findViewById(R.id.recycler_review_home);
        reviewList = new ArrayList<>();

        reviewList.add(new HomeReviewData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));
        reviewList.add(new HomeReviewData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));
        reviewList.add(new HomeReviewData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));
        reviewList.add(new HomeReviewData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));
        reviewList.add(new HomeReviewData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));
        reviewList.add(new HomeReviewData(R.drawable.ic_launcher_background, "Little Forest", R.string.tag_fruittarian, R.string.tag_vegan, R.string.tag_lacto));


        reviewAdapter = new HomeReviewAdapter(getContext(), reviewList, this);
        recyclerView.setAdapter(reviewAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }


    @Override
    public void onItemClick(HomeReviewData item) {
        Toast.makeText(getContext(), item.getStoreName() + " is clicked ", Toast.LENGTH_SHORT).show();
    }

}