package com.devinsight.vegiedo.view.store;

import static com.devinsight.vegiedo.utill.RetrofitClient.getStoreApiService;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    RelativeLayout likeBtn;
    ImageView greenStamp;
    ImageView greenStampBackground;
    ImageView whiteStamp;
    ImageView whiteStampBackground;
    Boolean isClickedStamp;
    Boolean isClickedLike;
    ImageView storeDetail_default_heart;
    ImageView storeDetail_selected_heart;
    ActivityViewModel viewModel;


    public StoreDetailPageFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_detail_page, container, false);

        initializeComponents(view);
        // 기본 프래그먼트(리뷰 화면) 로드
        loadFragment(new StoreReviewFragment());  // 이 부분이 추가되었습니다.
        callStoreAPI(5L);

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
        greenStamp = view.findViewById(R.id.storeDetail_green_stamp_btn);
        likeBtn = view.findViewById(R.id.storeDetail_like_btn);
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

//    private void populateData() {
//        userReviewItems = new ArrayList<>();
//        for (int i = 0; i < ITEMS_COUNT; i++) {
//            userReviewItems.add(createItem(i));
//        }
//    }

//    private UserReviewItem createItem(int index) {
//        UserReviewItem.ItemType itemType = UserReviewItem.ItemType.STORE_DETAIL_REVIEW_PAGE;
//        String title = "Title " + index;
//        String description = "Description 1";
//        String content = " 3천원 발렛 주차가능하며, 건물 지하 주차장에 주로 차를 댑니다.출차는 신속한 이동이 요구됩니다. 실내 분위기가 많이 활기차 보이며, 일반적인 레스토랑 대비 음식 종류별로 1만원가량 비싸지만 분위기로 대신하고 있습니다."; // 임시로 넣었습니다. 필요에 따라 수정하세요.
//        ArrayList<String> userReviewImageUrlList = new ArrayList<>();
//        // 이미지 URL을 ArrayList에 추가합니다. 여기서는 임의의 Drawable 리소스 ID를 사용했는데, 실제로는 이미지의 URL을 추가해야 합니다.
//        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
//        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
//        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
//        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
//        userReviewImageUrlList.add(String.valueOf(R.drawable.ic_launcher_background));
//        int ratingBar = 4; // 예를 들어 4를 줬습니다. 실제 필요한 값을 넣어주세요.
//
//        return new UserReviewItem(itemType, title, description, content, userReviewImageUrlList, ratingBar);
//    }

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

                // data.isStamp()가 true일 때의 동작
                isClickedStamp = data.isStamp(); // isClickedStamp를 data.isStamp()로 설정
                Log.d("LOGAPIisClickedStamp is", String.valueOf(isClickedStamp));

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