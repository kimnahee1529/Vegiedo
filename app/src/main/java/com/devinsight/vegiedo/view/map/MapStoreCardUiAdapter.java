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
import com.devinsight.vegiedo.view.store.UserReviewItemAdapter;

import java.util.ArrayList;

public class MapStoreCardUiAdapter extends RecyclerView.Adapter<MapStoreCardUiAdapter.cardViewHolder> {

    private ArrayList<MapStoreCardUiData> cardDataList;
    Context context;
    protected mapCardItemListner mapCardItemListner;
    private LikeBtnListener likeBtnListener;
    private CancleLikeBtnListener cancleLikeBtnListener;

    public MapStoreCardUiAdapter(Context context, ArrayList<MapStoreCardUiData> cardList, mapCardItemListner itemListner) {
        this.cardDataList = cardList;
        this.context = context;
        this.mapCardItemListner = itemListner;
    }

    public interface LikeBtnListener {
        void onLikeButton(Long storeId);
    }
    public interface CancleLikeBtnListener {
        void onCancleLiketButton(Long storeId);
    }

    public void setLikeBtnListener(LikeBtnListener likeBtnListener) {
        this.likeBtnListener = likeBtnListener;
        Log.d("찜버튼리스너", "setLikeBtnListener");
    }
    public void setCancleLikeBtnListener(CancleLikeBtnListener cancleLikeBtnListener) {
        this.cancleLikeBtnListener = cancleLikeBtnListener;
        Log.d("찜버튼취소리스너", "setCancleLikeBtnListener");
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
                        Log.d("찜버튼되어있음", String.valueOf(cardData.isLike())+", "+cardData.getStoreId());
                        cancleLikeBtnListener.onCancleLiketButton(cardData.getStoreId());
                        cardData.setLike(false);
                        notifyDataSetChanged();
                    }else{
                        Log.d("찜버튼취소되어있었음", String.valueOf(cardData.isLike())+", "+cardData.getStoreId());
                        likeBtnListener.onLikeButton(cardData.getStoreId());
                        cardData.setLike(true);
                        notifyDataSetChanged();
                    }
                }
            });
        }

        public void setData(MapStoreCardUiData cardData) {
            this.cardData = cardData;

            Glide.with(context)
                    .load(cardData.getStoreImage())
                    .into(storeImage);

            storeName.setText(cardData.getStoreName());
//            if(storeTag1.)


            storeTag1.setText(String.valueOf(cardData.getStoreTag1()));
            storeTag2.setText(String.valueOf(cardData.getStoreTag2()));
            address.setText(cardData.getAddress());
            starRating.setRating(cardData.getStarlating());
            distance.setText(cardData.getDistance() + "m");
            reviewers.setText(cardData.getReviewNum() + " reviews");

            if(cardData.isLike()) {
                like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_selected));
            } else {
                like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_default));
            }

            if (storeTag1.getText().toString().trim().isEmpty()) {
                storeTag1.setVisibility(View.GONE);
            } else {
                storeTag1.setVisibility(View.VISIBLE);
            }
            if (storeTag2.getText().toString().trim().isEmpty()) {
                storeTag2.setVisibility(View.GONE);
            } else {
                storeTag2.setVisibility(View.VISIBLE);
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
