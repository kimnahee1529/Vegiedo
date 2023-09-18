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

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    ProgressBar progressBar;
    CommunityPostAdaptper adatper;
    List<PostListData> postList;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    Fragment postMainFragment;
    FrameLayout community_Frame;

    int cursor = 1;
    int maxCursor;

    boolean isScrollingUp;
    private long lastScrollEventTime = System.currentTimeMillis();
    private final int SCROLL_DEBOUNCE_TIME = 3000; // 3초


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        scrollView = view.findViewById(R.id.scrollView);
        community_banner = view.findViewById(R.id.community_banner);
        progressBar = view.findViewById(R.id.progressbar);
        recyclerView = view.findViewById(R.id.post_recyclerview);
        postList = new ArrayList<>();
        adatper = new CommunityPostAdaptper(getContext(), postList, this);
        recyclerView.setAdapter(adatper);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        postMainFragment = new PostMainFragment();

        fragmentManager = getChildFragmentManager();
        transaction = fragmentManager.beginTransaction();


        communityViewModel = new ViewModelProvider(this).get(CommunityViewModel.class);
        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        /* 유저 토큰 값 프래그먼트 뷰 모델에 전달 */
        activityViewModel.getToken().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String token) {
                communityViewModel.getToken(token);
            }
        });

//        /* nav 바를 통한 화면 이동 시 보여질 최초 일반 게시글 리스트 */
//        communityViewModel.loadGeneralPostList(cursor);
//        communityViewModel.getGeneralPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
//            @Override
//            public void onChanged(List<PostListData> generalPostListData) {
//                setPostList(generalPostListData);
//            }
//        });


        /* 게시글 유형 구분 후 리스트 호출 */
        activityViewModel.getPostType().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean postType) {
                if (postType) {
                    /* postType : true -> 일반 게시글 */
                    communityViewModel.loadGeneralPostList(cursor);
                    communityViewModel.getGeneralPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
                        @Override
                        public void onChanged(List<PostListData> generalPostListData) {
                            if (generalPostListData != null) {
                                setPostList(generalPostListData);
                            } else {
                                Log.d("CommunityPostListFragment", "list == null");
                            }
                        }
                    });

                } else {
                    /* postType : false -> 인기 게시글 */
                    communityViewModel.loadPopularPostList(cursor);
                    communityViewModel.getPopularPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
                        @Override
                        public void onChanged(List<PostListData> popularPostListData) {
                            if (popularPostListData != null) {
                                setPostList(popularPostListData);
                            } else {
                                Log.d("CommunityPostListFragment", "list == null");
                            }

                        }
                    });
                }
            }
        });




        /* 스크롤 위치에 따른 게시글 호출 */
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                /* 현재 Y 좌표가 이전 Y 좌표 보다 작다면 = 스크롤 내림*/
                isScrollingUp = scrollY < oldScrollY;
                int childHeight = v.getChildAt(0).getMeasuredHeight();
                int scrollViewHeight = v.getMeasuredHeight();

                long currentTime = System.currentTimeMillis();
                communityViewModel.getIsLastItem().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        Log.d("ScrollCheck", "Current Time: " + currentTime);
                        Log.d("ScrollCheck", "Last Scroll Time: " + lastScrollEventTime);
                        if(currentTime - lastScrollEventTime < SCROLL_DEBOUNCE_TIME) {
                            return;
                        }
                        lastScrollEventTime = currentTime;
                        Log.d("ScrollCheck", "Time Diff: " + (currentTime - lastScrollEventTime));
                    }
                });

                communityViewModel.getMaxCursorLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer maxCursor) {

                        if (scrollY == childHeight - scrollViewHeight && !isScrollingUp) {
                            if( cursor < maxCursor ){
                                cursor ++;
                                Log.d("cursor scroll ", "cursor up" + cursor);
                                addPostListFromApi(cursor);
                                progressBar.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getContext(), " 마지막 페이지 입니다 ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                        else if (isScrollingUp){
                            if (cursor > 1) {
                                cursor--;
                                Log.d("cursor scroll", "cursor down" + cursor);
                                previousPostListFromApi(cursor);
                            }

                        }

                    }
                });
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                long currentTime = System.currentTimeMillis();

                if(currentTime - lastScrollEventTime < SCROLL_DEBOUNCE_TIME) {
                    return; // Do not process if the time difference is less than debounce time.
                }

                lastScrollEventTime = currentTime;

                if (!recyclerView.canScrollVertically(1)) { // Check if reached the last item
                    Log.d("ScrollCheck", "Reached the last item");
                    communityViewModel.getLastItem(true);
                }
            }
        });




//        setCursor();

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
        bundle.putLong("postId", postList.get(position).getPostId());
        Log.d("클릭된 데이터 ", "클릭된 데이터" + postList.get(position).getPostTitle() + " : " + postList.get(position).getUserName() + " : " + postList.get(position).getPostId() + " : " + postList.get(position).getCommentCount() + " : " + postList.get(position).getLike() + " : " + postList.get(position).getCreatedAt());

        postContentFragment.setArguments(bundle);

        ((MainActivity) getActivity()).replaceFragment(postMainFragment);

        FragmentManager fragmentManager = ((MainActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.post_content_frame, postContentFragment).commit();

        activityViewModel.setClickedPostData(postListData);

    }

    public void openPostContent() {
        transaction.replace(R.id.frame, postMainFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* 리사이클러뷰에 리스트 설정 */
    public void setPostList(List<PostListData> list) {

        adatper.setPostList(list);
        adatper.notifyDataSetChanged();
    }

    /* 스크롤 위치에 따라 호출 된 게시글 목록을 기존 목록에 추가 */
    public void addPostListFromApi(List<PostListData> list) {

        adatper.addPostList(list);
    }

    public void previousPostListFromApi(List<PostListData> list) {

        adatper.previousPostList(list);
    }


    public void previousPostListFromApi(int currentCursor) {
        activityViewModel.getPostType().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean postType) {
                if (postType) {
                    communityViewModel.loadGeneralPostList(currentCursor);
                } else {
                    communityViewModel.loadPopularPostList(currentCursor);

                }
            }
        });
    }


    /* 스크롤 위치에 따른 게시글 목록 커서에 따라 호출 */
    public void addPostListFromApi(int currentCursor) {
        activityViewModel.getPostType().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean postType) {
                if (postType) {
                    communityViewModel.loadGeneralPostList(currentCursor);

                } else {
                    communityViewModel.loadPopularPostList(currentCursor);

                }
            }
        });
    }

    public void setCursor() {
        if (postList != null) {
            cursor = (postList.size() / 5) + 1;
        } else {
            cursor = 1;
        }

    }


}