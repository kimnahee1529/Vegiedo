package com.devinsight.vegiedo.view.community;

import android.app.AlertDialog;
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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.PostReportRequestDTO;
import com.devinsight.vegiedo.data.response.ContentImage;
import com.devinsight.vegiedo.data.response.PostInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.PostRecommendRequestDTO;
import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;
import com.devinsight.vegiedo.service.api.PostApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.utill.UserInfoTag;
import com.devinsight.vegiedo.view.MainActivity;
import com.devinsight.vegiedo.view.community.adapter.PostContentAdapter;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
    ImageView recommend_btn;
    ImageView btn_post_report;

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
    String token;

    boolean isLike;

    Fragment communityMainFragment;

    ActivityViewModel activityViewModel;
    PostApiService postApiService = RetrofitClient.getPostApiService();
    AuthPrefRepository authPrefRepository;
    UserPrefRepository userPrefRepository;

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

        authPrefRepository = new AuthPrefRepository(getActivity());
        userPrefRepository = new UserPrefRepository(getActivity());

        user_image = view.findViewById(R.id.user_image);
        post_title = view.findViewById(R.id.post_title);
        user_name = view.findViewById(R.id.user_name);
        created_time = view.findViewById(R.id.created_time);
        like_count = view.findViewById(R.id.post_list_like_count);
        post_content = view.findViewById(R.id.post_content);
        post_recommend_count = view.findViewById(R.id.post_recommend_count);
        post_content_recommend = view.findViewById(R.id.post_content_recommend);
        post_content_delete = view.findViewById(R.id.post_content_delete);
        post_content_modify = view.findViewById(R.id.post_content_modify);
        recommend_btn = view.findViewById(R.id.content_heart);
        btn_post_report = view.findViewById(R.id.btn_post_report);

        communityMainFragment = new CommunityMainFragment();

        recyclerView = view.findViewById(R.id.content_image_rc);
        imageList = new ArrayList<>();
        adaptper = new PostContentAdapter(getContext(), imageList, this);
        recyclerView.setAdapter(adaptper);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        String social;
        if( authPrefRepository.getAuthToken("KAKAO") != null) {
            social = "KAKAO";
        } else {
            social = "GOOGLE";
        }
        token = authPrefRepository.getAuthToken(social);

        String myName = userPrefRepository.loadUserInfo(UserInfoTag.USER_NICKNAME.getInfoType());

        if(Objects.equals(userName, myName)) {
            btn_post_report.setVisibility(View.INVISIBLE);
            post_content_delete.setVisibility(View.VISIBLE);
            post_content_modify.setVisibility(View.VISIBLE);
        } else {
            btn_post_report.setVisibility(View.VISIBLE);
            post_content_delete.setVisibility(View.INVISIBLE);
            post_content_modify.setVisibility(View.INVISIBLE);
        }



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

                post_title.setText(postData.getPostTitle());
                user_name.setText(postData.getUserName());
                created_time.setText(postData.getCreatedAt());
                like_count.setText(String.valueOf(postData.getLikeReceiveCnt()));
                post_recommend_count.setText(String.valueOf(postData.getLikeReceiveCnt()));
                Log.d("포스트 단일 조호 호출","포스트 단일 조회 호출" + postData.getContent());

                if(postData.isReport()){
                    btn_post_report.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setDialog("이미 신고를 완료한 게시글 입니다.");
                        }
                    });

                } else {
                    btn_post_report.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setReportDialog();
                        }
                    });
                }
            }
        });

        recommend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityViewModel.recommendPost(postId);

            }
        });

        activityViewModel.getPostLikeReceiveLiveData().observe(getViewLifecycleOwner(), new Observer<PostRecommendRequestDTO>() {
            @Override
            public void onChanged(PostRecommendRequestDTO recommendData) {
                int recommendCount = recommendData.getLikeReceiveCount();
                like_count.setText(String.valueOf(recommendCount));
                post_recommend_count.setText(String.valueOf(recommendCount));


//                if(recommendData.isLike()){
//                    setDialog("추천이 취소 되었습니다.");
//                    like_count.setText(String.valueOf(recommendCount - 1));
//                    post_recommend_count.setText(String.valueOf(recommendCount - 1));
//                } else {
//                    setDialog(" 이 게시글을 추천 했습니다!");
//                    like_count.setText(String.valueOf(recommendCount + 1));
//                    post_recommend_count.setText(String.valueOf(recommendCount + 1));
//                }
            }
        });

//        post_title.setText(postTitle);
//        user_name.setText(userName);
//        created_time.setText(createdAt);
//        like_count.setText(String.valueOf(likeReceiveCount));
//        post_recommend_count.setText(String.valueOf(likeReceiveCount));



        post_content_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDeleteDialog("글을 삭제 하시겠어요?", postId);
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



