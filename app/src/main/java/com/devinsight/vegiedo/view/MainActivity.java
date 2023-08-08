package com.devinsight.vegiedo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.view.home.HomeMainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private String TAG = "메인홈";

    Fragment Homefragment;
    Fragment SearchPagefragment;
    Fragment StoreListPagfragment;
    Fragment Mapfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Homefragment = new HomeMainFragment();



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.nav_community){
                    transaction.replace(R.id.frame, Homefragment).commit();

                    return true;
                } else if(item.getItemId() == R.id.nav_home){
                    transaction.replace(R.id.frame, Homefragment).commit();

                    return true;
                } else if(item.getItemId() == R.id.nav_map){
                    transaction.replace(R.id.frame, Homefragment).commit();

                    return true;
                } else if(item.getItemId() == R.id.nav_user){
                    transaction.replace(R.id.frame, Homefragment).commit();

                    return true;
                }
                return false;
            }
        });


    }
}