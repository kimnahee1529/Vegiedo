package com.devinsight.vegiedo.view.store;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;


public class StoreDetailPageDDFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String imgUrl;
    private String storeName;
    private String address;
    private Integer rating;
    private Integer review;


    ImageView storeImageV;
    TextView storeNameV;
    TextView storeAddressV;
    TextView storeReviewV;
    RatingBar storeRatingV;

    public StoreDetailPageDDFragment() {
        // Required empty public constructor
    }

    public static StoreDetailPageDDFragment newInstance(String param1, String param2) {
        StoreDetailPageDDFragment fragment = new StoreDetailPageDDFragment();
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
            imgUrl = getArguments().getString("storeImage");
            storeName = getArguments().getString("storeName");
            address = getArguments().getString("storeAddress");
            rating = getArguments().getInt("storeRating", 0);
            review = getArguments().getInt("storeReview", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_detail_page_dodo, container, false);


        storeNameV = view.findViewById(R.id.store_detail_name);
        storeAddressV = view.findViewById(R.id.store_address);
        storeRatingV = view.findViewById(R.id.ratingbar_star);
        storeReviewV = view.findViewById(R.id.reviews);
        storeImageV = view.findViewById(R.id.detail_store_image);

        if (imgUrl != null && storeImageV != null) {
            Glide.with(getActivity()).load(imgUrl).into(storeImageV);
        }
        storeNameV.setText(storeName);
        storeAddressV.setText(address);
        storeRatingV.setRating(rating);
        storeReviewV.setText(String.valueOf(review));

        return view;
    }
}