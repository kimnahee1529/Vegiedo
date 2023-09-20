package com.devinsight.vegiedo.view.search;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.request.ReviewModifyrRequestDTO;
import com.devinsight.vegiedo.data.request.ReviewRegisterRequestDTO;
import com.devinsight.vegiedo.data.request.ReviewReportRequestDTO;
import com.devinsight.vegiedo.data.request.StoreReportRequestDTO;
import com.devinsight.vegiedo.data.request.UserNicknameModifyRequestDTO;
import com.devinsight.vegiedo.data.response.CommentListData;
import com.devinsight.vegiedo.data.response.MapInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.MapStoreListData;
import com.devinsight.vegiedo.data.response.PostInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.PostListData;
import com.devinsight.vegiedo.data.response.ReviewListInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.StampBookInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.StoreInquiryResponseDTO;
import com.devinsight.vegiedo.data.response.StoreListData;
import com.devinsight.vegiedo.data.response.StoreStampDetailDTO;
import com.devinsight.vegiedo.data.ui.login.TagStatus;
import com.devinsight.vegiedo.data.ui.map.MapStoreCardUiData;
import com.devinsight.vegiedo.data.ui.search.SearchStorSummaryeUiData;
import com.devinsight.vegiedo.repository.pref.AuthPrefRepository;
import com.devinsight.vegiedo.service.api.CommentApiService;
import com.devinsight.vegiedo.service.api.MapApiService;
import com.devinsight.vegiedo.service.api.PostApiService;
import com.devinsight.vegiedo.service.api.ReviewApiService;
import com.devinsight.vegiedo.service.api.StoreApiService;
import com.devinsight.vegiedo.service.api.UserApiService;
import com.devinsight.vegiedo.utill.RetrofitClient;
import com.devinsight.vegiedo.view.community.ClickedPostData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewModel extends ViewModel {
    private MutableLiveData<TagStatus> tagStatusLiveData = new MutableLiveData<>();

    /* 스토어 상세 목록에 보여 줄 가게 데이터 */
    private MutableLiveData<List<SummaryData>> storeListSummaryLiveData = new MutableLiveData<>();
    /* 최근 검색 목록 리스트*/
    private MutableLiveData<List<SummaryData>> storeListCurrentLiveData = new MutableLiveData<>();

    /* 검색 창 클릭 시 간략하게 보여 줄 가게 데이터 */
    private MutableLiveData<List<SearchStorSummaryeUiData>> storeLiveData = new MutableLiveData<>();

    /* 필터링을 끝 낸 라이브 데이터 */
    private MutableLiveData<List<StoreListData>> storeFilteredLiveData = new MutableLiveData<>();
    /* 검색창에 보여줄 라이브 데이터 */
    private MutableLiveData<List<SummaryData>> storeSearchLiveData = new MutableLiveData<>();
    /* 검색창에 입력 된 텍스트 라이브 데이터 */
    private MutableLiveData<String> inputTextLiveData = new MutableLiveData<>();

    /* 서버에서 내려주는 가게 리스트 */

    private MutableLiveData<List<StoreListData>> storeListLiveData = new MutableLiveData<>();
    /* 유저 토큰 전달을 위한 라이브데이터 */
    private MutableLiveData<String> tokenLiveData = new MutableLiveData<>();

    AuthPrefRepository authPrefRepository;

    /* api 를 통해 서버로 부터 받은 가게 리스트 */
    private List<StoreListData> allStoreList;

    //가게-가게 조회 API 호출에서 쓸 라이브 데이터
    private MutableLiveData<StoreInquiryResponseDTO> storeDataLiveData = new MutableLiveData<>();

    //가게-신고 API 호출에서 쓸 라이브 데이터
    private MutableLiveData storeReportDataLiveData = new MutableLiveData<>();

    //가게-스탬프 API 호출에서 쓸 라이브 데이터
    private MutableLiveData storeStampLiveData = new MutableLiveData<>();

    //가게-찜버튼 API 호출에서 쓸 라이브 데이터
    private MutableLiveData storeLikeLiveData = new MutableLiveData<>();

    //지도-가게 조회 API 호출에서 쓸 라이브 데이터
    private MutableLiveData<List<MapStoreListData>> mapStoreLiveData = new MutableLiveData<>();
    // 지도의 카드뷰에 보여줄 가게 리스트 라이브 데이터
    private MutableLiveData<List<MapStoreCardUiData>> mapStoreUiLiveData = new MutableLiveData<>();


    //리뷰-리뷰 조회 API 호출에서 쓸 라이브 데이터
    private MutableLiveData<ReviewListInquiryResponseDTO> reviewLiveData = new MutableLiveData<>();

    //리뷰-블로그 리뷰 조회 API 호출에서 쓸 라이브 데이터
    private MutableLiveData<ReviewListInquiryResponseDTO> blogReviewLiveData = new MutableLiveData<>();
    //리뷰-작성한 리뷰가 있는지 여부를 확인하는 라이브 데이터
    private final MutableLiveData<Boolean> canWriteReview = new MutableLiveData<>(true);

    //리뷰-리뷰 신고 API 호출에서 쓸 라이브 데이터
    private MutableLiveData reviewReportDataLiveData = new MutableLiveData<>();

    //유저-사용자 닉네임 변경에서 쓸 라이브 데이터
    private MutableLiveData userNickNameDataLiveData = new MutableLiveData<>();
    //유저-사용자 프로필 이미지 변경


    //스탬프북에서 쓸 라이브 데이터
    private MutableLiveData<StampBookInquiryResponseDTO> stampBookDataLiveData = new MutableLiveData<>();

    /* 게시글 종류 확인 라이브 데이터*/
    private MutableLiveData<Boolean> postTypeLiveData = new MutableLiveData<>();

    private MutableLiveData<PostListData> postClickedLiveData = new MutableLiveData<>();

    private MutableLiveData<PostInquiryResponseDTO> postContentLiveData = new MutableLiveData<>();

    private MutableLiveData<List<CommentListData>> postCommentLiveData = new MutableLiveData<>();

    private MutableLiveData<Long> postIdLiveData = new MutableLiveData<>();

    private MutableLiveData<List<String>> imageUrlListForModifyLiveData = new MutableLiveData<>();


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
    //TODO 토큰 적어놓은 거 지우기
    private String token;

    private Long postId;

    private List<Uri> imageUriList;


    ClickedPostData postData;

    // 선택한 가게를 알기 위한 storeId(StoreReviewFragment에서 본인이 쓴 리뷰 수정, 삭제하기 위함)
    private MutableLiveData<Long> storeId = new MutableLiveData<>();

    /* API 호출을 위한 레트로핏 초기화 */
    UserApiService userApiService = RetrofitClient.getUserApiService();
    StoreApiService storeApiService = RetrofitClient.getStoreApiService();
    MapApiService mapApiService = RetrofitClient.getMapApiService();
    ReviewApiService reviewApiService = RetrofitClient.getReviewApiService();
    CommentApiService commentApiService = RetrofitClient.getCommentApiService();
    PostApiService postApiService = RetrofitClient.getPostApiService();



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
        tokenLiveData.setValue(token);
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

    public void getDistance( int distance){
        this.distance = distance;
    }



    /* 실시간으로 입력 받는 검색어 */
    private LiveData<String> getInputText() {
        return inputTextLiveData;
    }

    /* 가게 리스트 API 호출 */
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
        Log.d("쿼리 재료","tag : " + tags + "latitude : " + latitude + "longitude : " + longitude );
        Log.e("store List 요청 동작", "store List 요청 동작");

        storeApiService.getStoreLists(tags, latitude, longitude, distance*1000, keyword, 10, 0, "Bearer " +  token).enqueue(new Callback<List<StoreListData>>() {
            @Override
            public void onResponse(Call<List<StoreListData>> call, Response<List<StoreListData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(" 가게 호출 쿼리","값" + tags + latitude + longitude + "거리 : " + distance + keyword + token);
                    List<StoreListData> data = response.body();
                    if ( data != null ) {
                        Log.e("store List 요청 성공","this is store List : " +data.toArray().toString());
                        storeListLiveData.setValue(data);
                        searchSummList();
                    } else {
                        Log.d(" 해당하는 가게 리스트가 없습니다","해당하는 가게 리스트가 없습니다" + data.size());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<StoreListData>> call, Throwable t) {
                Log.e("store List 요청 실패 2", "store List 요청 실패 2" + t.getMessage());
            }

        });

        Log.e("store List 요청 동작", "store List 요청 동작");

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

    public MutableLiveData<List<MapStoreListData>> getMapStoreLiveData() {
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
            if(storeDistance) {
                Log.d("거리필터링 성공","해당 범위 입니다" + i + (storeList.get(i).getDistance() < distance * 1000));

            }
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
//                Log.d("필터링1","필터링1" + filteredStoreList.get(i).getStoreName());
            } else {
                Log.d("키워드가 없을 때"," 발동 !");
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
            Log.d(" 데이터 저장2 ","데이터 저장2" );

            for( int k = 0 ; k < filteredStoreList.size() ; k ++ ) {
                Log.e("k","k " + k + " " + filteredStoreList.get(k).getDistance() + "필터링 거리 " + distance );
            }
        }

    }

    //가게 조회 API(StoreDetailPageFragment서 사용)
    public void StoreInquiryData(Long storeId) {
        Log.d("토큰", token);
        //가게 조회
        storeApiService.readStore("Bearer " + token, storeId).enqueue(new Callback<StoreInquiryResponseDTO>() {
            @Override
            public void onResponse(Call<StoreInquiryResponseDTO> call, Response<StoreInquiryResponseDTO> response) {
                if(response.isSuccessful()){
                    Log.d("StoreAPI 성공","StoreAPI 호출 성공");
                } else {
                    // API 응답이 오류 상태일 때
                    Log.e("StoreAPI실패 1", "Error Code: " + response.code() + ", Message: " + response.message());
                    try {
                        Log.e("StoreAPI실패 1", "Error Body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //                if (response.isSuccessful()) {
//                    storeDataLiveData.setValue(response.body());
//                    Log.d("LOGAPI", "StoreAPI 호출");
//                } else {
//                    Log.d("LOGAPI", "StoreAPI 호출실패 "+response.);
//                }
            }

            @Override
            public void onFailure(Call<StoreInquiryResponseDTO> call, Throwable t) {
                Log.d("LOGAPI", "StoreAPI 호출실패2");
            }
        });
    }

    //가게 신고(폐점)
    public void StoreReportData(Long storeId) {
        //가게 신고(폐점)
        storeApiService.reportStore("Bearer " + token,storeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("LOGAPI", "StoreReportAPI 호출 "+response.code());
                    storeReportDataLiveData.setValue(response.code());
                } else {
                    Log.d("LOGAPI", "StoreReportAPI 호출실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("LOGAPI", "StoreReportAPI 호출실패2");
            }
        });
    }
    public void resetStoreReportData() {
        storeReportDataLiveData.setValue(null);
    }

    //여기서 호출한 api 함수를 StoreDetailPageFragment서 씀
    public LiveData<StoreInquiryResponseDTO> getStoreDataLiveData() {
        return storeDataLiveData;
    }

    //가게 스탬프 활성화
    public void StoreActiveStampData(Long storeId) {
        //가게 스탬프 활성화
        storeApiService.activeStamp("Bearer " + token, storeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    storeStampLiveData.setValue(response.code());
                    Log.d("stampAPI", ""+response);
                } else {
                    storeStampLiveData.setValue(response.code());
                    Log.d("stampAPI", "STAMPAPI 호출실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                storeStampLiveData.setValue(-1);
            }
        });
    }

    //가게 스탬프 취소
    public void StoreInactiveStampData(Long storeId) {
        //가게 스탬프 취소
        storeApiService.inactiveStamp("Bearer " + token, storeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    storeStampLiveData.setValue(response.code());
                    Log.d("stampAPI", ""+response);
                } else {
                    storeStampLiveData.setValue(response.code());
                    Log.d("stampAPI", "STAMPAPI 호출실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                storeStampLiveData.setValue(-1);
                Log.d("stampAPI", "STAMPAPI 호출실패");
            }
        });
    }

    //가게 찜버튼 활성화
    public void StoreActiveLikeData(Long storeId) {
        //가게 찜버튼 활성화
        storeApiService.likeStore("Bearer " + token, storeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    storeLikeLiveData.setValue(response.code());
                    Log.d("likeAPI", ""+response);
                } else {
                    storeLikeLiveData.setValue(response.code());
                    Log.d("likeAPI", "LIKEAPI 호출실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                storeLikeLiveData.setValue(-1);
                Log.d("likeAPI", "LIKEAPI 호출실패");
            }
        });
    }
    //가게 찜버튼 취소
    public void StoreInactiveLikeData(Long storeId) {
        //가게 찜버튼 활성화
        storeApiService.cancleLikeStore("Bearer " + token, storeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    storeLikeLiveData.setValue(response.code());
                    Log.d("likeAPI", ""+response);
                } else {
                    storeLikeLiveData.setValue(response.code());
                    Log.d("likeAPI", "LIKEAPI 호출실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                storeLikeLiveData.setValue(-1);
                Log.d("likeAPI", "LIKEAPI 호출실패");
            }
        });
    }

    //스탬프북
