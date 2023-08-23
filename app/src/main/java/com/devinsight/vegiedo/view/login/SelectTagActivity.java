package com.devinsight.vegiedo.view.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.data.request.login.TagStatus;
import com.devinsight.vegiedo.data.request.login.VeganTag;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;
import com.devinsight.vegiedo.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class SelectTagActivity extends AppCompatActivity {


    private TextView btn_complete;
    private TextView btn_later;
    private TextView tt_selet_tag;
    private TagsViewModel viewModel;

    List<ToggleButton> toggleButtonsList;
    List<String> userTagList;
    UserPrefRepository userPrefRepository;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);
        /* 토글 버튼 9개 추가 .... */
//        setButtons();

        btn_complete = findViewById(R.id.tt_complete);
        btn_later = findViewById(R.id.tt_back);
        tt_selet_tag = findViewById(R.id.tt_select_tag);

        ToggleButton tagFruittarian = findViewById(VeganTag.TAG_FRUITTARIAN.getTagId());
        ToggleButton tagVegan = findViewById(VeganTag.TAG_VEGAN.getTagId());
        ToggleButton taglacto = findViewById(VeganTag.TAG_LACTO.getTagId());
        ToggleButton tagOvo = findViewById(VeganTag.TAG_OVO.getTagId());
        ToggleButton taglactoOvo = findViewById(VeganTag.TAG_LACTO_OVO.getTagId());
        ToggleButton tagPesca = findViewById(VeganTag.TAG_PESCA.getTagId());
        ToggleButton tagPollo = findViewById(VeganTag.TAG_POLLO.getTagId());
        ToggleButton tagKeto = findViewById(VeganTag.TAG_KETO.getTagId());
        ToggleButton tagGluten = findViewById(VeganTag.TAG_GLUTEN.getTagId());


        /* 3개 미만 선택 시 글자 색 변경*/
        String content = tt_selet_tag.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = "3개 이상";
        int start = content.indexOf(word);
        int end = start + word.length();
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        userPrefRepository = new UserPrefRepository(this);
        viewModel = new ViewModelProvider(this).get(TagsViewModel.class);
        toggleButtonsList = new ArrayList<>();
        userTagList = new ArrayList<>();


        tagFruittarian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String tagContent = VeganTag.TAG_FRUITTARIAN.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

//                viewModel.setToggleState(compoundButton.getId(), isChecked);
//                viewModel.result(compoundButton.getId(), tagContent);
//                int tagNum = VeganTag.TAG_FRUITTARIAN.getTagNum();
//                Log.d("프루테리언","클릭횟수 " + tagNum);
//                addTag(isChecked, tagContent, tagNum);
//                viewModel.setTag(isChecked, tagContent, tagNum);
            }
        });

        tagVegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.TAG_VEGAN.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

//                viewModel.setToggleState(compoundButton.getId(), isChecked);
//                viewModel.result(compoundButton.getId(), tagContent);
//                int tagNum = VeganTag.TAG_VEGAN.getTagNum();
//                tagNum ++;
//                Log.d("비건","클릭횟수 " + tagNum);

//                addTag(isChecked, tagContent, tagNum);
//                viewModel.setTag(isChecked, tagContent, tagNum);
            }
        });
        taglacto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.TAG_LACTO.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

//                viewModel.setToggleState(compoundButton.getId(), isChecked);
//                viewModel.result(compoundButton.getId(), tagContent);
//                int tagNum = VeganTag.TAG_LACTO.getTagNum();
//                tagNum ++;
//                addTag(isChecked, tagContent, tagNum);
//                viewModel.setTag(isChecked, tagContent, tagNum);

            }
        });
        tagOvo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.TAG_OVO.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

//                String tagContent = VeganTag.TAG_OVO.getTagContent();
//                int tagNum = VeganTag.TAG_OVO.getTagNum();
//                tagNum ++;
////                addTag(isChecked, tagContent, tagNum);
//                viewModel.setTag(isChecked, tagContent, tagNum);

            }
        });
        taglactoOvo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.TAG_LACTO_OVO.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());


//                String tagContent = VeganTag.TAG_LACTO_OVO.getTagContent();
//                int tagNum = VeganTag.TAG_LACTO_OVO.getTagNum();
//                tagNum ++;
////                addTag(isChecked, tagContent, tagNum);
//                viewModel.setTag(isChecked, tagContent, tagNum);

            }
        });
        tagPesca.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.TAG_PESCA.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

//                String tagContent = VeganTag.TAG_PESCA.getTagContent();
//                int tagNum = VeganTag.TAG_PESCA.getTagNum();
//                tagNum ++;
////                addTag(isChecked, tagContent, tagNum);
//                viewModel.setTag(isChecked, tagContent, tagNum);

            }
        });
        tagPollo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.TAG_POLLO.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

