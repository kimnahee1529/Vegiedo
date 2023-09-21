package com.devinsight.vegiedo.data.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentRegisterRequestDTO {
    @Expose
    @SerializedName("commentContent")private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommentRegisterRequestDTO(String content) {
        this.content = content;
    }
}
