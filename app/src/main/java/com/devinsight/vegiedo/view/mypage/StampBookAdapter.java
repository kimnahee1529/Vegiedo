package com.devinsight.vegiedo.view.mypage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.view.store.UserReviewItem;

import java.util.List;

public class StampBookAdapter extends RecyclerView.Adapter<StampBookAdapter.StampBookViewHolder> {

    private List<StampBookItem> stampBookItems;
    private StampBookFragment.OnXCircleClickListener listener;
    public void setOnXCircleClickListener(StampBookFragment.OnXCircleClickListener listener) {
        this.listener = listener;
    }

    public StampBookAdapter(List<StampBookItem> stampBookItems) {
        this.stampBookItems = stampBookItems;
    }

    public void updateData(List<StampBookItem> newData) {
        this.stampBookItems.clear();
        this.stampBookItems.addAll(newData);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public StampBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_stampbook_item, parent, false);
        return new StampBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StampBookViewHolder holder, int position) {
        StampBookItem stampBookItem = stampBookItems.get(position);
        holder.storeName.setText(stampBookItem.getStoreName());
        holder.storeAddress.setText(stampBookItem.getAddress());
        holder.numOfReviews.setText(String.valueOf(stampBookItem.getReviewCount())+" reviews");
        holder.ratingBar.setRating(stampBookItem.getStars());
        holder.stamBook_x_circle.setOnClickListener(v -> {
            if (listener != null) {
                listener.onXCircleClick(position);
            }
        });

        // 원하는 모서리의 반경을 설정. 이 값은 dp 단위로 설정해야 함
        int radius = 20;
        Glide.with(holder.storeImage.getContext())
                .load(stampBookItem.getImages())
                .transform(new CenterCrop(), new RoundedCorners(radius))
                .into(holder.storeImage);
    }

    @Override
    public int getItemCount() {
        return stampBookItems.size();
    }

    class StampBookViewHolder extends RecyclerView.ViewHolder {
        TextView storeName, storeAddress, numOfReviews;
        RatingBar ratingBar;
        ImageView storeImage;
        ImageView stamBook_x_circle;

        public StampBookViewHolder(@NonNull View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.stamBook_store_name);
            storeAddress = itemView.findViewById(R.id.stamBook_store_address);
            numOfReviews = itemView.findViewById(R.id.stamBook_store_numOfreview);
            ratingBar = itemView.findViewById(R.id.stamBook_store_ratingbar);
            storeImage = itemView.findViewById(R.id.stamBook_store_image);
            stamBook_x_circle = itemView.findViewById(R.id.stamBook_x_circle);

        }
        // 뷰홀더 생성자 및 초기화
    }
}
