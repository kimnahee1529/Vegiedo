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

public class UserReviewItemAdapter extends RecyclerView.Adapter<UserReviewItemAdapter.ViewHolderTypeThree> {

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
    public ViewHolderTypeThree onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == UserReviewItem.ItemType.STORE_DETAIL_PAGE.ordinal()) {
            View view = inflater.inflate(R.layout.store_detail_item, parent, false);
            return new ViewHolderTypeThree(view);
        }
        return null; // Ideally you should handle other view types as well
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTypeThree holder, int position) {
        UserReviewItem userReviewItem = userReviewItemList.get(position);
        holder.title.setText(userReviewItem.getTitle());
        holder.content.setText(userReviewItem.getContent());

        // Assuming your userReviewImageUrlList contains drawable resource ids.
        // If they are URLs, you would need to use a library like Glide or Picasso to load them into the ImageViews
        holder.imageView1.setImageResource(Integer.parseInt(userReviewItem.getUserReviewImageUrlList().get(0)));
        holder.imageView2.setImageResource(Integer.parseInt(userReviewItem.getUserReviewImageUrlList().get(1)));
        holder.imageView3.setImageResource(Integer.parseInt(userReviewItem.getUserReviewImageUrlList().get(2)));
        holder.imageView4.setImageResource(Integer.parseInt(userReviewItem.getUserReviewImageUrlList().get(3)));
        holder.imageView5.setImageResource(Integer.parseInt(userReviewItem.getUserReviewImageUrlList().get(4)));

        // Set the rating using the RatingBar
        holder.ratingBar.setRating(userReviewItem.getRatingBar());
    }

    @Override
    public int getItemCount() {
        return userReviewItemList.size();
    }

    static class ViewHolderTypeThree extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        ImageView imageView5;
        RatingBar ratingBar;

        ViewHolderTypeThree(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.storeReview_title);
            content = itemView.findViewById(R.id.storeReview_content);
            imageView1 = itemView.findViewById(R.id.storeReview_image1);
            imageView2 = itemView.findViewById(R.id.storeReview_image2);
            imageView3 = itemView.findViewById(R.id.storeReview_image3);
            imageView4 = itemView.findViewById(R.id.storeReview_image4);
            imageView5 = itemView.findViewById(R.id.storeReview_image5);
            ratingBar = itemView.findViewById(R.id.storeReview_ratingbar); // Assuming this is the id for the RatingBar in your layout
        }
    }
}
