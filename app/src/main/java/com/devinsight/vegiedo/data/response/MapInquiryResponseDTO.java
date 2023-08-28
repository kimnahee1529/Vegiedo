package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class MapInquiryResponseDTO {
    @Expose
    @SerializedName("stores")private List<MapStoreListData> stores;

    public List<MapStoreListData> getStores() {
        return stores;
    }

    public void setStores(List<MapStoreListData> stores) {
        this.stores = stores;
    }

    public MapInquiryResponseDTO(List<MapStoreListData> stores) {
        this.stores = stores;
    }
}