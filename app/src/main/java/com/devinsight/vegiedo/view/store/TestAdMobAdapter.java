package com.devinsight.vegiedo.view.store;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.ReviewReportRequestDTO;
import com.devinsight.vegiedo.view.search.ActivityViewModel;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class TestAdMobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<UserReviewItem> userReviewItemList;
    private static ActivityViewModel viewModel;
    private static Long currentStoreId;
    public static final int STORE_DETAIL_REVIEW_PAGE = 0;
    public static final int REVIEW_RC = 1;
    public static final int AD_BANNER = 2;
    private enum DialogType {
        DELETE,
        REPORT_TYPE,
    }

    public TestAdMobAdapter(List<UserReviewItem> userReviewItemList) {
        if (userReviewItemList != null) {
//            Log.d("어댑터1-1:받는 updatedItems", userReviewItemList.toString());
            this.userReviewItemList = userReviewItemList;
        } else {
//            Log.d("어댑터1:받는 updatedItems", userReviewItemList.toString());
            this.userReviewItemList = new ArrayList<>();
        }
    }
    public TestAdMobAdapter(List<UserReviewItem> items, ActivityViewModel viewModel) {
        if(userReviewItemList!=null){
            Log.d("어댑터1-2:받는 updatedItems", userReviewItemList.toString());
        }
        this.userReviewItemList = items;
        this.viewModel = viewModel;
        this.currentStoreId = viewModel.getStoreId().getValue();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("어댑터2 getItemViewType", "position:"+position+", "+String.valueOf(userReviewItemList.get(position).getItemType().ordinal()));
        return userReviewItemList.get(position).getItemType().ordinal();
    }

    public void setReviewItems(List<UserReviewItem> items) {
        this.userReviewItemList = items;
        for(int i=0; i<getItemCount(); i++){
//            Log.d("어댑터3:LOGAPISet", items.get(i).getReviewId().toString());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case AD_BANNER:
                View bannerLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_banner_item, parent, false);
                ViewHolderAdBanner myAdViewHolder = new ViewHolderAdBanner(bannerLayoutView);
                return myAdViewHolder;

            case STORE_DETAIL_REVIEW_PAGE:
                View blogView = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_reporting_type_dialog, parent, false);
                ViewHolderBlogReviewPage viewHolderBlogReviewPage = new ViewHolderBlogReviewPage(blogView);
                return viewHolderBlogReviewPage;

            case REVIEW_RC:
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_detail_review_item, parent, false);
                final ViewHolderReviewRC viewHolderReviewRC = new ViewHolderReviewRC(view);
                return viewHolderReviewRC;


        }

