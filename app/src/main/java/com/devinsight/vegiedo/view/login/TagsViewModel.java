package com.devinsight.vegiedo.view.login;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devinsight.vegiedo.data.ui.login.TagStatus;
import com.devinsight.vegiedo.repository.pref.UserPrefRepository;

import java.util.ArrayList;

public class TagsViewModel extends ViewModel {
//    private final MutableLiveData<List<String>> tagLiveData = new MutableLiveData<>();
//    private Map<Integer, MutableLiveData<Boolean>> toggleStatesLiveData = new HashMap<>();
//    private MutableLiveData<Map> toggleLiveData = new MutableLiveData<>();

    private MutableLiveData<TagStatus> tagStatusLiveData = new MutableLiveData<>();
    UserPrefRepository userPrefRepository;

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


    public LiveData<TagStatus> getTagStatusLiveData(){

        return tagStatusLiveData;
    }


}