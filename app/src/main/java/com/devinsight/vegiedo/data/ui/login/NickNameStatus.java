package com.devinsight.vegiedo.data.ui.login;

public class NickNameStatus {

    String nickName;
    String nickNameMessage;
    int isNicknameAvailable;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickNameMessage() {
        return nickNameMessage;
    }

    public void setNickNameMessage(String nickNameMessage) {
        this.nickNameMessage = nickNameMessage;
    }

    public int getIsNicknameAvailable() {
        return isNicknameAvailable;
    }

    public void setIsNicknameAvailable(int isNicknameAvailable) {
        this.isNicknameAvailable = isNicknameAvailable;
    }

    public NickNameStatus(String nickNameMessage, int isNicknameAvailable, String nickName) {
        this.nickNameMessage = nickNameMessage;
        this.isNicknameAvailable = isNicknameAvailable;
        this.nickName = nickName;
    }


}
