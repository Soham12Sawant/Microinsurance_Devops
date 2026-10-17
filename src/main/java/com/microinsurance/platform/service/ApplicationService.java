package com.microinsurance.platform.service;

import com.microinsurance.platform.dto.ApplicationResponseDto;
import com.microinsurance.platform.dto.ApprovalDecisionDto;
import com.microinsurance.platform.dto.EnrollmentRequestDto;
import com.microinsurance.platform.model.EnrollmentStatus;
import com.microinsurance.platform.model.ParticipantApplication;
import com.microinsurance.platform.model.PolicyType;
import com.microinsurance.platform.model.RiskTier;
import com.microinsurance.platform.repository.ParticipantApplicationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ParticipantApplicationRepository repository;

    @Value("${app.insurance.min-age:18}")
    private int minAge;

    @Value("${app.insurance.max-age:65}")
    private int maxAge;

    @Value("${app.insurance.max-eligible-annual-income:60000.0}")
    private double maxEligibleIncome;

    @Value("${app.insurance.auto-approval-threshold-coverage:5000.0}")
    private double autoApprovalCoverageLimit;

    public ApplicationService(ParticipantApplicationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ApplicationResponseDto submitEnrollment(EnrollmentRequestDto request) {
        // 1. Duplicate National ID Check
        if (repository.existsByNationalId(request.getNationalId().trim())) {
            throw new IllegalArgumentException("An application with National ID '" + request.getNationalId() + "' already exists in the platform.");
        }

        // 2. Age Calculation & Validation
        int calculatedAge = Period.between(request.getDateOfBirth(), LocalDate.now()).getYears();
        if (calculatedAge < minAge || calculatedAge > maxAge) {
            throw new IllegalArgumentException("Applicant age (" + calculatedAge + " years) does not meet microinsurance criteria (" + minAge + "-" + maxAge + " years).");
        }

        // 3. Income Eligibility Check
        if (request.getAnnualIncome() > maxEligibleIncome) {
            throw new IllegalArgumentException("Annual household income (" + request.getAnnualIncome() + ") exceeds the microinsurance threshold (" + maxEligibleIncome + "). Applicant is referred to standard commercial insurance.");
        }

        // 4. Policy & Coverage Assignment
        PolicyType policy = request.getPolicyType();
        double coverage = (request.getCoverageAmount() != null && request.getCoverageAmount() > 0)
                ? request.getCoverageAmount()
                : policy.getDefaultCoverage();
        double monthlyPremium = policy.getDefaultMonthlyPremium();

        // 5. Automated Risk Scoring & Tier Determination
        RiskTier riskTier;
        EnrollmentStatus initialStatus;
        String systemRemarks;

        if (calculatedAge <= 50 && request.getAnnualIncome() <= 40000 && coverage <= autoApprovalCoverageLimit) {
            riskTier = RiskTier.LOW_RISK;
            initialStatus = EnrollmentStatus.AUTO_APPROVED;
            systemRemarks = "Instant Auto-Approval: Met low-risk automated underwriting rules.";
        } else if (calculatedAge <= 60 && request.getAnnualIncome() <= maxEligibleIncome) {
            riskTier = RiskTier.MODERATE_RISK;
            initialStatus = EnrollmentStatus.UNDER_REVIEW;
            systemRemarks = "Pending Underwriter Verification: Moderate demographic risk bracket.";
        } else {
            riskTier = RiskTier.HIGH_RISK;
            initialStatus = EnrollmentStatus.UNDER_REVIEW;
            systemRemarks = "Flagged for Senior Underwriter Audit: Age or coverage parameters require manual evaluation.";
        }

        // 6. Generate Unique Reference Number (MIP-YYYY-XXXXX)
        String refNumber = generateReferenceNumber();

        // 7. Persist Application
        ParticipantApplication app = new ParticipantApplication();
        app.setReferenceNumber(refNumber);
        app.setFullName(request.getFullName().trim());
        app.setNationalId(request.getNationalId().trim());
        app.setPhoneNumber(request.getPhoneNumber().trim());
        app.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
        app.setDateOfBirth(request.getDateOfBirth());
        app.setAge(calculatedAge);
        app.setGender(request.getGender() != null ? request.getGender().trim() : "Unspecified");
        app.setAnnualIncome(request.getAnnualIncome());
        app.setOccupation(request.getOccupation().trim());
        app.setPolicyType(policy);
        app.setCoverageAmount(coverage);
        app.setMonthlyPremium(monthlyPremium);
        app.setNomineeName(request.getNomineeName().trim());
        app.setNomineeRelationship(request.getNomineeRelationship().trim());
        app.setNomineeContact(request.getNomineeContact() != null ? request.getNomineeContact().trim() : null);
        app.setRiskTier(riskTier);
        app.setStatus(initialStatus);
        app.setUnderwriterRemarks(systemRemarks);

        ParticipantApplication saved = repository.save(app);
        return ApplicationResponseDto.fromEntity(saved);
    }

    @Transactional
    public ApplicationResponseDto processApprovalDecision(Long id, ApprovalDecisionDto decision) {
        ParticipantApplication app = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Application not found with ID: " + id));

        if (decision.getStatus() != EnrollmentStatus.APPROVED && decision.getStatus() != EnrollmentStatus.REJECTED) {
            throw new IllegalArgumentException("Decision must be either APPROVED or REJECTED.");
        }

        app.setStatus(decision.getStatus());
        String remarks = (decision.getRemarks() != null && !decision.getRemarks().isBlank())
                ? decision.getRemarks().trim()
                : "Decision updated to " + decision.getStatus() + " by Underwriting Team.";
        app.setUnderwriterRemarks(remarks);

        ParticipantApplication updated = repository.save(app);
        return ApplicationResponseDto.fromEntity(updated);
    }

    public List<ApplicationResponseDto> getAllApplications(EnrollmentStatus status, PolicyType policyType, String search) {
        List<ParticipantApplication> list;

        if (search != null && !search.isBlank()) {
            list = repository.findByFullNameContainingIgnoreCaseOrReferenceNumberContainingIgnoreCaseOrderBySubmittedAtDesc(
                    search.trim(), search.trim());
        } else if (status != null) {
            list = repository.findByStatus(status);
        } else if (policyType != null) {
            list = repository.findByPolicyType(policyType);
        } else {
            list = repository.findByOrderBySubmittedAtDesc();
        }

        return list.stream()
                .map(ApplicationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ApplicationResponseDto getByReferenceNumber(String refNumber) {
        ParticipantApplication app = repository.findByReferenceNumber(refNumber.trim().toUpperCase())
                .orElseThrow(() -> new NoSuchElementException("No application found with Reference Number: " + refNumber));
        return ApplicationResponseDto.fromEntity(app);
    }

    public ApplicationResponseDto getById(Long id) {
        ParticipantApplication app = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No application found with ID: " + id));
        return ApplicationResponseDto.fromEntity(app);
    }

    public Map<String, Object> getMetricsSummary() {
        List<ParticipantApplication> all = repository.findAll();
        long total = all.size();
        long autoApproved = all.stream().filter(a -> a.getStatus() == EnrollmentStatus.AUTO_APPROVED).count();
        long approved = all.stream().filter(a -> a.getStatus() == EnrollmentStatus.APPROVED).count();
        long underReview = all.stream().filter(a -> a.getStatus() == EnrollmentStatus.UNDER_REVIEW).count();
        long rejected = all.stream().filter(a -> a.getStatus() == EnrollmentStatus.REJECTED).count();

        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("totalApplications", total);
        metrics.put("autoApproved", autoApproved);
        metrics.put("underwriterApproved", approved);
        metrics.put("underReview", underReview);
        metrics.put("rejected", rejected);
        metrics.put("activeCoverageSum", all.stream()
                .filter(a -> a.getStatus() == EnrollmentStatus.APPROVED || a.getStatus() == EnrollmentStatus.AUTO_APPROVED)
                .mapToDouble(ParticipantApplication::getCoverageAmount)
                .sum());
        return metrics;
    }

    private String generateReferenceNumber() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder("MIP-2026-");
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
