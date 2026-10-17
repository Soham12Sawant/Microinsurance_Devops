package com.microinsurance.platform.controller;

import com.microinsurance.platform.dto.ApplicationResponseDto;
import com.microinsurance.platform.dto.ApprovalDecisionDto;
import com.microinsurance.platform.dto.EnrollmentRequestDto;
import com.microinsurance.platform.model.EnrollmentStatus;
import com.microinsurance.platform.model.PolicyType;
import com.microinsurance.platform.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApplicationRestController {

    private final ApplicationService applicationService;

    public ApplicationRestController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/applications")
    public ResponseEntity<ApplicationResponseDto> submitEnrollment(@Valid @RequestBody EnrollmentRequestDto request) {
        ApplicationResponseDto response = applicationService.submitEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponseDto>> listApplications(
            @RequestParam(required = false) EnrollmentStatus status,
            @RequestParam(required = false) PolicyType policyType,
            @RequestParam(required = false) String search) {
        List<ApplicationResponseDto> list = applicationService.getAllApplications(status, policyType, search);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<ApplicationResponseDto> getApplicationById(@PathVariable Long id) {
        ApplicationResponseDto response = applicationService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/applications/track/{referenceNumber}")
    public ResponseEntity<ApplicationResponseDto> trackByReference(@PathVariable String referenceNumber) {
        ApplicationResponseDto response = applicationService.getByReferenceNumber(referenceNumber);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/applications/{id}/decision")
    public ResponseEntity<ApplicationResponseDto> processDecision(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalDecisionDto decision) {
        ApplicationResponseDto response = applicationService.processApprovalDecision(id, decision);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        return ResponseEntity.ok(applicationService.getMetricsSummary());
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("status", "UP");
        health.put("service", "Microinsurance Enrollment Platform");
        health.put("timestamp", LocalDateTime.now());
        health.put("metrics", applicationService.getMetricsSummary());
        return ResponseEntity.ok(health);
    }
}
