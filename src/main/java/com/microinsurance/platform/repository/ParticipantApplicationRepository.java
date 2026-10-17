package com.microinsurance.platform.repository;

import com.microinsurance.platform.model.EnrollmentStatus;
import com.microinsurance.platform.model.ParticipantApplication;
import com.microinsurance.platform.model.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantApplicationRepository extends JpaRepository<ParticipantApplication, Long> {

    Optional<ParticipantApplication> findByReferenceNumber(String referenceNumber);

    Optional<ParticipantApplication> findByNationalId(String nationalId);

    List<ParticipantApplication> findByPhoneNumber(String phoneNumber);

    List<ParticipantApplication> findByStatus(EnrollmentStatus status);

    List<ParticipantApplication> findByPolicyType(PolicyType policyType);

    boolean existsByNationalId(String nationalId);

    List<ParticipantApplication> findByOrderBySubmittedAtDesc();

    List<ParticipantApplication> findByFullNameContainingIgnoreCaseOrReferenceNumberContainingIgnoreCaseOrderBySubmittedAtDesc(
            String name, String ref);
}
