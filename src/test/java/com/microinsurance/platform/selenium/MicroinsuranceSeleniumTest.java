package com.microinsurance.platform.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MicroinsuranceSeleniumTest {

    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeAll
    public static void setUpDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");

        try {
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        } catch (Exception e) {
            System.err.println("Notice: Chrome WebDriver initialization skipped or failed: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp() {
        Assumptions.assumeTrue(driver != null, "ChromeDriver not initialized; skipping browser UI execution.");
        baseUrl = "http://localhost:" + port;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

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

    private void setInputValue(String elementId, String value) {
        WebElement element = driver.findElement(By.id(elementId));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", element, value);
    }

    @Test
    @Order(1)
    @DisplayName("Journey 1: Successful Participant Registration and Auto-Approval Feedback")
    public void testJourney1_SuccessfulRegistrationAndAutoApproval() {
        try {
            driver.get(baseUrl);

            // Verify Header
            WebElement brand = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("navbar-brand")));
            assertTrue(brand.getText().contains("MicroShield"));

            // Fill Registration Form
            setInputValue("inputFullName", "Laxman Gond");
            setInputValue("inputNationalId", "AID-SEL-9901");
            setInputValue("inputPhone", "+919876599901");
            setInputValue("inputEmail", "laxman.gond@forestagro.in");
            setInputValue("inputDob", "1995-05-15");
            setInputValue("inputIncome", "22000");

            Select occSelect = new Select(driver.findElement(By.id("selectOccupation")));
            occSelect.selectByVisibleText("Smallholder Farmer");

            Select policySelect = new Select(driver.findElement(By.id("selectPolicy")));
            policySelect.selectByValue("CROP_SHIELD");

            setInputValue("inputNomineeName", "Kamla Gond");
            Select relSelect = new Select(driver.findElement(By.id("selectNomineeRel")));
            relSelect.selectByVisibleText("Spouse");

            // Submit
            WebElement submitBtn = driver.findElement(By.id("btnSubmitEnrollment"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);

            // Wait for success alert
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("#enrollmentAlertContainer .alert-success")));
            assertNotNull(alert);
            assertTrue(alert.getText().contains("Enrollment Submitted Successfully"));
            assertTrue(alert.getText().contains("AUTO_APPROVED"));
        } catch (Throwable t) {
            takeScreenshot("testJourney1_failure");
            throw t;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Journey 2: Participant Directory Search and Filtering")
    public void testJourney2_ParticipantDirectoryListing() {
        try {
            driver.get(baseUrl);

            // Click Directory tab
            WebElement directoryTab = driver.findElement(By.id("tab-participants-btn"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", directoryTab);

            // Wait for table to populate
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("participantsTableBody")));

            // Filter by search
            setInputValue("searchParticipant", "Ramesh");
            WebElement searchBox = driver.findElement(By.id("searchParticipant"));
            ((JavascriptExecutor) driver).executeScript("filterParticipantsTable();");

            // Verify filtered results
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.id("participantsTableBody"), "Ramesh Kumar Patel"));
            assertTrue(driver.findElement(By.id("participantsTableBody")).getText().contains("Ramesh Kumar Patel"));
        } catch (Throwable t) {
            takeScreenshot("testJourney2_failure");
            throw t;
        }
    }

    @Test
    @Order(3)
    @DisplayName("Journey 3: Status Tracking by Application Reference Number")
    public void testJourney3_StatusTracking() {
        try {
            driver.get(baseUrl);

            // Click Tracking tab
            WebElement trackTab = driver.findElement(By.id("tab-tracking-btn"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", trackTab);

            // Enter seeded reference number
            setInputValue("inputTrackingRef", "MIP-2026-CRP01");

            WebElement trackBtn = driver.findElement(By.id("btnTrack"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", trackBtn);

            // Wait for async result text to appear
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.id("trackingResultContainer"), "Ramesh Kumar Patel"));

            WebElement resultContainer = driver.findElement(By.id("trackingResultContainer"));
            assertTrue(resultContainer.getText().contains("Ramesh Kumar Patel"));
            assertTrue(resultContainer.getText().contains("AUTO_APPROVED"));
            assertTrue(resultContainer.getText().toUpperCase().contains("ENROLLMENT PROGRESSION"));
        } catch (Throwable t) {
            takeScreenshot("testJourney3_failure");
            throw t;
        }
    }

    @Test
    @Order(4)
    @DisplayName("Journey 4: Duplicate National ID Prevention Error Feedback")
    public void testJourney4_DuplicateIdPrevention() {
        try {
            driver.get(baseUrl);

            // Submit using existing seeded national ID
            setInputValue("inputFullName", "Duplicate Attempt User");
            setInputValue("inputNationalId", "AID-9821453210"); // Already registered to Ramesh
            setInputValue("inputPhone", "+919876543299");
            setInputValue("inputDob", "1992-01-01");
            setInputValue("inputIncome", "25000");
            setInputValue("inputNomineeName", "Test Nominee");

            WebElement submitBtn = driver.findElement(By.id("btnSubmitEnrollment"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);

            // Wait for danger alert
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("#enrollmentAlertContainer .alert-danger")));
            assertNotNull(alert);
            assertTrue(alert.getText().contains("already exists in the platform"));
        } catch (Throwable t) {
            takeScreenshot("testJourney4_failure");
            throw t;
        }
    }
}
