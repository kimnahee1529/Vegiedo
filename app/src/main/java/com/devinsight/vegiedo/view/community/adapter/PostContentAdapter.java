package com.devinsight.vegiedo.view.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.ContentImage;

import java.util.List;

public class PostContentAdapter extends RecyclerView.Adapter<PostContentAdapter.ImageViewHolder> {

    private List<ContentImage> imageList;
    Context context;

    protected ImageClickListener imageClickListener;

    public void setImageList(List<ContentImage> list) {
        this.imageList.clear();
        this.imageList.addAll(list);

    }

    public PostContentAdapter(Context context, List<ContentImage> list, ImageClickListener imageClickListener){
        this.imageList = list;
        this.context = context;
        this.imageClickListener = imageClickListener;
    }

    @NonNull
    @Override
    public PostContentAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_image_rc_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostContentAdapter.ImageViewHolder holder, int position) {
        ContentImage data = imageList.get(position);
        String imageUrl = data.getImageUrl();
        if(imageUrl != null ) {
            Glide.with(context).load(imageUrl).into(holder.content_image);
        } else {
            holder.content_image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView content_image;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            content_image = itemView.findViewById(R.id.content_image);
        }

        @Override
        public void onClick(View view) {
            if(imageClickListener != null ){
                imageClickListener.onImageClicked(view, imageList.get(getLayoutPosition()), getLayoutPosition());
            }
        }
    }
    public interface ImageClickListener {
        void onImageClicked(View view, ContentImage image, int position);
    }
}
