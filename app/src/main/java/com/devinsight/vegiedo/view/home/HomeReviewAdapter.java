package com.devinsight.vegiedo.view.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.ui.home.HomeBannerData;
import com.devinsight.vegiedo.data.ui.home.HomeReviewUiData;

import java.util.ArrayList;
import java.util.List;

public class HomeReviewAdapter extends RecyclerView.Adapter<HomeReviewAdapter.ReviewViewHolder>{

    private ArrayList<HomeReviewUiData> reviewList;
    Context context;
    protected reviewItemListner reviewItemListner;

    public HomeReviewAdapter(Context context, ArrayList<HomeReviewUiData> reviewList, reviewItemListner itemListner){
        this.reviewList = reviewList;
        this.context = context;
        this.reviewItemListner = itemListner;
    }
    public void setReviewList(List<HomeReviewUiData> reviewList){
        this.reviewList.clear();
        this.reviewList.addAll(reviewList);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_review_item,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        HomeReviewUiData data = reviewList.get(position);
        holder.storeName.setText(data.getStoreName());
        for( int i = 0 ; i < reviewList.size() ; i ++ ) {
            List<String> tags = reviewList.get(i).getTags();
            if(tags != null) {
                if(tags.size() == 2) {
                    holder.storeTag1.setText(tags.get(0));
                    holder.storeTag2.setText(tags.get(1));
                } else if(tags.size() == 1) {
                    holder.storeTag2.setText(tags.get(0));
                    holder.storeTag1.setText(null);
                    holder.storeTag1.setVisibility(View.INVISIBLE);
                } else if(tags.size() == 0) {
                    Log.e("store tag is null", "store tag is null");
                }
            }
        }
       String imageUrl = data.getStoreImage();
        Glide.with(context).load(imageUrl).into(holder.storeImage);

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CardView storeCard;
        private ImageView storeImage;
        private TextView storeName;
        private TextView storeTag1;
        private TextView storeTag2;
        HomeReviewUiData reviewItem;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            storeCard = itemView.findViewById(R.id.store_card);
            storeImage = itemView.findViewById(R.id.store_image);
            storeName = itemView.findViewById(R.id.store_name);
            storeTag1 = itemView.findViewById(R.id.store_tag1);
            storeTag2 = itemView.findViewById(R.id.store_tag2);

        }

        @Override
        public void onClick(View view) {
            if(reviewItemListner != null){
                reviewItemListner.onReviewItemClick(view, reviewList.get(getLayoutPosition()), getLayoutPosition());
            }
        }
    }

    public interface reviewItemListner {
        void onReviewItemClick(View view, HomeReviewUiData item, int position);

    }

}