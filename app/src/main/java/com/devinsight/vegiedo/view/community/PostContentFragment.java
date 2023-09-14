package com.devinsight.vegiedo.view.community;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.ContentImage;
import com.devinsight.vegiedo.data.response.PostInquiryResponseDTO;
import com.devinsight.vegiedo.view.MainActivity;
import com.devinsight.vegiedo.view.community.adapter.PostContentAdapter;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class PostContentFragment extends Fragment implements PostContentAdapter.ImageClickListener {

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

    PostContentAdapter adaptper;
    RecyclerView recyclerView;
    List<ContentImage> imageList;

    private Dialog dialog;

    String postTitle;
    String userName;
    String createdAt;
    int likeReceiveCount;
    int commentCount;
    Long postId;

    Fragment communityMainFragment;

    ActivityViewModel activityViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postTitle = getArguments().getString("postTitle");
            userName = getArguments().getString("userName");
            createdAt = getArguments().getString("createdAt");
            likeReceiveCount = getArguments().getInt("likeReceiveCount", 0);
            commentCount = getArguments().getInt("commentCount", 0);
            postId = getArguments().getLong("postId",0);
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

        communityMainFragment = new CommunityMainFragment();

        recyclerView = view.findViewById(R.id.content_image_rc);
        imageList = new ArrayList<>();
        adaptper = new PostContentAdapter(getContext(), imageList, this);
        recyclerView.setAdapter(adaptper);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        activityViewModel.getPostData();
        activityViewModel.getPostContentLiveData().observe(getViewLifecycleOwner(), new Observer<PostInquiryResponseDTO>() {
            @Override
            public void onChanged(PostInquiryResponseDTO postData) {
                if(postData.getImages() != null) {
                    List<String> list =  postData.getImages();
                    List<ContentImage> imageList = new ArrayList<>();
                    for( int i = 0 ; i < list.size() ; i ++ ){
                        ContentImage contentImage = new ContentImage();
                        contentImage.setImageUrl(list.get(i));
                        imageList.add(contentImage);
                    }
                    adaptper.setImageList(imageList);
                    Log.d("clicked post : ","this is image list " + imageList.size());
                    adaptper.notifyDataSetChanged();
                }

                String userImageUrl = postData.getUserImageUrl();
                if( userImageUrl != null ){
                    Glide.with(getContext()).load(userImageUrl).into(user_image);
                } else {
                    user_image.setImageResource(R.drawable.sheep_profile);
                }
                post_content.setText(postData.getContent());

                Log.d("포스트 단일 조호 호출","포스트 단일 조회 호출" + postData.getContent());
            }
        });

        post_title.setText(postTitle);
        user_name.setText(userName);
        created_time.setText(createdAt);
        like_count.setText(String.valueOf(likeReceiveCount));
        post_recommend_count.setText(String.valueOf(likeReceiveCount));



        post_content_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog("글을 삭제 하시겠어요?", postId);
            }
        });

        post_content_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WritingFragment writingFragment = new WritingFragment();

                Bundle bundle = new Bundle();
                bundle.putString("postTitle", postTitle);
                bundle.putString("postContent", post_content.getText().toString());
                for(int i = 0 ; i < imageList.size() ; i ++ ) {
                    bundle.putString("imageList", imageList.get(i).getImageUrl());
                    Log.d("imageList 1","imageList num is : " + i + imageList.get(i).getImageUrl());
                    Log.d("imageList 2","imageList size is : " + imageList.size());
                }
                bundle.putInt("imageListSize", imageList.size());
                bundle.putBoolean("isModify", true);
                bundle.putLong("postId", postId);

                writingFragment.setArguments(bundle);

                List<String >imageListForModify = new ArrayList<>();
                for( int j = 0 ; j < imageList.size() ; j ++ ) {
                    imageListForModify.add(imageList.get(j).getImageUrl());
                }

                activityViewModel.setImageUrlForModify(imageListForModify);

                ((MainActivity) getActivity()).replaceFragment(writingFragment);

                FragmentManager fragmentManager = ((MainActivity) getActivity()).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame, writingFragment).commit();
            }
        });

        return view;
    }

    @Override
    public void onImageClicked(View view, ContentImage image, int position) {

    }

    public void setDialog(String message, Long postId) {
        Log.d(" 삭제 포스트 아이디 2","삭제 포스트 아이디 2 " + postId);
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.delete_dialog);

        Button yesDelete = dialog.findViewById(R.id.yes);
        Button noDelete = dialog.findViewById(R.id.no);
        TextView msg = dialog.findViewById(R.id.dialog);
        msg.setText(message);

        yesDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityViewModel.deletePost(postId);
                getParentFragmentManager().beginTransaction().replace(R.id.frame, communityMainFragment).commit();
                Log.d(" 삭제 포스트 아이디 3","삭제 포스트 아이디3 " + postId);
                dialog.dismiss();
            }
        });

        noDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}