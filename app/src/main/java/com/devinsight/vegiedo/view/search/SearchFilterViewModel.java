package com.devinsight.vegiedo.view.search;

import android.widget.SeekBar;

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

public class SearchFilterViewModel extends ViewModel {
    private MutableLiveData<TagStatus> tagStatusLiveData = new MutableLiveData<>();

    /* 스토어 상세 목록에 보여 줄 가게 데이터 */
    private MutableLiveData<List<SearchStoreDetailUiData>> storeListDetailLiveData = new MutableLiveData<>();

    /* 검색 창 클릭 시 간략하게 보여 줄 가게 데이터 */
    private MutableLiveData<List<SearchStorSummaryeUiData>> storeLiveData = new MutableLiveData<>();

    /* 필터링을 끝 낸 라이브 데이터 */
    private MutableLiveData<List<SearchStorSummaryeUiData>> storeFilteredLiveData = new MutableLiveData<>();

    /* api 를 통해 서버로 부터 받은 가게 리스트 */
    private List<StoreListData> allStoreList = new ArrayList<>();

    UserPrefRepository userPrefRepository;





    public void tagContent( boolean isChecked, String content, int btnId) {

        TagStatus tagStatus = new TagStatus(isChecked, content, btnId);

        if(isChecked) {
            tagStatus.setContent(content);
            tagStatus.setBtnId(btnId);
            tagStatus.setStatus(true);
        }else {
            tagStatus.setContent(content);
            tagStatus.setBtnId(btnId);
            tagStatus.setStatus(false);
        }

        tagStatusLiveData.setValue(tagStatus);
    }

    public LiveData<TagStatus> getTagStatusLiveData(){

        return tagStatusLiveData;
    }

    public void getFilterData(int distance, List<String> tags){
//        Call<StoreListInquiryResponseDTO> call = RetrofitClient.getStoreApiService()
//                .getStoreList(
//                        tags,
//
//                );

    }
}
