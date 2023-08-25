package com.devinsight.vegiedo;

public enum ConstLoginTokenType {

    KAKAO_LOGIN("KAKAO"),
    GOOGLE_LOGIN("GOOGLE");

    String loginType;


    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    ConstLoginTokenType(String loginType){
        this.loginType = loginType;
    }



}