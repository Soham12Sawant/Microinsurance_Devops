package com.microinsurance.platform.model;

public enum RiskTier {
    LOW_RISK("Low Risk - Eligible for Instant Auto-Approval"),
    MODERATE_RISK("Moderate Risk - Standard Verification Required"),
    HIGH_RISK("High Risk - Requires Senior Underwriter Inspection");

    private final String description;

    RiskTier(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
