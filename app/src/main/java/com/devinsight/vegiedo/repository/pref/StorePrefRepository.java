package com.devinsight.vegiedo.repository.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StorePrefRepository {
    private static final String STORE_ID = "store_id";
    private static final String STORE_ID_KEY = "store_ids";


    private SharedPreferences sharedPreferences;
    private Gson gson;

    public StorePrefRepository(Context context){
        sharedPreferences = context.getSharedPreferences(STORE_ID,Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void addStoreId(long storeId){
        List<Long> storeIds = getStoreIdList();
        if(!storeIds.contains(storeId)) { // 중복 저장 방지
            storeIds.add(storeId);
            String storeIdListJson = gson.toJson(storeIds);
            sharedPreferences.edit().putString(STORE_ID_KEY, storeIdListJson).apply();
        }
    }

    public List<Long> getStoreIdList(){
        String storeIdListJson = sharedPreferences.getString(STORE_ID_KEY, "");
        if(!storeIdListJson.isEmpty()){
            Type listType = new TypeToken<ArrayList<Long>>() {}.getType();
            return gson.fromJson(storeIdListJson, listType);
        } else {
            return new ArrayList<>(); // 저장된 ID가 없으면 빈 리스트 반환
        }
    }

}
