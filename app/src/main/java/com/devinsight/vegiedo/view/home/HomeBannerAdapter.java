package com.devinsight.vegiedo.view.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.PostListData;
import com.devinsight.vegiedo.data.ui.home.HomeBannerData;
import com.devinsight.vegiedo.view.search.SummaryData;

import java.util.List;

public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.ViewHolderPage>{

    private List<HomeBannerData> bannerList;
    Context context;

    BannerItemListener bannerItemListener;

    HomeBannerAdapter(Context context, List<HomeBannerData> bannerList, BannerItemListener bannerItemListener){
        this.bannerList = bannerList;
        this.context = context;
        this.bannerItemListener = bannerItemListener;
    }

    public void setBannerList(List<HomeBannerData> list){
        this.bannerList.clear();
        this.bannerList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_viewpager_item, parent, false);
        return new ViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPage holder, int position) {
        HomeBannerData data = bannerList.get(position);
        String imageUrl = data.getImages();
        Glide.with(context)
                .load(imageUrl)
                .into(holder.bannerImage);

    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }


    public class ViewHolderPage extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView bannerImage;
        private CardView cardView;

        HomeBannerData bannerData;
        public ViewHolderPage(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.home_banne_item);
            cardView = itemView.findViewById(R.id.card_banner);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(bannerItemListener != null ) {
                bannerItemListener.onBannerClicked(view, bannerList.get(getLayoutPosition()), getLayoutPosition());
            }
        }
    }

    public interface BannerItemListener {
        void onBannerClicked(View view, HomeBannerData homeBannerData, int position);
    }
}


