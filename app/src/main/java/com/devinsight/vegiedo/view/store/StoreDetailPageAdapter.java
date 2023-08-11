package com.devinsight.vegiedo.view.store;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;

import java.util.List;

public class StoreDetailPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> itemList;

    public StoreDetailPageAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getItemType().ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == Item.ItemType.STORE_DETAIL_PAGE.ordinal()){
            View view = inflater.inflate(R.layout.store_detail_item, parent, false);
            return new ViewHolderTypeThree(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item item = itemList.get(position);
        if (holder instanceof ViewHolderTypeThree) {
            ((ViewHolderTypeThree) holder).title.setText(item.getTitle());
            ((ViewHolderTypeThree) holder).imageView1.setImageResource(item.getImageResource1());
            ((ViewHolderTypeThree) holder).imageView2.setImageResource(item.getImageResource2());
            ((ViewHolderTypeThree) holder).imageView3.setImageResource(item.getImageResource3());
            ((ViewHolderTypeThree) holder).imageView4.setImageResource(item.getImageResource4());
            ((ViewHolderTypeThree) holder).imageView5.setImageResource(item.getImageResource5());
            ((ViewHolderTypeThree) holder).star1.setImageResource(item.getImageResource6());
            ((ViewHolderTypeThree) holder).star2.setImageResource(item.getImageResource7());
            ((ViewHolderTypeThree) holder).star3.setImageResource(item.getImageResource8());
            ((ViewHolderTypeThree) holder).star4.setImageResource(item.getImageResource9());
            ((ViewHolderTypeThree) holder).star5.setImageResource(item.getImageResource10());

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    static class ViewHolderTypeThree extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        ImageView imageView5;
        ImageView star1;
        ImageView star2;
        ImageView star3;
        ImageView star4;
        ImageView star5;


        ViewHolderTypeThree(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title3);
            content = itemView.findViewById(R.id.content3);
            imageView1 = itemView.findViewById(R.id.store_image1);
            imageView2 = itemView.findViewById(R.id.store_image2);
            imageView3 = itemView.findViewById(R.id.store_image3);
            imageView4 = itemView.findViewById(R.id.store_image4);
            imageView5 = itemView.findViewById(R.id.store_image5);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);

        }
    }
}