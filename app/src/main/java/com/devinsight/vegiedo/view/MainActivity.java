package com.devinsight.vegiedo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.view.community.GeneralPostFragment;
import com.devinsight.vegiedo.view.home.HomeMainFragment;
import com.devinsight.vegiedo.view.map.MapMainFragment;
import com.devinsight.vegiedo.view.mypage.MyPageFragment;
import com.devinsight.vegiedo.view.search.SearchFilterFragment;
import com.devinsight.vegiedo.view.search.ActivityViewModel;
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
    ActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_filter = findViewById(R.id.btn_filter);
        searchView = findViewById(R.id.searchView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolBar = findViewById(R.id.toolBar);
        btn_back = findViewById(R.id.btn_back);


        /* 프래그먼트 */
        homeMainFragment = new HomeMainFragment();
        mapMainFragment = new MapMainFragment();
        searchFilterFragment = new SearchFilterFragment();
        myPageFragment = new MyPageFragment();
        searchMainFragment = new SearchMainFragment();
        communityFragment = new GeneralPostFragment();
        storeListMainFragment = new StoreListMainFragment();

        /* 액티비티 뷰 모델 */
        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId() == R.id.nav_community) {
                    toolBar.setVisibility(View.GONE);
                    transaction.replace(R.id.frame, communityFragment, "communityFragment").addToBackStack("communityFragment").commit();

                    return true;
                } else if (item.getItemId() == R.id.nav_home) {
                    toolBar.setVisibility(View.VISIBLE);
                    transaction.replace(R.id.frame, homeMainFragment,"homeMainFragment").addToBackStack("homeMainFragment").commit();

                    return true;
                } else if (item.getItemId() == R.id.nav_map) {
                    toolBar.setVisibility(View.VISIBLE);
                    transaction.replace(R.id.frame, mapMainFragment,"mapMainFragment").addToBackStack("mapMainFragment").commit();

                    return true;
                } else if (item.getItemId() == R.id.nav_user) {
                    toolBar.setVisibility(View.GONE);
                    transaction.replace(R.id.frame, myPageFragment,"myPageFragment").addToBackStack("myPageFragment").commit();

                    return true;
                }
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setShortSearchBar();

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
                viewModel.getSearchInputText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) ){

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, storeListMainFragment,"storeListMainFragment").addToBackStack("storeListMainFragment").commit();

                    return true;
                }
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

                setLongSearchBar();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, homeMainFragment).commit();


            }
        });

    }

    private void setLongSearchBar(){
        ConstraintLayout.LayoutParams toolBarParams = (ConstraintLayout.LayoutParams) toolBar.getLayoutParams();
        toolBarParams.width = dpToPx(350);
        toolBar.setLayoutParams(toolBarParams);
        ViewGroup.LayoutParams searchViewParams = searchView.getLayoutParams();
        searchViewParams.width = dpToPx(250);  // 예: 너비를 200dp로 조정
        searchView.setLayoutParams(searchViewParams);
        btn_back.setVisibility(View.INVISIBLE);
    }

    private void setShortSearchBar(){
        ConstraintLayout.LayoutParams toolBarParams = (ConstraintLayout.LayoutParams) toolBar.getLayoutParams();
        toolBarParams.width = dpToPx(320);
        toolBar.setLayoutParams(toolBarParams);
        ViewGroup.LayoutParams searchViewParams = searchView.getLayoutParams();
        searchViewParams.width = dpToPx(200);  // 예: 너비를 200dp로 조정
        searchView.setLayoutParams(searchViewParams);
        btn_back.setVisibility(View.VISIBLE);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

}