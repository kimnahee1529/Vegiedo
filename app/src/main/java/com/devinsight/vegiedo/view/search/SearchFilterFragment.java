package com.devinsight.vegiedo.view.search;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.ui.login.TagStatus;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;
import com.devinsight.vegiedo.utill.VeganTag;
import com.devinsight.vegiedo.view.StoreListMainFragment;

import java.util.ArrayList;
import java.util.List;


public class SearchFilterFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    /* seekbar */
    SeekBar seekBar;
    TextView seekbar_distance;
    TextView btn_complete;

    /* 프래그먼트 이동*/
    StoreListMainFragment storeListMainFragment;
    SearchMainFragment searchMainFragment;

    /* 태그 선택 */
    List<String> userTagList;

    /* 뷰모델 */
    ActivityViewModel viewModel;
    FilterData filterData;

    /* 저장소 */
    UserPrefRepository userPrefRepository;

    LocationManager locationManager;


    public SearchFilterFragment() {
        // Required empty public constructor
    }


    public static SearchFilterFragment newInstance(String param1, String param2) {
        SearchFilterFragment fragment = new SearchFilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_filter, container, false);

        seekBar = view.findViewById(R.id.seekBar);
        seekbar_distance = view.findViewById(R.id.seekbar_distance);

        btn_complete = view.findViewById(R.id.btn_complete);

        seekBar.setProgress(5);
        seekbar_distance.setX(182);
        seekbar_distance.setText(seekBar.getProgress() + "km이내");

        storeListMainFragment = new StoreListMainFragment();
        searchMainFragment = new SearchMainFragment();
        userPrefRepository = new UserPrefRepository(this.getContext());
        filterData = new FilterData();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                /* thumb의 이동에 따른 거리 표시 및 textView 이동 */
                int padding = seekBar.getPaddingLeft() + seekBar.getPaddingRight();
                int sPos = seekBar.getLeft() + seekBar.getPaddingLeft();
                int xPos = (seekBar.getWidth() - padding) * seekBar.getProgress() / seekBar.getMax() + sPos - (seekbar_distance.getWidth() / 2);
                seekbar_distance.setX(xPos);
                seekbar_distance.setText(seekBar.getProgress() + "km이내");
                filterData.setDistance(seekBar.getProgress());
                Log.d("seekbar 위치 ", "위치 : " + xPos);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ToggleButton tagFruittarian = view.findViewById(VeganTag.FRUITTARIAN.getTagId());
        ToggleButton tagVegan = view.findViewById(VeganTag.VEGAN.getTagId());
        ToggleButton taglacto = view.findViewById(VeganTag.LACTO.getTagId());
        ToggleButton tagOvo = view.findViewById(VeganTag.OVO.getTagId());
        ToggleButton taglactoOvo = view.findViewById(VeganTag.LACTO_OVO.getTagId());
        ToggleButton tagPesca = view.findViewById(VeganTag.PESCO.getTagId());
        ToggleButton tagPollo = view.findViewById(VeganTag.POLLO.getTagId());
        ToggleButton tagKeto = view.findViewById(VeganTag.KETO.getTagId());
        ToggleButton tagGluten = view.findViewById(VeganTag.GLUTEN_FREE.getTagId());

        userTagList = new ArrayList<>();

        viewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        tagFruittarian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String tagContent = VeganTag.FRUITTARIAN.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());
            }
        });

        tagVegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.VEGAN.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

            }
        });
        taglacto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.LACTO.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());


            }
        });
        tagOvo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.OVO.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

            }
        });
        taglactoOvo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.LACTO_OVO.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());


            }
        });
        tagPesca.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.PESCO.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());


            }
        });
        tagPollo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.POLLO.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());


            }
        });
        tagKeto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.KETO.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

            }
        });
        tagGluten.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.GLUTEN_FREE.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

            }
        });

        viewModel.getTagStatusLiveData().observe(getViewLifecycleOwner(), new Observer<TagStatus>() {
            @Override
            public void onChanged(TagStatus tagStatus) {
                if (tagStatus.isStatus()) {
                    String userTag = tagStatus.getContent();
                    userTagList.add(userTag);
                    Log.d("리스트 추가", "태그 : " + userTagList);
                } else if (userTagList != null && !tagStatus.isStatus()) {
                    String userTagToRemove = tagStatus.getContent();  // 삭제하고자 하는 태그의 값
                    int indexToRemove = userTagList.indexOf(userTagToRemove);  // 해당 태그의 인덱스를 찾습니다.
                    if (indexToRemove != -1) {  // 만약 해당 태그가 리스트에 있다면
                        userTagList.remove(indexToRemove);  // 해당 인덱스의 태그를 제거합니다.
                        Log.d("리스트 삭제", "태그 : " + userTagList);
                    }
                }
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userPrefRepository.loadTagList() == null) {
                    userPrefRepository.saveUserTags(userTagList);
                    Log.d("태그", "저장됨");
                } else {
                    userPrefRepository.removeTagList();
                    Log.d("태그", "삭제됨");
                    userPrefRepository.saveUserTags(userTagList);
                    Log.d("태그", "저장됨 2 ");
                }


                if (userTagList != null && filterData.getDistance() != null) {
                    userPrefRepository.saveUserTags(userTagList);
                    filterData.setTags(userTagList);
                } else {
                    filterData.setTags(userPrefRepository.loadTagList());
                    filterData.setDistance(5);
                }

                viewModel.getFilterData(filterData.getDistance(), filterData.getTags());
                Log.d("필터 데이터", "성공" + filterData.getDistance() + filterData.getTags());
//                getParentFragmentManager().beginTransaction().replace(R.id.frame, searchMainFragment).commit();

                getParentFragmentManager().beginTransaction().replace(R.id.frame, storeListMainFragment).commit();

            }
        });

        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        boolean locationPermission = Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
//        if (locationPermission) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{
//                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
//        } else {
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                float latitude = (float) location.getLatitude();
//                float longitude = (float) location.getLongitude();
//
//                Log.d("위치 1 ", "위치" + "위도 : " + latitude + "경도 : " + longitude);
//            }
//
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
//        }
//    }
//
//    final LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(@NonNull Location location) {
//            float latitude = (float) location.getLatitude();
//            float longitude = (float) location.getLongitude();
//            viewModel.getCurrentLocationData(latitude, longitude);
//
//            Log.d("위치 2 ", "위치" + "위도 : " + latitude + "경도 : " + longitude);
//        }
//    };
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (locationListener != null) {
//            locationManager.removeUpdates(locationListener);
//        }
//    }


    public boolean isInitialTag(String tag) {
        List<String> initialTags = userPrefRepository.loadTagList();
        return initialTags.contains(tag);
    }


}