package com.devinsight.vegiedo.view.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.devinsight.vegiedo.R;


public class PostCommentFragment extends Fragment {

    TextView post_content_comment;
    EditText et_comment_input;
    Button btn_register;
    RecyclerView recyclerView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_main_comment, container, false);

       post_content_comment = view.findViewById(R.id.post_content_comment);
       et_comment_input = view.findViewById(R.id.et_comment_input);
       btn_register = view.findViewById(R.id.btn_register);
        return view;
    }
}