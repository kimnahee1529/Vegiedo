package com.devinsight.vegiedo.data.ui.login;

public class TagStatus {

    boolean status;
    String content;
    int btnId;

    public int getBtnId() {
        return btnId;
    }

    public void setBtnId(int btnId) {
        this.btnId = btnId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TagStatus(boolean status, String content, int btnId) {
        this.status = status;
        this.content = content;
    }
}
