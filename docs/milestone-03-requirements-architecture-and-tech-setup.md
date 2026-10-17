# Milestone 3: Requirements, Architecture and Technology Setup
## Automated Microinsurance Enrollment Platform

---

### 1. Software Requirements Specification (SRS) Summary

#### 1.1 Functional Requirements (FR)
* **FR-01: Participant Intake & Registration:** System shall capture applicant demographics (Name, National ID, Mobile, Email, DOB, Gender), socio-economic data (Annual Household Income, Occupation), and Nominee details.
* **FR-02: Policy Configuration:** System shall support multiple microinsurance tiers:
  * Crop Micro-Protection ($120/mo, $3,000 coverage)
  * Artisan Health Shield ($180/mo, $5,000 coverage)
  * Livestock Micro-Coverage ($150/mo, $4,000 coverage)
  * Daily Wage Accident & Disability ($90/mo, $2,500 coverage)
* **FR-03: Automated Rules & Risk Scoring Engine:**
  * Age validation: Reject applications where age < 18 or > 65.
  * Income threshold: Reject applicants exceeding $60,000 annual income (refer to commercial plans).
  * Auto-approval: Automatically approve applicants with Low Risk (`LOW_RISK`: age <= 50, income <= $40,000, coverage <= $5,000).
  * Manual Audit Flagging: Route applicants to `UNDER_REVIEW` if moderate/high risk.
* **FR-04: Duplicate Enrollment Prevention:** Unique database constraint on `national_id` ensures no applicant holds multiple active subsidized policies.
* **FR-05: Underwriter Review Queue:** Underwriting officers can view pending applications, examine audit reason flags, and record decisions (`APPROVED`/`REJECTED`) with notes.
* **FR-06: Status Tracking & Policy Certificate:** Applicants can track their lifecycle progress using their Reference ID and view digital insurance passes.
* **FR-07: Health & Monitoring Endpoint:** Provide `/api/health` and `/actuator/health` exposing platform liveness, uptime, and database metrics.

#### 1.2 Non-Functional Requirements (NFR)
* **NFR-01 (Performance):** Average REST API response time < 200ms; end-to-end form submission with auto-approval < 1500ms.
* **NFR-02 (Availability & Reliability):** 99.9% uptime target backed by automated health check probes and instant recovery scripts.
* **NFR-03 (Security):** Non-root unprivileged execution inside Docker containers; input sanitization and parameterized SQL via JPA/Hibernate.
* **NFR-04 (Portability & Idempotency):** Multi-stage containerized build run on any OCI-compliant runtime; idempotent Ansible provisioning for target hosts.

---

### 2. Use-Case Diagram
```mermaid
graph TD
    Applicant["Beneficiary / Field Agent"]
    Underwriter["Underwriting Desk Officer"]
    DevOps["DevOps / SRE Engineer"]

    subgraph Platform_Boundary["MicroShield Platform"]
        UC1["Submit Digital Enrollment Form"]
        UC2["Automated Risk & Eligibility Check"]
        UC3["Track Enrollment & View Certificate"]
        UC4["Inspect Underwriting Queue"]
        UC5["Record Approval / Rejection Decision"]
        UC6["Trigger CI/CD Pipeline & Deploy"]
        UC7["Monitor System Health & Metrics"]
    end

    Applicant --> UC1
    Applicant --> UC3
    UC1 -.->|Include| UC2

    Underwriter --> UC4
    Underwriter --> UC5

    DevOps --> UC6
    DevOps --> UC7
```

---

