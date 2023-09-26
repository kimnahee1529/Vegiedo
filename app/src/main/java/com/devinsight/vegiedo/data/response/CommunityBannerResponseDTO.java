package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommunityBannerResponseDTO {
    @Expose
    @SerializedName("communityBannerUrl") private String communityBannerUrl;

    public String getCommunityBannerUrl() {
        return communityBannerUrl;
    }

    public void setCommunityBannerUrl(String communityBannerUrl) {
        this.communityBannerUrl = communityBannerUrl;
    }

    public CommunityBannerResponseDTO(String communityBannerUrl) {
        this.communityBannerUrl = communityBannerUrl;
    }
}
