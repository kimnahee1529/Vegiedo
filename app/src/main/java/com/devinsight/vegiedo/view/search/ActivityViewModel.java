package com.devinsight.vegiedo.view.search;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.response.MapStoreListData;
import com.devinsight.vegiedo.data.response.StoreListData;
import com.devinsight.vegiedo.data.ui.login.TagStatus;
import com.devinsight.vegiedo.data.ui.search.SearchStorSummaryeUiData;
import com.devinsight.vegiedo.data.ui.search.SearchStoreDetailUiData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityViewModel extends ViewModel {
    private MutableLiveData<TagStatus> tagStatusLiveData = new MutableLiveData<>();

    /* 스토어 상세 목록에 보여 줄 가게 데이터 */
    private MutableLiveData<List<SearchStoreDetailUiData>> storeListDetailLiveData = new MutableLiveData<>();

    /* 검색 창 클릭 시 간략하게 보여 줄 가게 데이터 */
    private MutableLiveData<List<SearchStorSummaryeUiData>> storeLiveData = new MutableLiveData<>();

    /* 필터링을 끝 낸 라이브 데이터 */
    private MutableLiveData<List<StoreListData>> storeFilteredLiveData = new MutableLiveData<>();
    /* 검색창에 보여줄 라이브 데이터 */
    private MutableLiveData<List<StoreListData>> storeSearchLiveData = new MutableLiveData<>();

    /* api 를 통해 서버로 부터 받은 가게 리스트 */
    private List<StoreListData> allStoreList;

    /* 위치 권한 허용 여부값을 담은 변수*/
    private final MutableLiveData<Boolean> isGranted = new MutableLiveData<>(false);

    /* 지도의 카드뷰에 보여줄 가게 리스트 라이브 데이터*/
    private MutableLiveData<List<MapStoreListData>> mapStoreLiveData = new MutableLiveData<>();


    /* Query 요청 및 필터에 사용 하기 위한 전역 변수*/
    private float userCurrentLat;
    private float userCurrentLong;
    private float mapLat;
    private float mapLong;
    private float latitude;
    private float longitude;
    private List<String> tags;
    private int distance;
    private String keyword;



    public void tagContent(boolean isChecked, String content, int btnId) {

        TagStatus tagStatus = new TagStatus(isChecked, content, btnId);

        if (isChecked) {
            tagStatus.setContent(content);
            tagStatus.setBtnId(btnId);
            tagStatus.setStatus(true);
        } else {
            tagStatus.setContent(content);
            tagStatus.setBtnId(btnId);
            tagStatus.setStatus(false);
        }

        tagStatusLiveData.setValue(tagStatus);
    }

    public LiveData<TagStatus> getTagStatusLiveData() {
        return tagStatusLiveData;
    }

    /* 유저의 현재 위도, 경도 값을 받아옵니다. */
    public void getCurrentLocationData(float latitude, float longitude) {
        this.userCurrentLat = latitude;
        this.userCurrentLong = longitude;

        Log.d("위치3", "위치3" + "위도 : " + latitude + "경도" + longitude);
    }

    /* 유저가 선택한 태그와, 거리를 가져옵니다.*/
    public void getFilterData(int distance, List<String> tags) {
        this.distance = distance;
        this.tags = tags;

        Log.d("필터 데이터 2", "거리 : " + distance + "태그 : " + tags.toString());
    }

    /* 검색창에서 유저가 입력한 text를 받아옵니다. */
    public void getSearchInputText(String searchText) {
        this.keyword = searchText;
    }

    /* 지도상에서 유저가 선택한 위치의 위도, 경도를 받아옵니다. */
    public void getCurrentMapLocationData(float mapLat, float mapLong) {
        this.mapLat = mapLat;
        this.mapLong = mapLong;

        Log.d("지도 위치", "지도 위치 " + "위도 : " + mapLat + "경도" + mapLong);
    }


//    public void getStoreList() {
//        /* 맵에서 선택한 위치가 없다면 */
//        boolean noMapLocation = mapLat + mapLong == 0.0f;
//
//        if (noMapLocation) {
//            latitude = userCurrentLat;
//            longitude = userCurrentLong;
//        } else {
//            latitude = mapLat;
//            longitude = mapLong;
//        }
//
//        Call<StoreListInquiryResponseDTO> call = RetrofitClient.getStoreApiService()
//                .getStoreList(
//                        tags,
//                        latitude,
//                        longitude,
//                        distance,
//                        keyword,
//                        10,
//                        3
//                );
//        call.enqueue(new Callback<StoreListInquiryResponseDTO>() {
//            @Override
//            public void onResponse(Call<StoreListInquiryResponseDTO> call, Response<StoreListInquiryResponseDTO> response) {
//                if (response.isSuccessful()) {
//                    StoreListInquiryResponseDTO storeList = response.body();
//                    allStoreList = storeList.getStores();
//                    storeFilteredLiveData.setValue(allStoreList);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<StoreListInquiryResponseDTO> call, Throwable t) {
//
//            }
//        });
//    }


    public List<StoreListData> dummyData() {
        List<StoreListData> storeList = new ArrayList<>(); // storeFilteredLiveData.getValue;

        storeList.add(new StoreListData(1l, "향림원", "서울특별시 강남구 삼성동 123-45", 37.500731f, 127.039338f, 5, 5, Arrays.asList("#비건", "#락토"), true, 45, ""));
        storeList.add(new StoreListData(2l, "서울테이블", "부산광역시 해운대구 우동 56-78", 37.494575f, 127.034612f, 5, 4, Arrays.asList("#프루테리언", "#비건"), true, 45, ""));
        storeList.add(new StoreListData(3l, "바다의 선물", "대구광역시 중구 동인동 90-12", 37.499176f, 127.041257f, 5, 5, Arrays.asList("#락토", "#오보"), true, 45, ""));
        storeList.add(new StoreListData(4l, "마루키친", "서울특별시 강남구 논현동 123-45", 37.492988f, 127.035923f, 5, 1, Arrays.asList("#락토 오보", "#페스코"), true, 45, ""));
        storeList.add(new StoreListData(5l, "송림정", "서울특별시 중구 을지로 56-78", 37.503657f, 127.036592f, 5, 3, Arrays.asList("#오보", "#락토 오보"), true, 45, ""));
        storeList.add(new StoreListData(6l, "파스텔레스토", "서울특별시 용산구 한강로 90-12", 37.492142f, 127.045137f, 5, 4, Arrays.asList("#페스코", "#폴로"), true, 45, ""));
        storeList.add(new StoreListData(7l, "그릴 101", "서울특별시 마포구 서교동 78-90", 37.498235f, 127.032479f, 5, 2, Arrays.asList("#폴로", "#키토"), true, 45, ""));
        storeList.add(new StoreListData(8l, "하늘정원", "대전광역시 유성구 신성동 12-34", 37.502658f, 127.040892f, 5, 3, Arrays.asList("#키토", "#글루텐프리"), true, 45, ""));
        storeList.add(new StoreListData(9l, "산바다물회", "울산광역시 남구 신정동 45-67", 37.496312f, 127.043285f, 5, 3, Arrays.asList("#락토", "#프루테리언"), true, 45, ""));
        storeList.add(new StoreListData(10l, "리베로 스테이크하우스", "서울특별시 동작구 사당동 45-67", 37.504978f, 127.037501f, 5, 4, Arrays.asList("#오보", "#글루텐프리"), true, 45, ""));

        return storeList;
    }

    public void searchList() {

        boolean noMapLocation = mapLat + mapLong == 0.0f;

        if (noMapLocation) {
            latitude = userCurrentLat;
            longitude = userCurrentLong;
        } else {
            latitude = mapLat;
            longitude = mapLong;
        }

        List<StoreListData> storeList = dummyData();
        List<StoreListData> filteredStoreList = new ArrayList<>();

        for (int i = 0; i < storeList.size(); i++) {

            /* 유저 위치 */
            Location userLocation = new Location("");
            userLocation.setLatitude(latitude);
            userLocation.setLongitude(longitude);

            /* 가게 위치 */
            Location storeLocation = new Location("");
            storeLocation.setLatitude(storeList.get(i).getLatitude());
            storeLocation.setLongitude(storeList.get(i).getLongitude());
            Log.d("가게 주소 목록","위도" + storeList.get(i).getLatitude());

            /* 유저 위치 to 가게 위치 (m) */
            float userToStore = userLocation.distanceTo(storeLocation);

            /* 거리 필터 충족 */
            boolean storeDistance = userToStore < distance * 1000;
            storeList.get(i).setDistance((int) userToStore);

            if (keyword != null) {

                String tag1 = storeList.get(i).getTags().get(0);
                String tag2 = storeList.get(i).getTags().get(1);

                /* 검색 에서 문자열 체크 */
                boolean storeNameFilter = storeList.get(i).getStoreName().toLowerCase().contains(keyword.toLowerCase());
                boolean storeAddressFilter = storeList.get(i).getAddress().toLowerCase().contains(keyword.toLowerCase());
                boolean storeTagFilter = tag1.contains(keyword.toLowerCase());
                boolean storeTagFilter2 = tag2.contains(keyword.toLowerCase());

                /* 가게 이름, 가게 주소, 태그 문자열 중 하나 포함 */
                boolean stringFilter = storeNameFilter || storeAddressFilter || storeTagFilter || storeTagFilter2;


                boolean isTagMatched = false;
                if (tags != null)
                    for (String userTag : tags) {
                        for (String storeTag : storeList.get(i).getTags()) {
                            if (userTag.contentEquals(storeTag)) {
                                isTagMatched = true;
                                break;
                            }
                        }
                        if (isTagMatched) {
                            break; // 하나라도 일치하는 태그를 찾았으면 더 이상 검사하지 않습니다.
                        }
                    }

                if (storeDistance || isTagMatched || stringFilter) {
                    filteredStoreList.add(storeList.get(i));
                }
            }

            storeFilteredLiveData.setValue(filteredStoreList);
        }

    }
    public List<MapStoreListData> mapDummyData() {

        List<MapStoreListData> mapStoreList = new ArrayList<>();

        mapStoreList.add(new MapStoreListData(1l, "향림원", "서울특별시 강남구 삼성동 123-45", 37.500731f, 127.039338f, 5, Arrays.asList("#비건", "#락토"), 5, true, true, 40, ""));
        mapStoreList.add(new MapStoreListData(2l, "서울테이블", "부산광역시 해운대구 우동 56-78", 37.494575f, 127.034612f, 5, Arrays.asList("#프루테리언", "#비건"), 4, false, true, 45, ""));
        mapStoreList.add(new MapStoreListData(3l, "바다의 선물", "대구광역시 중구 동인동 90-12", 37.499176f, 127.041257f, 5, Arrays.asList("#락토", "#오보"), 5, true, true, 24, ""));
        mapStoreList.add(new MapStoreListData(4l, "마루키친", "서울특별시 강남구 논현동 123-45", 37.492988f, 127.035923f, 5, Arrays.asList("#락토 오보", "#페스코"), 1, true, true, 5, ""));
        mapStoreList.add(new MapStoreListData(5l, "송림정", "서울특별시 중구 을지로 56-78", 37.503657f, 127.036592f, 5, Arrays.asList("#오보", "#락토 오보"), 3, true, true, 8, ""));
        mapStoreList.add(new MapStoreListData(6l, "파스텔레스토", "서울특별시 용산구 한강로 90-12", 37.492142f, 127.045137f, 5, Arrays.asList("#페스코", "#폴로"), 4, false, true, 2, ""));
        mapStoreList.add(new MapStoreListData(7l, "그릴 101", "서울특별시 마포구 서교동 78-90", 37.498235f, 127.032479f, 5, Arrays.asList("#폴로", "#키토"), 2, true, true, 15, ""));
        mapStoreList.add(new MapStoreListData(8l, "하늘정원", "대전광역시 유성구 신성동 12-34", 37.502658f, 127.040892f, 5, Arrays.asList("#키토", "#글루텐프리"), 3, true, true, 45, ""));
        mapStoreList.add(new MapStoreListData(9l, "산바다물회", "울산광역시 남구 신정동 45-67", 37.496312f, 127.043285f, 5, Arrays.asList("#락토", "#프루테리언"), 3, false, true, 58, ""));
        mapStoreList.add(new MapStoreListData(10l, "리베로 스테이크하우스", "서울특별시 동작구 사당동 45-67", 37.504978f, 127.037501f, 5, Arrays.asList("#오보", "#글루텐프리"), 4, true, true, 45, ""));

        return mapStoreList;
    }

    public MutableLiveData<List<MapStoreListData>> getMapStoreLiveData() {
        List<MapStoreListData> mapStoreList = mapDummyData();
//        List<MapStoreListData> filteredStoreList = new ArrayList<>();
//        for (int i = 0; i < mapStoreList.size(); i++) {
//            Log.d("맵 가게 리스트", mapStoreList.get(i).getStoreName());
//        }
        mapStoreLiveData.setValue(mapStoreList);
        return mapStoreLiveData;
    }

    public void setMapStoreLiveData(MutableLiveData<List<MapStoreListData>> mapStoreLiveData) {
        this.mapStoreLiveData = mapStoreLiveData;
    }


    public LiveData<List<StoreListData>> getFilteredStoreList() {
        return storeFilteredLiveData;
    }

    public LiveData<List<StoreListData>> getStoreSearchList() {
        return storeSearchLiveData;
    }

    public LiveData<Boolean> isGranted() {
        return isGranted;
    }

    public void setGranted(boolean granted) {
        isGranted.setValue(granted);
    }





}



