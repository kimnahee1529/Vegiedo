package com.devinsight.vegiedo.data.response;

import java.util.List;
import java.util.Map;

public class MapInquiryResponseDTO {
    private List<MapStoreListData> stores;

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