//        if (viewType == UserReviewItem.ItemType.STORE_DETAIL_BLOG_REVIEW_PAGE.ordinal()) {
//            view = inflater.inflate(R.layout.store_detail_blog_review_item, parent, false);
//            return new ViewHolderBlogReviewPage(view);
//        } else if (viewType == UserReviewItem.ItemType.REPORT_COMPELETE.ordinal()) {
//            view = inflater.inflate(R.layout.select_reporting_type_dialog, parent, false);
//            return new ViewHolderBlogReviewPage(view);
//        } else if (viewType == UserReviewItem.ItemType.REVIEW_RC.ordinal()) {
//            view = inflater.inflate(R.layout.store_detail_review_item, parent, false);
//            return new ViewHolderReviewRC(view);
//        } else if (viewType == UserReviewItem.ItemType.AD_BANNER.ordinal()) {
//            view = inflater.inflate(R.layout.ad_banner_item, parent, false);
//            return new ViewHolderAdBanner(view);
//        } else {
//            throw new IllegalArgumentException("Unknown view type");
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case AD_BANNER: // Assuming you've defined this constant for AdView items.
                Object adItem = userReviewItemList.get(position);
                if (adItem instanceof AdView) {
                    ViewHolderAdBanner adViewHolder = (ViewHolderAdBanner) holder;
                    AdView adView = (AdView) adItem;
                    ViewGroup adCardView = (ViewGroup) adViewHolder.itemView;

                    if (adCardView.getChildCount() > 0) {
                        adCardView.removeAllViews();
                    }
                    if (adView.getParent() != null) {
                        ((ViewGroup) adView.getParent()).removeView(adView);
                    }

                    // Load the ad
                    com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
                    adView.loadAd(adRequest);

                    // Add the banner ad to the ad view.
                    adCardView.addView(adView);
                }
                break;

            case REVIEW_RC: // Assuming you've defined this constant for review items.
                Object reviewItem = userReviewItemList.get(position);
                if (reviewItem instanceof UserReviewItem) {
                    ViewHolderReviewRC viewHolder = (ViewHolderReviewRC) holder;
                    UserReviewItem currentItem = (UserReviewItem) reviewItem;
                    viewHolder.bindData(currentItem);
                }
                break;

            case STORE_DETAIL_REVIEW_PAGE: // Assuming you've defined this constant for blog review items.
                Object blogReviewItem = userReviewItemList.get(position);
                if (blogReviewItem instanceof UserReviewItem) {
                    ViewHolderBlogReviewPage viewHolder = (ViewHolderBlogReviewPage) holder;
                    UserReviewItem currentItem = (UserReviewItem) blogReviewItem;
                    viewHolder.bindData(currentItem);
                }
                break;

            default:
                // Handle any other unknown view types if necessary.
                break;
        }
    }


    @Override
    public int getItemCount() {
        if (userReviewItemList != null){
//            Log.d("LOGAPIgetItemCount", String.valueOf(userReviewItemList.size())); //20으로 잘 나옴
            Log.d("어댑터6 getItemCount", String.valueOf(userReviewItemList.size()));
            return userReviewItemList.size();
        }
        return 0;
    }

    abstract static class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ViewHolderBlogReviewPage extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        ImageView imageView;

        ViewHolderBlogReviewPage(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.storeBlogReview_user_name);
            content = itemView.findViewById(R.id.storeBlogReview_content);
            imageView = itemView.findViewById(R.id.storeBlogReview_image);
        }

        void bindData(UserReviewItem item) {
            currentStoreId = viewModel.getStoreId().getValue();
            title.setText(item.getTitle());
            content.setText(item.getContent());

            List<String> imageUrls = item.getUserReviewImageUrlList();
            if (imageUrls.size() > 0) Glide.with(itemView.getContext()).load(imageUrls.get(0)).into(imageView);
        }
    }

    class ViewHolderReviewRC extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        ImageView imageView5;
        RatingBar ratingBar;
        TextView storeReview_modify_text;
        TextView storeReview_delete_text;
        ImageView storeReview_report_btn;
        UserReviewItem userReviewItem;
        Long reviewId;
        Long storeId;

        ViewHolderReviewRC(@NonNull View itemView) {
            super(itemView);
//            this.userReviewItem = item;

            title = itemView.findViewById(R.id.storeReview_title);
            content = itemView.findViewById(R.id.storeReview_content);
            imageView1 = itemView.findViewById(R.id.storeReview_image1);
            imageView2 = itemView.findViewById(R.id.storeReview_image2);
            imageView3 = itemView.findViewById(R.id.storeReview_image3);
            imageView4 = itemView.findViewById(R.id.storeReview_image4);
            imageView5 = itemView.findViewById(R.id.storeReview_image5);
            ratingBar = itemView.findViewById(R.id.storeReview_ratingbar);
            storeReview_modify_text = itemView.findViewById(R.id.storeReview_modify_text);
            storeReview_delete_text = itemView.findViewById(R.id.storeReview_delete_text);
            storeReview_report_btn = itemView.findViewById(R.id.btn_comment_report);

        }
        void bindData(UserReviewItem item) {
            this.userReviewItem = item;

            //로컬에 저장된 닉네임값 가져오기
            SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            String userName = sharedPreferences.getString("userName", "기본값");
            reviewId = userReviewItem.getReviewId();
            currentStoreId = viewModel.getStoreId().getValue();
            String reviewWriter = userReviewItem.getTitle();

            Log.d("LOGAPI아이디!!!!!", "" + userName + ", " + reviewWriter +", "+currentStoreId + " " + reviewId);

            title.setText(userReviewItem.getTitle());
            content.setText(userReviewItem.getContent());

            List<String> imageUrls = userReviewItem.getUserReviewImageUrlList();
            if (imageUrls.size() > 0) Glide.with(itemView.getContext()).load(imageUrls.get(0)).into(imageView1);
            if (imageUrls.size() > 1) Glide.with(itemView.getContext()).load(imageUrls.get(1)).into(imageView2);
            if (imageUrls.size() > 2) Glide.with(itemView.getContext()).load(imageUrls.get(2)).into(imageView3);
            if (imageUrls.size() > 3) Glide.with(itemView.getContext()).load(imageUrls.get(3)).into(imageView4);
            if (imageUrls.size() > 4) Glide.with(itemView.getContext()).load(imageUrls.get(4)).into(imageView5);

            ratingBar.setRating(userReviewItem.getRatingBar());

            storeReview_modify_text.setOnClickListener(v -> {
                WritingReviewFragment fragment = WritingReviewFragment.newInstance(
                        currentStoreId,
                        userReviewItem.getReviewId(),
                        userReviewItem.getContent(),
                        userReviewItem.getUserReviewImageUrlList(),
                        userReviewItem.getRatingBar()
                );

                FragmentManager fragmentManager = ((FragmentActivity) itemView.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            });

            storeReview_delete_text.setOnClickListener(v -> {
                Log.d("LOG삭제", "삭제 버튼 눌림");
                showDialog(DialogType.DELETE, itemView.getContext(), currentStoreId, reviewId);
            });
            storeReview_report_btn.setOnClickListener(v -> {
                Log.d("LOG신고", "신고 버튼 눌림");
                showDialog(DialogType.REPORT_TYPE, itemView.getContext(), currentStoreId, reviewId);
            });

            if(Objects.equals(reviewWriter, userName)){
                storeReview_modify_text.setVisibility(View.VISIBLE);
                storeReview_delete_text.setVisibility(View.VISIBLE);
                storeReview_report_btn.setVisibility(View.INVISIBLE);
            }else{
                storeReview_modify_text.setVisibility(View.INVISIBLE);
                storeReview_delete_text.setVisibility(View.INVISIBLE);
                storeReview_report_btn.setVisibility(View.VISIBLE);
            }
        }

    }
    static class ViewHolderAdBanner extends RecyclerView.ViewHolder {
        AdView adBannerView;

        ViewHolderAdBanner(@NonNull View itemView) {
            super(itemView);
            adBannerView = itemView.findViewById(R.id.ad_banner_item_view);
        }
    }

    private void showDialog(DialogType type, Context context, Long storeId, Long reviewId) {
        int layoutId;
        View dialogView;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (type) {
            case DELETE:
                layoutId = R.layout.delete_review_dialog;
                dialogView = inflater.inflate(layoutId, null);
                builder.setView(dialogView);
                AlertDialog deleteDialog = builder.create();
                Log.d("AlertDialog", deleteDialog.toString());
                setupDeleteDialog(context, storeId, reviewId, dialogView, deleteDialog);
                deleteDialog.setContentView(R.layout.dialog_custom);
                deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명하게 설정
                deleteDialog.show();
                return; // 이 경우에서 다른 대화 상자를 생성하지 않도록 return합니다.
            case REPORT_TYPE:
                layoutId = R.layout.select_reporting_type_dialog;
                dialogView = inflater.inflate(layoutId, null);
                builder.setView(dialogView);
                AlertDialog reportDialog = builder.create();
                Log.d("AlertDialog", reportDialog.toString());
                setupReportDialog(context, storeId, reviewId, dialogView, reportDialog);
                reportDialog.setContentView(R.layout.dialog_custom);
                reportDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명하게 설정
                reportDialog.show();
                break;
            default:
                layoutId = R.layout.accept_reporting_dialog;
                dialogView = inflater.inflate(layoutId, null);
                builder.setView(dialogView);
                break;
        }

    }
    //    아래는 다이얼로그 관련 함수
