package com.microinsurance.platform.dto;

import com.microinsurance.platform.model.EnrollmentStatus;
import com.microinsurance.platform.model.ParticipantApplication;
import com.microinsurance.platform.model.PolicyType;
import com.microinsurance.platform.model.RiskTier;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApplicationResponseDto {

    private Long id;
    private String referenceNumber;
    private String fullName;
    private String nationalId;
    private String phoneNumber;
    private String email;
    private LocalDate dateOfBirth;
    private Integer age;
    private String gender;
    private Double annualIncome;
    private String occupation;
    private PolicyType policyType;
    private String policyDisplayName;
    private Double coverageAmount;
    private Double monthlyPremium;
    private String nomineeName;
    private String nomineeRelationship;
    private EnrollmentStatus status;
    private RiskTier riskTier;
    private String riskTierDescription;
    private String underwriterRemarks;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;

    public static ApplicationResponseDto fromEntity(ParticipantApplication entity) {
        ApplicationResponseDto dto = new ApplicationResponseDto();
        dto.setId(entity.getId());
        dto.setReferenceNumber(entity.getReferenceNumber());
        dto.setFullName(entity.getFullName());
        dto.setNationalId(entity.getNationalId());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setAge(entity.getAge());
        dto.setGender(entity.getGender());
        dto.setAnnualIncome(entity.getAnnualIncome());
        dto.setOccupation(entity.getOccupation());
        dto.setPolicyType(entity.getPolicyType());
        dto.setPolicyDisplayName(entity.getPolicyType() != null ? entity.getPolicyType().getDisplayName() : "");
        dto.setCoverageAmount(entity.getCoverageAmount());
        dto.setMonthlyPremium(entity.getMonthlyPremium());
        dto.setNomineeName(entity.getNomineeName());
        dto.setNomineeRelationship(entity.getNomineeRelationship());
        dto.setStatus(entity.getStatus());
        dto.setRiskTier(entity.getRiskTier());
        dto.setRiskTierDescription(entity.getRiskTier() != null ? entity.getRiskTier().getDescription() : "");
        dto.setUnderwriterRemarks(entity.getUnderwriterRemarks());
        dto.setSubmittedAt(entity.getSubmittedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    public String getPolicyDisplayName() {
        return policyDisplayName;
    }

    public void setPolicyDisplayName(String policyDisplayName) {
        this.policyDisplayName = policyDisplayName;
    }

    public Double getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(Double coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public Double getMonthlyPremium() {
        return monthlyPremium;
    }

    public void setMonthlyPremium(Double monthlyPremium) {
        this.monthlyPremium = monthlyPremium;
    }

    public String getNomineeName() {
        return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getNomineeRelationship() {
        return nomineeRelationship;
    }

    public void setNomineeRelationship(String nomineeRelationship) {
        this.nomineeRelationship = nomineeRelationship;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public RiskTier getRiskTier() {
        return riskTier;
    }

    public void setRiskTier(RiskTier riskTier) {
        this.riskTier = riskTier;
    }

    public String getRiskTierDescription() {
        return riskTierDescription;
    }

    public void setRiskTierDescription(String riskTierDescription) {
        this.riskTierDescription = riskTierDescription;
    }

    public String getUnderwriterRemarks() {
        return underwriterRemarks;
    }

    public void setUnderwriterRemarks(String underwriterRemarks) {
        this.underwriterRemarks = underwriterRemarks;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
