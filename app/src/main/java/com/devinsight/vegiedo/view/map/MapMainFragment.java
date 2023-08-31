package com.devinsight.vegiedo.view.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.MapInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.MapStoreListData;
import com.devinsight.vegiedo.data.ui.map.MapStoreCardUiData;
import com.devinsight.vegiedo.service.api.MapApiService;
import com.devinsight.vegiedo.service.api.StoreApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.view.search.ActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapMainFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private NaverMap naverMap;
    private static final int REQUEST_LOCATION_PERMISSION  = 1000;
    private FusedLocationSource locationSource;
    private Marker currentLocationMarker;
    MapApiService mapApiService = RetrofitClient.getMapApiService();

    //카드뷰 리사이클러뷰
    private RecyclerView recyclerView;
    private MapStoreCardAdapter cardAdapter;
    private MapStoreCardUiAdapter cardUiAdapter;
    private ArrayList<MapStoreListData> cardList;
    private ArrayList<MapStoreCardUiData> cardUiList;
    private FloatingActionButton floatingMapStorePageButton;

    /* 뷰 모델 */
    ActivityViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_map, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        locationSource = new FusedLocationSource(this, REQUEST_LOCATION_PERMISSION);
        currentLocationMarker = new Marker();

//        requestLocationPermission();

        //storePageBtn 눌렀을 때
        floatingMapStorePageButton = view.findViewById(R.id.btn_map_storePage);
        floatingMapStorePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFloatingListButtonClick();
            }
        });

        //API
//        storeApiService.readStore(2L);
        recyclerView = view.findViewById(R.id.rc_card);
        cardList = new ArrayList<>();
        cardUiList = new ArrayList<>();


        //MapStoreListData 사용
