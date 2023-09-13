package com.devinsight.vegiedo.view.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.PostListData;

import java.util.List;

public class CommunityPostAdaptper extends RecyclerView.Adapter<CommunityPostAdaptper.PostViewHolder> {

    private List<PostListData> postList;
    Context context;
    protected PostItemListnere postItemListnere;

    public void setPostList(List<PostListData> list){
        this.postList.clear();
        this.postList.addAll(list);
    }

    public void addPostList(List<PostListData> list){
//        if( list.size() >= 20 ) {
////            this.postList.clear();
//        } else {
            notifyItemRangeChanged(postList.size() - 1, 5 );
//        }

        this.postList.addAll(list);
    }

    public CommunityPostAdaptper(Context context, List<PostListData> list, PostItemListnere postItemListnere){
        this.postList = list;
        this.context = context;
        this.postItemListnere = postItemListnere;
    }

    @NonNull
    @Override
    public CommunityPostAdaptper.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post_recycler_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityPostAdaptper.PostViewHolder holder, int position) {
        PostListData data = postList.get(position);
        String imageUrl = data.getImageUrl();
        if(imageUrl != null ) {
            Glide.with(context).load(imageUrl).into(holder.postImage);
        } else {
            holder.postImage.setVisibility(View.GONE);
        }
        holder.postTitle.setText(data.getPostTitle());
        holder.postCommentCount.setText(String.valueOf("[" + data.getCommentCount() + "]" ));
        holder.postUserName.setText(data.getUserName());
        holder.postCreatedAt.setText(String.valueOf(data.getCreatedAt()));
        holder.postLikeCount.setText(String.valueOf(data.getLike()));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView postImage;
        private CardView postCard;
        private TextView postTitle;
        private TextView postCommentCount;
        private TextView postUserName;
        private TextView postCreatedAt;
        private TextView postLikeCount;

        PostListData postListData;



        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
//            postCard = itemView.findViewById(R.id.post_card);
            postImage = itemView.findViewById(R.id.user_image);
            postTitle = itemView.findViewById(R.id.post_title);
            postCommentCount = itemView.findViewById(R.id.comment_count);
            postUserName = itemView.findViewById(R.id.user_name);
            postCreatedAt = itemView.findViewById(R.id.created_time);
            postLikeCount = itemView.findViewById(R.id.like_count);
        }

        @Override
        public void onClick(View view) {
            if(postItemListnere != null){
                postItemListnere.onPostClicked(view, postList.get(getLayoutPosition()), getLayoutPosition());
            }

        }
    }

    public interface PostItemListnere {
        void onPostClicked(View view, PostListData postListData, int position);
    }
}
