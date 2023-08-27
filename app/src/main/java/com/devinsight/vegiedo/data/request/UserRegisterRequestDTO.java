package com.devinsight.vegiedo.data.request;

import java.util.List;

public class UserRegisterRequestDTO {

    private String nickName;
    private List<String> tagList;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public UserRegisterRequestDTO(String nickName, List<String> tagList) {
        this.nickName = nickName;
        this.tagList = tagList;
    }
}
