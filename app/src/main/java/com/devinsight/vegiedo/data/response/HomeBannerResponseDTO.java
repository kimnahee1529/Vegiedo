package com.devinsight.vegiedo.data.response;

import com.devinsight.vegiedo.data.ui.home.HomeBannerData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeBannerResponseDTO {
    @Expose
    @SerializedName("comments") private List<HomeBannerData> homeData;

    public HomeBannerResponseDTO(List<HomeBannerData> homeData) {
        this.homeData = homeData;
    }

    public List<HomeBannerData> getHomeData() {
        return homeData;
    }

    public void setHomeData(List<HomeBannerData> homeData) {
        this.homeData = homeData;
    }
}
