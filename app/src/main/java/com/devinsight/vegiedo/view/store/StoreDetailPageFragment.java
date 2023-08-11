package com.devinsight.vegiedo.view.store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devinsight.vegiedo.R;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailPageFragment extends Fragment {

    private static final int ITEMS_COUNT = 10;

    private RecyclerView recyclerView;
    private StoreDetailPageAdapter adapter;
    private List<Item> items;

    public StoreDetailPageFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_detail_page, container, false);

        initializeComponents(view);
        populateData();
        setupRecyclerView();

        return view;
    }

    private void initializeComponents(View view) {
        recyclerView = view.findViewById(R.id.store_recycle_view);
    }

    private void populateData() {
        items = new ArrayList<>();
        for (int i = 0; i < ITEMS_COUNT; i++) {
            items.add(createItem(i));
        }
    }

    private Item createItem(int index) {
        return new Item(Item.ItemType.STORE_DETAIL_PAGE, "Title " + index,
                R.drawable.full_star, R.drawable.full_star, R.drawable.full_star, R.drawable.full_star, R.drawable.empty_star,
                "Description 1", R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background);
    }

    private void setupRecyclerView() {
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new StoreDetailPageAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}