//        cardList.add(new MapStoreListData(1L, "little forest", "서울특별시 강남구 강남대로 \n"+"98길 12-5", 150, Arrays.asList("Vegan", "Organic"), 4, true, 37.1234f, 127.1234f));
//        cardList.add(new MapStoreListData(2L, "Veggie Store", "123 Veggie St.", 150, Arrays.asList("Vegan", "Organic"), 5, false, 37.1234f, 127.1234f));

        //MapStoreCardUiData 사용
        cardUiList.add(new MapStoreCardUiData("https://us.123rf.com/450wm/ingalinder/ingalinder1705/ingalinder170500032/77515524-%ED%9D%B0%EC%83%89-%EB%B0%B0%EA%B2%BD%EC%97%90-%EA%B3%A0%EB%A6%BD-%EB%90%9C-%EB%8B%A4%EC%B1%84%EB%A1%9C%EC%9A%B4-%ED%8F%89%EB%A9%B4-%EB%B2%88%ED%98%B8-1.jpg", 1, 2, 3, 3, 150, "1식당 이름", "1주소", true));
        cardUiList.add(new MapStoreCardUiData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSNRw5d8FosBXd_Qs_HbnUP_9nNnPAwASSbkw&usqp=CAU", 3, 4, 3, 4, 300, "2식당 이름", "2주소", false));
//        cardList.add(new MapStoreCardData("https://i.namu.wiki/i/l_7H5Zv2mhxYHVdmjT_An3gFWge9yHzoIZ7DWVsIYoy80AtKL9LOMYuwl4OWHUhDuBTNcrv4H7KEn3I159fp-Q.webp",7942, 9413, 33,4, 300,"가게 이름","주소",true));
//        cardList.add(new MapStoreCardData("https://pbs.twimg.com/media/F2bkFD7agAANERO?format=jpg&name=4096x4096,7942", 7942, 9413, 33,4, 300,"가게 이름","주소",true));
//        cardList.add(new MapStoreCardData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSeS17_vakustlHY5XZ0VuOiRbybzNfZbpKwULyoEqud6N9m81E9MoJkw2uwDVxh0U444&usqp=CAU",7942, 9413, 33,4, 300,"가게 이름","주소",true));


//        cardAdapter = new MapStoreCardAdapter(getContext(), cardList, this::onCardClick); //MapStoreListData 사용
        cardUiAdapter = new MapStoreCardUiAdapter(getContext(), cardUiList, this::onCardClick);
//        recyclerView.setAdapter(cardAdapter);
        recyclerView.setAdapter(cardUiAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    // 여기에 플로팅 버튼을 눌렀을 때 수행할 동작을 작성하세요.
    private void onFloatingListButtonClick() {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void onCardClick(MapStoreCardUiData mapStoreCardUiData, int i) {
    }

    public void onCardClick(MapStoreListData item, int position) {
//        cardAdapter.notifyItem
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        /* 네이버 지도에서 선택된 위치의 위도 경도를 전달 합니다. */
//        getMapLocation(naverMap);

//        naverMap.setLocationSource(locationSource);
//        LatLng defaultPosition = new LatLng(37.498095, 127.027610); //강남역
//        naverMap.moveCamera(CameraUpdate.scrollTo(defaultPosition));
//        naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);

        LatLng currentPosition;

        // 위치 권한을 요청하는 함수 호출
        Log.d("위치 권한", "요청 전");
        requestLocationPermission();
        Log.d("위치 권한", "요청 후");

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 위치 권한 허용 시 부산역으로 이동
            currentPosition = new LatLng(35.115095, 129.042694); //부산역
            Log.d("위치", "O");
        } else {
            requestLocationPermission(); // 권한을 받아오는 함수
            // 위치 권한 요청X 시 강남역으로 이동
            currentPosition = new LatLng(37.498095, 127.027610); //강남역
            Log.d("위치", "X");
        }

        // 마커 위치 및 지도 카메라 설정
        currentLocationMarker.setPosition(currentPosition);
        currentLocationMarker.setMap(naverMap);
        currentLocationMarker.setIconTintColor(Color.RED);
        naverMap.moveCamera(CameraUpdate.scrollTo(currentPosition).animate(CameraAnimation.Fly, 500));
    }


    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        //사용자가 이전에 권한 요청을 거부한 경우에 true 반환
//        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//        } else {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우
                double default_latitude = 35.115095;
                double default_lontitude = 129.042694;
                LatLng defaultPosition = new LatLng(default_latitude, default_lontitude);

                naverMap.moveCamera(CameraUpdate.scrollTo(defaultPosition).animate(CameraAnimation.Fly, 500));
                currentLocationMarker.setPosition(defaultPosition);
                currentLocationMarker.setMap(naverMap);
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            } else {
                // 권한이 거부된 경우
                double default_latitude = 37.498095;
                double default_lontitude = 127.027610;
                LatLng defaultPosition = new LatLng(default_latitude, default_lontitude);

                naverMap.moveCamera(CameraUpdate.scrollTo(defaultPosition).animate(CameraAnimation.Fly, 500));
                currentLocationMarker.setPosition(defaultPosition);
                currentLocationMarker.setMap(naverMap);
            }
        }
    }


//    private void requestLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE );
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
//            if (!locationSource.isActivated()) {
//                naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
//                LatLng defaultPosition = new LatLng(37.498095, 127.027610);
//                naverMap.moveCamera(CameraUpdate.scrollTo(defaultPosition));
//                currentLocationMarker.setPosition(defaultPosition);
//            }
//            return;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }


    private void loadStoreData() {
        mapApiService.getStoresOnMap(Arrays.asList("tag1", "tag2"), 37.5665, 126.9780, "500", "myKeyword").enqueue(new Callback<MapInquiryResponseDTO>() {

            @Override
            public void onResponse(Call<MapInquiryResponseDTO> call, Response<MapInquiryResponseDTO> response) {

            }

            @Override
            public void onFailure(Call<MapInquiryResponseDTO> call, Throwable t) {

            }
        });

    }

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






}
