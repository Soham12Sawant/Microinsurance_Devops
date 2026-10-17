package com.microinsurance.platform.model;

public enum PolicyType {
    CROP_SHIELD("Crop Micro-Protection", 120.0, 3000.0),
    HEALTH_BASIC("Artisan Health Shield", 180.0, 5000.0),
    LIVESTOCK_GUARD("Livestock Micro-Coverage", 150.0, 4000.0),
    LIFE_ACCIDENT("Daily Wage Accident Cover", 90.0, 2500.0);

    private final String displayName;
    private final double defaultMonthlyPremium;
    private final double defaultCoverage;

    PolicyType(String displayName, double defaultMonthlyPremium, double defaultCoverage) {
        this.displayName = displayName;
        this.defaultMonthlyPremium = defaultMonthlyPremium;
        this.defaultCoverage = defaultCoverage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDefaultMonthlyPremium() {
        return defaultMonthlyPremium;
    }

    public double getDefaultCoverage() {
        return defaultCoverage;
    }
}
