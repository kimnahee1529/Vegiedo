package com.devinsight.vegiedo.data.request;

public class ReviewReportRequestDTO {
    private Integer reportType; // 신고 유형
    private String opinion;    // 기타 사유 or 추가 의견

    // 기본 생성자
    public ReviewReportRequestDTO() {}

    // 모든 필드를 매개변수로 갖는 생성자
    public ReviewReportRequestDTO(Integer reportType, String opinion) {
        this.reportType = reportType;
        this.opinion = opinion;
    }

    // Getter, Setter

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

    // toString(), hashCode(), equals() 메서드 추가 가능
}
