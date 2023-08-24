package com.devinsight.vegiedo;

import com.google.gson.annotations.SerializedName;

public class GoogleLoginRequestDTO {

    @SerializedName("grant_type") private String grantType;
    @SerializedName("client_id") private String clientId;
    @SerializedName("client_secret") private String clientSecret;
    @SerializedName("code") private String code;

    public GoogleLoginRequestDTO(String grantType, String clientId, String clientSecret, String code) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
