# Milestone 10: Continuous Testing in Jenkins
## Automated Microinsurance Enrollment Platform

---

### 1. Integrating Selenium Suite in Jenkins CI
The Selenium WebDriver E2E test suite is integrated directly into Stage 3 of the `Jenkinsfile`:

```groovy
stage('3. Selenium E2E Test Suite') {
    when {
        expression { return params.RUN_SELENIUM_TESTS }
    }
    steps {
        echo "Executing automated Selenium WebDriver journeys in Headless Chrome..."
        sh 'mvn test -Dtest=*SeleniumTest -B'
    }
    post {
        always {
            junit testResults: '**/target/surefire-reports/*SeleniumTest*.xml', allowEmptyResults: true
            archiveArtifacts artifacts: 'target/screenshots/*.png', allowEmptyArchive: true
        }
        failure {
            echo "CRITICAL: E2E Regression detected. Aborting packaging and container deployment."
        }
    }
}
```

#### Key Quality Gates Enforced:
1. **Automated Test Report Generation:** Jenkins parses Surefire XML files and plots test trends across builds.
2. **Failure Artifact Archival:** Any captured failure PNGs are automatically preserved in Jenkins build artifacts.
3. **Pipeline Halting (Fail-Fast):** A test failure immediately terminates execution—preventing corrupted or broken builds from reaching the `Package Artifact` or `Docker Deploy` stages.

---

### 2. Deliberate Defect Injection & Failed Pipeline Demonstration

#### 2.1 Defect Introduction:
A regression defect was intentionally injected into the UI/assertion layer where the expected title for Journey 3 was altered to an invalid string:
```java
// Deliberately introduced defect:
assertTrue(resultContainer.getText().contains("NonExistentHeaderTitle"));
```

#### 2.2 Jenkins Failed Build Log Evidence:
```
[INFO] Running com.microinsurance.platform.selenium.MicroinsuranceSeleniumTest
Screenshot captured for failure: target/screenshots/testJourney3_failure_1792246755639.png
[ERROR] Tests run: 4, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 10.94 s <<< FAILURE!
[ERROR] com.microinsurance.platform.selenium.MicroinsuranceSeleniumTest.testJourney3_StatusTracking <<< FAILURE!
org.opentest4j.AssertionFailedError: expected: <true> but was: <false>
	at com.microinsurance.platform.selenium.MicroinsuranceSeleniumTest.testJourney3_StatusTracking(MicroinsuranceSeleniumTest.java:184)
[INFO] BUILD FAILURE
[Jenkins] Recording test results
[Jenkins] Test result: 1 failures, 3 passed out of 4 tests.
[Jenkins] Archiving artifacts: target/screenshots/testJourney3_failure_1792246755639.png
CRITICAL: E2E Regression detected. Aborting packaging and container deployment.
Finished: FAILURE
```

---

### 3. Defect Correction & Successful Rerun

#### 3.1 Defect Resolution:
The assertion was corrected to properly validate the uppercase progression header rendered by the responsive Bootstrap stylesheet:
```java
// Fixed assertion:
assertTrue(resultContainer.getText().toUpperCase().contains("ENROLLMENT PROGRESSION"));
```

#### 3.2 Git Resolution Commit:
```bash
git commit -am "fix(test): correct case sensitivity matching on progression timeline header"
```

#### 3.3 Successful Rerun Evidence:
```
[INFO] Running com.microinsurance.platform.selenium.MicroinsuranceSeleniumTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 10.22 s -- in com.microinsurance.platform.selenium.MicroinsuranceSeleniumTest
[INFO] BUILD SUCCESS
[Jenkins] Test result: 4 passed out of 4 tests (0 failures).
[Pipeline] Stage '4. Package Artifact': SUCCESS
[Pipeline] Stage '5. Docker Image Build & Tag': SUCCESS
[Pipeline] Stage '6. Continuous Deployment': SUCCESS
Finished: SUCCESS
```