//--------------------------------------------------------------------------------------------------------------------------------
    //삭제 다이얼로그 생성
    private void setupDeleteDialog(Context context, Long storeId, Long reviewId, View dialogView, AlertDialog dialog) {
        Button yesBtn = dialogView.findViewById(R.id.DeleteReivew_yesBtn);
        Button noBtn = dialogView.findViewById(R.id.DeleteReivew_noBtn);
        ImageView closeIcon = dialogView.findViewById(R.id.green_x_circle);

        yesBtn.setOnClickListener(v -> {
            Toast.makeText(context, "예 버튼입니다.", Toast.LENGTH_SHORT).show();
            Log.d("LOG리뷰삭제", "예 " + storeId);
            // viewModel이 여기서 접근 가능하다면 이렇게 호출할 수 있습니다.
            viewModel.ReviewDeleteData(storeId, reviewId);
            notifyDataSetChanged();
            dialog.dismiss();
        });

        noBtn.setOnClickListener(v -> {
            Toast.makeText(context, "아니오 버튼입니다.", Toast.LENGTH_SHORT).show();
            Log.d("LOG리뷰삭제", "아니오");
            dialog.dismiss();
        });

        closeIcon.setOnClickListener(v -> dialog.dismiss());
    }

    //신고 다이얼로그 생성
    private void setupReportDialog(Context context, Long storeId, Long reviewId, View dialogView, AlertDialog dialog) {

        ToggleButton report_type_btn1 = dialogView.findViewById(R.id.report_type_btn1);
        ToggleButton report_type_btn2 = dialogView.findViewById(R.id.report_type_btn2);
        ToggleButton report_type_btn3 = dialogView.findViewById(R.id.report_type_btn3);
        ToggleButton report_type_btn4 = dialogView.findViewById(R.id.report_type_btn4);
        EditText reasons_edit_text = dialogView.findViewById(R.id.other_reasons_text);
        TextView num_of_letter_text = dialogView.findViewById(R.id.num_of_letter);
        TextView reporting_btn = dialogView.findViewById(R.id.reporting_btn);

        //각 버튼 별 reportType(신고유형)과 기타에서의 option(기타 사유)
        AtomicReference<String> reportType = new AtomicReference<>("");
        AtomicReference<String> opinion = new AtomicReference<>("");

        ImageView closeIcon = dialogView.findViewById(R.id.green_x_circle);

        reasons_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                opinion.set(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        report_type_btn1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                report_type_btn2.setChecked(false);
                report_type_btn3.setChecked(false);
                report_type_btn4.setChecked(false);
                reasons_edit_text.setVisibility(View.INVISIBLE);
                num_of_letter_text.setVisibility(View.INVISIBLE);
                reportType.set("부적절한 사진 사용");
                opinion.set("");
            }
        });

        report_type_btn2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                report_type_btn1.setChecked(false);
                report_type_btn3.setChecked(false);
                report_type_btn4.setChecked(false);
                reasons_edit_text.setVisibility(View.INVISIBLE);
                num_of_letter_text.setVisibility(View.INVISIBLE);
                reportType.set("부적절한 언어 사용");
                opinion.set("");
            }
        });

        report_type_btn3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                report_type_btn1.setChecked(false);
                report_type_btn2.setChecked(false);
                report_type_btn4.setChecked(false);
                reasons_edit_text.setVisibility(View.INVISIBLE);
                num_of_letter_text.setVisibility(View.INVISIBLE);
                reportType.set("허위 정보 기재");
                opinion.set("");
            }
        });

        report_type_btn4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                report_type_btn1.setChecked(false);
                report_type_btn2.setChecked(false);
                report_type_btn3.setChecked(false);
                reasons_edit_text.setVisibility(View.VISIBLE);
                num_of_letter_text.setVisibility(View.VISIBLE);
                reportType.set("기타");
            }
        });

        // 완료 버튼을 눌렀을 때
        reporting_btn.setOnClickListener(v -> {

            ReviewReportRequestDTO requestDTO = new ReviewReportRequestDTO();
            Log.d("ReviewReportData", "reportType:" + reportType + ", opinion:" + opinion);
            requestDTO.setReportType(reportType.get());
            requestDTO.setOpinion(opinion.get());
            viewModel.ReviewReportData(storeId, reviewId, requestDTO);
            dialog.dismiss(); // 다이얼로그 닫기
        });

        closeIcon.setOnClickListener(v -> dialog.dismiss());

    }



}
