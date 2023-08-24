package com.devinsight.vegiedo;

public enum ConstLoginTokenType {

    KAKAO_LOGIN("KAKAO"),
    GOOGLE_LOGIN("GOOGLE");

    String loginType;

    ConstLoginTokenType(String loginType){
        this.loginType = loginType;
    }



}