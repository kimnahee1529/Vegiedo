package com.devinsight.vegiedo.view.community;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.PostListData;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommunityMainFragment extends Fragment implements CommunityPostAdatper.PostItemListner {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    TextView general_post;
    TextView popular_post;
    Button btn_writing;
    ImageView community_banner;
    RecyclerView recyclerView;
    CommunityPostAdatper adatper;
    List<PostListData> postList;
    ActivityViewModel activityViewModel;
    CommunityViewModel communityViewModel;

    Fragment postContentFragment;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;


    public CommunityMainFragment() {
        // Required empty public constructor
    }

    public static CommunityMainFragment newInstance(String param1, String param2) {
        CommunityMainFragment fragment = new CommunityMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_community, container, false);
        general_post = view.findViewById(R.id.general_posts);
        popular_post = view.findViewById(R.id.popular_posts);
        btn_writing = view.findViewById(R.id.writing_btn);
        community_banner = view.findViewById(R.id.community_banner);
        recyclerView = view.findViewById(R.id.post_recyclerview);
        postList = new ArrayList<>();
        adatper = new CommunityPostAdatper(getActivity(), postList, this);

        postContentFragment = new PostContentFragment();
        fragmentManager = getChildFragmentManager();
        transaction = fragmentManager.beginTransaction();


        communityViewModel = new ViewModelProvider(this).get(CommunityViewModel.class);
        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        activityViewModel.getToken().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String token) {
                communityViewModel.getToken(token);
            }
        });

        communityViewModel.loadGeneralPostList();
        communityViewModel.getGeneralPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
            @Override
            public void onChanged(List<PostListData> generalPostListData) {
                setPostList(generalPostListData);
            }
        });


        general_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                community_banner.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                setGeneralFontStyle();
                communityViewModel.loadGeneralPostList();
                communityViewModel.getGeneralPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
                    @Override
                    public void onChanged(List<PostListData> generalPostListData) {
                        setPostList(generalPostListData);
                    }
                });
            }
        });

        popular_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                community_banner.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                setPopuarFontStyle();
                communityViewModel.loadPopularPostList();
                communityViewModel.getPopularPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
                    @Override
                    public void onChanged(List<PostListData> popularPostListData) {
                        setPostList(popularPostListData);
                    }
                });

            }
        });

        btn_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
    @Override
    public void onPostClicked(View view, PostListData postListData, int position) {

        openPostContent();
    }

    public void openPostContent(){
        community_banner.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        btn_writing.setVisibility(View.GONE);
        transaction.replace(R.id.community_frame, postContentFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /* 리사이클러뷰에 리스트 설정 */
    public void setPostList(List<PostListData> list) {
        recyclerView.setAdapter(adatper);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adatper.setPostList(list);
        adatper.notifyDataSetChanged();
    }
    /* 일반 게시글 폰트 설정 */
    public void setGeneralFontStyle() {
        general_post.setTypeface(general_post.getTypeface(), Typeface.BOLD);
        general_post.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        popular_post.setTypeface(popular_post.getTypeface(), Typeface.NORMAL);
        popular_post.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
    }
    /* 인기 게시글 폰트 설정 */
    public void setPopuarFontStyle() {
        popular_post.setTypeface(popular_post.getTypeface(), Typeface.BOLD);
        popular_post.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        general_post.setTypeface(general_post.getTypeface(), Typeface.NORMAL);
        general_post.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
    }


}