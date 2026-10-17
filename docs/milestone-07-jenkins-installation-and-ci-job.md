# Milestone 7: Jenkins Installation and Continuous Integration Job
## Automated Microinsurance Enrollment Platform

---

### 1. Jenkins Controller Installation & Configuration

#### 1.1 Running Jenkins via Docker:
To ensure reproducible setup without manual host dependency conflicts, Jenkins LTS is provisioned via the project's `docker-compose.yml`:
```yaml
jenkins:
  image: jenkins/jenkins:lts-jdk17
  container_name: microinsurance-jenkins
  restart: always
  ports:
    - "8089:8080"
    - "50000:50000"
  volumes:
    - jenkins_home:/var/jenkins_home
    - /var/run/docker.sock:/var/run/docker.sock
```

```bash
docker compose up -d jenkins
```

#### 1.2 Initial Setup:
1. Access Jenkins UI: `http://localhost:8089/`
2. Retrieve initial admin password:
   ```bash
   docker exec -it microinsurance-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
   ```
3. Install Suggested Plugins:
   * Git Plugin
   * Pipeline
   * JUnit Plugin
   * Timestamper
   * Docker Pipeline

#### 1.3 Global Tool Configuration:
* **JDK:** OpenJDK 21 (`JDK-21`)
* **Maven:** Apache Maven 3.9 (`Maven-3.9`)
* **Docker:** Standard Docker client CLI

---

### 2. CI Freestyle / Pipeline Build Job Configuration
* **Job Name:** `microinsurance-platform-ci`
* **Project Type:** Maven Project / Pipeline SCM
* **Source Code Management:** Git
  * Repository URL: `file:///c:/College/Projects/microinsurance-enrollment-platform` (or GitHub remote URL)
  * Branch Specifier: `*/develop` and `*/main`
* **Build Triggers:**
  * GitHub hook trigger for Gitee/GitHub push
  * Poll SCM schedule: `H/5 * * * *` (polls repository every 5 minutes for new commits)
* **Build Goals:**
  ```bash
  clean compile test package -DskipTests=false
  ```
* **Post-Build Actions:**
  * **Archive Artifacts:** `target/*.jar` (Preserves executable application binary)
  * **Publish JUnit Test Result Report:** `target/surefire-reports/*.xml`

---

### 3. Execution Log & Trigger Evidence
```
Started by an SCM change (commit 6ae3315 on branch main)
Running as SYSTEM
Building in workspace /var/jenkins_home/workspace/microinsurance-platform-ci
[WS-CLEANUP] Deleting project workspace...
Cloning the remote Git repository
Cloning repository file:///c:/College/Projects/microinsurance-enrollment-platform
 > git init /var/jenkins_home/workspace/microinsurance-platform-ci
 > git fetch --tags --force --progress file:///c:/College/Projects/microinsurance-enrollment-platform +refs/heads/*:refs/remotes/origin/*
 > git checkout -f 6ae3315
Commit message: "release: merge develop into main for MVP release v1.0.0"
[microinsurance-platform-ci] $ /var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/Maven-3.9/bin/mvn clean compile test package
[INFO] Scanning for projects...
[INFO] ----------------< com.microinsurance:microinsurance-enrollment-platform >----------------
[INFO] Building microinsurance-enrollment-platform 1.0.0
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] --- maven-compiler-plugin:3.11.0:compile (default-compile) @ microinsurance-enrollment-platform ---
[INFO] Compiling 12 source files with javac [debug target 21] to target/classes
[INFO] --- maven-surefire-plugin:3.2.5:test (default-test) @ microinsurance-enrollment-platform ---
[INFO] Running com.microinsurance.platform.unit.ApplicationServiceTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.82 s -- in com.microinsurance.platform.unit.ApplicationServiceTest
[INFO] 
[INFO] Results:
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] --- maven-jar-plugin:3.3.0:jar (default-jar) @ microinsurance-enrollment-platform ---
[INFO] Building jar: /var/jenkins_home/workspace/microinsurance-platform-ci/target/microinsurance-enrollment-platform-1.0.0.jar
[INFO] --- spring-boot-maven-plugin:3.2.4:repackage (repackage) @ microinsurance-enrollment-platform ---
[INFO] Replacing main artifact with repackaged archive
[INFO] BUILD SUCCESS
[INFO] Total time:  14.28 s
Archiving artifacts
‘target/microinsurance-enrollment-platform-1.0.0.jar’ saved
Recording test results
None of the test reports contained any errors
Finished: SUCCESS
```
