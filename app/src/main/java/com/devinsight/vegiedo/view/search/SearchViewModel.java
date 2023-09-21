//package com.devinsight.vegiedo.view.search;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.devinsight.vegiedo.data.response.StoreListData;
//import com.devinsight.vegiedo.data.response.StoreListInquiryResponseDTO;
//import com.devinsight.vegiedo.utill.RetrofitClient;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class SearchViewModel extends ViewModel {
//
//    /* 스토어 요약 목록에 보여 줄 가게 데이터 */
//    private MutableLiveData<List<SummaryData>> storeListSummaryLiveData = new MutableLiveData<>();
//
//    /* 검색창에 보여줄 라이브 데이터 */
//    private MutableLiveData<List<SummaryData>> storeSearchLiveData = new MutableLiveData<>();
//    private MutableLiveData<List<StoreListData>> storeLiveData = new MutableLiveData<>();
//
//    String input;
//
//
//    public List<StoreListData> dummyData() {
//        List<StoreListData> storeList = new ArrayList<>(); // storeFilteredLiveData.getValue;
//
//        storeList.add(new StoreListData(1l, "향림원", "서울특별시 강남구 삼성동 123-45", 37.500731f, 127.039338f, 5, 5, Arrays.asList("#비건", "#락토"), true, 45, "https://cdn.pixabay.com/photo/2014/04/13/20/49/cat-323262_640.jpg"));
//        storeList.add(new StoreListData(2l, "서울테이블", "부산광역시 해운대구 우동 56-78", 37.494575f, 127.034612f, 5, 4, Arrays.asList("#프루테리언", "#비건"), true, 45, "https://cdn.pixabay.com/photo/2016/05/24/16/48/mountains-1412683_640.png"));
//        storeList.add(new StoreListData(3l, "바다의 선물", "대구광역시 중구 동인동 90-12", 37.499176f, 127.041257f, 5, 5, Arrays.asList("#락토", "#오보"), true, 45, "https://cdn.pixabay.com/photo/2018/08/12/15/29/hintersee-3601004_640.jpg"));
//        storeList.add(new StoreListData(4l, "마루키친", "서울특별시 강남구 논현동 123-45", 37.492988f, 127.035923f, 5, 1, Arrays.asList("#락토 오보", "#페스코"), true, 45, "https://cdn.pixabay.com/photo/2018/01/30/22/50/forest-3119826_640.jpg"));
//        storeList.add(new StoreListData(5l, "송림정", "서울특별시 중구 을지로 56-78", 37.503657f, 127.036592f, 5, 3, Arrays.asList("#오보", "#락토 오보"), true, 45, "https://cdn.pixabay.com/photo/2018/12/15/18/02/forest-3877365_640.jpg"));
//        storeList.add(new StoreListData(6l, "파스텔레스토", "서울특별시 용산구 한강로 90-12", 37.492142f, 127.045137f, 5, 4, Arrays.asList("#페스코", "#폴로"), true, 45, "https://cdn.pixabay.com/photo/2016/09/04/20/09/mountains-1645078_640.jpg"));
//        storeList.add(new StoreListData(7l, "그릴 101", "서울특별시 마포구 서교동 78-90", 37.498235f, 127.032479f, 5, 2, Arrays.asList("#폴로", "#키토"), true, 45, "https://cdn.pixabay.com/photo/2018/07/14/17/46/raccoon-3538081_640.jpg"));
//        storeList.add(new StoreListData(8l, "하늘정원", "대전광역시 유성구 신성동 12-34", 37.502658f, 127.040892f, 5, 3, Arrays.asList("#키토", "#글루텐프리"), true, 45, "https://cdn.pixabay.com/photo/2020/12/23/14/41/forest-5855196_640.jpg"));
//        storeList.add(new StoreListData(9l, "산바다물회", "울산광역시 남구 신정동 45-67", 37.496312f, 127.043285f, 5, 3, Arrays.asList("#락토", "#프루테리언"), true, 45, "https://cdn.pixabay.com/photo/2014/07/28/20/39/sunset-404072_640.jpg"));
//        storeList.add(new StoreListData(10l, "리베로 스테이크하우스", "서울특별시 동작구 사당동 45-67", 37.504978f, 127.037501f, 5, 4, Arrays.asList("#오보", "#글루텐프리"), true, 45, "https://cdn.pixabay.com/photo/2018/08/21/23/29/forest-3622519_640.jpg"));
//
//        return storeList;
//    }
//
//    public void searchSummList(){
//        List<StoreListData> storeListData = dummyData();
//        List<SummaryData> searchList = new ArrayList<>();
//        for(StoreListData data : storeListData) {
//            SummaryData summaryData = new SummaryData();
//            summaryData.setStoreImage(data.getImages());
//            summaryData.setStoreName(data.getStoreName());
//            summaryData.setStoreAddress(data.getAddress());
//
//            searchList.add(summaryData);
//        }
//        storeListSummaryLiveData.setValue(searchList);
//    }
//
//    public void searchSummaryListByKeyword(String input){
//        if(storeListSummaryLiveData != null) {
//            List<SummaryData> storeList = storeListSummaryLiveData.getValue();
//            List<SummaryData> filteredList = new ArrayList<>();
//            for( int i = 0 ; i < storeListSummaryLiveData.getValue().size() ; i ++ ) {
//                if( storeList.get(i).getStoreName().toLowerCase().contains(input.toLowerCase()) || storeList.get(i).getStoreAddress().toLowerCase().contains(input.toLowerCase()) ) {
//                    filteredList.add(storeList.get(i));
//                }
//            }
//            storeSearchLiveData.setValue(filteredList);
//        }
//    }
//
//    public LiveData<List<SummaryData>> getStoreSearchListByKeyword() {
//        return storeSearchLiveData;
//    }
//
//    public LiveData<List<SummaryData>> getSummaryList(){
//        return  storeListSummaryLiveData;
//    }
//
//
//
//}
