package com.devinsight.vegiedo.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.response.StoreListData;
import com.devinsight.vegiedo.view.search.ActivityViewModel;
import com.devinsight.vegiedo.view.search.StoreDetailListAdapter;

import java.util.ArrayList;
import java.util.List;

public class StoreListMainFragment extends Fragment implements StoreDetailListAdapter.searchListner {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private StoreDetailListAdapter storeDetailListAdapter;

    private List<StoreListData> storeList;
    private ActivityViewModel viewModel;

    ConstraintLayout layout;
    LinearLayout not_found_Sheep;

    public static StoreListMainFragment instance() {
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

        layout = view.findViewById(R.id.store_setting_message);
        not_found_Sheep = view.findViewById(R.id.not_found);

        layout.setVisibility(View.INVISIBLE);
        not_found_Sheep.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.recycler_store_list);
        storeList = new ArrayList<>();
        storeDetailListAdapter = new StoreDetailListAdapter(getActivity(), storeList, this);
        recyclerView.setAdapter(storeDetailListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        viewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        viewModel.searchDetailList();

        viewModel.getFilteredStoreListLiveData().observe(getViewLifecycleOwner(), new Observer<List<StoreListData>>() {
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout.setVisibility(View.VISIBLE);
        not_found_Sheep.setVisibility(View.INVISIBLE);
    }
}