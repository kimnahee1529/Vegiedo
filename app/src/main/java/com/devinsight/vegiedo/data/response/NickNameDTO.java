package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NickNameDTO {
    @Expose
    @SerializedName("nickname")private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public NickNameDTO(String nickName) {
        this.nickName = nickName;
    }
}
