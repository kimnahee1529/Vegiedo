package com.devinsight.vegiedo.view.map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.ui.map.MapStoreCardUiData;

import java.util.ArrayList;

public class MapStoreCardUiAdapter extends RecyclerView.Adapter<MapStoreCardUiAdapter.cardViewHolder> {

    private ArrayList<MapStoreCardUiData> cardDataList;
    Context context;
    protected mapCardItemListner mapCardItemListner;

    public MapStoreCardUiAdapter(Context context, ArrayList<MapStoreCardUiData> cardList, mapCardItemListner itemListner) {
        this.cardDataList = cardList;
        this.context = context;
        this.mapCardItemListner = itemListner;
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_map_store_item, parent, false);
        return new cardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        holder.setData(cardDataList.get(position));
    }


    @Override
    public int getItemCount() {
        return cardDataList.size();
    }

    public class cardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView mapCardView;
        private ImageView storeImage;
        private TextView storeName;
        private TextView storeTag1;
        private TextView storeTag2;
        private TextView address;
        private RatingBar starRating;
        private TextView distance;
        private TextView reviewers;
        private ToggleButton like;
        MapStoreCardUiData cardData;

        public cardViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mapCardView = itemView.findViewById(R.id.map_cardView);
            storeImage = itemView.findViewById(R.id.map_card_image);
            storeName = itemView.findViewById(R.id.map_card_store_name);
            storeTag1 = itemView.findViewById(R.id.map_store_tag1);
            storeTag2 = itemView.findViewById(R.id.map_store_tag2);
            address = itemView.findViewById(R.id.map_store_address);
            starRating = itemView.findViewById(R.id.map_ratingbar_star);
            distance = itemView.findViewById(R.id.map_store_distance);
            reviewers = itemView.findViewById(R.id.map_store_reviewes);
            like = itemView.findViewById(R.id.btn_map_store_like);

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cardData.isLike()){
                        cardData.setLike(false);
                        notifyDataSetChanged();
                    }else{
                        cardData.setLike(true);
                        notifyDataSetChanged();
                    }
                }
            });
            // 하트 버튼 토글
//            like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_selected));
//                        cardData.setLike(true);
//                    } else {
//                        like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_default));
//                        cardData.setLike(false);
//                    }
//                }
//            });
        }

        public void setData(MapStoreCardUiData cardData) {
            this.cardData = cardData;

            Glide.with(context)
                    .load(cardData.getStoreImage())
                    .into(storeImage);

            storeName.setText(cardData.getStoreName());
            storeTag1.setText(String.valueOf(cardData.getStoreTag1()));
            storeTag2.setText(String.valueOf(cardData.getStoreTag2()));
            address.setText(cardData.getAddress());
            starRating.setRating(cardData.getStarlating());
            distance.setText(cardData.getDistance() + "m");
            reviewers.setText(cardData.getReviewNum() + " reviews");

//            like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_selected));
            if(cardData.isLike()) {
                like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_selected));
            } else {
                like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_default));
            }
        }

        @Override
        public void onClick(View view) {
            if (mapCardItemListner != null) {
                mapCardItemListner.onCardClick(cardData, getAbsoluteAdapterPosition());
            }
        }
    }

    public interface mapCardItemListner {
        void onCardClick(MapStoreCardUiData item, int position);
    }
}
