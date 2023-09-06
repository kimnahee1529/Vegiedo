package com.devinsight.vegiedo.view.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;

public class CommunityMainActivity extends AppCompatActivity {

    TextView general_post;
    TextView popular_post;
    Button btn_writing;
    Context context;
    CommunityViewModel communityViewModel;
    AuthPrefRepository authPrefRepository;

    Fragment communityMainFragment;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_community);

        general_post = findViewById(R.id.general_posts);
        popular_post = findViewById(R.id.popular_posts);
        btn_writing = findViewById(R.id.writing_btn);

        communityMainFragment = new CommunityMainFragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        communityViewModel = new ViewModelProvider(this).get(CommunityViewModel.class);

        authPrefRepository = new AuthPrefRepository(context);
        String social;
        if(authPrefRepository.getAuthToken("KAKAO") == null){
            social = "GOOGLE";
        } else {
            social = "KAKAO";
        }
        String token = authPrefRepository.getAuthToken(social);
        communityViewModel.getToken(token);



        general_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGeneralFontStyle();
                loadPostList();
            }
        });

        popular_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPopuarFontStyle();
                loadPostList();
            }
        });

    }
    public void setGeneralFontStyle() {
        general_post.setTypeface(general_post.getTypeface(), Typeface.BOLD);
        general_post.setTextColor(ContextCompat.getColor(this, R.color.black));
        popular_post.setTypeface(popular_post.getTypeface(), Typeface.NORMAL);
        popular_post.setTextColor(ContextCompat.getColor(this, R.color.grey));
    }
    /* 인기 게시글 폰트 설정 */
    public void setPopuarFontStyle() {
        popular_post.setTypeface(popular_post.getTypeface(), Typeface.BOLD);
        popular_post.setTextColor(ContextCompat.getColor(this, R.color.black));
        general_post.setTypeface(general_post.getTypeface(), Typeface.NORMAL);
        general_post.setTextColor(ContextCompat.getColor(this, R.color.grey));
    }

    public void loadPostList(){
        transaction.replace(R.id.community_frame, communityMainFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}