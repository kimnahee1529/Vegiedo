package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StampBookInquiryResponseDTO {
    private List<StoreStampDetailDTO> storeStampDetailDtos;

    // Constructor
    public StampBookInquiryResponseDTO(List<StoreStampDetailDTO> storeStampDetailDtos) {
        this.storeStampDetailDtos = storeStampDetailDtos;
    }

    // Getter
    public List<StoreStampDetailDTO> getStoreStampDetailDtos() {
        return storeStampDetailDtos;
    }

    // Setter
    public void setStoreStampDetailDtos(List<StoreStampDetailDTO> storeStampDetailDtos) {
        this.storeStampDetailDtos = storeStampDetailDtos;
    }
}

