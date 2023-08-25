package com.devinsight.vegiedo.utill;

public enum UserInfoTag {

    USER_NICKNAME("userName"),
    USER_PROFILE("userProfile"),
    USER_TAGS("userTags");

    private String infoType;

    private UserInfoTag(String infoType){
        this.infoType = infoType;
    }

    public String getInfoType() {
        return infoType;
    }



}
