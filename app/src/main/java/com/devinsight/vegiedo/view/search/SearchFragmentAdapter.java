package com.devinsight.vegiedo.view.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.search.SearchStorSummaryeData;

import java.util.ArrayList;

public class SearchFragmentAdapter extends RecyclerView.Adapter<SearchFragmentAdapter.SearchViewHolder>{

    private ArrayList<SearchStorSummaryeData> storeList;
    Context context;

    protected searchItemListner searchItemListner;

    public SearchFragmentAdapter(Context context, ArrayList<SearchStorSummaryeData> storeList, searchItemListner searchItemListner){
        this.storeList = storeList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchFragmentAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_list_recyclerview_item,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFragmentAdapter.SearchViewHolder holder, int position) {
        holder.setSearchData(storeList.get(position));

    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView searchStoreImage;
        private TextView searchStoreName;
        private TextView searchStoreAddress;
        private View viewLine;
        SearchStorSummaryeData searchData;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            searchStoreImage = itemView.findViewById(R.id.search_store_image);
            searchStoreName = itemView.findViewById(R.id.search_store_name);
            searchStoreAddress = itemView.findViewById(R.id.search_store_address);
            viewLine = itemView.findViewById(R.id.view_line);
        }

        public void setSearchData(SearchStorSummaryeData searchData){
            this.searchData = searchData;

            searchStoreImage.setImageResource(searchData.getStoreImage());
            searchStoreName.setText(searchData.getStoreName());
            searchStoreAddress.setText(searchData.getStoreAddress());
        }

        @Override
        public void onClick(View view) {
            if(searchItemListner != null){
                searchItemListner.onSearchItemClick(searchData);
            }

        }
    }

    public interface searchItemListner{
        void onSearchItemClick(SearchStorSummaryeData searchData);
    }

}
