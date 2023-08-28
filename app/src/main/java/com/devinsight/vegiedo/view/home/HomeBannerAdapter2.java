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

import java.util.List;

public class HomeBannerAdapter2 extends RecyclerView.Adapter<HomeBannerAdapter2.ViewHolderPage>{

    private List<HomeBannerData> bannerList;
    Context context;

    HomeBannerAdapter2(Context context, List<HomeBannerData> bannerList){
        this.bannerList = bannerList;
        this.context = context;
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


    public static class ViewHolderPage extends RecyclerView.ViewHolder{

        private ImageView bannerImage;
        private CardView cardView;

        HomeBannerData bannerData;
        public ViewHolderPage(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.home_banne_item);
            cardView = itemView.findViewById(R.id.card_banner);
        }

    }
}


