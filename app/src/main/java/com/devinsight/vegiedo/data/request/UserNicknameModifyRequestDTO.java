package com.devinsight.vegiedo.data.request;

import java.util.List;

public class UserNicknameModifyRequestDTO {

    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public UserNicknameModifyRequestDTO(String nickName) {
        this.nickName = nickName;
    }
}
