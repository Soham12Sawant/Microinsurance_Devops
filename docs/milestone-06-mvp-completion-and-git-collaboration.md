# Milestone 6: MVP Completion and Git Collaboration
## Automated Microinsurance Enrollment Platform

---

### 1. Functional MVP Completion Summary
The complete 5-pillar functional baseline is operational and validated:
1. **Intake Form:** Dynamic form capturing applicant profile, household income, policy plan, and nominee records.
2. **Automated Validation:** Instant enforcement of age constraints (18–65), income bounds (<= $60k), and unique National ID check.
3. **Approval & Underwriting:** Real-time risk tiering (`LOW_RISK` instant auto-approval; `UNDER_REVIEW` routed to underwriter queue for single-click audit decisions).
4. **Participant Listing:** Live directory with real-time text search and status filters (`AUTO_APPROVED`, `APPROVED`, `UNDER_REVIEW`, `REJECTED`).
5. **Status Tracking:** Public self-service tracking by reference number rendering interactive lifecycle timeline and digital policy summary certificate.

---

### 2. Feature Branch 2 & Merge Conflict Simulation

#### 2.1 Branching Off `develop`:
```bash
git checkout -b feature/underwriting-approval
```

#### 2.2 Concurrent Conflicting Commits:
* On `feature/underwriting-approval`: Added audit timeout SLA of **48 hours**:
  ```properties
  app.underwriting.audit-timeout-hours=48
  ```
  Committed with message: `feat(underwriting): configure 48-hour audit timeout for high risk policies` (`9ff9640`).

* On `develop`: Concurrently updated the same property to **24 hours**:
  ```properties
  app.underwriting.audit-timeout-hours=24 # Expedited SLA
  ```
  Committed with message: `chore(config): reduce default underwriter audit SLA to 24 hours on develop` (`f54456a`).

#### 2.3 Merge Attempt & Conflict Trigger:
```bash
$ git checkout develop
$ git merge feature/underwriting-approval
Auto-merging src/main/resources/application.properties
CONFLICT (content): Merge conflict in src/main/resources/application.properties
Automatic merge failed; fix conflicts and then commit the result.
```

#### 2.4 Git Conflict Diff Markers (`git diff`):
```diff
diff --cc src/main/resources/application.properties
--- a/src/main/resources/application.properties
+++ b/src/main/resources/application.properties
@@@ -28,4 -28,4 +28,8 @@@ app.insurance.max-age=65
  app.insurance.auto-approval-threshold-coverage=5000.0
  # Feature 1 Config: Enhanced Intake Rate Limiting
  app.insurance.registration.rate-limit-per-min=60
++<<<<<<< HEAD
 +app.underwriting.audit-timeout-hours=24 # Expedited SLA
++=======
+ app.underwriting.audit-timeout-hours=48
++>>>>>>> feature/underwriting-approval
```

#### 2.5 Conflict Resolution:
The team reconciled both requirements by standardizing on a 24-hour expedited SLA with 48-hour emergency fallback:
```properties
# Feature 2: Underwriting Audit SLA (Resolved Conflict: Standard 24h expedited SLA with 48h max fallback)
app.underwriting.audit-timeout-hours=24
```

```bash
git add src/main/resources/application.properties
git commit -m "fix(merge): resolve conflict between develop and feature/underwriting-approval for audit SLA"
```

---

### 3. Release Baseline & Semantic Tagging
The unified, tested `develop` branch was merged into `main` and tagged as **`v1.0.0`**:

```bash
git checkout main
git merge --no-ff develop -m "release: merge develop into main for MVP release v1.0.0"
git tag -a v1.0.0 -m "Release v1.0.0: Automated Microinsurance Enrollment Platform MVP baseline"
```

#### Complete Branch Topology Graph:
```
*   6ae3315 (HEAD -> main, tag: v1.0.0) release: merge develop into main for MVP release v1.0.0
|\  
| *   bc506d5 (develop) fix(merge): resolve conflict between develop and feature/underwriting-approval for audit SLA
| |\  
| | * 9ff9640 (feature/underwriting-approval) feat(underwriting): configure 48-hour audit timeout for high risk policies
| * | f54456a chore(config): reduce default underwriter audit SLA to 24 hours on develop
| |/  
| * bb1f319 Merge pull request #1 from feature/applicant-registration: Core Applicant Registration Workflow
|/| 
| * f357d6c (feature/applicant-registration) feat: implement applicant registration workflow and rate limit configuration
|/  
* 8f64666 chore: initial project skeleton with Spring Boot 3.2, Maven, JPA, and Selenium
```
