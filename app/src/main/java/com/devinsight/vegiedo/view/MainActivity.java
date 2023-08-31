package com.devinsight.vegiedo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.view.community.GeneralPostFragment;
import com.devinsight.vegiedo.view.home.HomeMainFragment;
import com.devinsight.vegiedo.view.map.MapMainFragment;
import com.devinsight.vegiedo.view.mypage.MyPageFragment;
import com.devinsight.vegiedo.view.search.SearchFilterFragment;
import com.devinsight.vegiedo.view.search.SearchFilterViewModel;
import com.devinsight.vegiedo.view.search.SearchMainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private String TAG = "메인홈";
    BottomNavigationView bottomNavigationView;
    ImageButton btn_filter;
    ImageButton btn_filter_for_search;

    ImageButton btn_back;
    EditText searchView;
    EditText searchView_for_search;
    Toolbar toolBar;
    Toolbar toolbar_for_search;
    Fragment homeMainFragment;
    Fragment searchMainFragment;
    Fragment searchFilterFragment;
    Fragment storeListPageFragment;
    Fragment mapMainFragment;
    Fragment storeListMainFragment;
    Fragment myPageFragment;
    Fragment communityFragment;

    /* 뷰모델 */
    SearchFilterViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_filter = findViewById(R.id.btn_filter);
        searchView = findViewById(R.id.searchView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolBar = findViewById(R.id.toolBar);
        btn_back = findViewById(R.id.btn_back);


//      Fragment
        homeMainFragment = new HomeMainFragment();
        mapMainFragment = new MapMainFragment();
        searchFilterFragment = new SearchFilterFragment();
        myPageFragment = new MyPageFragment();
        searchMainFragment = new SearchMainFragment();
        communityFragment = new GeneralPostFragment();
        viewModel = new ViewModelProvider(this).get(SearchFilterViewModel.class);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId() == R.id.nav_community) {
                    toolBar.setVisibility(View.GONE);
                    transaction.replace(R.id.frame, communityFragment).addToBackStack(null).commit();

                    return true;
                } else if (item.getItemId() == R.id.nav_home) {
                    toolBar.setVisibility(View.VISIBLE);
                    transaction.replace(R.id.frame, homeMainFragment).addToBackStack(null).commit();

                    return true;
                } else if (item.getItemId() == R.id.nav_map) {
                    toolBar.setVisibility(View.VISIBLE);
                    transaction.replace(R.id.frame, mapMainFragment).addToBackStack(null).commit();

                    return true;
                } else if (item.getItemId() == R.id.nav_user) {
                    toolBar.setVisibility(View.GONE);
                    transaction.replace(R.id.frame, myPageFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout.LayoutParams toolBarParams = (ConstraintLayout.LayoutParams) toolBar.getLayoutParams();
                toolBarParams.width = dpToPx(320);
                toolBar.setLayoutParams(toolBarParams);

                ViewGroup.LayoutParams searchViewParams = searchView.getLayoutParams();
                searchViewParams.width = dpToPx(200);  // 예: 너비를 200dp로 조정
                searchView.setLayoutParams(searchViewParams);

                btn_back.setVisibility(View.VISIBLE);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, searchMainFragment, "SearchMainFragment")
                        .addToBackStack("SearchMainFragment").commit();

            }
        });


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.searchList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });


        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, searchFilterFragment).commit();
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConstraintLayout.LayoutParams toolBarParams = (ConstraintLayout.LayoutParams) toolBar.getLayoutParams();
                toolBarParams.width = dpToPx(350);
                toolBar.setLayoutParams(toolBarParams);

                ViewGroup.LayoutParams searchViewParams = searchView.getLayoutParams();
                searchViewParams.width = dpToPx(250);  // 예: 너비를 200dp로 조정
                searchView.setLayoutParams(searchViewParams);

                btn_back.setVisibility(View.INVISIBLE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, homeMainFragment).commit();


            }
        });

    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

}