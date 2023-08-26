package com.devinsight.vegiedo.data.response;

import java.util.List;

public class StoreListInquiryResponseDTO {
    private List<StoreListData> stores;

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
