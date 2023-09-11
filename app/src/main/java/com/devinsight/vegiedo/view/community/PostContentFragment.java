package com.devinsight.vegiedo.view.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devinsight.vegiedo.R;

public class PostContentFragment extends Fragment {

    ImageView user_image;
    TextView post_title;
    TextView user_name;
    TextView created_time;
    TextView like_count;
    TextView post_content;
    TextView post_recommend_count;
    TextView post_content_recommend;
    TextView post_content_delete;
    TextView post_content_modify;
    RecyclerView content_rc;

    String postTitle;
    String userName;
    String createdAt;
    int likeReceiveCount;
    int commentCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postTitle = getArguments().getString("postTitle");
            userName = getArguments().getString("userName");
            createdAt = getArguments().getString("createdAt");
            likeReceiveCount = getArguments().getInt("likeReceiveCount", 0);
            commentCount = getArguments().getInt("commentCount", 0);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_post, container, false);

        user_image = view.findViewById(R.id.user_image);
        post_title = view.findViewById(R.id.post_title);
        user_name = view.findViewById(R.id.user_name);
        created_time = view.findViewById(R.id.created_time);
        like_count = view.findViewById(R.id.like_count);
        post_content = view.findViewById(R.id.post_content);
        post_recommend_count = view.findViewById(R.id.post_recommend_count);
        post_content_recommend = view.findViewById(R.id.post_content_recommend);
        post_content_delete = view.findViewById(R.id.post_content_delete);
        post_content_modify = view.findViewById(R.id.post_content_modify);

        post_title.setText(postTitle);
        user_name.setText(userName);
        created_time.setText(createdAt);
        like_count.setText(likeReceiveCount);
        post_content_recommend.setText(likeReceiveCount);



        return view;
    }
}