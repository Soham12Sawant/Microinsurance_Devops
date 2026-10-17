# Milestone 4: Git and GitHub Repository Initialization
## Automated Microinsurance Enrollment Platform

---

### 1. Repository Structure & Metadata
* **Repository Name:** `microinsurance-enrollment-platform`
* **Default Branch:** `main`
* **Active Branches:** `main`, `develop`, `feature/applicant-registration`, `feature/underwriting-approval`
* **Repository URL:** `https://github.com/microinsurance-devops/microinsurance-enrollment-platform.git` (simulated / upstream remote)

#### Project Directory Tree:
```
microinsurance-enrollment-platform/
├── .github/
│   └── ISSUE_TEMPLATE/
│       ├── bug_report.md
│       └── feature_request.md
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── ansible/
│   ├── inventory.ini
│   ├── playbook.yml
│   ├── rollback.yml
│   └── roles/
│       └── microinsurance/
│           └── tasks/
│               └── main.yml
├── docker/
│   └── nginx.conf
├── docs/
│   ├── milestone-01-problem-definition-and-scope.md
│   ├── milestone-02-agile-planning-and-devops-workflow.md
│   ├── milestone-03-requirements-architecture-and-tech-setup.md
│   ├── milestone-04-git-and-github-repository-initialization.md
│   ├── ...
├── src/
│   ├── main/
│   │   ├── java/com/microinsurance/platform/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── DataInitializer.java
│   │   │   └── MicroinsuranceApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           └── index.html
│   └── test/
│       └── java/com/microinsurance/platform/
│           ├── selenium/
│           │   └── MicroinsuranceSeleniumTest.java
│           └── unit/
│               └── ApplicationServiceTest.java
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

### 2. Branch Naming Policy & Rules
To maintain rigorous version control hygiene across multiple collaborating engineers, the following standard is enforced:

| Branch Type | Syntax Convention | Example | Purpose |
| :--- | :--- | :--- | :--- |
| **Production Baseline** | `main` | `main` | Production-ready, stable, tagged releases only (`v1.0.0`). Protected branch. |
| **Integration Baseline** | `develop` | `develop` | Integration branch for verified features before release staging. |
| **Feature Branches** | `feature/<ticket-id>-<slug>` | `feature/applicant-registration` | New user stories or UI/API capabilities branched off `develop`. |
| **Bugfix Branches** | `bugfix/<ticket-id>-<slug>` | `bugfix/issue-42-age-validation` | Non-emergency bug fixes merged back into `develop`. |
| **Hotfix Branches** | `hotfix/<version>-<slug>` | `hotfix/v1.0.1-db-timeout` | Emergency fixes applied directly to `main` and back-ported to `develop`. |
| **Release Branches** | `release/v<semantic-ver>` | `release/v1.0.0` | Release stabilization, final regression testing, and version bumping. |

#### Branch Protection Rules:
1. Direct pushes to `main` and `develop` are disallowed.
2. All feature branches must merge via Pull Requests (PR).
3. PR merges require at least 1 approving peer review.
4. Automated Jenkins CI build and Selenium tests must pass with status `GREEN`.
5. Commits must follow the Conventional Commits specification (`feat:`, `fix:`, `chore:`, `docs:`, `test:`, `ci:`).

---

### 3. Git Issue Templates
Standardized Markdown templates are maintained under `.github/ISSUE_TEMPLATE/`:
* `bug_report.md`: Enforces step-by-step reproduction, expected vs. actual behavior, environment specs, and Selenium failure logs.
* `feature_request.md`: Captures business problem, proposed solution, persona-based User Story (`As a... I want... So that...`), and Acceptance Criteria checkboxes.

---

### 4. Git Initialization Commands & Execution Evidence
```bash
git init
git checkout -b main
git add .
git commit -m "chore: initial project skeleton with Maven and Spring Boot"
git branch develop
```
