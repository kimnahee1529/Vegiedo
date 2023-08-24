package com.devinsight.vegiedo;

import com.google.gson.annotations.SerializedName;

public class GoogleLoginResponseDTO {

    @SerializedName("access_token") private String accessToken;

    public GoogleLoginResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
