package com.devinsight.vegiedo.view.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.home.HomeReviewData;

import java.util.ArrayList;

public class HomeReviewAdapter extends RecyclerView.Adapter<HomeReviewAdapter.ReviewViewHolder>{

    private ArrayList<HomeReviewData> reviewList;
    Context context;
    protected reviewItemListner reviewItemListner;

    public HomeReviewAdapter(Context context, ArrayList<HomeReviewData> reviewList, reviewItemListner itemListner){
        this.reviewList = reviewList;
        this.context = context;
        this.reviewItemListner = itemListner;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_review_item,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.setData(reviewList.get(position));
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
        private TextView storeTag3;
        HomeReviewData reviewItem;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            storeCard = itemView.findViewById(R.id.store_card);
            storeImage = itemView.findViewById(R.id.store_image);
            storeName = itemView.findViewById(R.id.store_name);
            storeTag1 = itemView.findViewById(R.id.store_tag1);
            storeTag2 = itemView.findViewById(R.id.store_tag2);
            storeTag3 = itemView.findViewById(R.id.store_tag3);

        }
        public void setData(HomeReviewData reviewItem){
            this.reviewItem = reviewItem;

            storeImage.setImageResource(reviewItem.getStoreImage());
            storeName.setText(reviewItem.getStoreName());
            storeTag1.setText(reviewItem.getStoreTag1());
            storeTag2.setText(reviewItem.getStoreTag2());
            storeTag3.setText(reviewItem.getStoreTag3());

        }

        @Override
        public void onClick(View view) {
            if(reviewItemListner != null){
                reviewItemListner.onItemClick(reviewItem);
            }
        }
    }

    public interface reviewItemListner {
        void onItemClick(HomeReviewData item);

    }

}