package com.devinsight.vegiedo.view.search;

import static com.devinsight.vegiedo.utill.RetrofitClient.getStoreApiService;
import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.response.MapStoreListData;
import com.devinsight.vegiedo.data.response.StoreListData;
import com.devinsight.vegiedo.data.response.StoreListInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.response;
import com.devinsight.vegiedo.data.ui.login.TagStatus;
import com.devinsight.vegiedo.data.ui.map.MapStoreCardUiData;
import com.devinsight.vegiedo.data.ui.search.SearchStorSummaryeUiData;
import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;
import com.devinsight.vegiedo.service.api.StoreApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.view.StoreListMainFragment;
import com.devinsight.vegiedo.view.store.StoreDetailData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewModel extends ViewModel {
    private MutableLiveData<TagStatus> tagStatusLiveData = new MutableLiveData<>();

    /* 스토어 상세 목록에 보여 줄 가게 데이터 */
    private MutableLiveData<List<SummaryData>> storeListSummaryLiveData = new MutableLiveData<>();

    private MutableLiveData<List<SummaryData>> storeListCurrentLiveData = new MutableLiveData<>();

    /* 검색 창 클릭 시 간략하게 보여 줄 가게 데이터 */
    private MutableLiveData<List<SearchStorSummaryeUiData>> storeLiveData = new MutableLiveData<>();

    /* 필터링을 끝 낸 라이브 데이터 */
    private MutableLiveData<List<StoreListData>> storeFilteredLiveData = new MutableLiveData<>();
    /* 검색창에 보여줄 라이브 데이터 */
    private MutableLiveData<List<SummaryData>> storeSearchLiveData = new MutableLiveData<>();
    /* 검색창에 입력 된 텍스트 라이브 데이터 */
    private MutableLiveData<String> inputTextLiveData = new MutableLiveData<>();

    /* api 를 통해 서버로 부터 받은 가게 리스트 */
    private List<StoreListData> allStoreList;

    /* 위치 권한 허용 여부값을 담은 변수*/
    private final MutableLiveData<Boolean> isGranted = new MutableLiveData<>(false);

    /* 지도의 카드뷰에 보여줄 가게 리스트 라이브 데이터*/
    private MutableLiveData<List<MapStoreListData>> mapStoreLiveData = new MutableLiveData<>();

    private MutableLiveData<List<MapStoreCardUiData>> mapStoreUiLiveData = new MutableLiveData<>();
    /* 서버에서 내려주는 가게 리스트 */

    private MutableLiveData<List<StoreListData>> storeListLiveData = new MutableLiveData<>();

    AuthPrefRepository authPrefRepository;


    /* Query 요청 및 필터에 사용 하기 위한 전역 변수*/
    private float userCurrentLat;
    private float userCurrentLong;
    private float mapLat;
    private float mapLong;
    private float latitude;
    private float longitude;
    private List<String> tags;
    private List<String> initialTags;
    private int distance;
    private int initialDistance;
    private String keyword;

    private String currentInput;
    private String token;


    StoreApiService storeApiService = RetrofitClient.getStoreApiService();


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

    /* 유저의 최초 필터링 셋 */
    public void getInitialFilteredData(int distance, List<String> tags, String token) {
        this.initialDistance = distance;
        this.initialTags = tags;
        this.token = token;
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

        inputTextLiveData.setValue(keyword);
    }

    /* 지도상에서 유저가 선택한 위치의 위도, 경도를 받아옵니다. */
    public void getCurrentMapLocationData(float mapLat, float mapLong) {
        this.mapLat = mapLat;
        this.mapLong = mapLong;

        Log.d("지도 위치", "지도 위치 " + "위도 : " + mapLat + "경도" + mapLong);
    }

    /* 실시간으로 입력 받는 검색어 */
    private LiveData<String> getInputText() {
        return inputTextLiveData;
    }

    public List<StoreListData> dummyData() {
        List<StoreListData> storeList = new ArrayList<>(); // storeFilteredLiveData.getValue;

        storeList.add(new StoreListData(1l, "향림원", "서울특별시 강남구 삼성동 123-45", 37.500731f, 127.039338f, 5, 5, Arrays.asList("#비건", "#락토"), true, 45, "https://cdn.pixabay.com/photo/2014/04/13/20/49/cat-323262_640.jpg"));
        storeList.add(new StoreListData(2l, "서울테이블", "부산광역시 해운대구 우동 56-78", 37.494575f, 127.034612f, 5, 4, Arrays.asList("#프루테리언", "#비건"), true, 45, "https://cdn.pixabay.com/photo/2016/05/24/16/48/mountains-1412683_640.png"));
        storeList.add(new StoreListData(3l, "바다의 선물", "대구광역시 중구 동인동 90-12", 37.499176f, 127.041257f, 5, 5, Arrays.asList("#락토", "#오보"), true, 45, "https://cdn.pixabay.com/photo/2018/08/12/15/29/hintersee-3601004_640.jpg"));
        storeList.add(new StoreListData(4l, "마루키친", "서울특별시 강남구 논현동 123-45", 37.492988f, 127.035923f, 5, 1, Arrays.asList("#락토 오보", "#페스코"), true, 45, "https://cdn.pixabay.com/photo/2018/01/30/22/50/forest-3119826_640.jpg"));
        storeList.add(new StoreListData(5l, "송림정", "서울특별시 중구 을지로 56-78", 37.503657f, 127.036592f, 5, 3, Arrays.asList("#오보", "#락토 오보"), true, 45, "https://cdn.pixabay.com/photo/2018/12/15/18/02/forest-3877365_640.jpg"));
        storeList.add(new StoreListData(6l, "파스텔레스토", "서울특별시 용산구 한강로 90-12", 37.492142f, 127.045137f, 5, 4, Arrays.asList("#페스코", "#폴로"), true, 45, "https://cdn.pixabay.com/photo/2016/09/04/20/09/mountains-1645078_640.jpg"));
        storeList.add(new StoreListData(7l, "그릴 101", "서울특별시 마포구 서교동 78-90", 37.498235f, 127.032479f, 5, 2, Arrays.asList("#폴로", "#키토"), true, 45, "https://cdn.pixabay.com/photo/2018/07/14/17/46/raccoon-3538081_640.jpg"));
        storeList.add(new StoreListData(8l, "하늘정원", "대전광역시 유성구 신성동 12-34", 37.502658f, 127.040892f, 5, 3, Arrays.asList("#키토", "#글루텐프리"), true, 45, "https://cdn.pixabay.com/photo/2020/12/23/14/41/forest-5855196_640.jpg"));
        storeList.add(new StoreListData(9l, "산바다물회", "울산광역시 남구 신정동 45-67", 37.496312f, 127.043285f, 5, 3, Arrays.asList("#락토", "#프루테리언"), true, 45, "https://cdn.pixabay.com/photo/2014/07/28/20/39/sunset-404072_640.jpg"));
        storeList.add(new StoreListData(10l, "리베로 스테이크하우스", "서울특별시 동작구 사당동 45-67", 37.504978f, 127.037501f, 5, 4, Arrays.asList("#오보", "#글루텐프리"), true, 45, "https://cdn.pixabay.com/photo/2018/08/21/23/29/forest-3622519_640.jpg"));

        return storeList;
    }

    public void storeApiData() {
        Log.d("api 가져오는 함수 ", "  public void storeApiData() ");
        boolean noMapLocation = mapLat + mapLong == 0.0f;

        if (noMapLocation) {
            latitude = userCurrentLat;
            longitude = userCurrentLong;
        } else {
            latitude = mapLat;
            longitude = mapLong;
        }

        if (tags == null) {
            tags = initialTags;
        }

        if (distance == 0) {
            distance = initialDistance;
        }
        Log.d("여기까지 됨", "194번줄");

        storeApiService.getStoreLists(tags, latitude, longitude, distance, keyword, 10, 1, token).enqueue(new Callback<List<StoreListData>>() {
            @Override
            public void onResponse(Call<List<StoreListData>> call, Response<List<StoreListData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<StoreListData> data = response.body();
                    storeListLiveData.setValue(data);
                    searchSummList();
                    Log.e("성공","가져왔어요" + data.get(0).getStoreName());
                }
            }

            @Override
            public void onFailure(Call<List<StoreListData>> call, Throwable t) {
                Log.e("실패", "실패" + t.getMessage());
            }

        });

    }

    /* 최근 입력된 마지막 검색어 */
    public void getCurrentInput(String input) {
        this.currentInput = input;
    }

    /* 더미데이터에서 필요한 부분*/
    public void searchSummList() {
//        List<StoreListData> storeListData = dummyData();
        Log.d("필요 데이터 추출 함수"," public void searchSummList()");
        List<StoreListData> storeListData = storeListLiveData.getValue();
        List<SummaryData> summaryList = new ArrayList<>();
        for (StoreListData data : storeListData) {
            SummaryData summaryData = new SummaryData();
            summaryData.setStoreImage(data.getImages());
            summaryData.setStoreName(data.getStoreName());
            summaryData.setStoreAddress(data.getAddress());

            summaryList.add(summaryData);
        }

        storeListSummaryLiveData.setValue(summaryList);
    }

    public MutableLiveData<List<MapStoreCardUiData>> getMapStoreSummaryData() {

        List<MapStoreListData> originData = mapDummyData();
        List<MapStoreCardUiData> mapStoreCardUiDataList = new ArrayList<>();

        for (MapStoreListData data : originData) {
            MapStoreCardUiData mapStoreCardUiData = new MapStoreCardUiData();
            mapStoreCardUiData.setData(data.getImages(), data.getTags().get(0), data.getTags().get(1), data.getReviewCount(), data.getStars(),
                    data.getDistance(), data.getStoreName(), data.getAddress(), data.getLike(), data.getLatitude(), data.getLongitude());
            mapStoreCardUiDataList.add(mapStoreCardUiData);
        }
        mapStoreUiLiveData.setValue(mapStoreCardUiDataList);

        return mapStoreUiLiveData;
    }

    public List<MapStoreListData> mapDummyData() {

        List<MapStoreListData> mapStoreList = new ArrayList<>();

        mapStoreList.add(new MapStoreListData(1l, "향림원", "서울특별시 강남구 삼성동 123-45", 37.500731f, 127.039338f, 5, Arrays.asList("#비건", "#락토"), 5, true, true, 40, ""));
        mapStoreList.add(new MapStoreListData(2l, "서울테이블", "부산광역시 해운대구 우동 56-78", 35.1745200144f, 129.12693731157f, 5, Arrays.asList("#프루테리언", "#비건"), 4, false, true, 45, ""));
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
        mapStoreLiveData.setValue(mapStoreList);
        return mapStoreLiveData;
    }

    public void setMapStoreLiveData(MutableLiveData<List<MapStoreListData>> mapStoreLiveData) {
        this.mapStoreLiveData = mapStoreLiveData;
    }

    /* 최근검색어에 따른 리스트 */
    public void currentList() {
        Log.d("최근 검색어에 따른 리스트 ","public void currentList()");
        if (currentInput != null) {
            List<SummaryData> summaryList = storeListSummaryLiveData.getValue();
            List<SummaryData> currentList = new ArrayList<>();
            for (int i = 0; i < summaryList.size(); i++) {
                if (summaryList.get(i).getStoreName().toLowerCase().contains(currentInput.toLowerCase()) || summaryList.get(i).getStoreAddress().toLowerCase().contains(currentInput.toLowerCase())) {
                    currentList.add(summaryList.get(i));
                }

            }
            storeListCurrentLiveData.setValue(currentList);
        } else {
            storeListCurrentLiveData.setValue(null);
        }
    }

    /* 요약리스트에서 실시간 검색 */
    public void searchSummaryListByKeyword(String input) {
        Log.d("요약리스트 실시간 검색","public void searchSummaryListByKeyword(String input)");
        if (storeListSummaryLiveData != null) {
            List<SummaryData> storeList = storeListSummaryLiveData.getValue();
            List<SummaryData> filteredList = new ArrayList<>();
            for (int i = 0; i < storeListSummaryLiveData.getValue().size(); i++) {
                if (storeList.get(i).getStoreName().toLowerCase().contains(input.toLowerCase()) || storeList.get(i).getStoreAddress().toLowerCase().contains(input.toLowerCase())) {
                    filteredList.add(storeList.get(i));
                }
            }
            storeSearchLiveData.setValue(filteredList);
        }
    }


    /* 필터링을 통해 스토어 메인 리스트를 보여주기 위한 함수 */
    public void searchDetailList() {
        Log.d("필터함수", "필터함수 발동!");

        boolean noMapLocation = mapLat + mapLong == 0.0f;

        if (noMapLocation) {
            latitude = userCurrentLat;
            longitude = userCurrentLong;
        } else {
            latitude = mapLat;
            longitude = mapLong;
        }

        if (tags == null) {
            tags = initialTags;
        }

        if (distance == 0) {
            distance = initialDistance;
        }

        Log.d("필터링 데이터 ", "위도, 경도" + latitude + longitude + " 거리 : " + distance + " 태그 : " + tags.toArray().toString());

//        List<StoreListData> storeList = dummyData();
        List<StoreListData> storeList = storeListLiveData.getValue();
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
//            Log.d("가게 주소 목록", "위도" + storeList.get(i).getLatitude());

            /* 유저 위치 to 가게 위치 (m) */
            float userToStore = userLocation.distanceTo(storeLocation);

            /* 거리 필터 충족 */
            boolean storeDistance = userToStore < distance * 1000;
            storeList.get(i).setDistance((int) userToStore);
            Log.d("거리필터링 성공","해당 범위 입니다" + (storeList.get(i).getDistance() < distance * 1000));

            if (keyword != null) {
                List<String> storeTags = storeList.get(i).getTags();
                if (storeTags == null){
                }
                String tag1 = storeTags.size() > 0 ? storeTags.get(0) : "";
                String tag2 = storeTags.size() > 1 ? storeTags.get(1) : "";
//                String tag1 = storeList.get(i).getTags().get(0);
//                String tag2 = storeList.get(i).getTags().get(1);

                /* 검색 에서 문자열 체크 */
                boolean storeNameFilter = storeList.get(i).getStoreName().toLowerCase().contains(keyword.toLowerCase());
                boolean storeAddressFilter = storeList.get(i).getAddress().toLowerCase().contains(keyword.toLowerCase());
                boolean storeTagFilter = tag1.contains(keyword.toLowerCase());
                boolean storeTagFilter2 = tag2.contains(keyword.toLowerCase());

                /* 가게 이름, 가게 주소, 태그 문자열 중 하나 포함 */
                boolean stringFilter = storeNameFilter || storeAddressFilter || storeTagFilter || storeTagFilter2;

                /* 가게의 태그 == 유저 태그 ? */
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

                if (storeDistance && (isTagMatched || stringFilter)) {
                    filteredStoreList.add(storeList.get(i));
                }
//                storeFilteredLiveData.setValue(filteredStoreList);
                Log.d("필터링1","필터링1" + filteredStoreList.get(i).getStoreName());
            } else {
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
                if (storeDistance || isTagMatched) {
                    filteredStoreList.add(storeList.get(i));
//                    Log.d("필터링2","필터링2" + filteredStoreList.get(filteredStoreList.size()).getStoreName());
                }
//                storeFilteredLiveData.setValue(filteredStoreList);

            }
            storeFilteredLiveData.setValue(filteredStoreList);
        }
    }


    public LiveData<List<StoreListData>> getFilteredStoreListLiveData() {
        return storeFilteredLiveData;
    }

    public LiveData<List<SummaryData>> getStoreSearchListByKeywordLiveData() {
        return storeSearchLiveData;
    }

    public LiveData<List<SummaryData>> getSummaryListLiveData() {
        return storeListSummaryLiveData;
    }

    public LiveData<List<SummaryData>> getCurrentListLiveData() {
        return storeListCurrentLiveData;
    }

    public LiveData<Boolean> isGranted() {
        return isGranted;
    }

    public LiveData<List<StoreListData>> getStoreListLiveData() {
        return storeListLiveData;
    }

    public void setGranted(boolean granted) {
        isGranted.setValue(granted);
    }


}



