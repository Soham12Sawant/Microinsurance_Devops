package com.microinsurance.platform;

import com.microinsurance.platform.model.EnrollmentStatus;
import com.microinsurance.platform.model.ParticipantApplication;
import com.microinsurance.platform.model.PolicyType;
import com.microinsurance.platform.model.RiskTier;
import com.microinsurance.platform.repository.ParticipantApplicationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ParticipantApplicationRepository repository;

    public DataInitializer(ParticipantApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }

        // Participant 1: Smallholder Farmer (Crop Shield, Auto-Approved)
        ParticipantApplication app1 = new ParticipantApplication();
        app1.setReferenceNumber("MIP-2026-CRP01");
        app1.setFullName("Ramesh Kumar Patel");
        app1.setNationalId("AID-9821453210");
        app1.setPhoneNumber("+919876543210");
        app1.setEmail("ramesh.patel@agro.org");
        app1.setDateOfBirth(LocalDate.of(1988, 5, 14));
        app1.setAge(38);
        app1.setGender("Male");
        app1.setAnnualIncome(24000.0);
        app1.setOccupation("Smallholder Paddy Farmer");
        app1.setPolicyType(PolicyType.CROP_SHIELD);
        app1.setCoverageAmount(3000.0);
        app1.setMonthlyPremium(120.0);
        app1.setNomineeName("Sunita Patel");
        app1.setNomineeRelationship("Spouse");
        app1.setNomineeContact("+919876543211");
        app1.setStatus(EnrollmentStatus.AUTO_APPROVED);
        app1.setRiskTier(RiskTier.LOW_RISK);
        app1.setUnderwriterRemarks("Instant Auto-Approval: Met low-risk automated underwriting rules.");
        repository.save(app1);

        // Participant 2: Artisan Weaver (Health Shield, Approved after Review)
        ParticipantApplication app2 = new ParticipantApplication();
        app2.setReferenceNumber("MIP-2026-HLT02");
        app2.setFullName("Fatima Begum");
        app2.setNationalId("AID-8745129034");
        app2.setPhoneNumber("+919812345678");
        app2.setEmail("fatima.begum@handloom.net");
        app2.setDateOfBirth(LocalDate.of(1974, 9, 21));
        app2.setAge(52);
        app2.setGender("Female");
        app2.setAnnualIncome(38000.0);
        app2.setOccupation("Handloom Silk Weaver");
        app2.setPolicyType(PolicyType.HEALTH_BASIC);
        app2.setCoverageAmount(5000.0);
        app2.setMonthlyPremium(180.0);
        app2.setNomineeName("Tariq Begum");
        app2.setNomineeRelationship("Son");
        app2.setNomineeContact("+919812345679");
        app2.setStatus(EnrollmentStatus.APPROVED);
        app2.setRiskTier(RiskTier.MODERATE_RISK);
        app2.setUnderwriterRemarks("Verified artisan cooperative membership card; approved by Underwriter Sarah M.");
        repository.save(app2);

        // Participant 3: Dairy Herder (Livestock Guard, Under Review)
        ParticipantApplication app3 = new ParticipantApplication();
        app3.setReferenceNumber("MIP-2026-LVS03");
        app3.setFullName("Anand Sharma");
        app3.setNationalId("AID-5541298711");
        app3.setPhoneNumber("+919744123890");
        app3.setEmail("anand.sharma@dairyco.in");
        app3.setDateOfBirth(LocalDate.of(1964, 2, 10));
        app3.setAge(62);
        app3.setGender("Male");
        app3.setAnnualIncome(52000.0);
        app3.setOccupation("Dairy Cattle Herder");
        app3.setPolicyType(PolicyType.LIVESTOCK_GUARD);
        app3.setCoverageAmount(4000.0);
        app3.setMonthlyPremium(150.0);
        app3.setNomineeName("Geeta Sharma");
        app3.setNomineeRelationship("Spouse");
        app3.setNomineeContact("+919744123891");
        app3.setStatus(EnrollmentStatus.UNDER_REVIEW);
        app3.setRiskTier(RiskTier.HIGH_RISK);
        app3.setUnderwriterRemarks("Flagged for Senior Underwriter Audit: Age 62 requires veterinary health certificate inspection.");
        repository.save(app3);

        // Participant 4: Daily Wage Construction Worker (Life & Accident, Auto-Approved)
        ParticipantApplication app4 = new ParticipantApplication();
        app4.setReferenceNumber("MIP-2026-ACC04");
        app4.setFullName("Kavitha Murugan");
        app4.setNationalId("AID-3349812045");
        app4.setPhoneNumber("+919655223344");
        app4.setEmail("kavitha.m@labournet.org");
        app4.setDateOfBirth(LocalDate.of(1996, 11, 3));
        app4.setAge(30);
        app4.setGender("Female");
        app4.setAnnualIncome(18000.0);
        app4.setOccupation("Construction Daily Laborer");
        app4.setPolicyType(PolicyType.LIFE_ACCIDENT);
        app4.setCoverageAmount(2500.0);
        app4.setMonthlyPremium(90.0);
        app4.setNomineeName("Murugan Selvam");
        app4.setNomineeRelationship("Brother");
        app4.setNomineeContact("+919655223345");
        app4.setStatus(EnrollmentStatus.AUTO_APPROVED);
        app4.setRiskTier(RiskTier.LOW_RISK);
        app4.setUnderwriterRemarks("Instant Auto-Approval: Met low-risk automated underwriting rules.");
        repository.save(app4);
    }
}
