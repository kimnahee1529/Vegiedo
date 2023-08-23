package com.devinsight.vegiedo.view.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.request.login.TagStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagsViewModel extends ViewModel {
//    private final MutableLiveData<List<String>> tagLiveData = new MutableLiveData<>();
//    private Map<Integer, MutableLiveData<Boolean>> toggleStatesLiveData = new HashMap<>();
//    private MutableLiveData<Map> toggleLiveData = new MutableLiveData<>();

    private MutableLiveData<TagStatus> tagStatusLiveData = new MutableLiveData<>();

    public void tagContent( boolean isChecked, String content, int btnId) {

        TagStatus tagStatus = new TagStatus(isChecked, content, btnId);

        if(isChecked) {
            tagStatus.setContent(content);
            tagStatus.setBtnId(btnId);
            tagStatus.setStatus(true);
        }else {
            tagStatus.setContent(content);
            tagStatus.setBtnId(btnId);
            tagStatus.setStatus(false);
        }

        tagStatusLiveData.setValue(tagStatus);
    }

    public LiveData<TagStatus> getTagStatusLieveData(){
        return tagStatusLiveData;
    }





//    public void setToggleState(int buttonId, Boolean state) {
//        if (!toggleStatesLiveData.containsKey(buttonId)) {
//            toggleStatesLiveData.put(buttonId, new MutableLiveData<>());
//        }
//        toggleStatesLiveData.get(buttonId).setValue(state);
//    }
//
//    public LiveData<Boolean> getToggleStateLiveData(int buttonId) {
//        if (!toggleStatesLiveData.containsKey(buttonId)) {
//            toggleStatesLiveData.put(buttonId, new MutableLiveData<>());
//        }
//        return toggleStatesLiveData.get(buttonId); /* 버튼 id값에 따는 변화하는 true/false 값 입력 */
//    }
//
//    public void result(int buttonId, String content){
//        List<String> resultList = new ArrayList<>();
//        if (toggleStatesLiveData.get(buttonId).getValue() == true ){
//            resultList.add(content);
//        } else if (toggleStatesLiveData.get(buttonId).getValue() == false && resultList.contains(content)) {
//
//        }
//    }
//
//    public LiveData<List<String>> getTagLiveData() {
//        return tagLiveData;
//    }
//
//    public void setTag(boolean isChecked, String tagContent, int tagNum) {
//        if(tagNum % 2 == 0) {
//            /* tagNum이 짝수면 false*/
//
//        } else if (tagNum % 2 == 1 ){
//            /* tagNum이 홀수면 true*/
//
//        }
//    }

}
