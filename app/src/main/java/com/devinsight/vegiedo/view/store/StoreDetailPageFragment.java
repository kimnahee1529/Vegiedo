package com.devinsight.vegiedo.view.store;

import static com.devinsight.vegiedo.utill.RetrofitClient.getStoreApiService;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import com.devinsight.vegiedo.data.ui.login.NickNameStatus;
import com.devinsight.vegiedo.service.api.StoreApiService;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreDetailPageFragment extends Fragment {
    TextView blogReviewText;
    TextView reviewText;
    ImageView storeDetail_store_image;
    TextView storeDetail_store_name;
    TextView storeDetail_store_address;
    TextView storeDetail_store_reviewers;
    ImageView sheep_circle;
    ImageView img_sheep;
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
    Button StoreDetail_closure_report_btn;
    double Longitude;
    double Latitude;
    ActivityViewModel viewModel;
    private Long storeId;


    public StoreDetailPageFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_detail_page_seemore, container, false);

        initializeComponents(view);
        loadFragment(new StoreReviewFragment());

        //TODO 이 전페이지에서 받아온 storeId를 넣어줘야 함
//        storeId = viewModel.getStoreId().getValue();
//        callStoreAPI(viewModel.getStoreId().getValue());
        storeId = 1L;
        Log.d("LOGAPI storeId갖고 오는 거 맞나?", String.valueOf(storeId));
        callStoreAPI(storeId);

        // stamp 상태를 받아오고 넘겨주기 위한 LiveData Observer 설정
        viewModel.getStoreStampLiveData().observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged(Object o) {
                Log.d("stamp api", "뷰모델에서 넘겨준 code : "+o.toString());
                //뷰모델에서 받아온 스탬프 api 호출 상태 코드가 200일 때
                if("200".equals(o.toString())){
                    if(isClickedStamp) {
                        //스탬프가 이미 눌려져있을 때 취소 api 호출
                        Log.d("stampinactive api", "stamp api 호출 성공");
                        whiteStampBackground.setVisibility(View.INVISIBLE);
                        whiteStamp.setVisibility(View.INVISIBLE);
                        greenStampBackground.setVisibility(View.VISIBLE);
                        greenStamp.setVisibility(View.VISIBLE);
                    } else {
                        //스탬프가 안 눌려져있을 때 활성화 api 호출
                        Log.d("stampactive api", "stamp api 호출 성공");
                        greenStampBackground.setVisibility(View.INVISIBLE);
                        greenStamp.setVisibility(View.INVISIBLE);
                        whiteStampBackground.setVisibility(View.VISIBLE);
                        whiteStamp.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        // 찜버튼 상태를 받아오고 넘겨주기 위한 LiveData Observer 설정
        viewModel.getStoreLikeLiveData().observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged(Object o) {
                Log.d("like api", "뷰모델에서 넘겨준 code : "+o.toString());
                //뷰모델에서 받아온 찜버튼 api 호출 상태 코드가 200일 때
                if("200".equals(o.toString())){
                    if(isClickedLike) {
                        //찜버튼이 이미 눌려져있을 때 취소 api 호출
                        Log.d("likeinactive api", "찜 api 호출 성공");
                        storeDetail_default_heart.setVisibility(View.VISIBLE);
                        storeDetail_selected_heart.setVisibility(View.INVISIBLE);
                    } else {
                        //찜버튼이 안 눌려져있을 때 활성화 api 호출
                        Log.d("likeactive api", "찜 api 호출 성공");
                        storeDetail_default_heart.setVisibility(View.INVISIBLE);
                        storeDetail_selected_heart.setVisibility(View.VISIBLE);

                    }
                    isClickedLike = !isClickedLike;
                }
            }
        });

        // 리뷰 가게 신고를 위한 LiveData Observer 설정
//        viewModel.StoreReportData().observe(getViewLifecycleOwner(), new Observer() {
//            @Override
//            public void onChanged(Object o) {
//                Log.d("like api", "뷰모델에서 넘겨준 code : "+o.toString());
//                //뷰모델에서 받아온 찜버튼 api 호출 상태 코드가 200일 때
//                if("200".equals(o.toString())){
//                    if(isClickedLike) {
//                        //찜버튼이 이미 눌려져있을 때 취소 api 호출
//                        Log.d("likeinactive api", "찜 api 호출 성공");
//                        storeDetail_default_heart.setVisibility(View.VISIBLE);
//                        storeDetail_selected_heart.setVisibility(View.INVISIBLE);
//                    } else {
//                        //찜버튼이 안 눌려져있을 때 활성화 api 호출
//                        Log.d("likeactive api", "찜 api 호출 성공");
//                        storeDetail_default_heart.setVisibility(View.INVISIBLE);
//                        storeDetail_selected_heart.setVisibility(View.VISIBLE);
//
//                    }
//                    isClickedLike = !isClickedLike;
//                }
//            }
//        });


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
        sheep_circle = view.findViewById(R.id.sheep_circle);
        img_sheep = view.findViewById(R.id.img_sheep);
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
        StoreDetail_closure_report_btn = view.findViewById(R.id.StoreDetail_closure_report_btn);
        reviewText.setTypeface(null, Typeface.BOLD); // 글자를 bold로 설정
        reviewText.setTextColor(getResources().getColor(android.R.color.black)); // 글자를 검정색으로 설정
        stampBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStampBtnClicked();
            }
        });

        //지도 버튼을 눌렀을 때 네이버 지도 deep-link로 이동
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
                WritingReviewFragment fragment = WritingReviewFragment.newInstance(
                        storeId,
                        5
                );

                FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });

        //폐점가게 신고 버튼 클릭 리스너
        StoreDetail_closure_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "폐점가게신고", Toast.LENGTH_SHORT).show();
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.accept_reporting_dialog, null);
                ImageView closeIcon = dialogView.findViewById(R.id.green_x_circle);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.setContentView(R.layout.dialog_custom);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                closeIcon.setOnClickListener(v -> dialog.dismiss());
                dialog.show();
            }
        });
    }

    private void loadFragment(Fragment fragment) {

        if (fragment instanceof StoreBlogReviewFragment) {
            sheep_circle.setVisibility(View.GONE);
            img_sheep.setVisibility(View.GONE);
        } else {
            sheep_circle.setVisibility(View.VISIBLE);
            img_sheep.setVisibility(View.VISIBLE);
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //가게 조회 API-맨 처음 상세페이지로 들어왔을 때 보여줌
    private void callStoreAPI(Long storeId){
        viewModel.getStoreDataLiveData().observe(getViewLifecycleOwner(), new Observer<StoreInquiryResponseDTO>() {
            @Override
            public void onChanged(StoreInquiryResponseDTO data) {

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
                Log.d("api 처음 가게 데이터가 불러졌을 때 상태", "isClickedStamp : "+isClickedStamp+"isClickedLike"+isClickedLike);

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
    private void onStampBtnClicked() {
        Toast.makeText(getActivity(), "스탬프 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
        if(isClickedStamp){
            callStoreStampAPI(storeId, isClickedStamp);
            isClickedStamp = false;
        } else {
            callStoreStampAPI(storeId, isClickedStamp);
            isClickedStamp = true;
        }
    }

    private void callStoreStampAPI(Long storeId, boolean isClickedStamp){

        if(isClickedStamp){
            Log.d("stamp api", "inactive 시키는 api");
            viewModel.StoreInactiveStampData(storeId);
        } else {
            Log.d("stamp api", "active 시키는 api");
            viewModel.StoreActiveStampData(storeId);
        }
    }

    private void onLikeBtnClicked() {
        if(isClickedLike){
            Toast.makeText(getActivity(), "찜 버튼 취소시키기", Toast.LENGTH_SHORT).show();
            callStoreLikeAPI(storeId, isClickedLike);
//            isClickedLike = false;
        } else {
            Toast.makeText(getActivity(), "찜 버튼 활성화시키기", Toast.LENGTH_SHORT).show();
            callStoreLikeAPI(storeId, isClickedLike);
//            isClickedLike = true;
        }
    }

    private void callStoreLikeAPI(Long storeId, boolean isClickedLike){

        if(isClickedLike){
            Log.d("like api", "inactive 시키는 api");
            viewModel.StoreInactiveLikeData(storeId);
        } else {
            Log.d("like api", "active 시키는 api");
            viewModel.StoreActiveLikeData(storeId);
        }
    }


}

