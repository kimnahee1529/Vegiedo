package com.devinsight.vegiedo.view.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devinsight.vegiedo.R;


public class SearchFilterFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    /* seekbar */
    SeekBar seekBar;
    TextView seekbar_distance;

    /* 뷰모델 */
    SearchFilterViewModel viewModel;

    public SearchFilterFragment() {
        // Required empty public constructor
    }


    public static SearchFilterFragment newInstance(String param1, String param2) {
        SearchFilterFragment fragment = new SearchFilterFragment();
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
        View view = inflater.inflate(R.layout.fragment_search_filter, container, false);
        seekBar = view.findViewById(R.id.seekBar);
        seekbar_distance = view.findViewById(R.id.seekbar_distance);

        seekBar.setProgress(5);
        seekbar_distance.setX(182);
        seekbar_distance.setText(seekBar.getProgress() + "km이내");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int padding= seekBar.getPaddingLeft() + seekBar.getPaddingRight();
                int sPos = seekBar.getLeft() + seekBar.getPaddingLeft();
                int xPos = (seekBar.getWidth()-padding) * seekBar.getProgress() / seekBar.getMax() + sPos - (seekbar_distance.getWidth()/2);
                seekbar_distance.setX(xPos);
                seekbar_distance.setText(seekBar.getProgress() + "km이내");
                Log.d("seekbar 위치 ","위치 : " + xPos);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }
}