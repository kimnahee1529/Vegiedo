package com.devinsight.vegiedo.data.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostReportRequestDTO {

    @Expose
    @SerializedName("contentType")private String contentType;
    @Expose
    @SerializedName("trollType")private String trollType;
    @Expose
    @SerializedName("memo")private String memo;

    public PostReportRequestDTO(String contentType, String trollType, String memo) {
        this.contentType = contentType;
        this.trollType = trollType;
        this.memo = memo;
    }
    public PostReportRequestDTO() {

    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTrollType() {
        return trollType;
    }

    public void setTrollType(String trollType) {
        this.trollType = trollType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
