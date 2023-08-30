package com.devinsight.vegiedo.view.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.response.StoreListData;
import com.devinsight.vegiedo.data.response.StoreListInquiryResponseDTO;
import com.devinsight.vegiedo.data.ui.login.TagStatus;
import com.devinsight.vegiedo.data.ui.search.SearchStorSummaryeUiData;
import com.devinsight.vegiedo.data.ui.search.SearchStoreDetailUiData;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;
import com.devinsight.vegiedo.utill.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFilterViewModel extends ViewModel {
    private MutableLiveData<TagStatus> tagStatusLiveData = new MutableLiveData<>();

    /* 스토어 상세 목록에 보여 줄 가게 데이터 */
    private MutableLiveData<List<SearchStoreDetailUiData>> storeListDetailLiveData = new MutableLiveData<>();

    /* 검색 창 클릭 시 간략하게 보여 줄 가게 데이터 */
    private MutableLiveData<List<SearchStorSummaryeUiData>> storeLiveData = new MutableLiveData<>();

    /* 필터링을 끝 낸 라이브 데이터 */
    private MutableLiveData<List<StoreListData>> storeFilteredLiveData = new MutableLiveData<>();

    /* api 를 통해 서버로 부터 받은 가게 리스트 */
    private List<StoreListData> allStoreList;


    UserPrefRepository userPrefRepository;

    LocationData locationData;

    private float latitude;
    private float longitude;


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

    public void getCurrentLocationData(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

        Log.d("위치3", "위치3" + "위도 : " + latitude + "경도" + longitude);
    }

    public void getFilterData(int distance, List<String> tags) {
        String keyword = "";
        Call<StoreListInquiryResponseDTO> call = RetrofitClient.getStoreApiService()
                .getStoreList(
                        tags,
                        latitude,
                        longitude,
                        distance,
                        keyword,
                        10,
                        3
                );

        call.enqueue(new Callback<StoreListInquiryResponseDTO>() {
            @Override
            public void onResponse(Call<StoreListInquiryResponseDTO> call, Response<StoreListInquiryResponseDTO> response) {
                if(response.isSuccessful()){
                    StoreListInquiryResponseDTO storeList = response.body();
                    allStoreList = storeList.getStores();
                    storeFilteredLiveData.setValue(allStoreList);
                }
            }
            @Override
            public void onFailure(Call<StoreListInquiryResponseDTO> call, Throwable t) {

            }
        });
        Log.d("위치4", "위치4" + "위도 : " + latitude + "경도" + longitude);
    }

    public LiveData<List<StoreListData>> loadStoreList(){
        return storeFilteredLiveData;
    }
}



