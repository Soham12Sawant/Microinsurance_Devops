package com.microinsurance.platform.dto;

import com.microinsurance.platform.model.PolicyType;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class EnrollmentRequestDto {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "National ID / Identification Number is required")
    @Pattern(regexp = "^[A-Za-z0-9-]{6,20}$", message = "National ID must be alphanumeric and between 6 to 20 characters")
    private String nationalId;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9+]{10,15}$", message = "Phone number must be a valid 10-15 digit number")
    private String phoneNumber;

    @Email(message = "Provide a valid email address if available")
    private String email;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private String gender;

    @NotNull(message = "Annual income is required")
    @Positive(message = "Annual income must be greater than zero")
    private Double annualIncome;

    @NotBlank(message = "Occupation is required")
    private String occupation;

    @NotNull(message = "Policy type must be selected")
    private PolicyType policyType;

    @Positive(message = "Coverage amount must be positive")
    private Double coverageAmount;

    @NotBlank(message = "Nominee name is required")
    private String nomineeName;

    @NotBlank(message = "Nominee relationship is required")
    private String nomineeRelationship;

    private String nomineeContact;

    public EnrollmentRequestDto() {
    }

    // Getters and Setters
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

    public Double getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(Double coverageAmount) {
        this.coverageAmount = coverageAmount;
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

    public String getNomineeContact() {
        return nomineeContact;
    }

    public void setNomineeContact(String nomineeContact) {
        this.nomineeContact = nomineeContact;
    }
}
