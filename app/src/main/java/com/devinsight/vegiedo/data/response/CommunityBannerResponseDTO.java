package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommunityBannerResponseDTO {
    @Expose
    @SerializedName("communityBannerUrl") private String communityBannerUrl;
    @Expose
    @SerializedName("url")private String url;

    public String getCommunityBannerUrl() {
        return communityBannerUrl;
    }

    public void setCommunityBannerUrl(String communityBannerUrl) {
        this.communityBannerUrl = communityBannerUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CommunityBannerResponseDTO(String communityBannerUrl, String url) {
        this.communityBannerUrl = communityBannerUrl;
        this.url = url;
    }
}
