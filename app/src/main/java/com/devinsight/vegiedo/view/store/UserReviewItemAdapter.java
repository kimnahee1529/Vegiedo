package com.devinsight.vegiedo.view.store;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;

import java.util.List;

public class UserReviewItemAdapter extends RecyclerView.Adapter<UserReviewItemAdapter.BaseViewHolder> {

    private enum DialogType {
        DELETE,
        REPORT_TYPE,
        REPORT_COMPELETE
    }
    private List<UserReviewItem> userReviewItemList;

    public UserReviewItemAdapter(List<UserReviewItem> userReviewItemList) {
        this.userReviewItemList = userReviewItemList;
    }

    @Override
    public int getItemViewType(int position) {
        return userReviewItemList.get(position).getItemType().ordinal();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == UserReviewItem.ItemType.STORE_DETAIL_REVIEW_PAGE.ordinal()) {
            view = inflater.inflate(R.layout.store_detail_review_item, parent, false);
            return new ViewHolderReviewPage(view);
        } else if (viewType == UserReviewItem.ItemType.STORE_DETAIL_BLOG_REVIEW_PAGE.ordinal()) {
            view = inflater.inflate(R.layout.store_detail_blog_review_item, parent, false);
            return new ViewHolderBlogReviewPage(view);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        UserReviewItem userReviewItem = userReviewItemList.get(position);

        if (holder instanceof ViewHolderReviewPage) {
            ViewHolderReviewPage viewHolder = (ViewHolderReviewPage) holder;
            viewHolder.title.setText(userReviewItem.getTitle());
            viewHolder.content.setText(userReviewItem.getContent());
            viewHolder.imageView1.setImageResource(Integer.parseInt(userReviewItem.getUserReviewImageUrlList().get(0)));
            viewHolder.imageView2.setImageResource(Integer.parseInt(userReviewItem.getUserReviewImageUrlList().get(1)));
            viewHolder.imageView3.setImageResource(Integer.parseInt(userReviewItem.getUserReviewImageUrlList().get(2)));
            viewHolder.imageView4.setImageResource(Integer.parseInt(userReviewItem.getUserReviewImageUrlList().get(3)));
            viewHolder.imageView5.setImageResource(Integer.parseInt(userReviewItem.getUserReviewImageUrlList().get(4)));
            viewHolder.ratingBar.setRating(userReviewItem.getRatingBar());
            viewHolder.storeReview_modify_text.setOnClickListener(v -> {
                WritingReviewFragment fragment = new WritingReviewFragment();
                FragmentManager fragmentManager = ((FragmentActivity) viewHolder.itemView.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // 액티비티의 루트 뷰를 교체(예: android.R.id.content)
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
            viewHolder.storeReview_delete_text.setOnClickListener(v -> {
                Log.d("LOG삭제", "삭제 버튼 눌림");
                showDialog(DialogType.DELETE, holder.itemView.getContext());
            });
            viewHolder.storeReview_report_btn.setOnClickListener(v -> {
                Log.d("LOG신고", "신고 버튼 눌림");
                showDialog(DialogType.REPORT_TYPE, holder.itemView.getContext());
            });

        } else if (holder instanceof ViewHolderBlogReviewPage) {
            ViewHolderBlogReviewPage viewHolder = (ViewHolderBlogReviewPage) holder;
            viewHolder.title.setText(userReviewItem.getTitle());
            viewHolder.content.setText(userReviewItem.getContent());
            viewHolder.imageView1.setImageResource(Integer.parseInt(userReviewItem.getUserReviewImageUrlList().get(0)));
        }



    }

    @Override
    public int getItemCount() {
        return userReviewItemList.size();
    }

    abstract static class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class ViewHolderReviewPage extends BaseViewHolder {
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

        ViewHolderReviewPage(@NonNull View itemView) {
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
            storeReview_report_btn = itemView.findViewById(R.id.storeReview_report_btn);
        }

    }

    static class ViewHolderBlogReviewPage extends BaseViewHolder {
        TextView title;
        TextView content;
        ImageView imageView1;

        ViewHolderBlogReviewPage(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.storeReview_title);
            content = itemView.findViewById(R.id.storeReview_content);
            imageView1 = itemView.findViewById(R.id.storeReview_image1);
        }
    }

    private void showDialog(DialogType type, Context context) {
        int layoutId;
        if(type == DialogType.DELETE){
            layoutId = R.layout.delete_review_dialog;

            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(layoutId, null);

            Button yesBtn = dialogView.findViewById(R.id.DeleteReivew_yesBtn);
            Button noBtn = dialogView.findViewById(R.id.DeleteReivew_noBtn);
            ImageView closeIcon = dialogView.findViewById(R.id.green_x_circle);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);

            final AlertDialog dialog = builder.create();

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "예 버튼입니다.", Toast.LENGTH_SHORT).show();
                    Log.d("LOGf리뷰삭제", "예");
                    dialog.dismiss();
                }
            });

            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "아니오 버튼입니다.", Toast.LENGTH_SHORT).show();
                    Log.d("LOGf리뷰삭제", "아니오");
                    dialog.dismiss();
                }
            });

            closeIcon.setOnClickListener(v -> dialog.dismiss());
            dialog.show();

        } else if(type == DialogType.REPORT_TYPE){
            layoutId = R.layout.select_reporting_type_dialog;
        } else {
            layoutId = R.layout.accept_reporting_dialog;
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(layoutId, null);
        ImageView closeIcon = dialogView.findViewById(R.id.green_x_circle);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        closeIcon.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}
