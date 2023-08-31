package com.devinsight.vegiedo.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.StoreListData;
import com.devinsight.vegiedo.view.search.SearchFilterViewModel;
import com.devinsight.vegiedo.view.search.StoreDetailListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StoreListMainFragment extends Fragment implements StoreDetailListAdapter.searchListner {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private StoreDetailListAdapter storeDetailListAdapter;

    private List<StoreListData> storeList;
    private SearchFilterViewModel viewModel;

    public static StoreListMainFragment instance(){
        return new StoreListMainFragment();
    }

    public StoreListMainFragment() {

    }

    public static StoreListMainFragment newInstance(String param1, String param2) {
        StoreListMainFragment fragment = new StoreListMainFragment();
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
        View view = inflater.inflate(R.layout.fragment_store_list_main, container, false);

        recyclerView = view.findViewById(R.id.recycler_store_list);
        storeList = new ArrayList<>();

//        storeList.add(new StoreListData(1l,"향림원","서울특별시 강남구 삼성동 123-45",37.500731f, 127.039338f, 5, 5, Arrays.asList("#비건", "#락토") , true, 45, ""));
//        storeList.add(new StoreListData(2l,"서울테이블","부산광역시 해운대구 우동 56-78",37.494575f, 127.034612f, 5, 4, Arrays.asList("#프루테리언", "#비건") , true, 45, ""));
//        storeList.add(new StoreListData(3l,"바다의 선물","대구광역시 중구 동인동 90-12",37.499176f, 127.041257f, 5, 5, Arrays.asList( "#락토", "#오보") , true, 45, ""));
//        storeList.add(new StoreListData(4l,"마루키친","서울특별시 강남구 논현동 123-45",37.492988f, 127.035923f, 5, 1, Arrays.asList("#락토 오보", "#페스코") , true, 45, ""));
//        storeList.add(new StoreListData(5l,"송림정","서울특별시 중구 을지로 56-78",37.503657f, 127.036592f, 5, 3, Arrays.asList("#오보", "#락토 오보") , true, 45, ""));
//        storeList.add(new StoreListData(6l,"파스텔레스토","서울특별시 용산구 한강로 90-12",37.492142f, 127.045137f, 5, 4, Arrays.asList("#페스코", "#폴로") , true, 45, ""));
//        storeList.add(new StoreListData(7l,"그릴 101","서울특별시 마포구 서교동 78-90",37.498235f, 127.032479f, 5, 2, Arrays.asList("#폴로", "#키토") , true, 45, ""));
//        storeList.add(new StoreListData(8l,"하늘정원","대전광역시 유성구 신성동 12-34",37.502658f, 127.040892f, 5, 3, Arrays.asList("#키토", "#글루텐프리") , true, 45, ""));
//        storeList.add(new StoreListData(9l,"산바다물회","울산광역시 남구 신정동 45-67",37.496312f, 127.043285f, 5, 3, Arrays.asList("#락토", "#프루테리언") , true, 45, ""));
//        storeList.add(new StoreListData(10l,"리베로 스테이크하우스","서울특별시 동작구 사당동 45-67",37.504978f, 127.037501f, 5, 4, Arrays.asList("#오보", "#글루텐프리") , true, 45, ""));


        storeDetailListAdapter = new StoreDetailListAdapter(getActivity(), storeList, this);
        recyclerView.setAdapter(storeDetailListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        viewModel = new ViewModelProvider(requireActivity()).get(SearchFilterViewModel.class);

        viewModel.dummyData();

        viewModel.getFilteredStoreList().observe(getViewLifecycleOwner(), new Observer<List<StoreListData>>() {
            @Override
            public void onChanged(List<StoreListData> storeListData) {
                storeDetailListAdapter.setStoreList(storeListData);
                storeDetailListAdapter.notifyDataSetChanged();
            }
        });



        return view;
    }

    @Override
    public void onSearchClick(View view, StoreListData searchData, int position) {

    }
}