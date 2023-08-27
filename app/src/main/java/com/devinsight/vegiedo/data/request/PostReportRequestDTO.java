package com.devinsight.vegiedo.data.request;

public class PostReportRequestDTO {
    private Integer reportType;
    private String opinion;

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public PostReportRequestDTO(Integer reportType, String opinion) {
        this.reportType = reportType;
        this.opinion = opinion;
    }
}
