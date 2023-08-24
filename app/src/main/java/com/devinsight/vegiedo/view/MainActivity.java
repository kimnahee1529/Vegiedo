package com.devinsight.vegiedo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.view.home.HomeMainFragment;
import com.devinsight.vegiedo.view.map.MapMainFragment;
import com.devinsight.vegiedo.view.mypage.MyPageFragment;
import com.devinsight.vegiedo.view.search.SearchFilterFragment;
import com.devinsight.vegiedo.view.search.SearchMainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private String TAG = "메인홈";
    BottomNavigationView bottomNavigationView;
    ImageButton btn_filter;
    SearchView searchView;
    Toolbar toolBar;
    Fragment homeMainFragment;
    Fragment searchMainFragment;
    Fragment searchFilterFragment;
    Fragment storeListPageFragment;
    Fragment mapMainFragment;
    Fragment storeListMainFragment;
    Fragment myPageFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_filter = findViewById(R.id.btn_filter);
        searchView = findViewById(R.id.searchView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolBar = findViewById(R.id.toolBar);


//      Fragment
        homeMainFragment = new HomeMainFragment();
        mapMainFragment = new MapMainFragment();
        searchFilterFragment = new SearchFilterFragment();
        myPageFragment = new MyPageFragment();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId() == R.id.nav_community) {
                    toolBar.setVisibility(View.VISIBLE);
                    transaction.replace(R.id.frame, homeMainFragment).addToBackStack(null).commit();

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

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, searchFilterFragment).commit();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                openStoreListMainFragment();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void openSearchMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchMainFragment searchMainFragment = (SearchMainFragment) getSupportFragmentManager().findFragmentByTag("searchMainFragment");

        if (searchMainFragment == null) {
            searchMainFragment = SearchMainFragment.instance();
            transaction.add(R.id.frame, searchMainFragment, "searchMainFragment");
        } else {
            transaction.show(searchMainFragment);
        }
        transaction.addToBackStack(null)
                .commit();
    }

    private void openStoreListMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        StoreListMainFragment storeListhMainFragment = (StoreListMainFragment) getSupportFragmentManager().findFragmentByTag("StoreListhMainFragment");

        if (storeListMainFragment == null) {
            storeListhMainFragment = StoreListMainFragment.instance();
            transaction.add(R.id.frame, storeListhMainFragment, "StoreListhMainFragment");
        } else {
            transaction.show(storeListhMainFragment);
        }
        transaction.addToBackStack(null)
                .commit();
    }
}