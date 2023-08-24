package com.devinsight.vegiedo.repository.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class AuthPrefRepository {

    private static final String LOGIN_INFO = "login_info";
    private static final String LOGIN_TYPE_KEY = "login_token";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public AuthPrefRepository(Context context){
        sharedPreferences = context.getSharedPreferences(LOGIN_INFO,Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveAuthToken(String loginType, String authToken){
        sharedPreferences.edit().putString(loginType, authToken).apply();
    }

    public String getAuthToken(String loginType){
        return sharedPreferences.getString(loginType, null);
    }

    public void removeAuthToken(String loginType){
        sharedPreferences.edit().remove(loginType).apply();
    }

    public void clearAuthToken(){
        sharedPreferences.edit().clear().apply();
    }

}
