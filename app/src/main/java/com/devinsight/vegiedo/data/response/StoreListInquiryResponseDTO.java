package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreListInquiryResponseDTO {
    @Expose
    @SerializedName("stores")private List<StoreListData> stores;

    public List<StoreListData> getStores() {
        return stores;
    }

    public void setStores(List<StoreListData> stores) {
        this.stores = stores;
    }

    public StoreListInquiryResponseDTO(List<StoreListData> stores) {
        this.stores = stores;
    }
}
