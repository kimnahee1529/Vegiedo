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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.ReviewReportRequestDTO;
import com.devinsight.vegiedo.view.search.ActivityViewModel;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UserReviewItemAdapter extends RecyclerView.Adapter<UserReviewItemAdapter.BaseViewHolder> {
    private List<UserReviewItem> userReviewItemList;
    private static ActivityViewModel viewModel;
    private static Long currentStoreId;
    private int currentMaxItems = 2; // 처음에는 2개만 표시
    private int itemsPerPage = 7; // "더보기" 버튼을 클릭할 때마다 추가되는 아이템 수
    private MoreItemsListener moreItemsListener;
    private ReviewDeleteListener reviewDeleteListener;
    private ReportCompleteListener ReportCompleteListener;

    // "더보기" 버튼 숨기기와 관련된 이벤트를 처리할 콜백
    public interface MoreItemsListener {
        void onHideMoreButton();
    }

    // 리뷰 삭제와 관련된 이벤트를 처리할 콜백
    public interface ReviewDeleteListener {
        void ReviewDelete(Long storeId, Long reviewId);
    }

    // 신고 완료 버튼과 관련된 이벤트를 처리할 콜백
    public interface ReportCompleteListener{
        void ReportListener(Long storeId, Long reviewId, ReviewReportRequestDTO requestDTO);
    }

    // 더보기 버튼 기능을 위해 맨처음 실행되는 코드. 어댑터 내에서 해당 인터페이스를 구현한 리스너를 설정할 수 있는 메서드를 제공
    // StoreReviewFragment에 setMoreItemsListener 콜백 설정 메서드
    public void setMoreItemsListener(MoreItemsListener moreItemsListener) {
        this.moreItemsListener = moreItemsListener;
        Log.d("더보기리스너", "setMoreItemsListener");
    }

    public void setDeleteListener(ReviewDeleteListener reviewDeleteListener) {
        this.reviewDeleteListener = reviewDeleteListener;
        Log.d("삭제리스너", "reviewDeleteListener");
    }

    public void setReportListener(ReportCompleteListener ReportCompleteListener) {
        this.ReportCompleteListener = ReportCompleteListener;
        Log.d("삭제리스너", "reviewDeleteListener");
    }


    public void showMoreItems() {
        if (currentMaxItems < userReviewItemList.size()) {
            currentMaxItems += itemsPerPage;
            notifyDataSetChanged();
        }
        if (currentMaxItems >= userReviewItemList.size() && moreItemsListener != null) {
            Log.d("더보기", "더보기 버튼 다 누름");
            moreItemsListener.onHideMoreButton();
        }
    }
    @Override
    public int getItemCount() {
        if (userReviewItemList != null) {
            return Math.min(currentMaxItems, userReviewItemList.size());
        }
        return 0;
    }
    private enum DialogType {
        DELETE,
        REPORT_TYPE,
    }

    public UserReviewItemAdapter(List<UserReviewItem> userReviewItemList) {
        if (userReviewItemList != null) {
//            Log.d("어댑터1-1:받는 updatefdItems", userReviewItemList.toString());
            this.userReviewItemList = userReviewItemList;
        } else {
//            Log.d("어댑터1:받는 updatedItems", userReviewItemList.toString());
            this.userReviewItemList = new ArrayList<>();
        }
    }
    public UserReviewItemAdapter(List<UserReviewItem> items, ActivityViewModel viewModel) {
        if(userReviewItemList!=null){
            Log.d("어댑터1-2:받는 updatedItems", userReviewItemList.toString());
        }
        this.userReviewItemList = items;
        this.viewModel = viewModel;
        this.currentStoreId = viewModel.getStoreIdLiveData().getValue();
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
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        Log.d("어댑터4 viewType", String.valueOf(viewType));
        if (viewType == UserReviewItem.ItemType.STORE_DETAIL_BLOG_REVIEW_PAGE.ordinal()) {
            view = inflater.inflate(R.layout.store_detail_blog_review_item, parent, false);
            return new ViewHolderBlogReviewPage(view);
        } else if (viewType == UserReviewItem.ItemType.REPORT_COMPELETE.ordinal()) {
            view = inflater.inflate(R.layout.select_reporting_type_dialog, parent, false);
            return new ViewHolderBlogReviewPage(view);
        } else if (viewType == UserReviewItem.ItemType.REVIEW_RC.ordinal()) {
            view = inflater.inflate(R.layout.store_detail_review_item, parent, false);
            return new ViewHolderReviewRC(view);
        } else if (viewType == UserReviewItem.ItemType.AD_BANNER.ordinal()) {
            view = inflater.inflate(R.layout.ad_banner_item, parent, false);
            return new ViewHolderAdBanner(view);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        UserReviewItem userReviewItem = userReviewItemList.get(position);

        if (holder instanceof ViewHolderBlogReviewPage) {
            Log.d("어댑터5 onBindViewHolder", String.valueOf(holder));
            ViewHolderBlogReviewPage viewHolder = (ViewHolderBlogReviewPage) holder;
            UserReviewItem currentItem = userReviewItemList.get(position);
            Log.d("LOGAPIbindData확인", String.valueOf(currentItem));
            viewHolder.bindData(currentItem);
        } else if (holder instanceof ViewHolderReviewRC) {
            Log.d("어댑터5 onBindViewHolder", String.valueOf(holder));
            ViewHolderReviewRC viewHolder = (ViewHolderReviewRC) holder;
            UserReviewItem currentItem = userReviewItemList.get(position);
            viewHolder.bindData(currentItem);
        } else if (holder instanceof ViewHolderAdBanner) {
            Log.d("어댑터5 onBindViewHolder", String.valueOf(holder));
            ViewHolderAdBanner viewHolder = (ViewHolderAdBanner) holder;
            com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
            viewHolder.adBannerView.loadAd(adRequest);
        }

    }

    abstract static class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ViewHolderBlogReviewPage extends BaseViewHolder {
        TextView title;
        TextView content;
        ImageView imageView;
        UserReviewItem userReviewItem;

        ViewHolderBlogReviewPage(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.storeBlogReview_user_name);
            content = itemView.findViewById(R.id.storeBlogReview_content);
            imageView = itemView.findViewById(R.id.storeBlogReview_image);
        }

        void bindData(UserReviewItem item) {
            this.userReviewItem = item;

            currentStoreId = viewModel.getStoreIdLiveData().getValue();
            title.setText(userReviewItem.getTitle());
            content.setText(userReviewItem.getContent());

            List<String> imageUrls = userReviewItem.getUserReviewImageUrlList();
            if (imageUrls.size() > 0) Glide.with(itemView.getContext()).load(imageUrls.get(0)).into(imageView);
        }
    }


    class ViewHolderReviewRC extends BaseViewHolder {
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
            currentStoreId = viewModel.getStoreIdLiveData().getValue();
            String reviewWriter = userReviewItem.getTitle();
            int radius = 20;

            Log.d("LOGAPI아이디!!!!!", "" + userName + ", " + reviewWriter +", "+currentStoreId + " " + reviewId);

            title.setText(userReviewItem.getTitle());
            content.setText(userReviewItem.getContent());

            List<String> imageUrls = userReviewItem.getUserReviewImageUrlList();
            Log.d("이미지 null아님?", imageUrls.toString());

            if (imageUrls == null) {
                imageView1.setVisibility(View.GONE);
                imageView2.setVisibility(View.GONE);
                imageView3.setVisibility(View.GONE);
                imageView4.setVisibility(View.GONE);
                imageView5.setVisibility(View.GONE);
                return; // 이미지 URL이 null이면 아래 로직을 실행할 필요가 없으므로 return 합니다.
            }

            if (imageUrls.size() > 0) {
                Glide.with(itemView.getContext())
                        .load(imageUrls.get(0))
                        .transform(new CenterCrop(), new RoundedCorners(radius))
                        .into(imageView1);
                imageView1.setVisibility(View.VISIBLE);
            } else {
                imageView1.setVisibility(View.GONE);
            }

            if (imageUrls.size() > 1) {
                Glide.with(itemView.getContext())
                        .load(imageUrls.get(1))
                        .transform(new CenterCrop(), new RoundedCorners(radius))
                        .into(imageView2);
                imageView2.setVisibility(View.VISIBLE);
            } else {
                imageView2.setVisibility(View.GONE);
            }

            if (imageUrls.size() > 2) {
                Glide.with(itemView.getContext())
                        .load(imageUrls.get(2))
                        .transform(new CenterCrop(), new RoundedCorners(radius))
                        .into(imageView3);
                imageView3.setVisibility(View.VISIBLE);
            } else {
                imageView3.setVisibility(View.GONE);
            }

            if (imageUrls.size() > 3) {
                Glide.with(itemView.getContext())
                        .load(imageUrls.get(3))
                        .transform(new CenterCrop(), new RoundedCorners(radius))
                        .into(imageView4);
                imageView4.setVisibility(View.VISIBLE);
            } else {
                imageView4.setVisibility(View.GONE);
            }

            if (imageUrls.size() > 4) {
                Glide.with(itemView.getContext())
                        .load(imageUrls.get(4))
                        .transform(new CenterCrop(), new RoundedCorners(radius))
                        .into(imageView5);
                imageView5.setVisibility(View.VISIBLE);
            } else {
                imageView5.setVisibility(View.GONE);
            }


            ratingBar.setRating(userReviewItem.getStars());

            storeReview_modify_text.setOnClickListener(v -> {
                WritingReviewFragment fragment = WritingReviewFragment.newInstance(
                        currentStoreId,
                        userReviewItem.getReviewId(),
                        userReviewItem.getContent(),
                        userReviewItem.getUserReviewImageUrlList(),
                        userReviewItem.getStars()
                );

                FragmentManager fragmentManager = ((FragmentActivity) itemView.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            });

            storeReview_delete_text.setOnClickListener(v -> {
//                reviewDeleteListener.ReviewDelete(storeId, reviewId);
//                Log.d("LOG삭제", "삭제 버튼 눌림");
                showDialog(DialogType.DELETE, itemView.getContext(), currentStoreId, reviewId);
            });
            storeReview_report_btn.setOnClickListener(v -> {
                Log.d("LOG신고", "신고 버튼 눌림");
                showDialog(DialogType.REPORT_TYPE, itemView.getContext(), currentStoreId, reviewId);
            });

            //내가 작성한 리뷰일 경우 수정/삭제 가능
            if(userReviewItem.getMine()){
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
    static class ViewHolderAdBanner extends BaseViewHolder {
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
//            Toast.makeText(context, "예 버튼입니다.", Toast.LENGTH_SHORT).show();
            Log.d("LOG리뷰삭제", "예 " + storeId);
            reviewDeleteListener.ReviewDelete(storeId, reviewId);
            notifyDataSetChanged();
            dialog.dismiss();
        });

        noBtn.setOnClickListener(v -> {
//            Toast.makeText(context, "아니오 버튼입니다.", Toast.LENGTH_SHORT).show();
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
            requestDTO.setReportType(reportType.get());
            requestDTO.setOpinion(opinion.get());
            Log.d("ReviewReportData", "storeId:"+storeId+", reviewId:"+reviewId+", reportType:" + reportType + ", opinion:" + opinion +", DTO:"+requestDTO);
//            viewModel.ReviewReportData(storeId, reviewId, requestDTO);
            ReportCompleteListener.ReportListener(storeId, reviewId, requestDTO);
            dialog.dismiss(); // 다이얼로그 닫기
        });

        closeIcon.setOnClickListener(v -> dialog.dismiss());

    }




}
