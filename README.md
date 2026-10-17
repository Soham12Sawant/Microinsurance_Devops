# MicroShield: Automated Microinsurance Enrollment Platform
> **Enterprise DevOps & Full-Lifecycle Software Engineering Capstone Project**  
> *A Production-Ready, Automated Microinsurance Enrollment & Underwriting Platform for Vulnerable and Informal Sector Beneficiaries.*

[![Java 21](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Build-Maven%203.9-blue.svg)](https://maven.apache.org/)
[![Selenium](https://img.shields.io/badge/Tests-Selenium%20WebDriver%204-red.svg)](https://www.selenium.dev/)
[![Docker](https://img.shields.io/badge/Container-Docker%20Multi--Stage-blue.svg)](https://www.docker.com/)
[![Jenkins](https://img.shields.io/badge/CI%2FCD-Jenkins%20Pipeline-informational.svg)](https://www.jenkins.io/)
[![Ansible](https://img.shields.io/badge/Config-Ansible%20Playbook-black.svg)](https://www.ansible.com/)
[![Release](https://img.shields.io/badge/Version-v1.0.0-success.svg)](https://github.com/)

---

## 📌 Executive Summary
The **Automated Microinsurance Enrollment Platform** bridges the financial inclusion gap for informal laborers, rural smallholders, and artisans. It replaces slow, error-prone manual paper underwriting (which takes 3-4 weeks) with an automated, sub-2-minute digital intake engine featuring:
* **Instant Auto-Approval Engine:** Automatically issues digital policy coverage for qualified low-risk applicants based on demographic and income scoring rules.
* **Underwriter Audit Desk:** Centralized queue for risk auditors to evaluate flagged edge cases and record approval/rejection decisions.
* **Real-time Directory & Search:** Filterable registry of participants with active policy coverage calculations.
* **Beneficiary Self-Service Tracking:** Real-time lifecycle timeline tracking by Reference ID with digital policy certificates.
* **Automated DevOps Quality Gates:** Continuous integration, headless Chrome Selenium journeys, Docker containerization, and idempotent Ansible server provisioning.

---

## 📑 14-Milestone Project Deliverables Directory

All comprehensive milestone documentation, test reports, and architecture diagrams are maintained under the [`docs/`](file:///C:/College/Projects/microinsurance-enrollment-platform/docs) folder:

| Milestone | Deliverable Topic | Documentation Link |
| :---: | :--- | :--- |
| **M-01** | Problem Definition, Stakeholders & 15-Task MVP Scope | [docs/milestone-01-problem-definition-and-scope.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-01-problem-definition-and-scope.md) |
| **M-02** | Agile Planning, User Stories, DoD & DevOps Workflow | [docs/milestone-02-agile-planning-and-devops-workflow.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-02-agile-planning-and-devops-workflow.md) |
| **M-03** | Requirements, Architecture, ER Data Model & API Specs | [docs/milestone-03-requirements-architecture-and-tech-setup.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-03-requirements-architecture-and-tech-setup.md) |
| **M-04** | Git Repository Initialization, Branch Policy & Templates | [docs/milestone-04-git-and-github-repository-initialization.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-04-git-and-github-repository-initialization.md) |
| **M-05** | Feature Development with Branching (PR #1 Merge) | [docs/milestone-05-feature-development-with-branching.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-05-feature-development-with-branching.md) |
| **M-06** | MVP Completion, Merge Conflict Resolution & v1.0.0 Tag | [docs/milestone-06-mvp-completion-and-git-collaboration.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-06-mvp-completion-and-git-collaboration.md) |
| **M-07** | Jenkins Installation & Continuous Integration Job | [docs/milestone-07-jenkins-installation-and-ci-job.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-07-jenkins-installation-and-ci-job.md) |
| **M-08** | Pipeline as Code (Jenkinsfile) & Target Deployment | [docs/milestone-08-pipeline-as-code-and-server-deployment.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-08-pipeline-as-code-and-server-deployment.md) |
| **M-09** | Selenium E2E Test Suite & Failure Screenshot Hook | [docs/milestone-09-selenium-test-design-and-execution.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-09-selenium-test-design-and-execution.md) |
| **M-10** | Continuous Testing in Jenkins & Defect Fix Rerun | [docs/milestone-10-continuous-testing-in-jenkins.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-10-continuous-testing-in-jenkins.md) |
| **M-11** | Multi-Stage Dockerfile & Container Lifecycle | [docs/milestone-11-docker-image-and-container-lifecycle.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-11-docker-image-and-container-lifecycle.md) |
| **M-12** | Jenkins-Docker End-to-End Continuous Deployment | [docs/milestone-12-jenkins-docker-continuous-deployment.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-12-jenkins-docker-continuous-deployment.md) |
| **M-13** | Configuration Management Script (Ansible Playbook) | [docs/milestone-13-configuration-management-ansible.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-13-configuration-management-ansible.md) |
| **M-14** | Automated Provisioning, Idempotency & Rollback | [docs/milestone-14-automated-provisioning-and-reliability.md](file:///C:/College/Projects/microinsurance-enrollment-platform/docs/milestone-14-automated-provisioning-and-reliability.md) |

---

## 🛠️ Technology Stack Matrix
* **Backend:** Java 21 LTS, Spring Boot 3.2.4 (Web, Data JPA, Validation, Actuator, Thymeleaf)
* **Frontend:** Responsive HTML5, Bootstrap 5.3, Bootstrap Icons, JavaScript Fetch API
* **Database:** H2 In-Memory & Persistent Engine (with web console enabled)
* **Build Tool:** Apache Maven 3.9 & Maven Wrapper (`./mvnw`, `mvnw.cmd`)
* **Testing:** JUnit 5, Spring Boot Test, Selenium WebDriver 4.18 (Headless Chrome)
* **Containerization:** Docker Multi-Stage (Maven Builder -> Eclipse Temurin JRE 21 Alpine)
* **Reverse Proxy:** Nginx Alpine (`docker/nginx.conf`)
* **CI/CD Automation:** Jenkins LTS Declarative Pipeline (`Jenkinsfile`)
* **Config Management:** Ansible Playbook & Inventory (`ansible/`)

---

## 🚀 Quick Start Guide

### 1. Build and Run Locally
```bash
# Clone and enter directory
cd c:\College\Projects\microinsurance-enrollment-platform

# Compile and package application
.\mvnw.cmd clean package

# Run executable JAR
java -jar target\microinsurance-enrollment-platform-1.0.0.jar
```

* **Portal UI:** `http://localhost:8080/`
* **H2 Console:** `http://localhost:8080/h2-console`
* **Health Endpoint:** `http://localhost:8080/api/health`

### 2. Execute Automated Test Suites
```bash
# Run JUnit 5 Unit Tests:
.\mvnw.cmd test

# Run Selenium Headless Chrome E2E Suite:
.\mvnw.cmd test -Dtest=MicroinsuranceSeleniumTest
```

### 3. Run with Docker Compose (App + Nginx + Jenkins)
```bash
docker compose up -d
```
* **Nginx Reverse Proxy:** `http://localhost/`
* **Application Direct:** `http://localhost:8080/`
* **Jenkins Controller:** `http://localhost:8089/`

### 4. Provision Node with Ansible
```bash
# Execute idempotent configuration playbook:
ansible-playbook -i ansible/inventory.ini ansible/playbook.yml

# Execute emergency rollback to previous release:
ansible-playbook -i ansible/inventory.ini ansible/rollback.yml
```
