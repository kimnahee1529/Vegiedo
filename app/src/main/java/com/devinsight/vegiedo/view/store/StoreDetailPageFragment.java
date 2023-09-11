package com.devinsight.vegiedo.view.store;

import static com.devinsight.vegiedo.utill.RetrofitClient.getStoreApiService;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.MapStoreListData;
import com.devinsight.vegiedo.data.response.ReviewListInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.StoreInquiryResponseDTO;
import com.devinsight.vegiedo.service.api.StoreApiService;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreDetailPageFragment extends Fragment {

    StoreApiService storeApiService = getStoreApiService();
    private static final int ITEMS_COUNT = 10;

//    private RecyclerView recyclerView;
    TextView blogReviewText;
    TextView reviewText;
    private UserReviewItemAdapter adapter;
    private List<UserReviewItem> userReviewItems;
    ImageView storeDetail_store_image;
    TextView storeDetail_store_name;
    TextView storeDetail_store_address;
    TextView storeDetail_store_reviewers;
    RatingBar ratingBar;
    RelativeLayout stampBtn;
    RelativeLayout mapBtn;
    RelativeLayout likeBtn;
    ImageView greenStamp;
    ImageView greenStampBackground;
    ImageView whiteStamp;
    ImageView whiteStampBackground;
    Boolean isClickedStamp;
    Boolean isClickedLike;
    ImageView storeDetail_default_heart;
    ImageView storeDetail_selected_heart;
    Button StoreDetail_review_writing_btn;
    double Longitude;
    double Latitude;
    ActivityViewModel viewModel;


    public StoreDetailPageFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_detail_page_seemore, container, false);

        initializeComponents(view);
        // 기본 프래그먼트(리뷰 화면) 로드
        loadFragment(new StoreReviewFragment());

        //TODO 이 전페이지에서 받아온 storeId를 넣어줘야 함
        Long storeId = viewModel.getStoreId().getValue();
//        callStoreAPI(viewModel.getStoreId().getValue());
        callStoreAPI(1L);

        return view;
    }

    private void initializeComponents(View view) {

        // ViewModel 초기화
        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);

        reviewText = view.findViewById(R.id.StoreDetail_page_review_text);
        blogReviewText = view.findViewById(R.id.StoreDetail_page_blog_review_text);

        storeDetail_store_image =view.findViewById(R.id.storeDetail_store_image);
        storeDetail_store_name = view.findViewById(R.id.storeDetail_store_name);
        storeDetail_store_address = view.findViewById(R.id.storeDetail_store_address);
        storeDetail_store_reviewers = view.findViewById(R.id.storeDetail_store_reviewers);
        ratingBar = view.findViewById(R.id.storeDetail_ratingbar);
        stampBtn = view.findViewById(R.id.storeDetail_stamp_btn);
        mapBtn = view.findViewById(R.id.storeDetail_map_btn);
        greenStamp = view.findViewById(R.id.storeDetail_green_stamp_btn);
        likeBtn = view.findViewById(R.id.storeDetail_like_btn);
        StoreDetail_review_writing_btn = view.findViewById(R.id.StoreDetail_review_writing_btn);
        whiteStampBackground = view.findViewById(R.id.storeDetail_white_background_stamp_btn);
        whiteStamp = view.findViewById(R.id.storeDetail_white_stamp_btn);
        greenStampBackground = view.findViewById(R.id.storeDetail_green_background_stamp_btn);
        storeDetail_default_heart = view.findViewById(R.id.StoreDetail_default_heart);
        storeDetail_selected_heart = view.findViewById(R.id.StoreDetail_selected_heart);
        reviewText.setTypeface(null, Typeface.BOLD); // 글자를 bold로 설정
        reviewText.setTextColor(getResources().getColor(android.R.color.black)); // 글자를 검정색으로 설정
        stampBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStampBtnClicked();
            }
        });

        //지도 플로팅 버튼을 눌렀을 때 네이버 지도 deep-link로 이동
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lat = Latitude;
                double lng = Longitude;
                Log.d("LOGAPI 위도 경도 is",  lng+ " " + lat);

                String uriString = String.format("nmap://navigation?dlat=%s&dlng=%s&appname=vegiedo", lat, lng);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
                startActivity(intent);

            }
        });
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLikeBtnClicked();
            }
        });
        reviewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewText.setTypeface(null, Typeface.BOLD); // 글자를 bold로 설정
                blogReviewText.setTypeface(null, Typeface.NORMAL);
                reviewText.setTextColor(getResources().getColor(android.R.color.black)); // 글자를 검정색으로 설정
                blogReviewText.setTextColor(getResources().getColor(android.R.color.darker_gray)); // 글자를 검정색으로 설정
                loadFragment(new StoreReviewFragment());
            }
        });

        blogReviewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogReviewText.setTypeface(null, Typeface.BOLD); // 글자를 bold로 설정
                reviewText.setTypeface(null, Typeface.NORMAL);
                blogReviewText.setTextColor(getResources().getColor(android.R.color.black)); // 글자를 검정색으로 설정
                reviewText.setTextColor(getResources().getColor(android.R.color.darker_gray)); // 글자를 검정색으로 설정
                loadFragment(new StoreBlogReviewFragment());
            }
        });

        StoreDetail_review_writing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WritingReviewFragment fragment = new WritingReviewFragment();
