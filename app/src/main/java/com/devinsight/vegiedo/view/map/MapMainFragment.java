package com.devinsight.vegiedo.view.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.devinsight.vegiedo.view.search.SearchFilterViewModel;
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
    SearchFilterViewModel viewModel;

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
        recyclerView = view.findViewById(R.id.rc_card);
        cardList = new ArrayList<>();
        cardUiList = new ArrayList<>();


        //MapStoreListData 사용
//        cardList.add(new MapStoreListData(1L, "little forest", "서울특별시 강남구 강남대로 \n"+"98길 12-5", 150, Arrays.asList("Vegan", "Organic"), 4, true, 37.1234f, 127.1234f));
//        cardList.add(new MapStoreListData(2L, "Veggie Store", "123 Veggie St.", 150, Arrays.asList("Vegan", "Organic"), 5, false, 37.1234f, 127.1234f));

        //MapStoreCardUiData 사용
        cardUiList.add(new MapStoreCardUiData("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAZlBMVEX///8AAAYAAAB3d3jPz89/f4AAAANlZWbm5uZxcXLLy8yzs7QyMjO6uruOjo/7+/yWlpeioqNeXl/Fxcby8vJRUVJ+fn9YWFmoqKkkJCbc3NzZ2dns7OxBQUISEhXHx8dISEkZGRvqVqUDAAACNklEQVR4nO3c3XKiUBBFYe2I4A8KohNUjJn3f8mRmFSNCkRnsE521/puuelVByi46cEAAAAAAAAAAAAAAADAufUv24ee4ZmSiZ1MQ4/xNKNXs/FwaFaEnuQ5ZnOz4YexpaGH6V+1sK++E5uEnqdvo9XffafCl9AT9WpZbi/yvBXuXuy6z1Vh/Xa56fNTuIsa87wULsvm4/NSeIzb8xwU7lvvTheFSbb9rk+5sMo7Hj79wio/3JUnWrjL7js90cK0/m65O0+xcPVQnmLh6MFAvcLB28Wv0SUfhfm5Y3xumsdRPkuLY5GWWdyYqFe4/jyv96gcVVfXSh//Fq+2WaRJ87XoNlGwcNl1zUVhp3fP//gfbm9Tb4W5+8KZ+8LUfWHhvnDqvvD2w5xCNRTqo1Afhfoo1EehPgr1UaiPQn0U6qNQH4X6KNRHoT4K9VGoj0J9FOqjUB+F+ijUR6E+CvVRqI9CfRTqo1Afhfoo1EehPgr1UaiPQn0U6qNQH4X6KNRHoT4K9VGoj0J9/gsbtpkdQs/Uq6phwafloafqUdG8wTQLPVdfkrYdw3ZoWQaqZd+8gvacaNE69Hz/Kcnn3TuizeJj6CH/WZVGv79f8V3vHY5ncndrUtTrke/eP19bZdPr7cM/0nqfZpN5+4rr7krbROW06trWG9hi27W/++5Me9uELmnz8OL5ZqcnM3RJm7iXwPooQ5e0odBDYV9Cl7RZuS8EAAAAAAAAAAAAAAAAAAAAAADAz/cHh9AZtpkBi3AAAAAASUVORK5CYII=", 1, 2, 3, 3, 150, "1식당 이름", "1주소", true));
        cardUiList.add(new MapStoreCardUiData("data:image/png;base64,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", 3, 4, 3, 4, 300, "2식당 이름", "2주소", false));
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
                Toast.makeText(getActivity(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
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

    public void getUserLocation(){
        NaverMap.OnMapClickListener mapClickListener = new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                float latitude = (float)latLng.latitude;
                float longitude = (float)latLng.longitude;
            }
        };
    }






}
