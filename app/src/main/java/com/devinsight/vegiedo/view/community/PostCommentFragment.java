package com.devinsight.vegiedo.view.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.CommentListData;
import com.devinsight.vegiedo.view.community.adapter.PostCommentAdapter;
import com.devinsight.vegiedo.view.community.viewmodel.PostCommentViewModel;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;


public class PostCommentFragment extends Fragment implements PostCommentAdapter.CommentReportClickListener, PostCommentAdapter.CommentDeleteClickListener {

    TextView post_content_comment;
    EditText et_comment_input;
    Button btn_register;

    RecyclerView recyclerView;
    PostCommentAdapter adapter;
    List<CommentListData> commentList;

    ActivityViewModel activityViewModel;
    PostCommentViewModel commentViewModel;

    Long postId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getLong("postIdForComment",0);
            Log.d("포스트 아이디 코멘트","포스트 아이디 코멘트" + postId);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_comment, container, false);

        post_content_comment = view.findViewById(R.id.post_content_comment);
        et_comment_input = view.findViewById(R.id.et_comment_input);
        btn_register = view.findViewById(R.id.btn_register);

        recyclerView = view.findViewById(R.id.post_content_comment_rc);
        commentList = new ArrayList<>();
        adapter = new PostCommentAdapter(getContext(), commentList, this, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        commentViewModel = new ViewModelProvider(this).get(PostCommentViewModel.class);

        activityViewModel.getToken().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String token) {
                commentViewModel.getToken(token);
            }
        });
        activityViewModel.getPostIdLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long postId) {
                commentViewModel.getPostId(postId);
                Log.d("포스트 아이디", "포스트 아이디" + postId);
            }
        });

        activityViewModel.getPostCommentLiveData().observe(getViewLifecycleOwner(), new Observer<List<CommentListData>>() {
            @Override
            public void onChanged(List<CommentListData> commentList) {
                if(commentList != null ) {
                    adapter.setCommentList(commentList);
                    adapter.notifyDataSetChanged();
                    post_content_comment.setText("댓글 " + commentList.size());
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = et_comment_input.getText().toString();
                commentViewModel.getCommentContent(comment);
                et_comment_input.setText("");


            }
        });

        commentViewModel.getCommentListLiveData().observe(getViewLifecycleOwner(), new Observer<List<CommentListData>>() {
            @Override
            public void onChanged(List<CommentListData> commentList) {
                adapter.setCommentList(commentList);
                adapter.notifyDataSetChanged();
                post_content_comment.setText("댓글 " + commentList.size());

            }
        });


        return view;
    }

    @Override
    public void onCommentReportClicked(View view, CommentListData comment, int position) {
        Log.d("댓글 신고 버튼 클릭!"," 댓글 신고 버튼 클릭!");


    }

    @Override
    public void onCommentDeleteClicked(View view, CommentListData comment, int position) {
        Log.d("댓글 삭제 버튼 클릭!"," 댓글 삭제 버튼 클릭!");
        commentViewModel.deleteComment(postId, comment.getCommentId());
    }
}