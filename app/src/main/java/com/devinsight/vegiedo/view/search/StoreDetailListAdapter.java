package com.devinsight.vegiedo.view.search;

import android.content.Context;
import android.util.Log;
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
import com.devinsight.vegiedo.data.response.StoreListData;
import com.devinsight.vegiedo.data.ui.map.MapStoreCardUiData;
import com.devinsight.vegiedo.data.ui.search.SearchStorSummaryeUiData;
import com.devinsight.vegiedo.view.map.MapStoreCardUiAdapter;

import java.util.List;

public class StoreDetailListAdapter extends RecyclerView.Adapter<StoreDetailListAdapter.ViewHolder> {

    private List<StoreListData> searchList;
    Context context;
    protected searchListner searchListner;

    private LikeBtnListener likeBtnListener;
    private CancleLikeBtnListener cancleLikeBtnListener;

    public StoreDetailListAdapter(Context cotnext, List<StoreListData> searchList, searchListner searchlistner) {
        this.searchList = searchList;
        this.context = cotnext;
        this.searchListner = searchlistner;
    }

    public void setStoreList(List<StoreListData> storeList) {
        this.searchList.clear();
        this.searchList.addAll(storeList);
    }

    public interface LikeBtnListener {
        void onLikeButton(Long storeId);
    }
    public interface CancleLikeBtnListener {
        void onCancleLiketButton(Long storeId);
    }

    public void setLikeBtnListener(StoreDetailListAdapter.LikeBtnListener likeBtnListener) {
        this.likeBtnListener = likeBtnListener;
        Log.d("찜버튼리스너", "setLikeBtnListener");
    }
    public void setCancleLikeBtnListener(StoreDetailListAdapter.CancleLikeBtnListener cancleLikeBtnListener) {
        this.cancleLikeBtnListener = cancleLikeBtnListener;
        Log.d("찜버튼취소리스너", "setCancleLikeBtnListener");
    }


    @NonNull
    @Override
    public StoreDetailListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_store_list_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreDetailListAdapter.ViewHolder holder, int position) {
        StoreListData data = searchList.get(position);
        holder.searchData = data;

        String imageUrl = data.getImages();
        holder.storeName.setText(data.getStoreName());
        for( int i = 0 ; i < searchList.size() ; i ++ ) {
            List<String> tags = searchList.get(i).getTags();
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
        holder.address.setText(data.getAddress());
        holder.starRating.setRating(data.getStars());
        Glide.with(context).load(imageUrl).into(holder.storeImage);

        int distance = data.getDistance();

        if( distance < 1000 ) {
            holder.distance.setText(distance + "m");
        } else {
            holder.distance.setText(String.format("%.1f km", distance / 1000.0));
        }

        holder.reviewers.setText(String.valueOf(data.getReviewCount()));

        if(data.isLike()) {
            holder.like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_selected));
        } else {
            holder.like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_default));
        }


    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView storeImage;
        private TextView storeName;
        private TextView storeTag1;
        private TextView storeTag2;
        private TextView address;
        private RatingBar starRating;
        private TextView distance;
        private TextView reviewers;
        private ToggleButton like;
        StoreListData searchData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImage = itemView.findViewById(R.id.store_image);
            storeName = itemView.findViewById(R.id.map_card_store_name);
            storeTag1 = itemView.findViewById(R.id.store_tag1);
            storeTag2 = itemView.findViewById(R.id.store_tag2);
            address = itemView.findViewById(R.id.map_store_address);
            starRating = itemView.findViewById(R.id.map_ratingbar_star);
            distance = itemView.findViewById(R.id.store_distance);
            reviewers = itemView.findViewById(R.id.map_store_reviewes);
            like = itemView.findViewById(R.id.like_btn);
            itemView.setOnClickListener(this);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(searchData.isLike()){
                        Log.d("찜버튼되어있음", String.valueOf(searchData.isLike())+", "+searchData.getStoreId());
                        cancleLikeBtnListener.onCancleLiketButton(searchData.getStoreId());
                        searchData.setLike(false);
                        notifyDataSetChanged();
                    }else{
                        Log.d("찜버튼취소되어있었음", String.valueOf(searchData.isLike())+", "+searchData.getStoreId());
                        likeBtnListener.onLikeButton(searchData.getStoreId());
                        searchData.setLike(true);
                        notifyDataSetChanged();
                    }
                }
            });



        }

        @Override
        public void onClick(View view) {
            if (searchListner != null) {
//                searchListner.onSearchClick(view, searchData, getLayoutPosition());
                searchListner.onSearchClick(view, searchList.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

    public interface searchListner {
        void onSearchClick(View view, StoreListData searchData, int position);
    }
}
