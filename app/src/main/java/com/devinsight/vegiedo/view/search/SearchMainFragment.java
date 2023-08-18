package com.devinsight.vegiedo.view.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.search.SearchStoreSummaryData;

import java.util.ArrayList;


public class SearchMainFragment extends Fragment implements SearchFragmentAdapter.searchItemListner {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private SearchFragmentAdapter searchAdapter;
    private ArrayList<SearchStoreSummaryData> storeList;

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

        storeList.add(new SearchStoreSummaryData(R.drawable.ic_launcher_background,"Little Forest","집이최고야"));
        storeList.add(new SearchStoreSummaryData(R.drawable.ic_launcher_background,"Little Forest","집이최고야"));
        storeList.add(new SearchStoreSummaryData(R.drawable.ic_launcher_background,"Little Forest","집이최고야"));
        storeList.add(new SearchStoreSummaryData(R.drawable.ic_launcher_background,"Little Forest","집이최고야"));
        storeList.add(new SearchStoreSummaryData(R.drawable.ic_launcher_background,"Little Forest","집이최고야"));
        storeList.add(new SearchStoreSummaryData(R.drawable.ic_launcher_background,"Little Forest","집이최고야"));

        searchAdapter = new SearchFragmentAdapter(getContext(),storeList,this);
        recyclerView.setAdapter(searchAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onSearchItemClick(SearchStoreSummaryData searchData) {
        Toast.makeText(getContext(),searchData.getStoreName() + " is clicked ",Toast.LENGTH_SHORT).show();
    }


}