//    public void MypageStampBookData() {
//        //가게 찜버튼 활성화
//        storeApiService.myPageStampBook("Bearer " + token).enqueue(new Callback<StampBookInquiryResponseDTO>() {
//            @Override
//            public void onResponse(Call<StampBookInquiryResponseDTO> call, Response<StampBookInquiryResponseDTO> response) {
//                if (response.isSuccessful()) {
//                    stampBookDataLiveData.setValue(response.code());
//                    Log.d("likeAPI", ""+response);
//                } else {
////                    stampBookDataLiveData.setValue(response.code());
//                    Log.d("likeAPI", "LIKEAPI 호출실패");
//                }
//            }
//
//        });
//    }
    public void MyPageStampBookData() {
        storeApiService.myPageStampBook("Bearer " + token).enqueue(new Callback<StampBookInquiryResponseDTO>() {
            @Override
            public void onResponse(Call<StampBookInquiryResponseDTO> call, Response<StampBookInquiryResponseDTO> response) {
                if (response.isSuccessful()) {
                    stampBookDataLiveData.setValue(response.body());

                    // 각 필드 값 가져오기
//                    String storeName = storeDetail.getStoreName();
//                    String address = storeDetail.getAddress();
//                    int stars = storeDetail.getStars();
//                    int reviewCount = storeDetail.getReviewCount();
//                    String images = storeDetail.getImages();
//
//                    // 로그로 값 확인하기
//                    Log.d("stampAPI", "가게 이름: " + storeName);
//                    Log.d("stampAPI", "주소: " + address);
//                    Log.d("stampAPI", "별점: " + stars);
//                    Log.d("stampAPI", "리뷰 수: " + reviewCount);
//                    Log.d("stampAPI", "이미지 URL: " + images);
                } else {
                    Log.d("stampAPI", "stampAPI 호출실패1");
                }
            }

            @Override
            public void onFailure(Call<StampBookInquiryResponseDTO> call, Throwable t) {
                Log.d("stampAPI", "stampAPI 호출실패2");
            }
        });
    }


    //지도 가게 조회 API(MapMainFragment에서 사용)
    public void MapInquiryData() {
        float latitude = 37.559254f;
        float longitude = 126.985985f;
        Integer distance = 1500;

        mapApiService.getStoresOnMap("Bearer " + token, latitude, longitude, distance).enqueue(new Callback<List<MapStoreListData>>() {
            @Override
            public void onResponse(Call<List<MapStoreListData>> call, Response<List<MapStoreListData>> response) {
                if (response.isSuccessful()) {
                    mapStoreLiveData.setValue(response.body());
                    Log.d("LOGAPIMapInquiryData", ""+response);
                } else {
                    Log.d("LOGAPIMapInquiryData", "AMPAPI 호출실패");
                }
            }

            @Override
            public void onFailure(Call<List<MapStoreListData>> call, Throwable t) {
                Log.d("LOGAPIMapInquiryData", "AMPAPI 호출실패2");
            }
        });
    }

    //리뷰
    //리뷰 조회 API(StoreDetailPageFragment서 사용)
    public void ReviewInquiryData(Long storeId, int count, int cursor, boolean blogReview) {
        //리뷰 조회
        Call<ReviewListInquiryResponseDTO> reviewListInquiryResponseDTOCall = reviewApiService.getReviews("Bearer " + token, storeId, count, cursor, blogReview);
        reviewListInquiryResponseDTOCall.enqueue(new Callback<ReviewListInquiryResponseDTO>() {
            @Override
            public void onResponse(Call<ReviewListInquiryResponseDTO> call, Response<ReviewListInquiryResponseDTO> response) {
                if (response.isSuccessful()) {
                    Log.d("LOGAPI", "ReviewAPI 호출성공1 "+response);
                    ReviewListInquiryResponseDTO responseData = response.body();
                    Log.d("LOGAPI", "ReviewAPI 호출성공2 "+ responseData.getReviews());
                    if(blogReview){
                        blogReviewLiveData.setValue(responseData);
                    }else{
                        reviewLiveData.setValue(responseData);
                    }
                } else {
                    Log.d("LOGAPI", "ReviewAPI 호출실패2");
                }
            }

            @Override
            public void onFailure(Call<ReviewListInquiryResponseDTO> call, Throwable t) {
                Log.e("LOGAPI", "ReviewAPI 호출실패3 " + t.getMessage());
            }
        });
    }

    //리뷰 등록 API(WritingReviewFragment에서 사용)
    public void ReviewRegisterData(Long storeId, ReviewRegisterRequestDTO requestDTO) {
        //리뷰 등록
        Call<Void> reviewRegisterRequestDTOCall = reviewApiService.postReview("Bearer " + token, storeId, requestDTO);
        reviewRegisterRequestDTOCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Void responseData = response.body();
                    Log.d("LOGAPIReviewRegisterData", response.toString());
                } else{
                    Log.d("LOGAPI", "ReviewRegisterData 호출실패1 "+response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("LOGAPI", "ReviewRegisterData 호출실패2");
            }
        });
    }

    //리뷰 삭제 API(WritingReviewFragment에서 사용)
    public void ReviewDeleteData(Long storeId, Long reviewId) {
        //리뷰 수정
        Call<Void> reviewDeleteRequestDTOCall = reviewApiService.deleteReview("Bearer " + token, storeId, reviewId);
        reviewDeleteRequestDTOCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Void responseData = response.body();
//                    Log.d("LOGAPIReviewDeleteData", responseData.toString());
                    Log.d("LOGAPI", "ReviewDeleteData 호출성공 " + response);
                } else{
                    Log.d("LOGAPI", "ReviewDeleteData 호출실패1 " + response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("LOGAPI", "ReviewDeleteData 호출실패2"+ t.getMessage());
            }
        });
    }

    //리뷰 수정 API(WritingReviewFragment에서 사용)
    public void ReviewModifyData(Long storeId, Long reviewId, ReviewModifyrRequestDTO requestDTO) {
        //리뷰 수정
        Call<Void> reviewModifyrRequestDTOCall = reviewApiService.modifyReview("Bearer " + token, storeId, reviewId, requestDTO);
        reviewModifyrRequestDTOCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Void responseData = response.body();
                    Log.d("LOGAPIReviewModifyData", response.toString());
                } else{
                    Log.d("LOGAPI", "ReviewModifyData 호출실패1 "+response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("LOGAPI", "ReviewModifyData 호출실패2");
            }
        });
    }

    //리뷰 신고 API(WritingReviewFragment에서 사용)
    public void ReviewReportData(Long storeId, Long reviewId, ReviewReportRequestDTO requestDTO) {
        //리뷰 수정
        Call<Void> reviewReportRequestDTOCall = reviewApiService.reportReview("Bearer " + token, storeId, reviewId, requestDTO);
        reviewReportRequestDTOCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Void responseData = response.body();
                    Log.d("LOGAPIReviewReportData", response.toString());
                } else{
                    Log.d("LOGAPIReviewReportData", "ReviewReportData 호출실패1 "+response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("LOGAPIReviewReportData", "ReviewReportData 호출실패2");
            }
        });
    }

    //유저-사용자 닉네임 변경
    public void UserNicknameChange(UserNicknameModifyRequestDTO requestDTO) {
        Log.d("닉네임", String.valueOf(requestDTO));
        Log.d("닉네임token", token);
        //리뷰 수정
        Call<Void> userNicknameChangeRequestDTOCall = userApiService.changeNickname("Bearer " + token, requestDTO);
        userNicknameChangeRequestDTOCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("뷰모델 닉네임 onResponse", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    Void responseData = response.body();
                    Log.d("뷰모델에서 보낸 닉네임 api 성공", response.toString());
                    userNickNameDataLiveData.setValue(response.code());
                } else{
                    Log.d("뷰모델에서 보낸 닉네임 api", "UserNicknameChange 호출실패1 "+response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("뷰모델에서 보낸 닉네임 api", "UserNicknameChange 호출실패2");
            }
        });
    }

