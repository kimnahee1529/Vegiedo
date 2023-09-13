package com.devinsight.vegiedo.view.community;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
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
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.PostListData;
import com.devinsight.vegiedo.view.MainActivity;
import com.devinsight.vegiedo.view.community.adapter.CommunityPostAdaptper;
import com.devinsight.vegiedo.view.community.viewmodel.CommunityViewModel;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommunityPostListFragment extends Fragment implements CommunityPostAdaptper.PostItemListnere {

    ActivityViewModel activityViewModel;
    CommunityViewModel communityViewModel;
    ImageView community_banner;

    NestedScrollView scrollView;
    RecyclerView recyclerView;
    CommunityPostAdaptper adatper;
    List<PostListData> postList;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    Fragment postMainFragment;
    FrameLayout community_Frame;

    int cursor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        scrollView = view.findViewById(R.id.scrollView);
        community_banner = view.findViewById(R.id.community_banner);
        recyclerView = view.findViewById(R.id.post_recyclerview);
        postList = new ArrayList<>();
        adatper = new CommunityPostAdaptper(getContext(), postList, this);

        postMainFragment = new PostMainFragment();

        fragmentManager = getChildFragmentManager();
        transaction = fragmentManager.beginTransaction();


        communityViewModel = new ViewModelProvider(this).get(CommunityViewModel.class);
        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);


        cursor = 1 ;


        activityViewModel.getToken().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String token) {
                communityViewModel.getToken(token);
            }
        });

        communityViewModel.loadGeneralPostList(cursor);
        communityViewModel.getGeneralPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
            @Override
            public void onChanged(List<PostListData> generalPostListData) {
                setPostList(generalPostListData);
            }
        });

        activityViewModel.getPostType().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean postType) {
                if(postType){
                    communityViewModel.loadGeneralPostList(cursor);
                    communityViewModel.getGeneralPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
                        @Override
                        public void onChanged(List<PostListData> generalPostListData) {
                            if(generalPostListData != null ) {
                                setPostList(generalPostListData);
                            } else {
                                Log.d("CommunityPostListFragment","list == null");
                            }
                        }
                    });

                } else {
                    communityViewModel.loadPopularPostList(cursor);
                    communityViewModel.getPopularPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
                        @Override
                        public void onChanged(List<PostListData> popularPostListData) {
                            if(popularPostListData != null ) {
                                setPostList(popularPostListData);
                            } else {
                                Log.d("CommunityPostListFragment","list == null");
                            }

                        }
                    });
                }
            }
        });


        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int childHeight =  recyclerView.getMeasuredHeight();
                int scrollViewHeight = v.getMeasuredHeight();
                if (scrollY + scrollViewHeight >= childHeight) {
                    cursor ++;
                    Log.d("curor","cursur" + cursor);
//                    communityViewModel.getCursor(cursor);

                    getPostList(cursor);
                    Log.d("this id scroll Y : ","Y" + scrollY);
                    Log.d("this id scroll childHeight : ","childHeight" + childHeight);
                    Log.d("this id scroll scrollViewHeight : ","scrollViewHeight" + scrollViewHeight);

                }
            }
        });

        setCursor();

        return view;
    }

    @Override
    public void onPostClicked(View view, PostListData postListData, int position) {

        PostContentFragment postContentFragment = new PostContentFragment();

        Bundle bundle = new Bundle();
        bundle.putString("postTitle", postList.get(position).getPostTitle());
        bundle.putString("userName", postList.get(position).getUserName());
        bundle.putString("createdAt", postList.get(position).getCreatedAt());
        bundle.putInt("likeReceiveCount", postList.get(position).getLike());
        bundle.putInt("commentCount", postList.get(position).getCommentCount());
        Log.d("클릭된 데이터 ","클릭된 데이터" + postList.get(position).getPostTitle() + " : " +  postList.get(position).getUserName() + " : " +  postList.get(position).getPostId() + " : " +  postList.get(position).getCommentCount() + " : " +  postList.get(position).getLike() + " : " +  postList.get(position).getCreatedAt());

        postContentFragment.setArguments(bundle);

        ((MainActivity)getActivity()).replaceFragment(postMainFragment);

        FragmentManager fragmentManager = ((MainActivity)getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.post_content_frame, postContentFragment).commit();

        activityViewModel.setClickedPostData(postListData);

    }

    public void openPostContent(){
        transaction.replace(R.id.frame, postMainFragment);
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

    public void addPostList(List<PostListData> list) {
        recyclerView.setAdapter(adatper);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adatper.addPostList(list);
        adatper.notifyDataSetChanged();
    }



    public void setCursor(){
        if( postList != null) {
            cursor = ( postList.size() / 5) + 1;
        } else {
            cursor = 1;
        }
//        communityViewModel.getCursor(cursor);
    }

    public void getPostList( int cursor ){
        activityViewModel.getPostType().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean postType) {
                if(postType){
                    communityViewModel.loadGeneralPostList( cursor);
                    communityViewModel.getGeneralPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
                        @Override
                        public void onChanged(List<PostListData> generalPostListData) {
                            if(generalPostListData != null ) {
                                addPostList(generalPostListData);
                            } else {
                                Log.d("CommunityPostListFragment","list == null");
                            }
                        }
                    });

                } else {
                    communityViewModel.loadPopularPostList( cursor);
                    communityViewModel.getPopularPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
                        @Override
                        public void onChanged(List<PostListData> popularPostListData) {
                            if(popularPostListData != null ) {
                                addPostList(popularPostListData);
                            } else {
                                Log.d("CommunityPostListFragment","list == null");
                            }

                        }
                    });
                }
            }
        });
    }
}