### 3. Application Architecture Diagram
```mermaid
flowchart TD
    subgraph Client_Tier["Client Presentation Tier"]
        Browser["Responsive Web Browser / Mobile Device"]
        CurlClient["Automated Probes / REST API Consumers"]
    end

    subgraph Gateway_Tier["Reverse Proxy & Gateway"]
        Nginx["Nginx Reverse Proxy (Port 80)"]
    end

    subgraph App_Tier["Application Tier (Spring Boot / Embedded Tomcat Port 8080)"]
        WebCtrl["WebViewController (Thymeleaf / HTML5 UI)"]
        RestCtrl["ApplicationRestController (/api/*)"]
        ExHandler["GlobalExceptionHandler"]

        subgraph Service_Core["Business & Underwriting Core"]
            AppService["ApplicationService<br/>- Automated Rule Engine<br/>- Risk Tier Calculator<br/>- Duplicate Guard"]
        end

        subgraph Data_Access["Data Access Tier"]
            Repo["ParticipantApplicationRepository<br/>(Spring Data JPA)"]
        end
    end

    subgraph DB_Tier["Persistence Tier"]
        H2DB["H2 In-Memory / File Database<br/>(jdbc:h2:mem:microinsurancedb)"]
    end

    Browser -->|HTTP GET/POST| Nginx
    CurlClient -->|REST JSON| Nginx
    Nginx -->|Reverse Proxy| App_Tier
    WebCtrl --> AppService
    RestCtrl --> AppService
    AppService --> Repo
    Repo --> H2DB
```

---

### 4. Data Model (Entity-Relationship)
```mermaid
erDiagram
    PARTICIPANT_APPLICATIONS {
        BIGINT id PK
        VARCHAR reference_number UK "MIP-YYYY-XXXXX"
        VARCHAR full_name
        VARCHAR national_id UK "Unique Citizen ID"
        VARCHAR phone_number
        VARCHAR email
        DATE date_of_birth
        INT age
        VARCHAR gender
        DOUBLE annual_income "Max 60000"
        VARCHAR occupation
        VARCHAR policy_type "Enum: CROP_SHIELD, HEALTH_BASIC, ..."
        DOUBLE coverage_amount
        DOUBLE monthly_premium
        VARCHAR nominee_name
        VARCHAR nominee_relationship
        VARCHAR nominee_contact
        VARCHAR status "Enum: SUBMITTED, AUTO_APPROVED, UNDER_REVIEW, APPROVED, REJECTED"
        VARCHAR risk_tier "Enum: LOW_RISK, MODERATE_RISK, HIGH_RISK"
        VARCHAR underwriter_remarks
        TIMESTAMP submitted_at
        TIMESTAMP updated_at
    }
```

---

### 5. API Specification Matrix
| Method | Endpoint Path | Description | Request Payload / Params | Status Code |
| :--- | :--- | :--- | :--- | :---: |
| `POST` | `/api/applications` | Submit new participant enrollment | `EnrollmentRequestDto` (JSON) | `201 Created` |
| `GET` | `/api/applications` | List enrolled participants with filters | `status`, `policyType`, `search` | `200 OK` |
| `GET` | `/api/applications/{id}` | Retrieve application details by DB ID | Path variable `id` | `200 OK` |
| `GET` | `/api/applications/track/{ref}` | Public status tracking lookup | Path variable `ref` | `200 OK` |
| `POST` | `/api/applications/{id}/decision` | Record underwriter audit decision | `ApprovalDecisionDto` (JSON) | `200 OK` |
| `GET` | `/api/metrics` | Aggregate enrollment dashboard counts | None | `200 OK` |
| `GET` | `/api/health` | Automated readiness & liveness probe | None | `200 OK` |
| `GET` | `/actuator/health` | Spring Boot Actuator monitoring | None | `200 OK` |

---

### 6. Working Local Setup Guide
1. **Prerequisites:**
   * Java Development Kit (JDK) 21 LTS installed (`java -version`).
   * Git installed (`git --version`).
   * Google Chrome or MS Edge (for Selenium tests).
2. **Clone & Build:**
   ```bash
   cd c:\College\Projects\microinsurance-enrollment-platform
   .\mvnw.cmd clean package
   ```
3. **Run Application Locally:**
   ```bash
   java -jar target\microinsurance-enrollment-platform-1.0.0.jar
   ```
4. **Access Endpoints:**
   * Web Portal: `http://localhost:8080/`
   * H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:microinsurancedb`, user: `sa`, blank password)
   * Actuator Health: `http://localhost:8080/actuator/health`
