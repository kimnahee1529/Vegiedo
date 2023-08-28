package com.devinsight.vegiedo.view.community;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devinsight.vegiedo.R;

public class GeneralPostFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_post, container, false);

        // writing_btn 클릭 리스너 설정
        view.findViewById(R.id.writing_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // WritingFragment로 전환
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new WritingFragment());
                transaction.addToBackStack(null);  // 이 줄은 필요하면 추가. (뒤로 가기 버튼을 누르면 CommunityFragment로 돌아오게 됩니다)
                transaction.commit();
            }
        });

        // popural_posts 클릭 리스너 설정
        view.findViewById(R.id.popural_posts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PopuralPostFragment로 전환
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new PopuralPostFragment());
                transaction.addToBackStack(null);  // 뒤로 가기 버튼을 누르면 이전 프래그먼트로 돌아오게 됩니다
                transaction.commit();
            }
        });

        return view;
    }
}
