package com.devinsight.vegiedo.view.store;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;

import java.util.List;

public class UserReviewItemAdapter extends RecyclerView.Adapter<UserReviewItemAdapter.BaseViewHolder> {

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

}
