package com.devinsight.vegiedo.view.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.ui.search.SearchStorSummaryeUiData;

import java.util.ArrayList;
import java.util.List;


public class SearchMainFragment extends Fragment implements SearchSummaryListAdapter.searchSummaryItemListner {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private SearchSummaryListAdapter searchAdapter;
    private List<SummaryData> storeList;

    private ActivityViewModel viewModel;

    public SearchMainFragment() {

    }

    public static SearchMainFragment instance(){
        return new SearchMainFragment();
    }


    public static SearchMainFragment newInstance(String param1, String param2) {
        SearchMainFragment fragment = new SearchMainFragment();
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
        Log.d("search frag","onCreateView");
        View view = inflater.inflate(R.layout.fragment_search_list_main, container, false);

        recyclerView = view.findViewById(R.id.recycler_search_page);
        storeList = new ArrayList<>();
        searchAdapter = new SearchSummaryListAdapter(getActivity(), storeList, this);
        recyclerView.setAdapter(searchAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        viewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
//        viewModel.searchSummList();
//        viewModel.getSummaryListLiveData().observe(getViewLifecycleOwner(), new Observer<List<SummaryData>>() {
//            @Override
//            public void onChanged(List<SummaryData> summaryDataList) {
//                searchAdapter.setSearchList(summaryDataList);
//                searchAdapter.notifyDataSetChanged();
//            }
//        });


        /* 최근 검색어를 기준으로 최초 요약된 리스트를 보여줍니다.*/
        viewModel.currentList();
        viewModel.getCurrentListLiveData().observe(getViewLifecycleOwner(), new Observer<List<SummaryData>>() {
            @Override
            public void onChanged(List<SummaryData> summaryDataList) {
                if(summaryDataList != null) {
                    searchAdapter.setSearchList(summaryDataList);
                    searchAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "최근 검색어가 없습니다. ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        viewModel.storeApiData();
        /* 검색창에 입력된 글자를 기준으로 리스트를 보여줍니다.*/
//        viewModel.searchSummList();
        viewModel.getStoreSearchListByKeywordLiveData().observe(getViewLifecycleOwner(), new Observer<List<SummaryData>>() {
            @Override
            public void onChanged(List<SummaryData> summaryDataList) {
                searchAdapter.setSearchList(summaryDataList);
                searchAdapter.notifyDataSetChanged();
            }
        });



        return view;
    }

    @Override
    public void onSearchSummaryItemClick(SummaryData searchData, int position) {
        viewModel.setStoreIdLiveData(storeList.get(position).getStoreId());
    }
}
