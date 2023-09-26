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

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.PostListData;
import com.devinsight.vegiedo.service.api.PostApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
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
    int popCursor = 1;
    int maxCursor;

    boolean isScrollingUp;
    private long lastScrollEventTime = System.currentTimeMillis();
    private final int SCROLL_DEBOUNCE_TIME = 5000; // 3초


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        scrollView = view.findViewById(R.id.scrollView);
        community_banner = view.findViewById(R.id.community_banner);
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
//                                setPostList(generalPostListData);
                                adatper.setList(generalPostListData);
                                adatper.notifyItemRangeInserted((cursor - 1) * 10, 10);

                            } else {
                                Log.d("CommunityPostListFragment", "list == null");
                            }
                        }
                    });

                } else {
                    /* postType : false -> 인기 게시글 */
                    communityViewModel.loadPopularPostList(popCursor);
                    communityViewModel.getPopularPostList().observe(getViewLifecycleOwner(), new Observer<List<PostListData>>() {
                        @Override
                        public void onChanged(List<PostListData> popularPostListData) {
                            if (popularPostListData != null) {
                                setPostList(popularPostListData);
                                adatper.setList(popularPostListData);
                                adatper.notifyItemRangeInserted((popCursor - 1) * 10, 10);
                            } else {
                                Log.d("CommunityPostListFragment", "list == null");
                            }

                        }
                    });
                }
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
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    // 마지막 아이템에 도달했을 때, 다음 페이지의 데이터 로드
                    // loadMoreData(++currentPage);
                    adatper.deleteLoading();
                    communityViewModel.getMaxCursorLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer maxCursor) {

                            activityViewModel.getPostType().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean postType) {
                                    if (postType) {
                                        if (cursor < maxCursor) {
                                            cursor++;
                                            Log.d("cursor scroll ", "cursor up" + cursor);
                                            addPostListFromApi(cursor);
                                        } else {
                                            Toast.makeText(getContext(), " 마지막 페이지 입니다 ", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (popCursor < maxCursor) {
                                            popCursor++;
                                            Log.d("cursor scroll ", "cursor up" + popCursor);
                                            addPostListFromApi(popCursor);
                                        } else {
                                            Toast.makeText(getContext(), " 마지막 페이지 입니다 ", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                }
                            });

                        }
                    });

                }
            }
        });

        activityViewModel.getCommunityBanner();
        activityViewModel.getCommunityBannerListLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String bannerUrl) {
                Glide.with(getActivity()).load(bannerUrl).into(community_banner);
            }
        });



        /* 스크롤 위치에 따른 게시글 호출 */
//        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                /* 현재 Y 좌표가 이전 Y 좌표 보다 작다면 = 스크롤이 위로 올라감*/
//                /* scrollY 값은 화면의 상단 에서 시작 하여 아래로 스크롤 될 때 커짐 */
//                isScrollingUp = scrollY < oldScrollY;
//                int childHeight = v.getChildAt(0).getMeasuredHeight();
//                int scrollViewHeight = v.getMeasuredHeight();
//
//                long currentTime = System.currentTimeMillis();
//                communityViewModel.getIsLastItem().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//                    @Override
//                    public void onChanged(Boolean aBoolean) {
//                        Log.d("ScrollCheck", "Current Time: " + currentTime);
//                        Log.d("ScrollCheck", "Last Scroll Time: " + lastScrollEventTime);
//                        if (currentTime - lastScrollEventTime < SCROLL_DEBOUNCE_TIME) {
//                            return;
//                        }
//                        lastScrollEventTime = currentTime;
//                        Log.d("ScrollCheck", "Time Diff: " + (currentTime - lastScrollEventTime));
//                    }
//                });
//
//                communityViewModel.getMaxCursorLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//                    @Override
//                    public void onChanged(Integer maxCursor) {
//
//                        if (scrollY == childHeight - scrollViewHeight && !isScrollingUp) {
//                            if (cursor < maxCursor) {
//                                cursor++;
//                                Log.d("cursor scroll ", "cursor up" + cursor);
//                                addPostListFromApi(cursor);
//                                progressBar.setVisibility(View.VISIBLE);
//                            } else {
//                                Toast.makeText(getContext(), " 마지막 페이지 입니다 ", Toast.LENGTH_SHORT).show();
//                                progressBar.setVisibility(View.INVISIBLE);
//                            }
//
//                        } else if (isScrollingUp) {
//                            if (cursor > 1) {
//                                cursor--;
//                                Log.d("cursor scroll", "cursor down" + cursor);
//                                previousPostListFromApi(cursor);
//                            }
//
//                        }
//
//                    }
//                });
//            }
//        });


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

        PostCommentFragment postCommentFragment = new PostCommentFragment();

        Bundle commentBundle = new Bundle();
        commentBundle.putLong("postIdForComment", postList.get(position).getPostId());

        postCommentFragment.setArguments(commentBundle);

        ((MainActivity) getActivity()).replaceFragment(postMainFragment);

        FragmentManager fragmentManager = ((MainActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.post_content_frame, postContentFragment);
        transaction.replace(R.id.post_comment_frame, postCommentFragment).commit();

        activityViewModel.setClickedPostData(postListData);

    }

    /* 리사이클러뷰에 리스트 설정 */
    public void setPostList(List<PostListData> list) {
        adatper.setPostList(list);
        adatper.notifyDataSetChanged();
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

}