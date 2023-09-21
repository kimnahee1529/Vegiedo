package com.devinsight.vegiedo.data.request;

import java.util.List;

public class UserNicknameModifyRequestDTO {

    private String nickname;

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }

    public UserNicknameModifyRequestDTO(String nickname) {
        this.nickname = nickname;
    }
}
