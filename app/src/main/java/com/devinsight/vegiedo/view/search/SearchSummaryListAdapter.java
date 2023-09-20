package com.devinsight.vegiedo.view.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.ui.search.SearchStorSummaryeUiData;

import java.util.List;

public class SearchSummaryListAdapter extends RecyclerView.Adapter<SearchSummaryListAdapter.SummaryViewHolder> {

    private List<SummaryData> summaryDataList;
    Context context;

    protected searchSummaryItemListner searchItemListner;

    public void setSearchList(List<SummaryData> list){
        this.summaryDataList.clear();
        this.summaryDataList.addAll(list);
    }

    public SearchSummaryListAdapter(Context context, List<SummaryData> summaryDataList, searchSummaryItemListner searchItemListner) {
        this.summaryDataList = summaryDataList;
        this.context = context;
        this.searchItemListner = searchItemListner;
    }


    @NonNull
    @Override
    public SearchSummaryListAdapter.SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_list_recyclerview_item,parent,false);
        return new SummaryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchSummaryListAdapter.SummaryViewHolder holder, int position) {
        SummaryData data = summaryDataList.get(position);
        holder.data = data;
        String imageUrl = data.getStoreImage();
        holder.searchStoreName.setText(data.getStoreName());
        holder.searchStoreAddress.setText(data.getStoreAddress());
        Glide.with(context).load(imageUrl).into(holder.searchStoreImage);


    }

    @Override
    public int getItemCount() {
        return summaryDataList.size();
    }

    public class SummaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView searchStoreImage;
        private TextView searchStoreName;
        private TextView searchStoreAddress;
        private View viewLine;
        SummaryData data;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            searchStoreImage = itemView.findViewById(R.id.search_store_image);
            searchStoreName = itemView.findViewById(R.id.search_store_name);
            searchStoreAddress = itemView.findViewById(R.id.search_store_address);
            viewLine = itemView.findViewById(R.id.view_line);
        }

        @Override
        public void onClick(View view) {
            if(searchItemListner != null){
                searchItemListner.onSearchSummaryItemClick(view, data, getLayoutPosition());
            }
        }

    }

    public interface searchSummaryItemListner{
        void onSearchSummaryItemClick(View view, SummaryData searchData, int position);
    }

}
