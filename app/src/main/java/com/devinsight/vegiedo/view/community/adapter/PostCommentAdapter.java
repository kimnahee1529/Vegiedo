package com.devinsight.vegiedo.view.community.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.CommentListData;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;
import com.devinsight.vegiedo.utill.UserInfoTag;

import java.util.List;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.CommentViewHolder> {

    List<CommentListData> commentList;
    Context context;
    protected CommentReportClickListener commentReportClickListener;
    protected CommentDeleteClickListener commentDeleteClickListener;

    UserPrefRepository userPrefRepository;

    public void setInitialCommentList(List<CommentListData> list ) {
        this.commentList.clear();
        this.commentList.addAll(list);
    }
    public void setCommentList(List<CommentListData> list ) {
        this.commentList.clear();
        this.commentList.addAll(list);
    }

    public PostCommentAdapter(Context context, List<CommentListData> list, CommentReportClickListener commentReportClickListener, CommentDeleteClickListener commentDeleteClickListener) {
        this.commentList = list;
        this.context = context;
        this.commentReportClickListener = commentReportClickListener;
        this.commentDeleteClickListener = commentDeleteClickListener;
        this.userPrefRepository = new UserPrefRepository(context);

    }

    @NonNull
    @Override
    public PostCommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_rc_item, parent, false);
        return new CommentViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PostCommentAdapter.CommentViewHolder holder, int position) {
        String myName = userPrefRepository.loadUserInfo(UserInfoTag.USER_NICKNAME.getInfoType());
        Log.d("유저 이름","" + myName);

        CommentListData data = commentList.get(position);
        holder.comment_user_name.setText(data.getUserName());
        holder.comment_created_at.setText(data.getCreatedAt());
        holder.comment_content.setText(data.getCommentContent());

//TODO 아래 부분 주석 해제 하기.

//        if(data.getUserName().equals(myName)){
//            holder.btn_comment_report.setVisibility(View.INVISIBLE);
//            holder.comment_delete.setVisibility(View.VISIBLE);
//        } else {
//            holder.btn_comment_report.setVisibility(View.VISIBLE);
//            holder.comment_delete.setVisibility(View.INVISIBLE);
//        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView comment_user_name;
        TextView comment_created_at;
        TextView comment_content;
        TextView comment_delete;
        ImageView btn_comment_report;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_user_name = itemView.findViewById(R.id.comment_user_name);
            comment_created_at = itemView.findViewById(R.id.comment_created_at);
            comment_content = itemView.findViewById(R.id.comment_content);
            comment_delete = itemView.findViewById(R.id.comment_delete);
            btn_comment_report = itemView.findViewById(R.id.btn_comment_report);

            comment_delete.setOnClickListener(this);
            btn_comment_report.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.comment_delete) {
                if (commentDeleteClickListener != null) {
                    commentDeleteClickListener.onCommentDeleteClicked(view, commentList.get(getLayoutPosition()), getLayoutPosition());
                }
            } else if (view.getId() == R.id.btn_comment_report) {
                if (commentReportClickListener != null) {
                    commentReportClickListener.onCommentReportClicked(view, commentList.get(getLayoutPosition()), getLayoutPosition());
                }
            }
        }
    }

    public interface CommentReportClickListener {
        void onCommentReportClicked(View view, CommentListData comment, int position);
    }

    public interface CommentDeleteClickListener{
        void onCommentDeleteClicked(View view, CommentListData comment, int position);
    }
}
