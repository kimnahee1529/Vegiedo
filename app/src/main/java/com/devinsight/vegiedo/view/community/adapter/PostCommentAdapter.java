package com.devinsight.vegiedo.view.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.CommentListData;

import java.util.List;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.CommentViewHolder> {

    List<CommentListData> commentList;
    Context context;
    protected CommentClickListener commentClickListener;

    public void setCommentList(List<CommentListData> list ) {
        this.commentList.clear();
        this.commentList.addAll(list);
    }

    public PostCommentAdapter(Context context, List<CommentListData> list, CommentClickListener commentClickListener) {
        this.commentList = list;
        this.context = context;
        this.commentClickListener = commentClickListener;
    }

    @NonNull
    @Override
    public PostCommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_rc_item, parent, false);
        return new CommentViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PostCommentAdapter.CommentViewHolder holder, int position) {
        CommentListData data = commentList.get(position);
        holder.comment_user_name.setText(data.getUserName());
        holder.comment_created_at.setText(data.getCreatedAt());
        holder.comment_content.setText(data.getCommentContent());


    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView comment_user_name;
        TextView comment_created_at;
        TextView comment_content;
        ImageView btn_comment_report;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_user_name = itemView.findViewById(R.id.comment_user_name);
            comment_created_at = itemView.findViewById(R.id.comment_created_at);
            comment_content = itemView.findViewById(R.id.comment_content);
            btn_comment_report = itemView.findViewById(R.id.btn_comment_report);
        }

        @Override
        public void onClick(View view) {
            if(commentClickListener != null ) {
                commentClickListener.onCommentClicked(view, commentList.get(getLayoutPosition()), getLayoutPosition());
            }
        }
    }

    public interface CommentClickListener {
        void onCommentClicked(View view, CommentListData comment, int position);
    }
}
