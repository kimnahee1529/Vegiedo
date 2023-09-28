package com.devinsight.vegiedo.view.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.PostListData;
import com.kakao.sdk.template.model.Content;

import java.util.List;
import java.util.Objects;

public class CommunityPostAdaptper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private List<PostListData> postList;
    Context context;
    protected PostItemListnere postItemListnere;

    public void setPostList(List<PostListData> list) {
//        this.postList.clear();
//        this.postList.addAll(list);
        this.postList.addAll(list);
        this.postList.add(new PostListData(null, "", " ", "", "",0, 0, 0)); // placeholder for progress bar
    }

    public void addPostList(List<PostListData> list) {
        int positionStart = postList.size();
        this.postList.addAll(list);
        notifyItemRangeInserted(positionStart, list.size());
    }

    public void previousPostList(List<PostListData> list) {
        this.postList.addAll(0, list);
        notifyItemRangeInserted(0, list.size());
    }

    public void setList(List<PostListData> list) {
        this.postList.clear();
        this.postList.addAll(list);
        this.postList.add(new PostListData(null, "", " ", "", "",0, 0, 0)); // placeholder for progress bar
        notifyItemRangeInserted(0, list.size()); // 이 부분을 추가하세요.
    }

    public CommunityPostAdaptper(Context context, List<PostListData> list, PostItemListnere postItemListnere) {
        this.postList = list;
        this.context = context;
        this.postItemListnere = postItemListnere;
    }
    @Override
    public int getItemViewType(int position) {
//        return Objects.equals(postList.get(position).getUserName(), " ") ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
//        return postList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        return " ".equals(postList.get(position).getUserName()) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post_recycler_item, parent, false);
            return new PostViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof  PostViewHolder) {
            PostListData data = postList.get(position);
            PostViewHolder postViewHolder = (PostViewHolder) holder;
            String imageUrl = data.getImageUrl();
            if (imageUrl != null) {
                Glide.with(context).load(imageUrl).into(postViewHolder.postImage);
            } else {
                postViewHolder.postImage.setVisibility(View.GONE);
                postViewHolder.postCard.setVisibility(View.GONE);
            }
            postViewHolder.postTitle.setText(data.getPostTitle());
            postViewHolder.postCommentCount.setText(String.valueOf("[" + data.getCommentCount() + "]"));
            postViewHolder.postUserName.setText(data.getUserName());
            postViewHolder.postCreatedAt.setText(String.valueOf(data.getCreatedAt()));
            postViewHolder.postLikeCount.setText(String.valueOf(data.getLike()));
        }


    }

    @Override
    public int getItemCount() {
        return postList == null ? 0 : postList.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressbar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressbar = itemView.findViewById(R.id.progressbar);
        }
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
            postCard = itemView.findViewById(R.id.post_card);
            postImage = itemView.findViewById(R.id.user_image);
            postTitle = itemView.findViewById(R.id.post_title);
            postCommentCount = itemView.findViewById(R.id.comment_count);
            postUserName = itemView.findViewById(R.id.user_name);
            postCreatedAt = itemView.findViewById(R.id.created_time);
            postLikeCount = itemView.findViewById(R.id.post_list_like_count);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            // Add this check
            if (position != RecyclerView.NO_POSITION && position < postList.size()) {
                if (postItemListnere != null) {
                    postItemListnere.onPostClicked(view, postList.get(position), position);
                }
            }
        }
    }

    public interface PostItemListnere {
        void onPostClicked(View view, PostListData postListData, int position);
    }

    public void deleteLoading(){
//        postList.remove(postList.size() - 1);
        int position = postList.size() - 1;
        if (position >= 0 && " ".equals(postList.get(position).getUserName())) {
            postList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