//        activityViewModel.getPostLikeReceiveLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer likeCount) {
//                post_recommend_count.setText(String.valueOf(likeCount));
//                like_count.setText(String.valueOf(likeCount));
//            }
//        });



//        btn_post_report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setReportDialog();
//            }
//        });

        return view;
    }

    @Override
    public void onImageClicked(View view, ContentImage image, int position) {

    }

    public void setDialog(String message) {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_custom);

        TextView msg = dialog.findViewById(R.id.dialog);
        msg.setText(message);
        dialog.show();
    }

    public void setReportDialog(){
        int layoutId;
        View dialogView;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());

        Log.d(" 신고 포스트 아이디 2","신고 포스트 아이디 2 " + postId);
        layoutId = R.layout.select_reporting_type_dialog;
        dialogView = inflater.inflate(layoutId, null);
        builder.setView(dialogView);
        AlertDialog reportDialog = builder.create();
        Log.d("AlertDialog", reportDialog.toString());
        setupReportDialog(dialogView, reportDialog);
        reportDialog.setContentView(R.layout.dialog_custom);
        reportDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명하게 설정
        reportDialog.show();
    }

    private void setupReportDialog(View dialogView, AlertDialog dialog) {

        ToggleButton report_type_btn1 = dialogView.findViewById(R.id.report_type_btn1);
        ToggleButton report_type_btn2 = dialogView.findViewById(R.id.report_type_btn2);
        ToggleButton report_type_btn3 = dialogView.findViewById(R.id.report_type_btn3);
        ToggleButton report_type_btn4 = dialogView.findViewById(R.id.report_type_btn4);
        EditText reasons_edit_text = dialogView.findViewById(R.id.other_reasons_text);
        TextView num_of_letter_text = dialogView.findViewById(R.id.num_of_letter);
        TextView reporting_btn = dialogView.findViewById(R.id.reporting_btn);

        //각 버튼 별 reportType(신고유형)과 기타에서의 option(기타 사유)
        AtomicReference<String> reportType = new AtomicReference<>("");
        AtomicReference<String> opinion = new AtomicReference<>("");

        ImageView closeIcon = dialogView.findViewById(R.id.green_x_circle);

        reasons_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                opinion.set(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if(length > 200){
                    editable.delete(200, length);
                    Toast.makeText(getContext(), "최대 20자까지 입력 가능 합니다.", Toast.LENGTH_SHORT).show();
                }

                String opinionLength = editable.length() + "자/200자";
                num_of_letter_text.setText(opinionLength);
            }
        });

        report_type_btn1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                report_type_btn2.setChecked(false);
                report_type_btn3.setChecked(false);
                report_type_btn4.setChecked(false);
                reasons_edit_text.setVisibility(View.INVISIBLE);
                num_of_letter_text.setVisibility(View.INVISIBLE);
                reportType.set("부적절한 사진 사용");
                opinion.set("");
            }
        });

        report_type_btn2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                report_type_btn1.setChecked(false);
                report_type_btn3.setChecked(false);
                report_type_btn4.setChecked(false);
                reasons_edit_text.setVisibility(View.INVISIBLE);
                num_of_letter_text.setVisibility(View.INVISIBLE);
                reportType.set("부적절한 언어 사용");
                opinion.set("");
            }
        });

        report_type_btn3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                report_type_btn1.setChecked(false);
                report_type_btn2.setChecked(false);
                report_type_btn4.setChecked(false);
                reasons_edit_text.setVisibility(View.INVISIBLE);
                num_of_letter_text.setVisibility(View.INVISIBLE);
                reportType.set("허위 정보 기재");
                opinion.set("");
            }
        });

        report_type_btn4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                report_type_btn1.setChecked(false);
                report_type_btn2.setChecked(false);
                report_type_btn3.setChecked(false);
                reasons_edit_text.setVisibility(View.VISIBLE);
                num_of_letter_text.setVisibility(View.VISIBLE);
                reportType.set("기타");
            }
        });

        // 완료 버튼을 눌렀을 때
        reporting_btn.setOnClickListener(v -> {

            PostReportRequestDTO requestDTO = new PostReportRequestDTO();
            requestDTO.setContentType("게시글");
            requestDTO.setTrollType(reportType.get());
            requestDTO.setMemo(opinion.get());
            activityViewModel.reportPost(postId, requestDTO);
            dialog.dismiss(); // 다이얼로그 닫기
            Log.d("게시글 신고","게시글 신고" + reportType.get() + opinion.get());
        });

        closeIcon.setOnClickListener(v -> dialog.dismiss());

    }

    public void setDeleteDialog(String message, Long postId) {
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

    public void setDeleteRecommendDialog(String message, Long postId) {
        Log.d(" 추천 삭제 포스트 아이디 2"," 추천 삭제 포스트 아이디 2 " + postId);
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