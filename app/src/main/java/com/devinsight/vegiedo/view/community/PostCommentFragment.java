package com.devinsight.vegiedo.view.community;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.ReportRequestDTO;
import com.devinsight.vegiedo.data.response.CommentListData;
import com.devinsight.vegiedo.view.community.adapter.PostCommentAdapter;
import com.devinsight.vegiedo.view.community.viewmodel.PostCommentViewModel;
import com.devinsight.vegiedo.view.search.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


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
                    Log.d("액티비티 뷰모델의 댓글 리스트","리스트" + commentList.size());
                    adapter.setInitialCommentList(commentList);
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
                Log.d("코멘트 뷰모델의 댓글 리스트","리스트" + commentList.size());
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
        setReportDialog(comment.getCommentId());
    }

    @Override
    public void onCommentDeleteClicked(View view, CommentListData comment, int position) {
        Log.d("댓글 삭제 버튼 클릭!"," 댓글 삭제 버튼 클릭!");
        commentViewModel.deleteComment(comment.getCommentId());
    }

    public void setReportDialog(Long commentId){
        int layoutId;
        Long id = commentId;
        View dialogView;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());

        layoutId = R.layout.select_reporting_type_dialog;
        dialogView = inflater.inflate(layoutId, null);
        builder.setView(dialogView);
        AlertDialog reportDialog = builder.create();
        Log.d("AlertDialog", reportDialog.toString());
        setupReportDialog(dialogView, reportDialog, id);
        reportDialog.setContentView(R.layout.dialog_custom);
        reportDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명하게 설정
        reportDialog.show();
    }

    private void setupReportDialog(View dialogView, AlertDialog dialog, Long commentId) {

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
//                    Toast.makeText(getContext(), "최대 20자까지 입력 가능 합니다.", Toast.LENGTH_SHORT).show();
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

            ReportRequestDTO requestDTO = new ReportRequestDTO();
            requestDTO.setContentType("댓글");
            requestDTO.setTrollType(reportType.get());
            requestDTO.setMemo(opinion.get());
            commentViewModel.reportComment(commentId, requestDTO);
            dialog.dismiss(); // 다이얼로그 닫기
            Log.d("댓글 신고","댓글 신고" + reportType.get() + opinion.get());
        });

        closeIcon.setOnClickListener(v -> dialog.dismiss());

    }




}