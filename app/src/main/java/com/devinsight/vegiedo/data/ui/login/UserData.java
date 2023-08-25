package com.devinsight.vegiedo.data.ui.login;

import java.util.List;

public class UserData {

    private Long userId;
    private String userName;
    private String userProfile;
    private List<String> userTagList;

    public UserData(Long userId, String userName, String userProfile, List<String> userTagList) {
        this.userId = userId;
        this.userName = userName;
        this.userProfile = userProfile;
        this.userTagList = userTagList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public List<String> getUserTagList() {
        return userTagList;
    }

    public void setUserTagList(List<String> userTagList) {
        this.userTagList = userTagList;
    }


}
