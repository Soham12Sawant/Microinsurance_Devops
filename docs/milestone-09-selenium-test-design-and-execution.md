# Milestone 9: Selenium Test Design and Local Execution
## Automated Microinsurance Enrollment Platform

---

### 1. Test Plan & Critical User Journeys
The platform defines 4 comprehensive end-to-end user journeys executed against the live application using Selenium WebDriver 4 in Headless Chrome:

| Journey ID | Journey Name | Persona | Test Steps | Expected Outcome |
| :--- | :--- | :--- | :--- | :--- |
| **UJ-01** | **Participant Registration & Auto-Approval** | Rural Farmer | 1. Navigate to `/`<br>2. Fill legal name, valid National ID, income ($22,000), Crop Shield policy, and nominee<br>3. Submit form | Immediate success banner displayed; reference number generated; status matches `AUTO_APPROVED`. |
| **UJ-02** | **Directory Search & Status Filtering** | Field Agent / Auditor | 1. Click 'Participant Directory' tab<br>2. Type `"Ramesh"` into search bar<br>3. Trigger search filter | Table filters asynchronously; row displays "Ramesh Kumar Patel" with Crop Shield and active status. |
| **UJ-03** | **Application Tracking & Certificate Generation** | Policy Beneficiary | 1. Click 'Status Tracking' tab<br>2. Enter `"MIP-2026-CRP01"`<br>3. Click 'Track Status' | Progression timeline renders all 4 steps; status badge shows `AUTO_APPROVED`; policy summary details match. |
| **UJ-04** | **Duplicate National ID Error Handling** | System Guard | 1. Attempt submitting registration with already enrolled National ID (`AID-9821453210`) | Error banner displayed with message: `"already exists in the platform"`. Prevents duplicate record. |

---

### 2. Failure Screenshot Capture Mechanism
To guarantee immediate diagnostic traceability during automated CI runs, the test suite implements an automatic screenshot capture hook:

```java
private void takeScreenshot(String testName) {
    if (driver instanceof TakesScreenshot takesScreenshot) {
        File src = takesScreenshot.getScreenshotAs(OutputType.FILE);
        File destDir = new File("target/screenshots");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File dest = new File(destDir, testName + "_" + System.currentTimeMillis() + ".png");
        try {
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot captured for failure: " + dest.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to write screenshot: " + e.getMessage());
        }
    }
}
```

Whenever an assertion or timeout occurs, WebDriver captures the exact DOM state and saves the timestamped PNG under `target/screenshots/` before re-throwing the exception.

---

### 3. Local Execution Through Maven
```bash
$ .\mvnw.cmd test -Dtest=MicroinsuranceSeleniumTest
```

#### Test Execution Report:
```
[INFO] Running com.microinsurance.platform.selenium.MicroinsuranceSeleniumTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 10.22 s -- in com.microinsurance.platform.selenium.MicroinsuranceSeleniumTest
[INFO] 
[INFO] Results:
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

* **Test Report XML Location:** `target/surefire-reports/TEST-com.microinsurance.platform.selenium.MicroinsuranceSeleniumTest.xml`
* **Test Class:** `src/test/java/com/microinsurance/platform/selenium/MicroinsuranceSeleniumTest.java`