//                WritingReviewFragment fragment = WritingReviewFragment.newInstance(false, null);

                FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void onStampBtnClicked() {
        Toast.makeText(getActivity(), "스탬프 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
        Log.d("LOGAPIisClickedStamp is", String.valueOf(isClickedStamp));
        if(isClickedStamp){
            Log.d("LOGAPI스탬프는 이미 눌려져있음", String.valueOf(isClickedStamp));
            isClickedStamp = false;
            // Green background with white stamp
            greenStampBackground.setVisibility(View.INVISIBLE);
            greenStamp.setVisibility(View.INVISIBLE);
            whiteStampBackground.setVisibility(View.VISIBLE);
            whiteStamp.setVisibility(View.VISIBLE); // Change this line

        }else{
            Log.d("LOGAPI스탬프 안 눌려져있음", String.valueOf(isClickedStamp));
            isClickedStamp = true;
            // White background with green stamp
            whiteStampBackground.setVisibility(View.INVISIBLE);
            whiteStamp.setVisibility(View.INVISIBLE);
            greenStampBackground.setVisibility(View.VISIBLE);
            greenStamp.setVisibility(View.VISIBLE); // Change this line

        }
    }

    private void onLikeBtnClicked() {
        Toast.makeText(getActivity(), "찜 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
        Log.d("LOGAPIisClickedLike is", String.valueOf(isClickedStamp));
        if(isClickedLike){
            Log.d("LOGAPI찜은 이미 눌려져있음", String.valueOf(isClickedLike));
            isClickedLike = false;
            storeDetail_default_heart.setVisibility(View.VISIBLE);
            storeDetail_selected_heart.setVisibility(View.INVISIBLE);

        }else{
            Log.d("LOGAPI찜 안 눌려져있음", String.valueOf(isClickedLike));
            isClickedLike = true;
            storeDetail_selected_heart.setVisibility(View.VISIBLE);
            storeDetail_default_heart.setVisibility(View.INVISIBLE);

        }
    }

    //가게 조회 API
    private void callStoreAPI(Long storeId){
        viewModel.getStoreDataLiveData().observe(getViewLifecycleOwner(), new Observer<StoreInquiryResponseDTO>() {
            @Override
            public void onChanged(StoreInquiryResponseDTO data) {
                Log.d("LOGAPIStamp is", String.valueOf(data.isStamp()));
                Log.d("LOGAPIHeart is", String.valueOf(data.isLike()));

                // UI 업데이트
                Glide.with(getActivity())
                        .load(data.getStoreImage())
                        .into(storeDetail_store_image);
                storeDetail_store_name.setText(data.getStoreName());
                storeDetail_store_address.setText(data.getAddress());
                ratingBar.setRating(data.getStars());
                storeDetail_store_reviewers.setText(data.getReviewCount() + " reviews");
                Longitude = data.getLongitude();
                Latitude = data.getLatitude();

                // data.isStamp()가 true일 때의 동작
                isClickedStamp = data.isStamp(); // isClickedStamp를 data.isStamp()로 설정
                Log.d("LOGAPI 위도 경도 is", Longitude + " " + Latitude);

                isClickedLike = data.isLike();

                // data.isStamp()가 true일 때의 동작
                if (isClickedStamp) {  // 이 부분에서 isClickedStamp를 사용
                    whiteStampBackground.setVisibility(View.INVISIBLE);
                    whiteStamp.setVisibility(View.INVISIBLE);
                    greenStampBackground.setVisibility(View.VISIBLE);
                    greenStamp.setVisibility(View.VISIBLE);
                } else {
                    greenStampBackground.setVisibility(View.INVISIBLE);
                    greenStamp.setVisibility(View.INVISIBLE);
                    whiteStampBackground.setVisibility(View.VISIBLE);
                    whiteStamp.setVisibility(View.VISIBLE);
                }

                if (isClickedLike) {
                    storeDetail_selected_heart.setVisibility(View.VISIBLE);
                    storeDetail_default_heart.setVisibility(View.INVISIBLE);
                }else{
                    storeDetail_default_heart.setVisibility(View.VISIBLE);
                    storeDetail_selected_heart.setVisibility(View.INVISIBLE);
                }

            }
        });

        // 데이터 로드
        viewModel.StoreInquiryData(storeId);
    }

}