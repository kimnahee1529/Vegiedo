//리팩토링 하기 전 실행되는 코드
package com.devinsight.vegiedo.view.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.MapStoreListData;
import com.devinsight.vegiedo.data.response.PostRecommendRequestDTO;
import com.devinsight.vegiedo.data.response.StoreInquiryResponseDTO;
import com.devinsight.vegiedo.data.ui.map.MapStoreCardUiData;
import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;
import com.devinsight.vegiedo.service.api.MapApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.view.StoreListMainFragment;
import com.devinsight.vegiedo.view.search.ActivityViewModel;
import com.devinsight.vegiedo.view.store.StoreDetailPageFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapMainFragment extends Fragment implements OnMapReadyCallback {

    //위치 권한 요청 코드
    private static final int REQUEST_LOCATION_PERMISSION = 1000;
    //기본 위치로 설정된 강남역의 좌표
    private static final LatLng DEFAULT_LOCATION = new LatLng(37.498095, 127.027610);

    private MapView mapView;
    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private Marker currentLocationMarker;
    private RecyclerView recyclerView;
    private MapStoreCardUiAdapter cardUiAdapter;
    private ArrayList<MapStoreCardUiData> cardUiList = new ArrayList<>();
    private FloatingActionButton floatingMapLikeButton;
    private boolean isLikeButtonPressed = false; // 버튼이 눌린 상태를 기억하는 변수
    private FloatingActionButton floatingMapLocationButton;
    private FloatingActionButton floatingMapStorePageButton;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker clusterMarker;
    private int distance;
    List<Long> storeIdList = new ArrayList<>();
    private ArrayList<Marker> markersOnMap = new ArrayList<>();
    private MapApiService mapApiService = RetrofitClient.getMapApiService();
    ActivityViewModel viewModel;
    private float latitude;
    private float longitude;
    private LatLng center;
    private boolean fromList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            distance = getArguments().getInt("distance");
            latitude = getArguments().getFloat("latitude");
            longitude = getArguments().getFloat("longitude");
            fromList = getArguments().getBoolean("fromList");
            Log.d("검색 목록 기반 카드뷰","" + fromList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_map, container, false);
        setupMapView(view, savedInstanceState); // mapView를 초기화하고, 비동기로 지도가 준비될 때까지 기다림
        setupViewModel(); // ViewModel을 초기화
        setupLocationSource(); // 위치 정보를 추적하는 객체와 현재 위치 마커를 초기화
        setupFloatingActionButton(view);
        setupRecyclerView(view);
        callMapAPI();

        if(fromList){
            getMpaDataBasedOnList(view);
        }

        return view;
    }

    private void getMpaDataBasedOnList(View view){
        ArrayList<MapStoreCardUiData> cardUiListBasedOnList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.rc_card);
        MapStoreCardUiAdapter cardUiAdapter = new MapStoreCardUiAdapter(getContext(), cardUiListBasedOnList, this::onCardClick);
        recyclerView.setAdapter(cardUiAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //지도 어댑터에서의 찜버튼 리스너
        //찜시킬 때
        cardUiAdapter.setLikeBtnListener(new MapStoreCardUiAdapter.LikeBtnListener() {
            @Override
            public void onLikeButton(Long storeId) {
                Log.d("찜버튼 시킬 때", "onLikeButton, storeId:"+storeId);
                viewModel.StoreActiveLikeData(storeId);
            }
        });

        //찜취소 시킬 때
        cardUiAdapter.setCancleLikeBtnListener(new MapStoreCardUiAdapter.CancleLikeBtnListener() {
            @Override
            public void onCancleLiketButton(Long storeId) {
                Log.d("찜버튼 취소 시킬 때", "onCancleLiketButton, storeId:"+storeId);
                viewModel.StoreInactiveLikeData(storeId);
            }
        });
        AuthPrefRepository authPrefRepository = new AuthPrefRepository(getContext());
        String social;
        if( authPrefRepository.getAuthToken("KAKAO") != null) {
            social = "KAKAO";
        } else {
            social = "GOOGLE";
        }
        String token = authPrefRepository.getAuthToken(social);

        Log.d(" 내용","" + latitude + "" +longitude +"" +distance);
        mapApiService.getStoresOnMap("Bearer " + token, latitude, longitude, distance*1000).enqueue(new Callback<List<MapStoreListData>>() {
            @Override
            public void onResponse(Call<List<MapStoreListData>> call, Response<List<MapStoreListData>> response) {

                if(response.isSuccessful()){
                    Log.d("RetrofitRequestURL 성공", "Requested URL: " + call.request().url());
                    Log.d("리스트 기반 지도 api 호출 성공 ","성공" + response);
                    cardUiListBasedOnList.clear();

                    // 데이터 변환 후 cardUiList에 추가
                    for (MapStoreListData listData : response.body() ) {
                        cardUiListBasedOnList.add(convertToCardUiData(listData));
                    }

                    cardUiAdapter.notifyDataSetChanged();  // 어댑터에 데이터 변경 알림

                }else{
                    Log.d("RetrofitRequestURL 실패", "Requested URL: " + call.request().url());
                    Log.e("p리스트 기반 지도 api 호출 실패 1 ","실패1" + response);
                    try {
                        Log.e("리스트 기반 지도 실패 1", "Error Body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<MapStoreListData>> call, Throwable t) {
                Log.e("리스트 기반 지도 호출 실패 2 ","실패2" + t.getMessage());

            }
        });
    }

    private void setupMapView(View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    private void setupViewModel() {
        // You mentioned to leave the viewModel part out, so this is just a placeholder.
        viewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        distance = viewModel.passDistance();
        Log.d("지도 거리", String.valueOf(distance));
    }

    private void setupLocationSource() {
        locationSource = new FusedLocationSource(this, REQUEST_LOCATION_PERMISSION);
        currentLocationMarker = new Marker();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    private void setupFloatingActionButton(View view) {
        floatingMapLikeButton = view.findViewById(R.id.btn_map_like);
        floatingMapLikeButton.setOnClickListener(v -> onFloatingLikeButtonClick());

        floatingMapLocationButton = view.findViewById(R.id.btn_map_myLocation);
        floatingMapLocationButton.setOnClickListener(v -> onFloatingLocationButtonClick());

        floatingMapStorePageButton = view.findViewById(R.id.btn_map_storePage);  // Make sure this line is before the next
        floatingMapStorePageButton.setOnClickListener(v -> onFloatingListButtonClick());
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.rc_card);
        cardUiAdapter = new MapStoreCardUiAdapter(getContext(), cardUiList, this::onCardClick);
        recyclerView.setAdapter(cardUiAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //지도 어댑터에서의 찜버튼 리스너
        //찜시킬 때
        cardUiAdapter.setLikeBtnListener(new MapStoreCardUiAdapter.LikeBtnListener() {
            @Override
            public void onLikeButton(Long storeId) {
                Log.d("찜버튼 시킬 때", "onLikeButton, storeId:"+storeId);
                viewModel.StoreActiveLikeData(storeId);
            }
        });

        //찜취소 시킬 때
        cardUiAdapter.setCancleLikeBtnListener(new MapStoreCardUiAdapter.CancleLikeBtnListener() {
            @Override
            public void onCancleLiketButton(Long storeId) {
                Log.d("찜버튼 취소 시킬 때", "onCancleLiketButton, storeId:"+storeId);
                viewModel.StoreInactiveLikeData(storeId);
            }
        });
    }

    private void onCardClick(MapStoreCardUiData item, int position) {
//        Toast.makeText(getContext(), item.getStoreId() + " is clicked ", Toast.LENGTH_SHORT).show();
        viewModel.setStoreIdLiveData(item.getStoreId());
//        viewModel.getStoreIdLiveData().getValue();
        StoreDetailPageFragment detailFragment = new StoreDetailPageFragment();

        // 프래그먼트 트랜잭션 시작
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, detailFragment);  // R.id.container는 당신의 FrameLayout 또는 호스트 뷰의 ID여야 합니다.
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void onFloatingLikeButtonClick() {
        if(isLikeButtonPressed){
            isLikeButtonPressed = false;
            floatingMapLikeButton.setImageResource(R.drawable.ic_heart_default);
            displayDonotLikedMarkers(naverMap);
        }else{
            isLikeButtonPressed = true; // 버튼이 눌린 상태로 설정
            floatingMapLikeButton.setImageResource(R.drawable.red_heart);
            displayLikedMarkers(naverMap);
        }
    }

    private void onFloatingLocationButtonClick() {
        if(currentLocationMarker != null && currentLocationMarker.getPosition() != null) {
            LatLng currentMarkerPosition = currentLocationMarker.getPosition();
            naverMap.moveCamera(CameraUpdate.scrollTo(currentMarkerPosition).animate(CameraAnimation.Fly, 500));
        } else {
            Toast.makeText(getContext(), "현재 위치가 설정되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    private void onFloatingListButtonClick() {
        List<String> tags = Arrays.asList(
                "완전 비건",
                "락토 오보",
                "키토식단",
                "식물성 베이커리",
                "대체육",
                "폴로",
                "락토",
                "페스코테리언",
                "글루텐프리"
        );

        Log.d("이동하기 직전 MapCenter", "Center: " + center.latitude + ", " + center.longitude);
//        Log.d("마커의 storeId", ""+storeIdList.size()+"개"+storeIdList);
        viewModel.getFilterData(distance, tags);
        StoreListMainFragment storeListMainFragment  = new StoreListMainFragment();
        Bundle bundle = new Bundle();
//        Long[] storeIdArray = storeIdList.toArray(new Long[0]);
//        bundle.putSerializable("storeIdArray", storeIdArray);
        bundle.putDouble("centerLatitude", center.latitude);  // 위도를 번들에 추가
        bundle.putDouble("centerLongitude", center.longitude);  // 경도를 번들에 추가
        storeListMainFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, storeListMainFragment);
        transaction.commit();

    }


    private void callMapAPI(){
        viewModel.getMapStoreLiveData().observe(getViewLifecycleOwner(), new Observer<List<MapStoreListData>>() {
            @Override
            public void onChanged(List<MapStoreListData> data) {
                Log.d("LOGAPI callMapAPI 함수 안 ", " "+ data);

                cardUiList.clear();

                // 데이터 변환 후 cardUiList에 추가
                for (MapStoreListData listData : data) {
                    // distance 값보다 작은 데이터만 cardUiList에 추가
                    if (listData.getDistance() < distance * 1000) {
                        cardUiList.add(convertToCardUiData(listData));
                    }
                }

                cardUiAdapter.notifyDataSetChanged();  // 어댑터에 데이터 변경 알림
            }
        });

        // 데이터 로드
        viewModel.MapInquiryData();
    }


    private MapStoreCardUiData convertToCardUiData(MapStoreListData listData) {
        String tag1 = "";
        String tag2 = "";
//        float distance = listData.getDistance();


        // Check if we have enough tags for tag1 and tag2
        if(listData.getTags() != null && !listData.getTags().isEmpty()) {
            tag1 = listData.getTags().get(0);
            if(listData.getTags().size() > 1) {
                tag2 = listData.getTags().get(1);
            }
        }

        return new MapStoreCardUiData(
                listData.getStoreId(),
                listData.getImages(),          // storeImage
                tag1,                          // storeTag1
                tag2,                          // storeTag2
                listData.getReviewCount(),     // reviewer
                listData.getStars(),           // starRating
                listData.getDistance(),        // distance
                listData.getStoreName(),       // storeName
                listData.getAddress(),         // address
                listData.getLike(),            // like
                listData.getLatitude(),        // latitude
                listData.getLongitude()        // longitude
        );
    }

    // isLike가 true인 마커만 표시하는 함수
    private void displayLikedMarkers(NaverMap naverMap) {
        clearAllMarkers();  // 모든 마커를 지도에서 제거합니다.

        for (MapStoreCardUiData map : cardUiList) {
            if (map.isLike()) {  // isLike 값이 true인 경우에만
                LatLng location = new LatLng(map.getLatitude(), map.getLongitude());
                addMarkerAtLocation(map, location, naverMap);  // 해당 위치에 마커를 추가합니다.
            }
        }
    }

    // isLike가 false인 마커만 표시하는 함수
    private void displayDonotLikedMarkers(NaverMap naverMap) {
        clearAllMarkers();  // 모든 마커를 지도에서 제거합니다.

        for (MapStoreCardUiData map : cardUiList) {
            if (!map.isLike()) {  // isLike 값이 true인 경우에만
                LatLng location = new LatLng(map.getLatitude(), map.getLongitude());
                addMarkerAtLocation(map, location, naverMap);  // 해당 위치에 마커를 추가합니다.
            }
        }
    }


//    public void onCardClick(MapStoreListData item, int position) {
//        Toast.makeText(getContext(),item.getStoreName() + " is clicked ",Toast.LENGTH_SHORT).show();
//    }

    // 지도가 준비되면 호출되는 콜백 함수
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

//         ViewModel에서 가져온 데이터로 UI를 업데이트
//        viewModel.getMapStoreSummaryData().observe(getViewLifecycleOwner(), mapStoreUiLiveData -> {
//            cardUiList.clear(); // Clear previous data
//            cardUiList.addAll(mapStoreUiLiveData);
//            cardUiAdapter.notifyDataSetChanged();
//        });

//        viewModel.getMapStoreSummaryData().observe(getViewLifecycleOwner(), mapStoreCardUiData -> {
//
//        });

        // 위치 권한 허용 여부 묻는 창
        checkAndRequestLocationPermission();

        // 지도가 멈출 때마다 가시적인 마커들을 표시
        naverMap.addOnCameraIdleListener(() -> displayVisibleMarkers(naverMap));

        // 카메라의 위치가 변경될 때마다 호출될 리스너를 설정
        naverMap.addOnCameraChangeListener((reason, animated) -> {
            center = naverMap.getCameraPosition().target;
            Log.d("MapCenter", "Center: " + center.latitude + ", " + center.longitude);
        });
    }

    //권한 체크
    private void checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapView.setVisibility(View.VISIBLE); // 권한이 이미 허용된 경우
            setMarkerAndMoveCameraToCurrentLocation(naverMap); // 현재 위치로 마커를 찍는다.
        } else {
            mapView.setVisibility(View.INVISIBLE);
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapView.setVisibility(View.VISIBLE);
                setMarkerAndMoveCameraToCurrentLocation(naverMap); // 권한이 허용되면 현재 위치로 마커를 찍는다.
            } else {
                mapView.setVisibility(View.VISIBLE); // 권한 거부에도 맵은 보여준다.
                setMarkerAndMoveCamera(getDefaultLocation(), naverMap); // 권한이 거부되면 강남역에 마커를 찍는다.
            }
        }
    }

    private void setMarkerAndMoveCameraToCurrentLocation(NaverMap naverMap) {
        getCurrentLocation(new LocationCallback() {
            @Override
            public void onLocationResult(LatLng location) {
                if (location != null) {
                    setMarkerAndMoveCamera(location, naverMap);
                } else {
                    setMarkerAndMoveCamera(getDefaultLocation(), naverMap);
                }
            }
        });
    }

    // 주어진 위치에 마커를 설정하고 카메라를 해당 위치로 이동
    private void setMarkerAndMoveCamera(LatLng markerPosition, NaverMap naverMap) {
        currentLocationMarker.setPosition(markerPosition);
        currentLocationMarker.setMap(naverMap);
        currentLocationMarker.setIconTintColor(Color.RED);
        naverMap.moveCamera(CameraUpdate.scrollTo(markerPosition).animate(CameraAnimation.Fly, 500));
    }

    // 기본 위치인 강남역의 좌표를 반환
    private LatLng getDefaultLocation() {
        return DEFAULT_LOCATION;
    }


    //위치를 반환하기 위한 콜백 인터페이스
    public interface LocationCallback {
        void onLocationResult(LatLng location);
    }

    // 현재 사용자의 위치를 가져오는 메소드
    private void getCurrentLocation(LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            callback.onLocationResult(currentLocation);
                        } else {
                            callback.onLocationResult(null); // 위치 정보를 가져올 수 없을 경우
                        }
                    });
        } else {
            // 권한 요청 또는 권한 거부에 따른 추가 작업
            requestLocationPermission();
        }
    }


    // 위치 권한을 요청하는 메소드
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    // 지도에 표시된 모든 마커를 제거하는 메소드
    private void clearAllMarkers() {
        for (Marker marker : markersOnMap) {
            marker.setMap(null);
        }
        markersOnMap.clear();
    }

    //현재 지도 상의 가시 범위 내에 있는 위치에만 마커를 표시하는 메소드
    private void displayVisibleMarkers(NaverMap naverMap) {
        clearAllMarkers();
        storeIdList.clear();

        LatLngBounds visibleBounds = naverMap.getContentBounds();
        for (MapStoreCardUiData map : cardUiList) {
            LatLng location = new LatLng(map.getLatitude(), map.getLongitude());
            if (visibleBounds.contains(location)) {
                Long storeId = map.getStoreId();  // 이렇게 storeId를 가져올 수 있습니다.
                storeIdList.add(storeId);  // 리스트에 storeId 추가

//                Log.d("마커의 storeId", String.valueOf(storeId));  // storeId를 로그에 출력
                if(isLikeButtonPressed) { // 버튼이 눌린 상태인 경우
                    if(map.isLike()) { // isLike 값이 true인 경우에만
                        addMarkerAtLocation(map, location, naverMap);
                    }
                } else {
                    if(!map.isLike()) { // isLike 값이 false인 경우에만
                        addMarkerAtLocation(map, location, naverMap);
                    }
                }
            }
        }

//        Log.d("마커의 storeId", ""+storeIdList.size()+"개, "+storeIdList);
        int visibleMarkerCount = markersOnMap.size();
        Log.d("지도에 마커 몇개 있음?", String.valueOf(visibleMarkerCount));

    }


    // 특정 위치에 마커를 추가하고, 마커의 아이콘(씨앗,씨)과 캡션(가게이름)을 설정하는 메소드
    private void addMarkerAtLocation(MapStoreCardUiData map, LatLng location, NaverMap naverMap) {
        Marker marker = new Marker();
        marker.setPosition(location);
        marker.setMap(naverMap);
        marker.setCaptionText(map.getStoreName());

        int imageId = map.isLike() ? R.drawable.sprout : R.drawable.seed_brown;
        int width = map.isLike() ? 100 : 50;
        int height = map.isLike() ? 100 : 70;

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), imageId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false);
        OverlayImage resizedImage = OverlayImage.fromBitmap(resizedBitmap);
        marker.setIcon(resizedImage);

        int markerCount = markersOnMap.size();
        float currentZoom = (float) naverMap.getCameraPosition().zoom;
        Log.d("지도 확대 몇까지 함?", String.valueOf(currentZoom));
        if (currentZoom >= 14) {
            marker.setMap(naverMap);  // 각 마커에 대해 확대/축소 기능 적용
            if (clusterMarker != null) {
                clusterMarker.setMap(null);  // 이전 클러스터 마커를 제거합니다.
            }
        } else {
            marker.setMap(null);  // 각 마커에 대해 확대/축소 기능 적용
            // 데이터가 변경된 후
            updateClusterMarker(markerCount, naverMap);

        }

        // 마커 클릭 리스너 설정
        marker.setOnClickListener(overlay -> {
            String storeName = marker.getCaptionText();
            for (int i = 0; i < cardUiList.size(); i++) {
                if (cardUiList.get(i).getStoreName().equals(storeName)) {
                    Log.d("마커 클릭1", storeName + " " +String.valueOf(i));
                    scrollToItemInView(i);
                    break;
                }
            }
            return true;
        });

        markersOnMap.add(marker);
    }

    // 핀 클릭 시 해당 카드뷰가 중앙에 오도록 설정
    private void scrollToItemInView(int position) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager != null) {
            int screenWidth = recyclerView.getWidth();
            int offset = (int) (screenWidth * 0.1f);  // 예를 들어, 화면의 10%만큼의 오프셋
            layoutManager.scrollToPositionWithOffset(position, offset);
        }
    }

    // 사용자가 지도에서 위치를 클릭하면 해당 위치의 위도와 경도를 ViewModel로 전달하는 메소드
    public void getMapLocation(NaverMap naverMap){
        NaverMap.OnMapClickListener mapClickListener = new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                float latitude = (float)latLng.latitude;
                float longitude = (float)latLng.longitude;
                /* 네이버 지도 위에서 선택된 위치의 위도 경도를 뷰 모델로 전달 합니다. */
                viewModel.getCurrentMapLocationData(latitude, longitude);
            }
        };

        naverMap.setOnMapClickListener(mapClickListener);
    }

    private OverlayImage createClusterIcon(int markerCount) {

        // 이미지의 크기 설정
        int width = 150;
        int height = 150;

        // 새 비트맵과 캔버스 생성
        Bitmap clusterBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(clusterBitmap);

        // 원 그리기
        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.parseColor("#FC840D"));
        circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);

        // 텍스트 그리기
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(markerCount), width / 2, (height / 2) - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);

        // OverlayImage로 변환
        return OverlayImage.fromBitmap(clusterBitmap);
    }

    private void updateClusterMarker(int markerCount, NaverMap naverMap) {

        if (clusterMarker != null) {
            clusterMarker.setMap(null);  // 이전 클러스터 마커를 제거합니다.
        }

        CameraPosition cameraPosition = naverMap.getCameraPosition();
        LatLng centerPosition = cameraPosition.target;

        clusterMarker = new Marker();
        clusterMarker.setIcon(createClusterIcon(markerCount));
        clusterMarker.setPosition(centerPosition);
        clusterMarker.setMap(naverMap);
    }

}
