package com.devinsight.vegiedo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.view.home.HomeMainFragment;
import com.devinsight.vegiedo.view.search.SearchMainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private String TAG = "메인홈";
    BottomNavigationView bottomNavigationView;
    ImageButton btn_filter;
    SearchView searchView;
    Fragment homeMainfragment;
    Fragment searchMainfragment;
    Fragment storeListPagefragment;
    Fragment mapfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_filter = findViewById(R.id.btn_filter);
        searchView = findViewById(R.id.searchView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        searchMainfragment = new SearchMainFragment();
        homeMainfragment = new HomeMainFragment();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId() == R.id.nav_community) {
                    transaction.replace(R.id.frame, homeMainfragment).commit();

                    return true;
                } else if (item.getItemId() == R.id.nav_home) {
                    transaction.replace(R.id.frame, homeMainfragment).commit();

                    return true;
                } else if (item.getItemId() == R.id.nav_map) {
                    transaction.replace(R.id.frame, homeMainfragment).commit();

                    return true;
                } else if (item.getItemId() == R.id.nav_user) {
                    transaction.replace(R.id.frame, homeMainfragment).commit();

                    return true;
                }
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DODO","Search View clicled : ");
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                SearchMainFragment searchMainFragment = (SearchMainFragment) getSupportFragmentManager().findFragmentByTag("searchMainFragment");

                if(searchMainFragment == null){
                    searchMainFragment = new SearchMainFragment();
                    transaction.replace(R.id.frame,searchMainFragment,"searchMainFragment");
                } else{
                    transaction.show(searchMainFragment);
                }

                transaction.addToBackStack(null)
                        .commit();

                getSupportFragmentManager().executePendingTransactions();

            }
        });


    }
}