//    //유저-사용자 프로필 이미지 변경
//    public void UserProfileImageChange(String ) {
//        //리뷰 수정
//        Call<Void> userProfileChangeRequestDTOCall = userApiService.changeProfileImage("Bearer " + token, );
//        userProfileChangeRequestDTOCall.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Void responseData = response.body();
//                    Log.d("LOGAPIUserProfileChange", response.toString());
//                } else{
//                    Log.d("LOGAPIUserProfileChange", "UserProfileChange 호출실패1 "+response);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d("LOGAPIUserProfileChange", "UserProfileChange 호출실패2");
//            }
//        });
//    }



    /* true : 일반 게시글, false : 인기 게시글*/
    public void setPostType(Boolean postType){
        if(postType){
            postTypeLiveData.setValue(true);
        } else {
            postTypeLiveData.setValue(false);
        }
    }

    public void setClickedPostData(PostListData data){
        Long clickedPostId = data.getPostId();
        this.postId = clickedPostId;
        postClickedLiveData.setValue(data);
    }

    public void getPostData(){
        Call<PostInquiryResponseDTO> call = postApiService.getPost("Bearer " + token, postId);
        Log.d("post id","post Id" + postId);
        Log.d("this is token for post not list ","token : " + token);
        call.enqueue(new Callback<PostInquiryResponseDTO>() {
            @Override
            public void onResponse(Call<PostInquiryResponseDTO> call, Response<PostInquiryResponseDTO> response) {
                if(response.isSuccessful()){
                    PostInquiryResponseDTO data = response.body();
                    postContentLiveData.setValue(data);
                    postCommentLiveData.setValue(data.getCommentList());
                    postIdLiveData.setValue(data.getPostId());
                    Log.d("클릭된 포스트 성공","this is post id: " + data.getPostId());
                    Log.d("클릭된 포스트 성공","this is post userNAme: " + data.getUserName());
                    Log.d("클릭된 포스트 성공","this is post userImage: " + data.getUserImageUrl());
                    Log.d("클릭된 포스트 성공","this is post content: " + data.getContent());
//                    Log.d("클릭된 포스트 성공","this is post userImage: " + data.getCommentList().size());
                    Log.d("클릭된 포스트 성공","this is post title: " + data.getPostTitle());
//                    Log.d("클릭된 포스트 성공","this is post userImage: " + data.getImages().get(0));
                    Log.d("post 단일 조회 api 호출 성공 ","성공" + response);
                }else{
                    Log.e("post 단일 조회 api 호출 실패1  ","실패1" + response);

                }
            }
            @Override
            public void onFailure(Call<PostInquiryResponseDTO> call, Throwable t) {
                Log.e("post 단일 조회 api 호출 실패3 ","실패2" + t.getMessage());
            }
        });

    }

    public void deletePost(Long postId) {
        Log.d(" 삭제 포스트 아이디","삭제 포스트 아이디 " + postId);
        Call<Void> call = postApiService.deletePost("Bearer " + token, postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("post 삭제 성공","post 삭제 성공");
                } else {
                    // API 응답이 오류 상태일 때
                    Log.e("post 삭제 실패 1", "Error Code: " + response.code() + ", Message: " + response.message());
                    try {
                        Log.e("post 삭제 실패 1", "Error Body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("post 삭제 실패 2", "실패 2" + t.getMessage());
            }
        });
    }

    public void setImageUrlForModify(List<String> list) {
        imageUrlListForModifyLiveData.setValue(list);
        Log.d("수정을 위해 넘어온 image url 4 ", "this is url" + list);
    }

    public void getUriList(List<Uri> imageUri) {
        this.imageUriList = imageUri;
    }


    //리뷰 조회
    public MutableLiveData<ReviewListInquiryResponseDTO> getReviewLiveData() {
        return reviewLiveData;
    }

    public void setReviewLiveData(MutableLiveData<ReviewListInquiryResponseDTO> reviewLiveData) {
        this.reviewLiveData = reviewLiveData;
    }

    //블로그 리뷰 조회
    public MutableLiveData<ReviewListInquiryResponseDTO> getBlogReviewLiveData() {
        return blogReviewLiveData;
    }

    public void setBlogReviewLiveData(MutableLiveData<ReviewListInquiryResponseDTO> blogReviewLiveData) {
        this.blogReviewLiveData = blogReviewLiveData;
    }

    /* StoreMainList에 보여질 필터링 된 가게 리스트*/
    public LiveData<List<StoreListData>> getFilteredStoreListLiveData() {
        return storeFilteredLiveData;
    }
    /* 실시간 검색어 입력에 따른 가게 리스트 */
    public LiveData<List<SummaryData>> getStoreSearchListByKeywordLiveData() {
        return storeSearchLiveData;
    }

    public LiveData<List<SummaryData>> getSummaryListLiveData() {
        return storeListSummaryLiveData;
    }
    /* 최근 검색어에 따른 가게 리스트*/
    public LiveData<List<SummaryData>> getCurrentListLiveData() {
        return storeListCurrentLiveData;
    }

    public LiveData<List<StoreListData>> getStoreListLiveData() {
        return storeListLiveData;
    }
    /* 커뮤니티 뷰 모델로 유저 토큰을 전달합니다.*/
    public LiveData<String> getToken(){
        return tokenLiveData;
    }

    //선택한 가게를 알기 위한 storeId
    public void setStoreId(Long id) {
        storeId.setValue(id);
    }

    public LiveData<Long> getStoreId() {
        return storeId;
    }

    public LiveData<Boolean> getPostType(){
        return postTypeLiveData;
    }

    public LiveData<PostListData> getClickedPostLiveData(){
        return postClickedLiveData;
    }

    public LiveData<PostInquiryResponseDTO> getPostContentLiveData(){
        return postContentLiveData;
    }

    public LiveData<List<CommentListData>> getPostCommentLiveData(){
        return postCommentLiveData;
    }

    public LiveData<Long> getPostIdLiveData(){
        return postIdLiveData;
    }

    //가게 스탬프
    public MutableLiveData getStoreStampLiveData() {
        return storeStampLiveData;
    }

    public void setStoreStampLiveData(MutableLiveData storeStampLiveData) {
        this.storeStampLiveData = storeStampLiveData;
    }

    //가게 찜버튼
    public MutableLiveData getStoreLikeLiveData() {
        return storeLikeLiveData;
    }

    public void setStoreLikeLiveData(MutableLiveData storeLikeLiveData) {
        this.storeLikeLiveData = storeLikeLiveData;
    }

    //가게 신고
    public MutableLiveData getStoreReportDataLiveData() {
        return storeReportDataLiveData;
    }

    public void setStoreReportDataLiveData(MutableLiveData storeReportDataLiveData) {
        this.storeReportDataLiveData = storeReportDataLiveData;
    }

    public LiveData<List<String>> getImageUrlListForModifyLiveData(){
        return imageUrlListForModifyLiveData;
    }


    //리뷰 신고
    public MutableLiveData getReviewReportDataLiveData() {
        return reviewReportDataLiveData;
    }

    public void setReviewReportDataLiveData(MutableLiveData reviewReportDataLiveData) {
        this.reviewReportDataLiveData = reviewReportDataLiveData;
    }

    //유저 닉네임 변경
    public MutableLiveData getUserNickNameDataLiveData() {
        return userNickNameDataLiveData;
    }

    public void setUserNickNameDataLiveData(MutableLiveData userNickNameDataLiveData) {
        this.userNickNameDataLiveData = userNickNameDataLiveData;
    }

    //스탬프북
    public MutableLiveData<StampBookInquiryResponseDTO> getStampBookDataLiveData() {
        return stampBookDataLiveData;
    }

    public void setStampBookDataLiveData(MutableLiveData<StampBookInquiryResponseDTO> stampBookDataLiveData) {
        this.stampBookDataLiveData = stampBookDataLiveData;
    }
    //리뷰 작성 여부
    public MutableLiveData<Boolean> getCanWriteReview() {
        return canWriteReview;
    }

    public void setCanWriteReview(Boolean canWrite) {
        this.canWriteReview.setValue(canWrite);
    }
}




