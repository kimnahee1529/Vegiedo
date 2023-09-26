package com.devinsight.vegiedo.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.ui.map.MapStoreCardUiData;

import java.util.ArrayList;


public class MapStoreCardViewFragment extends Fragment implements MapStoreCardAdapter.mapCardItemListner {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ViewPager2 viewPager2;
    private MapStoreCardAdapter cardAdapter;

    private RecyclerView recyclerView;
    private ArrayList<MapStoreCardUiData> cardDataList;

    public MapStoreCardViewFragment() {
        // Required empty public constructor
    }

    public static MapStoreCardViewFragment newInstance(String param1, String param2) {
        MapStoreCardViewFragment fragment = new MapStoreCardViewFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_viewpager2, container, false);
        viewPager2 = view.findViewById(R.id.viewpager_map);
        cardAdapter = new MapStoreCardAdapter(getContext(), cardDataList, this);
        viewPager2.setAdapter(cardAdapter);
        return view;
    }

    @Override
    public void onCardClick(MapStoreCardUiData item) {
//        Toast.makeText(getContext(), item.getStoreName() + " is clicked", Toast.LENGTH_SHORT).show();
    }
}