package com.devinsight.vegiedo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

//import com.devinsight.vegiedo.Manifest;
import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;
import com.devinsight.vegiedo.view.community.CommunityMainFragment;
import com.devinsight.vegiedo.view.home.HomeMainFragment;
import com.devinsight.vegiedo.view.map.MapMainFragment;
import com.devinsight.vegiedo.view.mypage.MyPageFragment;
import com.devinsight.vegiedo.view.search.SearchFilterFragment;
import com.devinsight.vegiedo.view.search.ActivityViewModel;
import com.devinsight.vegiedo.view.search.SearchMainFragment;
import com.devinsight.vegiedo.view.store.StoreDetailPageFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

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
    Fragment storeDetailPageFragment;
    Fragment communityMainFragment;

    /* 뷰모델 */
    ActivityViewModel viewModel;
    List<String> currentInputList;
    LocationManager locationManager;
    UserPrefRepository userPrefRepository;
    AuthPrefRepository authPrefRepository;

    int INITIAL_DISTANCE = 10;

    private InputMethodManager inputMethodManager;


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
        storeListMainFragment = new StoreListMainFragment();
        storeDetailPageFragment = new StoreDetailPageFragment();
        communityMainFragment = new CommunityMainFragment();

        /* 액티비티 뷰 모델 */
        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        userPrefRepository = new UserPrefRepository(this);
        authPrefRepository = new AuthPrefRepository(this);
        String token = authPrefRepository.getAuthToken("KAKAO");
        List<String> initialTagList = userPrefRepository.loadTagList();
        int initialDistance = INITIAL_DISTANCE;

        //애드몹 초기화
        MobileAds.initialize(this, initializationStatus -> {});

        viewModel.getInitialFilteredData(initialDistance, initialTagList, token);

        currentInputList = new ArrayList<>();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean locationPermission = Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (locationPermission) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                float latitude = (float) location.getLatitude();
                float longitude = (float) location.getLongitude();

                Log.d("위치 1 ", "위치" + "위도 : " + latitude + "경도 : " + longitude);
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        }

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);




        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId() == R.id.nav_community) {
                    toolBar.setVisibility(View.GONE);
//                    Intent intent = new Intent(getApplicationContext(), CommunityMainActivity.class);
//                    startActivity(intent);
//                    finish();
                    transaction.replace(R.id.frame, communityMainFragment, "communityMainFragment").addToBackStack("communityMainFragment").commit();

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
                /* transaction. 을 통해 그 뒤에 활용 가능한 모든 함수들을 확인 한다.*/

            }
        });

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    setShortSearchBar();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, searchMainFragment, "SearchMainFragment")
                            .addToBackStack("SearchMainFragment").commit();
                } else {
                    setLongSearchBar();
                    getSupportFragmentManager().popBackStack();

                }
            }
        });


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.getSearchInputText(charSequence.toString());
                viewModel.searchSummaryListByKeyword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) ){
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String currentInput = textView.getText().toString();
                    if(currentInput != null ) {
                        currentInputList.add(currentInput);
                        viewModel.getCurrentInput(currentInputList.get( currentInputList.size() - 1 ));
                        for( int i = 0 ; i < currentInputList.size() ; i ++ ) {
                            Log.d("최근 검색어","최근 검색어 : " + currentInputList.get(i).toString());
                        }
                    }

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
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, searchFilterFragment,"searchFilterFragment").addToBackStack("searchFilterFragment").commit();
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setLongSearchBar();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack("homeMainFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, homeMainFragment).addToBackStack(null).commit();


            }
        });



    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, fragment).commit();
    }

    final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            float latitude = (float) location.getLatitude();
            float longitude = (float) location.getLongitude();
            viewModel.getCurrentLocationData(latitude, longitude);

            Log.d("위치 2 ", "위치" + "위도 : " + latitude + "경도 : " + longitude);
        }
    };

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

    @Override
    public void onPause() {
        super.onPause();
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }



}