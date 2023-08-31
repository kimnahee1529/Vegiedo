package com.devinsight.vegiedo.view.search;

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
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.StoreListData;
import com.devinsight.vegiedo.data.ui.map.MapStoreCardUiData;
import com.devinsight.vegiedo.data.ui.search.SearchStorSummaryeUiData;

import java.util.List;

public class StoreDetailListAdapter extends RecyclerView.Adapter<StoreDetailListAdapter.ViewHolder> {

    private List<StoreListData> searchList;
    Context context;
    protected searchListner searchListner;

    public StoreDetailListAdapter(Context cotnext, List<StoreListData> searchList, searchListner searchlistner){
        this.searchList = searchList;
        this.context = cotnext;
    }

    public void setStoreList(List<StoreListData> storeList){
        this.searchList.clear();
        this.searchList.addAll(storeList);
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
        holder.storeName.setText(data.getStoreName());
        holder.storeTag1.setText(data.getTags().get(0));
        holder.storeTag2.setText(data.getTags().get(1));
        holder.address.setText(data.getAddress());
        holder.starRating.setRating(data.getStars());
        if(data.getDistance() < 999f ){
            holder.distance.setText(String.valueOf(data.getDistance()+"m"));
        } else {
            holder.distance.setText(String.valueOf(data.getDistance()+"km"));
        }

        holder.reviewers.setText(String.valueOf(data.getReviewCount()));

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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

        }

        @Override
        public void onClick(View view) {
            if(searchListner != null) {
                searchListner.onSearchClick(view, searchData, getLayoutPosition());
            }
        }
    }

    public interface searchListner{
        void onSearchClick(View view, StoreListData searchData, int position);
    }
}
