package com.microinsurance.platform.unit;

import com.microinsurance.platform.dto.ApplicationResponseDto;
import com.microinsurance.platform.dto.ApprovalDecisionDto;
import com.microinsurance.platform.dto.EnrollmentRequestDto;
import com.microinsurance.platform.model.EnrollmentStatus;
import com.microinsurance.platform.model.PolicyType;
import com.microinsurance.platform.model.RiskTier;
import com.microinsurance.platform.service.ApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ApplicationServiceTest {

    @Autowired
    private ApplicationService applicationService;

    @Test
    @DisplayName("Test 1: Low-Risk applicant eligible for instant auto-approval")
    public void testAutoApprovalForLowRisk() {
        EnrollmentRequestDto req = new EnrollmentRequestDto();
        req.setFullName("Sunil Rao");
        req.setNationalId("AID-TEST-1001");
        req.setPhoneNumber("+919876500001");
        req.setDateOfBirth(LocalDate.of(1995, 4, 12)); // 31 years old
        req.setAnnualIncome(25000.0);
        req.setOccupation("Artisan");
        req.setPolicyType(PolicyType.CROP_SHIELD);
        req.setCoverageAmount(3000.0);
        req.setNomineeName("Lata Rao");
        req.setNomineeRelationship("Spouse");

        ApplicationResponseDto response = applicationService.submitEnrollment(req);

        assertNotNull(response.getId());
        assertNotNull(response.getReferenceNumber());
        assertEquals(EnrollmentStatus.AUTO_APPROVED, response.getStatus());
        assertEquals(RiskTier.LOW_RISK, response.getRiskTier());
        assertTrue(response.getReferenceNumber().startsWith("MIP-2026-"));
    }

    @Test
    @DisplayName("Test 2: High income exceeding microinsurance cap is rejected")
    public void testExceedingIncomeLimitThrowsException() {
        EnrollmentRequestDto req = new EnrollmentRequestDto();
        req.setFullName("Vikram Merchant");
        req.setNationalId("AID-TEST-1002");
        req.setPhoneNumber("+919876500002");
        req.setDateOfBirth(LocalDate.of(1985, 3, 10));
        req.setAnnualIncome(95000.0); // Exceeds 60,000 threshold
        req.setOccupation("Wholesale Trader");
        req.setPolicyType(PolicyType.HEALTH_BASIC);
        req.setNomineeName("Pooja Merchant");
        req.setNomineeRelationship("Spouse");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            applicationService.submitEnrollment(req);
        });

        assertTrue(ex.getMessage().contains("exceeds the microinsurance threshold"));
    }

    @Test
    @DisplayName("Test 3: Underage applicant (<18) is rejected by validation")
    public void testUnderageApplicantRejected() {
        EnrollmentRequestDto req = new EnrollmentRequestDto();
        req.setFullName("Young Student");
        req.setNationalId("AID-TEST-1003");
        req.setPhoneNumber("+919876500003");
        req.setDateOfBirth(LocalDate.now().minusYears(16)); // 16 years old
        req.setAnnualIncome(10000.0);
        req.setOccupation("Apprentice");
        req.setPolicyType(PolicyType.LIFE_ACCIDENT);
        req.setNomineeName("Father");
        req.setNomineeRelationship("Parent");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            applicationService.submitEnrollment(req);
        });

        assertTrue(ex.getMessage().contains("does not meet microinsurance criteria"));
    }

    @Test
    @DisplayName("Test 4: Duplicate National ID is detected and blocked")
    public void testDuplicateNationalIdBlocked() {
        EnrollmentRequestDto req1 = new EnrollmentRequestDto();
        req1.setFullName("Kishore Das");
        req1.setNationalId("AID-TEST-DUP-99");
        req1.setPhoneNumber("+919876500099");
        req1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        req1.setAnnualIncome(20000.0);
        req1.setOccupation("Potter");
        req1.setPolicyType(PolicyType.CROP_SHIELD);
        req1.setNomineeName("Meena");
        req1.setNomineeRelationship("Spouse");

        applicationService.submitEnrollment(req1);

        // Attempt second registration with same national ID
        EnrollmentRequestDto req2 = new EnrollmentRequestDto();
        req2.setFullName("Kishore Das Duplicate");
        req2.setNationalId("AID-TEST-DUP-99");
        req2.setPhoneNumber("+919876500099");
        req2.setDateOfBirth(LocalDate.of(1990, 1, 1));
        req2.setAnnualIncome(20000.0);
        req2.setOccupation("Potter");
        req2.setPolicyType(PolicyType.HEALTH_BASIC);
        req2.setNomineeName("Meena");
        req2.setNomineeRelationship("Spouse");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            applicationService.submitEnrollment(req2);
        });

        assertTrue(ex.getMessage().contains("already exists in the platform"));
    }

    @Test
    @DisplayName("Test 5: Underwriting manual review approval workflow")
    public void testUnderwriterApprovalWorkflow() {
        // Create an application with moderate risk (age 55)
        EnrollmentRequestDto req = new EnrollmentRequestDto();
        req.setFullName("Mohan Lal");
        req.setNationalId("AID-TEST-REVIEW-1");
        req.setPhoneNumber("+919876500088");
        req.setDateOfBirth(LocalDate.of(1970, 6, 15)); // Age 56
        req.setAnnualIncome(45000.0);
        req.setOccupation("Handloom Weaver");
        req.setPolicyType(PolicyType.HEALTH_BASIC);
        req.setCoverageAmount(5000.0);
        req.setNomineeName("Rita Lal");
        req.setNomineeRelationship("Spouse");

        ApplicationResponseDto submitted = applicationService.submitEnrollment(req);
        assertEquals(EnrollmentStatus.UNDER_REVIEW, submitted.getStatus());

        // Underwriter reviews and approves
        ApprovalDecisionDto decision = new ApprovalDecisionDto(
                EnrollmentStatus.APPROVED,
                "Health history verified and validated by Underwriting Team."
        );
        ApplicationResponseDto approved = applicationService.processApprovalDecision(submitted.getId(), decision);

        assertEquals(EnrollmentStatus.APPROVED, approved.getStatus());
        assertTrue(approved.getUnderwriterRemarks().contains("Health history verified"));
    }

    @Test
    @DisplayName("Test 6: Tracking application by Reference Number")
    public void testTrackingByReference() {
        EnrollmentRequestDto req = new EnrollmentRequestDto();
        req.setFullName("Aarti Devi");
        req.setNationalId("AID-TEST-TRACK-1");
        req.setPhoneNumber("+919876500077");
        req.setDateOfBirth(LocalDate.of(1992, 8, 20));
        req.setAnnualIncome(22000.0);
        req.setOccupation("Vendor");
        req.setPolicyType(PolicyType.LIFE_ACCIDENT);
        req.setNomineeName("Raju");
        req.setNomineeRelationship("Spouse");

        ApplicationResponseDto created = applicationService.submitEnrollment(req);
        ApplicationResponseDto tracked = applicationService.getByReferenceNumber(created.getReferenceNumber());

        assertEquals(created.getId(), tracked.getId());
        assertEquals("Aarti Devi", tracked.getFullName());
    }
}
