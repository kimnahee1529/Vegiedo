package com.devinsight.vegiedo.view.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.view.search.ActivityViewModel;


public class PostMainFragment extends Fragment {

    ImageButton btn_back;
    TextView post_type;

    Fragment postContentFragment;
    Fragment postCommentFragment;

    Fragment communityMainFragment;

    FrameLayout post_content_frame;
    FrameLayout post_comment_frame;

    ActivityViewModel activityViewModel;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_content, container, false);
        btn_back = view.findViewById(R.id.btn_back);
        post_type = view.findViewById(R.id.post_type);
        post_content_frame = view.findViewById(R.id.post_content_frame);
        post_comment_frame = view.findViewById(R.id.post_comment_frame);

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        fragmentManager = getChildFragmentManager();
        transaction = fragmentManager.beginTransaction();

        postContentFragment = new PostContentFragment();
        postCommentFragment = new PostCommentFragment();
        communityMainFragment = new CommunityMainFragment();

        transaction.replace(R.id.post_content_frame, postContentFragment).addToBackStack(null);
        transaction.replace(R.id.post_comment_frame, postCommentFragment).addToBackStack(null).commit();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.frame, communityMainFragment).commit();
            }
        });

        activityViewModel.getPostType().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean postType) {
                if(postType) {
                    post_type.setText("일반 게시글");
                } else {
                    post_type.setText("인기 게시글");
                }
            }
        });




        return view;
    }
}