package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportsInquiryResponseDTO {
    @Expose
    @SerializedName("")private List<Report> reports;

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public static class Report {
        private Long reportId;
        private String reportType;  // 신고 타입을 문자열로 가정합니다. 필요에 따라 다른 자료형을 사용할 수 있습니다.
        private String opinion;  // 기타 사유
        // 여기에 필요한 다른 필드들을 추가할 수 있습니다.

        // getters and setters...

        public Long getReportId() {
            return reportId;
        }

        public void setReportId(Long reportId) {
            this.reportId = reportId;
        }

        public String getReportType() {
            return reportType;
        }

        public void setReportType(String reportType) {
            this.reportType = reportType;
        }

        public String getOpinion() {
            return opinion;
        }

        public void setOpinion(String opinion) {
            this.opinion = opinion;
        }
    }
}
