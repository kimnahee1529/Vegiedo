package com.devinsight.vegiedo.view.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.devinsight.vegiedo.data.response.MapStoreListData;

import java.util.ArrayList;

public class MapStoreCardAdapter extends RecyclerView.Adapter<MapStoreCardAdapter.cardViewHolder> {

    private ArrayList<MapStoreListData> cardDataList;
    Context context;
    protected mapCardItemListner mapCardItemListner;

    public MapStoreCardAdapter(Context context, ArrayList<MapStoreListData> cardList, mapCardItemListner itemListner) {
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
        MapStoreListData cardData;

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
        }

        public void setData(MapStoreListData cardData) {
            this.cardData = cardData;

            // storeImage.setImageResource(); // TODO: 이미지를 어떻게 설정할지 결정해야 합니다.
            storeName.setText(cardData.getStoreName());
            //TODO: Tag 정보에 따라 화면에 표시할 방법을 결정해야 합니다.
            // 예를 들면, cardData.getTags().get(0) 등을 사용할 수 있습니다.
            address.setText(cardData.getAddress());
            starRating.setRating(cardData.getStars());
            distance.setText(String.format("%d", cardData.getDistance()));
            //TODO: 'reviewers'에 표시할 리뷰 수에 대한 데이터가 필요합니다.
            if(cardData.getLike()) {
                like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_selected));
            } else {
                like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_default));
            }
            like.setChecked(cardData.getLike());

//            //Glide를 사용하여 이미지를 로드합니다. 필요한 이미지 URL에 따라 수정해야 합니다.
//            Glide.with(context)
//                    // .load(cardData.getImageUrl())  // 여기서는 imageUrl을 가져오는 메소드가 필요합니다.
//                    .placeholder(R.drawable.ic_launcher_background)
//                    .error(R.drawable.ic_launcher_background)
//                    .into(storeImage);
        }

        @Override
        public void onClick(View view) {
            if (mapCardItemListner != null) {
                mapCardItemListner.onCardClick(cardData, getAbsoluteAdapterPosition());
            }
        }
    }

    public interface mapCardItemListner {
        void onCardClick(MapStoreListData item, int position);
    }
}