//                String tagContent = VeganTag.TAG_POLLO.getTagContent();
//                int tagNum = VeganTag.TAG_POLLO.getTagNum();
//                tagNum ++;
////                addTag(isChecked, tagContent, tagNum);
//                viewModel.setTag(isChecked, tagContent, tagNum);

            }
        });
        tagKeto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.TAG_KETO.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

//                String tagContent = VeganTag.TAG_KETO.getTagContent();
//                int tagNum = VeganTag.TAG_KETO.getTagNum();
//                tagNum ++;
////                addTag(isChecked, tagContent, tagNum);
//                viewModel.setTag(isChecked, tagContent, tagNum);


            }
        });
        tagGluten.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String tagContent = VeganTag.TAG_GLUTEN.getTagContent();
                viewModel.tagContent(isChecked, tagContent, compoundButton.getId());

//                String tagContent = VeganTag.TAG_GLUTEN.getTagContent();
//                int tagNum = VeganTag.TAG_GLUTEN.getTagNum();
//                tagNum ++;
////                addTag(isChecked, tagContent, tagNum);
//                viewModel.setTag(isChecked, tagContent, tagNum);

            }
        });

        viewModel.getTagStatusLieveData().observe(this, new Observer<TagStatus>() {
            @Override
            public void onChanged(TagStatus tagStatus) {
                if (tagStatus.isStatus()) {
                    String userTag = tagStatus.getContent();
                    userTagList.add(userTag);
                    Log.d("리스트 추가", "태그 : " + userTagList);
                } else if (userTagList != null && !tagStatus.isStatus()) {
                    String userTagToRemove = tagStatus.getContent();  // 삭제하고자 하는 태그의 값
                    int indexToRemove = userTagList.indexOf(userTagToRemove);  // 해당 태그의 인덱스를 찾습니다.

                    if (indexToRemove != -1) {  // 만약 해당 태그가 리스트에 있다면
                        userTagList.remove(indexToRemove);  // 해당 인덱스의 태그를 제거합니다.
                        Log.d("리스트 삭제", "태그 : " + userTagList);
                    }
                }
            }
        });


//        viewModel.getTagStatusLieveData().observe(this, new Observer<TagStatus>() {
//            @Override
//            public void onChanged(TagStatus tagStatus) {
//                if (tagStatus.isStatus()) {
//                    String userTag = tagStatus.getContent();
//                    userTagList.add(userTag);
//                    Log.d("리스트 추가","태그 : " + userTagList);
//                } else if (userTagList != null && tagStatus.isStatus() == false){
//                    for( String tag : userTagList){
//                        if ( userTagList.contains(tag) && tagStatus.getContent() == tag){
//                            userTagList.remove(tag);
//                            Log.d("리스트 추가","태그 : " + userTagList);
//                        }
//                    }
//                }
//            }
//        });


        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userTagList != null && userTagList.size() >= 3) {
                    userPrefRepository.saveUserTags(userTagList);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (userTagList.size() < 3) {
                    tt_selet_tag.setText(spannableString);
                    int message = R.string.select_three_or_more;
                    setDialog(message);
                }

            }
        });

        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int message = R.string.select_at_filter;
                setDialog(message);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

//    public void addTag(boolean isChecked, String tag, int index) {
//        if (isChecked) {
//            userTagList.add(tag);
//        } else if (isChecked == false) {
//            int count = 0;
//            for (String tagContent : userTagList) {
//                if (tagContent.equals(userTagList.get(count))) {
//                    userTagList.remove(count);
//                    count++;
//                }
//                count++;
//            }
//
//        }
//    }

    public void setDialog(int message) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_custom);

        TextView msg = dialog.findViewById(R.id.dialog);
        msg.setText(message);
        dialog.show();
    }

//    public void setButtons() {
//        ToggleButton tagFruittarian = findViewById(VeganTag.TAG_FRUITTARIAN.getTagId());
//        ToggleButton tagVegan = findViewById(VeganTag.TAG_VEGAN.getTagId());
//        ToggleButton taglacto = findViewById(VeganTag.TAG_LACTO.getTagId());
//        ToggleButton tagOvo = findViewById(VeganTag.TAG_OVO.getTagId());
//        ToggleButton taglactoOvo = findViewById(VeganTag.TAG_OVO.getTagId());
//        ToggleButton tagPesca = findViewById(VeganTag.TAG_PESCA.getTagId());
//        ToggleButton tagPollo = findViewById(VeganTag.TAG_POLLO.getTagId());
//        ToggleButton tagKeto = findViewById(VeganTag.TAG_KETO.getTagId());
//        ToggleButton tagGluten = findViewById(VeganTag.TAG_GLUTEN.getTagId());
//        btn_complete = findViewById(R.id.tt_complete);
//        btn_back = findViewById(R.id.tt_back);
//        tt_red = findViewById(R.id.tt_red);
//    }
}
