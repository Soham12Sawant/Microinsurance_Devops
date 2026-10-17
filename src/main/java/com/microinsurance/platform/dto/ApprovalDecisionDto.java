package com.microinsurance.platform.dto;

import com.microinsurance.platform.model.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;

public class ApprovalDecisionDto {

    @NotNull(message = "Decision status is required (APPROVED or REJECTED)")
    private EnrollmentStatus status;

    private String remarks;

    public ApprovalDecisionDto() {
    }

    public ApprovalDecisionDto(EnrollmentStatus status, String remarks) {
        this.status = status;
        this.remarks = remarks;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
