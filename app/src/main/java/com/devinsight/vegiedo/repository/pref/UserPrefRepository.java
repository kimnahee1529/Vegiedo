package com.devinsight.vegiedo.repository.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.devinsight.vegiedo.data.request.login.UserData;
import com.devinsight.vegiedo.data.request.login.VeganTag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserPrefRepository {

    private static final String USER_INFO = "use_info";
    private String USER_TAG_KEY = "user_tags"; // json으로 불러오기 위해 필요한 키 ( KEY, json) -> json의 내용 ( DataClass)이 KEY 값에 따라 불려온다.

    private SharedPreferences sharedPreferences;

    private Gson gson;

    public UserPrefRepository(Context context){
        sharedPreferences = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveUserInfo(String userInfoType, String value){
        sharedPreferences.edit().putString(userInfoType, value).apply();

    }
    public void saveUserTags(List<String> tagList) {
        String json = gson.toJson(tagList);
        sharedPreferences.edit().putString(USER_TAG_KEY, json).apply();
    }

    public ArrayList<String> loadTagList(List<String> tagList){
        String json = sharedPreferences.getString(USER_TAG_KEY, null);
        if( json != null ) {
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    public void clearUserInfo(){
        sharedPreferences.edit().clear().apply();
    }

}